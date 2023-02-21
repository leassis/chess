package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Point;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static com.lassis.sensysgatso.chess.game.ChessGameTest.at;
import static com.lassis.sensysgatso.chess.model.pieces.PieceTest.MAX;

public class QueenArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(queen(), at(4, 4),
                        straight(at(4, 4), Set.of(at(3, 3), at(3, 5), at(2, 2), at(2, 6), at(1, 1), at(1, 7), at(0, 0), at(5, 3), at(5, 5), at(6, 2), at(6, 6), at(7, 1), at(7, 7))),
                        "queen in the middle"
                ),
                Arguments.of(queen(), at(0, 0),
                        straight(at(0, 0), Set.of(at(1, 1), at(2, 2), at(3, 3), at(4, 4), at(5, 5), at(6, 6), at(7, 7))),
                        "queen in top left corner"
                ),
                Arguments.of(queen(), at(0, MAX),
                        straight(at(0, MAX), Set.of(at(1, MAX - 1), at(2, MAX - 2), at(3, MAX - 3), at(4, MAX - 4), at(5, MAX - 5), at(6, MAX - 6), at(7, MAX - 7))),
                        "queen in top right corner"
                ),
                Arguments.of(queen(), at(MAX, 0),
                        straight(at(MAX, 0), Set.of(at(MAX - 1, 1), at(MAX - 2, 2), at(MAX - 3, 3), at(MAX - 4, 4), at(MAX - 5, 5), at(MAX - 6, 6), at(MAX - 7, 7))),
                        "queen in bottom left corner"
                ),
                Arguments.of(queen(), at(MAX, MAX),
                        straight(at(MAX, MAX), Set.of(at(MAX - 1, MAX - 1), at(MAX - 2, MAX - 2), at(MAX - 3, MAX - 3), at(MAX - 4, MAX - 4), at(MAX - 5, MAX - 5), at(MAX - 6, MAX - 6), at(MAX - 7, MAX - 7))),
                        "queen in bottom right corner"
                ),
                Arguments.of(queen(), at(3, 0),
                        straight(at(3, 0), Set.of(at(2, 1), at(1, 2), at(0, 3), at(4, 1), at(5, 2), at(6, 3), at(7, 4))),
                        "queen in most left"
                ),
                Arguments.of(queen(), at(3, MAX),
                        straight(at(3, MAX), Set.of(at(2, MAX - 1), at(1, MAX - 2), at(0, MAX - 3), at(4, MAX - 1), at(5, MAX - 2), at(6, MAX - 3), at(7, MAX - 4))),
                        "queen in most right"
                ),
                Arguments.of(queen(), at(0, 3),
                        straight(at(0, 3), Set.of(at(1, 2), at(2, 1), at(3, 0), at(1, 4), at(2, 5), at(3, 6), at(4, 7))),
                        "queen in top"
                ),
                Arguments.of(queen(), at(MAX, 3),
                        straight(at(MAX, 3), Set.of(at(MAX - 1, 2), at(MAX - 2, 1), at(MAX - 3, 0), at(MAX - 1, 4), at(MAX - 2, 5), at(MAX - 3, 6), at(MAX - 4, 7))),
                        "queen in bottom"
                )
        );
    }

    private HashSet<Point> straight(Point point, Set<Point> expected) {
        int r = point.row();
        int c = point.column();

        HashSet<Point> result = new HashSet<>(expected);
        result.addAll(Arrays.asList(
                at(r, 0), at(r, 1), at(r, 2), at(r, 3), at(r, 4), at(r, 5), at(r, 6), at(r, 7),
                at(0, c), at(1, c), at(2, c), at(3, c), at(4, c), at(5, c), at(6, c), at(7, c))
        );
        result.remove(point);
        return result;
    }

    private Queen queen() {
        return new Queen(Color.BLACK);
    }
}
