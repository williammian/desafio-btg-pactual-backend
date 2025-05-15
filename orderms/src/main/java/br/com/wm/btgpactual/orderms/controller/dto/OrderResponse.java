package br.com.wm.btgpactual.orderms.controller.dto;

import java.math.BigDecimal;

import br.com.wm.btgpactual.orderms.entity.OrderEntity;

public record OrderResponse(
		Long orderId,
        Long customerId,
        BigDecimal total) {

	public static OrderResponse fromEntity(OrderEntity entity) {
		return new OrderResponse(entity.getOrderId(), entity.getCustomerId(), entity.getTotal());
	}
}
