package com.lassis.chess.model.pieces;

import com.lassis.chess.game.ChessGameTest;
import com.lassis.chess.model.Color;
import com.lassis.chess.model.Point;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class QueenArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(queen(), ChessGameTest.at(4, 4),
                             straight(ChessGameTest.at(4, 4), Set.of(ChessGameTest.at(3, 3), ChessGameTest.at(3, 5), ChessGameTest.at(2, 2), ChessGameTest.at(2, 6), ChessGameTest.at(1, 1), ChessGameTest.at(1, 7), ChessGameTest.at(0, 0), ChessGameTest.at(5, 3), ChessGameTest.at(5, 5), ChessGameTest.at(6, 2), ChessGameTest.at(6, 6), ChessGameTest.at(7, 1), ChessGameTest.at(7, 7))),
                             "queen in the middle"
                ),
                Arguments.of(queen(), ChessGameTest.at(0, 0),
                             straight(ChessGameTest.at(0, 0), Set.of(ChessGameTest.at(1, 1), ChessGameTest.at(2, 2), ChessGameTest.at(3, 3), ChessGameTest.at(4, 4), ChessGameTest.at(5, 5), ChessGameTest.at(6, 6), ChessGameTest.at(7, 7))),
                             "queen in top left corner"
                ),
                Arguments.of(queen(), ChessGameTest.at(0, PieceTest.MAX),
                             straight(ChessGameTest.at(0, PieceTest.MAX), Set.of(ChessGameTest.at(1, PieceTest.MAX - 1), ChessGameTest.at(2, PieceTest.MAX - 2), ChessGameTest.at(3, PieceTest.MAX - 3), ChessGameTest.at(4, PieceTest.MAX - 4), ChessGameTest.at(5, PieceTest.MAX - 5), ChessGameTest.at(6, PieceTest.MAX - 6), ChessGameTest.at(7, PieceTest.MAX - 7))),
                             "queen in top right corner"
                ),
                Arguments.of(queen(), ChessGameTest.at(PieceTest.MAX, 0),
                             straight(ChessGameTest.at(PieceTest.MAX, 0), Set.of(ChessGameTest.at(PieceTest.MAX - 1, 1), ChessGameTest.at(PieceTest.MAX - 2, 2), ChessGameTest.at(PieceTest.MAX - 3, 3), ChessGameTest.at(PieceTest.MAX - 4, 4), ChessGameTest.at(PieceTest.MAX - 5, 5), ChessGameTest.at(PieceTest.MAX - 6, 6), ChessGameTest.at(PieceTest.MAX - 7, 7))),
                             "queen in bottom left corner"
                ),
                Arguments.of(queen(), ChessGameTest.at(PieceTest.MAX, PieceTest.MAX),
                             straight(ChessGameTest.at(PieceTest.MAX, PieceTest.MAX), Set.of(ChessGameTest.at(PieceTest.MAX - 1, PieceTest.MAX - 1), ChessGameTest.at(PieceTest.MAX - 2, PieceTest.MAX - 2), ChessGameTest.at(PieceTest.MAX - 3, PieceTest.MAX - 3), ChessGameTest.at(PieceTest.MAX - 4, PieceTest.MAX - 4), ChessGameTest.at(PieceTest.MAX - 5, PieceTest.MAX - 5), ChessGameTest.at(PieceTest.MAX - 6, PieceTest.MAX - 6), ChessGameTest.at(PieceTest.MAX - 7, PieceTest.MAX - 7))),
                             "queen in bottom right corner"
                ),
                Arguments.of(queen(), ChessGameTest.at(3, 0),
                             straight(ChessGameTest.at(3, 0), Set.of(ChessGameTest.at(2, 1), ChessGameTest.at(1, 2), ChessGameTest.at(0, 3), ChessGameTest.at(4, 1), ChessGameTest.at(5, 2), ChessGameTest.at(6, 3), ChessGameTest.at(7, 4))),
                             "queen in most left"
                ),
                Arguments.of(queen(), ChessGameTest.at(3, PieceTest.MAX),
                             straight(ChessGameTest.at(3, PieceTest.MAX), Set.of(ChessGameTest.at(2, PieceTest.MAX - 1), ChessGameTest.at(1, PieceTest.MAX - 2), ChessGameTest.at(0, PieceTest.MAX - 3), ChessGameTest.at(4, PieceTest.MAX - 1), ChessGameTest.at(5, PieceTest.MAX - 2), ChessGameTest.at(6, PieceTest.MAX - 3), ChessGameTest.at(7, PieceTest.MAX - 4))),
                             "queen in most right"
                ),
                Arguments.of(queen(), ChessGameTest.at(0, 3),
                             straight(ChessGameTest.at(0, 3), Set.of(ChessGameTest.at(1, 2), ChessGameTest.at(2, 1), ChessGameTest.at(3, 0), ChessGameTest.at(1, 4), ChessGameTest.at(2, 5), ChessGameTest.at(3, 6), ChessGameTest.at(4, 7))),
                             "queen in top"
                ),
                Arguments.of(queen(), ChessGameTest.at(PieceTest.MAX, 3),
                             straight(ChessGameTest.at(PieceTest.MAX, 3), Set.of(ChessGameTest.at(PieceTest.MAX - 1, 2), ChessGameTest.at(PieceTest.MAX - 2, 1), ChessGameTest.at(PieceTest.MAX - 3, 0), ChessGameTest.at(PieceTest.MAX - 1, 4), ChessGameTest.at(PieceTest.MAX - 2, 5), ChessGameTest.at(PieceTest.MAX - 3, 6), ChessGameTest.at(PieceTest.MAX - 4, 7))),
                             "queen in bottom"
                )
        );
    }

    private HashSet<Point> straight(Point point, Set<Point> expected) {
        int r = point.row();
        int c = point.column();

        HashSet<Point> result = new HashSet<>(expected);
        result.addAll(Arrays.asList(
                ChessGameTest.at(r, 0), ChessGameTest.at(r, 1), ChessGameTest.at(r, 2), ChessGameTest.at(r, 3), ChessGameTest.at(r, 4), ChessGameTest.at(r, 5), ChessGameTest.at(r, 6), ChessGameTest.at(r, 7),
                ChessGameTest.at(0, c), ChessGameTest.at(1, c), ChessGameTest.at(2, c), ChessGameTest.at(3, c), ChessGameTest.at(4, c), ChessGameTest.at(5, c), ChessGameTest.at(6, c), ChessGameTest.at(7, c))
        );
        result.remove(point);
        return result;
    }

    private Queen queen() {
        return new Queen(Color.BLACK);
    }
}
