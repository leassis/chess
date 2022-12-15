package com.lassis.sensysgatso.chess.game;

import com.lassis.sensysgatso.chess.exception.EmptySquareException;
import com.lassis.sensysgatso.chess.exception.InvalidMoveException;
import com.lassis.sensysgatso.chess.exception.WrongPlayerException;
import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.ChessGameStatus;
import com.lassis.sensysgatso.chess.model.ChessStatus;
import com.lassis.sensysgatso.chess.model.Point;
import com.lassis.sensysgatso.chess.model.pieces.Bishop;
import com.lassis.sensysgatso.chess.model.pieces.Pawn;
import com.lassis.sensysgatso.chess.model.pieces.Queen;
import com.lassis.sensysgatso.chess.model.pieces.Rook;
import org.junit.jupiter.api.Test;

import static com.lassis.sensysgatso.chess.game.ChessGame.SIZE_8;
import static com.lassis.sensysgatso.chess.model.Color.BLACK;
import static com.lassis.sensysgatso.chess.model.Color.WHITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ChessGameTest {

    @Test
    void white_should_be_on_checkmate() {
        Board board = new Board(SIZE_8, SIZE_8);
        new Rook(BLACK, board, at(3, 4)); // create and place
        new Queen(WHITE, board, at(board.max().row(), 3));
        new Bishop(WHITE, board, at(board.max().row(), 5));
        new Pawn(WHITE, board, at(board.max().row() - 1, 3));
        new Pawn(WHITE, board, at(board.max().row() - 1, 5));

        ChessGame chessGame = new ChessGame(board, false);
        ChessGameStatus status = chessGame.getStatus();
        assertThat(status.blackStatus()).isEqualTo(ChessStatus.NORMAL);
        assertThat(status.whiteStatus()).isEqualTo(ChessStatus.CHECKMATE);
    }

    @Test
    void black_should_be_on_check_after_a_move() {
        Board board = new Board(SIZE_8, SIZE_8);
        new Rook(WHITE, board, at(board.max().row()-3, 2)); // create and place

        ChessGame chessGame = new ChessGame(board, false);
        ChessGameStatus status = chessGame.getStatus();
        assertThat(status.blackStatus()).isEqualTo(ChessStatus.NORMAL);
        assertThat(status.whiteStatus()).isEqualTo(ChessStatus.NORMAL);

        // white
        status = chessGame.moveTo(at(board.max().row()-3, 2), to(board.max().row()-3,4));
        assertThat(status.blackStatus()).isEqualTo(ChessStatus.CHECK);
        assertThat(status.whiteStatus()).isEqualTo(ChessStatus.NORMAL);
    }

    @Test
    void black_should_be_on_checkmate_cause_it_is_white_turn() {
        Board board = new Board(SIZE_8, SIZE_8);
        new Pawn(WHITE, board, at(board.max().row()-4, 3));

        new Queen(BLACK, board, at(board.min().row(), 3));
        new Bishop(BLACK, board, at(board.min().row(), 5));
        new Pawn(BLACK, board, at(board.min().row() + 1, 5));

        ChessGame chessGame = new ChessGame(board, false);
        ChessGameStatus status = chessGame.getStatus();
        assertThat(status.blackStatus()).isEqualTo(ChessStatus.NORMAL);
        assertThat(status.whiteStatus()).isEqualTo(ChessStatus.NORMAL);

        // white
        status = chessGame.moveTo(at(board.max().row()-4, 3), to(board.max().row()-6,3));
        assertThat(status.blackStatus()).isEqualTo(ChessStatus.CHECK);
        assertThat(status.whiteStatus()).isEqualTo(ChessStatus.NORMAL);

        // black (create checkmate on himself)
        status = chessGame.moveTo(at(board.min().row(), 5), to(board.min().row()+1, 6));
        assertThat(status.blackStatus()).isEqualTo(ChessStatus.CHECKMATE);
        assertThat(status.whiteStatus()).isEqualTo(ChessStatus.NORMAL);
    }

    @Test
    void should_not_allow_wrong_player(){
        ChessGame chessGame = new ChessGame();
        assertThatThrownBy(() -> chessGame.moveTo(at(1,0), to(2,0))).isInstanceOf(WrongPlayerException.class);
    }

    @Test
    void should_throw_exception_if_try_to_move_empty_square(){
        ChessGame chessGame = new ChessGame();
        assertThatThrownBy(() -> chessGame.moveTo(at(4,0), to(4,0))).isInstanceOf(EmptySquareException.class);
    }

    @Test
    void should_throw_exception_if_piece_is_allowed_to_move(){
        ChessGame chessGame = new ChessGame();
        assertThatThrownBy(() -> chessGame.moveTo(at(6,0), to(4,3))).isInstanceOf(InvalidMoveException.class);
    }

    public static Point at(int row, int column) {
        return new Point(row, column);
    }

    public static Point to(int row, int column) {
        return at(row, column);
    }


}