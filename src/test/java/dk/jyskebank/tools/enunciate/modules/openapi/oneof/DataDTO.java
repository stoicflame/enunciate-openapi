package dk.jyskebank.tools.enunciate.modules.openapi.oneof;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("oneOfSupport")
public class DataDTO {
	private Fruit fruit;
	private Pet pet;

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public Fruit getFruit() {
		return fruit;
	}

	public void setFruit(Fruit fruit) {
		this.fruit = fruit;
	}
}
