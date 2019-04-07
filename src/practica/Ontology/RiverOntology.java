/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.Ontology;

import jade.content.onto.BasicOntology;
import jade.content.onto.CFReflectiveIntrospector;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PredicateSchema;
import jade.content.schema.PrimitiveSchema;

/**
 *
 * @author edacal
 */
public class RiverOntology extends Ontology{
      // The name identifying this ontology
      public static final String ONTOLOGY_NAME = "River_Ontology"; 
        // VOCABULARY 
      public static final String MASS_WATER = "MassWater";
      public static final String VOLUME = "Volume"; 
      public static final String SS = "SS"; 
      public static final String DBO = "DBO";
      public static final String DQO = "DQO"; 
      public static final String TN = "TN"; 
      public static final String TS = "TS";
      
      public static final String HAVE = "Have"; 
      public static final String HAVE_VOLUME = "volume"; 
      public static final String HAVE_SS = "SS"; 
      public static final String HAVE_DBO = "DBO"; 
      public static final String HAVE_DQO = "DQO";
      public static final String HAVE_TN = "TN"; 
      public static final String HAVE_TS = "TS"; 
      public static final String AT = "at"; 
      public static final String AT_SECTION = "section"; 
      public static final String TAKE = "take";
      public static final String TAKE_SECTION = "section";
      public static final String THROW_SECTION = "section";
      public static final String TAKE_VOLUME = "volume";
      public static final String THROW = "throw";
      public static final String THROW_WATER = "Water";
      public static final String CLEAN = "clean";
      public static final String CLEAN_WATER = "water";
      public static final String CLEAN_SECTION = "section";
      
      
      // The singleton instance of this ontology
      private static final RiverOntology theInstance = new RiverOntology(); 
     
      // This is the method to access the river ontology object 
      public static RiverOntology getInstance() { 
          return theInstance; 
        } 
      // Private constructor  
       private RiverOntology() { 
              
          // The river ontology extends the basic ontology
          super(ONTOLOGY_NAME, BasicOntology.getInstance(), new CFReflectiveIntrospector()); 
          try
          { 
            add(new ConceptSchema(MASS_WATER), MassWater.class); 
            add(new AgentActionSchema(CLEAN), CleanWater.class); 
            add(new AgentActionSchema(THROW), ThrowWater.class);
            add(new AgentActionSchema(TAKE), TakeWater.class);
            add(new PredicateSchema(HAVE),Have.class);
            add(new PredicateSchema(AT),At.class);
            
            
            // Structure of the schema for the MassWater concept 
            ConceptSchema cs = (ConceptSchema) getSchema(MASS_WATER); 
            cs.add(VOLUME, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL); 
            cs.add(SS, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL); 
            cs.add(DBO, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
            cs.add(TN, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
            cs.add(TS, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
            cs.add(DQO, (PrimitiveSchema) getSchema(BasicOntology.FLOAT), ObjectSchema.OPTIONAL);
            //Structure of the schema for the HAVE predicate
            PredicateSchema ps = (PredicateSchema) getSchema(HAVE); 
            ps.add(HAVE_VOLUME, (PrimitiveSchema) getSchema(BasicOntology.FLOAT)); 
            ps.add(HAVE_SS, (PrimitiveSchema) getSchema(BasicOntology.FLOAT)); 
            ps.add(HAVE_DBO, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
            ps.add(HAVE_TN, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
            ps.add(HAVE_TS, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
            ps.add(HAVE_DQO, (PrimitiveSchema) getSchema(BasicOntology.FLOAT));
            PredicateSchema ap = (PredicateSchema) getSchema(AT);
            ap.add(AT_SECTION, (PrimitiveSchema) getSchema(BasicOntology.INTEGER));
            // Structure of the schema for the Take agent action 
            AgentActionSchema as = (AgentActionSchema) getSchema(CLEAN); 
            as.add(CLEAN_WATER, (ConceptSchema) getSchema(MASS_WATER),ObjectSchema.MANDATORY); 
            as.add(CLEAN_SECTION, (PrimitiveSchema) getSchema(BasicOntology.INTEGER),ObjectSchema.MANDATORY); 
            AgentActionSchema tw = (AgentActionSchema) getSchema(THROW); 
            as.add(THROW_WATER, (ConceptSchema) getSchema(MASS_WATER),ObjectSchema.MANDATORY); 
            as.add(THROW_SECTION, (PrimitiveSchema) getSchema(BasicOntology.INTEGER),ObjectSchema.MANDATORY); 
            AgentActionSchema taw = (AgentActionSchema) getSchema(TAKE); 
            taw.add(TAKE_SECTION, (PrimitiveSchema) getSchema(BasicOntology.INTEGER),ObjectSchema.MANDATORY);
            taw.add(TAKE_VOLUME,(PrimitiveSchema) getSchema(BasicOntology.FLOAT),ObjectSchema.MANDATORY);
          }  
      catch(OntologyException oe) { 
          }  
        } 
} 

