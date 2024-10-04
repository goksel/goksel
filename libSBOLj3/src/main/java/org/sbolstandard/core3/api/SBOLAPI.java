package org.sbolstandard.core3.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.sbolstandard.core3.entity.Component;
import org.sbolstandard.core3.entity.ComponentReference;
import org.sbolstandard.core3.entity.Constraint;
import org.sbolstandard.core3.entity.Feature;
import org.sbolstandard.core3.entity.Identified;
import org.sbolstandard.core3.entity.Interaction;
import org.sbolstandard.core3.entity.Participation;
import org.sbolstandard.core3.entity.Range;
import org.sbolstandard.core3.entity.SBOLDocument;
import org.sbolstandard.core3.entity.Sequence;
import org.sbolstandard.core3.entity.SequenceFeature;
import org.sbolstandard.core3.entity.SubComponent;
import org.sbolstandard.core3.util.SBOLGraphException;
import org.sbolstandard.core3.util.SBOLUtil;
import org.sbolstandard.core3.vocabulary.ComponentType;
import org.sbolstandard.core3.vocabulary.DataModel;
import org.sbolstandard.core3.vocabulary.Encoding;
import org.sbolstandard.core3.vocabulary.Orientation;
import org.sbolstandard.core3.vocabulary.RestrictionType;

/**
 * 
 * @author gokselmisirli
 *
 */
public class SBOLAPI {

	  public static List<Interaction> createInteraction(List<URI> interactionTypes, Component parent, Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles) throws SBOLGraphException
	    {
	    	List<Interaction> interactions=new ArrayList<Interaction>();
	    	List<SubComponent> features1=createSubComponents(parent, participant1);
	    	List<SubComponent> features2=createSubComponents(parent, participant2);
	    	if (features1!=null && features2!=null){
		    	for (Feature feature1: features1){
		    		for (Feature feature2: features2){
		    			Interaction interaction=createInteraction(interactionTypes, parent, feature1, participant1Roles, feature2, participant2Roles);	
		    			interactions.add(interaction);
		    		}
		    	}
	    	}
	    	return interactions;
	    }
	
	  	private static List<SubComponent> createSubComponents (Component parent, Component child) throws SBOLGraphException 
	  	{
	    	List<SubComponent> subComponents=getSubComponents(parent, child);
	    	//If not DNA and there is no subComponent yet, add a subcomponent for the child
	    	//if ((subComponents==null || subComponents.size()==0) && !child.getTypes().contains(ComponentType.DNA.getUrl()))
	    	if (subComponents==null || subComponents.size()==0){
	    		SubComponent subComponent=parent.createSubComponent(child);
		    	if (subComponents==null)
		    	{
		    		subComponents=new ArrayList<SubComponent>();
		    	}
		    	subComponents.add(subComponent);
	    	}
	    	return subComponents;
	  	}
	    
	    public static List<SubComponent> getSubComponents(Component parent, Component child) throws SBOLGraphException
	    {
	    	List<SubComponent> found= null;
	    	List<SubComponent> features=parent.getSubComponents();
	    	if (features!=null)
		    {
		    	for (SubComponent feature: features)
		    	{
		    		if (feature.getInstanceOf().getUri().equals(child.getUri()))
		    		{
		    			if (found==null)
		    			{
		    				found= new ArrayList<SubComponent>();
		    			}
		    			found.add(feature);
		    		}
		    	}
	    	}
	    	return found;
	    }
	    
	    
	    public static  Interaction createInteraction(List<URI> interactionTypes, Component container, Feature participant1, List<URI> participant1Roles, Feature participant2, List<URI> participant2Roles) throws SBOLGraphException
	    {
	    	Interaction interaction= container.createInteraction(interactionTypes);
	    	createParticipation(interaction, participant1Roles, participant1);
	    	createParticipation(interaction, participant2Roles, participant2);
	    	return interaction;
	    }
	    	   
