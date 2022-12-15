package com.lassis.sensysgatso.chess.web.controller.model;

import java.util.Collection;

public record StatusInfo(String whiteStatus, String blackStatus, String turn, Collection<StatusPieceInfo> deleted) {
}
