package dem.model;

// Other Imports
import dem.core.GameClient;

public class Player {

    private final int player_id;
    private int account_id;
    private String name;
    private int credits;
    private String last_played;
    // Other
    private GameClient client;

    
    public Player(int player_id) {
        this.player_id = player_id;
    }

    public Player(int player_id, int account_id, String name, int credits) {
        this.player_id = player_id;
        this.account_id = account_id;
        this.name = name;
        this.credits = credits;
    }

    public int getID() {
        return player_id;
    }

    public int getAccountID() {
        return account_id;
    }

    public String getName() {
        return name;
    }

    public String setName(String name) {
        return this.name = name;
    }

    public int getCredits() {
        return credits;
    }

    public int setCredits(int credits) {
        return this.credits = credits;
    }

    public String getLastPlayed() {
        return last_played;
    }

    public String setLastPlayed(String last_played) {
        return this.last_played = last_played;
    }

    public GameClient getClient() {
        return client;
    }

    public GameClient setClient(GameClient client) {
        return this.client = client;
    }


}
