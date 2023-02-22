package com.lassis.sensysgatso.chess.model;

import java.util.Set;

public record PieceInfo(Board board, Piece piece, Point point) {

    public Set<Point> allowedMoves() {
        return piece.allowedMoves(board, point);
    }
}