import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    static String[] products = {"Хлеб", "Яблоки", "Молоко"};
    static int[] prices = {100, 200, 300};

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws XPathExpressionException, ParserConfigurationException, IOException, SAXException {
        XMLSettingReader settings = new XMLSettingReader(new File("shop.xml"));
        File loadFile = new File(settings.loadFile);
        File saveFile = new File(settings.saveFile);
        File logFile = new File(settings.logFile);

        Basket basket = createBasket(loadFile, settings.isLoad, settings.loadFormat);

        ClientLog log = new ClientLog();
        while (true) {
            showPrice();
            System.out.println("Выберите товар и количество или введите `end`");
            String line = scanner.nextLine();

            if ("end".equals(line)) {
                if (settings.isLog) {
                    log.exportAsCSV(logFile);
                }
                break;
            }

            String[] parts = line.split(" ");
            int productNumber = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);
            basket.addToCart(productNumber, productCount);
            if (settings.isLog) {
                log.log(productNumber, productCount);
            }
            if (settings.isSave) {
                switch (settings.saveFormat) {
                    case "json" -> basket.saveJSON(saveFile);
                    case "txt" -> basket.saveTxt(saveFile);
                }
            }
        }
        basket.printCart();
    }

    private static Basket createBasket(File loadFile, boolean isLoad, String loadFormat) {
        Basket basket;

        if (isLoad && loadFile.exists()) {
            basket = switch (loadFormat) {
                case "json" -> Basket.loadFromJSONFile(loadFile);
                case "txt" -> Basket.loadFromTxtFile(loadFile);
                default -> new Basket(products, prices);
            };
        } else {
            basket = new Basket(products, prices);
        }
        return basket;
    }

    public static void showPrice() {
        System.out.println("Список возможных товаров");
        for (int i = 0; i < products.length; i++) {
            System.out.println(products[i] + " " + prices[i] + " руб/шт.");
        }
    }
}

