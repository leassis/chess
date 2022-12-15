package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
abstract class AbstractChessPiece implements Piece {
    private final Color color;
    private final Board board;
    private Point point;

    AbstractChessPiece(Color color, Board board, Point point) {
        this.color = color;
        this.board = board;
        this.point = point;
        board.place(this, point);
    }

    @Override
    public void at(Point point) {
        this.point = point;
    }

    @Override
    public Color getColor() {
        return color;
    }
}
