import java.util.*;
import java.io.*;

class Product implements Comparable<Product> {
    private static int productCount = 0;
    private int id;
    private String name;
    private double price;
    private int quantity;
    private double weight;
    private String unit;
    private String category;
    
    public Product(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.weight = 0.0;
        this.unit = "pcs";
        this.category = "General";
        productCount++;
    }
    
    public Product(int id, String name, double price, int quantity, double weight, String unit) {
        this(id, name, price, quantity);
        this.weight = weight;
        this.unit = unit;
    }
    
    public Product(Product other) {
        this.id = other.id;
        this.name = other.name;
        this.price = other.price;
        this.quantity = other.quantity;
        this.weight = other.weight;
        this.unit = other.unit;
        this.category = other.category;
    }
    
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public double getWeight() { return weight; }
    public String getUnit() { return unit; }
    public String getCategory() { return category; }
    
    public void setQuantity(int q) { this.quantity = q; }
    public void setPrice(double p) { this.price = p; }
    public void setWeight(double w) { this.weight = w; }
    public void setUnit(String u) { this.unit = u; }
    public void setCategory(String c) { this.category = c; }
    
    public static int getProductCount() { return productCount; }
    
    public double getTotalValue() { return price * quantity; }
    
    @Override
    public int compareTo(Product other) {
        return this.name.compareToIgnoreCase(other.name);
    }
    
    public void display() {
        System.out.printf("%-5d | %-20s | %-5d | ₹%-10.2f | %.2f %s%n",
                id, name, quantity, price, weight, unit);
    }
    
    public String toCSV() {
        return id + "," + name + "," + quantity + "," + price + "," + weight + "," + unit;
    }
    
    class ProductDetails {
        private String supplier;
        private Date addedDate;
        
        public ProductDetails(String supplier) {
            this.supplier = supplier;
            this.addedDate = new Date();
        }
        
        public String getSupplier() { return supplier; }
        public Date getAddedDate() { return addedDate; }
    }
    
    public ProductDetails createDetails(String supplier) {
        return new ProductDetails(supplier);
    }
}

class Electronics extends Product {
    private int warrantyMonths;
    private String brand;
    
    public Electronics(int id, String name, double price, int quantity, int warranty, String brand) {
        super(id, name, price, quantity);
        this.warrantyMonths = warranty;
        this.brand = brand;
        setCategory("Electronics");
    }
    
    public int getWarrantyMonths() { return warrantyMonths; }
    public String getBrand() { return brand; }
    
    @Override
    public void display() {
        System.out.printf("%-5d | %-20s | %-5d | ₹%-10.2f | %s | %d months%n",
                getId(), getName(), getQuantity(), getPrice(), brand, warrantyMonths);
    }
}

class Grocery extends Product {
    private Date expiryDate;
    private boolean perishable;
    
    public Grocery(int id, String name, double price, int quantity, double weight, String unit, boolean perishable) {
        super(id, name, price, quantity, weight, unit);
        this.perishable = perishable;
        setCategory("Grocery");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 6);
        this.expiryDate = cal.getTime();
    }
    
    public Date getExpiryDate() { return expiryDate; }
    public boolean isPerishable() { return perishable; }
    
    @Override
    public void display() {
        System.out.printf("%-5d | %-20s | %-5d | ₹%-10.2f | %.2f %s | %s%n",
                getId(), getName(), getQuantity(), getPrice(), getWeight(), getUnit(),
                perishable ? "Perishable" : "Non-Perishable");
    }
}

class InventoryManager {
    private ArrayList<Product> products;
    private Vector<String> categories;
    private LinkedList<String> transactionLog;
    private static InventoryManager instance;
    
    private InventoryManager() {
        products = new ArrayList<>();
        categories = new Vector<>();
        transactionLog = new LinkedList<>();
        categories.add("General");
        categories.add("Electronics");
        categories.add("Grocery");
    }
    
    public static InventoryManager getInstance() {
        if (instance == null) {
            instance = new InventoryManager();
        }
        return instance;
    }
    
    public void addProduct(Product product) {
        products.add(product);
        transactionLog.add("Added: " + product.getName());
    }
    
