package com.ars.orderservice.service;

public interface OutBoxService {
    void processOutBoxEvent();
}
