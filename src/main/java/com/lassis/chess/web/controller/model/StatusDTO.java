package com.lassis.chess.web.controller.model;

import java.util.Collection;

public record StatusDTO(String whiteStatus, String blackStatus, String turn, Collection<StatusPieceDTO> deleted) {}
