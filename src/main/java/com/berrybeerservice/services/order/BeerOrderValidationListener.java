package com.berrybeerservice.services.order;

import com.berrybeerservice.config.JmsConfig;
import com.brewery.model.events.ValidateOrderRequest;
import com.brewery.model.events.ValidateOrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    private final BeerOrderValidator beerOrderValidator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateOrderRequest validateOrderRequest){
        Boolean isValid = beerOrderValidator.validateOrder(validateOrderRequest.getBeerOrderDto());

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE, ValidateOrderResult.builder().isValid(isValid)
                .orderId(validateOrderRequest.getBeerOrderDto().getId()).build());
    }
}
