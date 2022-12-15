package com.lassis.sensysgatso.chess.web.controller.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Value
@Builder
public class PieceDetail {
    @JsonUnwrapped
    private final PieceInfo pieceInfo;
    private final Set<String> points;
}
