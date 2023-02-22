package com.lassis.sensysgatso.chess.web.controller;

import com.lassis.sensysgatso.chess.model.ChessGameStatus;
import com.lassis.sensysgatso.chess.model.Piece;
import com.lassis.sensysgatso.chess.model.Point;
import com.lassis.sensysgatso.chess.model.Square;
import com.lassis.sensysgatso.chess.web.controller.model.PieceDTO;
import com.lassis.sensysgatso.chess.web.controller.model.PieceDetailDTO;
import com.lassis.sensysgatso.chess.web.controller.model.PointDTO;
import com.lassis.sensysgatso.chess.web.controller.model.StatusDTO;
import com.lassis.sensysgatso.chess.web.controller.model.StatusPieceDTO;
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
    public static final Comparator<PointDTO> POINTINFO_COMPARATOR = Comparator.comparing(PointDTO::row).reversed().thenComparing(PointDTO::column);
    private static final char[] CHESS_COLUMNS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    private static final int[] CHESS_LINES = new int[]{8, 7, 6, 5, 4, 3, 2, 1};

    public PieceDetailDTO toPieceDetail(Square square, Collection<Point> allowedPoints) {
        PieceDTO pieceInfo = toPieceInfo(square);
        List<PointDTO> allowedMoves = allowedPoints.stream()
                                                   .map(this::toPointInfo)
                                                   .sorted(POINTINFO_COMPARATOR)
                                                   .toList();

        return new PieceDetailDTO(pieceInfo, allowedMoves);
    }

    public PieceDTO toPieceInfo(Square square) {
        Point point = square.point();
        Piece piece = square.piece();
        return toPieceInfo(piece, point);
    }

    public PointDTO toPointInfo(Point point) {
        return new PointDTO(toChessPoint(point), point.row(), point.column());
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

    public StatusDTO toStatusInfo(ChessGameStatus status) {
        return status.deleted().stream()
                     .map(v -> new StatusPieceDTO(v.getColor(), v.name()))
                     .collect(collectingAndThen(Collectors.toSet(), coll -> toStatusInfo(status, coll)));
    }

    private static StatusDTO toStatusInfo(ChessGameStatus status, Set<StatusPieceDTO> statuses) {
        return new StatusDTO(
                status.whiteStatus().toString(),
                status.blackStatus().toString(),
                status.turn().toString(),
                statuses);
    }

    private PieceDTO toPieceInfo(Piece piece, Point point) {
        return PieceDTO.builder()
                       .color(piece.getColor())
                       .pointInfo(toPointInfo(point))
                       .type(piece.name())
                       .build();
    }


}
