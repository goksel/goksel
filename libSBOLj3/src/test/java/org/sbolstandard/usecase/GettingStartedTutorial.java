package org.sbolstandard.usecase;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.sbolstandard.TestUtil;
import org.sbolstandard.api.SBOLAPI;
import org.sbolstandard.entity.Component;
import org.sbolstandard.entity.ComponentReference;
import org.sbolstandard.entity.Constraint;
import org.sbolstandard.entity.Interaction;
import org.sbolstandard.entity.Location;
import org.sbolstandard.entity.Participation;
import org.sbolstandard.entity.Location.LocationBuilder;
import org.sbolstandard.entity.SBOLDocument;
import org.sbolstandard.entity.Sequence;
import org.sbolstandard.entity.SequenceFeature;
import org.sbolstandard.entity.SubComponent;
import org.sbolstandard.io.SBOLIO;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.ComponentType;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.Encoding;
import org.sbolstandard.vocabulary.InteractionType;
import org.sbolstandard.vocabulary.Orientation;
import org.sbolstandard.vocabulary.ParticipationRole;
import org.sbolstandard.vocabulary.RestrictionType;
import org.sbolstandard.vocabulary.Role;

/**
 * COMBINE 2020 SBOL 3 Tutorial
 * October, 2020
 * This tutorial code goes with the slides at:
 * https://github.com/SynBioDex/Community-Media/blob/master/2020/IWBDA20/SBOL3-IWBDA-2020.pptx
 */
public class GettingStartedTutorial {


