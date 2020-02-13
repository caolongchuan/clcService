package entiry;

public class Merchandise {
    private int id;
    private String name;
    private Double price;
    private String img;

    public Merchandise(){
        this.id = -1;
        this.name = "";
        this.price = 0.00;
        this.img = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
