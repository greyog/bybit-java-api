package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.asset.request.AssetDataRequest;

public interface BybitApiAssetRestClient {
    // Asset Endpoints
    GenericResponse<?> getAssetCoinExchangeRecords(AssetDataRequest coinExchangeRecordsRequest);
    GenericResponse<?> getAssetDeliveryRecords(AssetDataRequest deliveryRecordsRequest);
    GenericResponse<?> getAssetUSDCSettlementRecords(AssetDataRequest usdcSettlementRequest);
    GenericResponse<?> getAssetInfo(AssetDataRequest assetInfoRequest);
    GenericResponse<?> getAssetAllCoinsBalance(AssetDataRequest allCoinsBalanceRequest);
    GenericResponse<?> getAssetTransferableCoins(AssetDataRequest request);
    GenericResponse<?> getAssetSingleCoinBalance(AssetDataRequest singleCoinBalanceRequest);
    GenericResponse<?> createAssetInternalTransfer(AssetDataRequest assetInternalTransferRequest);
    GenericResponse<?> getAssetTransferSubUidList();
    GenericResponse<?> createAssetUniversalTransfer(AssetDataRequest assetUniversalTransferRequest);
    GenericResponse<?> getAssetInternalTransferRecords(AssetDataRequest internalTransferRequest);
    GenericResponse<?> getAssetUniversalTransferRecords(AssetDataRequest universalTransferRequest);
    GenericResponse<?> getAssetAllowedDepositCoinInfo(AssetDataRequest allowedDepositCoinRequest);
    GenericResponse<?> setAssetDepositAccount(AssetDataRequest request);
    GenericResponse<?> getAssetDepositRecords(AssetDataRequest assetDepositRecordsRequest);
    GenericResponse<?> getAssetSubMembersDepositRecords(AssetDataRequest assetDepositRecordsRequest);
    GenericResponse<?> getAssetInternalDepositRecords(AssetDataRequest assetDepositRecordsRequest);
    GenericResponse<?> getAssetMasterDepositAddress(AssetDataRequest masterDepositRequest);
    GenericResponse<?> getAssetSubMemberDepositAddress(AssetDataRequest subDepositRequest);
    GenericResponse<?> getAssetCoinInfo(AssetDataRequest request);
    GenericResponse<?> getAssetWithdrawalAmount(AssetDataRequest request);
    GenericResponse<?> getAssetWithdrawalRecords(AssetDataRequest assetWithdrawRecordsRequest);
    GenericResponse<?> cancelAssetWithdraw(AssetDataRequest request);
    GenericResponse<?> createAssetWithdraw(AssetDataRequest assetWithdrawRequest);
    // convert coin endpoints
    GenericResponse<?> requestQuote(AssetDataRequest assetQuoteRequest);
    GenericResponse<?> confirmQuote(String quoteTxId);
    GenericResponse<?> confirmQuote(AssetDataRequest assetQuoteRequest);
    GenericResponse<?> getConvertCoinList(AssetDataRequest request);
    GenericResponse<?> getConvertCoinStatus(AssetDataRequest request);
    GenericResponse<?> getConvertCoinHistory(AssetDataRequest request);
}
