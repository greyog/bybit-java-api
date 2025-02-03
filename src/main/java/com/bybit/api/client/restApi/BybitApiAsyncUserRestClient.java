package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.preupgrade.PreUpgradeDataRequest;
import com.bybit.api.client.domain.user.UserDataRequest;

/**
 * Bybit API facade, supporting asynchronous/non-blocking access Bybit's REST API.
 */
public interface BybitApiAsyncUserRestClient {
    // Pre upgrade endpoints
    void getPreUpgradeOrderHistory(PreUpgradeDataRequest preupgradeOderHistoryRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getPreUpgradeTradeHistory(PreUpgradeDataRequest preUpgradeTradeHistoryRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getPreUpgradeClosePnl(PreUpgradeDataRequest preUpgradeClosePnlRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getPreUpgradeTransaction(PreUpgradeDataRequest preUpgradeTransactionRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getPreUpgradeOptionDelivery(PreUpgradeDataRequest preUpgradeOptionDeliveryRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getPreUpgradeUsdcSettlement(PreUpgradeDataRequest preUpgradeUsdcSettlementRequest, BybitApiCallback<GenericResponse<?>> callback);

    // User Data
    void createSubMember(UserDataRequest subUserRequest, BybitApiCallback<GenericResponse<?>> callback);
    void createSubAPI(UserDataRequest createApiKeyRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getSubUIDList(BybitApiCallback<GenericResponse<?>> callback);
    void freezeSubMember(UserDataRequest freezeSubUIDRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getCurrentAPIKeyInfo(BybitApiCallback<GenericResponse<?>> callback);
    void getUIDWalletType(UserDataRequest userDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void modifyMasterApiKey(UserDataRequest userDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void modifySubApiKey(UserDataRequest userDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void deleteMasterApiKey(BybitApiCallback<GenericResponse<?>> callback);
    void deleteSubApiKey(UserDataRequest userDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAffiliateUserInfo(UserDataRequest userDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getSubUIDListUnlimited(UserDataRequest subUserRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getSubUIDListUnlimited(BybitApiCallback<GenericResponse<?>> callback);
    void getSubAccAllAPIKeyInfo(UserDataRequest subUserRequest, BybitApiCallback<GenericResponse<?>> callback);
}
