package com.lassis.sensysgatso.chess.model.pieces;

import com.lassis.sensysgatso.chess.model.Board;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Point;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommonMovementsTest {

    public static final int SIZE_4x4 = 4;
    public static final int COL_2 = 2;
    public static final int ROW_2 = 2;

    @Test
    void straight_bottom_allow_jump_over_opponent_but_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Rook rook = new Rook(Color.BLACK, board, new Point(0, COL_2));
        Pawn pawn = new Pawn(Color.WHITE, board, new Point(2, COL_2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 0), new Point(0, 1), new Point(0, 3), new Point(1, COL_2), new Point(2, COL_2));

        assertThat(pawn.allowedMoves())
                .containsExactlyInAnyOrder(new Point(1, COL_2));
    }

    @Test
    void straight_bottom_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Rook rook = new Rook(Color.BLACK, board, new Point(0, COL_2));
        Pawn pawn = new Pawn(Color.BLACK, board, new Point(2, COL_2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 0), new Point(0, 1), new Point(0, 3), new Point(1, COL_2));

        assertThat(pawn.allowedMoves())
                .containsExactlyInAnyOrder(new Point(3, COL_2));
    }

    @Test
    void straight_up_allow_jump_over_opponent_but_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Pawn pawn = new Pawn(Color.WHITE, board, new Point(1, COL_2));
        Rook rook = new Rook(Color.BLACK, board, new Point(2, COL_2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(2, 0), new Point(2, 1), new Point(2, 3), new Point(3, COL_2), new Point(1, COL_2));

        assertThat(pawn.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, COL_2));
    }

    @Test
    void straight_up_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Pawn pawn = new Pawn(Color.BLACK, board, new Point(2, COL_2));
        Rook rook = new Rook(Color.BLACK, board, new Point(3, COL_2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(3, 0), new Point(3, 1), new Point(3, 3));

        assertThat(pawn.allowedMoves()).isEmpty();
    }

    @Test
    void straight_right_allow_jump_over_opponent_but_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Pawn pawn = new Pawn(Color.WHITE, board, new Point(ROW_2, 2));
        Rook rook = new Rook(Color.BLACK, board, new Point(ROW_2, 0));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 0), new Point(1, 0), new Point(3, 0), new Point(ROW_2, 1), new Point(ROW_2, 2));

        assertThat(pawn.allowedMoves())
                .containsExactlyInAnyOrder(new Point(ROW_2 - 1, 2), new Point(ROW_2 - 2, 2));
    }

    @Test
    void straight_right_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Pawn pawn = new Pawn(Color.BLACK, board, new Point(ROW_2, 2));
        Rook rook = new Rook(Color.BLACK, board, new Point(ROW_2, 0));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 0), new Point(1, 0), new Point(3, 0), new Point(ROW_2, 1));

        assertThat(pawn.allowedMoves()).containsExactlyInAnyOrder(new Point(ROW_2 + 1, 2));
    }

    @Test
    void straight_left_allow_jump_over_opponent_but_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Pawn pawn = new Pawn(Color.WHITE, board, new Point(ROW_2, 1));
        Rook rook = new Rook(Color.BLACK, board, new Point(ROW_2, 2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 2), new Point(1, 2), new Point(3, 2), new Point(ROW_2, 1), new Point(ROW_2, 3));

        assertThat(pawn.allowedMoves())
                .containsExactlyInAnyOrder(new Point(ROW_2 - 1, 1), new Point(ROW_2 - 2, 1));
    }

    @Test
    void straight_left_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Pawn pawn = new Pawn(Color.BLACK, board, new Point(ROW_2, 1));
        Rook rook = new Rook(Color.BLACK, board, new Point(ROW_2, 2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 2), new Point(1, 2), new Point(3, 2), new Point(ROW_2, 3));

        assertThat(pawn.allowedMoves()).containsExactlyInAnyOrder(new Point(ROW_2 + 1, 1));
    }

    @Test
    void diagonal_bottom_top_right_allow_jump_over_opponent_but_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Bishop rook = new Bishop(Color.WHITE, board, new Point(2, 1));
        new Pawn(Color.BLACK, board, new Point(1, 2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(1, 0), new Point(1, 2), new Point(3, 0), new Point(3, 2));

    }


    @Test
    void diagonal_bottom_top_right_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Bishop rook = new Bishop(Color.WHITE, board, new Point(2, 1));
        new Pawn(Color.WHITE, board, new Point(1, 2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(1, 0), new Point(3, 0), new Point(3, 2));
    }

    @Test
    void diagonal_bottom_top_left_allow_jump_over_opponent_but_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Bishop rook = new Bishop(Color.WHITE, board, new Point(2, 2));
        new Pawn(Color.BLACK, board, new Point(1, 1));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(1, 1), new Point(1, 3), new Point(3, 1), new Point(3, 3));

    }

    @Test
    void diagonal_bottom_top_left_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Bishop rook = new Bishop(Color.WHITE, board, new Point(2, 2));
        new Pawn(Color.WHITE, board, new Point(1, 1));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(1, 3), new Point(3, 1), new Point(3, 3));

    }

    @Test
    void diagonal_top_bottom_right_allow_jump_over_opponent_but_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Bishop rook = new Bishop(Color.BLACK, board, new Point(1, 1));
        new Pawn(Color.WHITE, board, new Point(2, 2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 0), new Point(0, 2), new Point(2, 0), new Point(2, 2));
    }


    @Test
    void diagonal_top_bottom_right_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Bishop rook = new Bishop(Color.BLACK, board, new Point(1, 1));
        new Pawn(Color.BLACK, board, new Point(2, 2));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 0), new Point(0, 2), new Point(2, 0));
    }

    @Test
    void diagonal_top_bottom_left_allow_jump_over_opponent_but_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Bishop rook = new Bishop(Color.BLACK, board, new Point(1, 2));
        new Pawn(Color.WHITE, board, new Point(2, 1));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 1), new Point(0, 3), new Point(2, 1), new Point(2, 3));
    }


    @Test
    void diagonal_top_bottom_left_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Bishop rook = new Bishop(Color.BLACK, board, new Point(1, 2));
        new Pawn(Color.BLACK, board, new Point(2, 1));

        assertThat(rook.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 1), new Point(0, 3), new Point(2, 3));
    }

    @Test
    void straight_and_diagonal_top_bottom_left_do_not_allow_jump_over_same_color_and_stop_travelling() {
        Board board = new Board(SIZE_4x4, SIZE_4x4);
        Queen queen = new Queen(Color.BLACK, board, new Point(1, 2));
        new Pawn(Color.BLACK, board, new Point(2, 1));

        assertThat(queen.allowedMoves())
                .containsExactlyInAnyOrder(new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(2, 3),
                        new Point(2, 2), new Point(3, 2), new Point(1,0), new Point(1,1), new Point(1,3));
    }
}