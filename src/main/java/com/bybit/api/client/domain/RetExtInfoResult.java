package com.bybit.api.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.util.List;

@JsonPropertyOrder()
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RetExtInfoResult {
    @JsonProperty("list")
    private List<RetExtInfoEntry> list;
}
