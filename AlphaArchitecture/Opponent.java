package AlphaArchitecture;

/**
 * Created by jocamar on 17/06/2016.
 */
public abstract class Opponent {
    float trust;
    String id;
    int disposition;

    public float getTrust() {
        return trust;
    }

    public String getId() {
        return id;
    }

    public Opponent(String id) {
        this.id = id;
        this.trust = 1.0f;
        this.disposition = 0;
    }

    public int getDisposition() {
        return disposition;
    }

    public void setDisposition(int newDisposition) {
        this.disposition = newDisposition;
    }
}
