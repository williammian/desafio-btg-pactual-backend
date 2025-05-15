package br.com.wm.btgpactual.orderms.listener;

import static br.com.wm.btgpactual.orderms.config.RabbitMqConfig.ORDER_CREATED_QUEUE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import br.com.wm.btgpactual.orderms.listener.dto.OrderCreatedEvent;
import br.com.wm.btgpactual.orderms.service.OrderService;

@Component
public class OrderCreatedListener {

	private final Logger logger = LoggerFactory.getLogger(OrderCreatedListener.class);

	@Autowired
    private OrderService orderService;

    @RabbitListener(queues = ORDER_CREATED_QUEUE)
    public void listen(Message<OrderCreatedEvent> message){
        logger.info("Message consumed: {}", message);

        orderService.save(message.getPayload());
    }
	
}
