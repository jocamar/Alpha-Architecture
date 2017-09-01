package AlphaArchitecture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jocamar on 17/06/2016.
 */
public abstract class Move {
    boolean needsDeals;
    List<Deal> dealsNeeded;
    String playerId;

    public Move(String playerId, List<Deal> needed) {
        this.playerId = playerId;

        dealsNeeded = needed;
        if(needed != null && needed.size() > 0)
            needsDeals = true;
        else
            needsDeals = false;
    }

    boolean moveNeedsDeals() {
        return needsDeals;
    }

    List<Deal> getDealsNeeded() {
        return new ArrayList<>(dealsNeeded);
    }
}
