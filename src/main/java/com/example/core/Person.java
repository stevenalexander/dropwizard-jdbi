package com.example.core;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;

public class Person {

    @NotNull
    @JsonProperty
    private Integer id;

    @NotNull
    @JsonProperty
    private String name;

	public Person() {
		super();
	}

	public Person(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
        return id;
    }

    public Person setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;

        Person that = (Person) o;

        if (!getId().equals(that.getId())) return false;
        if (!getName().equals(that.getName())) return false;

        return true;
    }
}
