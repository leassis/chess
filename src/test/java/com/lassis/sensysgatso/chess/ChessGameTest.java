package com.lassis.sensysgatso.chess;

import com.lassis.sensysgatso.chess.model.ChessStatus;
import com.lassis.sensysgatso.chess.model.Position;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ChessGameTest {
    @Test
    void check() {
        ChessGame chessGame = new ChessGame();
        chessGame.moveTo(at(1, 4), at(3, 4)); // black
        chessGame.moveTo(at(6, 4), at(4, 4)); // white

        ChessGameStatus status = chessGame.moveTo(at(3, 4), at(4, 4));// black;
        assertThat(status.getDeleted()).isPresent();

        chessGame.moveTo(at(6, 0), at(4, 0));// white

        status = chessGame.moveTo(at(4, 4), at(5, 4));// black;
        assertThat(status.getWhiteStatus()).isEqualTo(ChessStatus.CHECKMATE);

        System.out.println(chessGame);
    }

    public static Position at(int row, int column) {
        return new Position(row, column);
    }


}