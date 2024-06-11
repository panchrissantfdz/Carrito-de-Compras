import java.awt.Image;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private double price;
    private int quantity;
    public ImageIcon image;

    public Product(String name, String description, double price, int quantity, Image ImagePath) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.image = new ImageIcon(ImagePath);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ImageIcon getImageIcon() {return image; }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}