import java.util.HashSet;
import java.util.Set;

public class TextProcessor {

    private static final Set<String> STOP_WORDS = new HashSet<>();

    static {
        STOP_WORDS.add("a");
        STOP_WORDS.add("an");
        STOP_WORDS.add("the");
        STOP_WORDS.add("is");
        STOP_WORDS.add("are");
        STOP_WORDS.add("of");
        STOP_WORDS.add("and");
        STOP_WORDS.add("to");
        STOP_WORDS.add("in");
        STOP_WORDS.add("for");
        STOP_WORDS.add("on");
        STOP_WORDS.add("by");
        STOP_WORDS.add("with");
        STOP_WORDS.add("as");
        STOP_WORDS.add("it");
        STOP_WORDS.add("or");
        STOP_WORDS.add("can");
        STOP_WORDS.add("be");
    }

    public static boolean isStopWord(String word){
        return STOP_WORDS.contains(word);
    }
    public static String normalize(String text){
        text = text.toLowerCase();
        text = text.replaceAll("[^a-zA-Z0-9]", "");

        return text;
    }

    public static String stem(String word){
        if(word.endsWith("s") && word.length() > 3){
            return word.substring(0, word.length() - 1);
        }

        return word;
    }

    public static String[] processText(String text){
        String[] words = text.split("\\s+");

        for (int i = 0; i < words.length; i++) {
            words[i] = stem(normalize(words[i]));

            if (isStopWord(text)){
                words[i] = "";
            }
        }

        return words;
    }
}
