package Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import Trie.Hybrid.HybridTrie;
import Trie.Patricia.PatriciaTrie;
import Trie.Patricia.TrieNode;

public class ConvertJson {
    private static String FILESTRINGPAT = "Samples/formatJson/pat.json";
    private static void toChildrenString(int length ,TrieNode node, StringBuilder result,String prefix){
        int i=0;
        boolean getchildren = false;

        for(int j=0;j<length;j++){
            result.append("\t");
        }
        result.append("\"label\" : \"").append(prefix).append("\",\n");

        if(node.isEndNode()){
            for(int j=0;j<length;j++){
                result.append("\t");
            }
            result.append("\"is_end_of_word\" : ").append("true, \n");
        }else{
            for(int j=0;j<length;j++){
                result.append("\t");
            }
            result.append("\"is_end_of_word\" : ").append("false, \n");
        }

        for(int j=0;j<length;j++){
            result.append("\t");
        }
        result.append("\"children\" : {");
        for(Map.Entry<String,TrieNode> children : node.getChildren().entrySet()){
            getchildren=true;
            result.append("\n");
            if(i+1 == node.getChildren().size()){
                for(int j=0;j<length;j++){
                    result.append("\t");
                }
                result.append("\""+ children.getKey().charAt(0)+"\": {\n");
                toChildrenString(length+1, children.getValue(), result, children.getKey());
                for(int j=0;j<length;j++){
                    result.append("\t");
                }
                result.append("}\n");
            }else{
                for(int j=0;j<length;j++){
                    result.append("\t");
                }
                result.append("\""+ children.getKey().charAt(0)+"\": {\n");
                toChildrenString(length+1, children.getValue(), result, children.getKey());
                for(int j=0;j<length;j++){
                    result.append("\t");
                }
                result.append("},");
            }
            i++;

        };

        if(getchildren){
            for(int j=0;j<length;j++){
                result.append("\t");
            }
        }
        
        result.append("}\n");
        
        return;
    }

    private static String formatJsonPatricia(TrieNode root) {
        StringBuilder result = new StringBuilder("{\n");
        toChildrenString(1, root, result,"");
        return result.toString() + "}\n\n";
    }


    public static void convertPatriciaToJson(PatriciaTrie pat) {
        String patStringJson = formatJsonPatricia(pat.getRoot());

        File file = new File(FILESTRINGPAT);
        
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(patStringJson);
            System.out.println("Arbre Patricia ajouté dans " + FILESTRINGPAT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

    public static Map<String, Object> parseJsonString(String jsonString) {
        Map<String, Object> map = new HashMap<>();

        // Supprimer les accolades de début et de fin
        jsonString = jsonString.trim();
        if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
            jsonString = jsonString.substring(1, jsonString.length() - 1);
        }

        int i = 0;
        while (i < jsonString.length()) {
            // Ignorer les espaces blancs
            i = skipWhitespace(jsonString, i);
            
            // Lire la clé
            if (jsonString.charAt(i) == '"') {
                int keyStart = i + 1;
                int keyEnd = jsonString.indexOf('"', keyStart);
                String key = jsonString.substring(keyStart, keyEnd);
                i = keyEnd + 1;

                // Avancer jusqu'au prochain :
                while (i < jsonString.length() && jsonString.charAt(i) != ':') {
                    i++;
                }
                i++; // Passer le deux-points

                // Lire la valeur
                i = skipWhitespace(jsonString, i);
                if (jsonString.charAt(i) == '"') {  // Valeur simple sous forme de chaîne
                    int valueStart = i + 1;
                    int valueEnd = jsonString.indexOf('"', valueStart);
                    String value = jsonString.substring(valueStart, valueEnd);
                    map.put(key, value);
                    i = valueEnd + 1;
                } else if (jsonString.charAt(i) == '{') {  // Valeur sous forme d'objet JSON
                    int braceStart = i;
                    int braceEnd = findClosingBrace(jsonString, braceStart);
                    String nestedJson = jsonString.substring(braceStart, braceEnd + 1);
                    map.put(key, parseJsonString(nestedJson));
                    i = braceEnd + 1;
                }

                // Aller au prochain élément
                i = skipWhitespace(jsonString, i);
                if (i < jsonString.length() && jsonString.charAt(i) == ',') {
                    i++;
                }
            }
        }

        return map;
    }

    // Méthode pour ignorer les espaces
    private static int skipWhitespace(String jsonString, int i) {
        while (i < jsonString.length() && Character.isWhitespace(jsonString.charAt(i))) {
            i++;
        }
        return i;
    }

    // Trouver la fin de l'accolade fermante pour un objet imbriqué
    private static int findClosingBrace(String jsonString, int openBraceIndex) {
        int count = 0;
        for (int i = openBraceIndex; i < jsonString.length(); i++) {
            if (jsonString.charAt(i) == '{') {
                count++;
            } else if (jsonString.charAt(i) == '}') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1; // Si la syntaxe JSON est incorrecte
    }

    public static PatriciaTrie convertJsonToPatricia(File file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                json.append(line);
            }

            PatriciaTrie pat = new PatriciaTrie();


            return pat;
        } catch (IOException e){
            e.getStackTrace();
        }

        return null;
    }

    public void convertHybridToJson(HybridTrie hybrid) {
    }
}
