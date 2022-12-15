package com.lassis.chess.web.controller;

import com.lassis.chess.game.ChessGame;
import com.lassis.chess.model.ChessGameStatus;
import com.lassis.chess.model.Point;
import com.lassis.chess.web.controller.model.MoveDTO;
import com.lassis.chess.web.controller.model.PieceDTO;
import com.lassis.chess.web.controller.model.PieceDetailDTO;
import com.lassis.chess.web.controller.model.StatusDTO;
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

import java.util.List;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
class ChessController {
    private final ObjectTransformer transformer;

    private ChessGame chessGame = new ChessGame();

    @DeleteMapping("/game")
    ResponseEntity<Void> reset() {
        chessGame = new ChessGame();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    ResponseEntity<StatusDTO> status() {
        return ResponseEntity.ok(transformer.toStatusInfo(chessGame.getStatus()));
    }

    @PostMapping("/moves")
    ResponseEntity<PieceDTO> move(@RequestBody @Valid MoveDTO info) {
        Point from = transformer.toPoint(info.from());
        Point to = transformer.toPoint(info.to());
        if (Objects.isNull(from) || Objects.isNull(to)) {
            return ResponseEntity.badRequest().build();
        }

        ChessGameStatus status = chessGame.moveTo(from, to);

        return chessGame.at(to)
                        .filter(v -> v.piece().isPresent())
                        .map(transformer::toPieceInfo)
                        .map(body -> ResponseEntity.ok()
                                                   .header("black", status.blackStatus().toString())
                                                   .header("white", status.whiteStatus().toString())
                                                   .header("turn", status.turn().toString())
                                                   .body(body))
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/pieces")
    ResponseEntity<List<PieceDTO>> allPieces() {
        List<PieceDTO> result = chessGame.notEmptySquares()
                                         .stream()
                                         .map(transformer::toPieceInfo)
                                         .sorted((o1, o2) -> ObjectTransformer.POINTINFO_COMPARATOR.compare(o1.pointInfo(), o2.pointInfo()))
                                         .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/pieces/{chessPoint}")
    ResponseEntity<PieceDetailDTO> pieceDetail(@PathVariable String chessPoint) {
        Point point = transformer.toPoint(chessPoint);
        if (Objects.isNull(point)) {
            return ResponseEntity.badRequest().build();
        }

        return chessGame.at(point)
                        .filter(v -> v.piece().isPresent())
                        .map(square -> transformer.toPieceDetail(square, chessGame.allowedMoves(point)))
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
