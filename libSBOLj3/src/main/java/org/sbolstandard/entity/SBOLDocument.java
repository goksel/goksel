package org.sbolstandard.entity;

import java.lang.reflect.Constructor;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.sbolstandard.util.URINameSpace;
import org.sbolstandard.entity.provenance.Agent;
import org.sbolstandard.util.RDFHandler;
import org.sbolstandard.util.RDFUtil;
import org.sbolstandard.util.SBOLGraphException;
import org.sbolstandard.vocabulary.DataModel;
import org.sbolstandard.vocabulary.MeasureDataModel;
import org.sbolstandard.vocabulary.ProvenanceDataModel;
import org.sbolstandard.entity.measure.BinaryPrefix;
import org.sbolstandard.entity.measure.Measure;
import org.sbolstandard.entity.measure.Prefix;
import org.sbolstandard.entity.measure.PrefixedUnit;
import org.sbolstandard.entity.measure.SIPrefix;
import org.sbolstandard.entity.measure.SingularUnit;
import org.sbolstandard.entity.measure.UnitDivision;
import org.sbolstandard.entity.measure.UnitExponentiation;
import org.sbolstandard.entity.measure.UnitMultiplication;
import org.sbolstandard.entity.provenance.*;

public class SBOLDocument {
	protected Model model = null;
	
	private List<Component> components;
	private List<Sequence> sequences;
	private List<CombinatorialDerivation> combinatorialDerivations;
	private List<Implementation> implementations;
	private List<ExperimentalData> experimentalDatas;
	private List<org.sbolstandard.entity.Model> models;
	private List<Collection> collections;
	private List<Namespace> namespaces;
	private List<Experiment> experiments;
	private List<Attachment> attachments;
	private List<Agent> agents;
	private List<Plan> plans;
	private List<Activity> activities;
	private List<SIPrefix> siPrefixes;
	private List<BinaryPrefix> binaryPrefixes;
	private List<SingularUnit> singularUnits;
	private List<UnitMultiplication> unitMultiplications;
	private List<UnitDivision> unitDivisions;
	private List<UnitExponentiation> unitExponentiations;
	private List<PrefixedUnit> prefixedUnits;
	private List<Measure> measures;
	
	
	public Model getModel() {
		return model;
	}
	
	private void setNameSpacePrefixes()
	{
		this.model.setNsPrefix(URINameSpace.SBOL.getPrefix(), URINameSpace.SBOL.getUri().toString());
		this.model.setNsPrefix(URINameSpace.SO.getPrefix(), URINameSpace.SO.getUri().toString());
		this.model.setNsPrefix(URINameSpace.SBO.getPrefix(), URINameSpace.SBO.getUri().toString());
		this.model.setNsPrefix(URINameSpace.GO.getPrefix(), URINameSpace.GO.getUri().toString());
		this.model.setNsPrefix(URINameSpace.CHEBI.getPrefix(), URINameSpace.CHEBI.getUri().toString());
		this.model.setNsPrefix(URINameSpace.EDAM.getPrefix(), URINameSpace.EDAM.getUri().toString());
		this.model.setNsPrefix(URINameSpace.PROV.getPrefix(), URINameSpace.PROV.getUri().toString());
		this.model.setNsPrefix(URINameSpace.OM.getPrefix(), URINameSpace.OM.getUri().toString());
		this.model.setNsPrefix(URINameSpace.RDFS.getPrefix(), URINameSpace.RDFS.getUri().toString());
	}
	
	public void addNameSpacePrefixes(String prefix, URI namespace)
	{
		if (this.model.getNsURIPrefix(namespace.toString())==null)
		{
			this.model.setNsPrefix(prefix, namespace.toString());	
		}
	}
	
	public void addNameSpacePrefixes(URINameSpace ns)
	{
		addNameSpacePrefixes(ns.getPrefix(), ns.getUri());
	}
	

