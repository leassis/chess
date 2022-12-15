package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.ChessGame;
import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Position;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.lassis.sensysgatso.chess.ChessGameTest.at;
import static com.lassis.sensysgatso.chess.model.pieces.PieceTest.MAX;

public class RookArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                arguments(4, 3, "rook in the middle"),
                arguments(0, 0, "rook in top left corner"),
                arguments(0, MAX, "rook in top right corner"),
                arguments(MAX, 0, "rook in down left corner"),
                arguments(MAX, MAX, "rook in down right corner"),
                arguments(3, 0, "rook in most left"),
                arguments(3, MAX, "rook in most right"),
                arguments(0, 3, "rook in top"),
                arguments(MAX, 0, "rook in bottom")

        );
    }

    private Arguments arguments(int r, int c, String description) {
        Position position = new Position(r, c);
        Set<Position> result = new HashSet<>(Arrays.asList(
                at(r, 0), at(r, 1), at(r, 2), at(r, 3), at(r, 4), at(r, 5), at(r, 6), at(r, 7),
                at(0, c), at(1, c), at(2, c), at(3, c), at(4, c), at(5, c), at(6, c), at(7, c))
        );
        result.remove(position);

        return Arguments.of(rook(position), result, description);
    }

    private Rook rook(Position position) {
        Board board = new Board(ChessGame.SIZE, ChessGame.SIZE);

        Rook rook = new Rook(Color.BLACK, board);
        board.place(rook, position);

        return rook;
    }
}
