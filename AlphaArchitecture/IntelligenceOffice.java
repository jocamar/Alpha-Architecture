package AlphaArchitecture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jocamar on 14/04/2016.
 */
public abstract class IntelligenceOffice<M extends Move, D extends Deal, O extends Opponent, G extends Goal> implements IIntelligenceOffice<M,D,O,G>, Office {
    protected President president;

    public final void update(KnowledgeBase knowledge, IStrategyOffice<M,D> strategyOffice) {
        Map<String,List<G>> oppGoals = this.calculateOpponentGoals(knowledge.getPastRounds(), strategyOffice);
        if(oppGoals != null)
            knowledge.setOpponentGoals(oppGoals);


        Map<String,Float> oppTrust = this.calculateOpponentTrust(knowledge.getPastRounds(), knowledge.getBetrayedDeals(), knowledge.getCompletedDeals(), strategyOffice);
        if(oppTrust != null) {
            Map<String,O> opps = knowledge.getOpponents();
            for(Map.Entry<String,Float> e : oppTrust.entrySet()) {
                if (opps.containsKey(e.getKey()))
                    opps.get(e.getKey()).trust = e.getValue();
            }
        }

        Map<D,Float> dealTrust = this.calculateDealTrust(knowledge, strategyOffice);
        if(dealTrust != null) {
            for(Map.Entry<D,Float> e : dealTrust.entrySet())
                e.getKey().trust = e.getValue();
        }
    }

    public void init(President president) {
        this.president = president;
    }

    public String getType() {
        return "IntelligenceOffice";
    }

    public abstract Map<String,List<G>> calculateOpponentGoals(List<Round<M,D,O>> rounds, IStrategyOffice<M,D> strategyOffice);

    public abstract Map<String,Float> calculateOpponentTrust(List<Round<M,D,O>> rounds, List<D> betrayedDeals, List<D> completedDeals, IStrategyOffice<M,D> strategyOffice);

    public final Map<D,Float> calculateDealTrust(KnowledgeBase knowledge, IStrategyOffice<M,D> strategyOffice) {
        List<D> proposed = knowledge.getProposedDeals();
        List<D> confirmed = knowledge.getConfirmedDeals();
        Map<String,O> opponents = knowledge.getOpponents();
        Map<O,List<G>> oppGoals = knowledge.getOpponentGoals();
        List<Round<M,D,O>> rounds = knowledge.getPastRounds();

        Map<D,Float> trusts = new HashMap<>();

        for(D deal : proposed) {
            trusts.put(deal,evaluateDealTrust(deal,rounds,opponents,oppGoals,strategyOffice));
        }

        for(D deal : confirmed) {
            trusts.put(deal,evaluateDealTrust(deal,rounds,opponents,oppGoals,strategyOffice));
        }

        return trusts;
    }
}
