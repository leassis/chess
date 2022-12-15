package com.lassis.chess.model;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Square {
    private final Board board;
    private final Point point;
    @EqualsAndHashCode.Exclude
    private Piece piece;

    public Set<Point> allowedMoves() {
        return piece().map(p -> p.allowedMoves(board, point)).orElseGet(Collections::emptySet);
    }

    public Optional<Piece> piece() {
        return Optional.ofNullable(piece);
    }

    public Point point() {
        return point;
    }

    public void piece(Piece piece) {this.piece = piece;}

}
