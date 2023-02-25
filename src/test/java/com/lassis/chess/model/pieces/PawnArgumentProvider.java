package com.lassis.chess.model.pieces;

import com.lassis.chess.game.ChessGameTest;
import com.lassis.chess.model.Color;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Set;
import java.util.stream.Stream;

import static com.lassis.chess.model.pieces.PieceTest.MAX;

public class PawnArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
        return Stream.of(
                Arguments.of(pawn(Color.BLACK), ChessGameTest.at(4, 4),
                             Set.of(ChessGameTest.at(5, 4), ChessGameTest.at(6, 4)),
                             "black pawn in the middle"
                ),
                Arguments.of(pawn(Color.WHITE), ChessGameTest.at(4, 4),
                             Set.of(ChessGameTest.at(3, 4), ChessGameTest.at(2, 4)),
                             "white pawn in the middle"
                ),
                Arguments.of(pawn(Color.BLACK), ChessGameTest.at(1, 0),
                             Set.of(ChessGameTest.at(2, 0), ChessGameTest.at(3, 0)),
                             "black pawn in top left corner"
                ),
                Arguments.of(pawn(Color.BLACK), ChessGameTest.at(1, MAX),
                             Set.of(ChessGameTest.at(2, MAX), ChessGameTest.at(3, MAX)),
                             "black pawn in top right corner"
                ),
                Arguments.of(pawn(Color.WHITE), ChessGameTest.at(MAX - 1, 0),
                             Set.of(ChessGameTest.at(MAX - 2, 0), ChessGameTest.at(MAX - 3, 0)),
                             "while pawn in bottom left corner"
                ),
                Arguments.of(pawn(Color.WHITE), ChessGameTest.at(MAX - 1, MAX),
                             Set.of(ChessGameTest.at(MAX - 2, MAX), ChessGameTest.at(MAX - 3, MAX)),
                             "black pawn in bottom right corner"
                )
        );
    }

    private Pawn pawn(Color color) {
        return new Pawn(color);
    }
}
