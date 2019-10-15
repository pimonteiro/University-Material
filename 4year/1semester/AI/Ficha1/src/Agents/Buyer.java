package Agents;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class Buyer extends Agent{

    private AID[] known_agents;

    public void setup(){
        super.setup();
        // Initializations that the Agent require
        System.out.println("Hello! Agent " + getAID().getName() + " is ready.");
        this.addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("market");
                template.addServices(sd);

                DFAgentDescription[] results;
                try{
                    results = DFService.search(this.myAgent,template);
                    known_agents = new AID[results.length];
                    for(int j = 0; j < results.length; j++){
                        known_agents[j] = results[j].getName();

                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

                        int i = new Random().nextInt(Seller.items.size());
                        String it = Seller.items.keySet().toArray()[i].toString();
                        msg.setContent(it);
                        msg.setConversationId(""+System.currentTimeMillis());
                        msg.addReceiver(known_agents[j]);
                        myAgent.send(msg);
                        System.out.println("Buying " + it + ".");
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });

        this.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage rec = receive();
                if (rec != null) {
                    if (rec.getPerformative() == ACLMessage.FAILURE)
                        System.out.println("Failure: " + rec.getContent() + ".");
                    else if (rec.getPerformative() == ACLMessage.CONFIRM)
                        System.out.println("Success!");
                    else {
                        System.out.println("Error on message.");
                    }
                }
            }
        });
    }

    public void takeDown(){
        super.takeDown();
        System.out.println("Agent " + getAID().getName() + " terminating.");
    }




}
