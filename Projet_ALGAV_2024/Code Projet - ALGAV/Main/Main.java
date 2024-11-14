package Main;

import java.io.File;
import java.util.Map;

import Trie.Patricia.PatriciaTrie;
import Trie.Patricia.TrieNode;
import Util.ConvertJson;

public class Main {

    public static void main(String[] args) {
        /*File exemple = new File("Code Projet - ALGAV/Samples/formatTxt/ExemplePatricia.txt");

        System.out.println("Chemin absolu : " + exemple.getAbsolutePath());

        if(exemple.exists()){
            try (java.util.Scanner scanner = new java.util.Scanner(exemple)) {
                System.out.println("main try");
                PatriciaTrie trie = new PatriciaTrie();
                while(scanner.hasNext()){
                    trie.insertSentence(scanner.nextLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } */

        System.out.println("main");
        File patFile = new File("Code Projet - ALGAV/Samples/formatJson/patTrie1.json");

        PatriciaTrie trie = ConvertJson.convertJsonToPatricia(patFile);
        ConvertJson.convertPatriciaToJson(trie);


        for(Map.Entry<String,TrieNode> children : trie.getRoot().getChildren().entrySet()){
            System.out.println("key :"+children.getKey());
        
            System.out.println(children.getValue());
        }
    }
}
