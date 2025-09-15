public class Products {
    // Encapsulamiento
    private String nameProduct;
    private double priceProduct;
    private int stockProduct;

    // Constructor
    public Products(String nameProduct, double priceProduct, int stockProduct) {
        this.nameProduct = nameProduct;
        this.priceProduct = priceProduct;
        this.stockProduct = stockProduct;
    }

    public void setStockProduct(int stockProduct) {
        this.stockProduct = stockProduct;
    }

    // get para poder leerlos
    public String getNameProduct() {
        return nameProduct;
    }

    public double getPriceProduct() {
        return priceProduct;
    }

    public int getStockProduct() {
        return stockProduct;
    }

    public String getInfoProduct() {
        return "Nombre del producto: " + nameProduct + " - Precio del producto: $" + priceProduct +  " - Cantidad disponible del producto: " + stockProduct;
    }
}
