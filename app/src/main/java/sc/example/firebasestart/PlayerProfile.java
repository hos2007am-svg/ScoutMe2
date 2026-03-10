package sc.example.firebasestart;


public class PlayerProfile {
    private String name;
    private int age;
    private String position;
    private String team;
    private int rating;

    public PlayerProfile(String name, int age, String position, String team, int rating) {
        this.name = name;
        this.age = age;
        this.position = position;
        this.team = team;
        this.rating = rating;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return team;
    }

    public int getRating() {
        return rating;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}

