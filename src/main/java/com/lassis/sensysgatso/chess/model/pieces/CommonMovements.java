package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import com.lassis.sensysgatso.chess.model.Square;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Helper class to be shared between elements
 */
@UtilityClass
class CommonMovements {

    /**
     * creates possible movements in straight line in all directions, as long as there no piece on the way
     *
     * @param piece given piece to move
     * @param point where the piece sits
     * @param board active board
     * @return set of possible points
     */
    Set<Point> straight(Piece piece, Point point, Board board) {

        Set<Point> result = new HashSet<>();
        // up
        for (int row = point.row() - 1; row >= board.min().row(); row--) {
            if (processSquare(piece, point, board, result, row, point.column())) break;
        }

        // down
        for (int row = point.row() + 1; row <= board.max().row(); row++) {
            if (processSquare(piece, point, board, result, row, point.column())) break;
        }

        // left
        for (int column = point.column() - 1; column >= board.min().column(); column--) {
            if (processSquare(piece, point, board, result, point.row(), column)) break;
        }

        // right
        for (int column = point.column() + 1; column <= board.max().column(); column++) {
            if (processSquare(piece, point, board, result, point.row(), column)) break;
        }
        return result;
    }

    /**
     * creates possible movements in diagonal in all directions, as long as there no piece on the way
     *
     * @param piece given piece to move
     * @param point where the piece sits
     * @param board active board
     * @return set of possible points
     */
    Set<Point> diagonal(Piece piece, Point point, Board board) {
        Set<Point> result = new HashSet<>();
        // right down
        for (int row = point.row() + 1, column = point.column() + 1; row <= board.max().row() && column <= board.max().column(); row++, column++) {
            if (processSquare(piece, point, board, result, row, column)) break;
        }

        // left down
        for (int row = point.row() + 1, column = point.column() - 1; row <= board.max().row() && column >= board.min().column(); row++, column--) {
            if (processSquare(piece, point, board, result, row, column)) break;
        }

        // right up
        for (int row = point.row() - 1, column = point.column() + 1; row >= board.min().row() && column <= board.max().column(); row--, column++) {
            if (processSquare(piece, point, board, result, row, column)) break;
        }

        // left up
        for (int row = point.row() - 1, column = point.column() - 1; row >= board.min().row() && column >= board.min().column(); row--, column--) {
            if (processSquare(piece, point, board, result, row, column)) break;
        }
        return result;
    }

    private boolean processSquare(Piece piece, Point point, Board board, Set<Point> points, int row, int column) {

        Optional<Piece> pieceFound = board.at(row, column).map(Square::piece);
        // found piece in the way
        if (pieceFound.isPresent()) {
            if (!isSameColor(piece, pieceFound.get())) {
                points.add(new Point(row, column));
            }
            return true;
        } else if (board.isInBounds(row, column) && (point.row() != row || point.column() != column)) {
            points.add(new Point(row, column));
        }
        return false;
    }

    private boolean isSameColor(Piece piece, Piece pieceFound) {
        return Objects.equals(piece.getColor(), pieceFound.getColor());
    }

}
