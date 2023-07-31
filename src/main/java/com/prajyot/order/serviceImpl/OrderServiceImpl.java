package com.prajyot.order.serviceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.prajyot.order.dto.AllOrderOutDTO;
import com.prajyot.order.dto.InventoryResponseDTO;
import com.prajyot.order.dto.OrderLineItemOutDTO;
import com.prajyot.order.dto.OrderLineItemsInDTO;
import com.prajyot.order.dto.OrderRequest;
import com.prajyot.order.entity.Order;
import com.prajyot.order.entity.OrderLineItems;
import com.prajyot.order.repository.OrderRepository;
import com.prajyot.order.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository repository;

	@Autowired
	private WebClient.Builder webClientBuilder;

	@Override
	public int placeOrder(OrderRequest request) {

		List<String> skuCodes = request.getOrders().stream().map(i -> i.getSkewCode()).toList();
		InventoryResponseDTO[] result = null;
		try {
			 result = webClientBuilder.build().get()
					.uri("http://inventory-service/api/inventory",
							uribuilder -> uribuilder.queryParam("skuCodes", skuCodes).build())
					.retrieve().bodyToMono(InventoryResponseDTO[].class).block();
		}catch(WebClientException ex){
			log.error("Service Unavailable......");
			return 1;
		} catch (Exception e) {
			log.error("Service Unavailable......");
			return 1;
		}
		log.info("Inventory present... " + Arrays.toString(result));
		boolean allProductInAvailable = areProductsAvailable(result, request);
		log.info("Inventory present... " + Arrays.toString(result));
		Order order = new Order();

		List<OrderLineItems> list = request.getOrders().stream().map(i -> mapToDTO(i, order)).toList();
		order.setOrders(list);

		if (allProductInAvailable) {
			repository.save(order);
			log.info("Order saved succesfully..");
			log.info("Order placed... ");
			return 0;
		} else {
			log.error("Product not in stock, try again later..");
			return 2;
		}

	}

	private OrderLineItems mapToDTO(OrderLineItemsInDTO inDTO, Order order) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setSkewCode(inDTO.getSkewCode());
		orderLineItems.setPrice(inDTO.getPrice());
		orderLineItems.setQty(inDTO.getQty());
		orderLineItems.setOrder(order);
		return orderLineItems;
	}

	private boolean areProductsAvailable(InventoryResponseDTO[] available, OrderRequest request) {
		List<InventoryResponseDTO> availableList = Arrays.asList(available);
		
		boolean[] list = new boolean[availableList.size()];
		List<InventoryResponseDTO> required = new ArrayList<>();
		
		request.getOrders().stream().forEach( i -> required.add(new InventoryResponseDTO(i.getSkewCode(), i.getQty()) ) );
		log.info(" "+ required);
		

		for (int i=0;i<required.size();i++) {
			boolean found = false;
			for(int j=0;j<availableList.size();j++) {
				InventoryResponseDTO avail = availableList.get(j);
				InventoryResponseDTO req = required.get(i); 
				if(req.getSkuCode().equals(avail.getSkuCode()) && req.getQty() <= avail.getQty() && list[j] == false) {
					list[j] = true;
					found = true; 
					}
			}
			if(!found)
				return found;
		}

	return true;

	}

	@Override
	public void deleteOrder(Long orderId) {
		log.info("Attempting delete order...");
		repository.deleteById(orderId);
		
	}

	@Override
	public void deleteAllOrder() {
		
		log.info("deleteing all records ....... ");
		repository.deleteAll();
		
	}

	@Override
	public List<AllOrderOutDTO> getAllOrders() {
		List<AllOrderOutDTO> list = repository.findAll().stream().map(this::mapTOAllOrderDTO).toList();
		return list;
	}

	private AllOrderOutDTO mapTOAllOrderDTO(Order order) {
		return new AllOrderOutDTO(order.getId(), order.getOrder_no(), getOrderLineItemOutDTOList(order.getOrders()));
	}

	private List<OrderLineItemOutDTO> getOrderLineItemOutDTOList(List<OrderLineItems> list) {
		List<OrderLineItemOutDTO> output = new ArrayList<>();
		for (OrderLineItems orderLineItemOutDTO : list) {
			OrderLineItemOutDTO dto = new OrderLineItemOutDTO();
			dto.setId(orderLineItemOutDTO.getId());
			dto.setSkewCode(orderLineItemOutDTO.getSkewCode());
			dto.setPrice(orderLineItemOutDTO.getPrice());
			dto.setQty(orderLineItemOutDTO.getQty());
			output.add(dto);
		}
		return output;
	}

}
