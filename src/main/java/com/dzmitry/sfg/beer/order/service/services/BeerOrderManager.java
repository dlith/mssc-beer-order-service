package com.dzmitry.sfg.beer.order.service.services;

import com.dzmitry.sfg.beer.order.service.domain.BeerOrder;
import com.dzmitry.sfg.brewery.model.BeerOrderDto;

import java.util.UUID;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);

    void processValidationResult(UUID beerOrderId, Boolean isValid);
    void beerOrderAllocationPassed(BeerOrderDto beerOrder);

    void beerOrderAllocationPendingInventory(BeerOrderDto beerOrder);

    void beerOrderAllocationFailed(BeerOrderDto beerOrder);
    void beerOrderPickedUp(UUID id);
}
