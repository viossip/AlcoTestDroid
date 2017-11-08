package ilya.vitaly.alcotestdroid.Entities;

import java.util.Date;

/**
 * Created by USER on 09/11/2017.
 */

public class Game {
    String Type;
    Date Date;
    String Steps;
    String Time;

    public Game(String type, java.util.Date date, String steps, String time) {
        Type = type;
        Date = date;
        Steps = steps;
        Time = time;
    }



    public void setType(String type) {
        Type = type;
    }

    public void setDate(java.util.Date date) {
        Date = date;
    }

    public void setSteps(String steps) {
        Steps = steps;
    }

    public void setTime(String time) {
        Time = time;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public String getSteps() {
        return Steps;
    }

    public String getTime() {
        return Time;
    }

    public String getType() {

        return Type;
    }

    @Override
    public String toString() {
        return "Game{" +
                "Type='" + Type + '\'' +
                ", Date=" + Date +
                ", Steps='" + Steps + '\'' +
                ", Time='" + Time + '\'' +
                '}';
    }
}
