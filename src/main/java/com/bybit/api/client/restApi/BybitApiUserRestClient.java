package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.preupgrade.PreUpgradeDataRequest;
import com.bybit.api.client.domain.user.UserDataRequest;

public interface BybitApiUserRestClient {
    // User Data
    GenericResponse<?> createSubMember(UserDataRequest subUserRequest);
    GenericResponse<?> createSubAPI(UserDataRequest createApiKeyRequest);
    GenericResponse<?> getSubUIDList();
    GenericResponse<?> freezeSubMember(UserDataRequest freezeSubUIDRequest);
    GenericResponse<?> getCurrentAPIKeyInfo();
    GenericResponse<?> getUIDWalletType(UserDataRequest userDataRequest);
    GenericResponse<?> modifyMasterApiKey(UserDataRequest userDataRequest);
    GenericResponse<?> modifySubApiKey(UserDataRequest userDataRequest);
    GenericResponse<?> deleteMasterApiKey();
    GenericResponse<?> deleteSubApiKey(UserDataRequest userDataRequest);
    GenericResponse<?> getAffiliateUserInfo(UserDataRequest userDataRequest);

    // Pre Upgrade
    GenericResponse<?> getPreUpgradeOrderHistory(PreUpgradeDataRequest preupgradeOderHistoryRequest);
    GenericResponse<?> getPreUpgradeTradeHistory(PreUpgradeDataRequest preUpgradeTradeHistoryRequest);
    GenericResponse<?> getPreUpgradeClosePnl(PreUpgradeDataRequest preUpgradeClosePnlRequest);
    GenericResponse<?> getPreUpgradeTransaction(PreUpgradeDataRequest preUpgradeTransactionRequest);
    GenericResponse<?> getPreUpgradeOptionDelivery(PreUpgradeDataRequest preUpgradeOptionDeliveryRequest);
    GenericResponse<?> getPreUpgradeUsdcSettlement(PreUpgradeDataRequest preUpgradeUsdcSettlementRequest);

    GenericResponse<?> getSubUIDListUnlimited(UserDataRequest subUserRequest);
    GenericResponse<?> getSubUIDListUnlimited();
    GenericResponse<?> getSubAccAllAPIKeyInfo(UserDataRequest subUserRequest);
}
