package AlphaArchitecture;

/**
 * Created by João on 05-Apr-16.
 */
public interface IPresident<K, T extends Traits,M extends Move,D extends Deal, GS> {
    IForeignOffice getForeignOffice();
    IIntelligenceOffice getIntelligenceOffice();
    IStrategyOffice<M,D> getStrategyOffice();

    K getKnowledgeBase();
    T getTraits();
    int getGamePhase();
    String getId();

    void updateGameState(GS gamestate);
    void handleIncomingMessage(Object msg);

    void setInGame(boolean ingame);
    void setDeadline(long deadline);
    long getDeadline();
}
