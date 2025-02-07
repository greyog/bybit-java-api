package com.bybit.api.examples.http.async;

import com.bybit.api.client.domain.spot.SpotMarginDataRequest;
import com.bybit.api.client.domain.spot.SwitchStatus;
import com.bybit.api.client.service.BybitApiClientFactory;

public class UTASpotMarginAsyncExample {
    public static void main(String[] args) {
        var client = BybitApiClientFactory.newInstance(System.getenv("API_KEY"),
                System.getenv("API_SECRET")).newSpotMarginAsyncRestClient();

        // Get VIP Margin Data
        var utaMarginDataRequest = SpotMarginDataRequest.builder().build();
        client.getUtaVipSpotMarginTradeData(utaMarginDataRequest, System.out::println);

        // Toggle Margin Trade
        client.setUTASpotMarginTrade("0", System.out::println);

        // Set Leverage
        client.setUTASpotMarginTradeLeverage("2", System.out::println);

        // Set Status And Leverage
        client.getUTASpotMarginTradeLeverageState(System.out::println);
    }
}
