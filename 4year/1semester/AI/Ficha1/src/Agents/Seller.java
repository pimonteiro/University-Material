package Agents;

import jade.core.*;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Date;
import java.util.HashMap;

public class Seller extends Agent{

    public static HashMap<String,Integer> items = new HashMap<>(){
        {
            put("Chocolate",20);
            put("Leite",5);
            put("Arroz",10);
            put("Massa",2);
        }
    };
    private int profit = 0;

    public void setup(){
        super.setup();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("market");
        sd.setName("seller");
        dfd.addServices(sd);

        try {
            DFService.register(this,dfd);
        } catch (FIPAException e){
            e.printStackTrace();
        }

        // Initializations that the Agent require
        System.out.println("Hello! Agent " + getAID().getName() + " is ready.");
        this.addBehaviour(new TickerBehaviour(this, 10000) {
            @Override
            protected void onTick() {
                System.out.println("[" + new Date().getTime() + "] " + ((Seller) this.myAgent).profit + "$");
            }
        });
        this.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage rec = receive();
                if (rec != null){
                    AID receiver = rec.getSender();
                    ACLMessage msg = new ACLMessage();
                    if((Seller.items.getOrDefault(rec.getContent(),-1)) != -1){
                        msg.setPerformative(ACLMessage.CONFIRM);
                        msg.addReceiver(receiver);
                        msg.setConversationId(""+System.currentTimeMillis());
                        myAgent.send(msg);

                        System.out.println("Agent " + rec.getSender().getLocalName() + "(" + rec.getSender().getName() + ") just bought " + rec.getContent() + ".");
                        ((Seller) this.myAgent).updateProfit(rec.getContent());
                    }
                    else{
                        msg.setPerformative(ACLMessage.FAILURE);
                        msg.setContent("Bad item.");
                        msg.addReceiver(receiver);
                        msg.setConversationId(""+System.currentTimeMillis());
                        myAgent.send(msg);
                    }
                }
            }
        });
    }

    public void takeDown(){
        super.takeDown();

        try {
            DFService.deregister(this);
        } catch (FIPAException e){
            e.printStackTrace();
        }

        System.out.println("Agent " + getAID().getName() + " terminating.");
    }

    public void updateProfit(String item){
        int p = Seller.items.get(item);
        this.profit += p;
    }
}
