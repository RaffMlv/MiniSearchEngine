import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SearchEngine {

    private HashMap<String, HashMap<String, Integer>> index;

    public SearchEngine(){
        index = new HashMap<>();
    }
    public void buildIndex(Path documentsPath) throws IOException {
        for (Path path : Files.list(documentsPath).toList()) {

            String content = Files.readString(path);
            String[] words = TextProcessor.processText(content);

            String documentName = path.getFileName().toString();


            for (String word : words) {

                if (word.isEmpty()) {
                    continue;
                }


                index
                        .computeIfAbsent(word, k -> new HashMap<>())
                        .merge(documentName, 1, Integer::sum);
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

                double tf = termFrequency;

                double score = tf * idf;

                scores.put
                        (document,
                                scores.getOrDefault(document, 0.0) + score
                        );
            }
        }
        return scores;
    }

    public int getTotalDocuments(){
        HashSet<String> documents = new HashSet<>();

        for (HashMap<String, Integer> documentMap : index.values()){
            documents.addAll(documentMap.keySet());
        }

        return documents.size();
    }
}
