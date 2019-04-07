/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica.Agents;

import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.abs.AbsPredicate;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.lang.sl.SLVocabulary;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.proto.AchieveREResponder;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import practica.Ontology.Have;
import practica.Ontology.MassWater;
import practica.Ontology.RiverOntology;
import practica.Ontology.TakeWater;
import practica.Ontology.ThrowWater;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.util.leap.*;
/**
 *
 * @author edacal
 */
public class River extends Agent{   
    private final Random ran;
    private final Ontology ontology;
    private final Codec codec;
    private static ArrayList<MassWater> sections;
    
    public River() {
        ontology = RiverOntology.getInstance(); 
        codec = new SLCodec();
        sections = new ArrayList<>();
        ran = new Random();
    } 
    
    /************************************************************************/
    
    @Override
    protected void setup() {
        getContentManager().registerLanguage(codec);
        getContentManager().registerOntology(ontology);
        /*MessageTemplate mt = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST); //!!!!!!!!!!!!!!!!!!!!!!!!!
        addBehaviour(new AchieveREResponder(this, mt) { 
            @Override
            protected ACLMessage prepareResponse(ACLMessage request) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                try {
                    Object content = request.getContentObject();

                    switch (request.getPerformative()) {

                        case (ACLMessage.REQUEST):

                            System.out.println("Request from " + request.getSender().getLocalName());

                            if (content instanceof TakeWater)
                                addBehaviour(new HandleTakeWater(myAgent, request));
                            else if (content instanceof ThrowWater)
                                addBehaviour(new HandleThrowWater(myAgent, request));
                            else
                                replyNotUnderstood(request);
                            break;

                        default: replyNotUnderstood(request);
                    }
                }
                catch(Exception ex) { ex.printStackTrace(); }
                return null;   
            }
             @Override
            protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
                return null;
            }

            private void replyNotUnderstood(ACLMessage request) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });*/
       
        /*MessageTemplate mtreq = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST); 
        addBehaviour(new AchieveREResponder(this, mtreq) { 
            @Override
            protected ACLMessage prepareResponse(ACLMessage request) {
                ContentElement content;
                try {
                    content = getContentManager().extractContent(request);
                    Concept action = ((Action)content).getAction();
                    if(action instanceof ThrowWater) {
                        ThrowWater cw = (ThrowWater)((Action)content).getAction();
                        cw.getMassWater();
                        int section = cw.getSection();
                        MassWater mw = cw.getMassWater();
                        sections.set(section, mw);
                        
                    }
                } catch (Codec.CodecException | OntologyException ex) {
                    Logger.getLogger(River.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
           
            @Override
            protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {
                return null;
            }
        });*/
        addBehaviour(new ReceiveMessages(this));
        addBehaviour(new WaterRun());
    }
    
    class RegisterInDF extends OneShotBehaviour { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
// ---------------------------------------------  Register in the DF for the client agent
//                                                be able to retrieve its AID
      RegisterInDF(Agent a) {
         super(a);
      }

      public void action() {

         ServiceDescription sd = new ServiceDescription();
         //sd.setType(RIVER_AGENT);
         sd.setName(getName());
         sd.setOwnership("Prof6802");
         DFAgentDescription dfd = new DFAgentDescription();
         dfd.setName(getAID());
         dfd.addServices(sd);
         try {
            DFAgentDescription[] dfds = DFService.search(myAgent, dfd);
            if (dfds.length > 0 ) {
               DFService.deregister(myAgent, dfd);
            }
            DFService.register(myAgent, dfd);
            System.out.println(getLocalName() + " is ready.");
         }
         catch (Exception ex) {
            System.out.println("Failed registering with DF! Shutting down...");
            ex.printStackTrace();
            doDelete();
         }
      }
   }
       
    class ReceiveMessages extends CyclicBehaviour {
// -----------------------------------------------  Receive requests and queries from client
//                                                  agent and launch appropriate handlers

        public ReceiveMessages(Agent a) {
            super(a);
        }

