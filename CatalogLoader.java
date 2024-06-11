import java.io.*;

import javax.swing.ImageIcon;


public class CatalogLoader {

    public static Catalog loadCatalogFromFile(String filePath) {
        Catalog catalog = new Catalog();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String name = parts[0];
                    String description = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);
                   
                    catalog.addProduct(new Product(name, description, price, quantity, new ImageIcon(parts[4]).getImage()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return catalog;
    }

    public static Catalog loadCatalogManually() {
        Catalog catalog = new Catalog();
        catalog.addProduct(new Product("Leche", "Producto lacteo", 10.99, 100,new ImageIcon("C:\\Users\\chequ\\OneDrive\\Desktop\\carrito\\Carrito-de-Compras\\Imágenes\\Leche.jpg").getImage()));
        catalog.addProduct(new Product("Cafe", "Produducto con cafeina", 20.99, 50,new ImageIcon("C:\\Users\\chequ\\OneDrive\\Desktop\\carrito\\Carrito-de-Compras\\Imágenes\\Nescafe.jpg").getImage()));
        catalog.addProduct(new Product("Gansito", "Dulce", 30.99, 30,new ImageIcon("C:\\Users\\chequ\\OneDrive\\Desktop\\carrito\\Carrito-de-Compras\\Imágenes\\Gansito.jpg").getImage()));
        catalog.addProduct(new Product("Sabritas", "Botana", 25.99, 30,new ImageIcon("C:\\Users\\chequ\\OneDrive\\Desktop\\carrito\\Carrito-de-Compras\\Imágenes\\Sabritas.jpg").getImage()));
        catalog.addProduct(new Product("Corona", "Producto alçoholico", 25.69, 30,new ImageIcon("C:\\Users\\chequ\\OneDrive\\Desktop\\carrito\\Carrito-de-Compras\\Imágenes\\Corona.jpg").getImage()));
        catalog.addProduct(new Product("Doritos", "Botana", 25.99, 30,new ImageIcon("C:\\Users\\chequ\\OneDrive\\Desktop\\carrito\\Carrito-de-Compras\\Imágenes\\Doritos.jpg").getImage()));
        catalog.addProduct(new Product("Pepsi", "Refresco", 25.99, 30,new ImageIcon("C:\\Users\\chequ\\OneDrive\\Desktop\\carrito\\Carrito-de-Compras\\Imágenes\\Pepsi.jpeg").getImage()));
        catalog.addProduct(new Product("Chips", "Botana", 25.99, 30,new ImageIcon("C:\\Users\\chequ\\OneDrive\\Desktop\\carrito\\Carrito-de-Compras\\Imágenes\\Chips.jpg").getImage()));
        return catalog;
    }

    public static void saveCatalogToFile(Catalog catalog, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Product product : catalog.getProducts()) {
                bw.write(String.format("%s,%s,%.2f,%d%n",
                        product.getName(), product.getDescription(),
                        product.getPrice(), product.getQuantity()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Catalog catalog = loadCatalogManually();
        saveCatalogToFile(catalog, "catalog.txt");
        Catalog loadedCatalog = loadCatalogFromFile("catalog.txt");
        System.out.println(loadedCatalog);
    }
}