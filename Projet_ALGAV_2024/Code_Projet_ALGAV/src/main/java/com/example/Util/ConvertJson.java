package com.example.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.example.Trie.Hybrid.HybridTrie;
import com.example.Trie.Hybrid.HybridTrieNode;
import com.example.Trie.Patricia.PatriciaTrie;
import com.example.Trie.Patricia.PatriciaTrieNode;

@SuppressWarnings("CallToPrintStackTrace")
public class ConvertJson {

    /* ****************************  PATRICIA TRIE  *********************************** */


    private static void patriciaTrieString(String tab ,PatriciaTrieNode node, StringBuilder result,String prefix){
        /* Label */
        result.append(tab).append("\"label\" : \"").append(prefix).append("\",\n");

        /* Is end of node */
        if(node.isEndNode()){
            result.append(tab).append("\"is_end_of_word\" : ").append("true, \n");
        }else{
            result.append(tab).append("\"is_end_of_word\" : ").append("false, \n");
        }

        /* Children */
        result.append(tab).append("\"children\" : {");

        int i=0;
        boolean getchildren = false;

        for(Map.Entry<String,PatriciaTrieNode> children : node.getChildren().entrySet()){
            getchildren=true;
            result.append("\n");
            if(i+1 == node.getChildren().size()){
                result.append(tab + "\t").append("\""+ children.getKey().charAt(0)+"\": {\n");
                patriciaTrieString(tab + "\t\t", children.getValue(), result, children.getKey());

                result.append(tab + "\t").append("}\n");
            }else{
                result.append(tab + "\t").append("\""+ children.getKey().charAt(0)+"\": {\n");
                patriciaTrieString(tab + "\t\t", children.getValue(), result, children.getKey());
                result.append(tab + "\t").append("},");
            }
            i++;
        }

        if(getchildren){
            result.append(tab);
        }
        
        result.append("}\n");
        
        return;
    }

    private static String formatJsonPatricia(PatriciaTrieNode root) {
        StringBuilder result = new StringBuilder("{\n");
        patriciaTrieString("\t", root, result,"");
        return result.toString() + "}";
    }
    /* Méthode qui permet de convertir un objet Patricia Trie en format json dans le fichier pat.json */
    public static void convertPatriciaToJson(PatriciaTrie pat) {
        String patStringJson = formatJsonPatricia(pat.getRoot());

        File file = new File(Constant.PATJSONFILE);
        
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(patStringJson);
            System.out.println("Arbre Patricia ajouté dans " + Constant.PATJSONFILE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

    /**
     * Méthode qui permet de convertir un format json en objet Patricia Trie.
     * @param file : Le fichier à convertion en PatriciaTrie.
     * 
     * @return : Le patriciaTrie créer à partir de @param file.
    */
    public static PatriciaTrie convertJsonToPatricia(File file) {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                json.append(line);
            }

            PatriciaTrie pat = new PatriciaTrie();
            ParseJson.parseJsonToPatriciaTrieNode(json.toString(),pat.getRoot());
            
            return pat;
        } catch (Exception e) {
            e.getStackTrace();
        }

        return null;
    }

    /* ****************************  HYBRID TRIE  *********************************** */

    private static void hybridTrieString(String tab, HybridTrieNode node, StringBuilder result) {
        if (node == null) {
            result.append(tab).append("null");
            return;
        }
    
        result.append("{\n");
    
        // "char"
        result.append(tab).append("\t").append("\"char\": \"").append(node.getCar()).append("\",\n");
    
        // "is_end_of_word"
        if (node.getVal() == HybridTrieNode.ENDWORD) {
            result.append(tab).append("\t").append("\"is_end_of_word\": ").append("true,\n");
        } else {
            result.append(tab).append("\t").append("\"is_end_of_word\": ").append("false,\n");
        }
    
        // "left"
        result.append(tab).append("\t").append("\"left\": ");
        if (node.getPointeurs()[HybridTrieNode.INF] != null) {
            hybridTrieString(tab + "\t", node.getPointeurs()[HybridTrieNode.INF], result);
        } else {
            result.append("null");
        }
        result.append(",\n");
    
        // "middle"
        result.append(tab).append("\t").append("\"middle\": ");
        if (node.getPointeurs()[HybridTrieNode.EQ] != null) {
            hybridTrieString(tab + "\t", node.getPointeurs()[HybridTrieNode.EQ], result);
        } else {
            result.append("null");
        }
        result.append(",\n");
    
        // "right"
        result.append(tab).append("\t").append("\"right\": ");
        if (node.getPointeurs()[HybridTrieNode.SUP] != null) {
            hybridTrieString(tab + "\t", node.getPointeurs()[HybridTrieNode.SUP], result);
        } else {
            result.append("null");
        }
        result.append("\n");
    
        result.append(tab).append("}");

    }    

    public static String formatJsonHybrid(HybridTrieNode root) {
        StringBuilder result = new StringBuilder();
        hybridTrieString("", root, result);
        return result.toString();
    }

    /**
     * Méthode qui convertit le trie hybride au format json
     * @param hybrid : le trie hybride à convertir au format json
     */
    public static void convertHybridToJson(HybridTrie hybrid) {
        String hybStringJson = formatJsonHybrid(hybrid.getRoot());
    
        File file = new File(Constant.HYBRIDJSONFILE);
    
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(hybStringJson);
            System.out.println("Arbre Hybrid Trie ajouté dans " + Constant.HYBRIDJSONFILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    /**
     * Méthode qui convertit un format json en objet HybridTrie
     * @param file : le fichier .json à lire
     * @return : le trie hybride obtenu par la conversion du fichier.json
     */
    public static HybridTrie convertJsonToHybridTrie(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line); 
            }
            HybridTrie hybridTrie = new HybridTrie();
            ParseJson.parseJsonToHybridTrieNode(json.toString(), hybridTrie.getRoot());
            return hybridTrie;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }    

}
