package com.example.Trie.Hybrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        int i = 0;

        //Insertion
        arbre.insert("car", i++);
        arbre.insert("cat", i++);
        arbre.insert("cart", i++);
        arbre.insert("dog", i++);
        arbre.insert("bat", i++);

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
        assertFalse(arbre.recherche("cat"));
        assertTrue(arbre.recherche("cart"));
    }
    
}
