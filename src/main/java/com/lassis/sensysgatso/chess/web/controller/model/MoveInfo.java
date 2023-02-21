package com.lassis.sensysgatso.chess.web.controller.model;

import jakarta.validation.constraints.NotBlank;

public record MoveInfo(
        @NotBlank
        String from,
        @NotBlank
        String to
) {}
