package com.lassis.sensysgatso.chess.web.controller.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import java.util.List;

public record PieceDetailInfo(@JsonUnwrapped PieceInfo pieceInfo, List<PointInfo> allowedMoves) {}
