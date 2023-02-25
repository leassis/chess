package com.lassis.sensysgatso.chess.web.controller.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.lassis.sensysgatso.chess.model.Color;
import lombok.Builder;

@Builder
public record PieceDTO(
        @JsonUnwrapped
        PointDTO pointInfo,
        Color color,
        String type) {}
