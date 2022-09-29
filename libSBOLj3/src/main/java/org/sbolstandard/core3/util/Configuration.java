package org.sbolstandard.core3.util;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;

public class Configuration {
	private static Configuration configuration = null;
	
	private boolean enforceOneToOneRelationships=true;
	private boolean validateAfterSettingProperties=true;
	private boolean validateBeforeSaving=true;
	private boolean validateAfterReadingSBOLDocuments=true;
	private boolean validateRecommendedRules=true;

	
	public boolean isValidateAfterSettingProperties() {
		return validateAfterSettingProperties;
	}

	public void setValidateAfterSettingProperties(boolean validateAfterSettingProperties) {
		this.validateAfterSettingProperties = validateAfterSettingProperties;
	}

	public boolean isValidateBeforeSaving() {
		return validateBeforeSaving;
	}

	/**
	 * Set this parameter to enforce validating SBOL graphs before writing to the disk.
	 * @param validateBeforeSaving
	 */
	public void setValidateBeforeSaving(boolean validateBeforeSaving) {
		this.validateBeforeSaving = validateBeforeSaving;
	}

	public boolean isValidateRecommendedRules() {
		return validateRecommendedRules;
	}

	public void setValidateRecommendedRules(boolean validateRecommendedRules) {
		this.validateRecommendedRules = validateRecommendedRules;
	}

	
	public boolean validateAfterReadingSBOLDocuments() {
		return validateAfterReadingSBOLDocuments;
	}

	public void setValidateAfterReadingSBOLDocuments(boolean validateAfterReadingSBOLDocuments) {
		this.validateAfterReadingSBOLDocuments = validateAfterReadingSBOLDocuments;
	}

	public boolean enforceOneToOneRelationships() {
		return this.enforceOneToOneRelationships;
	}

	/***
	 * When set to true, it will enforce one-to-one relationships for SBOL graphs that are generated by other tools. For performance reasons,
	 * if the graphs are created by libSBOLj, then this property can be set to false.
	 * @param enforceOneToOneRelationships
	 */
	public void setEnforceOneToOneRelationships(boolean enforceOneToOneRelationships) {
		this.enforceOneToOneRelationships = enforceOneToOneRelationships;
	}

	private Model edamOntology=null;
	private Model soOntology=null;
	
	private Configuration()
	{
		this.edamOntology=SBOLUtil.getModelFromFileResource("edam.owl.reduced", Lang.TURTLE);
		this.soOntology=SBOLUtil.getModelFromFileResource("so-simple.owl.reduced", Lang.TURTLE);	
	}
	
	private static class SingletonHelper {
        private static final Configuration INSTANCE = new Configuration();
    }
	
	public static Configuration getInstance()
	{
		return SingletonHelper.INSTANCE;
	}
	
	public Model getEDAMOntology()
	{
		return this.edamOntology;   
	}
	
	public Model getSOOntology()
	{
		return this.soOntology;   
	}
	
	/*public static Configuration getConfiguration()
	{
		if (configuration == null)
		{
			configuration=new Configuration();
		}
		return configuration;
	}
	*/

	
	/*public enum PropertyValidationType{
		ValidateBeforeSavingSBOLDocuments,
		ValidateAfterSettingProperties,
		NoValidation
	}*/
}
