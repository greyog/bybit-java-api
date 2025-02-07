package com.bybit.api.client.domain.account.response.walletBalance;

import com.bybit.api.client.domain.account.AccountType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
public class WalletBalanceEntry {

    @JsonProperty("totalEquity")
    public BigDecimal totalEquity;
    @JsonProperty("accountIMRate")
    public BigDecimal accountIMRate;
    @JsonProperty("totalMarginBalance")
    public BigDecimal totalMarginBalance;
    @JsonProperty("totalInitialMargin")
    public BigDecimal totalInitialMargin;
    @JsonProperty("accountType")
    public AccountType accountType;
    @JsonProperty("totalAvailableBalance")
    public BigDecimal totalAvailableBalance;
    @JsonProperty("accountMMRate")
    public BigDecimal accountMMRate;
    @JsonProperty("totalPerpUPL")
    public BigDecimal totalPerpUPL;
    @JsonProperty("totalWalletBalance")
    public BigDecimal totalWalletBalance;
    @JsonProperty("accountLTV")
    public BigDecimal accountLTV;
    @JsonProperty("totalMaintenanceMargin")
    public BigDecimal totalMaintenanceMargin;
    @JsonProperty("coin")
    public List<Coin> coin;

}
