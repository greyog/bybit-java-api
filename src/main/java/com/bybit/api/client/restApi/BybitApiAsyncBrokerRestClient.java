package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.broker.request.BrokerDataRequest;

public interface BybitApiAsyncBrokerRestClient {
    // Broker endpoints
    void getBrokerEarningData(BrokerDataRequest brokerDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getBrokerAccountInfo(BybitApiCallback<GenericResponse<?>> callback);
    void getSubAccountsDeposits(BrokerDataRequest brokerDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getSubAccountsDeposits(BybitApiCallback<GenericResponse<?>> callback);
    void getVoucherSpec(BrokerDataRequest brokerDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void issueVoucher(BrokerDataRequest brokerDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getIssuedVoucher(BrokerDataRequest brokerDataRequest, BybitApiCallback<GenericResponse<?>> callback);
}
