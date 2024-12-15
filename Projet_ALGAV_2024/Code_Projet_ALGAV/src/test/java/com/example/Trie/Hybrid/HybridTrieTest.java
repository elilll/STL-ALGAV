package com.example.Trie.Hybrid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
        arbre.insert("car", HybridTrieNode.ENDWORD);
        arbre.insert("cat", HybridTrieNode.ENDWORD);
        arbre.insert("cart", HybridTrieNode.ENDWORD);
        arbre.insert("dog", HybridTrieNode.ENDWORD);
        arbre.insert("bat", HybridTrieNode.ENDWORD);

        //Recherche
        assertTrue(arbre.recherche("cat"));
        assertTrue(arbre.recherche("bat"));
        assertFalse(arbre.recherche("bonjour"));

        //Comptage mots
        assertEquals(5, arbre.comptageMots());

        //Liste des mots du trie
        listMotsArbre = arbre.listeMots();
        Collections.sort(listMots);
        Collections.sort(listMotsArbre);
        assertEquals(listMots, listMotsArbre);

        //Comptage Nil
        assertEquals(23, arbre.comptageNil());

        //Hauteur
        assertEquals(4, arbre.hauteur());

        //Profondeur moyenne
        assertEquals(3, arbre.profondeurMoyenne());

        //Prefixe
        assertEquals(3, arbre.prefixe("ca"));
        assertEquals(1, arbre.prefixe("ba"));
        assertEquals(0, arbre.prefixe("e"));

        //Suppression
        arbre.suppression("cat");
        System.out.println("Mots restants dans le trie : " + arbre.listeMots());
        assertFalse(arbre.recherche("cat"));
        assertTrue(arbre.recherche("cart"));
        assertTrue(arbre.recherche("car"));
    }
    
    @Test
    public void testHybridTrieBuildJson(){
        File file = new File("src/test/java/com/example/Samples/formatTxt/exempleSimple.txt");

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
                trie.insert(currentLine, HybridTrieNode.ENDWORD);
            }

            System.out.println("File : " + contient.size() + "mots | Hybrid Trie : " + trie.comptageMots()+" mots");
            assertEquals(contient.size(), trie.comptageMots(),"Le fichier "+ file.getName() +" ne contient pas le même nombre de mot : " + contient.size() + " que l'hybrid trie : " + trie.comptageMots());

            //Affiche le format Json obtenu
            System.out.println("Représentation JSON de l'Hybrid Trie :");
            System.out.println(ConvertJson.formatJsonHybrid(trie.getRoot()));
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
}
