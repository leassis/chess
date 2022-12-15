package com.lassis.sensysgatso.chess.web.controller;

import com.lassis.sensysgatso.chess.model.ChessGameStatus;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import com.lassis.sensysgatso.chess.model.Square;
import com.lassis.sensysgatso.chess.web.controller.model.PieceDetail;
import com.lassis.sensysgatso.chess.web.controller.model.PieceInfo;
import com.lassis.sensysgatso.chess.web.controller.model.StatusInfo;
import com.lassis.sensysgatso.chess.web.controller.model.StatusPieceInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;

@Component
public class ObjectTransformer {
    private static final char[] CHESS_COLUMNS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    private static final int[] CHESS_LINES = new int[]{8, 7, 6, 5, 4, 3, 2, 1};

    public PieceDetail toPieceDetail(Point point, Piece piece, Collection<Point> allowedPoints) {
        PieceInfo pieceInfo = toPieceInfo(piece, point);
        Set<String> allowedMoves = allowedPoints.stream()
                .map(this::toChessPoint)
                .collect(Collectors.toSet());

        return PieceDetail.builder()
                .pieceInfo(pieceInfo)
                .points(allowedMoves)
                .build();
    }

    public PieceInfo toPieceInfo(Square square) {
        Point point = square.point();
        Piece piece = square.piece();
        return toPieceInfo(piece, point);
    }

    public PieceInfo toPieceInfo(Piece piece, Point point) {
        return PieceInfo.builder()
                .color(piece.getColor())
                .row(point.row())
                .column(point.column())
                .type(getPieceName(piece))
                .id(toChessPoint(point.row(), point.column()))
                .build();
    }

    public Point toPoint(String chessPoint) {
        if (Objects.isNull(chessPoint) || chessPoint.length() < 2) {
            return null;
        }

        chessPoint = chessPoint.toUpperCase(Locale.ROOT);
        int row = Math.abs(chessPoint.charAt(1) - '8');
        int column = chessPoint.charAt(0) - 'A';

        return new Point(row, column);
    }

    public String toChessPoint(int row, int column) {
        return CHESS_COLUMNS[column] + "" + CHESS_LINES[row];
    }

    public String toChessPoint(Point point) {
        return toChessPoint(point.row(), point.column());
    }

    public StatusInfo toStatusInfo(ChessGameStatus status) {
        return status.deleted().stream()
                .map(v -> new StatusPieceInfo(v.getColor(), getPieceName(v)))
                .collect(collectingAndThen(Collectors.toSet(), coll -> toStatusInfo(status, coll)));
    }

    private static StatusInfo toStatusInfo(ChessGameStatus status, Set<StatusPieceInfo> statuses) {
        return new StatusInfo(
                status.whiteStatus().toString(),
                status.blackStatus().toString(),
                status.turn().toString(),
                statuses);
    }


    private static String getPieceName(Piece piece) {
        return piece.getClass().getSimpleName().toUpperCase(Locale.ENGLISH);
    }
}
