package Agents;

import Behaviours.BH_ReceiveMessage;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import utils.Pair;

import java.util.HashMap;
import java.util.Map;

public class Manager extends Agent {

    private Map<AID, Pair<Float,Float>> taxis;
    private Map<AID, Boolean> availables;
    public void setup(){
        super.setup();

        this.taxis = new HashMap<>();
        this.availables = new HashMap<>();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("Manager");
        dfd.addServices(sd);

        try{
            DFService.register(this,dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sds = new ServiceDescription();
        sd.setType("Taxi");
        template.addServices(sds);
        DFAgentDescription[] results;

        try {
            results = DFService.search(this,template);
            for(int i = 0; i < results.length; i++){
                this.taxis.put(results[i].getName(),new Pair(0.0f,0.0f));
                this.availables.put(results[i].getName(),true);
            }

        } catch (FIPAException e) {
            e.printStackTrace();
        }
        this.addBehaviour(new BH_ReceiveMessage(this));
    }

    public void updateTaxi(AID name, float final_x, float final_y, boolean state) {
        this.taxis.replace(name, new Pair(final_x,final_y));
        this.availables.replace(name,state);
    }

    public Map<AID, Pair<Float, Float>> getTaxis() {
        return taxis;
    }

    public Map<AID, Boolean> getAvailables() {
        return availables;
    }

    public void addTaxiInfo(AID sender, float x_pos, float y_pos) {
        System.out.println("AQUI:" + sender);
        this.taxis.replace(sender,new Pair(x_pos,y_pos));
        this.availables.replace(sender, true);
    }

}
