package com.example.Trie.Hybrid;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HybridTrie {
    //Varibales pour calculer les complexités effectives
    public int cmplx_insert;
    public int cmplx_recherche;
    public int cmplx_comptage_mots;
    public int cmplx_liste_mots;
    public int cmplx_comptage_nils;
    public int cmplx_hauteur;
    public int cmplx_profondeur;
    public int cmplx_prefixe;
    public int cmplx_suppression;
    public int nb_node;

    //Constantes
    public static final boolean BALANCED = true;
    public static final boolean NON_BALANCED = false;

    //Attribut
    private HybridTrieNode root; // Racine du trie hybride

    //Constructeur
    public HybridTrie() {
        this.root = null; // Initialisation du trie vide
        this.cmplx_comptage_mots = 0;
        this.cmplx_comptage_nils = 0;
        this.cmplx_hauteur = 0;
        this.cmplx_insert = 0;
        this.cmplx_liste_mots = 0;
        this.cmplx_prefixe = 0;
        this.cmplx_profondeur = 0;
        this.cmplx_recherche = 0;
        this.cmplx_suppression = 0;
        this.nb_node = 0;
    }

    //Getteur et Setteur
    public HybridTrieNode getRoot(){
        return this.root;
    }

    public void setRoot(HybridTrieNode root) {
        this.root = root;
    }

    //Méthodes avancées

    /** 
     * Fonction d'insertion d'un mot 
     * @param word : le mot à insérer dans notre trie
     * @param balance : true si on veut rééquilibrer l'arbre (au besoin) à chaque insertion, false sinon
    */
    public void insert(String word, boolean balance) {
        /* Vérifie si le mot entré ne comporte que des caratères du code ASCII < 128 charatères */
        byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }
        
        cmplx_insert = 0; // A chaque appel de la fonction, on réinitialise la complexité

        if(recherche(word) == false){
            root = insertRec(root, word, 0, balance);
            //On affiche la complexité après chaque ajout
            //System.out.println("Complexité de l'ajout du mot " + word + " : " + cmplx_insert);
        }
    }

    /**
     * Fonction récursive d'insertion d'un mot ( utilisée dans insert() )
     * @param node : le noeud courant sur lequel on doit naviguer
     * @param word : le mot à insérer dans notre trie
     * @param charIndex : le numéro du caractère de word sur lequel on travaille (lettre par lettre)
     * @param balance : true si on veut rééquilibrer l'arbre (au besoin) à chaque insertion, false sinon
     * @return : on retourne le noeud courant modifié (pour correctement mettre à jour la structure du trie)
     */
    private HybridTrieNode insertRec(HybridTrieNode node, String word, int charIndex, boolean balance) {
        char currentChar = word.charAt(charIndex);

        if (node == null) {
            node = new HybridTrieNode(currentChar);
            nb_node++;
        }

        cmplx_insert++;
        if (currentChar < node.getCar()) {
            node.getPointeurs()[HybridTrieNode.INF] = insertRec(node.getPointeurs()[HybridTrieNode.INF], word, charIndex, balance); // Inf
        } else if (currentChar > node.getCar()) {
            node.getPointeurs()[HybridTrieNode.SUP] = insertRec(node.getPointeurs()[HybridTrieNode.SUP], word, charIndex, balance); // Sup
        } else { // currentChar == node.getCar()
            if (charIndex + 1 == word.length()) { // Si on est bien arrivé à la fin de la lecture de notre mot ...
                node.setVal(HybridTrieNode.END_WORD); // ... on marque la fin du mot 
            } else { // Sinon : on continue de parcourir l'arbre dans la branche Eq
                node.getPointeurs()[HybridTrieNode.EQ] = insertRec(node.getPointeurs()[HybridTrieNode.EQ], word, charIndex + 1, balance); // Eq
            }
        }

        // Rééquilibrer si demandé
        return balance ? reequilibrer(node) : node;
    }

    /**
     * Fonction pour rechercher un mot dans le trie
     * @param word : le mot à rechercher
     * @return : true si le mot est présent dans le trie, false sinon
     */
    public boolean recherche(String word){
        cmplx_recherche = 0; // A chaque appel de la fonction, on réinitialise la complexité
        boolean found = rechercheRec(root, word, 0);
        //On affiche la complexité après chaque recherche
        //System.out.println("Complexité de la recherche du mot " + word + " : " + cmplx_recherche);
        return found;
    }

    /**
     * Fonction de recherche récursive ( utilisée dans recherche() )
     * @param node : le noeud courant sur lequel on travaille
     * @param word : le mot à rechercher
     * @param charIndex : le numéro du caractère de word sur lequel on travaille (lettre par lettre)
     * @return : true si le mot existe, false sinon
     */
    private boolean rechercheRec(HybridTrieNode node, String word, int charIndex){
        char currentChar = word.charAt(charIndex);

        if (node == null) {
            return false;
        }

        cmplx_recherche++;
        if (currentChar < node.getCar()) {
            return rechercheRec(node.getPointeurs()[HybridTrieNode.INF], word, charIndex);  // Inf
        } else if (currentChar > node.getCar()) {
            return rechercheRec(node.getPointeurs()[HybridTrieNode.SUP], word, charIndex); // Sup
        } else { // currentChar == node.getCar()
            if (charIndex + 1 == word.length()) {
                return node.getVal() != HybridTrieNode.NOT_END_WORD; // On vérifie que le noeud marque bien la fin valide d'un mot dans le trie
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
        cmplx_comptage_mots = 0;
        int res = comptageMotsRec(root);
        //On affiche la complexité finale
        //System.out.println("Complexité de comptage des mots dans le trie : " + cmplx_comptage_mots);
        return res;
    }

    /**
     * Fonction de comptage récursive ( utilisée dans comptageMots() )
     * Rappel : si un noeud a pour valeur -1 (HybridTrieNode.NOTENDWORD), c'est que ce n'est pas la terminaison d'un mot du trie
     * @param node : le noeud courant sur lequel on travaille
     * @return : le nombre de mots détectés
     */
    private int comptageMotsRec(HybridTrieNode node){
        if (node == null){
            return 0; // Pas de mot si le noeud est vide
        }

        int count = 0;
        cmplx_comptage_mots++;
        if (node.getVal() == HybridTrieNode.END_WORD) {
            count++; // Si val est différent de -1 (HybridTrieNode.NOTENDWORD), c'est qu'un mot a été ajouté dans le trie
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
        cmplx_liste_mots = 0;
        // On utilise un StringBuilder pour éviter les copies inutiles de String lors des modifications
        StringBuilder prefixe = new StringBuilder();
        List<String> finalList = listeMotsRec(root, prefixe);
        //On affiche la complexité finale
        //System.out.println("Complexité de la création de la liste des mots : " + cmplx_liste_mots);
        return finalList;
    }

    /**
     * Fonction récursive pour créer la liste des mots du trie ( utilisée dans listeMots() )
     * Principe : on a un préfixe qui permet de garder en mémoire à chaque appel récursif les lettres parcourues (et ainsi construire le mot petit à petit).
     * Quand on arrive sur la fin d'un mot (val != -1 || HybridTrieNode.NOTENDWORD), le préfixe contient le mot dans son entierté, il suffit alors de l'ajouter dans notre liste.
     * @return : la liste contenant les mots qu'on a trouvé jusqu'ici
     */
    private List<String> listeMotsRec(HybridTrieNode node, StringBuilder prefixe){
        List<String> liste = new ArrayList<>();

        if (node == null){
            return liste; // Si le noeud est vide, on renvoie la liste vide
        }

        // On parcourt récursivement les trois sous-arbres du noeud courant
        cmplx_liste_mots++;
        liste.addAll(listeMotsRec(node.getPointeurs()[HybridTrieNode.INF], new StringBuilder(prefixe))); // Inf

        cmplx_liste_mots++;
        prefixe.append(node.getCar()); // On ajoute le caractère pour explorer le sous-arbre Eq
        cmplx_liste_mots++;
        liste.addAll(listeMotsRec(node.getPointeurs()[HybridTrieNode.EQ], prefixe)); // Eq
        cmplx_liste_mots++;
        prefixe.deleteCharAt(prefixe.length() - 1); // On retire le caractère après avoir exploré Eq

        cmplx_liste_mots++;
        liste.addAll(listeMotsRec(node.getPointeurs()[HybridTrieNode.SUP], new StringBuilder(prefixe))); // Sup

        // Si le noeud marque la fin d'un mot, ajouter le mot complet à la liste
        cmplx_liste_mots++;
        if (node.getVal() != HybridTrieNode.NOT_END_WORD) {
            liste.add(prefixe.toString() + node.getCar());
        }

        return liste;
    }

    /**
     * Fonction qui compte les pointeurs vers Nil 
     * NB : pour éviter de prendre trop de place en mémoire, on ne crée des noeuds qu'en cas de besoin. 
     * Ainsi, nous n'avons pas concrètement de pointeurs Nil dans notre trie. 
     * Il suffit donc de compter le nombre de cases vides dans le tableau "pointeurs" de chaque noeud du trie.
     * @return : nombre de pointeurs vides
     */
    public int comptageNil(){
        cmplx_comptage_nils = 0;
        if (root == null) {
            return 0; // Si la racine est null, aucun pointeur à compter
        }
        int nils = comptageNilRec(root);
        //On affiche la complexité finale
        //System.out.println("Complexité du calcul du nombre de Nils : " + cmplx_comptage_nils);
        return nils;
    }

    /**
     * Fonction récursive pour compter les pointeurs Nil ( utilisée dans comptageNil() )
     * @param node : le noeud courant sur lequel on travaille
     * @return : le nombre de pointeurs Nil trouvés jusqu'ici
     */
    private int comptageNilRec(HybridTrieNode node){
        if (node == null){
            return 1;
        }

        int count = 0;

        // Pour chaque noeud, on regarde si les cases de pointeurs sont à null ou non
        for(HybridTrieNode fils : root.getPointeurs()){
            cmplx_comptage_nils++;
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
     * Rappel : hauteur de l'arbre = distance maximale entre la racine et la feuille la plus profonde
     * @return : hauteur du trie
     */
    public int hauteur(){
        cmplx_hauteur = 0;
        int hauteur = hauteurRec(root);
        //On affiche la complexité finale
        //System.out.println("Complexité du calcul de la hauteur : " + cmplx_hauteur);
        return hauteur;
    }

    /**
     * Fonction récursive pour calculer la hauteur du trie ( utilisée dans hauteur() )
     * @param node : le noeud sur lequel on travaille
     * @return : 1 + la plus grande hauteur parmi ses fils
     */
    private int hauteurRec(HybridTrieNode node){
        if (node == null){
            return 1;
        }

        int hauteurInf = hauteurRec(node.getPointeurs()[HybridTrieNode.INF]);
        int hauteurEq = hauteurRec(node.getPointeurs()[HybridTrieNode.EQ]);
        int hauteurSup = hauteurRec(node.getPointeurs()[HybridTrieNode.SUP]);

        cmplx_hauteur++;
        return 1 + Math.max(hauteurInf, Math.max(hauteurEq, hauteurSup));
    }

    /**
     * Fonction qui calcule la profondeur moyenne des feuilles de l'arbre
     * Calcul : profondeur moyenne = somme des profondeurs des feuilles / nombre de feuilles
     * Principe : on a un tableau result pour stocker la somme des profondeurs des feuilles (dans result[0])
     * et pour stocker le nombre de feuilles rencontrées (dans rersult[1]).
     * @return : la profondeur moyenne des feuilles de l'arbre
     */
    public int profondeurMoyenne() {
        cmplx_profondeur = 0;
        int[] result = new int[2];
        profondeurMoyenneRec(root, 0, result);
        
        if (result[1] == 0) {
            return 0; // Pour ne pas diviser par 0 (cas où on n'a pas de feuilles == trie vide)
        }
        //On affiche la complexité finale
        //System.out.println("Complexité du calcul de la profondeur moyenne des feuilles : " + cmplx_profondeur);
        return result[0] / result[1];
    }
    
    /**
     * Fonction récursive pour calculer la profondeur moyenne des feuilles
     * @param node : le noeud courant sur lequel on travaille
     * @param profondeur : la profondeur du noeud courant
     * @param result : tableau qui stocke la somme des profondeurs et le nombre de feuilles
     */
    private void profondeurMoyenneRec(HybridTrieNode node, int profondeur, int[] result) {
        if (node == null) {
            return;
        }
    
        // On vérifie si le noeud courant est une feuille ou pas
        cmplx_profondeur++;
        if (node.getPointeurs()[HybridTrieNode.INF] == null &&
            node.getPointeurs()[HybridTrieNode.EQ] == null &&
            node.getPointeurs()[HybridTrieNode.SUP] == null) {
            
            result[0] += profondeur; 
            result[1]++;             
        }
    
        // On parcourt récursivement les trois sous-arbres du noeud courant
        profondeurMoyenneRec(node.getPointeurs()[HybridTrieNode.INF], profondeur + 1, result); // Inf
        profondeurMoyenneRec(node.getPointeurs()[HybridTrieNode.EQ], profondeur + 1, result);  // Eq
        profondeurMoyenneRec(node.getPointeurs()[HybridTrieNode.SUP], profondeur + 1, result); // Sup
    }
    

    /**
     * Fonction qui prend un mot A en argument et qui indique de combien de mots du dictionnaire 
     * le mot A est préfixe. Le mot A n'est pas forcément un mot de l'arbre.
     * Principe : 1) On trouve le noeud correspondant au préfixe (qui n'est pas forcément un mot du trie, donc on ne peut pas utiliser val ici)
     * 2) 
     * @param word : notre préfixe
     * @return : le nombre de mot dont A est le préfixe
     */
    public int prefixe(String word){
        cmplx_prefixe = 0;
        cmplx_comptage_mots = 0;
        HybridTrieNode node = goToNodeFromWord(root, word, 0);

        if (node == null){
            return 0;
        }
        
        int res = comptageMotsRec(node);

        //On affiche la complexité finale
        // System.out.println("Complexité pour le parcours du préfixe : " + cmplx_prefixe);
        // System.out.println("Complexité pour le comptage des mots : " + cmplx_comptage_mots);
        // System.out.println("Complexité totale pour le prefixe " + word + " : " + (cmplx_prefixe + cmplx_comptage_mots));

        return res;
    }

    /**
     * Fonction de recherche récursive pour aller directement au noeud qui correspond à word ( utilisée dans prefixe() )
     * @param node : le noeud courant sur lequel on travaille
     * @param word : le mot à rechercher
     * @param charIndex : le numéro du caractère de word sur lequel on travaille (lettre par lettre)
     * @return : le noeud actuel modifié
     */
    private HybridTrieNode goToNodeFromWord(HybridTrieNode node, String word, int charIndex) {
        if (node == null || charIndex >= word.length()) {
            return node;
        }

        char currentChar = word.charAt(charIndex);

        if (currentChar < node.getCar()) {
            cmplx_prefixe++;
            return goToNodeFromWord(node.getPointeurs()[HybridTrieNode.INF], word, charIndex);
        } else if (currentChar > node.getCar()) {
            cmplx_prefixe++;
            return goToNodeFromWord(node.getPointeurs()[HybridTrieNode.SUP], word, charIndex);
        } else {
            cmplx_prefixe++;
            return goToNodeFromWord(node.getPointeurs()[HybridTrieNode.EQ], word, charIndex + 1);
        }
    }


    /**
     * Fonction qui prend un mot en argument et qui le supprime de l’arbre s’il y figure
     * @param word : le mot à supprimer
     */
    public void suppression(String word){
        cmplx_suppression = 0;

        if(recherche(word) == true){
            root = suppressionRec(root, word, 0);
            //On affiche la complexité finale
            //System.out.println("Complexité de la suppression du mot " + word + " : " + cmplx_suppression);
        }else{
            System.out.println("Le mot que vous cherchez à supprimer n'existe pas dans le trie hybride");
        }
    }

    /**
     * Fonction récursive pour supprimer un mot de l'arbre
     * NB : pour pouvoir respecter la structure de l'arbre, la suppression doit prendre en compte 
     * @param node : le noeud courant sur lequel on travaille
     * @param word : le mot à supprimer
     * @param charIndex : le numéro du caractère de word sur lequel on travaille (lettre par lettre) 
     * @return : le noeud actuel modifié
     */
    private HybridTrieNode suppressionRec(HybridTrieNode node, String word, int charIndex) {
        if (node == null) {
            return null; 
        }
        
        // Vérification pour éviter un dépassement d'index
        if (charIndex >= word.length()) {
            return node;
        }

        char currentChar = word.charAt(charIndex);

        cmplx_suppression++;
        if (charIndex + 1 == word.length() && node.getVal() != HybridTrieNode.NOT_END_WORD && currentChar == node.getCar()) {
            node.setVal(HybridTrieNode.NOT_END_WORD); // On enlève le marquage de fin de mot
        } 

        if (currentChar < node.getCar()) {
            cmplx_suppression++;
            node.getPointeurs()[HybridTrieNode.INF] = suppressionRec(node.getPointeurs()[HybridTrieNode.INF], word, charIndex);
        } else if (currentChar > node.getCar()) {
            cmplx_suppression++;
            node.getPointeurs()[HybridTrieNode.SUP] = suppressionRec(node.getPointeurs()[HybridTrieNode.SUP], word, charIndex);
        } else { // currentChar == node.getCar()
            cmplx_suppression++;
            node.getPointeurs()[HybridTrieNode.EQ] = suppressionRec(node.getPointeurs()[HybridTrieNode.EQ], word, charIndex + 1);
        }

        // Après avoir traité le mot, vérifier si ce noeud peut être supprimé == il ne contient aucun enfant
        cmplx_suppression++;
        if (node.getVal() == HybridTrieNode.NOT_END_WORD 
            && node.getPointeurs()[HybridTrieNode.INF] == null
            && node.getPointeurs()[HybridTrieNode.EQ] == null
            && node.getPointeurs()[HybridTrieNode.SUP] == null) {
                return null;
        }

        return node;
    }

    //Méthode complexe

    /**
     * Fonction qui permet de calculer la hauteur d'un noeud en parcourant ses sous-arbres gauches et droites
     * @param node : le noeud dont on veut calculer la hauteur
     * @return : la hauteur du noeud
     */
    private int hauteurNoeud(HybridTrieNode node) {
        if (node == null) {
            return 1; 
        }
        return 1 + Math.max(
            hauteurNoeud(node.getPointeurs()[HybridTrieNode.INF]), 
            hauteurNoeud(node.getPointeurs()[HybridTrieNode.SUP])
        );
    }

    /**
     * Fonction qui permet de faire une rotation à droite sur le sous-arbre gauche s'il y a un déséquilibre
     * @param node : le noeud à partir duquel on veut faire la rotation à droite
     * @return : l'arbre après la rotation
     */
    private HybridTrieNode rotationDroite(HybridTrieNode node) {
        HybridTrieNode newRoot = node.getPointeurs()[HybridTrieNode.INF];
        node.getPointeurs()[HybridTrieNode.INF] = newRoot.getPointeurs()[HybridTrieNode.SUP];
        newRoot.getPointeurs()[HybridTrieNode.SUP] = node;
        return newRoot;
    }

    /**
     * Fonction qui permet de faire une rotation à gauche sur le sous-arbre droit s'il y a un déséquilibre
     * @param node : le noeud à partir duquel on veut faire la rotation à gauche
     * @return : l'arbre après la rotation
     */
    private HybridTrieNode rotationGauche(HybridTrieNode node) {
        HybridTrieNode newRoot = node.getPointeurs()[HybridTrieNode.SUP];
        node.getPointeurs()[HybridTrieNode.SUP] = newRoot.getPointeurs()[HybridTrieNode.INF];
        newRoot.getPointeurs()[HybridTrieNode.INF] = node;
        return newRoot;
    }

    /**
     * Fonction qui permet de vérifier l'équilibre d'un arbre et d'effectuer les rotations nécessaires au besoin
     * NB : nous avons choisi un seuil de 1 pour déterminer si un arbre est déséquilibré ou non
     * @param node : le noeud à partir duquel on veut appliquer le rééquilibrage
     * @return : l'arbre rééquilibré
     */
    private HybridTrieNode reequilibrer(HybridTrieNode node) {
        if (node == null) {
            return null;
        }

        int hauteurGauche = hauteurNoeud(node.getPointeurs()[HybridTrieNode.INF]);
        int hauteurDroite = hauteurNoeud(node.getPointeurs()[HybridTrieNode.SUP]);
    
        // Rotation droite si le sous-arbre gauche est trop profond == la différence de hauteur entre les deux sous-arbres est supérieure à 1
        if (hauteurGauche - hauteurDroite > 1) {
            if (hauteurNoeud(node.getPointeurs()[HybridTrieNode.INF].getPointeurs()[HybridTrieNode.SUP]) >
            hauteurNoeud(node.getPointeurs()[HybridTrieNode.INF].getPointeurs()[HybridTrieNode.INF])) {
                node.getPointeurs()[HybridTrieNode.INF] = rotationGauche(node.getPointeurs()[HybridTrieNode.INF]);
            }
            node = rotationDroite(node);
        }
    
        // Rotation gauche si le sous-arbre droit est trop profond == la différence de hauteur entre les deux sous-arbres est supérieure à 1
        if (hauteurDroite - hauteurGauche > 1) {
            if (hauteurNoeud(node.getPointeurs()[HybridTrieNode.SUP].getPointeurs()[HybridTrieNode.INF]) >
            hauteurNoeud(node.getPointeurs()[HybridTrieNode.SUP].getPointeurs()[HybridTrieNode.SUP])) {
                node.getPointeurs()[HybridTrieNode.SUP] = rotationDroite(node.getPointeurs()[HybridTrieNode.SUP]);
            }
            node = rotationGauche(node);
        }
    
        return node;
    }
    
}

