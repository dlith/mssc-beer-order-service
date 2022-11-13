package com.dzmitry.sfg.beer.order.service.config.sm.actions;

import com.dzmitry.sfg.beer.order.service.config.JmsConfig;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrder;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrderEventEnum;
import com.dzmitry.sfg.beer.order.service.domain.BeerOrderStatusEnum;
import com.dzmitry.sfg.beer.order.service.repositories.BeerOrderRepository;
import com.dzmitry.sfg.beer.order.service.services.BeerOrderManagerImpl;
import com.dzmitry.sfg.beer.order.service.web.mappers.BeerOrderMapper;
import com.dzmitry.sfg.brewery.model.events.DeallocateOrderRequest;
import com.dzmitry.sfg.brewery.model.events.ValidateOrderRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeallocateOrderAction implements Action<BeerOrderStatusEnum, BeerOrderEventEnum> {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final JmsTemplate jmsTemplate;

    @Override
    public void execute(StateContext<BeerOrderStatusEnum, BeerOrderEventEnum> context) {
        String beerOrderId = (String) context.getMessage().getHeaders().get(BeerOrderManagerImpl.ORDER_ID_HEADER);
        Optional<BeerOrder> beerOrderOptional = beerOrderRepository.findById(UUID.fromString(beerOrderId));

        beerOrderOptional.ifPresentOrElse(beerOrder -> {
            jmsTemplate.convertAndSend(JmsConfig.DEALLOCATE_ORDER_QUEUE,
                    DeallocateOrderRequest.builder()
                            .beerOrderDto(beerOrderMapper.beerOrderToDto(beerOrder))
                            .build());
            log.debug("Sent Deallocation Request for order id: " + beerOrderId);
        }, () -> log.error("Beer Order Not Found!"));
    }
}