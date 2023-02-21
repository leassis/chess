package com.lassis.sensysgatso.chess.game;

import com.lassis.sensysgatso.chess.exception.EmptySquareException;
import com.lassis.sensysgatso.chess.exception.GameOverException;
import com.lassis.sensysgatso.chess.exception.InvalidMoveException;
import com.lassis.sensysgatso.chess.exception.WrongPlayerException;
import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.ChessGameStatus;
import com.lassis.sensysgatso.chess.model.ChessStatus;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Placement;
import com.lassis.sensysgatso.chess.model.Point;
import com.lassis.sensysgatso.chess.model.Square;
import com.lassis.sensysgatso.chess.model.pieces.Bishop;
import com.lassis.sensysgatso.chess.model.pieces.King;
import com.lassis.sensysgatso.chess.model.pieces.Knight;
import com.lassis.sensysgatso.chess.model.pieces.Pawn;
import com.lassis.sensysgatso.chess.model.pieces.Queen;
import com.lassis.sensysgatso.chess.model.pieces.Rook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lassis.sensysgatso.chess.model.ChessStatus.CHECK;
import static com.lassis.sensysgatso.chess.model.ChessStatus.CHECKMATE;
import static com.lassis.sensysgatso.chess.model.ChessStatus.NORMAL;
import static com.lassis.sensysgatso.chess.model.Color.BLACK;
import static com.lassis.sensysgatso.chess.model.Color.WHITE;

/**
 * Chess game, contains the basic operations to the game and the rules to define Check or CheckMate
 */
@Slf4j
public class ChessGame {
    public static final int SIZE_8 = 8;

    private final Board board;
    private final List<Piece> deletedPieces = new ArrayList<>();
    private final Map<Color, Square> colorKings;
    private final Map<Color, ChessStatus> colorStatuses;
    private final ReentrantLock lock = new ReentrantLock();

    private Color currentTurn = WHITE;

    public ChessGame() {
        this(new Board(SIZE_8, SIZE_8), true);
    }

    ChessGame(Board board, boolean placePieces) {
        this.board = board;

        // king is always at the same places
        this.colorKings = Collections.unmodifiableMap(placePieceFirstRow(King::new, board.min().column() + 4));
        if (placePieces) {
            placePieces();
        }
        this.colorStatuses = calculateStatus();
        logBoard();
    }

    private void placePieces() {
        placeAndMirrorPieceFirstRow(Rook::new, board.min().column());
        placeAndMirrorPieceFirstRow(Knight::new, board.min().column() + 1);
        placeAndMirrorPieceFirstRow(Bishop::new, board.min().column() + 2);

        placePieceFirstRow(Queen::new, board.min().column() + 3);

        placePawns();
    }

    private <T extends Piece> Map<Color, Square> placePieceFirstRow(Function<Color, T> transform, int column) {
        Point max = board.max();

        Map<Color, Square> result = new EnumMap<>(Color.class);
        for (Color color : Color.values()) {
            int row = color.placement() == Placement.NORTH ? board.min().row() : max.row();
            Square piece = board.place(transform.apply(color), new Point(row, column));
            result.put(color, piece);
        }
        return result;
    }

    private <T extends Piece> void placeAndMirrorPieceFirstRow(Function<Color, T> transform, int column) {
        placePieceFirstRow(transform, column);
        placePieceFirstRow(transform, board.max().column() - column);
    }

    private void placePawns() {
        for (Color color : Color.values()) {
            int row = color.placement() == Placement.NORTH ? board.min().row() + 1 : board.max().row() - 1;
            for (int col = 0; col < board.columns(); col++) {
                board.place(new Pawn(color), new Point(row, col));
            }
        }
    }

    /**
     * retrieve squares from the board
     *
     * @return set of non empty squares
     */
    public Set<Square> notEmptySquares() {
        lock.lock();
        try {
            return board.nonEmptySquares().values().stream()
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
        } finally {
            lock.unlock();
        }
    }

    /**
     * get a piece from a given point
     *
     * @param point where the piece sit
     * @return optional with piece, optional empty if piece is not found
     */
    public Optional<Square> at(Point point) {
        lock.lock();
        try {
            return board.at(point);
        } finally {
            lock.unlock();
        }
    }

    /**
     * list all allowed moves from a certain point based on the rules of piece
     *
     * @param point where piece sit
     * @return set of point where a piece can go
     */
    public Set<Point> allowedMoves(Point point) {
        lock.lock();
        try {
            Optional<Square> at = board.at(point);

            at.ifPresent(p -> log.debug("calculating allowed moves to {}", p));

            return at
                    .filter(sq -> sq.piece().getColor() == currentTurn)
                    .map(Square::allowedMoves)
                    .orElse(Collections.emptySet());
        } finally {
            lock.unlock();
        }
    }

