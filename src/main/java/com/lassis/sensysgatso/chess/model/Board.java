package com.lassis.sensysgatso.chess.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Board {
    public static final Position MIN_POSITION = new Position(0, 0);

    private final Position maxPosition;
    private final int rows;
    private final int columns;
    private final Piece[][] pieces;

    public Board(int rows, int columns) {
        this.pieces = new Piece[rows][columns];
        this.rows = rows;
        this.columns = columns;
        this.maxPosition = new Position(rows - 1, columns - 1);
    }

    public Map<Color, List<Piece>> pieces() {
        return Arrays.stream(pieces)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Piece::getColor));
    }

    public List<Piece> pieces(Color color) {
        return Arrays.stream(pieces)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull)
                .filter(p -> Objects.equals(p.getColor(), color))
                .toList();
    }

    /**
     * get a piece from a certain spot
     *
     * @param position coordinate to check
     * @return an Optional piece if found, empty otherwise
     */
    public Optional<Piece> at(Position position) {
        return at(position.row(), position.column());
    }

    public Optional<Piece> at(int row, int column) {
        if (!isInBounds(row, column)) {
            return Optional.empty();
        }

        return Optional.ofNullable(pieces[row][column]);
    }

    public Optional<Piece> place(Piece piece, Position position) {
        Optional<Piece> deleted = remove(position);

        piece.setPosition(position);
        pieces[position.row()][position.column()] = piece;
        return deleted;
    }

    public Optional<Piece> remove(Position position) {
        Optional<Piece> deleted = at(position);
        pieces[position.row()][position.column()] = null;
        deleted.ifPresent(p -> p.setPosition(null));
        return deleted;
    }

    public boolean isInBounds(Position position) {
        return isInBounds(position.row(), position.column());
    }

    public boolean isInBounds(int row, int column) {
        return row >= min().row() && row <= max().row()
                && column >= min().column() && column <= max().column();
    }

    public Position min() {
        return MIN_POSITION;
    }

    public Position max() {
        return maxPosition;
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

}

