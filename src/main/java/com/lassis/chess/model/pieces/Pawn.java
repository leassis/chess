package com.lassis.chess.model.pieces;

import com.lassis.chess.model.Board;
import com.lassis.chess.model.Color;
import com.lassis.chess.model.Piece;
import com.lassis.chess.model.Placement;
import com.lassis.chess.model.Point;
import com.lassis.chess.model.Square;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Pawn chess piece. Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
public record Pawn(Color color) implements Piece {
    /**
     * provides a set of possible moves
     *
     * @return set of possible moves. Take in consideration all elements in the board
     */
    @Override
    public Set<Point> allowedMoves(Board board, Point point) {
        final short sum = (short) (color.placement() == Placement.NORTH ? 1 : -1);
        final int column = point.column();

        int row = point.row() + sum;

        Set<Point> result = new HashSet<>();
        // diagonal left
        Optional<Piece> diagonalLeft = board.at(row, column - 1).flatMap(Square::piece)
                                            .filter(piece -> !Objects.equals(piece.color(), color));

        if (diagonalLeft.isPresent()) {
            result.add(new Point(row, column - 1));
        }

        // diagonal right
        Optional<Piece> diagonalRight = board.at(row, column + 1).flatMap(Square::piece)
                                             .filter(piece -> !Objects.equals(piece.color(), color));
        if (diagonalRight.isPresent()) {
            result.add(new Point(row, column + 1));
        }

        //step one
        if (isAllowed(row, column, board)) {
            result.add(new Point(row, column));

            //step two
            row += sum;
            if (isAllowed(row, column, board)) {
                result.add(new Point(row, column));
            }
        }

        return result;
    }

    private boolean isAllowed(int row, int column, Board board) {
        return board.isInBounds(row, column) && board.at(row, column).flatMap(Square::piece).isEmpty();
    }
}
