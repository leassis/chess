package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Point;
import lombok.ToString;

import java.util.Set;

/**
 * Bishop chess piece, execute movements on diagonals, as long as no one in the way.
 * Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
@ToString
public class Bishop extends AbstractChessPiece {

    public Bishop(Color color, Board board, Point point) {
        super(color, board, point);
    }

    /**
     * provides a set of possible moves
     *
     * @return set of possible moves. Take in consideration all elements in the board
     */
    @Override
    public Set<Point> allowedMoves() {
        return CommonMovements.diagonal(this, getPoint(), getBoard());
    }

}
