package com.lassis.sensysgatso.chess.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class Board {
    public static final Point MIN_POINT = new Point(0, 0);

    private final Point maxPoint;
    private final int rows;
    private final int columns;
    private final Piece[][] pieces;

    public Board(int rows, int columns) {
        this.pieces = new Piece[rows][columns];
        this.rows = rows;
        this.columns = columns;
        this.maxPoint = new Point(rows - 1, columns - 1);
    }

    public Map<Color, Set<Square>> nonEmptySquares() {
        Map<Color, Set<Square>> result = new HashMap<>();
        for (int row = 0; row < pieces.length; row++) {
            for (int column = 0; column < pieces[row].length; column++) {
                Piece piece = pieces[row][column];
                if (Objects.nonNull(piece)) {
                    result.computeIfAbsent(piece.getColor(), k -> new HashSet<>())
                            .add(new Square(piece, new Point(row, column)));
                }
            }
        }
        return result;
    }

    /**
     * get a piece from a certain spot
     *
     * @param point coordinate to check
     * @return an Optional piece if found, empty otherwise
     */
    public Optional<Piece> at(Point point) {
        return at(point.row(), point.column());
    }

    public Optional<Piece> at(int row, int column) {
        if (!isInBounds(row, column)) {
            return Optional.empty();
        }

        return Optional.ofNullable(pieces[row][column]);
    }

    public void place(Piece piece, Point point) {
        if (!isInBounds(point)) {
            throw new IllegalStateException("invalid position");
        }

        if (Objects.nonNull(pieces[point.row()][point.column()])) {
            throw new IllegalStateException("position is not empty");
        }

        piece.at(point);

        pieces[point.row()][point.column()] = piece;
    }

    public Optional<Piece> remove(Point point) {
        Optional<Piece> deleted = at(point);
        pieces[point.row()][point.column()] = null;

        deleted.ifPresent(p -> p.at(null));

        return deleted;
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

}

