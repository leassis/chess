package com.lassis.chess.model;

import lombok.Builder;

import java.util.List;

@Builder
public record ChessGameStatus(ChessStatus blackStatus, ChessStatus whiteStatus, List<Piece> deleted, Color turn) {}
