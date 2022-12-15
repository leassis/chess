package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Position;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@RequiredArgsConstructor
@ToString
public class King implements Piece {
    private static final int SQUARE_SIZE = 3;

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
        final int startRow = position.row() - 1;
        final int startColumn = position.column() - 1;

        final Set<Position> possible = new HashSet<>();
        for (int row = startRow; row < startRow + SQUARE_SIZE; row++) {
            for (int column = startColumn; column < startColumn + SQUARE_SIZE; column++) {
                boolean isSameColor = board.at(row, column).filter(p -> Objects.equals(getColor(), p.getColor())).isPresent();
                if ((row != position.row() || column != position.column()) && board.isInBounds(row, column) && !isSameColor) {
                    possible.add(new Position(row, column));
                }
            }
        }

        return possible;
    }

}
