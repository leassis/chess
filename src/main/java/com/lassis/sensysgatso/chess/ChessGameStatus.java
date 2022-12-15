package com.lassis.sensysgatso.chess;

import com.lassis.sensysgatso.chess.model.ChessStatus;
import com.lassis.sensysgatso.chess.model.Color;
import com.lassis.sensysgatso.chess.model.Piece;
import lombok.Builder;
import lombok.Value;

import java.util.Optional;

@Value
@Builder
public class ChessGameStatus {
    private final ChessStatus blackStatus;
    private final ChessStatus whiteStatus;
    private final Piece deleted;
    private final Color turn;

    public Optional<Piece> getDeleted() {
        return Optional.ofNullable(deleted);
    }
}
