package com.dzmitry.sfg.beer.order.service.services;

import com.dzmitry.sfg.beer.order.service.domain.BeerOrder;

public interface BeerOrderManager {

    BeerOrder newBeerOrder(BeerOrder beerOrder);
}
