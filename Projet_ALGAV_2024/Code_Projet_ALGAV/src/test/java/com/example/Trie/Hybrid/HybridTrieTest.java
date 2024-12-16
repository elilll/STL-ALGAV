package com.example.Trie.Hybrid;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.example.Util.ConvertJson;

@SuppressWarnings("CallToPrintStackTrace")

public class HybridTrieTest {
    private static HybridTrie arbre;
    private static List<String> listMots;
    private static List<String> listMotsArbre;

    private static final File folder = new File("src/test/java/com/example/Samples/Shakespeare/");

    @BeforeAll
    public static void initialisationHybridTrie(){
        arbre = new HybridTrie();
        listMots = new ArrayList<>();
        listMotsArbre = new ArrayList<>();
        listMots.add("car");
        listMots.add("cat");
        listMots.add("cart");
        listMots.add("dog");
        listMots.add("bat");
    }

    @Test
    public void testHybridTrieFunctions(){
        //Insertion
        arbre.insert("car", HybridTrie.NON_BALANCED);
        arbre.insert("cat", HybridTrie.NON_BALANCED);
        arbre.insert("cart", HybridTrie.NON_BALANCED);
        arbre.insert("dog", HybridTrie.NON_BALANCED);
        arbre.insert("bat", HybridTrie.NON_BALANCED);

        //Recherche
        assertTrue(arbre.recherche("cat"));
        assertTrue(arbre.recherche("bat"));
        assertFalse(arbre.recherche("bonjour"));

        //Comptage mots
        assertEquals(5, arbre.comptageMots());
        assertNotEquals(7, arbre.comptageMots());
        assertNotEquals(3, arbre.comptageMots());

        //Liste des mots du trie
        listMotsArbre = arbre.listeMots();
        Collections.sort(listMots);
        Collections.sort(listMotsArbre);
        assertEquals(listMots, listMotsArbre);

        //Comptage Nil
        assertEquals(23, arbre.comptageNil());

        //Hauteur
        assertEquals(5, arbre.hauteur());

        //Profondeur moyenne
        assertEquals(3, arbre.profondeurMoyenne());

        //Prefixe
        assertEquals(3, arbre.prefixe("ca"));
        assertEquals(1, arbre.prefixe("ba"));
        assertEquals(0, arbre.prefixe("e"));

        //Suppression
        arbre.suppression("car");
        System.out.println("Mots restants dans le trie : " + arbre.listeMots());
        assertFalse(arbre.recherche("car"));
        assertTrue(arbre.recherche("cart"));
        assertTrue(arbre.recherche("cat"));
    }
    
