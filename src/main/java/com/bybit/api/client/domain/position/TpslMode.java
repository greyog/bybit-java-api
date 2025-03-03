package com.bybit.api.client.domain.position;

import com.bybit.api.client.domain.trade.TriggerDirection;
import lombok.Getter;

@Getter
public enum TpslMode {
    FULL("Full"),
    PARTIAL("Partial");

    private final String description;

    TpslMode(String description) {
        this.description = description;
    }

    public static TpslMode fromString(String s) {
        if (s == null) {
            return null;
        }
        return valueOf(s.toUpperCase());
    }
}
