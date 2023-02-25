package com.lassis.sensysgatso.chess.game;

import com.lassis.sensysgatso.chess.exception.EmptySquareException;
import com.lassis.sensysgatso.chess.exception.GameOverException;
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

    private final List<Piece> deletedPieces = new ArrayList<>();
    private final ReentrantLock lock = new ReentrantLock();

    private final Board board;
    private final Map<Color, ChessStatus> colorStatuses;

    private Color currentTurn = WHITE;

    public ChessGame() {
        this(new Board(SIZE_8, SIZE_8), true);
    }

    ChessGame(Board board, boolean placePieces) {
        this.board = board;
        placePieceFirstRow(King::new, board.min().column() + 4);

        if (placePieces) {
            placePieces();
        }

        this.colorStatuses = calculateStatus();
        logBoard();
    }

    private void placePieces() {
        placePieceFirstRow(Queen::new, board.min().column() + 3);
        placeAndMirrorPieceFirstRow(Rook::new, board.min().column());
        placeAndMirrorPieceFirstRow(Knight::new, board.min().column() + 1);
        placeAndMirrorPieceFirstRow(Bishop::new, board.min().column() + 2);

        placePawns();
    }

    private <T extends Piece> void placePieceFirstRow(Function<Color, T> transform, int column) {
        Point max = board.max();

        for (Color color : Color.values()) {
            int row = color.placement() == Placement.NORTH ? board.min().row() : max.row();
            T piece = transform.apply(color);
            Point point = new Point(row, column);
            board.place(piece, point);
        }
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
     * @return set of not empty squares
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
            Optional<Square> square = board.at(point);

            log.debug("calculating allowed moves to {}", square);

            return square.flatMap(Square::piece).filter(p -> p.color() == currentTurn).isPresent()
                    ? square.map(Square::allowedMoves).orElseGet(Collections::emptySet)
                    : Collections.emptySet();

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

            Optional<Square> square = board.at(origin);
            Optional<Piece> oPiece = square.flatMap(Square::piece);
            if (oPiece.isEmpty()) {
                log.warn("there is no piece at origin position {}", origin);
                throw new EmptySquareException();
            }

            boolean wrongPlayer = oPiece.map(Piece::color).filter(c -> Objects.equals(c, currentTurn)).isEmpty();
            if (wrongPlayer) {
                log.warn("it is {} turn", currentTurn);
                throw new WrongPlayerException();
            }

            board.moveTo(origin, destination).ifPresent(deletedPieces::add);

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

            Square kingSquare = pieces.getOrDefault(color, Collections.emptySet())
                                      .stream()
                                      .filter(p -> p.piece().isPresent() && p.piece().get() instanceof King)
                                      .findFirst()
                                      .orElseThrow();

            Set<Point> opponentPossibleMoves = pieces.getOrDefault(other, Collections.emptySet()).stream()
                                                     .map(Square::allowedMoves)
                                                     .flatMap(Collection::stream)
                                                     .collect(Collectors.toSet());

            ChessStatus chessStatus = NORMAL;

            if (opponentPossibleMoves.contains(kingSquare.point())) {
                Piece kingPiece = kingSquare.piece().orElseThrow();
                chessStatus = currentTurn == kingPiece.color() ? CHECK : CHECKMATE;

                if (chessStatus == CHECK) {
                    Set<Point> kingPossible = kingSquare.allowedMoves();
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
            return board.toString() +
                    "\n\n" +
                    "black: " + colorStatuses.get(BLACK) + "\twhite: " + colorStatuses.get(WHITE);
        } finally {
            lock.unlock();
        }
    }

    private boolean isGameOver() {
        ChessGameStatus status = getStatus();
        return status.blackStatus() == CHECKMATE || status.whiteStatus() == CHECKMATE;
    }

    private void logBoard() {
        log.debug("showing board: \n{}", this);
    }

}
