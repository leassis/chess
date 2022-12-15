package com.lassis.sensysgatso.chess.web.controller.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MovementInfo {
    @NotBlank
    private String from;
    @NotBlank
    private String to;
}