	public SBOLDocument() {
		this.model = ModelFactory.createDefaultModel();
		setNameSpacePrefixes();
	}
	
	public SBOLDocument(URI base) {
		this();
		RDFUtil.setBaseURI(this.model, base);
	}
	
	public SBOLDocument(Model model) {
		this.model = model;
		setNameSpacePrefixes();
	}

	public URI getBaseURI()
	{
		String baseURI =this.model.getNsPrefixURI("");
		if (baseURI!=null)
		{
			return URI.create(baseURI);
		}
		else
		{
			return null;
		}
	}
	
	public List<Component> getComponents() throws SBOLGraphException {
		this.components=addToList(model, this.components, DataModel.Component.uri,Component.class);
		return components;
	}
	
	public Component createComponent(URI uri, List<URI> types) throws SBOLGraphException {

		Component component = new Component(this.model, uri);
		component.setTypes(types);
		if (components == null) {
			components = new ArrayList<Component>();
		}
		components.add(component);
		return component;
	}
	
	public List<Sequence> getSequences() throws SBOLGraphException {
		this.sequences=addToList(model, this.sequences, DataModel.Sequence.uri,Sequence.class);
		return sequences;
	}
	

	public Sequence createSequence(URI uri) throws SBOLGraphException {
		Sequence sequence = new Sequence(this.model, uri);
		if (sequences == null) {
			sequences = new ArrayList<Sequence>();
		}
		sequences.add(sequence);
		return sequence;
	}
	
	public List<CombinatorialDerivation> getCombinatorialDerivations() throws SBOLGraphException {
		this.combinatorialDerivations=addToList(model, this.combinatorialDerivations, DataModel.CombinatorialDerivation.uri,CombinatorialDerivation.class);
		return combinatorialDerivations;
	}

	public CombinatorialDerivation createCombinatorialDerivation(URI uri, URI template) throws SBOLGraphException {

		CombinatorialDerivation combinatorialDerivation= new CombinatorialDerivation(this.model, uri);
		combinatorialDerivation.setTemplate(template);
		addToInMemoryList(combinatorialDerivation, combinatorialDerivations);
		return combinatorialDerivation;
	}
	
	
	public List<Implementation> getImplementations() throws SBOLGraphException {
		this.implementations=addToList(model, this.implementations, DataModel.Implementation.uri,Implementation.class);
		return implementations;
	}

	public Implementation createImplementation(URI uri) throws SBOLGraphException {
		Implementation implementation= new Implementation(this.model, uri);
		addToInMemoryList(implementation, implementations);
		return implementation;
	}
	
	public List<ExperimentalData> getExperimentalData() throws SBOLGraphException {
		this.experimentalDatas=addToList(model, this.experimentalDatas, DataModel.ExperimentalData.uri,ExperimentalData.class);
		return experimentalDatas;
	}

	public ExperimentalData createExperimentalData(URI uri) throws SBOLGraphException {
		ExperimentalData experimentalData= new ExperimentalData(this.model, uri);
		addToInMemoryList(experimentalData, experimentalDatas);
		return experimentalData;
	}
	
	
	public List<org.sbolstandard.entity.Model> getModels() throws SBOLGraphException {
		this.models=addToList(model, this.models, DataModel.Model.uri,org.sbolstandard.entity.Model.class);
		return models;
	}

	public org.sbolstandard.entity.Model createModel(URI uri, URI source, URI framework, URI language) throws SBOLGraphException {
		org.sbolstandard.entity.Model model= new org.sbolstandard.entity.Model(this.model, uri);
		model.setSource(source);
		model.setFramework(framework);
		model.setLanguage(language);
		addToInMemoryList(model, models);
		return model;
	}
	
	public List<Collection> getCollections() throws SBOLGraphException {
		this.collections=addToList(model, this.collections, DataModel.Collection.uri,Collection.class);
		return collections;
	}

