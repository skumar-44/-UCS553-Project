package model;

public class Tournament {

    private final int id;
    private String title;
    private String game;
    private String eventDate;
    private double prizePool;

    public Tournament(int id, String title, String game,
            String eventDate, double prizePool) {

        this.id = id;
        this.title = title;
        this.game = game;
        this.eventDate = eventDate;
        this.prizePool = prizePool;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGame() {
        return game;
    }

    public String getEventDate() {
        return eventDate;
    }

    public double getPrizePool() {
        return prizePool;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public void setPrizePool(double prizePool) {
        this.prizePool = prizePool;
    }
}
