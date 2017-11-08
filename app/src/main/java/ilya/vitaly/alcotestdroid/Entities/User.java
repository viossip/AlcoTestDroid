package ilya.vitaly.alcotestdroid.Entities;

/**
 * Created by ilya on 05/11/2017.
 */

public class User {


    int id;
    String email;
    String name;
    Game[] games;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(int id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }


    public int getID() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setID(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGame(Game game) {
        if (game.getType().equals("Simple"))
            games[0] = game;
        else
            games[1] = game;

    }




}