	public Collection createCollection(URI uri) throws SBOLGraphException {
		Collection collection= new Collection(this.model, uri);
		addToInMemoryList(collection, collections);
		return collection;
	}
	
	public List<Namespace> getNamespaces() throws SBOLGraphException {
		this.namespaces=addToList(model, this.namespaces, DataModel.Namespace.uri,Namespace.class);
		return namespaces;
	}

	public Collection createNamespace(URI uri) throws SBOLGraphException {
		Namespace namespace= new Namespace(this.model, uri);
		addToInMemoryList(namespace, namespaces);
		return namespace;
	}
	
	public List<Experiment> getExperiments() throws SBOLGraphException {
		this.experiments=addToList(model, this.experiments, DataModel.Experiment.uri,Experiment.class);
		return experiments;
	}

	public Experiment createExperiment(URI uri) throws SBOLGraphException {
		Experiment experiment= new Experiment(this.model, uri);
		addToInMemoryList(experiment, experiments);
		return experiment;
	}
	
	public List<Attachment> getAttachments() throws SBOLGraphException {
		this.attachments=addToList(model, this.attachments, DataModel.Attachment.uri,Attachment.class);
		return attachments;
	}

	public Attachment createAttachment(URI uri, URI source) throws SBOLGraphException {
		Attachment attachment= new Attachment(this.model, uri);
		attachment.setSource(source);
		addToInMemoryList(attachment, attachments);
		return attachment;
	}
	
	public Agent createAgent(URI uri) throws SBOLGraphException {

		Agent agent = new Agent(this.model, uri) {};
		if (agents == null) {
			agents = new ArrayList<Agent>();
		}
		agents.add(agent);
		return agent;
	}
	
	public List<Agent> getAgents() throws SBOLGraphException {
		this.agents=addToList(model, this.agents, ProvenanceDataModel.Agent.uri,Agent.class);
		return agents;
	}
	
	public Plan createPlan(URI uri) throws SBOLGraphException {

		Plan plan = new Plan(this.model, uri) {};
		if (plans == null) {
			plans = new ArrayList<Plan>();
		}
		plans.add(plan);
		return plan;
	}
	
	public List<Plan> getPlans() throws SBOLGraphException {
		this.plans=addToList(model, this.plans, ProvenanceDataModel.Plan.uri,Plan.class);
		return plans;
	}
	
	public Activity createActivity(URI uri) throws SBOLGraphException {

		Activity activity = new Activity(this.model, uri) {};
		if (activities == null) {
			activities = new ArrayList<Activity>();
		}
		activities.add(activity);
		return activity;
	}
	
	public List<Activity> getActivities() throws SBOLGraphException {
		this.activities=addToList(model, this.activities, ProvenanceDataModel.Activity.uri,Activity.class);
		return activities;
	}
	
	private void initialisePrefix(Prefix prefix, String symbol, String name, float factor)
	{
		prefix.setSymbol(symbol);
		prefix.setLabel(name);
		prefix.setFactor(factor);
	}
	
	public SIPrefix createSIPrefix(URI uri, String symbol, String name, float factor) throws SBOLGraphException {

		SIPrefix prefix = new SIPrefix(this.model, uri) {};
		initialisePrefix(prefix, symbol, name, factor);
		if (siPrefixes == null) {
			siPrefixes = new ArrayList<SIPrefix>();
		}
		siPrefixes.add(prefix);
		return prefix;
	}
	
	public List<SIPrefix> getSIPrefixes() throws SBOLGraphException {
		this.siPrefixes=addToList(model, this.siPrefixes, MeasureDataModel.SIPrefix.uri,SIPrefix.class);
		return siPrefixes;
	}
	
	public BinaryPrefix createBinaryPrefix(URI uri, String symbol, String name, float factor) throws SBOLGraphException {

		BinaryPrefix prefix = new BinaryPrefix(this.model, uri) {};
		initialisePrefix(prefix, symbol, name, factor);
		if (binaryPrefixes == null) {
			binaryPrefixes = new ArrayList<BinaryPrefix>();
		}
		binaryPrefixes.add(prefix);
		return prefix;
	}
	
