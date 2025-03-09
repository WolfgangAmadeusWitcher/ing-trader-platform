package com.example.jwtdemo.dto;

import lombok.Getter;

import java.util.Date;

public class GetOrdersBetweenDateRequest {

    @Getter
    private Date dateFrom;
    @Getter
    private Date dateTo;

    public GetOrdersBetweenDateRequest() {

    }

    public GetOrdersBetweenDateRequest(Date dateFrom, Date dateTo) {
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
