package com.prajyot.order.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.prajyot.order.dto.AllOrderOutDTO;
import com.prajyot.order.dto.OrderRequest;

@Service
public interface OrderService {

	int placeOrder(OrderRequest request);

	void deleteOrder(Long orderId);

	void deleteAllOrder();

	List<AllOrderOutDTO> getAllOrders();

}
