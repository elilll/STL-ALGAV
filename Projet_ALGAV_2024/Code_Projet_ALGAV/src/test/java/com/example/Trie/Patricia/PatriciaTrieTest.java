package com.example.Trie.Patricia;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.io.File;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;


public class PatriciaTrieTest {
    @Test
    public void testBuildPatriciaTreeForAllFiles() {

        File folder = new File("src/test/java/com/example/Samples/Shakespeare/");

        assertNotNull(folder,"Aucun répertoire trouvé");
        System.out.println("Peut lire le dossier : " + folder.canRead());
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        // Vérifie que des fichiers ont été trouvés
        assertNotNull(listOfFiles,"Aucun fichier trouvé");

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
    }

    @Test
    public void testDeleteWordsOfPatriciaTree() {

    }
}