    @Test
    public void testHybridTrieBuildJson(){
        File file = new File("src/test/java/com/example/Samples/formatTxt/lettres.txt");

        // Vérifie que des fichiers ont été trouvés
        assertNotNull(file,"Aucun fichier trouvé");

        // Parcourt le fichier exempleSimple.txt
        System.out.println("*****************************************************************************************");
        System.out.println("File : " + file.getAbsolutePath());

        
        try (Scanner scan = new Scanner(file)) {
            Set<String> contient = new HashSet<>();
            HybridTrie trie = new HybridTrie();
    
            while (scan.hasNextLine()) {
                String currentLine = scan.nextLine();
                contient.add(currentLine);


                trie.insert(currentLine, HybridTrie.NON_BALANCED);
            }

            System.out.println("File : " + contient.size() + "mots | Hybrid Trie : " + trie.comptageMots()+" mots");
            assertEquals(contient.size(), trie.comptageMots(),"Le fichier "+ file.getName() +" ne contient pas le même nombre de mot : " + contient.size() + " que l'hybrid trie : " + trie.comptageMots());

            //Affiche le format Json obtenu
            // System.out.println("Représentation JSON de l'Hybrid Trie :");
            // System.out.println(ConvertJson.formatJsonHybrid(trie.getRoot()));

            // Écrit dans trie.json
            ConvertJson.convertHybridToJson(trie); 

            List<String> getWordsTrie = trie.listeMots();
            int countWords = getWordsTrie.size();

            while(!contient.isEmpty()) {
                if(contient.contains(getWordsTrie.get(countWords - 1))){
                    contient.remove(getWordsTrie.get(countWords - 1));
                    countWords--;
                }else {
                    assertTrue(contient.contains(getWordsTrie.get(countWords - 1)), "Ce mots : "+ getWordsTrie.get(countWords - 1)+"n'est pas dans le texte original");
                }
            }

            System.out.println("L'hybrid trie créé à partir du fichier "+ file.getName() + " contient bien tous les mots de ce fichier\n");
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    @Test
    public void testInsertHybridTrie() {
        assertNotNull(folder,"Aucun répertoire trouvé");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        // Vérifie que des fichiers ont été trouvés
        assertNotNull(listOfFiles,"Aucun fichier trouvé");

        File insertTime = new File("insertion_times_hyb.txt");
        File insertGlobalTime = new File("insertion_times_global_hyb.txt");
        File outputFile = new File("insertion_times_sans_occurrences_hyb.txt");
        
        try (BufferedWriter insertWriter = new BufferedWriter(new FileWriter(insertTime));
             BufferedWriter insertGlobalWriter = new BufferedWriter(new FileWriter(insertGlobalTime));
             BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            // Parcourt tous les fichiers .txt
            for (File file : Objects.requireNonNull(listOfFiles)) {
                System.out.println("*****************************************************************************************");
                System.out.println("File : " + file.getAbsolutePath());

                try (Scanner scan = new Scanner(file)) {
                    Set<String> contient = new HashSet<>();
                    HybridTrie trie = new HybridTrie();

                    double sum = 0.0;
                    int nbOfWord = 0;

                    while (scan.hasNextLine()) {
                        String currentLine = scan.nextLine();

                        double startTime = System.nanoTime();
                        trie.insert(currentLine, HybridTrie.NON_BALANCED);
                        double endTime = System.nanoTime();

                        double time = (endTime - startTime);
                    
                        sum += time;
                        insertWriter.write(currentLine.length() + " " + time/100000.0);
                        //writer.write(trie.nbnode + " " + time);
                        insertWriter.newLine();
                        nbOfWord++;

                        if (!contient.contains(currentLine)){
                            writer.write(currentLine.length() + " " + time/100000.0);
                            writer.newLine();
                        }
                
                        //trie.nbnode=0;
                        contient.add(currentLine);
                    }

                    insertGlobalWriter.write( nbOfWord + " " + sum/100000.0);
                    insertGlobalWriter.newLine();

                    insertWriter.flush();
                    insertGlobalWriter.flush();

                    System.out.println("File : " + contient.size() + "mots | Hybrid Trie : " + trie.comptageMots()+" mots");
                    assertEquals(contient.size(), trie.comptageMots(),"Le fichier "+ file.getName() +" ne contient pas le même nombre de mot : " + contient.size() + " que l'hybrid trie : " + trie.comptageMots());

                    //Affiche le format Json obtenu
                    // System.out.println("Représentation JSON de l'Hybrid Trie :");
                    // System.out.println(ConvertJson.formatJsonHybrid(trie.getRoot()));
                    // Écrit dans trie.json
                    ConvertJson.convertHybridToJson(trie); 

                    List<String> getWordsTrie = trie.listeMots();
                    int countWords = getWordsTrie.size();

                    while(!contient.isEmpty()) {
                        if(contient.contains(getWordsTrie.get(countWords - 1))){
                            contient.remove(getWordsTrie.get(countWords - 1));
                            countWords--;
                        }else {
                            assertTrue(contient.contains(getWordsTrie.get(countWords - 1)), "Ce mots : "+ getWordsTrie.get(countWords - 1)+"n'est pas dans le texte original");
                        }
                    }

                    System.out.println("L'hybrid trie créé à partir du fichier "+ file.getName() + " contient bien tous les mots de ce fichier\n");
    
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    @Test
    public void testDeleteWordsOfHybridTrie() {
        assertNotNull(folder,"Aucun répertoire trouvé");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        // Vérifie que des fichiers ont été trouvés
        assertNotNull(listOfFiles,"Aucun fichier trouvé");

        File outputFile = new File("delete_times_hyb.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
        // Parcourt tous les fichiers .txt
        for (File file : Objects.requireNonNull(listOfFiles)) {
            System.out.println("*****************************************************************************************");
            System.out.println("File : " + file.getAbsolutePath());
            try (Scanner scan = new Scanner(file)) {
                Set<String> contient = new HashSet<>();
                HybridTrie trie = new HybridTrie();
        
                while (scan.hasNextLine()) {
                    String currentLine = scan.nextLine();

                    contient.add(currentLine);

                    trie.insert(currentLine, HybridTrie.NON_BALANCED);
                }

                Iterator<String> it = contient.iterator();

                while(it.hasNext()) {
                    String word = it.next();
                    double startTime = System.nanoTime();
                    trie.suppression(word);
                    double endTime = System.nanoTime();
                    it.remove();
                    writer.write(word.length() + " " + (endTime - startTime)/100000.0);
                    writer.newLine();
                }

                writer.flush();

                assertEquals(trie.comptageMots(),0,"L'arbre ne devrait pas contenir de mots");

                System.out.println("La méthode de suppression est bien fonctionnelle, tous les mots de l'arbre ont été supprimés");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    @Test
    public void testSearchWordsOfHybridTrie() {
        assertNotNull(folder,"Aucun répertoire trouvé");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        // Vérifie que des fichiers ont été trouvés
        assertNotNull(listOfFiles,"Aucun fichier trouvé");

        File outputFile = new File("search_times_hyb.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
        // Parcourt tous les fichiers .txt
        for (File file : Objects.requireNonNull(listOfFiles)) {
            System.out.println("*****************************************************************************************");
            System.out.println("File : " + file.getAbsolutePath());
            try (Scanner scan = new Scanner(file)) {
                Set<String> contient = new HashSet<>();
                HybridTrie trie = new HybridTrie();
        
                while (scan.hasNextLine()) {
                    String currentLine = scan.nextLine();

                    contient.add(currentLine);

                    trie.insert(currentLine, HybridTrie.NON_BALANCED);
                }

                Iterator<String> it = contient.iterator();

                while(it.hasNext()) {
                    String word = it.next();
                    double startTime = System.nanoTime();
                    boolean found = trie.recherche(word);
                    double endTime = System.nanoTime();
                    it.remove();
                    writer.write(word.length() + " " + (endTime - startTime)/100000.0);
                    writer.newLine();

                    assertTrue(found, "Le mot est sensé être dans l'arbre patricia");
                }

                writer.flush();

                System.out.println("La méthode de recherche est bien fonctionnelle, tous les mots de l'arbre ont été trouvés");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    @Test
    public void testCountWordsOfHybridTrie() {
        assertNotNull(folder,"Aucun répertoire trouvé");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        // Vérifie que des fichiers ont été trouvés
        assertNotNull(listOfFiles,"Aucun fichier trouvé");

        File outputFile = new File("countWords_times_hyb.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
        // Parcourt tous les fichiers .txt
        for (File file : Objects.requireNonNull(listOfFiles)) {
            System.out.println("*****************************************************************************************");
            System.out.println("File : " + file.getAbsolutePath());
            try (Scanner scan = new Scanner(file)) {
                Set<String> contient = new HashSet<>();
                HybridTrie trie = new HybridTrie();
        
                while (scan.hasNextLine()) {
                    String currentLine = scan.nextLine();

                    contient.add(currentLine);

                    trie.insert(currentLine, HybridTrie.NON_BALANCED);
                }

                double startTime = System.nanoTime();
                int countWord = trie.comptageMots() ;
                double endTime = System.nanoTime();

                assertEquals(countWord,contient.size(), "Ne contient pas le même nombre de mots");
                System.out.println(" Nb de mots : " + countWord);

                writer.write(countWord + " " + (endTime - startTime)/100000.0);
                writer.newLine();

                System.out.println("La méthode de recherche est bien fonctionnelle, tous les mots de l'arbre ont été trouvés");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }catch(IOException e){
            e.getStackTrace();
        }
    }
}
