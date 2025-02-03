package com.bybit.api.client.impl;

import com.bybit.api.client.restApi.BybitApiAssetRestClient;
import com.bybit.api.client.restApi.BybitApiService;
import com.bybit.api.client.domain.asset.request.AssetDataRequest;
import com.bybit.api.client.domain.asset.request.AssetCancelWithdrawRequest;
import com.bybit.api.client.domain.asset.request.SetAssetDepositAccountRequest;
import com.bybit.api.client.service.BybitJsonConverter;

import java.util.HashMap;
import java.util.Map;

import static com.bybit.api.client.service.BybitApiServiceGenerator.createService;
import static com.bybit.api.client.service.BybitApiServiceGenerator.executeSync;

public class BybitApiAssetRestClientImpl implements BybitApiAssetRestClient {
    private final BybitApiService bybitApiService;
    private final BybitJsonConverter converter = new BybitJsonConverter();

    public BybitApiAssetRestClientImpl(BybitApiService bybitApiService){
        this.bybitApiService = bybitApiService;
    }

    public BybitApiAssetRestClientImpl(String apiKey, String secret, String baseUrl, boolean debugMode, long recvWindow, String logOption) {
        bybitApiService = createService(BybitApiService.class, apiKey, secret, baseUrl, debugMode, recvWindow, logOption, "");
    }

