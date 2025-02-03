package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.position.request.BatchMovePositionRequest;
import com.bybit.api.client.domain.position.request.PositionDataRequest;

public interface BybitApiPositionRestClient {
    // Position Data
    GenericResponse<?> getPositionInfo(PositionDataRequest positionListRequest);
    GenericResponse<?> setPositionLeverage(PositionDataRequest setLeverageRequest);
    GenericResponse<?> swithMarginRequest(PositionDataRequest switchMarginRequest);
    GenericResponse<?> switchPositionMode(PositionDataRequest switchPositionModeRequest);
    @Deprecated
    GenericResponse<?> setTpslMode(PositionDataRequest setTpSlModeRequest);
    @Deprecated
    GenericResponse<?> setRiskLimit(PositionDataRequest setRiskLimitRequest);
    GenericResponse<?> setTradingStop(PositionDataRequest tradingStopRequest);
    GenericResponse<?> setAutoAddMargin(PositionDataRequest setAutoAddMarginRequest);
    GenericResponse<?> modifyPositionMargin(PositionDataRequest modifyMarginRequest);
    GenericResponse<?> getClosePnlList(PositionDataRequest closePnlHistoryRequest);
    GenericResponse<?> getMovePositionHistory(PositionDataRequest movePositionHistoryRequest);
    GenericResponse<?> batchMovePositions(BatchMovePositionRequest batchMovePositionRequest);
    GenericResponse<?> confirmPositionRiskLimit(PositionDataRequest confirmNewRiskLimitRequest);
}
