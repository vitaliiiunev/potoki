import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    static String[] products = {"Хлеб", "Яблоки", "Молоко"};
    static int[] prices = {100, 200, 300};

    static Scanner scanner = new Scanner(System.in);
    static File saveFile = new File("basket.json");


    public static void main(String[] args) {

        Basket basket = null;
        if (saveFile.exists()) {
            basket = Basket.loadFromJSONFile(saveFile);
        } else {
            basket = new Basket(products, prices);
        }

        ClientLog log = new ClientLog();
        while (true) {
            showPrice();
            System.out.println("Выберите товар и количество или введите `end`");
            String line = scanner.nextLine();

            if ("end".equals(line)) {
                log.exportAsCSV(new File("log.csv"));
                break;
            }

            String[] parts = line.split(" ");
            int productNumber = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);
            basket.addToCart(productNumber, productCount);
            log.log(productNumber, productCount);
            basket.saveJSON(saveFile);

        }
        basket.printCart();
    }

    public static void showPrice() {
        System.out.println("Список возможных товаров");
        for (int i = 0; i < products.length; i++) {
            System.out.println(products[i] + " " + prices[i] + " руб/шт.");
        }
    }
}

