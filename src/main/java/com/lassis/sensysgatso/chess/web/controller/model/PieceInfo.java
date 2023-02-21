package com.lassis.sensysgatso.chess.web.controller.model;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.lassis.sensysgatso.chess.model.Color;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PieceInfo {
    @JsonUnwrapped
    PointInfo pointInfo;
    Color color;
    String type;
}
