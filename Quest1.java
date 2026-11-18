import java.text.DecimalFormat;
import java.util.Scanner;

// Q.1.1 - main program 
public class Quest1 {

    public static void main(String[] args) {
        String[] productNames = {"Cozy Nights Monarch", "Restonic Barbados", "Sealy Phuket"};
        int months = 3;
        int[][] sales = new int[productNames.length][months];

        Scanner input = new Scanner(System.in);

        for (int p = 0; p < productNames.length; p++) {
            for (int m = 0; m < months; m++) {
                System.out.print("Enter number of sales for month " + (m + 1)
                        + " for " + productNames[p] + " mattress: ");
                sales[p][m] = input.nextInt();
            }
            System.out.println();
        }

        Product product = new Product(productNames, sales);
        PrintProduct printer = new PrintProduct(product);

        System.out.println();
        System.out.println("PRODUCT SALES REPORT - 2026");
        System.out.println();
        System.out.println("************************************************");
        printer.printTotalProductSales();
        printer.printAverageProductSales();
        printer.printMostPopularProduct();
    }
}

// Q.1.2 - interface
interface IProduct {
    int GetTotalProductsSold();

    double GetAverageProductSold();

    String GetMostPopularProduct();
}

// Q.1.2 - class implementing the interface
class Product implements IProduct {

    private final String[] productNames;
    private final int[][] sales; // sales[product][month]

    public Product(String[] productNames, int[][] sales) {
        this.productNames = productNames;
        this.sales = sales;
    }

    @Override
    public int GetTotalProductsSold() {
        int total = 0;
        for (int[] productSales : sales) {
            for (int monthSales : productSales) {
                total += monthSales;
            }
        }
        return total;
    }

    @Override
    public double GetAverageProductSold() {
        int count = 0;
        for (int[] productSales : sales) {
            count += productSales.length;
        }
        return count == 0 ? 0 : (double) GetTotalProductsSold() / count;
    }

    @Override
    public String GetMostPopularProduct() {
        return productNames[findBestProductIndex()];
    }

    // The figure shown in brackets in the sample output
    public int GetMostPopularSales() {
        int best = 0;
        for (int[] productSales : sales) {
            for (int monthSales : productSales) {
                best = Math.max(best, monthSales);
            }
        }
        return best;
    }

    private int findBestProductIndex() {
        int bestIndex = 0;
        int bestValue = Integer.MIN_VALUE;
        for (int p = 0; p < sales.length; p++) {
            for (int m = 0; m < sales[p].length; m++) {
                if (sales[p][m] > bestValue) {
                    bestValue = sales[p][m];
                    bestIndex = p;
                }
            }
        }
        return bestIndex;
    }
}

// Q.1.3 - printing class
class PrintProduct {

    private final Product product;

    public PrintProduct(Product product) {
        this.product = product;
    }

    public void printTotalProductSales() {
        System.out.println("TOTAL PRODUCT SALES: " + product.GetTotalProductsSold());
    }

    public void printAverageProductSales() {
        System.out.println("AVERAGE PRODUCT SALES: "
                + new DecimalFormat("#.##").format(product.GetAverageProductSold()));
    }

    public void printMostPopularProduct() {
        System.out.println("MOST POPULAR PRODUCT: " + product.GetMostPopularProduct()
                + "(" + product.GetMostPopularSales() + ")");
    }
}