    public boolean removeProduct(int id) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == id) {
                String name = products.get(i).getName();
                products.remove(i);
                transactionLog.add("Removed: " + name);
                return true;
            }
        }
        return false;
    }
    
    public Product findProductById(int id) {
        for (Product p : products) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    public void displayAllProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }
        
        System.out.println("─────────────────────────────────────────────────────────────────────────");
        System.out.printf("%-5s | %-20s | %-5s | %-12s | %-15s%n", "ID", "Name", "Qty", "Price", "Weight");
        System.out.println("─────────────────────────────────────────────────────────────────────────");
        
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) {
            iterator.next().display();
        }
        
        System.out.println("─────────────────────────────────────────────────────────────────────────");
        System.out.println("Total Products: " + products.size());
    }
    
    public void sortByName() {
        Collections.sort(products);
        transactionLog.add("Sorted by name");
    }
    
    public void sortByPrice() {
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2) {
                return Double.compare(p1.getPrice(), p2.getPrice());
            }
        });
        transactionLog.add("Sorted by price");
    }
    
    public void sortByQuantity() {
        products.sort((p1, p2) -> Integer.compare(p1.getQuantity(), p2.getQuantity()));
        transactionLog.add("Sorted by quantity");
    }
    
    public Product[] getProductsArray() {
        return products.toArray(new Product[0]);
    }
    
    public Product[][] getProductsGrid(int cols) {
        int rows = (int) Math.ceil((double) products.size() / cols);
        Product[][] grid = new Product[rows][cols];
        
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols && index < products.size(); j++) {
                grid[i][j] = products.get(index++);
            }
        }
        return grid;
    }
    
    public void displayTransactionLog() {
        System.out.println("\n=== Transaction Log ===");
        ListIterator<String> iterator = transactionLog.listIterator();
        int count = 1;
        while (iterator.hasNext()) {
            System.out.println(count++ + ". " + iterator.next());
        }
    }
    
    public String getStatistics() {
        StringBuffer stats = new StringBuffer();
        stats.append("=== Inventory Statistics ===\n");
        stats.append("Total Products: ").append(products.size()).append("\n");
        
        double totalValue = 0;
        int totalQuantity = 0;
        for (Product p : products) {
            totalValue += p.getTotalValue();
            totalQuantity += p.getQuantity();
        }
        
        stats.append("Total Value: ₹").append(String.format("%.2f", totalValue)).append("\n");
        stats.append("Total Quantity: ").append(totalQuantity).append("\n");
        
        return stats.toString();
    }
    
    public void exportCSV(String filename) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        writer.println("ID,Name,Quantity,Price,Weight,Unit");
        
        for (Product p : products) {
            writer.println(p.toCSV());
        }
        
        writer.close();
        transactionLog.add("Exported to CSV: " + filename);
    }
    
    public ArrayList<Product> searchByName(String keyword) {
        ArrayList<Product> results = new ArrayList<>();
        StringTokenizer tokenizer;
        
        for (Product p : products) {
            tokenizer = new StringTokenizer(p.getName().toLowerCase());
            while (tokenizer.hasMoreTokens()) {
                if (tokenizer.nextToken().contains(keyword.toLowerCase())) {
                    results.add(p);
                    break;
                }
            }
        }
        return results;
    }
    
    public void clearAllProducts() {
        products.clear();
        transactionLog.add("Cleared all products");
    }
}

public class InventoryManagementSystem {
    private static Scanner sc = new Scanner(System.in);
    private static InventoryManager manager = InventoryManager.getInstance();
    private static int nextId = 1;
    
