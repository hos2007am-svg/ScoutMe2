package sc.example.firebasestart;

public class Scouter {
    private String name;
   private String uid;
   public Scouter(){

   }
   public Scouter(String name,String uid){
       this.name=name;
       this.uid=uid;
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
}
