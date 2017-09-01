package AlphaArchitecture;

import java.util.*;


public abstract class KnowledgeBase<M extends Move, D extends Deal, O extends Opponent, G extends Goal, GS> {
    int gamePhase;
    boolean inGame;

    Map<String, O> opponents;
    Map<String, O> unmodifiableOpponents;

    List<D> confirmedDeals;
    List<D> unmodifiableConfirmedDeals;
    List<D> proposedDeals;
    List<D> unmodifiableProposedDeals;
    List<D> completedDeals;
    List<D> unmodifiableCompletedDeals;
    List<D> betrayedDeals;
    List<D> unmodifiableBetrayedDeals;
    List<D> rejectedDeals;
    List<D> unmodifiableRejectedDeals;
    List<Round<M,D,O>> pastRounds;
    List<Round<M,D,O>> unmodifiablePastRounds;

    List<G> goals;
    List<G> unmodifiableGoals;
    Map<String, List<G>> opponentGoals;
    Map<String, List<G>> unmodifiableOpponentGoals;

    GS gameState;

    public void init(GS initialGameState) {
        inGame = true;
        opponents = new HashMap<>();
        unmodifiableOpponents = Collections.unmodifiableMap(opponents);

        confirmedDeals = new ArrayList<>();
        unmodifiableConfirmedDeals = Collections.unmodifiableList(confirmedDeals);
        proposedDeals = new ArrayList<>();
        unmodifiableProposedDeals = Collections.unmodifiableList(proposedDeals);
        completedDeals = new ArrayList<>();
        unmodifiableCompletedDeals = Collections.unmodifiableList(completedDeals);
        betrayedDeals = new ArrayList<>();
        unmodifiableBetrayedDeals = Collections.unmodifiableList(betrayedDeals);
        rejectedDeals = new ArrayList<>();
        unmodifiableRejectedDeals = Collections.unmodifiableList(rejectedDeals);

        pastRounds = new ArrayList<>();
        unmodifiablePastRounds = Collections.unmodifiableList(pastRounds);
        goals = new ArrayList<>();
        unmodifiableGoals = Collections.unmodifiableList(goals);
        opponentGoals = new HashMap<>();
        unmodifiableOpponentGoals = Collections.unmodifiableMap(opponentGoals);

        gameState = initialGameState;
    }

    void setOpponentGoals(Map<String,List<G>> opponentGoals) {
        this.opponentGoals = opponentGoals;
    }

    void setGoals(List<G> goals) {
        this.goals = goals;
    }

    void addConfirmedDeal(D confirmed) {
        proposedDeals.remove(confirmed);
        confirmedDeals.add(confirmed);

        if(pastRounds != null && pastRounds.size() > 0)
            pastRounds.get(pastRounds.size()-1).dealsAccepted.add(confirmed);
    }

    void addCompletedDeal(D completed) {
        completedDeals.add(completed);
        confirmedDeals.remove(completed);
    }

    void addBetrayedDeal(D betrayed) {
        betrayedDeals.add(betrayed);
        confirmedDeals.remove(betrayed);
    }

    void addRejectedDeal(D rejected) {
        rejectedDeals.add(rejected);
        proposedDeals.remove(rejected);

        if(pastRounds != null && pastRounds.size() > 0)
            pastRounds.get(pastRounds.size()-1).dealsRejected.add(rejected);
    }

    public final void addProposal(D proposal) {
        proposedDeals.add(proposal);
        if(pastRounds != null && pastRounds.size() > 0)
            pastRounds.get(pastRounds.size()-1).dealsProposed.add(proposal);
    }

    public final void addMovePlayed(O opponent, M move) {
        if(pastRounds != null && pastRounds.size() > 0)
            pastRounds.get(pastRounds.size()-1).movesPlayed.put(opponent,move);
    }

    public void startNewRound() {
        pastRounds.add(new Round(new HashMap<>(),new ArrayList<>(),new ArrayList<>(), new ArrayList<>()));
    }

    public final Map<String,O> getOpponents() {
        return unmodifiableOpponents;
    }

    public final void addOpponent(String id, O opp) {
        opponents.put(id,opp);
    }

    public final Map<String,List<G>> getOpponentGoals() {
        return unmodifiableOpponentGoals;
    }

    public final List<D> getConfirmedDeals() {
        return unmodifiableConfirmedDeals;
    }

    public final List<D> getCompletedDeals() {
        return unmodifiableCompletedDeals;
    }

    public final List<D> getProposedDeals() {
        return unmodifiableProposedDeals;
    }

    public final List<D> getBetrayedDeals() {
        return unmodifiableBetrayedDeals;
    }

    public final List<D> getRejectedDeals() {
        return unmodifiableRejectedDeals;
    }

    public final List<G> getGoals() {
        return unmodifiableGoals;
    }

    public final List<Round<M,D,O>> getPastRounds() {
        return unmodifiablePastRounds;
    }

    public final int getGamePhase() {
        return gamePhase;
    }

    public final void setGamePhase(int newPhase) {
        gamePhase = newPhase;
    }

    public final boolean isInGame() {
        return inGame;
    }

    public final boolean moveIsViable(M m) {
        if(m.needsDeals) {
            if(confirmedDeals.containsAll(m.dealsNeeded))
                return true;

            return false;
        }

        return true;
    }

    public final void setGamestate(GS gamestate) {
        this.gameState = gamestate;
    }

    public final GS getGameState() {
        return gameState;
    }
}
