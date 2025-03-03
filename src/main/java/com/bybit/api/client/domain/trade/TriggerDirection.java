package com.bybit.api.client.domain.trade;

import lombok.Getter;

/**
 * Position index. Used to identify positions in different position modes
 */
@Getter
public enum TriggerDirection {
    RISE_TO_TRIGGER_PRICE(1),
    FALL_TO_TRIGGER_PRICE(2);

    private final int index;

    TriggerDirection(int index) {
        this.index = index;
    }

    public static TriggerDirection fromInt(Integer index) {
        if (index == null) {
            return null;
        }
        switch (index) {
            case 1 : return RISE_TO_TRIGGER_PRICE;
            case 2 : return FALL_TO_TRIGGER_PRICE;
        }
        return null;
    }
}
