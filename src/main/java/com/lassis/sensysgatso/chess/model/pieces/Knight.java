package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import lombok.Value;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Knight chess piece. Execute movements on L shape.
 * Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
@Value
public class Knight implements Piece {
    private static final int[] rowSums = new int[]{-2, -2, -1, -1, 1, 1, 2, 2};
    private static final int[] columnSums = new int[]{-1, 1, -2, 2, -2, 2, -1, 1};
    Color color;

    /**
     * provides a set of possible moves
     *
     * @return set of possible moves. Take in consideration all elements in the board
     */
    @Override
    public Set<Point> allowedMoves(Board board, Point point) {

        final Set<Point> result = new HashSet<>();
        for (int i = 0; i < rowSums.length; i++) {
            final int row = point.row() + rowSums[i];
            final int column = point.column() + columnSums[i];
            final boolean isOtherColor = board.at(row, column).filter(p -> Objects.equals(getColor(), p.piece().getColor())).isEmpty();
            if (board.isInBounds(row, column) && isOtherColor) {
                result.add(new Point(row, column));
            }
        }
        return result;
    }

}
