package Agents;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class Buyer extends Agent{
    public void setup(){
        super.setup();
        // Initializations that the Agent require
        System.out.println("Hello! Agent " + getAID().getName() + " is ready.");
        this.addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                AID receiver= new AID();
                receiver.setLocalName("seller");
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);

                int i = new Random().nextInt(Seller.items.size());
                String it = Seller.items.keySet().toArray()[i].toString();
                msg.setContent(it);
                msg.setConversationId(""+System.currentTimeMillis());
                msg.addReceiver(receiver);
                myAgent.send(msg);
                System.out.println("Buying " + it + ".");
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