	    public static  Participation createParticipation(Interaction interaction, List<URI> roles, Feature feature) throws SBOLGraphException
	    {
	    	Participation participation=interaction.createParticipation(roles, feature);
	    	return participation;
	    }
	    
	    private static  int getIndex(List items) 
	    {
	    	int index=1;
	    	if (items!=null)
	    	{
	    		index=items.size()+1;
	    	}
	    	return index;
	    }
	    
	    private static  int getIndex(List items, Class instanceType) 
	    {
	    	int index=1;
	    	if (items!=null)
	    	{
	    		for (int i=0;i<items.size();i++)
	    		{
	    			if (instanceType.isInstance(items.get(i)))
	    			{
	    				index++;
	    			}
	    		}
	    	}
	    	return index;
	    }
	    
	    private static  int getIndex(Set items) 
	    {
	    	int index=1;
	    	if (items!=null)
	    	{
	    		index=items.size()+1;
	    	}
	    	return index;
	    }
	    
	    private static String getSubString(String data, String searchString)
	    {
	    	String found=null;
	    	int index=data.indexOf(searchString);
    		if (index!=-1 && data.length()>index+searchString.length())
 	    	{
 	    		found=data.substring(index+searchString.length());
 	    	}
    		return found;
	    }
	    
	    private static String getLocal(URI uri)
	    {
	    	String local=null;
	    	String uriString=uri.toString();
	    	local=getSubString(uriString, "#");
	    	if (local==null)
	    	{
	    		local=getSubString(uriString, "/");
	    	}
	    	return local;
	    }
	    
	    public static <T extends Identified> String createLocalName(URI entityType, List<T> items)
	    {
	    	int suffix=getIndex(items);
	    	String name=null;
	    	boolean valid=false;
	    	while (!valid) {
	    		name = createLocalName(entityType, suffix);
	    		boolean uniqueName=true;
	    		if (items!=null) {
		    		for (Identified identified: items){
		    			if (identified.getUri().toString().endsWith(name)){
		    				suffix++;
		    				uniqueName=false;
		    				break;
		    			}
		    		}
	    		}
	    		
	    		if (uniqueName) {
	    			valid=true;
	    			break;
	    		}
	    	}
	    	return name;
	    }
	    
	    /* GM: 20230324
	    public static String createLocalName(URI entityType, List items, Class entityClass)
	    {
	    	int suffix=getIndex(items,entityClass);
	    	return createLocalName(entityType, suffix);
	    }*/
	    
	    private static String createLocalName(URI entityType, int suffix)
	    {
	    	return createLocalName(entityType, String.valueOf(suffix));
	    }
	    
	    public static URI createLocalUri(Identified identified, URI entityType, List items)
	    {
	    	String displayId=SBOLAPI.createLocalName(entityType, items);	
	    	URI uri=SBOLAPI.append(identified.getUri(), displayId);
	    	return uri;
	    }
	    
	    /* GM: 20230324
	     public static URI createLocalUri2(Identified identified, URI entityType, List items, Class entityClass)
	    {
	    	String displayId=SBOLAPI.createLocalName(entityType, items,entityClass);	
	    	URI uri=SBOLAPI.append(identified.getUri(), displayId);
	    	return uri;
	    }*/
		
	    private static String createLocalName(URI entityType, String suffix)
	    {
	    	String displayId=getLocal(entityType) + suffix;
	    	return displayId;
	    }
	   
	    public static void appendComponent(SBOLDocument document, Component parent, Component child) throws SBOLGraphException 
	    {
	    	appendComponent(document, parent, child, Orientation.inline);
	    }
	    
