/**
 * Copyright 2018 Schlumberger. All Rights Reserved.
 *
 */
package org.opengroup.osdu.unitservice.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.JsonSyntaxException;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.opengroup.osdu.unitservice.helper.Utility;
import org.opengroup.osdu.unitservice.model.UnitEssenceImpl;

/**
 *  UnitRequest is class which encapsulates the request body for a unit object.
 */
@Data
@Schema(description = "Request to get a single unit instance given an essence.")
public class UnitRequest {
	@Schema(description = "The essence of a unit parameterization")
	private UnitEssenceImpl unitEssence;
	@Schema(description = "The persistable reference string for the unit; optional, only one 'persistableReference' or 'essence' must be defined")
	private String persistableReference;

	/**
	 * Constructor
	 */
    public UnitRequest()
    {
    }

	/**
	 * Constructor
	 * @param unitEssence The {@link UnitEssenceImpl} or null
	 * @param persistableReference The persistable reference string or null
	 */
    public UnitRequest(UnitEssenceImpl unitEssence, String persistableReference)
    {
		this.unitEssence = unitEssence;
		this.persistableReference = persistableReference;
	}

	/**
	 * get the unit essence
	 * @return the unit essence
	 */
	@JsonProperty("essence")
	public UnitEssenceImpl getUnitEssence() {
		if (this.unitEssence == null) {
			try {
				this.unitEssence = Utility.fromJsonString(this.persistableReference, UnitEssenceImpl.class);
				if (this.unitEssence != null && !this.unitEssence.isValid()) return null;
			} catch (JsonSyntaxException ex) {
				this.unitEssence = null;
			}
		}
		return this.unitEssence;
	}

	/**
	 * Gets the essence as JSON string aka persistable reference
	 * @return the persistable reference string
	 */
	public String getPersistableReference() { return this.persistableReference; }
}

