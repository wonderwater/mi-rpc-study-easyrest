package com.citic.resteasy.controller.request;

public class Person {
	
	private String name;
	
	private Integer	id;

	public Person() {

	}

	public Person(int id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}
