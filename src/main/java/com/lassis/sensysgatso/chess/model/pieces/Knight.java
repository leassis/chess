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

@RequiredArgsConstructor
@Setter
@ToString
public class Knight implements Piece {
    private static final int[] rowSums = new int[]{-2, -2, -1, -1, 1, 1, 2, 2};
    private static final int[] columnSums = new int[]{-1, 1, -2, 2, -2, 2, -1, 1};

    private final Color color;
    private final Board board;

    @Getter
    private Position position;

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Set<Position> allowedMoves() {
        Set<Position> result = new HashSet<>();
        for (int i = 0; i < rowSums.length; i++) {
            int row = position.row() + rowSums[i];
            int column = position.column() + columnSums[i];
            boolean isOtherColor = board.at(row, column).filter(p -> Objects.equals(getColor(), p.getColor())).isEmpty();
            if (board.isInBounds(row, column) && isOtherColor) {
                result.add(new Position(row, column));
            }
        }
        return result;
    }

}
