/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.Agents;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.AchieveREResponder;
import jade.proto.ContractNetInitiator;
import jade.proto.ContractNetResponder;
import java.util.Date;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica.Ontology.CleanWater;
import practica.Ontology.Have;
import practica.Ontology.MassWater;
import practica.Ontology.RiverOntology;
import practica.Ontology.TakeWater;
import practica.Ontology.ThrowWater;

/**
 *
 * @author edacal
 */
public class Industry_Scenari2 extends Agent{
    private final Ontology ontology;
    private final Codec codec;
    private final Random ran;
   
    private int section; //Section were the indistry takes the water
    private float maxVolume; //Max volume that can be stored in the water tank
    private MassWater storedWater; //Mater Mass representing the water stored in the tank.
    private float DBO_affect; /*Values representing how much the industry "dirties" the water it recieves.*/
    private float DQO_affect;
    private float TN_affect;
    private float TS_affect;
    private float SS_affect;
    private float consumingRate;
    
    public Industry_Scenari2() {
        ontology = RiverOntology.getInstance(); 
        codec = new SLCodec();
        ran = new Random();
    }
    @Override
    protected void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        maxVolume = ran.nextFloat() * 250;
        storedWater = new MassWater();
        DBO_affect = ran.nextFloat();
        TN_affect = ran.nextFloat();
        TS_affect = ran.nextFloat();
        SS_affect = ran.nextFloat();
        DBO_affect = ran.nextFloat();
        section = ran.nextInt(10);
        consumingRate = ran.nextFloat()*10;
       
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName( getAID() ); 
        ServiceDescription sd  = new ServiceDescription();
        sd.setType( "Industry" );
        sd.setName( getLocalName() );
        dfd.addServices(sd);
        
        try {  
            DFService.register(this, dfd );  
        }
        catch (FIPAException fe) { fe.printStackTrace(); }
        
        addBehaviour(new DirtyWater());
    }
    
    
    ///////////////EDAR////////////////////////
       class WaitEDARResponse extends ParallelBehaviour {
// ----------------------------------------------------  launch a SimpleBehaviour to receive
//                                                       servers response and a WakerBehaviour
//                                                       to terminate the waiting if there is
//                                                       no response from the server
      WaitEDARResponse(Agent a) {

         super(a, 1);

         addSubBehaviour(new ReceiveEDARResponse(myAgent));

         addSubBehaviour(new WakerBehaviour(myAgent, 5000) {

            protected void handleElapsedTimeout() {
               System.out.println("\n\tNo response from server. Please, try later!");
            }
         });
      }
   }


   class ReceiveEDARResponse extends SimpleBehaviour {
// -----------------------------------------------  // Receive and handle server responses

      private boolean finished = false;

      ReceiveEDARResponse(Agent a) {
         super(a);
      }

      public void action() {
         AID edar = (new AID("EDAR", AID.ISLOCALNAME));
         ACLMessage msg = receive(MessageTemplate.MatchSender(edar));
         

         if (msg == null) {block(); return; } 
         if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD){
            System.out.println("\n\n\tResponse from EDAR: NOT UNDERSTOOD!");
         }
         else if (msg.getPerformative() == ACLMessage.REFUSE){
            System.out.println("\n\n\tResponse from EDAR: CANNOT HANDLE THAT WATER MASS!");
         }
         else if (msg.getPerformative() == ACLMessage.INFORM){
            try {
               Object content = msg.getContentObject();
               if (!(content instanceof MassWater)) {
                    storedWater = (MassWater)content;
                    System.out.println("\n\n\tResponse from EDAR: TANK EMPTIER!");
               }
               else {
                  System.out.println("\n\tUnable de decode response from EDAR!");
               }
            }
            catch (Exception e) { e.printStackTrace(); }
            finished = true;
        }
    }
      public boolean done() { return finished; }
   }

   ///////////////RIVER////////////////////////
   
    class WaitRiverResponse extends ParallelBehaviour {
// ----------------------------------------------------  launch a SimpleBehaviour to receive
//                                                       servers response and a WakerBehaviour
//                                                       to terminate the waiting if there is
//                                                       no response from the server
      WaitRiverResponse(Agent a) {

         super(a, 1);

         addSubBehaviour(new ReceiveRiverResponse(myAgent));

         addSubBehaviour(new WakerBehaviour(myAgent, 5000) {

            protected void handleElapsedTimeout() {
               System.out.println("\n\tNo response from server. Please, try later!");
            }
         });
      }
   }
      class ReceiveRiverResponse extends SimpleBehaviour {
// -----------------------------------------------  // Receive and handle server responses

      private boolean finished = false;

      ReceiveRiverResponse(Agent a) {
         super(a);
      }

      public void action() {
         AID river = (new AID("River", AID.ISLOCALNAME));
         ACLMessage msg = receive(MessageTemplate.MatchSender(river));
         

         if (msg == null) {block(); return; } 
         if (msg.getPerformative() == ACLMessage.NOT_UNDERSTOOD){
            System.out.println("\n\n\tResponse from server: NOT UNDERSTOOD!");
         }
         else if (msg.getPerformative() == ACLMessage.REFUSE){
            System.out.println("\n\n\tResponse from server: NOT ENOUGH WATER!");
         }
         else if (msg.getPerformative() == ACLMessage.INFORM){
            try {
               Object content = msg.getContentObject();
               if (content instanceof MassWater) {
                  MassWater result = (MassWater) content;
                  storedWater.mixWater(result,storedWater);
                  System.out.println("OMPLINT DIPOSIT. AIGUA TOTAL: %f"+storedWater.getVolume());
               }
               else {
                  System.out.println("\n\tUnable de decode response from server!");
               }
            }
            catch (Exception e) { e.printStackTrace(); }
            finished = true;
        }
    }
      public boolean done() { return finished; }
   }

    private class DirtyWater extends CyclicBehaviour {
        @Override
        public void action() {
            if(storedWater.getVolume() < (maxVolume-consumingRate)){
                ACLMessage takeWaterMessage = new ACLMessage(ACLMessage.REQUEST); 
                takeWaterMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
                takeWaterMessage.setLanguage(codec.getName());
                takeWaterMessage.setOntology(ontology.getName());
                takeWaterMessage.addReceiver(new AID("River", AID.ISLOCALNAME));
                TakeWater tw = new TakeWater();
                tw.setSection(section);
                tw.setVolume(consumingRate);
                try{
                    //getContentManager().fillContent(takeWaterMessage, new Action(new AID("Plant", AID.ISLOCALNAME), tw));
                    takeWaterMessage.setContentObject(tw);
                } 
                catch(Exception ex) { ex.printStackTrace(); }
                send(takeWaterMessage);
                addBehaviour(new WaitRiverResponse(myAgent));
                block();
            }
            else{
                ACLMessage throwWaterMessage = new ACLMessage(ACLMessage.REQUEST); 
                throwWaterMessage.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST); 
                throwWaterMessage.setLanguage(codec.getName());
                throwWaterMessage.setOntology(ontology.getName());
                throwWaterMessage.addReceiver(new AID("EDAR", AID.ISLOCALNAME));
                ThrowWater tw = new ThrowWater();
                tw.setMassWater(storedWater);
                try{
                    //getContentManager().fillContent(takeWaterMessage, new Action(new AID("Plant", AID.ISLOCALNAME), tw));
                    throwWaterMessage.setContentObject(tw);
                } 
                catch(Exception ex) { ex.printStackTrace(); }
                send(throwWaterMessage);
                addBehaviour(new WaitEDARResponse(myAgent));
                block();
            }
        }        
    } 
}
