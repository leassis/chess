package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.PieceInfo;
import com.lassis.sensysgatso.chess.model.Placement;
import com.lassis.sensysgatso.chess.model.Point;
import lombok.Value;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Pawn chess piece. Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
@Value
public class Pawn implements Piece {
    Color color;

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
        Optional<Piece> diagonalLeft = board.at(row, column - 1)
                                            .map(PieceInfo::piece)
                                            .filter(piece -> !Objects.equals(piece.getColor(), color));

        if (diagonalLeft.isPresent()) {
            result.add(new Point(row, column - 1));
        }

        // diagonal right
        Optional<Piece> diagonalRight = board.at(row, column + 1)
                                             .map(PieceInfo::piece)
                                             .filter(piece -> !Objects.equals(piece.getColor(), color));
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
        return board.isInBounds(row, column) && board.at(row, column).isEmpty();
    }
}
