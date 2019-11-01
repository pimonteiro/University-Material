package Behaviours;


import Messages.UpdateState;
import Agents.Taxi;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.io.IOException;

public class BH_FinishTrip extends OneShotBehaviour {
    private float final_x;
    private float final_y;

    public BH_FinishTrip(float x, float y){
        super();
        this.final_x = x;
        this.final_y = y;
    }

    @Override
    public void action() { //TODO timer de 1 segundo
        Taxi t = ((Taxi)this.myAgent);
        t.setAvailable(true);
        t.setX_loc(this.final_x);
        t.setY_loc(this.final_y);

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Manager");
        template.addServices(sd);

        DFAgentDescription[] results;
        try {
            results = DFService.search(this.myAgent,template);
            for(int i = 0; i < results.length; i++){
                UpdateState ac = new UpdateState(this.final_x, this.final_y, t.isAvailable());
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(results[i].getName());
                msg.setContentObject(ac);
                myAgent.send(msg);
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
