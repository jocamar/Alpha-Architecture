package AlphaArchitecture;

import java.util.List;
import java.util.Map;

/**
 * Created by jocamar
 */
public class Round<M,D,O> {
    public Map<O,M> movesPlayed;
    public List<D> dealsProposed;
    public List<D> dealsAccepted;
    public List<D> dealsRejected;

    public Round(Map<O,M> moves, List<D> dealsProp, List<D> dealsAccept, List<D> dealsReject) {
        movesPlayed = moves;
        dealsProposed = dealsProp;
        dealsAccepted = dealsAccept;
        dealsRejected = dealsReject;
    }
}
