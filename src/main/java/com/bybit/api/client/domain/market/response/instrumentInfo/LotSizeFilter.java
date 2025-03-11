package com.bybit.api.client.domain.market.response.instrumentInfo;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class LotSizeFilter {
    private BigDecimal maxOrderQty;
    private BigDecimal minOrderQty;
    private BigDecimal qtyStep;
    private BigDecimal postOnlyMaxOrderQty;
    private BigDecimal basePrecision;
    private BigDecimal quotePrecision;
    private BigDecimal minOrderAmt;
    private BigDecimal maxOrderAmt;
}
