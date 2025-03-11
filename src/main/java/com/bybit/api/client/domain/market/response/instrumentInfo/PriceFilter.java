package com.bybit.api.client.domain.market.response.instrumentInfo;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PriceFilter {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal tickSize;
}
