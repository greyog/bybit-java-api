package com.bybit.api.client.domain.position.response;

import com.bybit.api.client.domain.trade.PositionIdx;
import com.bybit.api.client.domain.trade.Side;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.math.BigDecimal;

@JsonPropertyOrder()
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PositionEntry {
    @JsonProperty("adlRankIndicator")
    private int adlRankIndicator;
    @JsonProperty("autoAddMargin")
    private int autoAddMargin;
    @JsonProperty("avgPrice")
    private BigDecimal avgPrice;
    @JsonProperty("bustPrice")
    private BigDecimal bustPrice;
    @JsonProperty("createdTime")
    private String createdTime;
    @JsonProperty("cumRealisedPnl")
    private BigDecimal cumRealisedPnl;
    @JsonProperty("curRealisedPnl")
    private BigDecimal curRealisedPnl;
    @JsonProperty("delta")
    private BigDecimal delta;
    @JsonProperty("gamma")
    private BigDecimal gamma;
    @JsonProperty("isReduceOnly")
    private boolean isReduceOnly;
    @JsonProperty("leverage")
    private BigDecimal leverage;
    @JsonProperty("leverageSysUpdatedTime")
    private String leverageSysUpdatedTime;
    @JsonProperty("liqPrice")
    private BigDecimal liqPrice;
    @JsonProperty("markPrice")
    private BigDecimal markPrice;
    @JsonProperty("mmrSysUpdatedTime")
    private String mmrSysUpdatedTime;
    @JsonProperty("positionBalance")
    private BigDecimal positionBalance;
    @JsonProperty("positionIdx")
    private PositionIdx positionIdx;
    @JsonProperty("positionIM")
    private BigDecimal positionIM;
    @JsonProperty("positionMM")
    private BigDecimal positionMM;
    @JsonProperty("positionStatus")
    private String positionStatus;
    @JsonProperty("positionValue")
    private BigDecimal positionValue;
    @JsonProperty("riskId")
    private int riskId;
    @JsonProperty("riskLimitValue")
    private BigDecimal riskLimitValue;
    @JsonProperty("seq")
    private long seq;
    @JsonProperty("sessionAvgPrice")
    private BigDecimal sessionAvgPrice;
    @JsonProperty("side")
    private Side side;
    @JsonProperty("size")
    private BigDecimal size;
    @JsonProperty("stopLoss")
    private BigDecimal stopLoss;
    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("takeProfit")
    private BigDecimal takeProfit;
    @JsonProperty("theta")
    private BigDecimal theta;
    @JsonProperty("tpslMode")
    private String tpslMode;
    @JsonProperty("tradeMode")
    private int tradeMode;
    @JsonProperty("trailingStop")
    private String trailingStop;
    @JsonProperty("unrealisedPnl")
    private BigDecimal unrealisedPnl;
    @JsonProperty("updatedTime")
    private String updatedTime;
    @JsonProperty("vega")
    private BigDecimal vega;
}
