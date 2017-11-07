package ilya.vitaly.alcotestdroid.Entities;

/**
 * Created by ilya on 05/11/2017.
 */

public class User {
    String name;
    int time;
    int id;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String name, int time, int id) {
        this.name = name;
        this.time = time;
        this.id = id;


    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public int getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setID(int id) {
        this.id = id;
    }

//    public String getLevel() {
//        return level;
//    }
//
//    public void setLevel(String level) {
//        this.level = level;
//    }

}
