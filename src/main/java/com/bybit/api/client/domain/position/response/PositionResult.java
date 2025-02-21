package com.bybit.api.client.domain.position.response;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.trade.response.OrderEntry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

/**
 * Trade order information.
 */
@JsonPropertyOrder()
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PositionResult {
    @JsonProperty("nextPageCursor")
    private String nextPageCursor;
    @JsonProperty("category")
    private CategoryType category;
    @JsonProperty("list")
    private List<PositionEntry> positionEntries;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PositionResult[nextPageCursor=").append(nextPageCursor)
                .append(", category=").append(category)
                .append(", positionEntries=[\n");

        for (PositionEntry entry : positionEntries) {
            builder.append("\t").append(entry.toString()).append(",\n");
        }

        builder.append("]]");

        return builder.toString();
    }
}
