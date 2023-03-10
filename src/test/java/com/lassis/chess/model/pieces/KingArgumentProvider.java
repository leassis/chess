package com.lassis.chess.model.pieces;

import com.lassis.chess.model.Color;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Set;
import java.util.stream.Stream;

import static com.lassis.chess.game.ChessGameTest.at;
import static com.lassis.chess.model.pieces.PieceTest.MAX;

public class KingArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(king(), at(3, 3),
                        Set.of(at(2, 2), at(2, 3), at(2, 4), at(3, 2), at(3, 4), at(4, 2), at(4, 3), at(4, 4)),
                        "king in the middle"
                ),
                Arguments.of(king(), at(0, 0),
                        Set.of(at(0, 1), at(1, 0), at(1, 1)),
                        "king in top left corner"
                ),
                Arguments.of(king(), at(0, MAX),
                        Set.of(at(0, MAX - 1), at(1, MAX - 1), at(1, MAX)),
                        "king in top right corner"
                ),
                Arguments.of(king(), at(MAX, 0),
                        Set.of(at(MAX, 1), at(MAX - 1, 0), at(MAX - 1, 1)),
                        "king in bottom left corner"
                ),
                Arguments.of(king(), at(MAX, MAX),
                        Set.of(at(MAX, MAX - 1), at(MAX - 1, MAX), at(MAX - 1, MAX - 1)),
                        "king in bottom right corner"
                ),
                Arguments.of(king(), at(3, 0),
                        Set.of(at(2, 0), at(2, 1), at(3, 1), at(4, 1), at(4, 0)),
                        "king in most left"
                ),
                Arguments.of(king(), at(3, MAX),
                        Set.of(at(2, MAX), at(2, MAX - 1), at(3, MAX - 1), at(4, MAX - 1), at(4, MAX)),
                        "king in most right"
                ),
                Arguments.of(king(), at(0, 3),
                        Set.of(at(0, 2), at(1, 2), at(1, 3), at(1, 4), at(0, 4)),
                        "king in top"
                ),
                Arguments.of(king(), at(MAX, 3),
                        Set.of(at(MAX, 2), at(MAX - 1, 2), at(MAX - 1, 3), at(MAX - 1, 4), at(MAX, 4)),
                        "king in bottom"
                )
        );
    }

    private King king() {
        return new King(Color.BLACK);
    }
}
