package com.bybit.api.client.domain.account.response.walletBalance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class WalletBalanceResult {

    @JsonProperty("list")
    private List<WalletBalanceEntry> tickerEntries;

}
