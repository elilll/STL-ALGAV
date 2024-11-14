package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import Trie.Patricia.PatriciaTrie;
import Util.ConvertJson;

public class Main {
    public static void main(String[] args) {
        File exemple = new File("Samples/formatTxt/ExemplePatricia.txt");

        if(exemple.exists()){
            try (java.util.Scanner scanner = new java.util.Scanner(exemple)) {
                PatriciaTrie trie = new PatriciaTrie();
                while(scanner.hasNext()){
                    trie.insertSentence(scanner.nextLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 


        try(BufferedReader reader = new BufferedReader(new FileReader("Samples/formatJson/pat.json"))) {
            StringBuilder jsonString = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                jsonString.append(line);
            }

            Map<String, Object> resultMap = ConvertJson.parseJsonString(jsonString.toString());
        
        // Afficher la Map résultante
            for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
                System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
            }
        } catch (IOException e){
            e.getStackTrace();
        }
    }
}
