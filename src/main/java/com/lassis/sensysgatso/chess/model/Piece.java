package com.lassis.sensysgatso.chess.model;

import java.util.Set;

public interface Piece {
    Color getColor();

    Set<Position> allowedMoves();

    void setPosition(Position position);

    Position getPosition();
}
