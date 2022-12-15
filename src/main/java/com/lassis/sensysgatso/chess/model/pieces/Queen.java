package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Point;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Queen chess piece. Executes movements straight or diagonal. Further info on <a href="https://en.wikipedia.org/wiki/Chess">...</a>
 */
@ToString
public class Queen extends AbstractChessPiece {
    public Queen(Color color, Board board, Point point) {
        super(color, board, point);
    }

    /**
     * provides a set of possible moves
     *
     * @return set of possible moves. Take in consideration all elements in the board
     */
    @Override
    public Set<Point> allowedMoves() {
        Set<Point> points = new HashSet<>();
        points.addAll(CommonMovements.diagonal(this, getPoint(), getBoard()));
        points.addAll(CommonMovements.straight(this, getPoint(), getBoard()));
        return points;
    }

}
