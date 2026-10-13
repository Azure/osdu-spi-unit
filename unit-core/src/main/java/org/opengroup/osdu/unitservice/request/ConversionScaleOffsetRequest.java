/**
 * Copyright 2018 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.unitservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
 *  ConversionScaleOffsetRequest is class which encapsulates the request body for a scale offset conversion object.
 */
@Data
@Schema(description = "A scale offset unit conversion request. The requested units are either passed as persistable reference strings (JSON serialized Unit 'essence') or as Unit essence structure.")
public class ConversionScaleOffsetRequest {
	@Schema(description = "The essence of a unit parameterization",implementation = UnitEssenceImpl.class)
	private UnitEssenceImpl fromUnitEssence;

	@Schema(description = "The essence of a unit parameterization",implementation = UnitEssenceImpl.class)
	private UnitEssenceImpl toUnitEssence;
	@Schema(description = "The persistable reference string (JSON serialized Unit 'essence') representing toe 'from Unit'. Either 'fromUnitPersistableReference' or 'fromUnit' must be populated.",type = "string")
	private String fromUnitPersistableReference;
	@Schema(description = "The persistable reference string (JSON serialized Unit 'essence') representing toe 'to Unit'. Either 'toUnitPersistableReference' or 'toUnit' must be populated.",type = "string")
	private String toUnitPersistableReference;

	/**
	 * Constructor
	 */
    public ConversionScaleOffsetRequest()
    {
    }

	/**
	 * Constructor
	 */
    public ConversionScaleOffsetRequest(UnitEssenceImpl fromUnitEssence, UnitEssenceImpl toUnitEssence,
										String fromUnitPersistableReference, String toUnitPersistableReference)
    {
		this.fromUnitEssence = fromUnitEssence;
		this.toUnitEssence   = toUnitEssence;
		this.fromUnitPersistableReference = fromUnitPersistableReference;
		this.toUnitPersistableReference = toUnitPersistableReference;
	}
	
	/**
	 * get the from unit essence
	 * @return the from unit essence
	 */
	@JsonProperty("fromUnit")
	public UnitEssenceImpl getFromUnitEssence() {
		UnitRequest request = new UnitRequest(fromUnitEssence, fromUnitPersistableReference);
		return request.getUnitEssence();
	}

	/**
	 * get the from unit essence
	 * @return the from unit essence
	 */
	@JsonProperty("toUnit")
	public UnitEssenceImpl getToUnitEssence() {
		UnitRequest request = new UnitRequest(toUnitEssence, toUnitPersistableReference);
		return request.getUnitEssence();
	}

	/**
	 * Get the from fromUnitPersistableReference
	 * @return fromUnitPersistableReference
	 */
	@JsonProperty("fromUnitPersistableReference")
	public String getFromUnitPersistableReference() {
		return this.fromUnitPersistableReference;
	}
	/**
	 * Get the from toUnitPersistableReference
	 * @return toUnitPersistableReference
	 */
	@JsonProperty("toUnitPersistableReference")
	public String getToUnitPersistableReference() {
		return this.toUnitPersistableReference;
	}
}

