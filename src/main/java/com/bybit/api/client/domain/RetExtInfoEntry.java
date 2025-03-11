package com.bybit.api.client.domain;

import com.bybit.api.client.constant.BybitApiConstants;
import com.bybit.api.client.domain.trade.CancelType;
import com.bybit.api.client.domain.trade.OrderStatus;
import com.bybit.api.client.domain.trade.PositionIdx;
import com.bybit.api.client.domain.trade.RejectReason;
import com.bybit.api.client.domain.trade.Side;
import com.bybit.api.client.domain.trade.SmpType;
import com.bybit.api.client.domain.trade.StopOrderType;
import com.bybit.api.client.domain.trade.TimeInForce;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

@JsonPropertyOrder()
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RetExtInfoEntry {
    private Integer code;

    private String msg;
}
