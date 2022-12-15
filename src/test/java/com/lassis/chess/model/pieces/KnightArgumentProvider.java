package com.lassis.chess.model.pieces;

import com.lassis.chess.game.ChessGameTest;
import com.lassis.chess.model.Color;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Set;
import java.util.stream.Stream;

import static com.lassis.chess.model.pieces.PieceTest.MAX;

public class KnightArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(knight(), ChessGameTest.at(4, 3),
                             Set.of(ChessGameTest.at(2, 2), ChessGameTest.at(2, 4), ChessGameTest.at(3, 1), ChessGameTest.at(3, 5), ChessGameTest.at(5, 1), ChessGameTest.at(5, 5), ChessGameTest.at(6, 2), ChessGameTest.at(6, 4)),
                             "knight in the middle"
                ),
                Arguments.of(knight(), ChessGameTest.at(0, 0),
                             Set.of(ChessGameTest.at(1, 2), ChessGameTest.at(2, 1)),
                             "knight in top left corner"
                ),
                Arguments.of(knight(), ChessGameTest.at(0, MAX),
                             Set.of(ChessGameTest.at(1, MAX - 2), ChessGameTest.at(2, MAX - 1)),
                             "knight in top right corner"
                ),
                Arguments.of(knight(), ChessGameTest.at(MAX, 0),
                             Set.of(ChessGameTest.at(MAX - 2, 1), ChessGameTest.at(MAX - 1, 2)),
                             "knight in bottom left corner"
                ),
                Arguments.of(knight(), ChessGameTest.at(MAX, MAX),
                             Set.of(ChessGameTest.at(MAX - 2, MAX - 1), ChessGameTest.at(MAX - 1, MAX - 2)),
                             "knight in bottom right corner"
                ),
                Arguments.of(knight(), ChessGameTest.at(3, 0),
                             Set.of(ChessGameTest.at(1, 1), ChessGameTest.at(2, 2), ChessGameTest.at(4, 2), ChessGameTest.at(5, 1)),
                             "knight in most left"
                ),
                Arguments.of(knight(), ChessGameTest.at(3, MAX),
                             Set.of(ChessGameTest.at(1, MAX - 1), ChessGameTest.at(2, MAX - 2), ChessGameTest.at(4, MAX - 2), ChessGameTest.at(5, MAX - 1)),
                             "knight in most right"
                ),
                Arguments.of(knight(), ChessGameTest.at(0, 4),
                             Set.of(ChessGameTest.at(1, 2), ChessGameTest.at(1, 6), ChessGameTest.at(2, 3), ChessGameTest.at(2, 5)),
                             "knight in top"
                ),
                Arguments.of(knight(), ChessGameTest.at(MAX, 4),
                             Set.of(ChessGameTest.at(MAX - 2, 3), ChessGameTest.at(MAX - 2, 5), ChessGameTest.at(MAX - 1, 2), ChessGameTest.at(MAX - 1, 6)),
                             "knight in bottom"
                )
        );
    }

    private Knight knight() {
        return new Knight(Color.BLACK);
    }
}
