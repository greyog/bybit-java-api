package com.bybit.api.client.domain.market.response.instrumentInfo;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LeverageFilter {
    private BigDecimal minLeverage;
    private BigDecimal maxLeverage;
    private BigDecimal leverageStep;
}
