package com.lassis.sensysgatso.chess.model;

import java.util.Set;

/**
 * Defines the contract for any piece able to play with the {@link Board}
 */
public interface Piece {
    /**
     * provides the piece color
     *
     * @return enum {@link Color}
     */
    Color getColor();

    /**
     * return all possible movements
     *
     * @return set points
     */
    Set<Point> allowedMoves();

    /**
     * set a point to a piece
     *
     * @param point where the piece is sit
     */
    void at(Point point);
}
