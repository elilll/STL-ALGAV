package com.example.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.example.Trie.Hybrid.HybridTrie;
import com.example.Trie.Patricia.PatriciaTrie;
import com.example.Util.Constant;
import com.example.Util.ConvertJson;

@SuppressWarnings("CallToPrintStackTrace")
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
                    HybridTrie trie = new HybridTrie();
                    while(scan.hasNext()){
                        trie.insert(scan.nextLine(), HybridTrie.NON_BALANCED);
                    }
                    ConvertJson.convertHybridToJson(trie);
                }catch(Exception e){
                    e.printStackTrace();
                }
            
                break;
            default : throw new IllegalArgumentException("Invalid instruction: "  + modeTrie +"(0-PatriciaTrie; 1-HybridTrie) ");
                
        }
    }

    //Version pour les Tries hybrides
    //balance == "true" : insertion avec rééquilibrage
    //balance == "false" : insertion sans rééquilibrage
    private static void insert(File words, String modeTrie, String balance){
        switch (modeTrie) {
            case "0":
                throw new IllegalArgumentException("Invalid instruction (0). Please try with hybrid tries (1).");
            case "1":
                // Hybrid tries
                switch(balance){
                    case "true":
                        try (Scanner scan = new Scanner(words)) {
                            HybridTrie trie = new HybridTrie();
                            while(scan.hasNext()){
                                trie.insert(scan.nextLine(), HybridTrie.BALANCED);
                            }
                            ConvertJson.convertHybridToJson(trie);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    case "false":
                        try (Scanner scan = new Scanner(words)) {
                            HybridTrie trie = new HybridTrie();
                            while(scan.hasNext()){
                                trie.insert(scan.nextLine(), HybridTrie.NON_BALANCED);
                            }
                            ConvertJson.convertHybridToJson(trie);
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        break;
                    default: 
                        throw new IllegalArgumentException("Invalid instruction : 4th argument isn't true or false. Please try with valid arguments, or none.");
                }
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
                // Hybrid Trie
                try (Scanner scan = new Scanner(words)) {
                    HybridTrie trie = ConvertJson.convertJsonToHybridTrie(new File(Constant.HYBRIDJSONFILE));
        
                    while(scan.hasNext()){
                        trie.suppression(scan.nextLine());
                    }
        
                    ConvertJson.convertHybridToJson(trie);
                }catch(Exception e){
                    e.printStackTrace();
                }
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
            // Hybrid Trie
                HybridTrie hyb = ConvertJson.convertJsonToHybridTrie(trie);
                    
                try (FileWriter writer = new FileWriter(Constant.NBMOTPRESENTTRIE)) {
                    writer.write("Le nombre de mots dans l'arbre : " + trie.getName() + " est " + hyb.comptageMots());
                    System.out.println("Nombre de mots dans l'arbre : "+ trie.getName() +" ajouté dans " + Constant.NBMOTPRESENTTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                // Patricia tries
                HybridTrie hyb = ConvertJson.convertJsonToHybridTrie(trie);
        
                try (FileWriter writer = new FileWriter(Constant.NILSTRIE)) {
                    writer.write("Le nombre de Nils de l'arbre : " + trie.getName() + " est " + hyb.comptageNil());
                    System.out.println("Nombre de Nils dans l'arbre : "+ trie.getName() +" ajouté dans " + Constant.NILSTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                // Hybrid Trie
                HybridTrie hyb = ConvertJson.convertJsonToHybridTrie(trie);
        
                try (FileWriter writer = new FileWriter(Constant.HAUTEURTRIE)) {
                    writer.write("La hauteur de l'arbre : "+ trie.getName() + " est " + hyb.hauteur());
                    System.out.println("Hauteur de l'arbre : "+ trie.getName() +" ajoutée dans " + Constant.HAUTEURTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                // Hybrid Trie
                HybridTrie hyb = ConvertJson.convertJsonToHybridTrie(trie);
        
                try (FileWriter writer = new FileWriter(Constant.PROFONDEURTRIE)) {
                    writer.write("La profondeur moyenne de l'arbre : "+ trie.getName() + " est " + hyb.profondeurMoyenne());
                    System.out.println("Profondeur moyenne de l'arbre : "+ trie.getName() +" ajoutée dans " + Constant.PROFONDEURTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                // Hybrid Trie
                HybridTrie hyb = ConvertJson.convertJsonToHybridTrie(trie);
        
                try (FileWriter writer = new FileWriter(Constant.LISTEMOTTRIE)) {
                    for(String word : hyb.listeMots()){
                        writer.write(word + "\n");
                    }
                    System.out.println("Mots présent dans l'arbre : "+ trie.getName() +" ajoutés dans " + Constant.LISTEMOTTRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                // Hybrid Trie
                HybridTrie hyb = ConvertJson.convertJsonToHybridTrie(trie);
        
                try (FileWriter writer = new FileWriter(Constant.PREFIXETRIE)) {
                    writer.write("Dans l'arbre " + trie.getName() + ", il y a "+ hyb.prefixe(prefix) +" de mots ayant pour préfixe : " + prefix);
                    System.out.println("Mots présent dans l'arbre : "+ trie.getName() +" ajoutés dans " + Constant.PREFIXETRIE);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {      
                    e.printStackTrace();
                }
                break;
            default: throw new IllegalArgumentException("Invalid instruction: "  + modeTrie +"(0-PatriciaTrie; 1-HybridTrie) ");
        }
    }

    private static void fusionPat(File pat1, File pat2){
        PatriciaTrie pat1Trie = ConvertJson.convertJsonToPatricia(pat1);
        PatriciaTrie pat2Trie = ConvertJson.convertJsonToPatricia(pat2);

        pat1Trie.fusion(pat2Trie);

        ConvertJson.convertPatriciaToJson(pat1Trie);
        System.out.println("Arbre fusionné ajouté dans " + Constant.PATJSONFILE);
    }

    
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String line = scanner.nextLine();
            String[] instruction = line.split(" ");

            switch (instruction[0]) {
                case "inserer": case "Inserer" : case "INSERER":
                switch (instruction.length) {
                    case 3: //3 arguments --> insertion de base
                        {
                            File file = new File(instruction[2]);
                            if(file.exists()){
                                insert(file,instruction[1]);
                            }else{
                                throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                            }   break;
                        }
                    case 4: //4 arguments --> pour indiquer si on veut rééquilibrer le Trie hybride à chaque insertion
                        {
                            File file = new File(instruction[2]);
                            if(file.exists()){
                                insert(file,instruction[1], instruction[4]);
                            }else{
                                throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                            } 
                        }
                    default:
                        throw new IllegalArgumentException("Invalid number of arguments");
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
                        throw new IllegalArgumentException("Invalid number of arguments");
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
                        throw new IllegalArgumentException("Invalid number of arguments");
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
                        throw new IllegalArgumentException("Invalid number of arguments");
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
                        throw new IllegalArgumentException("Invalid number of arguments");
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
                        throw new IllegalArgumentException("Invalid number of arguments");
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
                        throw new IllegalArgumentException("Invalid number of arguments");
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
                        throw new IllegalArgumentException("Invalid number of arguments");
                    }
                    break;
                case "fusionPat": case "FusionPat": case "FUSIONPAT":
                    if(instruction.length == 3){
                        File file1 = new File(instruction[1]);
                        File file2 = new File(instruction[2]);
                        if(file1.exists()  && file2.exists()){
                            fusionPat(file1,file2);
                        }else{
                            throw new IllegalArgumentException("Invalid file path: " + instruction[2]);
                        }
                    }else{
                        throw new IllegalArgumentException("Invalid number of arguments");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid instruction: "  + instruction[0]); 
            }
        }
    }

    
}
