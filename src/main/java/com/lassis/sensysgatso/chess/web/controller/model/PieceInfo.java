package com.lassis.sensysgatso.chess.web.controller.model;

import com.lassis.sensysgatso.chess.model.Color;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PieceInfo {
    private final String id;
    private final int row;
    private final int column;
    private final Color color;
    private final String type;
}
