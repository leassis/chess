package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import lombok.Value;

import java.util.Set;

/**
 * Chess Rook piece, executes movements on straight line as long as no one in the way
 * Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
@Value
public class Rook implements Piece {

    Color color;

    /**
     * provides a set of possible moves
     *
     * @return set of possible moves. Take in consideration all elements in the board
     */
    @Override
    public Set<Point> allowedMoves(Board board, Point point) {
        return CommonMovements.straight(this, point, board);
    }
}
