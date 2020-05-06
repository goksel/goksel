package org.sbolstandard;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.Location.LocationBuilder;
import org.sbolstandard.Location.LocationFactory;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public class Participation extends Feature{
	private List<URI> roles=new ArrayList<URI>();
	private URI participant=null;

	protected  Participation(Model model,URI uri)
	{
		super(model, uri);
	}
	
	protected  Participation(Resource resource)
	{
		super(resource);
	}

	
	public List<URI> getRoles() {
		if (roles==null)
		{
			roles=RDFUtil.getPropertiesAsURIs(this.resource, DataModel.role);
		}
		return roles;
	}
	
	public void setRoles(List<URI> roles) {
		this.roles = roles;
		RDFUtil.setProperty(resource, DataModel.role, roles);
	}
	
	
	public URI getParticipant() throws SBOLGraphException {
		if (participant==null)
		{
			participant=RDFUtil.getPropertyAsURI(this.resource, DataModel.Participation.participant);	
		}
		return participant;
	}

	public void setParticipant(URI participant) {
		this.participant = participant;
		RDFUtil.setProperty(resource, DataModel.Participation.participant, participant);
	}

	
	
	@Override
	public URI getResourceType() {
		return DataModel.Participation.uri;
	}
	
	
}