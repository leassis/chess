package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import lombok.Value;

import java.util.HashSet;
import java.util.Set;

/**
 * Queen chess piece. Executes movements straight or diagonal. Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
@Value
public class Queen implements Piece {
    Color color;

    /**
     * provides a set of possible moves
     *
     * @return set of possible moves. Take in consideration all elements in the board
     */
    @Override
    public Set<Point> allowedMoves(Board board, Point point) {
        Set<Point> points = new HashSet<>();
        points.addAll(CommonMovements.diagonal(this, point, board));
        points.addAll(CommonMovements.straight(this, point, board));
        return points;
    }

}
