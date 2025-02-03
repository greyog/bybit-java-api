package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.broker.request.BrokerDataRequest;

public interface BybitApiBrokerRestClient {
    // Broker endpoints
    GenericResponse<?> getBrokerEarningData(BrokerDataRequest brokerDataRequest);
    GenericResponse<?> getBrokerAccountInfo();
    GenericResponse<?> getSubAccountsDeposits(BrokerDataRequest brokerDataRequest);
    GenericResponse<?> getSubAccountsDeposits();
    GenericResponse<?> getVoucherSpec(BrokerDataRequest brokerDataRequest);
    GenericResponse<?> issueVoucher(BrokerDataRequest brokerDataRequest);
    GenericResponse<?> getIssuedVoucher(BrokerDataRequest brokerDataRequest);
}
