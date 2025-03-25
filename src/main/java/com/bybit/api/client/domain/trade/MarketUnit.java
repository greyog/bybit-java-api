package com.bybit.api.client.domain.trade;

public enum MarketUnit {

    BASE_COIN("baseCoin"), // BTCUSDT, then "qty" unit is BTC
    QUOTE_COIN("quoteCoin"); // BTCUSDT, then "qty" unit is USDT

    private final String value;

    MarketUnit(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
