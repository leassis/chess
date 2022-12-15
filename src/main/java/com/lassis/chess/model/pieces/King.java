package com.lassis.chess.model.pieces;

import com.lassis.chess.model.Board;
import com.lassis.chess.model.Color;
import com.lassis.chess.model.Piece;
import com.lassis.chess.model.Point;
import com.lassis.chess.model.Square;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * King chess piece executes movements around himself.
 * Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
public record King(Color color) implements Piece {
    private static final int SQUARE_SIZE = 3;

    /**
     * provides a set of possible moves
     *
     * @return set of possible moves. Take in consideration all elements in the board
     */
    @Override
    public Set<Point> allowedMoves(Board board, Point point) {
        final int startRow = point.row() - 1;
        final int startColumn = point.column() - 1;

        final Set<Point> possible = new HashSet<>();
        for (int row = startRow; row < startRow + SQUARE_SIZE; row++) {
            for (int column = startColumn; column < startColumn + SQUARE_SIZE; column++) {
                boolean isSameColor = board.at(row, column).flatMap(Square::piece).filter(p -> Objects.equals(color(), p.color())).isPresent();
                if ((row != point.row() || column != point.column()) && board.isInBounds(row, column) && !isSameColor) {
                    possible.add(new Point(row, column));
                }
            }
        }

        return possible;
    }

}
