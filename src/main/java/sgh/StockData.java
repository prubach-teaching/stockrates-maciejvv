package sgh;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class StockData {

    public static void getAndProcessChange(String stock) throws IOException {
        String filePath = "data_in/" + stock + ".csv";

        File dataIn = new File(filePath);
        if (!dataIn.exists()) {
            download("https://query1.finance.yahoo.com/v7/finance/download/" + stock +
                            "?period1=1554504399&period2=1586126799&interval=1d&events=history",
                    filePath);
        }

        Scanner scan = new Scanner(dataIn);
        String line = scan.nextLine();

        FileWriter dataOut = new FileWriter("data_out" + stock +".csv");
        dataOut.write(line + ",Change (in %)" + "\n");

        while (scan.hasNextLine()) {
            line = scan.nextLine();
            String[] values = line.split(",");
            double opening = Double.valueOf(values[1]);
            double closing = Double.valueOf(values[4]);
            dataOut.write(line + "," + ((closing - opening) / opening) * 100 + "\n");
        }
        dataOut.close();
    }

    public static void download(String url, String fileName) throws IOException {
        try (InputStream in = URI.create(url).toURL().openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }

    public static void main(String[] args) throws IOException {
        String[] stocks = new String[] { "IBM", "MSFT", "GOOG" };
        for (String s : stocks) {
            getAndProcessChange(s);
        }
    }
}
