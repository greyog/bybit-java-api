package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.loan.request.CryptoLoanAdjustLtvRequest;
import com.bybit.api.client.domain.loan.request.CryptoLoanBorrowRequest;
import com.bybit.api.client.domain.loan.request.CryptoLoanDataRequest;
import com.bybit.api.client.domain.loan.request.CryptoLoanRepayRequest;

public interface BybitApiLoanRestClient {
    GenericResponse<?> getCollateralCoins(CryptoLoanDataRequest request);
    GenericResponse<?> getBorrowableCoins(CryptoLoanDataRequest request);
    GenericResponse<?> getAcctMortgageLoanLimit(CryptoLoanDataRequest request);
    GenericResponse<?> borrow(CryptoLoanBorrowRequest borrowRequest);
    GenericResponse<?> repay(CryptoLoanRepayRequest repayRequest);
    GenericResponse<?> adjustCollateralAmount(CryptoLoanAdjustLtvRequest adjustLtvRequest);
    GenericResponse<?> getUnpaidOrders(CryptoLoanDataRequest request);
    GenericResponse<?> getCompletedOrders(CryptoLoanDataRequest request);
    GenericResponse<?> getRepayTransactions(CryptoLoanDataRequest request);
    GenericResponse<?> getLtvAdjustmentHistory(CryptoLoanDataRequest request);
    GenericResponse<?> getMaxReduceAmount(CryptoLoanDataRequest request);
}
