package com.phantoms.helper.user.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {

	private long orderId;
	private String userName;
	private long userId;
	private int amount;
	private int hashVal;
}
