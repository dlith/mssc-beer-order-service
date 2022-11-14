package com.dzmitry.sfg.beer.order.service.web.mappers;

import com.dzmitry.sfg.beer.order.service.domain.Customer;
import com.dzmitry.sfg.brewery.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface CustomerMapper {
    CustomerDto customerToDto(Customer customer);

    Customer dtoToCustomer(Customer dto);
}
