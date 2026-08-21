import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class SearchEngine {

    private HashMap<String, HashMap<String, Integer>> index;
    private HashMap<String, Integer> documentLengths;

    public SearchEngine(){

        index = new HashMap<>();
        documentLengths = new HashMap<>();
    }
    public void buildIndex(Path documentsPath) throws IOException {

        try (var paths = Files.list(documentsPath)) {

            for (Path path : paths.toList()) {

                String content = Files.readString(path);
                String[] words = TextProcessor.processText(content);

                String documentName = path.getFileName().toString();

                int totalTerms = 0;

                for (String word : words) {

                    if (word.isEmpty()) {
                        continue;
                    }

                    totalTerms++;

                    index
                            .computeIfAbsent(word, k -> new HashMap<>())
                            .merge(documentName, 1, Integer::sum);
                }

                documentLengths.put(documentName, totalTerms);
            }
        }
    }

    public Map<String, Double> search(String query) {

        String[] queryComponents = TextProcessor.processText(query);

        Map<String, Double> scores = new HashMap<>();

        int totalDocuments = getTotalDocuments();

        for (String word : queryComponents){
            if (word.isEmpty()){
                continue;
            }

            HashMap<String, Integer> documents = index.get(word);

            if (documents == null){
                continue;
            }

            double idf = Math.log((double) totalDocuments / documents.size());

            for(Map.Entry<String, Integer> entry : documents.entrySet()){

                String document = entry.getKey();
                int termFrequency = entry.getValue();

                int documentLength = documentLengths.get(document);

                double tf = (double) termFrequency/documentLength;

                double score = tf * idf;

                scores.put
                        (document,
                                scores.getOrDefault(document, 0.0) + score
                        );
            }
        }
        return scores;
    }

    public int getTotalDocuments() {
        return documentLengths.size();
    }
}
