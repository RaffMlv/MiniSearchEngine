import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class Main {

    private static String normalize(String text){
        text = text.toLowerCase();
        text = text.replaceAll("[^a-zA-Z0-9]", "");

        return text;
    }

    private static HashMap<String, HashSet<String>> buildIndex(Path docuemntsPath) throws IOException{
        Path documentsPath = Path.of("documents");

        HashMap<String, HashSet<String>> index = new HashMap<>();

        for(Path path : Files.list(documentsPath).toList()){

            String content = Files.readString(path);

            String[] words = content.split("\\s+");

            String documentName = path.getFileName().toString();

            for (String word : words) {
                word = normalize(word);

                if (word.isEmpty()) {
                    continue;
                }

                if (!index.containsKey(word)) {
                    HashSet<String> set = new HashSet<>();
                    set.add(documentName);
                    index.put(word, set);
                } else {
                    index.get(word).add(documentName);
                }
            }
        }

        return index;
    }

    private static HashSet<String> search(HashMap<String, HashSet<String>> index, String query){
        String[] queryComponents = query.split("\\s+");
        for (int i = 0; i < queryComponents.length; i++) {
            queryComponents[i] = normalize(queryComponents[i]);
        }
        HashSet<String> firstDocuments = index.get(queryComponents[0]);

        if (firstDocuments == null){
            return null;
        }

        HashSet<String> results = new HashSet<>(firstDocuments);

        for (int i = 1; i < queryComponents.length; i++){
            HashSet<String> documents = index.get(queryComponents[i]);

            if (documents == null) {
                return null;
            }
            results.retainAll(documents);
        }
        return results;
    }

    public static void main(String[] args) throws IOException {

        Path documentsPath = Path.of("documents");

        HashMap<String, HashSet<String>> index = buildIndex(documentsPath);

        System.out.println("Mini Search Engine \n ------------------\n");

        System.out.print("Search: ");

        Scanner scanner = new Scanner(System.in);

        String query = scanner.nextLine();

        HashSet<String> results = search(index, query);

        if (results == null) {
            System.out.println("No documents found.");
        } else {
            System.out.println("Results:");
            for (String document : results) {
                System.out.println("- " + document);
            }
        }
        scanner.close();
    }
}
