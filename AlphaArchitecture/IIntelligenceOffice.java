package AlphaArchitecture;

import java.util.List;
import java.util.Map;

/**
 * Created by João on 05-Apr-16.
 */
public interface IIntelligenceOffice<M extends  Move, D extends Deal, O extends Opponent, G extends Goal> {
    float evaluateDealTrust(D deal, List<Round<M,D,O>> rounds, Map<String,O> opponents, Map<O,List<G>> oppGoals, IStrategyOffice<M,D> strategyOffice);
}