	    /*public static SubComponent appendComponentRemove(SBOLDocument document, Component parent, Component child, Orientation orientation) throws SBOLGraphException 
	    {
	    	SubComponent subComponent=parent.createSubComponent(child.getUri());
	    	subComponent.setOrientation(orientation);
	    	
	    	if (child.getSequences()!=null && child.getSequences().size()>0)
	    	{
		    	List<URI> sequences= parent.getSequences();
		    	Sequence sequence=null;
		    	if (sequences!=null && sequences.size()>0)
		    	{
		    		 sequence=(Sequence)document.getIdentified(sequences.get(0),Sequence.class);
		    	}
		    	else
		    	{
		    		sequence=createSequence(document, parent, Encoding.NucleicAcid, "");	
		    	}
		    	
		    	
		    	if ( child.getSequences()!=null && child.getSequences().size()>0)
		    	{
		    		URI childSequenceUri=child.getSequences().get(0);
		    		Sequence childSequence=(Sequence)document.getIdentified(childSequenceUri, Sequence.class);
		    		if (orientation==Orientation.inline)
		    		{
		    			sequence.setElements(sequence.getElements() + childSequence.getElements());
		    		}
		    		else
		    		{
		    			throw new SBOLGraphException("Reverse complement sequence addition has not been implemented yet!");
		    		}
		    		int start=sequence.getElements().length() + 1;
		        	int end=start + childSequence.getElements().length()-1;
		        	LocationBuilder builder=new Location.RangeLocationBuilder(start, end,sequence.getUri());
		        	Location location=subComponent.createLocation(builder);
		        	location.setOrientation(orientation);
		    	}
	    	}
	    	return subComponent;
	    }*/
	    
	    
	    public static SubComponent appendComponent(SBOLDocument document, Component parent, Component child, Orientation orientation) throws SBOLGraphException 
	    {
	    	SubComponent subComponent=parent.createSubComponent(child);
	    	subComponent.setOrientation(orientation);
	    	if (child.getSequences()!=null && child.getSequences().size()>0)
	    	{
	    		/*URI childSequenceUri=child.getSequences().get(0);
	    		Sequence childSequence=(Sequence)document.getIdentified(childSequenceUri, Sequence.class);*/
	    		Sequence childSequence=child.getSequences().get(0);
	    		createRange(document, parent, subComponent, childSequence.getElements(), orientation);
	    	}
	    	return subComponent;
	    }

		public static SequenceFeature appendSequenceFeature(SBOLDocument document, Component parent, String elements, Orientation orientation) throws SBOLGraphException {
			SequenceFeature feature = null;
			if (elements != null && elements.length() > 0) {
				List<Sequence> sequences = parent.getSequences();
				Sequence sequence = null;
				int start, end;
				if (sequences != null && sequences.size() > 0) {
					sequence = parent.getSequences().get(0);
					start = sequence.getElements().length() + 1;
					end = start + elements.length() - 1;
				} 
				else {
					sequence = createSequence(document, parent, Encoding.NucleicAcid, "");
					start = 1;
					end = elements.length() - 1;
				}

				if (orientation == Orientation.inline) {
					sequence.setElements(sequence.getElements() + elements);
				} 
				else {
					throw new SBOLGraphException("Reverse complement sequence addition has not been implemented yet!");
				}
				feature = parent.createSequenceFeature(start, end, sequence);
				if (feature != null) {
					feature.setOrientation(orientation);
				}
				((Range)feature.getLocations().get(0)).setOrientation(orientation);
				
			}
			return feature;
		}

	    private static Range createRange(SBOLDocument document, Component parent, SubComponent subComponent, String elements, Orientation orientation) throws SBOLGraphException
	    {
	    	Range range=null;
	    	if (elements!=null && elements.length()>0)
	    	{
		    	List<Sequence> sequences= parent.getSequences();
		    	Sequence sequence=null;
		    	int start, end;
		    	if (sequences!=null && sequences.size()>0)
		    	{
		    		sequence=parent.getSequences().get(0);
		    		start=sequence.getElements().length() + 1;
			        end=start + elements.length()-1;
		    	}
		    	else
		    	{
		    		sequence=createSequence(document, parent, Encoding.NucleicAcid, "");	
			    	start=1;
		        	end=elements.length();
		    	}
		    	
		    	if (orientation==Orientation.inline)
	    		{
	    			sequence.setElements(sequence.getElements() + elements);
	    		}
	    		else
	    		{
	    			throw new SBOLGraphException("Reverse complement sequence addition has not been implemented yet!");
	    		}
		    	range=subComponent.createRange(start, end, sequence);
		    	range.setOrientation(orientation);
	    	}
	    	return range;

	    }
	   
