package com.lassis.sensysgatso.chess.web.controller.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

public record PieceDetailDTO(@JsonUnwrapped PieceDTO pieceInfo, List<PointDTO> allowedMoves) {}