        public void action() {
            ACLMessage msg = receive();
            if (msg == null) { block(); return; }
            try {
                Object content = msg.getContentObject();

                switch (msg.getPerformative()) {

                    case (ACLMessage.REQUEST):

                        System.out.println("Request from " + msg.getSender().getLocalName());

                        if (content instanceof TakeWater)
                            addBehaviour(new HandleTakeWater(myAgent, msg));
                        else if (content instanceof ThrowWater)
                            addBehaviour(new HandleThrowWater(myAgent, msg));
                        else
                            replyNotUnderstood(msg);
                        break;

                    default: replyNotUnderstood(msg);
                    }
            }
            catch(Exception ex) { ex.printStackTrace(); }
        }
    }

    class HandleTakeWater extends OneShotBehaviour {
// ----------------------------------------------------  Handler for a TakeWater request

        private ACLMessage request;

        HandleTakeWater(Agent a, ACLMessage request) {
            super(a);
            this.request = request;
        }

        public void action() {
            try {
                TakeWater tw = (TakeWater) request.getContentObject();
                int section = tw.getSection();
                float demandVolume = tw.getVolume();
                float actualVolume = sections.get(section).getVolume(); 
                if(actualVolume >= demandVolume) {
                    MassWater r =  sections.get(section);
                    r.setVolume(demandVolume);
                    sections.get(section).setVolume(actualVolume - demandVolume);

                    ACLMessage reply = request.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContentObject(r);
                    send(reply);

                    System.out.println("An industry has taken some water from the river!");
                }
                else { 
                    ACLMessage reply = request.createReply();
                    reply.setPerformative(ACLMessage.REFUSE);
                    System.out.println("Not enough water!");
                    send(reply);
                }
            }
            catch(Exception ex) { ex.printStackTrace(); }
        }
    }
    
    class HandleThrowWater extends OneShotBehaviour {
// ----------------------------------------------------  Handler for a ThrowWater request

        private ACLMessage request;

        HandleThrowWater(Agent a, ACLMessage request) {
            super(a);
            this.request = request;
        }

        public void action() {
            try {
                ThrowWater tw = (ThrowWater) request.getContentObject();
                int section = tw.getSection();
                MassWater r1 = tw.getMassWater();
                MassWater r2 = sections.get(section);
                MassWater f = new MassWater();
                f.mixWater(r1,r2);
                sections.set(section,f);
                
                ACLMessage reply = request.createReply();
                reply.setPerformative(ACLMessage.INFORM);
                send(reply);
                
                System.out.println("The EDAR has thrown some water to the river!");
            }
            catch(Exception ex) { ex.printStackTrace(); }
        }
    }
    
       void replyNotUnderstood(ACLMessage msg) {
// -----------------------------------------

      try {
         java.io.Serializable content = msg.getContentObject();
         ACLMessage reply = msg.createReply();
         reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
         reply.setContentObject(content);
         send(reply);
      }
      catch(Exception ex) { ex.printStackTrace(); }
   }
       
    /************************************************************************/
    private void newWater() {
        if(sections.isEmpty()) {
            for(int i = 0; i < 10; ++i) {
                MassWater mw = new MassWater();
                mw.setVolume(ran.nextFloat()*20);
                mw.setDBO(0.0f);
                mw.setSS(0.0f);
                mw.setTN(0.0f);
                mw.setTS(0.0f);
                sections.add(mw);
            }
        }
        else {
            MassWater mw = new MassWater();
            mw.setVolume(ran.nextFloat()*20);
            mw.setDBO(0.0f);
            mw.setSS(0.0f);
            mw.setTN(0.0f);
            mw.setTS(0.0f);
            sections.set(0,mw);
        }
    }
    private class WaterRun extends CyclicBehaviour {
        @Override
        public void action() {
            System.out.println("The River is flowing!");
            newWater();
            for(int i = sections.size() - 1; i > 0; --i) {
                sections.set(i, sections.get(i - 1));
            }
        }   
    }
}
    
    

