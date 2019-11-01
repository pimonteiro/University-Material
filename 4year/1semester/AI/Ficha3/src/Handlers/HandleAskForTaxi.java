package Handlers;

import Agents.Manager;
import Messages.AskForTaxi;
import Messages.SendTaxi;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import utils.Pair;

import java.io.IOException;
import java.util.*;

public class HandleAskForTaxi extends OneShotBehaviour {
    private ACLMessage request;

    public HandleAskForTaxi(Agent a, ACLMessage req) {
        super(a);
        this.request = req;
    }

    @Override
    public void action() {
        try {
            AskForTaxi cont = (AskForTaxi) this.request.getContentObject();

            AID a_name = searchClosestTaxi(cont.getX_pos(), cont.getY_pos(), cont);
            SendTaxi con = new SendTaxi(a_name, cont.getClient(), cont.getX_dest(),cont.getY_dest());
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.setContentObject(con);
            msg.addReceiver(a_name);
            myAgent.send(msg);
            System.out.println(this.myAgent.getAID().toString() + ": Requesting Taxi to " + a_name);

        } catch (UnreadableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private AID searchClosestTaxi(float x_pos, float y_pos, AskForTaxi cont) {
        SortedSet<Map.Entry<AID, Double>> sortedSet = new TreeSet<Map.Entry<AID, Double>>(
                new Comparator<Map.Entry<AID, Double>>() {
                    @Override
                    public int compare(Map.Entry<AID, Double> e1,
                                       Map.Entry<AID, Double> e2) {
                        return e1.getValue().compareTo(e2.getValue());
                    }
                });
        SortedMap<AID, Double> dists = new TreeMap<AID, Double>();
        Map<AID, Pair<Float,Float>> taxis = ((Manager) this.myAgent).getTaxis();
        Map<AID, Boolean> availables = ((Manager) this.myAgent).getAvailables();

        for (AID key : taxis.keySet()){
            System.out.println(key.toString());
            if(availables.get(key)){
                Pair<Float,Float> p = taxis.get(key);
                Float aa = p.key;
                Float bb = p.value;

                float v1 = ((cont.getX_pos() - aa)*(cont.getX_pos() - aa));
                float v2 = ((cont.getY_pos() - bb)*(cont.getY_pos() - bb));
                double v = Math.sqrt((double)v1 + (double)v2);
                dists.put(key,v);
            }
        }
        sortedSet.addAll(dists.entrySet());
        return sortedSet.first().getKey();
    }
}