    public static void main(String[] args) {
        
        if (args.length > 0) {
            System.out.println("Command line arguments received:");
            for (int i = 0; i < args.length; i++) {
                System.out.println("Arg " + i + ": " + args[i]);
            }
        }
        
        System.out.println("╔═══════════════════════════════════════════════════════╗");
        System.out.println("║   PRODUCT INVENTORY MANAGEMENT SYSTEM                ║");
        System.out.println("║   Java OOP Project                                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════╝");
        
        boolean running = true;
        
        while (running) {
            displayMenu();
            int choice = readInt("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    removeProduct();
                    break;
                case 3:
                    showAllProducts();
                    break;
                case 4:
                    sortProducts();
                    break;
                case 5:
                    updateQuantity();
                    break;
                case 6:
                    searchProduct();
                    break;
                case 7:
                    exportToCSV();
                    break;
                case 8:
                    showStatistics();
                    break;
                case 9:
                    showTransactionLog();
                    break;
                case 0:
                    System.out.println("Exiting... Thank you!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
    
    private static void displayMenu() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║            MAIN MENU                   ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("1. Add Product");
        System.out.println("2. Remove Product");
        System.out.println("3. Show All Products");
        System.out.println("4. Sort Products");
        System.out.println("5. Update Quantity");
        System.out.println("6. Search Product");
        System.out.println("7. Export to CSV");
        System.out.println("8. Show Statistics");
        System.out.println("9. Show Transaction Log");
        System.out.println("0. Exit");
        System.out.println("────────────────────────────────────────");
    }
    
    private static void addProduct() {
        System.out.println("\n=== Add Product ===");
        System.out.println("1. General Product");
        System.out.println("2. Electronics");
        System.out.println("3. Grocery");
        
        int type = readInt("Select type: ");
        
        System.out.print("Enter product name: ");
        String name = sc.nextLine();
        
        double price = readDouble("Enter price: ");
        int quantity = readInt("Enter quantity: ");
        
        Product product = null;
        
        switch (type) {
            case 1:
                double weight = readDouble("Enter weight: ");
                System.out.print("Enter unit (kg/g/L/ml/pcs): ");
                String unit = sc.nextLine();
                product = new Product(nextId++, name, price, quantity, weight, unit);
                break;
            case 2:
                int warranty = readInt("Enter warranty (months): ");
                System.out.print("Enter brand: ");
                String brand = sc.nextLine();
                product = new Electronics(nextId++, name, price, quantity, warranty, brand);
                break;
            case 3:
                weight = readDouble("Enter weight: ");
                System.out.print("Enter unit: ");
                unit = sc.nextLine();
                System.out.print("Is perishable? (true/false): ");
                boolean perishable = Boolean.parseBoolean(sc.nextLine());
                product = new Grocery(nextId++, name, price, quantity, weight, unit, perishable);
                break;
            default:
                System.out.println("Invalid type!");
                return;
        }
        
        if (product != null) {
            manager.addProduct(product);
            System.out.println("Product added successfully! ID: " + (nextId - 1));
        }
    }
    
    private static void removeProduct() {
        System.out.println("\n=== Remove Product ===");
        int id = readInt("Enter product ID to remove: ");
        
        if (manager.removeProduct(id)) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Product not found!");
        }
    }
    
    private static void showAllProducts() {
        System.out.println("\n=== All Products ===");
        manager.displayAllProducts();
    }
    
    private static void sortProducts() {
        System.out.println("\n=== Sort Products ===");
        System.out.println("1. Sort by Name");
        System.out.println("2. Sort by Price");
        System.out.println("3. Sort by Quantity");
        
        int choice = readInt("Select sorting option: ");
        
        switch (choice) {
            case 1:
                manager.sortByName();
                System.out.println("Sorted by name!");
                break;
            case 2:
                manager.sortByPrice();
                System.out.println("Sorted by price!");
                break;
            case 3:
                manager.sortByQuantity();
                System.out.println("Sorted by quantity!");
                break;
            default:
                System.out.println("Invalid option!");
        }
    }
    
    private static void updateQuantity() {
        System.out.println("\n=== Update Quantity ===");
        int id = readInt("Enter product ID: ");
        
        Product product = manager.findProductById(id);
        
        if (product != null) {
            System.out.println("Current quantity: " + product.getQuantity());
            int newQty = readInt("Enter new quantity: ");
            product.setQuantity(newQty);
            System.out.println("Quantity updated successfully!");
        } else {
            System.out.println("Product not found!");
        }
    }
    
    private static void searchProduct() {
        System.out.println("\n=== Search Product ===");
        System.out.print("Enter search keyword: ");
        String keyword = sc.nextLine();
        
        ArrayList<Product> results = manager.searchByName(keyword);
        
        if (results.isEmpty()) {
            System.out.println("No products found!");
        } else {
            System.out.println("Found " + results.size() + " product(s):");
            for (Product p : results) {
                p.display();
            }
        }
    }
    
    private static void exportToCSV() {
        System.out.println("\n=== Export to CSV ===");
        System.out.print("Enter filename (e.g., inventory.csv): ");
        String filename = sc.nextLine();
        
        try {
            manager.exportCSV(filename);
            System.out.println("Exported successfully to " + filename);
        } catch (IOException e) {
            System.out.println("Error exporting: " + e.getMessage());
        }
    }
    
    private static void showStatistics() {
        System.out.println("\n" + manager.getStatistics());
        System.out.println("Total Product Types: " + Product.getProductCount());
    }
    
    private static void showTransactionLog() {
        manager.displayTransactionLog();
    }
    
    private static int readInt(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private static double readDouble(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
