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
import org.opengroup.osdu.unitservice.model.MeasurementEssenceImpl;

/**
 *  MeasurementRequest is class which encapsulates the request body for a Measurement.
 */
@Data
@Schema(description = "Request to get a specific measurement given a persistable reference string or measurement essence structure.")
public class MeasurementRequest {
	@Schema(description = "The essence of a measurement definition")
	private MeasurementEssenceImpl measurementEssence;
	@Schema(type = "string")
	private String measurementEssenceJson;

	/**
	 * Constructor
	 */
    public MeasurementRequest()
    {
    }

	/**
	 * Constructor
	 */
    public MeasurementRequest(MeasurementEssenceImpl measurementEssence, String measurementEssenceJson)
    {
		this.measurementEssence = measurementEssence;
		this.measurementEssenceJson = measurementEssenceJson;
	}

	/**
	 * get the measurement essence
	 * @return the measurement essence
	 */
	@JsonProperty("essence")
	public MeasurementEssenceImpl getMeasurementEssence() {
		try {
			if (this.measurementEssence == null) {
				this.measurementEssence = Utility.fromJsonString(this.measurementEssenceJson, MeasurementEssenceImpl.class);
				if (this.measurementEssence != null) {
					if (!this.measurementEssence.getType().equals("UM") || this.measurementEssence.getAncestry() == null) return  null;
				}
			}
			return this.measurementEssence;
		} catch (JsonSyntaxException ex) {
			return null;
		}
	}

	/**
	 * get the measurement essence JSON
	 * @return the measurement essence as persistable reference string
	 */
	@JsonProperty("persistableReference")
	public String getMeasurementEssenceJson() {
		return this.measurementEssenceJson;
	}
}

