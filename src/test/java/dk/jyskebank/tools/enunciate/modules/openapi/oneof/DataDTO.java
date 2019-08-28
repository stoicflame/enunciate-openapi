package dk.jyskebank.tools.enunciate.modules.openapi.oneof;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("oneOfSupport")
public class DataDTO {

   private Pet pet;

   public Pet getPet() {
      return pet;
   }

   public void setPet(Pet pet) {
      this.pet = pet;
   }
}
