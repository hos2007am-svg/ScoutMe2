package sc.example.firebasestart;

public class Player {
        private String name;
        private String uid;
        private String team;
        private String position;
        private String age;
        public Player(){

        }
        public Player(String name,String uid,String team,String position,String age){
            this.name=name;
            this.uid=uid;
            this.age=age;
            this.team=team;
            this.position=position;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getUid() {
            return uid;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    public String getAge() {
        return age;
    }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return team;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
