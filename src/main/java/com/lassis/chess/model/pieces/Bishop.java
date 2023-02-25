package com.lassis.chess.model.pieces;

import com.lassis.chess.model.Board;
import com.lassis.chess.model.Color;
import com.lassis.chess.model.Piece;
import com.lassis.chess.model.Point;

import java.util.Set;

/**
 * Bishop chess piece, execute movements on diagonals, as long as no one in the way.
 * Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
public record Bishop(Color color) implements Piece {
    /**
     * provides a set of possible moves
     *
     * @return set of possible moves. Take in consideration all elements in the board
     */
    @Override
    public Set<Point> allowedMoves(Board board, Point point) {
        return CommonMovements.diagonal(this, point, board);
    }

}
