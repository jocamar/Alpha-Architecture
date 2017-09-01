package AlphaArchitecture;

/**
 * Created by João on 05-Apr-16.
 */
public interface IStrategyOffice<M extends Move, D extends Deal> {
    float evaluateMove(M move);
    float evaluateDeal(D deal, String player);
}
