package com.lassis.chess.model;

import lombok.RequiredArgsConstructor;

import static com.lassis.chess.model.Placement.NORTH;
import static com.lassis.chess.model.Placement.SOUTH;

@RequiredArgsConstructor
public enum Color {
    BLACK(NORTH), WHITE(SOUTH);

    final Placement placement;

    public Placement placement() {
        return placement;
    }
}
