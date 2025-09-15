import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        showMenu();
    }

    public static void showMenu() {
        int option;

        do {
            option = Integer.parseInt(JOptionPane.showInputDialog(
                    null,
                    "¿Que deseas hacer?\n\n1. Agregar producto al inventario\n2. Listar inventario\n3. Comprar producto\n4. Mostrar estadisticas\n5. Buscar producto\n6. Pagar y salir\n\nIngresa tú opción"
            ));

            switch (option) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    showInventory();
                    break;
                case 3:
                    buyProduct();
                    break;
                case 4:
                    statisticsProducts();
                    break;
                case 5:
                    findProductForName();
                    break;
                case 6:
                    checkoutAndExit();
                    break;
            }
        } while (option != 6);
    }


    // Opción 1 - Agregar producto al inventario
    public static void addProduct() {
        String inputNameProduct = JOptionPane.showInputDialog(
                null,
                "Nombre del producto:"
        );

        double inputPriceProduct = Double.parseDouble(JOptionPane.showInputDialog(
                null,
                "Precio del producto:"
        ));

        int inputStockProduct = Integer.parseInt(JOptionPane.showInputDialog(
                null,
                "Cantidad del producto:"
        ));

        int confirmAddProduct = JOptionPane.showConfirmDialog(
                null,
                "¿Deseas agregar el siguiente item al inventario?",
                "Confirmación",
                JOptionPane.YES_NO_CANCEL_OPTION
        );
        switch (confirmAddProduct) {
            case JOptionPane.YES_OPTION:
                Inventory.idProduct++;
                Products newProduct = new Products(inputNameProduct, inputPriceProduct, inputStockProduct);
                Inventory.inventory.put(Inventory.idProduct, newProduct);
                JOptionPane.showMessageDialog(
                        null,
                        "Producto agregado con ID #" + Inventory.idProduct + "\n\nInformación del producto:\n" + newProduct.getInfoProduct()
                );
                break;
            case JOptionPane.NO_OPTION, JOptionPane.CANCEL_OPTION, JOptionPane.CLOSED_OPTION:
                JOptionPane.showMessageDialog(
                        null,
                        "Acción cancelada"
                );
                break;
        }
    }


    // Opción 2 - Mostrar inventario
    public static void showInventory() {
        String text = "";

        for (Map.Entry<Integer, Products> entry : Inventory.inventory.entrySet()) {
            Integer key = entry.getKey();
            Products products = entry.getValue();

            text = text + "ID: " + key + " - " + products.getInfoProduct() + "\n";
        }

        JOptionPane.showMessageDialog(
                null,
                text,
                "Inventario",
                JOptionPane.INFORMATION_MESSAGE
        );
    }


    // Opción 3 - Comprar producto
    public static void buyProduct() {
        int askProduct;
        String text = "";

        for (Map.Entry<Integer, Products> entry : Inventory.inventory.entrySet()) {
            Integer key = entry.getKey();
            Products products = entry.getValue();

            text = text + "ID: " + key + " - " + products.getInfoProduct() + "\n";
        }

        JFrame showInventoryToBuy = new JFrame();
        showInventoryToBuy.setUndecorated(true);
        showInventoryToBuy.setSize(1, 1);
        showInventoryToBuy.setLocation(960, 300);
        showInventoryToBuy.setVisible(true);

        JOptionPane.showMessageDialog(
                showInventoryToBuy,
                text,
                "Inventario",
                JOptionPane.INFORMATION_MESSAGE
        );

        askProduct = Integer.parseInt(JOptionPane.showInputDialog(
                null,
                "¿Que producto deseas comprar? (Ingresa el ID)"
        ));

        int decreaseStock;
        for (Map.Entry<Integer, Products> entry : Inventory.inventory.entrySet()) {
            Integer key = entry.getKey();
            Products products = entry.getValue();

            if (askProduct == key) {
                decreaseStock = Integer.parseInt(JOptionPane.showInputDialog(null, "¿Cuanto vas a comprar de este producto?"));
                int confirmBuyProduct = JOptionPane.showConfirmDialog(
                        null,
                        "¿Deseas comprar el siguiente item?",
                        "Confirmación",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                switch (confirmBuyProduct) {
                    case JOptionPane.YES_OPTION:
                        products.setStockProduct(products.getStockProduct() - decreaseStock);

                        Products purchasedProduct = new Products(
                                products.getNameProduct(),
                                products.getPriceProduct(),
                                decreaseStock
                        );
                        ShoppingCart.shoppingCart.add(purchasedProduct);

                        JOptionPane.showMessageDialog(
                                null,
                                "Has comprado la cantidad de " + decreaseStock
                        );
                        break;
                    case JOptionPane.NO_OPTION, JOptionPane.CANCEL_OPTION, JOptionPane.CLOSED_OPTION:
                        JOptionPane.showMessageDialog(
                                null,
                                "Acción cancelada"
                        );
                        break;
                }
            }
        }
    }

    // Opción 4 - Estadística de productos
    public static void statisticsProducts() {
        List<Products> productsList = new ArrayList<>(Inventory.inventory.values());

        productsList.sort(Comparator.comparingDouble(Products::getPriceProduct));

        // Producto mayor y menor
        Products firstForPrice = productsList.getFirst();
        Products lastForPrice = productsList.getLast();

        JOptionPane.showMessageDialog(
                null,
                "Producto con el menor precio es: " + firstForPrice.getInfoProduct() + "\nProducto con el mayor precio es: " + lastForPrice.getInfoProduct()
        );
    }

    // Opción 5 - Buscar producto
    public static void findProductForName() {
        String findProduct = JOptionPane.showInputDialog(
                null,
                "¿Que producto buscas?"
        );

        boolean productFound = false;

        for (Map.Entry<Integer, Products> entry : Inventory.inventory.entrySet()) {
            Integer key = entry.getKey();
            Products products = entry.getValue();

            if (products.getNameProduct().equalsIgnoreCase(findProduct)) {
                JOptionPane.showMessageDialog(
                        null,
                        "Producto encontrado: ID: #" + key + products.getInfoProduct()
                );
                productFound = true;
                break;
            }
        }

        if (!productFound) {
            JOptionPane.showMessageDialog(
                    null,
                    "Producto no encontrado"
            );
        }
    }

    // Opción 6 - Pagar y salir
    public static void checkoutAndExit() {
        if (ShoppingCart.shoppingCart.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null,
                    "No has comprado nada aún."
            );
            return;
        }

        double total = 0;
        String receipt = "Resumen de tus compras:\n\n";

        for (Products item : ShoppingCart.shoppingCart) {
            double subtotal = item.getPriceProduct() * item.getStockProduct();

            receipt = receipt + item.getNameProduct() + " * " + item.getStockProduct() + " = $" + subtotal + "\n";
            total = total + subtotal;
        }
        receipt = receipt + "\nEl total de su compra es: $" + total;
        JOptionPane.showMessageDialog(
                null,
                receipt
        );
    }
}