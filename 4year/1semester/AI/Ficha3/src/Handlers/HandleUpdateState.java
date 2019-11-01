package Handlers;

import Agents.Manager;
import Messages.UpdateState;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HandleUpdateState extends OneShotBehaviour {
    private ACLMessage request;

    public HandleUpdateState(Agent a, ACLMessage req) {
        super(a);
        this.request = req;
    }

    @Override
    public void action() {
        try {
            UpdateState cont = (UpdateState) this.request.getContentObject();
            ((Manager) this.myAgent).updateTaxi(this.request.getSender(),cont.getFinal_x(), cont.getFinal_y(), cont.isState());
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }

}
