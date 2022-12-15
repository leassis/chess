package com.lassis.sensysgatso.chess;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.ChessStatus;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Placement;
import com.lassis.sensysgatso.chess.model.Position;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class ChessGame {
    public static final int SIZE = 8;

    private final Board board = new Board(SIZE, SIZE);
    private final List<Piece> deletedPieces = new ArrayList<>();
    private final Map<Color, King> colorKings;
    private final Map<Color, ChessStatus> colorStatuses;

    private Color currentTurn = Color.BLACK;

    public ChessGame() {
        this.colorKings = Collections.unmodifiableMap(placePieceFirstRow(c -> new King(c, board), board.min().column() + 4));
        this.colorStatuses = calculateStatus();
        placePieces();
    }

    private void placePieces() {
        placeAndMirrorPieceFirstRow(color -> new Rook(color, board), board.min().column());
        placeAndMirrorPieceFirstRow(color -> new Knight(color, board), board.min().column() + 1);
        placeAndMirrorPieceFirstRow(color -> new Bishop(color, board), board.min().column() + 2);

        placePieceFirstRow(color -> new Queen(color, board), board.min().column() + 3);

        placePawns();
    }

    private <T extends Piece> Map<Color, T> placePieceFirstRow(Function<Color, T> transform, int column) {
        Position max = board.max();

        Map<Color, T> result = new HashMap<>();
        for (Color color : Color.values()) {
            int row = color.placement() == Placement.NORTH ? board.min().row() : max.row();
            T piece = transform.apply(color);
            board.place(piece, new Position(row, column));
            result.put(color, piece);
        }
        return result;
    }

    private void placeAndMirrorPieceFirstRow(Function<Color, Piece> transform, int column) {
        placePieceFirstRow(transform, column);
        placePieceFirstRow(transform, board.max().column() - column);
    }

    private void placePawns() {
        for (Color color : Color.values()) {
            int row = color.placement() == Placement.NORTH ? board.min().row() + 1 : board.max().row() - 1;
            for (int col = 0; col < board.columns(); col++) {
                board.place(new Pawn(color, board), new Position(row, col));
            }
        }
    }

    public Set<Position> allowedMoves(Position position) {
        return board.at(position).map(Piece::allowedMoves)
                .orElse(Collections.emptySet());
    }

    public ChessGameStatus getStatus(){
        return ChessGameStatus.builder()
                .blackStatus(colorStatuses.get(Color.BLACK))
                .whiteStatus(colorStatuses.get(Color.WHITE))
                .turn(currentTurn)
                .build();
    }

    public synchronized ChessGameStatus moveTo(Position origin, Position destination) {
        ChessGameStatus.ChessGameStatusBuilder statusBuilder = ChessGameStatus.builder()
                .blackStatus(colorStatuses.get(Color.BLACK))
                .whiteStatus(colorStatuses.get(Color.WHITE))
                .turn(currentTurn);

        Optional<Piece> oPiece = board.at(origin);
        if (oPiece.isEmpty()) {
            log.warn("there is no piece at origin position {}", origin);
            return statusBuilder.build();
        }

        boolean wrongPlayer = oPiece.map(Piece::getColor).filter(c -> Objects.equals(c, currentTurn)).isEmpty();
        if (wrongPlayer) {
            log.warn("it is {} turn", currentTurn);
            return statusBuilder.build();
        }

        Set<Position> positions = oPiece.map(Piece::allowedMoves)
                .orElse(Collections.emptySet());
        if (!positions.contains(destination)) {
            log.warn("piece {} is not allowed to move to {}", oPiece.get(), positions);
            return statusBuilder.build();
        }

        Optional<Piece> deleted = board.remove(origin).flatMap(p -> board.place(p, destination));
        deleted.ifPresent(deletedPieces::add);

        currentTurn = (currentTurn == Color.BLACK ? Color.WHITE : Color.BLACK);

        colorStatuses.putAll(calculateStatus());

        return statusBuilder
                .blackStatus(colorStatuses.get(Color.BLACK))
                .whiteStatus(colorStatuses.get(Color.WHITE))
                .turn(currentTurn)
                .deleted(deleted.orElse(null))
                .build();
    }

    private Map<Color, ChessStatus> calculateStatus() {
        Map<Color, ChessStatus> result = new HashMap<>();
        for (Color color : Color.values()) {

            Color other = getOtherColor(color);

            Map<Color, List<Piece>> pieces = board.pieces();
            Set<Position> opponentPossibleMoves = pieces.get(other).stream()
                    .map(Piece::allowedMoves)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toSet());

            ChessStatus chessStatus = ChessStatus.NORMAL;
            King king = colorKings.get(color);
            if (opponentPossibleMoves.contains(king.getPosition())) {
                chessStatus = ChessStatus.CHECK;

                Set<Position> kingPossible = king.allowedMoves();
                int oldSize = kingPossible.size();
                kingPossible.retainAll(opponentPossibleMoves);
                if (kingPossible.size() == oldSize) {
                    chessStatus = ChessStatus.CHECKMATE;
                }
            }
            result.put(color, chessStatus);
        }
        return result;
    }

    private static Color getOtherColor(Color color) {
        return color == Color.BLACK ? Color.WHITE : Color.BLACK;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = board.min().row(); row <= board.max().row(); row++) {
            List<String> line = new ArrayList<>();
            for (int col = board.min().column(); col <= board.max().column(); col++) {
                line.add(fixSize(board.at(row, col).map(v -> v.getColor().toString().charAt(0) + ":" + v.getClass().getSimpleName()).orElse("")));
            }
            sb.append(line.stream().collect(Collectors.joining("|", "|", "|")));
            sb.append("\n");

        }
        sb.append("\n\n")
                .append("black: ").append(colorStatuses.get(Color.BLACK))
                .append("\nwhite: ").append(colorStatuses.get(Color.WHITE));
        return sb.toString();
    }

    private String fixSize(String value) {
        String v = ObjectUtils.isEmpty(value) ? "" : value;
        return value + " ".repeat(Math.max(0, 8 - v.length()));
    }
}
