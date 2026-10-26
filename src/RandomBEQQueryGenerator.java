import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class RandomBEQQueryGenerator {

    public static void generateQueries(
            String filename,
            int nodeNum,
            int queryNum) {

        File file = new File(filename);
        if (file.exists()) {
            System.out.println(filename + " already exists. Skipping.");
            return;
        }

        Random random = new Random();

        try (PrintWriter out =
                     new PrintWriter(new FileWriter(file))) {

            out.println(queryNum);

            for (int i = 0; i < queryNum; i++) {

                int u = random.nextInt(nodeNum);
                int v = random.nextInt(nodeNum);

               
                while (u == v) {
                    v = random.nextInt(nodeNum);
                }

                out.println(u + " " + v);
            }

            System.out.println(
                filename + " created: " +
                queryNum + " queries for " +
                nodeNum + " nodes."
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
            "queries_beq5.txt",
            100,
            20
        );

        generateQueries(
            "queries_beq6.txt",
            1000,
            20
        );

        generateQueries(
            "queries_beq7.txt",
            10000,
            20
        );
    }
}