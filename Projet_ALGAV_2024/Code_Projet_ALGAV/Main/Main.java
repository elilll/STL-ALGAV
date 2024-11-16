package Main;

import java.io.File;
import java.util.Scanner;

import Trie.Patricia.PatriciaTrie;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            String[] instruction = line.split(" ");

            switch (instruction[0]) {
                case "inserer": case "Inserer" : case "INSERER":
                    if(instruction.length == 3){
                        if(instruction[1].equals("0")){
                            System.out.println("file: "+ instruction[2]);
                            File file = new File(instruction[2]);
                            StringBuilder sentence = new StringBuilder();
                            if(file.exists()){
                                try (Scanner scan = new Scanner(file)) {
                                    System.out.println("exists : "+ file.getAbsolutePath());
                                    PatriciaTrie trie = new PatriciaTrie();

                                    while(scan.hasNext()){
                                        sentence.append(scan.nextLine()).append(" ");
                                    }
                                    trie.insertSentence(sentence.toString());
                                }catch(Exception e){
                                    e.printStackTrace();
                                }
                            }else{
                                throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                            }
                        }else if(instruction[1].equals("1")){
   
                        }else{
                            throw new IllegalArgumentException("Invalid instruction: "  + instruction[1] +"(0-PatriciaTrie; 1-HybridTrie) "); 
                        }
                    } 
                    break;
                default:
                    throw new IllegalArgumentException("Invalid instruction: "  + instruction[0]); 
            }
        }
    }
}
