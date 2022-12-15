package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.game.ChessGame;
import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Set;
import java.util.stream.Stream;

import static com.lassis.sensysgatso.chess.game.ChessGameTest.at;
import static com.lassis.sensysgatso.chess.model.pieces.PieceTest.MAX;

public class KnightArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(knight(4, 3),
                        Set.of(at(2, 2), at(2, 4), at(3, 1), at(3, 5), at(5, 1), at(5, 5), at(6, 2), at(6, 4)),
                        "knight in the middle"
                ),
                Arguments.of(knight(0, 0),
                        Set.of(at(1, 2), at(2, 1)),
                        "knight in top left corner"
                ),
                Arguments.of(knight(0, MAX),
                        Set.of(at(1, MAX - 2), at(2, MAX - 1)),
                        "knight in top right corner"
                ),
                Arguments.of(knight(MAX, 0),
                        Set.of(at(MAX - 2, 1), at(MAX - 1, 2)),
                        "knight in bottom left corner"
                ),
                Arguments.of(knight(MAX, MAX),
                        Set.of(at(MAX - 2, MAX - 1), at(MAX - 1, MAX - 2)),
                        "knight in bottom right corner"
                ),
                Arguments.of(knight(3, 0),
                        Set.of(at(1, 1), at(2, 2), at(4, 2), at(5, 1)),
                        "knight in most left"
                ),
                Arguments.of(knight(3, MAX),
                        Set.of(at(1, MAX - 1), at(2, MAX - 2), at(4, MAX - 2), at(5, MAX - 1)),
                        "knight in most right"
                ),
                Arguments.of(knight(0, 4),
                        Set.of(at(1, 2), at(1, 6), at(2, 3), at(2, 5)),
                        "knight in top"
                ),
                Arguments.of(knight(MAX, 4),
                        Set.of(at(MAX - 2, 3), at(MAX - 2, 5), at(MAX - 1, 2), at(MAX - 1, 6)),
                        "knight in bottom"
                )
        );
    }

    private Knight knight(int row, int column) {
        Board board = new Board(ChessGame.SIZE_8, ChessGame.SIZE_8);
        return new Knight(Color.BLACK, board, at(row, column));
    }
}
