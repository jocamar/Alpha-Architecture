package AlphaArchitecture;

import java.util.*;

/**
 * Created by jocamar on 17/06/2016.
 */
public abstract class Deal {
    float trust;
    Map<Opponent,Boolean> acceptedPlayer;
    List<Opponent> rejectedPlayers;
    List<String> proposers;

    public float getTrust() {
        return trust;
    }

    public Deal(List<String> proposers, List<? extends Opponent> oppInvolved) {
        this.proposers = proposers;
        rejectedPlayers = new ArrayList<>();
        acceptedPlayer = new HashMap<>();
        for(Opponent o : oppInvolved)
            acceptedPlayer.put(o,false);
        trust = 1;
    }

    public List<String> getProposers() {
        return Collections.unmodifiableList(proposers);
    }

    public void addAccepted(Opponent player) {
        if(acceptedPlayer.containsKey(player))
            acceptedPlayer.put(player,true);
    }

    public void addAccepted(List<? extends Opponent> players) {
        for(Opponent player : players)
            if(acceptedPlayer.containsKey(player))
                acceptedPlayer.put(player,true);
    }

    public void setAllAccepted() {
        for(Opponent player : acceptedPlayer.keySet())
            acceptedPlayer.put(player,true);
    }

    public void removeAccepted(Opponent player) {
        if(acceptedPlayer.containsKey(player))
            acceptedPlayer.put(player,false);
    }

    public void removeAccepted(List<? extends Opponent> players) {
        for(Opponent player : players)
            if(acceptedPlayer.containsKey(player))
                acceptedPlayer.put(player,false);
    }

    public Set<Opponent> getOpponentsInvolved() {
        return acceptedPlayer.keySet();
    }

    public boolean allAccepted() {
        for(Opponent o : acceptedPlayer.keySet())
            if(!acceptedPlayer.get(o))
                return false;

        return true;
    }

    public void addRejected(Opponent o) {
        if(!rejectedPlayers.contains(o))
            rejectedPlayers.add(o);
    }

    public List<Opponent> getRejectedOpponents() {
        return rejectedPlayers;
    }
}
