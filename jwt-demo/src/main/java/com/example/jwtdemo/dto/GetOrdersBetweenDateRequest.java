package com.example.jwtdemo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;

import java.util.Date;

@Getter
public class GetOrdersBetweenDateRequest {

    @NotNull
    private Date dateFrom;
    @NotNull
    private Date dateTo;

    public GetOrdersBetweenDateRequest() {

    }

    public GetOrdersBetweenDateRequest(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
