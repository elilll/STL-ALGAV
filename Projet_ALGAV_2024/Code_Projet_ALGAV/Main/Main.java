package Main;

import Trie.Hybrid.HybridTrie;
import Trie.Patricia.PatriciaTrie;
import Util.Constant;
import Util.ConvertJson;
import java.io.File;
import java.util.Scanner;

public class Main {

    private static void insert(File words, String modeTrie){
        switch (modeTrie) {
            case "0" -> {
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
            }

            case "1" -> {
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
            }

            default -> throw new IllegalArgumentException("Invalid instruction: "  + modeTrie +"(0-PatriciaTrie; 1-HybridTrie) ");
        }
    }

    private static void delete(File words, String modeTrie){
        switch (modeTrie) {
            case "0" -> {
                try (Scanner scan = new Scanner(words)) {
                    PatriciaTrie trie = ConvertJson.convertJsonToPatricia(new File(Constant.PATJSONFILE));
                    
                    while(scan.hasNext()){
                        trie.deleteWord(scan.nextLine());
                    }
                    
                    ConvertJson.convertPatriciaToJson(trie);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }

            case "1" -> {
                //TODO
                //Oriane
            }
            
            default -> throw new IllegalArgumentException("Invalid instruction: "  + modeTrie +"(0-PatriciaTrie; 1-HybridTrie) ");
        }
        
    }

    
    public static void main(String[] args) {
        /* 
        File file1 = new File("Samples/formatTxt/SamplesPat1.txt");
        File file2 = new File("Samples/formatTxt/SamplesPat2.txt");
        if(file1.exists() && file2.exists()){
            insert(file1,"0");
            delete(file2, "0");
        }else{
            throw new IllegalArgumentException("Invalid files path: " + file1 + " and " + file2);
        }*/
    

        try (Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            String[] instruction = line.split(" ");

            switch (instruction[0]) {
                case "inserer", "Inserer", "INSERER" -> {
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
                }

                case "supprimer", "Supprimer", "SUPPRIMER" -> {
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
                }

                default -> throw new IllegalArgumentException("Invalid instruction: "  + instruction[0]); 
            }
        }
    }
    
}