	public List<BinaryPrefix> getBinaryPrefixes() throws SBOLGraphException {
		this.binaryPrefixes=addToList(model, this.binaryPrefixes, MeasureDataModel.BinaryPrefix.uri,BinaryPrefix.class);
		return binaryPrefixes;
	}
	
	public SingularUnit createSingularUnit(URI uri, String symbol, String name) throws SBOLGraphException {

		SingularUnit unit = new SingularUnit(this.model, uri) {};
		unit.setSymbol(symbol);
		unit.setLabel(name);
		if (singularUnits== null) {
			singularUnits = new ArrayList<SingularUnit>();
		}
		singularUnits.add(unit);
		return unit;
	}
	
	public List<SingularUnit> getSingularUnits() throws SBOLGraphException {
		this.singularUnits=addToList(model, this.singularUnits, MeasureDataModel.SingularUnit.uri,SingularUnit.class);
		return singularUnits;
	}
	
	public UnitMultiplication createUnitMultiplication(URI uri, String symbol, String name, URI unit1, URI unit2) throws SBOLGraphException {

		UnitMultiplication unit = new UnitMultiplication(this.model, uri) {};
		unit.setSymbol(symbol);
		unit.setLabel(name);
		unit.setTerm1(unit1);
		unit.setTerm2(unit2);
		if (unitMultiplications== null) {
			unitMultiplications = new ArrayList<UnitMultiplication>();
		}
		unitMultiplications.add(unit);
		return unit;
	}
	
	public List<UnitMultiplication> getUnitMultiplications() throws SBOLGraphException {
		this.unitMultiplications=addToList(model, this.unitMultiplications, MeasureDataModel.UnitMultiplication.uri,UnitMultiplication.class);
		return unitMultiplications;
	}
	
	public UnitDivision createUnitDivision(URI uri, String symbol, String name, URI numerator, URI denominator) throws SBOLGraphException {

		UnitDivision unit = new UnitDivision(this.model, uri) {};
		unit.setSymbol(symbol);
		unit.setLabel(name);
		unit.setNumerator(numerator);
		unit.setDenominator(denominator);
		if (unitDivisions== null) {
			unitDivisions = new ArrayList<UnitDivision>();
		}
		unitDivisions.add(unit);
		return unit;
	}
	
	public List<UnitDivision> getUnitDivisions() throws SBOLGraphException {
		this.unitDivisions=addToList(model, this.unitDivisions, MeasureDataModel.UnitDivision.uri,UnitDivision.class);
		return unitDivisions;
	}
	
	public UnitExponentiation createUnitExponentiation(URI uri, String symbol, String name, URI baseUnit, int exponent) throws SBOLGraphException {

		UnitExponentiation unit = new UnitExponentiation(this.model, uri) {};
		unit.setSymbol(symbol);
		unit.setLabel(name);
		unit.setBase(baseUnit);
		unit.setExponent(exponent);
		if (unitExponentiations== null) {
			unitExponentiations = new ArrayList<UnitExponentiation>();
		}
		unitExponentiations.add(unit);
		return unit;
	}
	
	public List<UnitExponentiation> getUnitExponentiations() throws SBOLGraphException {
		this.unitExponentiations=addToList(model, this.unitExponentiations, MeasureDataModel.UnitExponentiation.uri,UnitExponentiation.class);
		return unitExponentiations;
	}
	
	
	public PrefixedUnit createPrexiedUnit(URI uri, String symbol, String name, URI unitURI) throws SBOLGraphException {

		PrefixedUnit unit = new PrefixedUnit(this.model, uri) {};
		unit.setSymbol(symbol);
		unit.setLabel(name);
		unit.setUnit(unitURI);
		if (prefixedUnits== null) {
			prefixedUnits = new ArrayList<PrefixedUnit>();
		}
		prefixedUnits.add(unit);
		return unit;
	}
	
