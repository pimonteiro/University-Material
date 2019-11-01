package Agents;

import Messages.AskForTaxi;
import Messages.FinishTrip;
import Messages.UpdateState;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.Random;

public class Client extends Agent {
    private float x_pos;
    private float y_pos;
    private float x_dest;
    private float y_dest;

    public void setup(){
        super.setup();
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("Client");
        dfd.addServices(sd);

        try{
            DFService.register(this,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        Random r = new Random();
        this.x_pos = (float)r.nextInt((100));
        this.y_pos = (float)r.nextInt((100));
        this.x_dest = (float)r.nextInt((100)); //TODO verificar se não cai em cima da prórpia posição
        this.y_dest = (float)r.nextInt((100));

        this.addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                System.out.println(this.myAgent.getAID().toString() + ": Starting journey!");
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Manager");
                template.addServices(sd);

                Client c = ((Client)this.myAgent);

                DFAgentDescription[] results;
                try {
                    results = DFService.search(this.myAgent,template);
                    for(int i = 0; i < results.length; i++){
                        AskForTaxi ac = new AskForTaxi(this.myAgent.getAID(), c.getY_pos(), c.getY_pos(), c.getX_dest(), c.getY_dest());
                        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                        msg.addReceiver(results[i].getName());
                        msg.setContentObject(ac);
                        send(msg);
                        System.out.println(this.myAgent.getAID().toString() + ": Waiting for Taxi!");
                    }
                } catch (FIPAException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                System.out.println(this.myAgent.getAID().toString() + ": Received a Message!");
                ACLMessage msg = myAgent.receive();
                if(msg != null) {
                    try {
                        FinishTrip content = (FinishTrip) msg.getContentObject();
                        ((Client) this.myAgent).finishTrip();
                        System.out.println(this.myAgent.getAID().toString() + ": Acabei a viagem!");
                        System.out.println(((Client) this.myAgent).toString());
                    } catch (UnreadableException e) {
                        e.printStackTrace();
                    }
                    this.myAgent.doDelete();
                }
                block();
                return;
            }
        });
    }

    public void takeDown(){

    }
    public float getX_pos() {
        return x_pos;
    }

    public void setX_pos(float x_pos) {
        this.x_pos = x_pos;
    }

    public float getY_pos() {
        return y_pos;
    }

    public void setY_pos(float y_pos) {
        this.y_pos = y_pos;
    }

    public float getX_dest() {
        return x_dest;
    }

    public void setX_dest(float x_dest) {
        this.x_dest = x_dest;
    }

    public float getY_dest() {
        return y_dest;
    }

    public void setY_dest(float y_dest) {
        this.y_dest = y_dest;
    }

    public void finishTrip(){
        this.x_pos = this.x_dest;
        this.y_pos = this.y_dest;
    }
    @Override
    public String toString() {
        return "Agents.Client{" +
                "x_pos=" + x_pos +
                ", y_pos=" + y_pos +
                ", x_dest=" + x_dest +
                ", y_dest=" + y_dest +
                '}';
    }
}
