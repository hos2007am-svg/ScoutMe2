package sc.example.firebasestart;

public class Purchase {
    private String buyer;
    private String uid;
    private int price;
    private String shoeType;
    public Purchase() {
    }

    public Purchase(String buyer, int price, String shoeType, String uid) {
        this.buyer = buyer;
        this.price = price;
        this.shoeType = shoeType;
        this.uid=uid;
    }

    public String getBuyer() {
        return buyer;
    }
    public String getUid() {
        return uid;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getShoeType() {
        return shoeType;
    }

    public void setShoeType(String shoeType) {
        this.shoeType = shoeType;
    }
}
