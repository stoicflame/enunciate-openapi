package dk.jyskebank.tools.enunciate.modules.openapi;

import java.util.List;

import com.webcohesion.enunciate.api.datatype.DataTypeReference;
import com.webcohesion.enunciate.api.datatype.DataTypeReference.ContainerType;

public class TypeHelper {
	private TypeHelper() {}
	
	/**
	 * Determine if the type should be rendered as a simple reference.
	 * 
	 * A simple reference type must have no other attributes at the $ref
	 * location. Avoid rendering other attributes if this call returns true.
	 *  
	 * @param dtr type to test
	 * @return true if the type should be rendered as a reference
	 */
	public static boolean renderAsSimpleRef(DataTypeReference dtr) {
	    List<ContainerType> containers = dtr.getContainers();
	    boolean hasContainers = containers != null && !containers.isEmpty();
		return dtr.getValue() != null && !hasContainers; 
	}
}