    // Asset Endpoints
    @Override
    public GenericResponse<?> getAssetCoinExchangeRecords(AssetDataRequest coinExchangeRecordsRequest) {
        return executeSync(bybitApiService.getAssetCoinExchangeRecords(
                coinExchangeRecordsRequest.getFromCoin(),
                coinExchangeRecordsRequest.getToCoin(),
                coinExchangeRecordsRequest.getLimit(),
                coinExchangeRecordsRequest.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getAssetDeliveryRecords(AssetDataRequest deliveryRecordsRequest) {
        return executeSync(bybitApiService.getAssetDeliveryRecords(
                deliveryRecordsRequest.getCategory() == null ? null : deliveryRecordsRequest.getCategory().getCategoryTypeId(),
                deliveryRecordsRequest.getSymbol(),
                deliveryRecordsRequest.getExpDate(),
                deliveryRecordsRequest.getLimit(),
                deliveryRecordsRequest.getCursor())
        );
    }

    @Override
    public GenericResponse<?> getAssetUSDCSettlementRecords(AssetDataRequest usdcSettlementRequest) {
        return executeSync(bybitApiService.getAssetUSDCSettlementRecords(
                usdcSettlementRequest.getCategory() == null ? null : usdcSettlementRequest.getCategory().getCategoryTypeId(),
                usdcSettlementRequest.getSymbol(),
                usdcSettlementRequest.getLimit(),
                usdcSettlementRequest.getCursor())
        );
    }

    @Override
    public GenericResponse<?> getAssetInfo(AssetDataRequest assetInfoRequest) {
        return executeSync(bybitApiService.getAssetInfo(
                assetInfoRequest.getAccountType() == null ? null : assetInfoRequest.getAccountType().getAccountTypeValue(),
                assetInfoRequest.getCoin())
        );
    }

    @Override
    public GenericResponse<?> getAssetAllCoinsBalance(AssetDataRequest allCoinsBalanceRequest) {
        return executeSync(bybitApiService.getAssetAllCoinsBalance(
                allCoinsBalanceRequest.getAccountType() == null ? null : allCoinsBalanceRequest.getAccountType().getAccountTypeValue(),
                allCoinsBalanceRequest.getMemberId(),
                allCoinsBalanceRequest.getCoin(),
                allCoinsBalanceRequest.getWithBonus() == null ? null : String.valueOf(allCoinsBalanceRequest.getWithBonus().getValue()))
        );
    }

    @Override
    public GenericResponse<?> getAssetTransferableCoins(AssetDataRequest request) {
        return executeSync(bybitApiService.getAssetTransferableCoins(
                request.getFromAccountType() == null ? null : request.getFromAccountType().getAccountTypeValue(),
                request.getToAccountType() == null ? null : request.getToAccountType().getAccountTypeValue()));
    }

    @Override
    public GenericResponse<?> getAssetSingleCoinBalance(AssetDataRequest singleCoinBalanceRequest) {
        return executeSync(converter.getSingleCoinBalance(bybitApiService, singleCoinBalanceRequest)
        );
    }

    @Override
    public GenericResponse<?> createAssetInternalTransfer(AssetDataRequest assetInternalTransferRequest) {
        var request = converter.mapToAssetInternalTransferRequest(assetInternalTransferRequest);
        return executeSync(bybitApiService.createAssetInternalTransfer(request));
    }

    @Override
    public GenericResponse<?> getAssetTransferSubUidList() {
        return executeSync(bybitApiService.getAssetTransferSubUidList());
    }

    @Override
    public GenericResponse<?> createAssetUniversalTransfer(AssetDataRequest assetUniversalTransferRequest) {
        var request = converter.mapToAssetUniversalTransferRequest(assetUniversalTransferRequest);
        return executeSync(bybitApiService.createAssetUniversalTransfer(request));
    }

    @Override
    public GenericResponse<?> getAssetInternalTransferRecords(AssetDataRequest internalTransferRequest) {
        return executeSync(bybitApiService.getAssetInternalTransferRecords(
                internalTransferRequest.getTransferId(),
                internalTransferRequest.getCoin(),
                internalTransferRequest.getTransferStatus() == null ? null : internalTransferRequest.getTransferStatus().getStatus(),
                internalTransferRequest.getStartTime(),
                internalTransferRequest.getEndTime(),
                internalTransferRequest.getLimit(),
                internalTransferRequest.getCursor())
        );
    }

    @Override
    public GenericResponse<?> getAssetUniversalTransferRecords(AssetDataRequest universalTransferRequest) {
        return executeSync(bybitApiService.getAssetUniversalTransferRecords(
                universalTransferRequest.getTransferId(),
                universalTransferRequest.getCoin(),
                universalTransferRequest.getTransferStatus() == null ? null : universalTransferRequest.getTransferStatus().getStatus(),
                universalTransferRequest.getStartTime(),
                universalTransferRequest.getEndTime(),
                universalTransferRequest.getLimit(),
                universalTransferRequest.getCursor())
        );
    }

    @Override
    public GenericResponse<?> getAssetAllowedDepositCoinInfo(AssetDataRequest allowedDepositCoinRequest) {
        return executeSync(bybitApiService.getAssetAllowedDepositCoinInfo(
                allowedDepositCoinRequest.getCoin(),
                allowedDepositCoinRequest.getChain(),
                allowedDepositCoinRequest.getLimit(),
                allowedDepositCoinRequest.getCursor())
        );
    }

    @Override
    public GenericResponse<?> setAssetDepositAccount(AssetDataRequest request) {
        SetAssetDepositAccountRequest setAssetDepositAccountRequest = converter.mapToSetDepositAccountRequest(request);
        return executeSync(bybitApiService.setAssetDepositAccount(setAssetDepositAccountRequest));
    }

    @Override
    public GenericResponse<?> getAssetDepositRecords(AssetDataRequest assetDepositRecordsRequest) {
        return executeSync(bybitApiService.getAssetDepositRecords(
                assetDepositRecordsRequest.getCoin(),
                assetDepositRecordsRequest.getStartTime(),
                assetDepositRecordsRequest.getEndTime(),
                assetDepositRecordsRequest.getLimit(),
                assetDepositRecordsRequest.getCursor())
        );
    }

    @Override
    public GenericResponse<?> getAssetSubMembersDepositRecords(AssetDataRequest assetDepositRecordsRequest) {
        return executeSync(bybitApiService.getAssetSubMembersDepositRecords(
                assetDepositRecordsRequest.getSubMemberId(),
                assetDepositRecordsRequest.getCoin(),
                assetDepositRecordsRequest.getStartTime(),
                assetDepositRecordsRequest.getEndTime(),
                assetDepositRecordsRequest.getLimit(),
                assetDepositRecordsRequest.getCursor())
        );
    }

    @Override
    public GenericResponse<?> getAssetInternalDepositRecords(AssetDataRequest assetDepositRecordsRequest) {
        return executeSync(bybitApiService.getAssetInternalDepositRecords(
                assetDepositRecordsRequest.getCoin(),
                assetDepositRecordsRequest.getStartTime(),
                assetDepositRecordsRequest.getEndTime(),
                assetDepositRecordsRequest.getLimit(),
                assetDepositRecordsRequest.getCursor())
        );
    }

    @Override
    public GenericResponse<?> getAssetMasterDepositAddress(AssetDataRequest masterDepositRequest) {
        return executeSync(bybitApiService.getAssetMasterDepositAddress(
                masterDepositRequest.getCoin(),
                masterDepositRequest.getChainType()
        ));
    }

    @Override
    public GenericResponse<?> getAssetSubMemberDepositAddress(AssetDataRequest subDepositRequest) {
        return executeSync(bybitApiService.getAssetSubMemberDepositAddress(
                subDepositRequest.getCoin(),
                subDepositRequest.getChainType(),
                subDepositRequest.getSubMemberId()
        ));
    }

    @Override
    public GenericResponse<?> getAssetCoinInfo(AssetDataRequest request) {
        return executeSync(bybitApiService.getAssetCoinInfo(request.getCoin()));
    }

    @Override
    public GenericResponse<?> getAssetWithdrawalAmount(AssetDataRequest request) {
        return executeSync(bybitApiService.getAssetWithdrawalAmount(request.getCoin()));
    }

    @Override
    public GenericResponse<?> getAssetWithdrawalRecords(AssetDataRequest assetWithdrawRecordsRequest) {
        return executeSync(bybitApiService.getAssetWithdrawalRecords(
                assetWithdrawRecordsRequest.getWithdrawID(),
                assetWithdrawRecordsRequest.getCoin(),
                assetWithdrawRecordsRequest.getWithdrawType() == null ? null : assetWithdrawRecordsRequest.getWithdrawType().getValue(),
                assetWithdrawRecordsRequest.getStartTime(),
                assetWithdrawRecordsRequest.getEndTime(),
                assetWithdrawRecordsRequest.getLimit(),
                assetWithdrawRecordsRequest.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> cancelAssetWithdraw(AssetDataRequest request) {
        AssetCancelWithdrawRequest assetCancelWithdrawRequest = converter.mapToAssetCancelWithdrawRequest(request);
        return executeSync(bybitApiService.cancelAssetWithdraw(assetCancelWithdrawRequest));
    }

    @Override
    public GenericResponse<?> createAssetWithdraw(AssetDataRequest assetWithdrawRequest) {
        var request = converter.mapToAssetWithdrawRequest(assetWithdrawRequest);
        return executeSync(bybitApiService.createAssetWithdraw(request));
    }

    @Override
    public GenericResponse<?> requestQuote(AssetDataRequest assetQuoteRequest) {
        var request = converter.mapToAssetQuoteRequest(assetQuoteRequest);
        return executeSync(bybitApiService.requestQuote(request));
    }

    @Override
    public GenericResponse<?> confirmQuote(String quoteTxId) {
        Map<String, String> map = new HashMap<>();
        map.put("quoteTxId", quoteTxId);
        return executeSync(bybitApiService.confirmQuote(map));
    }

    @Override
    public GenericResponse<?> confirmQuote(AssetDataRequest assetQuoteRequest) {
        var request = converter.mapToAssetConfirmQuoteRequest(assetQuoteRequest);
        return executeSync(bybitApiService.confirmQuote(request));
    }

    @Override
    public GenericResponse<?> getConvertCoinList(AssetDataRequest request) {
        return executeSync(bybitApiService.getConvertCoinList(
                request.getCoin(),
                request.getSide(),
                request.getAccountType() == null ? null : request.getAccountType().getAccountTypeValue()));
    }

    @Override
    public GenericResponse<?> getConvertCoinStatus(AssetDataRequest request) {
        return executeSync(bybitApiService.getConvertCoinStatus(
                request.getQuoteTxId(),
                request.getAccountType() == null ? null : request.getAccountType().getAccountTypeValue()));
    }

    @Override
    public GenericResponse<?> getConvertCoinHistory(AssetDataRequest request) {
        return executeSync(bybitApiService.getConvertCoinHistory(
                request.getAccountType() == null ? null : request.getAccountType().getAccountTypeValue(),
                request.getIndex(),
                request.getLimit()
                ));
    }
}
