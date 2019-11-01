package Handlers;

import Agents.Taxi;
import Messages.AskForTaxi;
import Messages.FinishTrip;
import Messages.SendTaxi;
import Messages.UpdateState;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.io.IOException;

public class HandleSendTaxi extends OneShotBehaviour {
    private ACLMessage request;

    public HandleSendTaxi(Agent a, ACLMessage msg) {
        super(a);
        this.request = msg;
    }

    @Override
    public void action() {
        try {
            SendTaxi cont = (SendTaxi) this.request.getContentObject();
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            UpdateState ct1 = new UpdateState(((Taxi) this.myAgent).getX_loc(),((Taxi) this.myAgent).getY_loc(),false);
            msg.setContentObject(ct1);
            msg.setSender(this.request.getSender());
            this.myAgent.send(msg);
            System.out.println(this.myAgent.getAID().toString() + ": updating state on manager #1");
            ((Taxi) this.myAgent).setAvailable(false);

            wait(1000);

            ((Taxi) this.myAgent).setX_loc(cont.getX_dest());
            ((Taxi) this.myAgent).setY_loc(cont.getY_dest());
            ((Taxi) this.myAgent).setAvailable(true);
            UpdateState ct = new UpdateState(cont.getX_dest(),cont.getY_dest(),true);
            msg.addReceiver(this.request.getSender());
            msg.setContentObject(ct);
            this.myAgent.send(msg);
            System.out.println(this.myAgent.getAID().toString() + ": updating state on manager #2");

            msg = new ACLMessage(ACLMessage.INFORM);
            FinishTrip ft = new FinishTrip();
            msg.addReceiver(cont.getClient());
            msg.setContentObject(ft);
            this.myAgent.send(msg);
            System.out.println(this.myAgent.getAID().toString() + ": telling " + cont.getClient().toString() + " trip is over.");

        } catch (UnreadableException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
