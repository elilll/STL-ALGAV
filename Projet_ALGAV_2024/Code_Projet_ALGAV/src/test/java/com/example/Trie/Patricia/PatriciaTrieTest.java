package com.example.Trie.Patricia;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

@SuppressWarnings("CallToPrintStackTrace")

public class PatriciaTrieTest {
    private static final File folder = new File("src/test/java/com/example/Samples/Shakespeare/");
    @Test
    public void testBuildPatriciaTrie() {
        assertNotNull(folder,"Aucun répertoire trouvé");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        // Vérifie que des fichiers ont été trouvés
        assertNotNull(listOfFiles,"Aucun fichier trouvé");

        File outputFile = new File("insertion_times.txt");
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
        // Parcourt tous les fichiers .txt
        for (File file : Objects.requireNonNull(listOfFiles)) {
            System.out.println("*****************************************************************************************");
            System.out.println("File : " + file.getAbsolutePath());

            try (Scanner scan = new Scanner(file)) {

                

                Set<String> contient = new HashSet<>();
                PatriciaTrie trie = new PatriciaTrie();
                double sum = 0.0;
                while (scan.hasNextLine()) {
                    String currentLine = scan.nextLine();

                    double time = trie.insertWord(currentLine);

                    writer.write(currentLine.length() + " " + time);
                   //writer.write(trie.nbnode + " " + time);
                    writer.newLine();
                    // nbOfWord++;
                
                    //trie.nbnode=0;
                    sum+=time;
                    contient.add(currentLine);
                }

                //writer.write("Moyenne des temps d'insertion : " + sum/contient.size());
                writer.flush();

                System.out.println("File : " + contient.size() + "mots | Arbre Patricia : " + trie.countWords()+" mots");
                assertEquals(contient.size(), trie.countWords(),"Le fichier "+ file.getName() +" ne contient pas le même nombre de mot : " + contient.size() + " que l'arbre patricia : " + trie.countWords());

                List<String> getWordsTrie = trie.getWords();

                int countWords = getWordsTrie.size();

                while(!contient.isEmpty()) {
                    if(contient.contains(getWordsTrie.get(countWords - 1))){
                        contient.remove(getWordsTrie.get(countWords - 1));
                        countWords--;
                    }else {
                        assertTrue(contient.contains(getWordsTrie.get(countWords - 1)), "Ce mots : "+ getWordsTrie.get(countWords - 1)+"n'est pas dans le texte original");
                    }
                }

                System.out.println("L'arbre patricia créer à partir du fichier "+ file.getName() + " contient bien tous les mots de ce fichier\n");
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        }catch(IOException e){
            e.getStackTrace();
        }
    }

    @Test
    public void testDeleteWordsOfPatriciaTrie() {
        assertNotNull(folder,"Aucun répertoire trouvé");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        // Vérifie que des fichiers ont été trouvés
        assertNotNull(listOfFiles,"Aucun fichier trouvé");

        File outputFile = new File("delete_times.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
        // Parcourt tous les fichiers .txt
        for (File file : Objects.requireNonNull(listOfFiles)) {
            System.out.println("*****************************************************************************************");
            System.out.println("File : " + file.getAbsolutePath());
            try (Scanner scan = new Scanner(file)) {
                Set<String> contient = new HashSet<>();
                PatriciaTrie trie = new PatriciaTrie();
        
                while (scan.hasNextLine()) {
                    String currentLine = scan.nextLine();

                    contient.add(currentLine);

                    trie.insertWord(currentLine);
                }

                Iterator<String> it = contient.iterator();

                while(it.hasNext()) {
                    String word = it.next();
                    double startTime = System.nanoTime();
                    trie.deleteWord(word);
                    double endTime = System.nanoTime();
                    it.remove();
                    writer.write(word.length() + " " + (endTime - startTime)/1000.0);
                    writer.newLine();
                }

                assertEquals(trie.countWords(),0,"L'arbre ne devrait pas contenir de mots");

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
    public void testFusionPatriciaTrie(){


    }
}
