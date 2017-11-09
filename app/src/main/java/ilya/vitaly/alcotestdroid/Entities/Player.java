package ilya.vitaly.alcotestdroid.Entities;

/**
 * Created by vital on 09/11/2017.
 */

public class Player {

    private String name;
    private String time;
    private String steps;
    private String gameType;

    public Player(String name, String time, String steps, String gameType) {
        this.name = name;
        this.time = time;
        this.steps = steps;
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }



}
