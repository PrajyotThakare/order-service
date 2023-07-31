package com.prajyot.order.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllOrderOutDTO {

	private Long id;
	private Long order_no;
	private List<OrderLineItemOutDTO> orders;
}