    /**
     * provide a game status. Such as checkmate, user turn
     *
     * @return status of the game
     */
    public ChessGameStatus getStatus() {
        lock.lock();
        try {
            logBoard();

            return ChessGameStatus.builder()
                                  .blackStatus(colorStatuses.get(BLACK))
                                  .whiteStatus(colorStatuses.get(WHITE))
                                  .turn(currentTurn)
                                  .deleted(deletedPieces)
                                  .build();
        } finally {
            lock.unlock();
        }
    }

    /**
     * move a piece from origin to destination if a piece is found on origin, it's a player turn and the destination
     * is allowed for the piece. If a piece is already in the destination it will be removed and marked as delete.
     *
     * @param origin      point where the piece sit
     * @param destination destination of the piece
     * @return game status after movement.
     */
    public ChessGameStatus moveTo(Point origin, Point destination) {
        lock.lock();
        try {
            if (isGameOver()) {
                log.warn("the game is already finished");
                throw new GameOverException();
            }

            Optional<Square> oSquare = board.at(origin);
            if (oSquare.isEmpty()) {
                log.warn("there is no piece at origin position {}", origin);
                throw new EmptySquareException();
            }
            oSquare.ifPresent(p -> log.debug("moving {} from {} -> {}", p, origin, destination));

            boolean wrongPlayer = oSquare.map(Square::piece).map(Piece::getColor).filter(c -> Objects.equals(c, currentTurn)).isEmpty();
            if (wrongPlayer) {
                log.warn("it is {} turn", currentTurn);
                throw new WrongPlayerException();
            }

            Set<Point> points = oSquare.map(Square::allowedMoves)
                                       .orElse(Collections.emptySet());
            if (!points.contains(destination)) {
                log.warn("piece {} is not allowed to move to {}", oSquare.get(), points);
                throw new InvalidMoveException();
            }

            // remove destination and add to deleted if not empty
            board.remove(destination).ifPresent(deletedPieces::add);

            // remove from origin and place on destination
            board.remove(origin).ifPresent(p -> board.place(p, destination));

            // next turn
            currentTurn = (currentTurn == BLACK ? WHITE : BLACK);

            // new status
            colorStatuses.putAll(calculateStatus());

            logBoard();
            return getStatus();
        } finally {
            lock.unlock();
        }
    }

    private Map<Color, ChessStatus> calculateStatus() {
        Map<Color, ChessStatus> result = new EnumMap<>(Color.class);
        Map<Color, Set<Square>> pieces = board.nonEmptySquares();
        for (Color color : Color.values()) {
            Color other = getOtherColor(color);
            Set<Point> opponentPossibleMoves = pieces.getOrDefault(other, Collections.emptySet()).stream()
                                                     .map(Square::allowedMoves)
                                                     .flatMap(Collection::stream)
                                                     .collect(Collectors.toSet());

            ChessStatus chessStatus = NORMAL;
            Square king = colorKings.get(color);
            if (opponentPossibleMoves.contains(king.point())) {
                chessStatus = currentTurn == king.piece().getColor() ? CHECK : CHECKMATE;

                if (chessStatus == CHECK) {
                    Set<Point> kingPossible = king.allowedMoves();
                    int oldSize = kingPossible.size();
                    kingPossible.retainAll(opponentPossibleMoves);
                    if (kingPossible.size() == oldSize) {
                        chessStatus = CHECKMATE;
                    }
                }
            }
            result.put(color, chessStatus);
        }
        return result;
    }

    private static Color getOtherColor(Color color) {
        return color == BLACK ? WHITE : BLACK;
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            StringBuilder sb = new StringBuilder();
            for (int row = board.min().row(); row <= board.max().row(); row++) {
                List<String> line = new ArrayList<>();
                for (int col = board.min().column(); col <= board.max().column(); col++) {
                    line.add(fixSize(board.at(row, col).map(Square::piece).map(v -> v.getColor().toString().charAt(0) + ":" + v.name()).orElse("")));
                }
                sb.append(line.stream().collect(Collectors.joining("|", "|", "|")));
                sb.append("\n");

            }
            sb.append("\n\n")
              .append("black: ").append(colorStatuses.get(BLACK))
              .append("\nwhite: ").append(colorStatuses.get(WHITE));
            return sb.toString();
        } finally {
            lock.unlock();
        }
    }

    private String fixSize(String value) {
        String v = ObjectUtils.isEmpty(value) ? "" : value;
        return value + " ".repeat(Math.max(0, 8 - v.length()));
    }

    private boolean isGameOver() {
        ChessGameStatus status = getStatus();
        return status.blackStatus() == CHECKMATE || status.whiteStatus() == CHECKMATE;
    }

    private void logBoard() {
        log.debug("showing board: \n{}", this);
    }
}
