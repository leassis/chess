package com.lassis.chess.model.pieces;

import com.lassis.chess.model.Color;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Set;
import java.util.stream.Stream;

import static com.lassis.chess.game.ChessGameTest.at;
import static com.lassis.chess.model.pieces.PieceTest.MAX;

public class BishopArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {

        return Stream.of(
                Arguments.of(bishop(), at(4, 4),
                        Set.of(at(3, 3), at(3, 5), at(2, 2), at(2, 6), at(1, 1), at(1, 7), at(0, 0), at(5, 3), at(5, 5), at(6, 2), at(6, 6), at(7, 1), at(7, 7)),
                        "bishop in the middle"
                ),
                Arguments.of(
                        bishop(), at(0, 0),
                        Set.of(at(1, 1), at(2, 2), at(3, 3), at(4, 4), at(5, 5), at(6, 6), at(7, 7)),
                        "bishop in top left corner"
                ),
                Arguments.of(bishop(), at(0, MAX),
                        Set.of(at(1, MAX - 1), at(2, MAX - 2), at(3, MAX - 3), at(4, MAX - 4), at(5, MAX - 5), at(6, MAX - 6), at(7, MAX - 7)),
                        "bishop in top right corner"
                ),
                Arguments.of(bishop(), at(MAX, 0),
                        Set.of(at(MAX - 1, 1), at(MAX - 2, 2), at(MAX - 3, 3), at(MAX - 4, 4), at(MAX - 5, 5), at(MAX - 6, 6), at(MAX - 7, 7)),
                        "bishop in bottom left corner"
                ),
                Arguments.of(bishop(), at(MAX, MAX),
                        Set.of(at(MAX - 1, MAX - 1), at(MAX - 2, MAX - 2), at(MAX - 3, MAX - 3), at(MAX - 4, MAX - 4), at(MAX - 5, MAX - 5), at(MAX - 6, MAX - 6), at(MAX - 7, MAX - 7)),
                        "bishop in bottom right corner"
                ),
                Arguments.of(bishop(), at(3, 0),
                        Set.of(at(2, 1), at(1, 2), at(0, 3), at(4, 1), at(5, 2), at(6, 3), at(7, 4)),
                        "bishop in most left"
                ),
                Arguments.of(bishop(), at(3, MAX),
                        Set.of(at(2, MAX - 1), at(1, MAX - 2), at(0, MAX - 3), at(4, MAX - 1), at(5, MAX - 2), at(6, MAX - 3), at(7, MAX - 4)),
                        "bishop in most right"
                ),
                Arguments.of(bishop(), at(0, 3),
                        Set.of(at(1, 2), at(2, 1), at(3, 0), at(1, 4), at(2, 5), at(3, 6), at(4, 7)),
                        "bishop in top"
                ),
                Arguments.of(bishop(), at(MAX, 3),
                        Set.of(at(MAX - 1, 2), at(MAX - 2, 1), at(MAX - 3, 0), at(MAX - 1, 4), at(MAX - 2, 5), at(MAX - 3, 6), at(MAX - 4, 7)),
                        "bishop in bottom"
                )
        );
    }

    private Bishop bishop() {
        return new Bishop(Color.BLACK);
    }

}
