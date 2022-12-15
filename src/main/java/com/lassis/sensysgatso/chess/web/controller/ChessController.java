package com.lassis.sensysgatso.chess.web.controller;

import com.lassis.sensysgatso.chess.game.ChessGame;
import com.lassis.sensysgatso.chess.model.ChessGameStatus;
import com.lassis.sensysgatso.chess.model.Point;
import com.lassis.sensysgatso.chess.web.controller.model.MovementInfo;
import com.lassis.sensysgatso.chess.web.controller.model.PieceDetail;
import com.lassis.sensysgatso.chess.web.controller.model.PieceInfo;
import com.lassis.sensysgatso.chess.web.controller.model.StatusInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
class ChessController {
    private final ObjectTransformer transformer;

    private ChessGame chessGame = new ChessGame();

    @DeleteMapping("/reset")
    ResponseEntity<Void> reset() {
        chessGame = new ChessGame();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    ResponseEntity<StatusInfo> status() {
        return ResponseEntity.ok(transformer.toStatusInfo(chessGame.getStatus()));
    }

    @PostMapping("/movements")
    ResponseEntity<PieceInfo> move(@RequestBody @Valid MovementInfo info) {
        Point from = transformer.toPoint(info.getFrom());
        Point to = transformer.toPoint(info.getTo());
        if (Objects.isNull(from) || Objects.isNull(to)) {
            return ResponseEntity.badRequest().build();
        }

        ChessGameStatus status = chessGame.moveTo(from, to);

        return chessGame.at(to)
                .map(piece -> transformer.toPieceInfo(piece, to))
                .map(body -> ResponseEntity.ok()
                        .header("black", status.blackStatus().toString())
                        .header("white", status.whiteStatus().toString())
                        .header("turn", status.turn().toString())
                        .body(body))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pieces")
    ResponseEntity<List<PieceInfo>> allPieces() {
        List<PieceInfo> result = chessGame.notEmptySquares()
                .stream()
                .map(transformer::toPieceInfo)
                .sorted(Comparator.comparing(PieceInfo::getRow).reversed().thenComparing(PieceInfo::getColumn))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pieces/{chessPoint}")
    ResponseEntity<PieceDetail> pieceDetail(@PathVariable String chessPoint) {
        Point point = transformer.toPoint(chessPoint);
        if (Objects.isNull(point)) {
            return ResponseEntity.badRequest().build();
        }

        return chessGame.at(point)
                .map(piece -> transformer.toPieceDetail(point, piece, chessGame.allowedMoves(point)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

}
