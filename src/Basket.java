import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.Arrays;
import static java.lang.System.out;

public class Basket {
    private String[] goods;
    private int[] prices;
    private int[] quantities;

    public Basket() {

    }

    public Basket(String[] goods, int[] prices) {
        this.goods = goods;
        this.prices = prices;
        this.quantities = new int[goods.length];
    }

    public void addToCart(int productNum, int amount) {
        quantities[productNum] += amount;
    }

    public void printCart() {
        int totalPrices = 0;
        out.println("Список покупок: ");

        for (int i = 0; i < goods.length; i++) {
            if (quantities[i] > 0) {
                int currentPrice = prices[i] * quantities[i];
                totalPrices += currentPrice;
                out.printf("%15s%4d р/шт%4d шт%6d p%n", goods[i], prices[i], quantities[i], currentPrice);
            }
        }
        out.printf("Итого: %dp", totalPrices);

    }

    public void saveJSON(File textFile) {

        try (PrintWriter writer = new PrintWriter(textFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this);
            writer.print(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromJSONFile(File textFile) {

        Basket basket = new Basket();
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {

            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {

                builder.append(line);
            }
            Gson gson = new Gson();
            basket = gson.fromJson(builder.toString(), Basket.class);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }

    public static Basket loadFromTxtFile(File textFile) {
        Basket basket = new Basket();
        try (BufferedReader reader = new BufferedReader(new FileReader(textFile))) {

            String goodsStr = reader.readLine();
            String pricesStr = reader.readLine();
            String quantitiesStr = reader.readLine();

            basket.goods = goodsStr.split(" ");
            basket.prices = Arrays.stream(pricesStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();
            basket.quantities = Arrays.stream(quantitiesStr.split(" "))
                    .map(Integer::parseInt)
                    .mapToInt(Integer::intValue)
                    .toArray();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }

    public void saveTxt(File textFile) {
        try (PrintWriter writer = new PrintWriter(textFile)) {
            writer.println(String.join(" ", goods));
            writer.println(String.join(" ", Arrays.stream(prices)
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new)));
            writer.println(String.join(" ", Arrays.stream(quantities)
                    .mapToObj(String::valueOf)
                    .toArray(String[]::new)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveBin(File file) throws FileNotFoundException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {

            oos.writeObject(this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static Basket loadFromBinFile(File file) throws FileNotFoundException {

        Basket basket = null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            basket = (Basket) ois.readObject();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return basket;
    }
}
