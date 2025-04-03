package model.interactionclass;

import skf.model.interaction.annotations.InteractionClass;
import skf.model.interaction.annotations.Parameter;
import skf.coder.HLAunicodeStringCoder;

@InteractionClass(name = "DeliveryRequest")
public class DeliveryRequest {
	@Parameter(name = "Request", coder = HLAunicodeStringCoder.class)
	public String Request = null;

	public DeliveryRequest() {
	}

	public DeliveryRequest(String Request) {

		this.Request = Request;
	}

	public String getRequest() {
		return this.Request;
	}

	public void setRequest(String Request) {
		this.Request = Request;
	}
}