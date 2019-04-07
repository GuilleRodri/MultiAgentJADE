/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.Agents;


import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.ContractNetResponder;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica.Ontology.RiverOntology;
import practica.Ontology.MassWater;
import practica.Ontology.ThrowWater;

/**
 *
 * @author edacal
 */
public class Sewage_Plant_Scenari2 extends Agent{
    public Sewage_Plant_Scenari2() {
        ontology = RiverOntology.getInstance(); 
        codec = new SLCodec();
        ran = new Random();
        section = 10;
    }
    
     @Override
    protected void setup() { 
        
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        MessageTemplate mt = ContractNetResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_PROPOSE);
        addBehaviour(new ContractNetResponder(this, mt) { 
           @Override
           protected ACLMessage handleCfp(ACLMessage cfp) 
           {
               cfp.createReply();
               if(ran.nextInt(10) < 5) cfp.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
               else cfp.setPerformative(ACLMessage.REJECT_PROPOSAL);
               return cfp;
           }
        });
    }
    
    
    private class PurifyWater extends OneShotBehaviour {
        public PurifyWater(MassWater mw) {
            this.mw = mw;
        
        }
        @Override
        public void action() 
        {
            mw.setDBO(0.0f);
            mw.setDQO(0.0f);
            mw.setSS(0.0f);
            mw.setTN(0.0f);
            mw.setTS(0.0f);
            ACLMessage throw_message = new ACLMessage(ACLMessage.REQUEST); 
            throw_message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            throw_message.setLanguage(codec.getName());
            throw_message.setOntology(ontology.getName());
            throw_message.addReceiver(new AID("River", AID.ISLOCALNAME));
            ThrowWater tw = new ThrowWater();
            tw.setMassWater(mw);
            try {
                getContentManager().fillContent(throw_message, new Action(new AID("River", AID.ISLOCALNAME), tw));
                myAgent.addBehaviour(new AchieveREInitiator(myAgent,throw_message));
            } catch (Codec.CodecException | OntologyException ex) {
                Logger.getLogger(Sewage_Plant_Scenari2.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        MassWater mw;
        
    }
    private final Ontology ontology;
    private final Codec codec;
    private final int section;
    private final Random ran;
    
}
