package com.prajyot.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemOutDTO {

	private Long id;
	private String skewCode;
	private Double price;
	private Integer qty;

}
