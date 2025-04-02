import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class TestEpicor {

    //considering below set as excluding predefined. + excluding list provided in Question.
    static Set<String> excludeWords = new HashSet<>(Arrays.asList("in", "on", "at", "he", "she", "it", "and", "or", "but", "the" , "a", "an", "is", "was", "are", "will", "would", "should", "can", "could", "may", "might", "shall", "must", "being", "been")); // Add more words as needed

    static Set<String> prepositions = new HashSet<>(Arrays.asList("about", "above", "across", "after", "against", "along", "among", "around", "at", "before", "behind", "below", "beneath", "beside", "between", "beyond", "by", "down", "during", "except",
            "for", "from", "in", "into", "of", "off", "on", "onto", "out", "over", "past", "since", "through", "throughout", "to", "toward", "under", "underneath", "until", "up", "upon", "with", "within", "without", "according to", "because of", "due to", "in front of", "instead of", "out of", "apart from", "aside from", "prior to", "next to", "as far as"));

    static Set<String> pronouns = new HashSet<>(Arrays.asList("I", "you", "he", "she", "it", "we", "they", "me", "him", "her", "us", "them", "my", "your", "his", "her", "its", "our", "their","mine", "yours", "his", "hers", "ours", "theirs", "myself", "yourself", "himself", "herself", "itself", "ourselves", "yourselves", "themselves",
            "who", "whom", "whose", "which", "what", "this", "that", "these", "those", "anyone", "everyone", "someone", "no one", "each", "none", "either", "neither", "all", "few", "many", "several", "some", "any"));

    static Set<String> conjunctions = new HashSet<>(Arrays.asList("and", "but", "or", "nor", "for", "so", "yet", "although", "because", "since", "unless", "until", "while",
            "whereas", "after", "before", "once", "even though", "if", "though", "rather than", "whether", "both...and", "either...or", "neither...nor", "not only...but also", "as well as"));

    static Set<String> articles = new HashSet<>(Arrays.asList("a", "an", "the"));


    public static void main(String[] args) {
        try{
            long startTime = System.currentTimeMillis();

                    // pointer to a resource on the web.
            URL url = new URL("https://courses.cs.washington.edu/courses/cse390c/22sp/lectures/moby.txt");
                    //url.openStream() -> connects to resource and creates InputStream of the resource.
                    //InputStreamReader(only 1 object for entire file) -> reads bytes of Stream Data and decodes into characters by using UTF-8.
                    // so now we have Stream of Character data from resource file.
            InputStreamReader isr = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
                    //a wrapper object of BufferedReader(only 1 object for entire file) will be crated for the character output from InputStreamReader , which has efficient ways in reading data.
                    // This is just for reading the characters.
                    //Since all connection is in Stream data will be read by BufferedReader line by line here.
            BufferedReader bf = new BufferedReader( isr );

                    //When printing large data in console mismatch is occuring but able to store correct data in file.
                    //String outputPath = "C:\\Users\\b2vj2\\Desktop\\Java8FeaturesProject\\EpicoreTest\\epicor.txt";
                    //FileWriter fw = new FileWriter(outputPath);

            StringBuilder content = new StringBuilder();
            String inputLine ;
            while ( (inputLine = bf.readLine()) != null ){
                content.append(inputLine).append(" ");
                    //fw.write(inputLine+"\n");
            }
            isr.close();
            //fw.close();


            //Report Generating:
            String reportFile = "C:\\Users\\b2vj2\\Desktop\\Java8FeaturesProject\\EpicoreTest\\report.txt";
            FileWriter fwOut = new FileWriter(reportFile);

            System.out.println("-------------------Report-------------------");
            //1. Count words
            String[] words = content.toString().split("\\W+"); // regex to split based on all non word characters
            System.out.println("Total Word Count: "+words.length); //214710
            fwOut.write("Total Word Count: "+words.length+"\n");

            //2. MostFrequent words and its count: word occurrence >= 15
            Map<String,Integer> mapCounter = new HashMap<>();
            Arrays.stream(words)
                    .map( s -> s.trim())
                    .map(String::toLowerCase) //converting to lowercase to not miss filtering of capital excluded words
                    .filter( f ->  !excludeWords.contains(f) && !prepositions.contains(f) && !pronouns.contains(f) && !conjunctions.contains(f) && !articles.contains(f) ) // for prepositions,conjuctions,articles,modal verbs
                    .filter(f -> !f.endsWith("'s")) // for 's
                    .filter(f -> !f.matches("\\d+")) // for digits
                    .filter(f -> !f.matches("^\\d+.*")) // starting digit
                    .filter(f -> !f.matches(".*\\d+$"))  //ending digit
                    .filter(f -> f.length() > 2) // ignoring i , a  etc.. and other 2 char length words
                    .forEach( m ->  mapCounter.put(m,mapCounter.getOrDefault(m,0) + 1));

            System.out.println("Most Frequent Top 5 Words:  -------------------------------------------");
            fwOut.write("Most Frequent Top 5 Words:  -------------------------------------------\n");
            mapCounter.entrySet().stream().sorted( (a,b) -> b.getValue() - a.getValue() ) //descending order
                            .limit(5) // top 5 max
                            .forEach( m -> {
                                System.out.println("W: "+m.getKey() +"  Count: "+ m.getValue());
                                try {
                                    fwOut.write("W: "+m.getKey() +"  Count: "+ m.getValue()+"\n");
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            });


            System.out.println("Unique Words in alphabetic order : ------------------------------------");
            fwOut.write("Unique Words in alphabetic order : ------------------------------------\n");
            mapCounter.entrySet().stream()
                    .filter( entry -> entry.getValue() == 1)
                    .sorted((a,b) -> a.getKey().compareTo(b.getKey()))
                    .limit(50)
                    .forEach(m -> {
                        System.out.println("W: "+m.getKey() +"   Count: "+m.getValue());
                        try {
                            fwOut.write("W: "+m.getKey() +"  Count: "+ m.getValue()+"\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });


        fwOut.close();









            long endTime = System.currentTimeMillis();
            double seconds = (endTime - startTime)/1000.0 ;
            System.out.println("Execution Time: Total seconds  "+ seconds);
            System.out.println("Execution Time: Total minutes  "+ seconds/ 60.0);


        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
