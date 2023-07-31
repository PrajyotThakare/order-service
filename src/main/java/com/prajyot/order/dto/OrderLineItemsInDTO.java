package com.prajyot.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderLineItemsInDTO  {
	private Long id;
	private String skewCode;
	private Double price;
	private Integer qty;
}
