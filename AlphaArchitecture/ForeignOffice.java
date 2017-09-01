package AlphaArchitecture;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * Created by jocamar on 14/04/2016.
 */
public abstract class ForeignOffice<M extends Move, D extends Deal, O extends Opponent, G extends Goal> implements IForeignOffice, Office {
    protected President president;

    public ForeignOffice(President president) {
        this.president = president;
    }

    public final void negotiate(List<M> movesConsidered, KnowledgeBase knowledge, Traits traits, IStrategyOffice<M,D> strategyOffice) {
        List<D> betrayed = getDealsBetrayed(knowledge.getPastRounds(), knowledge.getConfirmedDeals(), strategyOffice);
        List<D> completed = getDealsCompleted(knowledge.getPastRounds(), knowledge.getConfirmedDeals(), strategyOffice);
        if(betrayed != null)
            for(D deal : betrayed)
                knowledge.addBetrayedDeal(deal);
        if(completed != null)
            for(D deal : completed)
                knowledge.addCompletedDeal(deal);

        List<G> goals = knowledge.getGoals();
        List<D> proposedDeals = knowledge.getProposedDeals();
        Map<String,O> opponents = knowledge.getOpponents();
        List<D> stopConsidering = calculateDealsToStopConsidering(movesConsidered,goals,proposedDeals,traits,strategyOffice);

        if(stopConsidering != null)
            for(D deal : stopConsidering)
                knowledge.proposedDeals.remove(deal);

        proposedDeals = knowledge.getProposedDeals();

        Pair<List<D>,List<D>> negotiationResult = negotiateDeals(movesConsidered,goals,proposedDeals,opponents,traits,strategyOffice);

        if(negotiationResult != null) {
            if(negotiationResult.getKey() != null) {
                for(D deal : negotiationResult.getKey())
                    knowledge.addConfirmedDeal(deal);
            }

            if(negotiationResult.getValue() != null) {
                for(D deal : negotiationResult.getValue())
                    knowledge.addRejectedDeal(deal);
            }
        }
    }

    public void init(President president) {
        this.president = president;
    }

    public String getType() {
        return "ForeignOffice";
    }

    protected abstract Pair<List<D>,List<D>> negotiateDeals(List<M> movesConsidered, List<G> goals, List<D> proposedDeals,
                                                            Map<String,O> opponents, Traits traits, IStrategyOffice<M,D> strategyOffice);

    protected abstract List<D> calculateDealsToStopConsidering(List<M> movesConsidered, List<G> goals, List<D> proposedDeals, Traits traits, IStrategyOffice<M,D> strategyOffice);

    public abstract void handleIncomingMsg(Object msg, KnowledgeBase knowledge, IStrategyOffice<M,D> strategyOffice, IIntelligenceOffice<M,D,O,G> intelligenceOffice);

    public abstract List<D> getDealsBetrayed(List<Round<M,D,O>> rounds, List<D> confirmedDeals, IStrategyOffice<M,D> strategyOffice);

    public abstract List<D> getDealsCompleted(List<Round<M,D,O>> rounds, List<D> confirmedDeals, IStrategyOffice<M,D> strategyOffice);

}
