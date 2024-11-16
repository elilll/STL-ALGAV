package Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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

    public static PatriciaTrie convertJsonToPatricia(File file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                json.append(line);
            }

            System.out.println("Convert json to patricia");

            PatriciaTrie pat = new PatriciaTrie();
            ParseJson.parseJsonToPatriciaTrieNode(json.toString(),pat.getRoot());

            return pat;
        } catch (Exception e) {
            e.getStackTrace();
        }

        return null;
    }

    public void convertHybridToJson(HybridTrie hybrid) {
    }
}
