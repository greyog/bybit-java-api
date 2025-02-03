package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.loan.request.CryptoLoanAdjustLtvRequest;
import com.bybit.api.client.domain.loan.request.CryptoLoanBorrowRequest;
import com.bybit.api.client.domain.loan.request.CryptoLoanDataRequest;
import com.bybit.api.client.domain.loan.request.CryptoLoanRepayRequest;

public interface BybitApiAsyncLoanRestClient {
    void getCollateralCoins(CryptoLoanDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getBorrowableCoins(CryptoLoanDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAcctMortgageLoanLimit(CryptoLoanDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void borrow(CryptoLoanBorrowRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void repay(CryptoLoanRepayRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void adjustCollateralAmount(CryptoLoanAdjustLtvRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getUnpaidOrders(CryptoLoanDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getCompletedOrders(CryptoLoanDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getRepayTransactions(CryptoLoanDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getLtvAdjustmentHistory(CryptoLoanDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getMaxReduceAmount(CryptoLoanDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
}
