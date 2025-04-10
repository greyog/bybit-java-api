package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.domain.position.request.BatchMovePositionRequest;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.position.response.PositionResult;

public interface BybitApiPositionRestClient {
    // Position Data
    GenericResponse<PositionResult> getPositionInfo(PositionDataRequest positionListRequest);
    Object setPositionLeverage(PositionDataRequest setLeverageRequest);
    Object swithMarginRequest(PositionDataRequest switchMarginRequest);
    Object switchPositionMode(PositionDataRequest switchPositionModeRequest);
    @Deprecated
    Object setTpslMode(PositionDataRequest setTpSlModeRequest);
    @Deprecated
    Object setRiskLimit(PositionDataRequest setRiskLimitRequest);
    GenericResponse<?> setTradingStop(PositionDataRequest tradingStopRequest);
    Object setAutoAddMargin(PositionDataRequest setAutoAddMarginRequest);
    Object modifyPositionMargin(PositionDataRequest modifyMarginRequest);
    Object getClosePnlList(PositionDataRequest closePnlHistoryRequest);
    Object getMovePositionHistory(PositionDataRequest movePositionHistoryRequest);
    Object batchMovePositions(BatchMovePositionRequest batchMovePositionRequest);
    Object confirmPositionRiskLimit(PositionDataRequest confirmNewRiskLimitRequest);
}
