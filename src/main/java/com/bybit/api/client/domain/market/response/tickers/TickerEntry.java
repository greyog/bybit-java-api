package com.bybit.api.client.domain.market.response.tickers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickerEntry {
    @JsonProperty("ask1Price")
    public BigDecimal ask1Price;
    @JsonProperty("ask1Size")
    public BigDecimal ask1Size;
    @JsonProperty("basis")
    public String basis;
    @JsonProperty("basisRate")
    public String basisRate;
    @JsonProperty("bid1Price")
    public BigDecimal bid1Price;
    @JsonProperty("bid1Size")
    public BigDecimal bid1Size;
    @JsonProperty("curPreListingPhase")
    public String curPreListingPhase;
    @JsonProperty("deliveryFeeRate")
    public BigDecimal deliveryFeeRate;
    @JsonProperty("deliveryTime")
    public String deliveryTime;
    @JsonProperty("fundingRate")
    public BigDecimal fundingRate;
    @JsonProperty("highPrice24h")
    public BigDecimal highPrice24h;
    @JsonProperty("indexPrice")
    public BigDecimal indexPrice;
    @JsonProperty("lastPrice")
    public BigDecimal lastPrice;
    @JsonProperty("lowPrice24h")
    public BigDecimal lowPrice24h;
    @JsonProperty("markPrice")
    public BigDecimal markPrice;
    @JsonProperty("nextFundingTime")
    public String nextFundingTime;
    @JsonProperty("openInterest")
    public BigDecimal openInterest;
    @JsonProperty("openInterestValue")
    public BigDecimal openInterestValue;
    @JsonProperty("predictedDeliveryPrice")
    public BigDecimal predictedDeliveryPrice;
    @JsonProperty("preOpenPrice")
    public BigDecimal preOpenPrice;
    @JsonProperty("preQty")
    public BigDecimal preQty;
    @JsonProperty("prevPrice1h")
    public BigDecimal prevPrice1h;
    @JsonProperty("prevPrice24h")
    public BigDecimal prevPrice24h;
    @JsonProperty("price24hPcnt")
    public BigDecimal price24hPcnt;
    @JsonProperty("symbol")
    public String symbol;
    @JsonProperty("turnover24h")
    public BigDecimal turnover24h;
    @JsonProperty("volume24h")
    public BigDecimal volume24h;

}
