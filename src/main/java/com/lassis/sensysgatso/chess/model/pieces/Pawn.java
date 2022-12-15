package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Placement;
import com.lassis.sensysgatso.chess.model.Position;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Setter
@ToString
public class Pawn implements Piece {
    private final Color color;
    private final Board board;

    @Getter
    private Position position;


    @Override
    public Color getColor() {
        return color;
    }

    /**
     * provides all moves for the king. The king moves only in surrounding squares, it creates a square figure around
     * himself
     *
     * @return
     */
    @Override
    public Set<Position> allowedMoves() {
        final short sum = (short) (color.placement() == Placement.NORTH ? 1 : -1);
        final int column = position.column();

        int row = position.row() + sum;

        Set<Position> result = new HashSet<>();
        // diagonal left
        Optional<Piece> diagonalLeft = board.at(row, column - 1)
                .filter(piece -> !Objects.equals(piece.getColor(), getColor()));
        if (diagonalLeft.isPresent()) {
            result.add(new Position(row, column - 1));
        }

        // diagonal right
        Optional<Piece> diagonalRight = board.at(row, column + 1)
                .filter(piece -> !Objects.equals(piece.getColor(), getColor()));
        if (diagonalRight.isPresent()) {
            result.add(new Position(row, column + 1));
        }

        //step one
        if (isAllowed(row, column)) {
            result.add(new Position(row, column));

            //step two
            row += sum;
            if (isAllowed(row, column)) {
                result.add(new Position(row, column));
            }
        }

        return result;
    }

    private boolean isAllowed(int row, int column) {
        return board.isInBounds(row, column)
                && board.at(row, column).isEmpty();
    }
}
