package org.sbolstandard.core3.entity;

import java.net.URI;
import java.util.List;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.core3.util.RDFUtil;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.validation.IdentityValidator;
import org.sbolstandard.core3.validation.PropertyValidator;
import org.sbolstandard.core3.vocabulary.DataModel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class Participation extends Identified{
	/*private List<URI> roles=null;
	private URI participant=null;
	private URI higherOrderParticipant=null;*/
	
	protected  Participation(Model model,URI uri) throws SBOLGraphException
	{
		super(model, uri);
	}
	
	protected  Participation(Resource resource) throws SBOLGraphException
	{
		super(resource);
	}
	
	@NotEmpty(message = "{PARTICIPANT_ROLES_NOT_EMPTY}")
	public List<URI> getRoles() {
		return RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
	}
	
	public void setRoles(@NotEmpty(message = "{PARTICIPANT_ROLES_NOT_EMPTY}") List<URI> roles) throws SBOLGraphException {
		PropertyValidator.getValidator().validate(this, "setRoles", new Object[] {roles}, List.class);
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	public URI getParticipant() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.participant);	
	}

	public void setParticipant(URI participant) {
		RDFUtil.setProperty(resource, DataModel.Participation.participant, participant);
	}
	
	public URI getHigherOrderParticipant() throws SBOLGraphException {
		return IdentityValidator.getValidator().getPropertyAsURI(this.resource, DataModel.Participation.higherOrderParticipant);	
	}

	public void setHigherOrderParticipant(URI higherOrderParticipant) {
		RDFUtil.setProperty(resource, DataModel.Participation.higherOrderParticipant, higherOrderParticipant);
	}
	
	@Override
	public URI getResourceType() {
		return DataModel.Participation.uri;
	}
	
}