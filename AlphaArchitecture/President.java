package AlphaArchitecture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class President<T extends Traits, M extends Move, D extends Deal, O extends Opponent, G extends Goal, GS> implements IPresident<KnowledgeBase<M,D,O,G,GS>,T,M,D,GS> {
    protected ForeignOffice<M,D,O,G> foreignOffice;
    protected IntelligenceOffice<M,D,O,G> intelligenceOffice;
    protected StrategyOffice<M,D,O,G,GS> strategyOffice;

    protected KnowledgeBase<M,D,O,G,GS> knowledge;
    protected T traits;
    protected long processDeadline = 15000;

    private List<M> movesToConsider;
    private M backupMove;
    private M moveToPlay;

    private String playerId;

    public void init(String id, T traits, KnowledgeBase<M,D,O,G,GS> knowledge) {

        this.playerId = id;

        movesToConsider = new ArrayList<>();
        backupMove = null;
        moveToPlay = null;

        this.traits = traits;
        this.traits.init();

        this.knowledge = knowledge;
    }

    public final void attachOffice(ForeignOffice<M,D,O,G> office) {
        this.foreignOffice = office;
        if (foreignOffice != null)
            this.foreignOffice.init(this);
    }

    public final void attachOffice(IntelligenceOffice<M,D,O,G> office) {
        this.intelligenceOffice = office;
        if (intelligenceOffice != null)
            this.intelligenceOffice.init(this);
    }

    public final void attachOffice(StrategyOffice<M,D,O,G,GS> office) {
        this.strategyOffice = office;
        if(strategyOffice != null)
            this.strategyOffice.init(this);
    }

    @Override
    public final IForeignOffice getForeignOffice() {
        return foreignOffice;
    }

    @Override
    public final IIntelligenceOffice getIntelligenceOffice() {
        return intelligenceOffice;
    }

    @Override
    public final IStrategyOffice getStrategyOffice() {
        return strategyOffice;
    }

    @Override
    public final KnowledgeBase<M,D,O,G,GS> getKnowledgeBase() {
        return knowledge;
    }

    @Override
    public final T getTraits() {
        return traits;
    }

    @Override
    public final int getGamePhase() {
        return knowledge.getGamePhase();
    }

    public final M playMove() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;

        if(movesToConsider != null)
            movesToConsider.clear();
        backupMove = null;
        moveToPlay = null;

        beforePlay();

        M move = null;
        while(currTime - startTime < processDeadline && move == null) {
            calculateGoals();

            Map<O,Integer> disp = updateDisposition();
            if(disp != null) {
                for (Map.Entry<O, Integer> e : disp.entrySet())
                    e.getKey().disposition = e.getValue();
            }

            move = getMoveToPlay();
            currTime = System.currentTimeMillis();
        }

        if(move == null)
            move = backupMove;

        afterPlay();
        return move;
    }

    private M getMoveToPlay() {
        process();

        if(knowledge != null) {
            moveToPlay = selectMove(this.getMovesToConsider(),this.getBackupMove());
            if(moveToPlay != null) {
                return moveToPlay;
            }
        }

        return null;
    }

    public void process() {
        callIntelligenceOffice();
        callStrategyOffice();
        callForeignOffice();
    }

    protected abstract M selectMove(List<M> movesBeingConsidered, M fallbackMove);

    protected abstract void afterPlay();

    protected abstract void beforePlay();

    protected abstract List<G> calculateGoals();

    protected abstract Map<O,Integer> updateDisposition();

    @Override
    public final String getId() {
        return playerId;
    }

    @Override
    public final void updateGameState(GS gameState) {
        if(knowledge != null)
            knowledge.setGamestate(gameState);
    }

    @Override
    public final void handleIncomingMessage(Object msg) {
        if(foreignOffice != null)
            foreignOffice.handleIncomingMsg(msg,knowledge,strategyOffice,intelligenceOffice);
    }

    @Override
    public final void setInGame(boolean ingame) {
        knowledge.inGame = ingame;
    }

    @Override
    public final void setDeadline(long deadline) {
        this.processDeadline = deadline;
    }

    @Override
    public final long getDeadline() {
        return processDeadline;
    }

    protected final M getBackupMove() {
        return backupMove;
    }

    protected final List<M> getMovesToConsider() {
        return Collections.unmodifiableList(movesToConsider);
    }

    protected final void callStrategyOffice() {
        if(strategyOffice != null) {
            if(backupMove == null) {
                backupMove = strategyOffice.suggestFallbackMove(knowledge,traits);
            }

            movesToConsider = strategyOffice.suggestMoves(knowledge,traits);
        }
    }

    protected final void callForeignOffice() {
        if(foreignOffice != null)
            foreignOffice.negotiate(movesToConsider,knowledge,traits,strategyOffice);
    }

    protected final void callIntelligenceOffice() {
        if(intelligenceOffice != null)
            intelligenceOffice.update(knowledge,strategyOffice);
    }
}
