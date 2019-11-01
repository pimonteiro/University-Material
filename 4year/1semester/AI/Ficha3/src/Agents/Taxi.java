package Agents;

import Behaviours.BH_FinishTrip;
import Behaviours.BH_ReceiveMessage;
import Messages.SendTaxiData;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class Taxi extends Agent {
    private float x_loc;
    private float y_loc;
    private boolean available;

    public void setup(){
        super.setup();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("Taxi");
        dfd.addServices(sd);

        Random r = new Random();
        this.x_loc = (float)r.nextInt((100));
        this.y_loc = (float)r.nextInt((100));

        try{
            DFService.register(this,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        this.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Manager");
                template.addServices(sd);

                DFAgentDescription[] result;
                try{
                    result = DFService.search(myAgent, template);
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    SendTaxiData dt = new SendTaxiData(((Taxi)this.myAgent).getX_loc(), ((Taxi)this.myAgent).getY_loc());
                    msg.setSender(result[0].getName());
                    this.myAgent.send(msg);
                    System.out.println(this.myAgent.getAID().toString() + ": Sending first Data!");
                } catch (FIPAException e) {
                    e.printStackTrace();
                }
            }
        });
        this.addBehaviour(new BH_ReceiveMessage(this));
    }

    public float getX_loc() {
        return x_loc;
    }

    public void setX_loc(float x_loc) {
        this.x_loc = x_loc;
    }

    public float getY_loc() {
        return y_loc;
    }

    public void setY_loc(float y_loc) {
        this.y_loc = y_loc;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

}
