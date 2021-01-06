package net.shyshkin.study.beerorderservice.services;

import net.shyshkin.study.beerorderservice.domain.BeerOrder;

import java.util.UUID;

public interface BeerOrderManager {

    String ORDER_ID_HEADER = "order_id";

    BeerOrder newBeerOrder(BeerOrder beerOrder);

    void processValidationResult(UUID orderId, boolean isValid);
}