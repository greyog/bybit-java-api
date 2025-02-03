package com.bybit.api.client.impl;

import com.bybit.api.client.restApi.BybitApiService;
import com.bybit.api.client.restApi.BybitApiUserRestClient;
import com.bybit.api.client.domain.preupgrade.PreUpgradeDataRequest;
import com.bybit.api.client.domain.user.UserDataRequest;
import com.bybit.api.client.domain.user.request.UserSubMemberRequest;
import com.bybit.api.client.service.BybitJsonConverter;

import static com.bybit.api.client.constant.Helper.listToString;
import static com.bybit.api.client.service.BybitApiServiceGenerator.createService;
import static com.bybit.api.client.service.BybitApiServiceGenerator.executeSync;

/**
 * Implementation of Bybit's REST API using Retrofit with synchronous/blocking
 * method calls.
 */
public class BybitApiUserRestClientImpl implements BybitApiUserRestClient {
    private final BybitApiService bybitApiService;
    private final BybitJsonConverter converter = new BybitJsonConverter();

    public BybitApiUserRestClientImpl(String apiKey, String secret, String baseUrl, boolean debugMode, long recvWindow, String logOption) {
        bybitApiService = createService(BybitApiService.class, apiKey, secret, baseUrl, debugMode, recvWindow, logOption, "");
    }

    // User endpoints
    @Override
    public GenericResponse<?> getCurrentAPIKeyInfo() {
        return executeSync(bybitApiService.getCurrentAPIKeyInfo());
    }

    @Override
    public GenericResponse<?> getSubUIDList() {
        return executeSync(bybitApiService.getSubUIDList());
    }

    @Override
    public GenericResponse<?> getSubUIDListUnlimited(UserDataRequest subUserRequest) {
        return executeSync(bybitApiService.getSubUIDListUnlimited(
                subUserRequest.getPageSize(),
                subUserRequest.getNextCursor()
        ));
    }

    @Override
    public GenericResponse<?> getSubUIDListUnlimited() {
        return executeSync(bybitApiService.getSubUIDListUnlimited());
    }

    @Override
    public GenericResponse<?> getSubAccAllAPIKeyInfo(UserDataRequest subUserRequest) {
        return executeSync(bybitApiService.getSubAccAllAPIKeyInfo(
                subUserRequest.getSubMemberId(),
                subUserRequest.getLimit(),
                subUserRequest.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> createSubMember(UserDataRequest request) {
        UserSubMemberRequest subUserRequest = converter.mapToCreateSubMemberRequest(request);
        return executeSync(bybitApiService.createSubMember(subUserRequest));
    }

    @Override
    public GenericResponse<?> createSubAPI(UserDataRequest request) {
        var createApiKeyRequest = converter.mapToCreateSubApiRequest(request);
        return executeSync(bybitApiService.createSubAPI(createApiKeyRequest));
    }

    @Override
    public GenericResponse<?> freezeSubMember(UserDataRequest request) {
        var freezeSubUIDRquest = converter.mapToFreezeSubApiRequest(request);
        return executeSync(bybitApiService.freezeSubMember(freezeSubUIDRquest));
    }

    @Override
    public GenericResponse<?> getUIDWalletType(UserDataRequest request) {
        return executeSync(bybitApiService.getUIDWalletType(request.getMemberIds() == null ? null : listToString(request.getMemberIds())));
    }

    @Override
    public GenericResponse<?> modifyMasterApiKey(UserDataRequest userDataRequest) {
        var modifyMasterApiKeyRequest = converter.mapToModifyApiKeyRequest(userDataRequest);
        return executeSync(bybitApiService.modifyMasterApiKey(modifyMasterApiKeyRequest));
    }

    @Override
    public GenericResponse<?> modifySubApiKey(UserDataRequest userDataRequest) {
        var modifySubApiKeyRequest = converter.mapToModifyApiKeyRequest(userDataRequest);
        return executeSync(bybitApiService.modifySubApiKey(modifySubApiKeyRequest));
    }

    @Override
    public GenericResponse<?> deleteMasterApiKey() {
        return executeSync(bybitApiService.deleteMasterApiKey());
    }

    @Override
    public GenericResponse<?> deleteSubApiKey(UserDataRequest userDataRequest) {
        var deleteSubApiKeyRequest = converter.mapToDeleteSubApiKeyRequest(userDataRequest);
        return executeSync(bybitApiService.deleteSubApiKey(deleteSubApiKeyRequest));
    }

    @Override
    public GenericResponse<?> getAffiliateUserInfo(UserDataRequest request) {
        return executeSync(bybitApiService.getAffiliateUserInfo(request.getUid()));
    }

    // Pre upgrade endpoints
    @Override
    public GenericResponse<?> getPreUpgradeClosePnl(PreUpgradeDataRequest request) {
        return executeSync(bybitApiService.getPreUpgradeClosePnl(
                request.getCategory().getCategoryTypeId(),
                request.getSymbol(),
                request.getStartTime(),
                request.getEndTime(),
                request.getLimit(),
                request.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getPreUpgradeOrderHistory(PreUpgradeDataRequest request) {
        return executeSync(bybitApiService.getPreUpgradeOrderHistory(
                request.getCategory().getCategoryTypeId(),
                request.getSymbol(),
                request.getBaseCoin(),
                request.getOrderId(),
                request.getOrderLinkId(),
                request.getOrderFilter(),
                request.getOrderStatus() == null ? null : request.getOrderStatus().getDescription(),
                request.getStartTime(),
                request.getEndTime(),
                request.getLimit(),
                request.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getPreUpgradeTradeHistory(PreUpgradeDataRequest request) {
        return executeSync(bybitApiService.getPreUpgradeTradeHistory(
                request.getCategory().getCategoryTypeId(),
                request.getSymbol(),
                request.getOrderId(),
                request.getOrderLinkId(),
                request.getBaseCoin(),
                request.getStartTime(),
                request.getEndTime(),
                request.getExecType() == null ? null : request.getExecType().getExecTypeId(),
                request.getLimit(),
                request.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getPreUpgradeTransaction(PreUpgradeDataRequest request) {
        return executeSync(bybitApiService.getPreUpgradeTransaction(
                request.getCategory().getCategoryTypeId(),
                request.getBaseCoin(),
                request.getTransactionType() == null ? null : request.getTransactionType().getTransactionTypeId(),
                request.getStartTime(),
                request.getEndTime(),
                request.getLimit(),
                request.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getPreUpgradeOptionDelivery(PreUpgradeDataRequest request) {
        return executeSync(bybitApiService.getPreUpgradeOptionDelivery(
                request.getCategory().getCategoryTypeId(),
                request.getSymbol(),
                request.getExpDate(),
                request.getLimit(),
                request.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getPreUpgradeUsdcSettlement(PreUpgradeDataRequest request) {
        return executeSync(bybitApiService.getPreUpgradeUsdcSettlement(
                request.getCategory().getCategoryTypeId(),
                request.getSymbol(),
                request.getLimit(),
                request.getCursor()
        ));
    }
}
