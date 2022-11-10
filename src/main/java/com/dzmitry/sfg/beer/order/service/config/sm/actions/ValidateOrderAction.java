package com.dzmitry.sfg.beer.order.service.config.sm.actions;

import com.dzmitry.sfg.beer.order.service.config.JmsConfig;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrder;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrderEventEnum;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import com.dzmitry.sfg.beer.order.service.repositories.BeerOrderRepository;
import com.dzmitry.sfg.beer.order.service.services.BeerOrderManagerImpl;
import com.dzmitry.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import com.dzmitry.sfg.brewery.model.events.ValidateOrderRequest;
import org.springframework.jms.core.JmsTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jsmTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        String beerOrderId = (String) stateContext.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        BeerOrder beerOrder = beerOrderRepository.findOneById(UUID.fromString(beerOrderId));

        jsmTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_QUEUE, ValidateOrderRequest.builder()
                .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                .build());

        log.debug("Send Validation request to queue for order id " + beerOrderId);
    }
}
