package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Position;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@RequiredArgsConstructor
@Setter
@ToString
public class Rook implements Piece {

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
        return CommonMovements.straight(this, board);
    }
}
