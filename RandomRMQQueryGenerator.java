import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class RandomRMQQueryGenerator {

    public static void generateQueries(
            String filename,
            int arraySize,
            int queryNum) {

        File file = new File(filename);

        if (file.exists()) {
            System.out.println(
                filename + " already exists. Skipping."
            );
            return;
        }

        Random random = new Random();

        try (PrintWriter out =
                     new PrintWriter(new FileWriter(file))) {

         
            out.println(queryNum);

            for (int i = 0; i < queryNum; i++) {

                int a = random.nextInt(arraySize);
                int b = random.nextInt(arraySize);

                while (a == b) {
                    b = random.nextInt(arraySize);
                }

                int left = Math.min(a, b);
                int right = Math.max(a, b);

                out.println(left + " " + right);
            }

            System.out.println(
                filename + " created: " +
                queryNum + " queries for array size " +
                arraySize
            );

        } catch (IOException e) {

            System.out.println(
                "Error creating file: " + filename
            );

            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        generateQueries(
            "queries_rmq6.txt",
            100,
            20
        );

        generateQueries(
            "queries_rmq7.txt",
            1000,
            20
        );

        generateQueries(
            "queries_rmq8.txt",
            10000,
            20
        );
    }
}