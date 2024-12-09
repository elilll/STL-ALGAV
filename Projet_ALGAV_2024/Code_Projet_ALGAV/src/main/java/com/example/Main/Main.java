package com.example.Main;

import com.example.Trie.Hybrid.HybridTrie;
import com.example.Trie.Patricia.PatriciaTrie;
import com.example.Util.Constant;
import com.example.Util.ConvertJson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
                // Patricia tries
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

    private static void countWords(File trie, String modeTrie){
        switch (modeTrie) {
            case "0":
                // Patricia tries
                PatriciaTrie pat = ConvertJson.convertJsonToPatricia(trie);
                
                try (FileWriter writer = new FileWriter(Constant.NBMOTPRESENTTRIE)) {
                    writer.write("Le nombre de mots dans l'arbre : " + trie.getName() + " est " + pat.countWords());
                    System.out.println("Nombre de mots dans l'arbre : "+ trie.getName() +" ajouté dans " + Constant.NBMOTPRESENTTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "1":
                //TODO
                //Oriane
                break;
            default: throw new IllegalArgumentException("Invalid instruction: "  + modeTrie +"(0-PatriciaTrie; 1-HybridTrie)");
        }
    }

    private static void countNils(File trie, String modeTrie){
        switch (modeTrie) {
            case "0":
                // Patricia tries
                PatriciaTrie pat = ConvertJson.convertJsonToPatricia(trie);
        
                try (FileWriter writer = new FileWriter(Constant.NILSTRIE)) {
                    writer.write("Le nombre de Nils de l'arbre : " + trie.getName() + " est " + pat.countNils());
                    System.out.println("Nombre de Nils dans l'arbre : "+ trie.getName() +" ajouté dans " + Constant.NILSTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        
            case "1":
                //TODO
                //Oriane
                break;
            default: throw new IllegalArgumentException("Invalid instruction " + modeTrie + "(0-PatriciaTrie; 1-HybridTrie)");
        }
    }

    private static void height(File trie, String modeTrie){
        switch (modeTrie) {
            case "0":
                // Patricia tries
                PatriciaTrie pat = ConvertJson.convertJsonToPatricia(trie);
        
                try (FileWriter writer = new FileWriter(Constant.HAUTEURTRIE)) {
                    writer.write("La hauteur de l'arbre : "+ trie.getName() + " est " + pat.height());
                    System.out.println("Hauteur de l'arbre : "+ trie.getName() +" ajoutée dans " + Constant.HAUTEURTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "1":
                //TODO
                //Oriane
                break;
            default: throw new IllegalArgumentException("Invalid instruction " + modeTrie + "(0-PatriciaTrie; 1-HybridTrie)");
        }
    }

    private static void meanHeight(File trie, String modeTrie){
        switch (modeTrie) {
            case "0":
                // Patricia tries
                PatriciaTrie pat = ConvertJson.convertJsonToPatricia(trie);
        
                try (FileWriter writer = new FileWriter(Constant.PROFONDEURTRIE)) {
                    writer.write("La profondeur moyenne de l'arbre : "+ trie.getName() + " est " + pat.MeanHeight());
                    System.out.println("Profondeur moyenne de l'arbre : "+ trie.getName() +" ajoutée dans " + Constant.PROFONDEURTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "1":
                //TODO
                //Oriane
                break;
            default: throw new IllegalArgumentException("Invalid instruction " + modeTrie + "(0-PatriciaTrie; 1-HybridTrie)");
        }
        
    }

    private static void listOfWords(File trie, String modeTrie){
        switch (modeTrie) {
            case "0":
                //Patricia tries
                PatriciaTrie pat = ConvertJson.convertJsonToPatricia(trie);
        
                try (FileWriter writer = new FileWriter(Constant.LISTEMOTTRIE)) {
                    for(String word : pat.getWords()){
                        writer.write(word + "\n");
                    }
                    System.out.println("Mots présent dans l'arbre : "+ trie.getName() +" ajoutés dans " + Constant.LISTEMOTTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "1":
                //TODO
                //Oriane
                break;
            default: throw new IllegalArgumentException("Invalid instruction " + modeTrie + "(0-PatriciaTrie; 1-HybridTrie)");
        }
    }
    
    private static void countPrefix(File trie,String prefix, String modeTrie){
        switch (modeTrie) {
            case "0":
                // Patricia tries
                PatriciaTrie pat = ConvertJson.convertJsonToPatricia(trie);
        
                try (FileWriter writer = new FileWriter(Constant.PREFIXETRIE)) {
                    writer.write("Dans l'arbre " + trie.getName() + ", il y a "+ pat.countWordsPrefix(prefix) +" de mots ayant pour préfixe : " + prefix);
                    System.out.println("Mots présent dans l'arbre : "+ trie.getName() +" ajoutés dans " + Constant.PREFIXETRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {      
                    e.printStackTrace();
                }
                break;
            case "1":
                //TODO
                //Oriane
                break;
            default: throw new IllegalArgumentException("Invalid instruction: "  + modeTrie +"(0-PatriciaTrie; 1-HybridTrie) ");
        }
    }

    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            String[] instruction = line.split(" ");

            switch (instruction[0]) {
                case "inserer": case "Inserer" : case "INSERER":
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
                case "supprimer": case"Supprimer": case "SUPPRIMER":
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
                case "comptermots" : case "compterMots": case "Comptermots": case "CompterMots": case "COMPTERMOTS":
                    if(instruction.length == 3){
                        File file = new File(instruction[2]);
                        if(file.exists()){
                            countWords(file,instruction[1]);
                        } else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid arguments");
                    }
                    break;
                case "compternils": case "compterNils": case "Compternils": case "CompterNils": case "COMPTERNILS":
                    if(instruction.length == 3){
                        File file = new File(instruction[2]);
                        if(file.exists()){
                            countNils(file,instruction[1]);
                        } else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid arguments");
                    }
                    break;
                case "hauteur": case "Hauteur": case "HAUTEUR":
                    if(instruction.length == 3){
                        File file = new File(instruction[2]);
                        if(file.exists()){
                            height(file,instruction[1]);
                        } else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid arguments");
                    }
                    break;
                case "profondeurMoyenne" : case "ProfondeurMoyenne" : case "PROFONDEURMOYENNE" :
                    if(instruction.length == 3){
                        File file = new File(instruction[2]);
                        if(file.exists()){
                            meanHeight(file,instruction[1]);
                        }else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid arguments");
                    }
                    break;
                case "listeMots": case "ListeMots": case "LISTEMOTS":
                    if(instruction.length == 3){
                        File file = new File(instruction[2]);
                        if(file.exists()){
                            listOfWords(file,instruction[1]);
                        }else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid arguments");
                    }
                    break;
                case "prefixe": case "Prefixe": case "PREFIXE":
                    if(instruction.length == 4){
                        File file = new File(instruction[2]);
                        if(file.exists()){
                            countPrefix(file,instruction[3],instruction[1]);
                        } else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[3]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid arguments");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid instruction: "  + instruction[0]); 
            }
        }
    }
    
}