	    /*private static LocationBuilder createLocationBuilder(SBOLDocument document, Component parent, String elements, Orientation orientation) throws SBOLGraphException
	    {
	    	LocationBuilder locationBuilder=null;
	    	if (elements!=null && elements.length()>0)
	    	{
		    	List<Sequence> sequences= parent.getSequences();
		    	Sequence sequence=null;
		    	int start, end;
		    	if (sequences!=null && sequences.size()>0)
		    	{
		    		sequence=parent.getSequences().get(0);
		    		start=sequence.getElements().length() + 1;
			        end=start + elements.length()-1;
		    	}
		    	else
		    	{
		    		sequence=createSequence(document, parent, Encoding.NucleicAcid, "");	
			    	start=1;
		        	end=elements.length()-1;
		    	}
		    	
		    	if (orientation==Orientation.inline)
	    		{
	    			sequence.setElements(sequence.getElements() + elements);
	    		}
	    		else
	    		{
	    			throw new SBOLGraphException("Reverse complement sequence addition has not been implemented yet!");
	    		}
		    	 

	        	locationBuilder=new Location.RangeLocationBuilder(start, end,sequence);
	        	locationBuilder.setOrientation(orientation);
	    	}
	    	return locationBuilder;
	    }*/
	    
	    public static Component createDnaComponent(SBOLDocument doc, String displayId, String name, String description, URI role, String sequence) throws SBOLGraphException
	    {
	    	Component dna=createComponent(doc, displayId, ComponentType.DNA.getUri(), name, description, role);	
	    	if (sequence!=null && sequence.length()>0)
	    	{
	    		createSequence(doc, dna, Encoding.NucleicAcid, sequence);
	    	}
	    	return dna;
	    }
	    
	    public static Component createProteinComponent(SBOLDocument doc, Component container, String displayId, String name, String description, URI role, String sequence) throws SBOLGraphException
	    {
	    	Component protein=createComponent(doc, displayId, ComponentType.Protein.getUri(), name, description, role);
	    	if (container!=null)
	    	{
	    		container.createSubComponent(protein);
	    	}
	    	if (sequence!=null && sequence.length()>0)
	    	{
	    		createSequence(doc, protein, Encoding.AminoAcid, sequence);
	    	}
	    	return protein;
	    }
	    
	    public static Component createProteinComponent(SBOLDocument doc, String displayId, String name, String description, URI role, String sequence) throws SBOLGraphException
	    {
	    	return createProteinComponent(doc, null, displayId, name, description, role, sequence);
	    }	    
	    
	    public static SubComponent addSubComponent(Component parent, Component child) throws SBOLGraphException
	    {
	    	SubComponent subComponent=parent.createSubComponent(child);
	    	return subComponent;
	    }
	    
	    
	    public static Sequence createSequence(SBOLDocument doc, Component component, Encoding encoding, String elements) throws SBOLGraphException
	    {
	    	String localName=createLocalName(DataModel.Sequence.uri, component.getSequences());
	    	
	    	Sequence seq=createSequence(doc, URI.create(component.getUri().toString() + "_" + localName), localName, component.getDisplayId() + " sequence", elements, encoding);
	    	component.setSequences(Arrays.asList(seq)); 
	 		return seq;
	    }
	    