	public List<PrefixedUnit> getPrefixedUnits() throws SBOLGraphException {
		this.prefixedUnits=addToList(model, this.prefixedUnits, MeasureDataModel.PrefixedUnit.uri,PrefixedUnit.class);
		return prefixedUnits;
	}
	
	public Measure createMeasure(URI uri, float value, URI unit) throws SBOLGraphException {

		Measure measure = new Measure(this.model, uri) {};
		measure.setValue(value);
		measure.setUnit(unit);
		if (measures== null) {
			measures = new ArrayList<Measure>();
		}
		measures.add(measure);
		return measure;
	}
	
	public List<Measure> getMeasures() throws SBOLGraphException {
		this.measures=addToList(model, this.measures, MeasureDataModel.Measure.uri,Measure.class);
		return measures;
	}
	
	public <T extends Identified>Identified getIdentified(URI uri, Class<T> identified) throws SBOLGraphException
	{
		Resource res=this.model.getResource(uri.toString());
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			if (!constructor.isAccessible())
			{
				constructor.setAccessible(true);
			}
			Identified entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}

	}
	
	protected <T extends Identified> Identified createIdentified(Resource res, Class<T> identified) throws SBOLGraphException
	{
		try
		{
			Constructor<T> constructor = identified.getDeclaredConstructor( new Class[] {Resource.class});
			Identified entity=null;
			if (!constructor.isAccessible())
			{
				constructor.setAccessible(true);
			}
			entity= (Identified)constructor.newInstance(new Object[]{res});
			return entity;
		}
		catch (Exception ex)
		{
			throw new SBOLGraphException(ex.getMessage());
		}
	}

	private <T extends Identified>  List<T> addToList(Model model, List<T> items, URI entityType, Class<T> identifiedClass) throws SBOLGraphException
	{
		List<Resource> resources=RDFUtil.getResourcesOfType(model, entityType);
		if (resources!=null && resources.size()>0)
		{
			items=new ArrayList<T>();
		}
		for (Resource resource:resources)
		{
			Identified identified=createIdentified(resource, identifiedClass) ;
			items.add((T)identified);
		}
		return items;
	}
	
	private <T extends Identified> void addToInMemoryList(T item, List<T> list)
	{
		if (list == null) {
			list = new ArrayList<T>();
		}
		list.add(item);
	}
	
	private Set<URI> topLevelResourceTypes;
	public Set<URI> getTopLevelResourceTypes()
	{
		if (topLevelResourceTypes==null)
		{
			List<URI> types = Arrays.asList(DataModel.Component.uri,
					DataModel.Sequence.uri,
					DataModel.Model.uri,
					DataModel.Implementation.uri,
					DataModel.ExperimentalData.uri,
					DataModel.Attachment.uri,
					DataModel.Collection.uri,
					DataModel.Namespace.uri,
					DataModel.CombinatorialDerivation.uri,
					ProvenanceDataModel.Agent.uri,
					ProvenanceDataModel.Plan.uri,
					ProvenanceDataModel.Activity.uri,
					MeasureDataModel.Measure.uri,
					MeasureDataModel.SIPrefix.uri,
					MeasureDataModel.BinaryPrefix.uri,
					MeasureDataModel.SingularUnit.uri,
					MeasureDataModel.UnitMultiplication.uri,
					MeasureDataModel.UnitDivision.uri,
					MeasureDataModel.UnitExponentiation.uri,
					MeasureDataModel.PrefixedUnit.uri
					);
			topLevelResourceTypes=new HashSet<URI>(types);
		}
		return topLevelResourceTypes;
	}
	
	public void addTopLevelResourceType(URI type)
	{
		getTopLevelResourceTypes();
		if (type!=null)
		{
			topLevelResourceTypes.add(type);
		}
	}

	
}
