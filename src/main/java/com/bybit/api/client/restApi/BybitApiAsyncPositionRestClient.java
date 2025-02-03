package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.position.request.BatchMovePositionRequest;
import com.bybit.api.client.domain.position.request.PositionDataRequest;

public interface BybitApiAsyncPositionRestClient {
    // Position endpoints
    void getPositionInfo(PositionDataRequest positionListRequest, BybitApiCallback<GenericResponse<?>> callback);
    void setPositionLeverage(PositionDataRequest setLeverageRequest, BybitApiCallback<GenericResponse<?>> callback);
    void swithMarginRequest(PositionDataRequest switchMarginRequest, BybitApiCallback<GenericResponse<?>> callback);
    void switchPositionMode(PositionDataRequest switchPositionModeRequest, BybitApiCallback<GenericResponse<?>> callback);
    @Deprecated
    void setTpslMode(PositionDataRequest setTpSlModeRequest, BybitApiCallback<GenericResponse<?>> callback);
    @Deprecated
    void setRiskLimit(PositionDataRequest setRiskLimitRequest, BybitApiCallback<GenericResponse<?>> callback);
    void setTradingStop(PositionDataRequest tradingStopRequest, BybitApiCallback<GenericResponse<?>> callback);
    void setAutoAddMargin(PositionDataRequest setAutoAddMarginRequest, BybitApiCallback<GenericResponse<?>> callback);
    void modifyPositionMargin(PositionDataRequest modifyMarginRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getClosePnlList(PositionDataRequest closePnlHistoryRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getMovePositionHistory(PositionDataRequest movePositionHistoryRequest, BybitApiCallback<GenericResponse<?>> callback);
    void batchMovePositions(BatchMovePositionRequest batchMovePositionRequest, BybitApiCallback<GenericResponse<?>> callback);
    void confirmPositionRiskLimit(PositionDataRequest confirmNewRiskLimitRequest, BybitApiCallback<GenericResponse<?>> callback);
}
