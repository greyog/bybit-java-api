package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.institution.LendingDataRequest;
import com.bybit.api.client.domain.institution.clientLending.ClientLendingFundsRequest;
import com.bybit.api.client.domain.institution.clientLending.ClientLendingOrderRecordsRequest;

public interface BybitApiLendingRestClient {
    // Institution Endpoints
    GenericResponse<?> getInsProductInfo(LendingDataRequest lendingDataRequest);
    GenericResponse<?> getInsMarginCoinInfo(LendingDataRequest lendingDataRequest);
    GenericResponse<?> getInsLoanOrders(LendingDataRequest lendingDataRequest);
    GenericResponse<?> getInsRepayOrders(LendingDataRequest lendingDataRequest);
    GenericResponse<?> getInsLoanToValue();
    GenericResponse<?> updateInstitutionLoanUid(LendingDataRequest lendingDataRequest);
    // C2C Endpoints
/*    GenericResponse<?> getC2CLendingCoinInfo(LendingDataRequest lendingDataRequest);
    GenericResponse<?> C2cLendingDepositFunds(LendingDataRequest lendingDataRequest);
    GenericResponse<?> C2cLendingRedeemFunds(LendingDataRequest lendingDataRequest);
    GenericResponse<?> C2cLendingRedeemCancel(LendingDataRequest lendingDataRequest);
    GenericResponse<?> getC2cOrdersRecords(LendingDataRequest lendingDataRequest);
    GenericResponse<?> getC2CLendingAccountInfo(LendingDataRequest lendingDataRequest);*/
}
