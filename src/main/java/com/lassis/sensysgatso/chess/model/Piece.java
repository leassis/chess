package com.lassis.sensysgatso.chess.model;

import java.util.Locale;
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
    Color color();

    /**
     * return all possible movements
     *
     * @return set points
     */
    Set<Point> allowedMoves(Board board, Point point);

    default String name() {
        return this.getClass().getSimpleName().toUpperCase(Locale.ROOT);
    }

}
