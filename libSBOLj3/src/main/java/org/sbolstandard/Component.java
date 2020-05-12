package org.sbolstandard;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.Location.LocationBuilder;
import org.sbolstandard.util.RDFHandler;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.vocabulary.DataModel;

public class Component extends TopLevel {
	
	private List<URI> roles=new ArrayList<URI>();
	private List<URI> sequences=new ArrayList<URI>();	
	private List<URI> types=new ArrayList<URI>();
	private List<Feature> features=null;
	private List<SubComponent> subComponents=null;
	private List<ComponentReference> componentReferences=null;
	private List<LocalSubComponent> localSubComponents=null;
	private List<ExternallyDefined> externallyDefineds=null;
	private List<SequenceFeature> sequenceFatures=null;
	private List<Interaction> interactions=null;
	//private Set<Interaction> interactions2=null;
	
	protected Component(Model model, URI uri)
	{
		super(model, uri);
	}
	
	protected Component(Resource resource)
	{
		super(resource);
	}
	
	
	public List<URI> getTypes() {
		if (types==null)
		{
			types=RDFUtil.getPropertiesAsURIs(this.resource, URI.create("http://sbols.org/v3#type"));
		}
		return types;
	}
	
	public void setTypes(List<URI> types) {
		this.types = types;
		RDFUtil.setProperty(resource, URI.create("http://sbols.org/v3#type"), types);
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
	
	
	public List<URI> getSequences() {
		if (sequences==null)
		{
			sequences=RDFUtil.getPropertiesAsURIs(this.resource, URI.create("http://sbols.org/v3#sequence"));
		}
		return sequences;
	}
	
	public void setSequences(List<URI> sequences) {
		this.sequences = sequences;
		RDFUtil.setProperty(resource, URI.create("http://sbols.org/v3#sequence"), sequences);
	}
	
	//Features
	
	/*public List<Feature> getFeatures() throws SBOLException, SBOLGraphException {
		if (features==null)
		{
			features = new ArrayList<Feature>();
			addToList(this.resource, features, DataModel.Property.feature, DataModel.Entity.Feature);	
		}
		return features;
	}
	
	private Feature addToFeatures(Feature feature) {
		RDFUtil.setProperty(resource, DataModel.Property.feature, feature.getUri());
		if (features == null) {
			features = new ArrayList<Feature>();
		}
		features.add(feature);
		return feature;
	}
	*/
	
	public List<Feature> getFeatures() throws SBOLGraphException{
		if (features==null)
		{
			features = new ArrayList<Feature>();
			features.addAll(this.getSubComponents());
			features.addAll(this.getComponentReferences());
			features.addAll(this.getLocalSubComponents());
			features.addAll(this.getExternallyDefineds());
			features.addAll(this.getSequenceFeatures());	
		}
		return features;
	}
	
	public List<SubComponent> getSubComponents() throws SBOLGraphException {
		this.subComponents=addToList(this.subComponents, DataModel.Component.feature, SubComponent.class, DataModel.SubComponent.uri);
		return this.subComponents;
		/*
		if (subComponents==null)
		{
			List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.feature, DataModel.Entity.SubComponent);
			for (Resource res:resources)
			{
				SubComponent subComponent=new SubComponent(res);
				subComponents.add(subComponent);			
			}
		}
		return subComponents;*/
	}
	
	public SubComponent createSubComponent(URI uri, URI isInstanceOf) {
		SubComponent feature = new SubComponent(this.resource.getModel(), uri);
		feature.setIsInstanceOf(isInstanceOf);
		this.subComponents=addToList(this.subComponents, feature, DataModel.Component.feature);
		return feature;	
	}
		
	public List<ComponentReference> getComponentReferences() throws SBOLGraphException {
		this.componentReferences=addToList(this.componentReferences, DataModel.Component.feature, ComponentReference.class, DataModel.ComponentReference.uri);
		return this.componentReferences;
	}
	
	public ComponentReference createComponentReference(URI uri, URI feature, URI inChildOf) {
		ComponentReference componentReference= new ComponentReference(this.resource.getModel(), uri);
		componentReference.setFeature(feature);
		componentReference.setInChildOf(inChildOf);
		this.componentReferences=addToList(this.componentReferences, componentReference, DataModel.Component.feature);
		return componentReference;	
	}	

	public List<LocalSubComponent> getLocalSubComponents() throws SBOLGraphException {
		this.localSubComponents=addToList(this.localSubComponents, DataModel.Component.feature, LocalSubComponent.class, DataModel.LocalSubComponent.uri);
		return this.localSubComponents;
	}
	
	public LocalSubComponent createLocalSubComponent(URI uri, List<URI> types) {
		LocalSubComponent localSubComponent= new LocalSubComponent(this.resource.getModel(), uri);
		localSubComponent.setTypes(types);
		this.localSubComponents=addToList(this.localSubComponents, localSubComponent, DataModel.Component.feature);
		return localSubComponent;	
	}	
	
	
	public List<ExternallyDefined> getExternallyDefineds() throws SBOLGraphException {
		this.externallyDefineds=addToList(this.externallyDefineds, DataModel.Component.feature, ExternallyDefined.class, DataModel.ExternalyDefined.uri);
		return this.externallyDefineds;
	}
	
	public ExternallyDefined createExternallyDefined(URI uri, List<URI> types, URI definition) {
		ExternallyDefined externallyDefined= new ExternallyDefined(this.resource.getModel(), uri);
		externallyDefined.setTypes(types);
		externallyDefined.setDefinition(definition);
		this.externallyDefineds=addToList(this.externallyDefineds, externallyDefined, DataModel.Component.feature);
		return externallyDefined;	
	}	
	
	
	public List<SequenceFeature> getSequenceFeatures() throws SBOLGraphException {
		this.sequenceFatures=addToList(this.sequenceFatures, DataModel.Component.feature, SequenceFeature.class, DataModel.SequenceFeature.uri);
		return this.sequenceFatures;
	}
	
	public SequenceFeature createSequenceFeature(URI uri, List<LocationBuilder> locations) {
		SequenceFeature sequenceFeature= new SequenceFeature(this.resource.getModel(), uri);
		
		RDFUtil.addProperty(resource, DataModel.Component.feature, sequenceFeature.getUri());
		if (locations!=null && locations.size()>0)
		{
			for (LocationBuilder locationBuilder:locations)
			{
				sequenceFeature.createLocation(locationBuilder);
			}
		}
		return sequenceFeature;	
	}	

	public Interaction createInteraction(URI uri, List<URI> types) {
		Interaction interaction= new Interaction(this.resource.getModel(), uri);
		interaction.setTypes(types);
		this.interactions=addToList(this.interactions, interaction, DataModel.Component.interaction);
		return interaction;
	}
	
	public List<Interaction> getInteractions() throws SBOLGraphException, SBOLException {
		this.interactions=addToList(this.interactions, DataModel.Component.interaction, Interaction.class);
		return this.interactions;
	}

	public URI getResourceType()
	{
		return URI.create("http://sbols.org/v3#Component");
	}
	
	

}



/*public Interaction createInteractionDel(URI uri, List<URI> types ) {
Interaction interaction= new Interaction(this.resource.getModel(), uri);
interaction.setTypes(types);
RDFUtil.addProperty(resource, DataModel.Component.interaction, interaction.getUri());
if (this.interactions==null)
{
	interactions=new ArrayList<Interaction>();
}
interactions.add(interaction);
return interaction;	
}


public Interaction createInteractionDel2(URI uri, List<URI> types ) {
Interaction interaction= new Interaction(this.resource.getModel(), uri);
interaction.setTypes(types);
RDFUtil.addProperty(resource, DataModel.Component.interaction, interaction.getUri());
if (this.interactions2==null)
{
	interactions2=new HashSet<Interaction>();
}
interactions2.add(interaction);
return interaction;	
}
*/

/*
public List<Interaction> getInteractionsDel() throws SBOLGraphException {
	if (interactions==null)
	{
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.interaction);
		if (resources!=null && resources.size()>0)
		{
			interactions=new ArrayList<Interaction>();
		}
		for (Resource res:resources)
		{
			Interaction interaction=new Interaction(res);
			interactions.add(interaction);			
		}
	}
	return interactions;
}

public Set<Interaction> getInteractionsDel2() throws SBOLGraphException {
	if (interactions2==null)
	{
		List<Resource> resources=RDFUtil.getResourcesWithProperty(this.resource, DataModel.Component.interaction);
		if (resources!=null && resources.size()>0)
		{
			interactions2=new HashSet<Interaction>();
		}
		for (Resource res:resources)
		{
			Interaction interaction=new Interaction(res);
			interactions2.add(interaction);			
		}
	}
	return interactions2;
}
*/

