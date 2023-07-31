package com.prajyot.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.prajyot.order.dto.AllOrderOutDTO;
import com.prajyot.order.dto.OrderRequest;
import com.prajyot.order.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

	@Autowired
	private OrderService service;
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<AllOrderOutDTO> getAllOrders(){
		log.info("Getting orders......");
		return service.getAllOrders();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public String placeOrder(@RequestBody OrderRequest request) {
		int returnCode = service.placeOrder(request);
		if(returnCode == 0) {
			log.info("Order placed successfully....!");
			return "Order placed successfully....!";
		} else {
			log.info("Order can not be placed....!");
			return "Order can not be placed....!";
		}
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public String deleteOrder(@PathVariable Long orderId) {
		service.deleteOrder(orderId);
		log.info("Deleting Order......");
		return "Order deleted successfully....!";
	}
	
	@DeleteMapping("/delete-all")
	@ResponseStatus(HttpStatus.OK)
	public String deleteAllOrder() {
		service.deleteAllOrder();
		log.info("Deleting all orders....!");
		return "Order deleted successfully....!";
	}
}
