package com.dzmitry.sfg.beer.order.service.services;

import com.dzmitry.sfg.brewery.model.CustomerPagedList;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerPagedList listCustomers(Pageable pageable);

}
