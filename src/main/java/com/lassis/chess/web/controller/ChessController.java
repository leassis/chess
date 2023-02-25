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
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.lassis.chess.web.controller.ObjectTransformer.POINTINFO_COMPARATOR;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
class ChessController {
    private final Map<UUID, ChessGame> games = new ConcurrentHashMap<>();
    private final ObjectTransformer transformer;

    @DeleteMapping("/game/{id}")
    ResponseEntity<Void> reset(@PathVariable("id") UUID id) {
        var game = games.get(id);
        if (Objects.isNull(game)) {
            return ResponseEntity.notFound().build();
        }

        games.remove(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/game")
    ResponseEntity<Void> newGame() {
        UUID id = UUID.randomUUID();
        games.put(id, new ChessGame());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/game/{id}/status")
    ResponseEntity<StatusDTO> status(@PathVariable("id") UUID id) {
        var game = games.get(id);
        return Objects.nonNull(game)
                ? ResponseEntity.ok(transformer.toStatusInfo(game.getStatus()))
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/game/{id}/moves")
    ResponseEntity<PieceDTO> move(@PathVariable("id") UUID id, @RequestBody @Valid MoveDTO info) {
        var game = games.get(id);
        if (Objects.isNull(game)) {
            return ResponseEntity.notFound().build();
        }

        Point from = transformer.toPoint(info.from());
        Point to = transformer.toPoint(info.to());
        if (Objects.isNull(from) || Objects.isNull(to)) {
            return ResponseEntity.badRequest().build();
        }

        ChessGameStatus status = game.moveTo(from, to);

        return game.at(to)
                   .filter(v -> v.piece().isPresent())
                   .map(transformer::toPieceInfo)
                   .map(body -> ResponseEntity.ok()
                                              .header("black", status.blackStatus().toString())
                                              .header("white", status.whiteStatus().toString())
                                              .header("turn", status.turn().toString())
                                              .body(body))
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/game/{id}/pieces")
    ResponseEntity<List<PieceDTO>> allPieces(@PathVariable("id") UUID id) {
        var game = games.get(id);
        if (Objects.isNull(game)) {
            return ResponseEntity.notFound().build();
        }

        List<PieceDTO> result = game.notEmptySquares()
                                    .stream()
                                    .map(transformer::toPieceInfo)
                                    .sorted((o1, o2) -> POINTINFO_COMPARATOR.compare(o1.pointInfo(), o2.pointInfo()))
                                    .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/game/{id}/pieces/{chessPoint}")
    ResponseEntity<PieceDetailDTO> pieceDetail(@PathVariable("id") UUID id, @PathVariable String chessPoint) {
        var game = games.get(id);
        if (Objects.isNull(game)) {
            return ResponseEntity.notFound().build();
        }

        Point point = transformer.toPoint(chessPoint);
        if (Objects.isNull(point)) {
            return ResponseEntity.badRequest().build();
        }

        return game.at(point)
                   .filter(v -> v.piece().isPresent())
                   .map(square -> transformer.toPieceDetail(square, game.allowedMoves(point)))
                   .map(ResponseEntity::ok)
                   .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
