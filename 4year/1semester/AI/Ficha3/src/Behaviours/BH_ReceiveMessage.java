package Behaviours;

import Handlers.HandleAskForTaxi;
import Handlers.HandleSendTaxi;
import Handlers.HandleSendTaxiData;
import Handlers.HandleUpdateState;
import Messages.AskForTaxi;
import Messages.SendTaxi;
import Messages.SendTaxiData;
import Messages.UpdateState;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class BH_ReceiveMessage extends CyclicBehaviour {

    public BH_ReceiveMessage(Agent a){
        super(a);
    }
    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg == null) {
            block();
            return;
        }
        try{
            Object content = msg.getContentObject();
            switch (msg.getPerformative()){
                case (ACLMessage.REQUEST):
                    if(content instanceof AskForTaxi)
                        this.myAgent.addBehaviour(new HandleAskForTaxi(myAgent,msg));
                    else if(content instanceof SendTaxi)
                        this.myAgent.addBehaviour(new HandleSendTaxi(myAgent,msg));
                case (ACLMessage.INFORM):
                    if(content instanceof SendTaxiData)
                        this.myAgent.addBehaviour(new HandleSendTaxiData(myAgent,msg));
                    else if(content instanceof UpdateState)
                        this.myAgent.addBehaviour(new HandleUpdateState(myAgent,msg));
            }
        } catch (UnreadableException e) {
            e.printStackTrace();
        }
    }
}
