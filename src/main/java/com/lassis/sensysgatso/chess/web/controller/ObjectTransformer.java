package com.lassis.sensysgatso.chess.web.controller;

import com.lassis.sensysgatso.chess.model.ChessGameStatus;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import com.lassis.sensysgatso.chess.model.Square;
import com.lassis.sensysgatso.chess.web.controller.model.PieceDetailInfo;
import com.lassis.sensysgatso.chess.web.controller.model.PieceInfo;
import com.lassis.sensysgatso.chess.web.controller.model.PointInfo;
import com.lassis.sensysgatso.chess.web.controller.model.StatusInfo;
import com.lassis.sensysgatso.chess.web.controller.model.StatusPieceInfo;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;

@Component
public class ObjectTransformer {
    public static final Comparator<PointInfo> POINTINFO_COMPARATOR = Comparator.comparing(PointInfo::row).reversed().thenComparing(PointInfo::column);
    private static final char[] CHESS_COLUMNS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    private static final int[] CHESS_LINES = new int[]{8, 7, 6, 5, 4, 3, 2, 1};

    public PieceDetailInfo toPieceDetail(Point point, Piece piece, Collection<Point> allowedPoints) {
        PieceInfo pieceInfo = toPieceInfo(piece, point);
        List<PointInfo> allowedMoves = allowedPoints.stream()
                .map(this::toPointInfo)
                .sorted(POINTINFO_COMPARATOR)
                .collect(Collectors.toList());

        return new PieceDetailInfo(pieceInfo, allowedMoves);
    }

    public PieceInfo toPieceInfo(Square square) {
        Point point = square.point();
        Piece piece = square.piece();
        return toPieceInfo(piece, point);
    }

    public PieceInfo toPieceInfo(Piece piece, Point point) {
        return PieceInfo.builder()
                .color(piece.getColor())
                .pointInfo(toPointInfo(point))
                .type(getPieceName(piece))
                .build();
    }

    public PointInfo toPointInfo(Point point){
        return new PointInfo(toChessPoint(point), point.row(), point.column());
    }

    public Point toPoint(String chessPoint) {
        if (Objects.isNull(chessPoint) || chessPoint.length() != 2) {
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
