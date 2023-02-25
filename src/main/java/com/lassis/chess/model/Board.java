package com.lassis.chess.model;

import com.lassis.chess.exception.InvalidMoveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class Board {
    public static final Point MIN_POINT = new Point(0, 0);

    private final Point maxPoint;
    private final int rows;
    private final int columns;
    private final Square[][] squares;

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.squares = initSquares();
        this.maxPoint = new Point(rows - 1, columns - 1);
    }

    public Map<Color, Set<Square>> nonEmptySquares() {
        Map<Color, Set<Square>> result = new EnumMap<>(Color.class);
        for (Square[] row : squares) {
            for (Square square : row) {
                square.piece()
                      .ifPresent(piece -> result.computeIfAbsent(piece.color(), k -> new HashSet<>()).add(square));
            }
        }
        return result;
    }

    public Optional<Square> at(Point point) {
        return at(point.row(), point.column());
    }

    public Optional<Square> at(int row, int column) {
        return isInBounds(row, column)
                ? Optional.of(squares[row][column])
                : Optional.empty();
    }


    public Set<Point> allowedMoves(Point point) {
        return allowedMoves(point.row(), point.column());
    }

    public Set<Point> allowedMoves(int row, int column) {
        return at(row, column).map(Square::allowedMoves).orElseGet(Collections::emptySet);
    }

    public Square place(Piece piece, Point point) {
        if (!isInBounds(point)) {
            throw new IllegalStateException("invalid position");
        }

        var square = squares[point.row()][point.column()];
        if (square.piece().isPresent()) {
            throw new IllegalStateException("position is not empty");
        }

        square.piece(piece);
        return square;
    }

    public Optional<Piece> moveTo(Point origin, Point destination) {
        Optional<Square> source = at(origin);
        Optional<Piece> oPiece = source.flatMap(Square::piece);

        Optional<Piece> oDeleted = at(destination).flatMap(Square::piece);

        log.debug("moving {} from {} -> {}", oPiece, origin, destination);
        Set<Point> allowedMoves = source.map(Square::allowedMoves).orElseGet(Collections::emptySet);

        if (oPiece.isPresent() && allowedMoves.contains(destination)) {
            squares[origin.row()][origin.column()].piece(null);
            squares[destination.row()][destination.column()].piece(oPiece.get());
        } else {
            log.warn("piece {} is not allowed to move to {}", oPiece, destination);
            throw new InvalidMoveException();
        }

        return oDeleted;
    }

    public boolean isInBounds(Point point) {
        return isInBounds(point.row(), point.column());
    }

    public boolean isInBounds(int row, int column) {
        return row >= min().row() && row <= max().row()
                && column >= min().column() && column <= max().column();
    }

    public Point min() {
        return MIN_POINT;
    }

    public Point max() {
        return maxPoint;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    private Square[][] initSquares() {
        var result = new Square[rows][columns];
        for (int row = 0; row < result.length; row++) {
            for (int column = 0; column < result[row].length; column++) {
                result[row][column] = new Square(this, new Point(row, column));
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = min().row(); row <= max().row(); row++) {
            List<String> line = new ArrayList<>();
            for (int col = min().column(); col <= max().column(); col++) {
                line.add(fixedLength(at(row, col).flatMap(Square::piece).map(v -> v.color().toString().charAt(0) + ":" + v.name()).orElse("")));
            }
            sb.append(line.stream().collect(Collectors.joining("|", "|", "|")));
            sb.append("\n");

        }
        return sb.toString();
    }

    private String fixedLength(String value) {
        String v = ObjectUtils.isEmpty(value) ? "" : value;
        return value + " ".repeat(Math.max(0, 8 - v.length()));
    }

}
