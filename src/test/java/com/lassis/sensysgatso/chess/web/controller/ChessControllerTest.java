package com.lassis.sensysgatso.chess.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lassis.sensysgatso.chess.model.ChessStatus;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.web.controller.model.MoveInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ComponentScan
@WebAppConfiguration
@AutoConfigureMockMvc
@EnableWebMvc
class ChessControllerTest {
    final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void reset() throws Exception {
        MockHttpServletRequestBuilder delete = MockMvcRequestBuilders.delete("/api/game").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(delete)
                .andExpect(status().isNoContent());
    }

    @Test
    void should_get_status() throws Exception {
        MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/status").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(get)
                .andExpect(jsonPath("$.whiteStatus", is(ChessStatus.NORMAL.toString())))
                .andExpect(jsonPath("$.blackStatus", is(ChessStatus.NORMAL.toString())))
                .andExpect(jsonPath("$.turn", is(Color.WHITE.toString())))
                .andExpect(jsonPath("$.deleted", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    void should_retrieve_32_pieces_and_sort() throws Exception {
        MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/pieces").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(get)
                .andExpect(jsonPath("$", hasSize(32)))
                .andExpect(jsonPath("$[0].id", is("A1")))
                .andExpect(jsonPath("$[0].row", is(7)))
                .andExpect(jsonPath("$[0].column", is(0)))
                .andExpect(jsonPath("$[0].type", is("ROOK")))
                .andExpect(jsonPath("$[0].color", is(Color.WHITE.toString())))
                .andExpect(jsonPath("$[1].id", is("B1")))
                .andExpect(jsonPath("$[2].id", is("C1")))
                .andExpect(jsonPath("$[8].id", is("A2")))
                .andExpect(jsonPath("$[8].color", is(Color.WHITE.toString())))
                .andExpect(jsonPath("$[15].color", is(Color.WHITE.toString())))
                .andExpect(jsonPath("$[16].color", is(Color.BLACK.toString())))
                .andExpect(status().isOk());
    }

    @Test
    void should_retrieve_piece_and_check_movement_to_pawn() throws Exception {
        MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/pieces/A2").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(get)
                .andExpect(jsonPath("$.id", is("A2")))
                .andExpect(jsonPath("$.row", is(6)))
                .andExpect(jsonPath("$.column", is(0)))
                .andExpect(jsonPath("$.color", is(Color.WHITE.toString())))
                .andExpect(jsonPath("$.type", is("PAWN")))
                .andExpect(jsonPath("$.allowedMoves", hasSize(2)))
                .andExpect(jsonPath("$.allowedMoves[0].id", is("A3")))
                .andExpect(jsonPath("$.allowedMoves[0].row", is(5)))
                .andExpect(jsonPath("$.allowedMoves[0].column", is(0)))
                .andExpect(jsonPath("$.allowedMoves[1].id", is("A4")))
                .andExpect(jsonPath("$.allowedMoves[1].row", is(4)))
                .andExpect(jsonPath("$.allowedMoves[1].column", is(0)))
                .andExpect(status().isOk());
    }

    @Test
    void should_be_400_piece_invalid_ID() throws Exception {
        MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/pieces/DD2").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(get)
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_404_piece_empty_square() throws Exception {
        MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/pieces/D4").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(get)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_be_move_piece() throws Exception {
        MoveInfo moveInfo = buildMove("A2", "A4");

        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/api/moves")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(moveInfo));

        mockMvc.perform(post)
                .andExpect(jsonPath("$.id", is("A4")))
                .andExpect(jsonPath("$.row", is(4)))
                .andExpect(jsonPath("$.column", is(0)))
                .andExpect(jsonPath("$.color", is(Color.WHITE.toString())))
                .andExpect(jsonPath("$.type", is("PAWN")))
                .andExpect(status().isOk());
    }

    @Test
    void should_be_404_move_empty_square() throws Exception {
        MoveInfo moveInfo = buildMove("D5", "D7");

        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/api/moves")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(moveInfo));

        mockMvc.perform(post)
                .andExpect(status().isNotFound());
    }

    @Test
    void should_be_400_invalid_move() throws Exception {
        MoveInfo moveInfo = buildMove("A1", "B2");

        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/api/moves")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(moveInfo));

        mockMvc.perform(post)
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_be_409_black_trying_to_move_on_white_turn() throws Exception {
        MoveInfo moveInfo = buildMove("A7", "A6");

        MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/api/moves")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(moveInfo));

        mockMvc.perform(post)
                .andExpect(status().isConflict());
    }

    @Test
    void should_delete_black_pawn() throws Exception {
        List<MoveInfo> moves = Arrays.asList(
                buildMove("A2", "A4"),
                buildMove("A7", "A6"),
                buildMove("A1", "A3"),
                buildMove("A6", "A5"),
                buildMove("A3", "D3"),
                buildMove("B7", "B6"),
                buildMove("D3", "D7")
        );

        for (MoveInfo moveInfo : moves) {
            MockHttpServletRequestBuilder post = MockMvcRequestBuilders.post("/api/moves")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(moveInfo));

            mockMvc.perform(post)
                    .andExpect(jsonPath("$.id", is(moveInfo.getTo())))
                    .andExpect(status().isOk());
        }

        MockHttpServletRequestBuilder get = MockMvcRequestBuilders.get("/api/status").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(get)
                .andExpect(jsonPath("$.whiteStatus", is(ChessStatus.NORMAL.toString())))
                .andExpect(jsonPath("$.blackStatus", is(ChessStatus.NORMAL.toString())))
                .andExpect(jsonPath("$.turn", is(Color.BLACK.toString())))
                .andExpect(jsonPath("$.deleted", hasSize(1)))
                .andExpect(jsonPath("$.deleted[0].color", is(Color.BLACK.toString())))
                .andExpect(jsonPath("$.deleted[0].type", is("PAWN")))
                .andExpect(status().isOk());

        get = MockMvcRequestBuilders.get("/api/pieces").accept(MediaType.APPLICATION_JSON_VALUE);
        mockMvc.perform(get)
                .andExpect(jsonPath("$", hasSize(31)))
                .andExpect(status().isOk());

    }

    private static MoveInfo buildMove(String A7, String A6) {
        MoveInfo moveInfo = new MoveInfo();
        moveInfo.setFrom(A7);
        moveInfo.setTo(A6);
        return moveInfo;
    }
}