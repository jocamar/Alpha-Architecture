package AlphaArchitecture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jocamar on 14/04/2016.
 */
public abstract class StrategyOffice<M extends Move,D extends Deal, O extends Opponent, G extends Goal, GS> implements IStrategyOffice<M,D>, Office {
    protected President president;

    public void init(President president) {
        this.president = president;
    }

    public String getType() {
        return "StrategyOffice";
    }

    public abstract List<M> suggestMoves(KnowledgeBase knowledge, Traits traits);

    public abstract M suggestFallbackMove(KnowledgeBase knowledge, Traits traits);

    protected abstract float calculateMoveUtility(M move, List<G> goals, List<D> activeDeals, Map<String,O> opponents, GS gamestate);

    protected abstract float calculateDealUtility(D deal, List<G> goals, List<D> activeDeals, Map<String,O> opponents, GS gamestate);

    @Override
    public final float evaluateMove(M move) {
        List<G> goals = null;
        List<D> deals = null;

        if(move.playerId.equals(president.getId())) {
            goals = president.getKnowledgeBase().getGoals();
            deals = president.getKnowledgeBase().getConfirmedDeals();
        }
        else {
            Map<String,List<G>> opponentGoals = president.getKnowledgeBase().getOpponentGoals();
            if(opponentGoals.containsKey(move.playerId))
                goals = opponentGoals.get(move.playerId);

            List<D> confirmedDeals = president.getKnowledgeBase().getConfirmedDeals();
            deals = new ArrayList<>();
            for(D deal : confirmedDeals )
                if(deal.acceptedPlayer.containsKey(move.playerId))
                    deals.add(deal);
        }
        return calculateMoveUtility(move,goals,deals,president.getKnowledgeBase().getOpponents(),(GS) president.getKnowledgeBase().gameState);
    }

    @Override
    public final float evaluateDeal(D d, String playerId) {
        List<G> goals = null;
        List<D> deals = null;

        if(playerId.equals(president.getId())) {
            goals = president.getKnowledgeBase().getGoals();
            deals = president.getKnowledgeBase().getConfirmedDeals();
        }
        else {
            Map<String,List<G>> opponentGoals = president.getKnowledgeBase().getOpponentGoals();
            if(opponentGoals.containsKey(playerId))
                goals = opponentGoals.get(playerId);

            List<D> confirmedDeals = president.getKnowledgeBase().getConfirmedDeals();
            deals = new ArrayList<>();
            for(D deal : confirmedDeals )
                if(deal.acceptedPlayer.containsKey(playerId))
                    deals.add(deal);
        }
        return calculateDealUtility(d,goals,deals,president.getKnowledgeBase().getOpponents(),(GS) president.getKnowledgeBase().gameState);
    }
}