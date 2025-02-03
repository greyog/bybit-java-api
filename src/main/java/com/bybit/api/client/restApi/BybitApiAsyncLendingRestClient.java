package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.institution.LendingDataRequest;

public interface BybitApiAsyncLendingRestClient {
    // Institution Endpoints
    void getInsProductInfo(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getInsMarginCoinInfo(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getInsLoanOrders(LendingDataRequest institutionLoanOrdersRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getInsRepayOrders(LendingDataRequest institutionRepayOrdersRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getInsLoanToValue(BybitApiCallback<GenericResponse<?>> callback);
    void updateInstitutionLoanUid(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    // C2C Endpoints
/*    void getC2CLendingCoinInfo(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void C2cLendingDepositFunds(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void C2cLendingRedeemFunds(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void C2cLendingRedeemCancel(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getC2cOrdersRecords(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getC2CLendingAccountInfo(LendingDataRequest lendingDataRequest, BybitApiCallback<GenericResponse<?>> callback);*/
}