	    public static Sequence addSequence(SBOLDocument doc, Component component, Encoding encoding, String elements) throws SBOLGraphException
	    {
	    	String localName=createLocalName(DataModel.Sequence.uri, doc.getSequences());
	    	Sequence seq=createSequence(doc, URI.create(component.getUri().toString() + "_" + localName), localName, component.getDisplayId() + " sequence", elements, encoding);
	    	List<Sequence> sequences=component.getSequences();
	    	if (sequences==null){
	    		component.setSequences(Arrays.asList(seq)); 
	    	}
	    	else{
	    		sequences.add(seq); 
	    		component.setSequences(sequences);
	    	}
	 		return seq;
	    }
	   
	    
	    public static Component createComponent(SBOLDocument doc, URI uri, URI type, String name, String description, URI role) throws SBOLGraphException
	    {
	    	URI namespace=null;
	    	if (doc.getBaseURI()!=null){
	    		namespace= SBOLUtil.toNameSpace(doc.getBaseURI());
	    	}
	    	Component component=doc.createComponent(uri, namespace, Arrays.asList(type)); 
	    	component.setName(name);
	    	component.setDescription(description);
	        if (role!=null){
	        	component.setRoles(Arrays.asList(role));
	        }
	        
	        return component;   
	    }
	    
	    public static Component createComponent(SBOLDocument doc, String displayId, URI type, String name, String description, URI role) throws SBOLGraphException
	    {
	    	Component component=doc.createComponent(displayId, Arrays.asList(type)); 
	    	component.setName(name);
	    	component.setDescription(description);
	        if (role!=null){
	        	component.setRoles(Arrays.asList(role));
	        }
	        
	        return component;     
	    }
	    
	   
	    
	    public static URI append(URI uri, String id)
	    {
	    	return append(uri.toString(),id);   	
	    }
	    
	    public static URI append(String text, String add)
	    {
	    	if (text.endsWith("/") || text.endsWith("#")){
	    		return URI.create(String.format("%s%s", text,add));
	    	}
	    	else{	
	    		return URI.create(String.format("%s/%s", text,add));
	    	}
	    }
	    
	    
	    public static Sequence createSequence(SBOLDocument doc, URI uri, String name, String description, String sequence, Encoding encoding) throws SBOLGraphException
	    {
	        Sequence sequenceEntity=doc.createSequence(uri,SBOLUtil.toNameSpace(doc.getBaseURI()));
	        sequenceEntity.setName(name);
	        sequenceEntity.setDescription(description);   
	        if (sequence!=null)
	        {
	        	sequenceEntity.setElements(sequence);
	        }
	        sequenceEntity.setEncoding(encoding);
	        return sequenceEntity;
	        
	    }
	    
	   
	    
	    /*public static void mapTo(SubComponent subComponentInContainer,Component container, Component parent, Component child) throws SBOLGraphException
		{
			 List<ComponentReference> childReferences=createComponentReference(container, parent, child);
			 if (childReferences!=null)
			 {
				 for (ComponentReference compRef: childReferences)
				 {
					 String localName=SBOLAPI.createLocalName(DataModel.Constraint.uri, container.getConstraints());
					 container.createConstraint(SBOLAPI.append(container.getUri(), localName), RestrictionType.Identity.verifyIdentical, subComponentInContainer.getUri(), compRef.getUri());
				 } 
			 }
		}*/
	    
	    public static void mapTo(Component container, Component parent1, Component child1, Component parent2, Component child2) throws SBOLGraphException
	    {
	    	 List<ComponentReference> childReferences1=createComponentReference(container, parent1, child1);
	    	 List<ComponentReference> childReferences2=createComponentReference(container, parent2, child2);
	    	 if (childReferences1!=null && childReferences2!=null)
			 {
				 for (ComponentReference compRef1: childReferences1)
				 {
					 for (ComponentReference compRef2: childReferences2)
					 {
						 container.createConstraint(RestrictionType.IdentityRestriction.verifyIdentical.getUri(), compRef1, compRef2);
					 }
				 } 
			 }	 
				
	    }
	    
