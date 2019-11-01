package Handlers;

import Agents.Manager;
import Messages.AskForTaxi;
import Messages.SendTaxi;
import Messages.SendTaxiData;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HandleSendTaxiData extends OneShotBehaviour {
    private ACLMessage request;

    public HandleSendTaxiData(Agent a, ACLMessage msg) {
        super(a);
        this.request = msg;
    }

    @Override
    public void action() {
        SendTaxiData cont;
        try {
            cont = (SendTaxiData) this.request.getContentObject();
            ((Manager)this.myAgent).addTaxiInfo(this.request.getSender(),cont.getX_pos(),cont.getY_pos());
            System.out.println("AQUI:" + this.request.getSender());

        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
