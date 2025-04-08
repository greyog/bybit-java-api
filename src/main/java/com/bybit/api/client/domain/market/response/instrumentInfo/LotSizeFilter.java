package com.bybit.api.client.domain.market.response.instrumentInfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
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
    private BigDecimal minNotionalValue;
}