	public void runExample() throws SBOLGraphException, IOException
	{
		// Create a new SBOL document
		URI base=URI.create("https://synbiohub.org/public/igem/");
		SBOLDocument doc=new SBOLDocument(base);
		
		/**
		 *Slide 26: GFP expression cassette
		 * --------------------------------------------------
		 * Component
		 * identity: iGEM#I13504
		 * name: "iGEM 2016 interlab reporter"
		 * description: "GFP expression cassette used for 2016 iGEM interlab"
		 * type: SBO:0000251 (DNA)
		 * role: SO:0000804 (Engineered Region)
		 */
		System.out.println("Creating GFP expression cassette");
		Component i13504=doc.createComponent(SBOLAPI.append(base, "i13504"), Arrays.asList(ComponentType.DNA.getUrl())); 
		i13504.setName("iGEM 2016 interlab reporter");
		i13504.setDescription("iGEM 2016 interlab reporter");
		i13504.setRoles(Arrays.asList(Role.EngineeredGene));
		System.out.println(String.format("Created GFP expression cassette component: %s", i13504.getUri()));
		  
		/* --------------------------------------------------
		 Slide 28: expression cassette parts
		-------------------------------------------------- */
		//Add the RBS subcomponent:
		//Create the RBS component
		Component rbs=doc.createComponent("B0034", Arrays.asList(ComponentType.DNA.getUrl())); 
		rbs.setName("B0034");
		rbs.setRoles(Arrays.asList(Role.RBS));
		
		//Create a sequence entity for the RBS component
		Sequence rbs_seq=doc.createSequence("B0034_seq");
		rbs_seq.setElements("aaagaggagaaa");
		rbs_seq.setEncoding(Encoding.NucleicAcid);
		rbs.setSequences(Arrays.asList(rbs_seq.getUri()));
		
		//Start assembling the i13504 device's sequence by adding the RBS component.
		SBOLAPI.appendComponent(doc, i13504,rbs,Orientation.inline);
		System.out.println(String.format("Added the RBS subcomponent: %s", rbs.getUri()));
			
		//Add the scar sequence between the RBS and CDS components
		SequenceFeature scar1=SBOLAPI.appendSequenceFeature(doc, i13504, "tactag", Orientation.inline);
		System.out.println(String.format("Added the scar sequence between the RBS and the CDS components: %s", scar1.getUri()));
		
		//Create the GFP component and add it as a subcomponent to continue assembling the i13504 device.
		String gfp_na="atgcgtaaaggagaagaacttttcactggagttgtcccaattcttgttgaattagatggtgatgttaatgggcacaaattttctgtcagtggagagggtgaaggtgatgcaacatacggaaaacttacccttaaatttatttgcactactggaaaactacctgttccatggccaacacttgtcactactttcggttatggtgttcaatgctttgcgagatacccagatcatatgaaacagcatgactttttcaagagtgccatgcccgaaggttatgtacaggaaagaactatatttttcaaagatgacgggaactacaagacacgtgctgaagtcaagtttgaaggtgatacccttgttaatagaatcgagttaaaaggtattgattttaaagaagatggaaacattcttggacacaaattggaatacaactataactcacacaatgtatacatcatggcagacaaacaaaagaatggaatcaaagttaacttcaaaattagacacaacattgaagatggaagcgttcaactagcagaccattatcaacaaaatactccaattggcgatggccctgtccttttaccagacaaccattacctgtccacacaatctgccctttcgaaagatcccaacgaaaagagagaccacatggtccttcttgagtttgtaacagctgctgggattacacatggcatggatgaactatacaaataataa";
		Component gfp=SBOLAPI.createDnaComponent(doc, "E0040", null, Role.CDS, gfp_na);
		SubComponent gfpSubComponent=SBOLAPI.appendComponent(doc, i13504,gfp, Orientation.inline);
		System.out.println(String.format("Added the GFP subcomponent %s", gfp.getUri()));
		
		//Add the scar between the CDS and terminator components
		SequenceFeature scar2=SBOLAPI.appendSequenceFeature(doc, i13504, "tactagag", Orientation.inline);
		System.out.println(String.format("Added the scar sequence between the CDS and the terminator components: %s", scar2.getUri()));
		
		//Create the terminator component
		String term_na="ccaggcatcaaataaaacgaaaggctcagtcgaaagactgggcctttcgttttatctgttgtttgtcggtgaacgctctc";
		Component term=SBOLAPI.createDnaComponent(doc, "B0015", null, Role.Terminator,term_na);
		
		//Add the terminator as a subcomponent. This time we will be using low level API methods, which can be used to create features and locations.
		SubComponent termSubComponent=i13504.createSubComponent("B0015", term.getUri());
		termSubComponent.setOrientation(Orientation.inline);
		Sequence i13504Sequence= (Sequence)doc.getIdentified(i13504.getSequences().get(0),Sequence.class);
		int start=i13504Sequence.getElements().length() + 1;
    	int end=start + term_na.length()-1;
    	i13504Sequence.setElements(i13504Sequence.getElements() + term_na);
    	LocationBuilder locationBuilder=new Location.RangeLocationBuilder(SBOLAPI.append(termSubComponent.getUri(), "location"), start, end,i13504Sequence.getUri());
    	locationBuilder.setOrientation(Orientation.inline);
    	termSubComponent.createLocation(locationBuilder);
    	System.out.println(String.format("Added the terminator subcomponent %s", term.getUri()));
		
		/* --------------------------------------------------
		 Slide 32: GFP production from expression cassette
		 -------------------------------------------------- */
		 Component i13504_system=SBOLAPI.createComponent(doc,"i13504_system", ComponentType.FunctionalEntity.getUrl(), "i13504 system", null, Role.FunctionalCompartment);
		 Component GFP=SBOLAPI.createComponent(doc, "GFP_protein", ComponentType.Protein.getUrl(), "GFP", "GFP", null); 
		 SubComponent i13504SubComponent=SBOLAPI.addSubComponent(i13504_system, i13504);
		 SubComponent gfpProteinSubComponent=SBOLAPI.addSubComponent(i13504_system, GFP);
		 
		// List<ComponentReference> gfpRefs=SBOLAPI.createComponentReference(i13504_system, i13504, gfp);
	     
		 ComponentReference gfpCDSReference=i13504_system.createComponentReference("compRef",gfpSubComponent, i13504SubComponent);
					    
		 Interaction interaction= i13504_system.createInteraction("gfp_production", Arrays.asList(InteractionType.GeneticProduction));
    	 interaction.createParticipation("cds",Arrays.asList(ParticipationRole.Template), gfpCDSReference.getUri());
    	 interaction.createParticipation("protein",Arrays.asList(ParticipationRole.Product), gfpProteinSubComponent.getUri());
	    	
		 /* --------------------------------------------------
		  Slide 34: Example: concatenating & reusing components
		  -------------------------------------------------- */
		 
		 //Left hand side of slide: interlab16device1
		 Component ilab16_dev1=doc.createComponent("interlab16device1", Arrays.asList(ComponentType.DNA.getUrl())); 
		 Component j23101=doc.createComponent("j23101", Arrays.asList(ComponentType.DNA.getUrl())); 
		 SubComponent sc_j23101=SBOLAPI.addSubComponent(ilab16_dev1, j23101);	
		 SubComponent sc_i13504_system=SBOLAPI.addSubComponent(ilab16_dev1, i13504_system);	
		 
		 ComponentReference compRef_i13504_dev1=ilab16_dev1.createComponentReference("i13504_dev1_compref", i13504SubComponent, sc_i13504_system);
		 ilab16_dev1.createConstraint("constraint_dev1", RestrictionType.Topology.meets, sc_j23101.getUri(), compRef_i13504_dev1.getUri());
	        
		 // Right hand side of slide: interlab16device2
		 Component ilab16_dev2=doc.createComponent("interlab16device2", Arrays.asList(ComponentType.DNA.getUrl())); 
		 Component j23106=doc.createComponent("j23106", Arrays.asList(ComponentType.DNA.getUrl())); 
		 SubComponent sc_j23106=SBOLAPI.addSubComponent(ilab16_dev2, j23106);	
		 SubComponent sc_i13504_system_dev2=SBOLAPI.addSubComponent(ilab16_dev2, i13504_system);	
		 
		 ComponentReference compRef_i13504_dev2=ilab16_dev2.createComponentReference("i13504_dev2_compref", i13504SubComponent, sc_i13504_system_dev2);
		 ilab16_dev2.createConstraint("constraint_dev2", RestrictionType.Topology.meets, sc_j23106.getUri(), compRef_i13504_dev2.getUri());
		    
		 String output=SBOLIO.write(doc, "RDF/XML-ABBREV");
		 System.out.println("");
		 System.out.println("SBOL:");
		 System.out.println(output);   
	     TestUtil.serialise(doc, "combine2020", "combine2020");         
	}
	
	public static void main(String[] args) throws SBOLGraphException, IOException
	{
		GettingStartedTutorial tutorial=new GettingStartedTutorial();
		tutorial.runExample();
	}
}