import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        Path documentsPath = Path.of("documents");

        SearchEngine searchEngine = new SearchEngine();

        searchEngine.buildIndex(documentsPath);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Mini Search Engine");
        System.out.println("------------------");

        while (true) {

            System.out.print("Search: ");

            String query = scanner.nextLine();

            if (query.equalsIgnoreCase("exit")) {
                break;
            }

            Map<String, Double> results = searchEngine.search(query);

            if (results.isEmpty()) {
                System.out.println("No documents found.");
            } else {
                System.out.println("Found in:");

                List<Map.Entry<String, Double>> sortedResults = new ArrayList<>(results.entrySet());

                sortedResults.sort(
                        (a, b) -> Double.compare(
                                b.getValue(),
                                a.getValue()
                        )
                );

                for (Map.Entry<String, Double> result : sortedResults) {
                    System.out.println("- " + result.getKey() +
                            " (score: " + result.getValue() + ")");
                }
            }
        }

        scanner.close();
    }
}