	   /* private static <T extends Feature> void createConstraint(Component container, List<T> subjects, List<T> objects) throws SBOLGraphException
	    {
	    	if (subjects!=null && objects!=null){
		    	for (Feature compRef1: subjects)
				 {
					 for (Feature compRef2: objects)
					 {
						 String localName=SBOLAPI.createLocalName(DataModel.Constraint.uri, container.getConstraints());
						 container.createConstraint(SBOLAPI.append(container.getUri(), localName), RestrictionType.Identity.verifyIdentical, compRef1.getUri(), compRef2.getUri());
					 }
				 } 
			 }
	    }*/
	    
	    public static void mapTo(Component container, Component parent1, Component child1, Component containerChild) throws SBOLGraphException
	    {
	    	 List<ComponentReference> childReferences1=createComponentReference(container, parent1, child1);
	    	 List<SubComponent> childReferences2=getSubComponents(container, containerChild);
	    	 //createConstraint(containerChild, childReferences1, childReferences2);
	    	 if (childReferences1!=null && childReferences2!=null)
			 {
				 for (ComponentReference compRef1: childReferences1)
				 {
					 for (SubComponent compRef2: childReferences2)
					 {
						 container.createConstraint(RestrictionType.IdentityRestriction.verifyIdentical.getUri(), compRef1, compRef2);
					 }
				 } 
			 }	 
				
	    }
	      
		//TODO:Remove
	   /* public static  List<ComponentReference> createComponentReference2(Component container, Component parent, Component child) throws SBOLGraphException
		{
			List<ComponentReference> componentReferences=null;
			List<SubComponent> subComponents=getSubComponents(parent, child);
			if (subComponents!=null)
			{
				for (SubComponent subComponent:subComponents)
				{
					ComponentReference compRef=container.createComponentReference(child.getUri(), subComponent.getUri());
			        if (componentReferences==null)
			        {
			        	componentReferences=new ArrayList<ComponentReference>();
			        }
			        componentReferences.add(compRef);
				}
			}
			return componentReferences;
		}*/
	    
	    public static  List<ComponentReference> createComponentReference(Component container, Component parent, Component child) throws SBOLGraphException
	  		{
	  			List<ComponentReference> componentReferences=null;
	  			List<SubComponent> subComponentsInContainer=getSubComponents(container, parent);
	  			List<SubComponent> subComponentsInParent=getSubComponents(parent, child);
	  				  			
	  			if (subComponentsInContainer!=null && subComponentsInParent!=null)
	  			{
	  				for (SubComponent subComponentInContainer:subComponentsInContainer)
	  				{
	  					for (SubComponent subComponentInParent:subComponentsInParent)
	  					{	
		  					ComponentReference compRef=container.createComponentReference(subComponentInParent, subComponentInContainer);
		  			        if (componentReferences==null)
		  			        {
		  			        	componentReferences=new ArrayList<ComponentReference>();
		  			        }
		  			        componentReferences.add(compRef);
	  					}
	  				}
	  			}
	  			return componentReferences;
	  		}
		
	    public static List<Constraint> createConstraint(Component container, Component component1, Component component2, URI restriction) throws SBOLGraphException
	    {
	    	List<Constraint> result=null;
	    	List<SubComponent> subComponents1=createSubComponents(container, component1);
	    	List<SubComponent> subComponents2=createSubComponents(container, component2);
	    	
	    	if (subComponents1!=null && subComponents2!=null)
	    	{
	    		for (SubComponent subComponent1:subComponents1)
	    		{
	    			for (SubComponent subComponent2:subComponents2)
		    		{	
	    		        Constraint constraint=container.createConstraint(restriction, subComponent1, subComponent2);
	    		        if (result==null)
	    		        {
	    		        	result=new ArrayList<Constraint>();
	    		        }
	    		        result.add(constraint);
		    		}
	    		}
	    	}
	    	return result;
	    }
	    
		/*private static List<URI> getSubComponent(Component parent, Component child) throws SBOLGraphException
		{
			List<URI> result=null;
			for (SubComponent subComponent:parent.getSubComponents())
			{
				if (subComponent.getIsInstanceOf().equals(child.getUri()))
				{
					if (result==null)
					{
						result=new ArrayList<URI>();
					}
					result.add(subComponent.getUri());
				}
				
			}
			return result;
		}
		*/
		
