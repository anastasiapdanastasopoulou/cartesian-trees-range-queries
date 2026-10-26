import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class RandomArrayGenerator {

    public static void generateArray(String filename, int n) {

        File file = new File(filename);

        if (file.exists()) {
            System.out.println(filename + " already exists. Skipping.");
            return;
        }

        Random random = new Random();

        try (PrintWriter out =
                     new PrintWriter(new FileWriter(file))) {

            out.println(n);

            for (int i = 0; i < n; i++) {

                int value = random.nextInt(100000) + 1;

                out.print(value);

                if (i < n - 1) {
                    out.print(" ");
                }
            }

            out.println();

            System.out.println(
                filename + " created: n=" + n
            );

        } catch (IOException e) {

            System.out.println(
                "Error creating file: " + filename
            );

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        generateArray("array6.txt", 100);
        generateArray("array7.txt", 1000);
        generateArray("array8.txt", 10000);
    }
}