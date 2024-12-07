package src.main.java.com.example.Main;

import src.main.java.com.example.Main.Trie.Hybrid.HybridTrie;
import src.main.java.com.example.Main.Trie.Patricia.PatriciaTrie;
import src.main.java.com.example.Main.Util.Constant;
import src.main.java.com.example.Main.Util.ConvertJson;

import java.io.File;
import java.util.Scanner;

public class Main {

    private static void insert(File words, String modeTrie){
        switch (modeTrie) {
            case "0":
                // Patricia tries
                try (Scanner scan = new Scanner(words)) {
                    PatriciaTrie trie = new PatriciaTrie();
                    
                    while(scan.hasNext()){
                        trie.insertWord(scan.nextLine());
                    }
                    ConvertJson.convertPatriciaToJson(trie);
                }catch(Exception e){
                    e.printStackTrace();
                }
            
                break;
            case "1":
                // Hybrid tries
                try (Scanner scan = new Scanner(words)) {
                    int i = 0;
                    HybridTrie trie = new HybridTrie();
                    while(scan.hasNext()){
                        trie.insert(scan.nextLine(), i++);
                    }
                    ConvertJson.convertHybridToJson(trie);
                }catch(Exception e){
                    e.printStackTrace();
                }
            
                break;
            default : throw new IllegalArgumentException("Invalid instruction: "  + modeTrie +"(0-PatriciaTrie; 1-HybridTrie) ");
                
        }
    }

    private static void delete(File words, String modeTrie){
        switch (modeTrie) {
            case "0":
                try (Scanner scan = new Scanner(words)) {
                    PatriciaTrie trie = ConvertJson.convertJsonToPatricia(new File(Constant.PATJSONFILE));
                    
                    while(scan.hasNext()){
                        trie.deleteWord(scan.nextLine());
                    }
                    
                    ConvertJson.convertPatriciaToJson(trie);
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
            case "1" :
                //TODO
                //Oriane
                break;
            
            default : throw new IllegalArgumentException("Invalid instruction: "  + modeTrie +"(0-PatriciaTrie; 1-HybridTrie) ");
                
        }
        
    }

    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            String[] instruction = line.split(" ");

            switch (instruction[0]) {
                case "inserer": case "Inserer": case "INSERER":
                    if(instruction.length == 3){
                        File file = new File(instruction[2]);
                        if(file.exists()){
                            insert(file,instruction[1]);
                        }else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid arguments");
                    }
                
                break;

                case "supprimer" : case "Supprimer": case "SUPPRIMER":
                    if(instruction.length == 3){
                        File file = new File(instruction[2]);
                        if(file.exists()){
                            delete(file,instruction[1]);
                        }else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid arguments");
                    }
                break;

                default : throw new IllegalArgumentException("Invalid instruction: "  + instruction[0]); 
            }
        }
    }
    
}
