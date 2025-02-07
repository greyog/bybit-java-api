package com.bybit.api.client.domain.account.response.walletBalance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class Coin {

    @JsonProperty("availableToBorrow")
    public BigDecimal availableToBorrow;
    @JsonProperty("bonus")
    public BigDecimal bonus;
    @JsonProperty("accruedInterest")
    public BigDecimal accruedInterest;
    @JsonProperty("availableToWithdraw")
    public BigDecimal availableToWithdraw;
    @JsonProperty("totalOrderIM")
    public BigDecimal totalOrderIM;
    @JsonProperty("equity")
    public BigDecimal equity;
    @JsonProperty("totalPositionMM")
    public BigDecimal totalPositionMM;
    @JsonProperty("usdValue")
    public BigDecimal usdValue;
    @JsonProperty("unrealisedPnl")
    public BigDecimal unrealisedPnl;
    @JsonProperty("collateralSwitch")
    public Boolean collateralSwitch;
    @JsonProperty("spotHedgingQty")
    public BigDecimal spotHedgingQty;
    @JsonProperty("borrowAmount")
    public BigDecimal borrowAmount;
    @JsonProperty("totalPositionIM")
    public BigDecimal totalPositionIM;
    @JsonProperty("walletBalance")
    public BigDecimal walletBalance;
    @JsonProperty("cumRealisedPnl")
    public BigDecimal cumRealisedPnl;
    @JsonProperty("locked")
    public BigDecimal locked;
    @JsonProperty("marginCollateral")
    public Boolean marginCollateral;
    @JsonProperty("coin")
    public String coin;

}