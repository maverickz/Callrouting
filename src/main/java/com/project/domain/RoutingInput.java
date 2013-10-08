package com.project.domain;

import java.util.ArrayList;
import java.util.List;

public class RoutingInput {
	
	private String message;
	private List<String> numbers = new ArrayList<String>();
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public List<String> getNumbers() {
		return numbers;
	}
	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	} 
	@Override
	public String toString() {
		return "Message =" + message + "Recipients=" + numbers.toString() + "]";
	}

}
