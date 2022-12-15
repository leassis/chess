package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Position;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@UtilityClass
class CommonMovements {

    Set<Position> straight(Piece piece, Board board) {
        final Position position = piece.getPosition();

        Set<Position> result = new HashSet<>();
        // up
        for (int row = position.row() - 1; row >= board.min().row(); row--) {
            if (processSquare(piece, board, result, row, position.column())) break;
        }

        // down
        for (int row = position.row() + 1; row <= board.max().row(); row++) {
            if (processSquare(piece, board, result, row, position.column())) break;
        }

        // left
        for (int column = position.column() - 1; column >= board.min().column(); column--) {
            if (processSquare(piece, board, result, position.row(), column)) break;
        }

        // right
        for (int column = position.column() + 1; column <= board.max().column(); column++) {
            if (processSquare(piece, board, result, position.row(), column)) break;
        }
        return result;
    }

    Set<Position> diagonal(Piece piece, Board board) {
        Position position = piece.getPosition();

        Set<Position> result = new HashSet<>();
        // right down
        for (int row = position.row() + 1, column = position.column() + 1; row <= board.max().row() && column <= board.max().column(); row++, column++) {
            if (processSquare(piece, board, result, row, column)) break;
        }

        // left down
        for (int row = position.row() + 1, column = position.column() - 1; row <= board.max().row() && column >= board.min().column(); row++, column--) {
            if (processSquare(piece, board, result, row, column)) break;
        }

        // right up
        for (int row = position.row() - 1, column = position.column() + 1; row >= board.min().row() && column <= board.max().column(); row--, column++) {
            if (processSquare(piece, board, result, row, column)) break;
        }

        // left up
        for (int row = position.row() - 1, column = position.column() - 1; row >= board.min().row() && column >= board.min().column(); row--, column--) {
            if (processSquare(piece, board, result, row, column)) break;
        }
        return result;

    }

    private boolean processSquare(Piece piece, Board board, Set<Position> positions, int row, int column) {
        Position position = piece.getPosition();

        Optional<Piece> pieceFound = board.at(row, column);
        // found piece in the way
        if (pieceFound.isPresent()) {
            if (!isSameColor(piece, pieceFound.get())) {
                positions.add(new Position(row, column));
            }
            return true;
        } else if (board.isInBounds(row, column) && (position.row() != row || position.column() != column)) {
            positions.add(new Position(row, column));
        }
        return false;
    }

    private boolean isSameColor(Piece piece, Piece pieceFound) {
        return Objects.equals(piece.getColor(), pieceFound.getColor());
    }

}
