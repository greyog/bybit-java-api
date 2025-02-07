package com.bybit.api.examples.http.sync;

import com.bybit.api.client.config.BybitApiConfig;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.domain.account.AccountType;
import com.bybit.api.client.log.LogOption;
import com.bybit.api.client.service.BybitApiClientFactory;

public class AccountExample {
    public static void main(String[] args) {
        var client = BybitApiClientFactory.newInstance( System.getenv("API_KEY"),
                System.getenv("API_SECRET"),
                BybitApiConfig.DEMO_TRADING_DOMAIN,
                true,
                LogOption.OKHTTP3.getLogOptionType()
        ).newAccountRestClient();

        // Get wallet balance
        var walletBalanceRequest = AccountDataRequest.builder().accountType(AccountType.UNIFIED).build();
        var walletBalanceData = client.getWalletBalance(walletBalanceRequest);
        System.out.println(walletBalanceData);

        // Upgrade to uta
//        var upgradeToUTAResult = client.upgradeAccountToUTA();
//        System.out.println(upgradeToUTAResult);

//        // Get Borrow History
//        var accountBorrowHistoryRequest = AccountDataRequest.builder().build();
//        var accountBorrowData = client.getAccountBorrowHistory(accountBorrowHistoryRequest);
//        System.out.println(accountBorrowData);
//
//        // Get Account info
//        var accountInfo = client.getAccountInfo();
//        System.out.println(accountInfo);
//
//        // Get Coin Geek
//        var coinGeekRequest = AccountDataRequest.builder().baseCoin("BTC").build();
//        var coinGeeks = client.getAccountCoinGeeks(coinGeekRequest);
//        System.out.println(coinGeeks);
//
//        // Get Transaction Log
//        var transactionLogRequest = AccountDataRequest.builder().build();
//        var transactionLogData = client.getTransactionLog(transactionLogRequest);
//        System.out.println(transactionLogData);

    }
}
