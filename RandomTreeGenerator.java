import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class RandomTreeGenerator {

    public static void generateTree(String filename, int n) {

        File file = new File(filename);

        if (file.exists()) {
            System.out.println(filename + " already exists. Skipping.");
            return;
        }

        Random random = new Random();

        int[] depth = new int[n];
        int height = 0;

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

            out.println(n + " " + (n - 1));

            for (int i = 1; i < n; i++) {
                int parent = random.nextInt(i);
                int weight = random.nextInt(1000) + 1;
                out.println(parent + " " + i + " " + weight);
                depth[i] = depth[parent] + 1;

                if (depth[i] > height) {
                    height = depth[i];
                }
            }
            System.out.println(
                    filename + " created: n=" + n +
                    ", edges=" + (n - 1) +
                    ", height=" + height
            );
        } catch (IOException e) {
            System.out.println("Error creating file: " + filename);
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {

        generateTree("tree5.txt", 100);
        generateTree("tree6.txt", 1000);
        generateTree("tree7.txt", 10000);
    }
}