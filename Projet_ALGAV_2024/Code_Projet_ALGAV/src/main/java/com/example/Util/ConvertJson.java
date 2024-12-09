package com.example.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.example.Trie.Hybrid.HybridTrie;
import com.example.Trie.Patricia.PatriciaTrie;
import com.example.Trie.Patricia.PatriciaTrieNode;


public class ConvertJson {
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

    /**
     * Méthode qui convertit le trie hybride au format json
     * @param hybrid : le trie hybride à convertir au format json
     */
    public static void convertHybridToJson(HybridTrie hybrid) {
        //TODO
        //Oriane
    }

    /**
     * Méthode qui convertit un format json en objet HybridTrie
     * @param file : le fichier .json à lire
     * @return : le trie hybride obtenu par la conversion du fichier.json
     */
    public static HybridTrie convertJsonToHybridTrie(File file){
        //TODO
        //Oriane
        return null;
    }
}
