package com.lassis.chess.web.controller.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.lassis.chess.model.Color;
import lombok.Builder;

@Builder
public record PieceDTO(
        @JsonUnwrapped
        PointDTO pointInfo,
        Color color,
        String type) {}
