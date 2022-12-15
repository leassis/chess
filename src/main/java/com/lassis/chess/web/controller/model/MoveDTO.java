package com.lassis.chess.web.controller.model;

import jakarta.validation.constraints.NotBlank;

public record MoveDTO(
        @NotBlank
        String from,
        @NotBlank
        String to
) {}
