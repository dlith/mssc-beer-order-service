package com.dzmitry.sfg.beer.order.service.config.sm.actions;

import com.dzmitry.sfg.beer.order.service.config.JmsConfig;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrder;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrderEventEnum;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import com.dzmitry.sfg.beer.order.service.repositories.BeerOrderRepository;
import com.dzmitry.sfg.beer.order.service.services.BeerOrderManagerImpl;
import com.dzmitry.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Component
public class AllocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final JmsTemplate jmsTemplate;
    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> stateContext) {
        String beerOrderId = (String) stateContext.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        BeerOrder beerOrder = beerOrderRepository.findOneById(UUID.fromString(beerOrderId));

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_QUEUE,
                beerOrderMapper.beerOrderToDto(beerOrder));

        log.debug("Sent Allocation Request for order id: " + beerOrderId);
    }
}
