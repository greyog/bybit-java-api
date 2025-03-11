package com.bybit.api.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenericResponse<T> {
    @JsonProperty("retCode")
    private int retCode;

    @JsonProperty("retMsg")
    private String retMsg;

    @JsonProperty("result")
    private T result;

    @JsonProperty("retExtInfo")
    private RetExtInfoResult retExtInfo;

    @JsonProperty("time")
    private long time;
}
