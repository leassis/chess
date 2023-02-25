package com.lassis.sensysgatso.chess.model;

import lombok.RequiredArgsConstructor;

import static com.lassis.sensysgatso.chess.model.Placement.NORTH;
import static com.lassis.sensysgatso.chess.model.Placement.SOUTH;

@RequiredArgsConstructor
public enum Color {
    BLACK(NORTH), WHITE(SOUTH);

    final Placement placement;

    public Placement placement() {
        return placement;
    }
}
