package model;

public class Player extends Person {

    private String gameId;

    public Player(int id, String name, String email, String gameId) {
        super(id, name, email);
        this.gameId = gameId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public String getRole() {
        return "Player";
    }
}
