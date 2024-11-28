package Trie.Hybrid;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HybridTrie {
    //Attribut
    private HybridTrieNode root; // Racine du trie hybride

    //Constructeur
    public HybridTrie() {
        this.root = null; // Initialisation du trie vide
    }

    //Méthodes

    /** 
     * Fonction d'insertion d'un mot avec indice 
     * @param word : le mot à insérer dans notre trie
     * @param index : l'indice associé au mot qu'on veut entrer (pour le retrouver aisément plus tard et marquer
     * un noeud comme fin de mot)
    */
    public void insert(String word, int index) {
        /* Vérifie si le mot entré ne comporte que des caratères du code ASCII < 128 charatères */
        byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }

        root = insertRec(root, word, 0, index);
    }

    /**
     * Fonction récursive d'insertion ( utilisée dans insert() )
     * @param node : le noeud courant sur lequel on doit naviguer
     * @param word : le mot à insérer dans notre trie
     * @param charIndex : le numéro du caractère de word sur lequel on travaille (lettre par lettre)
     * @param wordIndex : l'indice associé au mot qu'on veut entrer
     * @return : on retourne le noeud courant modifié (pour correctement mettre à jour la structure du trie)
     */
    private HybridTrieNode insertRec(HybridTrieNode node, String word, int charIndex, int wordIndex) {
        char currentChar = word.charAt(charIndex);

        if (node == null) {
            node = new HybridTrieNode(currentChar);
        }

        if (currentChar < node.getCar()) {
            node.getPointeurs()[HybridTrieNode.INF] = insertRec(node.getPointeurs()[HybridTrieNode.INF], word, charIndex, wordIndex); // Inf
        } else if (currentChar > node.getCar()) {
            node.getPointeurs()[HybridTrieNode.SUP] = insertRec(node.getPointeurs()[HybridTrieNode.SUP], word, charIndex, wordIndex); // Sup
        } else { // currentChar == node.getCar()
            if (charIndex + 1 == word.length()) { // Si on est bien arrivé à la fin de la lecture de notre mot ...
                node.setVal(wordIndex); // ... on marque la fin du mot avec son indice
            } else { // Sinon : on continue de parcourir l'arbre dans la branche Eq
                node.getPointeurs()[HybridTrieNode.EQ] = insertRec(node.getPointeurs()[HybridTrieNode.EQ], word, charIndex + 1, wordIndex); // Eq
            }
        }

        return node;
    }

    /**
     * Fonction pour rechercher un mot dans le trie
     * @param word : le mot à rechercher
     * @return : true si le mot est présent dans le trie, false sinon
     */
    public boolean recherche(String word){
        return rechercheRec(root, word, 0);
    }

    /**
     * Fonction de recherche récursive ( utilisée dans recherche() )
     * @param node : le noeud courant sur lequel on travaille
     * @param word : le mot à rechercher
     * @param charIndex : le numéro du caractère de word sur lequel on travaille (lettre par lettre)
     * @return
     */
    private boolean rechercheRec(HybridTrieNode node, String word, int charIndex){
        char currentChar = word.charAt(charIndex);

        if (node == null) {
            return false;
        }

        if (currentChar < node.getCar()) {
            return rechercheRec(node.getPointeurs()[HybridTrieNode.INF], word, charIndex);  // Inf
        } else if (currentChar > node.getCar()) {
            return rechercheRec(node.getPointeurs()[HybridTrieNode.SUP], word, charIndex); // Sup
        } else { // currentChar == node.getCar()
            if (charIndex + 1 == word.length()) {
                return node.getVal() != -1; // On vérifie que le noeud marque bien la fin valide d'un mot dans le trie
            } else {
                return rechercheRec(node.getPointeurs()[HybridTrieNode.EQ], word, charIndex + 1); // Eq
            }
        }
    }

    /**
     * Fonction qui compte le nombre de mots présents dans le dictionnaire
     * @return : le nombre de mots dans le dictionnaire
     */
    public int comptageMots(){
        return comptageMotsRec(root);
    }

    /**
     * Fonction de comptage récursive ( utilisée dans comptageMots() )
     * Rappel : si un noeud a pour valeur -1, c'est que ce n'est pas la terminaison d'un mot du trie
     * @param node : le noeud courant sur lequel on travaille
     * @return : le nombre de mots détectés
     */
    private int comptageMotsRec(HybridTrieNode node){
        if (node == null){
            return 0; // Pas de mot si le noeud est vide
        }

        int count = 0;
        if (node.getVal() != -1) {
            count++; // Si val est différent de -1, c'est qu'un mot a été ajouté dans le trie
        }

        // On parcourt récursivement les trois sous-arbres du noeud courant
        count += comptageMotsRec(node.getPointeurs()[HybridTrieNode.INF]);
        count += comptageMotsRec(node.getPointeurs()[HybridTrieNode.EQ]);
        count += comptageMotsRec(node.getPointeurs()[HybridTrieNode.SUP]);

        return count;
    }

    /**
     * Fonction qui liste les mots du dictionnaire dans l’ordre alphabétique
     * @return : la liste des mots du trie
     */
    public List<String> listeMots(){
        // On utilise un StringBuilder pour éviter les copies inutiles de String lors des modifications
        StringBuilder prefixe = new StringBuilder();
        return listeMotsRec(root, prefixe);
    }

    /**
     * Fonction récursive pour créer la liste des mots du trie ( utilisée dans listeMots() )
     * Principe : on a un préfixe qui permet de garder en mémoire à chaque appel récursif les lettres parcourues (et ainsi construire le mot petit à petit).
     * Quand on arrive sur la fin d'un mot (val != -1), le préfixe contient le mot dans son entierté, il suffit alors de l'ajouter dans notre liste.
     * @return : la liste contenant les mots qu'on a trouvé jusqu'ici
     */
    private List<String> listeMotsRec(HybridTrieNode node, StringBuilder prefixe){
        List<String> liste = new ArrayList<>();

        if (node == null){
            return liste; // Si le noeud est vide, on renvoie la liste vide
        }

        // Si le noeud marque la fin d'un mot dans le trie, on l'ajoute dans la liste
        if (node.getVal() != -1){
            liste.add(prefixe.append(node.getCar()).toString());
        }

        // On parcourt récursivement les trois sous-arbres du noeud courant
        liste.addAll(listeMotsRec(node.getPointeurs()[HybridTrieNode.INF], prefixe));
        liste.addAll(listeMotsRec(node.getPointeurs()[HybridTrieNode.EQ], prefixe));
        liste.addAll(listeMotsRec(node.getPointeurs()[HybridTrieNode.SUP], prefixe));

        // On retire le dernier caractère ajouté pour revenir à l'état précédent 
        // (Exemple : une fois qu'on a terminé d'explorer "cat" dans l'arbre, on doit restaurer le prefixe pour pouvoir explorer "bat")
        prefixe.deleteCharAt(prefixe.length() - 1);

        return liste;
    }

    /**
     * Fonction qui compte les pointeurs vers Nil 
     * NB : pour éviter de prendre trop de places en mémoire, on ne crée des noeuds qu'en cas de besoin. 
     * Ainsi, nous n'avons pas concrètement de pointeurs Nil dans notre trie. 
     * Il suffit donc de compter le nombre de cases vides dans le tableau "pointeurs" de chaque noeud du trie.
     * @return : nombre de pointeurs vides
     */
    public int comptageNil(){
        return comptageNilRec(root);
    }

    /**
     * Fonction récursive pour compter les pointeurs Nil ( utilisée dans comptageNil() )
     * @param node : le noeud courant sur lequel on travaille
     * @return : le nombre de pointeurs Nil trouvés jusqu'ici
     */
    private int comptageNilRec(HybridTrieNode node){
        if (node == null){
            return 0;
        }

        int count = 0;

        // Pour chaque noeud, on regarde si les cases de pointeurs sont à null ou non
        for(HybridTrieNode fils : root.getPointeurs()){
            if (fils == null){
                count++;
            }
        }

        // On parcourt récursivement les trois sous-arbres du noeud courant
        count += comptageNilRec(node.getPointeurs()[HybridTrieNode.INF]);
        count += comptageNilRec(node.getPointeurs()[HybridTrieNode.EQ]);
        count += comptageNilRec(node.getPointeurs()[HybridTrieNode.SUP]);

        return count;
    }

    /**
     * Fonction qui calcule la hauteur de l’arbre
     * @return : hauteur du trie
     */
    public int hauteur(){
        //TODO
        return 0;
    }

    /**
     *  Fonction qui calcule la profondeur moyenne des feuilles de l’arbre
     * @return : profondeur moyenne des feuilles de l'arbre
     */
    public int profondeurMoyenne(){
        //TODO
        return 0;
    }

    /**
     * Fonction qui prend un mot A en argument et qui indique de combien de mots du dictionnaire 
     * le mot A est préfixe. Le mot A n'est pas forcément un mot de l'arbre.
     * @param word : notre préfixe
     * @return : le nombre de mot dont A est le préfixe
     */
    public int prefixe(String word){
        //TODO
        return 0;
    }

    /**
     * Fonction qui prend un mot en argument et qui le supprime de l’arbre s’il y figure
     * @param word : le mot à supprimer
     */
    public void suppression(String word){
        //TODO
    }
}

