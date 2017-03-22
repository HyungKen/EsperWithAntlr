package com.espertech.esper.epl.dataclass;

import java.time.Instant;

public class FruitEvent {
	String name;
	int price;
	Instant timestamp;
	
	public FruitEvent(String name, int price, Instant timestamp){
		this.name = name;
		this.price = price;
		this.timestamp = timestamp;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public Instant getTimestamp()
	{
		return timestamp;
	}
}