	   /* 
	    public static List<Interaction> createInteractionDel(List<URI> interactionTypes, Component parent, Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles) throws SBOLGraphException, SBOLException
	    {
	    	List<Interaction> interactions=new ArrayList<Interaction>();
	    	List<SubComponent> features1=getSubComponents(parent, participant1);
	    	List<SubComponent> features2=getSubComponents(parent, participant2);
	    	
	    	for (Feature feature1: features1)
	    	{
	    		for (Feature feature2: features2)
	    		{
	    			Interaction interaction=createInteractionDel(interactionTypes, parent, feature1, participant1Roles, feature2, participant2Roles);	
	    			interactions.add(interaction);
	    		}
	    	}
	    	return interactions;
	    }
	    
	    public static  Interaction createInteractionDel(List<URI> interactionTypes, Component container, Feature participant1, List<URI> participant1Roles, Feature participant2, List<URI> participant2Roles) throws SBOLGraphException, SBOLException
	    {
	    	int index=getIndex(container.getInteractionsDel());
	    	Interaction interaction= container.createInteractionDel(append(container.getUri(), "interaction" + index), interactionTypes);
	    	createParticipation(interaction, participant1Roles, participant1);
	    	createParticipation(interaction, participant2Roles, participant2);
	    	return interaction;
	    }
	    
	    public static Set<Interaction> createInteractionDel2(List<URI> interactionTypes, Component parent, Component participant1, List<URI> participant1Roles, Component participant2, List<URI> participant2Roles) throws SBOLGraphException, SBOLException
	    {
	    	Set<Interaction> interactions=new HashSet<Interaction>();
	    	List<SubComponent> features1=getSubComponents(parent, participant1);
	    	List<SubComponent> features2=getSubComponents(parent, participant2);
	    	
	    	for (Feature feature1: features1)
	    	{
	    		for (Feature feature2: features2)
	    		{
	    			Interaction interaction=createInteractionDel2(interactionTypes, parent, feature1, participant1Roles, feature2, participant2Roles);	
	    			interactions.add(interaction);
	    		}
	    	}
	    	return interactions;
	    }
	    
	    public static  Interaction createInteractionDel2(List<URI> interactionTypes, Component container, Feature participant1, List<URI> participant1Roles, Feature participant2, List<URI> participant2Roles) throws SBOLGraphException, SBOLException
	    {
	    	int index=getIndex(container.getInteractionsDel2());
	    	Interaction interaction= container.createInteractionDel2(append(container.getUri(), "interaction" + index), interactionTypes);
	    	createParticipation(interaction, participant1Roles, participant1);
	    	createParticipation(interaction, participant2Roles, participant2);
	    	return interaction;
	    }
	    */
	    
	    public static Set<Component> getRootComponents(SBOLDocument doc, ComponentType type) throws SBOLGraphException
		{
			Set<URI> childNodes=null;
			Set<Component> rootNodes=new HashSet<Component>();
			if (doc!=null && doc.getComponents()!=null)
			{
				
				for (Component compDef:doc.getComponents())
				{
					if (compDef.getSubComponents()!=null && compDef.getTypes().contains(type.getUri()))
					{
						for (SubComponent comp:compDef.getSubComponents())
						{
							if (comp.getInstanceOf()!=null)
							{
								if (childNodes==null)
								{
									childNodes=new HashSet<URI>();
								}
								childNodes.add(comp.getInstanceOf().getUri());
							}
						}
					}
				}
				for (Component compDef:doc.getComponents())
				{
					if (!childNodes.contains(compDef.getUri()) && compDef.getTypes().contains(type.getUri()))
					{
						if (rootNodes==null)
						{
							rootNodes=new HashSet<Component>();
						}	
						rootNodes.add(compDef);
					}
				}
			}
			return rootNodes;
		}	    
}
