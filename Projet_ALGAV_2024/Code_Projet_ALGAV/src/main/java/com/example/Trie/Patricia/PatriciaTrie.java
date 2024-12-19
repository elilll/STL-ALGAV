package com.example.Trie.Patricia;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class PatriciaTrie {
    /************************************************* Attributs *************************************************/
    private PatriciaTrieNode root;
    public int nbnodes;

    /************************************************* Constructeur *************************************************/
    public PatriciaTrie() {
        this.root = new PatriciaTrieNode("");
        this.nbnodes = 1;
    }

    /************************************************* Getteur *************************************************/
    public PatriciaTrieNode getRoot() {
        return this.root;
    }

    public void setRoot(PatriciaTrieNode root){
        this.root = root;
    }

    /******************************************************* Méthodes annexes *******************************************************/
    /** Méthodes qui permet de savoir si deux chaînes de caratères ont un préfixe commun (utiliser dans insertRec, deleteRec, searchRec)
     * @param s1 : Chaine de caractères 1.
     * @param s2 : Chaine de caractères 2.
     * 
     * @return : Retourne le nombre de caractères préfixes communs entre @param s1 et @param s2
    */
     private int commonPrefixLength(String s1, String s2) {
        int i = 0;
        while(i < s1.length() && i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }

    private void compressRec(PatriciaTrieNode node) {
        if (node == null) return;

        for (Character key : new ArrayList<>(node.getChildren().keySet())) {
            PatriciaTrieNode child = node.getChildren().get(key);

            // Appel récursif pour compresser le sous-arbre du nœud enfant.
            compressRec(child);

            // Si l'enfant a exactement un enfant et n'est pas une fin de mot, fusionnez.
            if (child.getChildren().size() == 1 && !child.isEndNode()) {
                PatriciaTrieNode grandChild = child.getChildren().values().iterator().next();
                String grandChildEdge = grandChild.getEdge();

                // Fusionnez les clés.
                grandChild.setEdge(child.getEdge() + grandChildEdge);

                // Supprimez l'enfant actuel.
                node.getChildren().remove(key);

                // Ajoutez le petit-enfant directement au parent actuel.
                node.addChild(grandChild);
            }
        }
    }
        

    /******************************************************* Méthodes récursives complémentaire aux primitives *******************************************************/

    /** Méthodes récursive d'insertion utiliser dans 'insertWord'
     * @param word : La chaine de caratères à insérer
     * @param current : Noeud courrant dans lequel on parcourt pour insérer le @param word
     * @return : Ne retourne rien
     * 
     * Cette méthode parcours l'arbre à partir du noeud courant à la recherche de préfixe commun entre le mot à insérer et les arêtes des noeuds fils.
     * Si un préfixe commun est détecté, la structure de l'arbre est ajustée, si besoin, pour l'insérer en créant des noeuds et arêtes intermédiaires.
     * 
     * Quand le mot est entièrement insérer, le noeud est terminal du mot est marqué (fin de mot).
     */
    private void insertRec(String word, PatriciaTrieNode current) {
        String edge = current.getEdge();
        int commonPrefixLength = commonPrefixLength(word, edge);

        if (commonPrefixLength == edge.length() && commonPrefixLength == word.length()) {
            current.setEndNode(true);
            return;
        } else if (commonPrefixLength == 0 || (commonPrefixLength >= edge.length() && commonPrefixLength < word.length())) {
            char nextChar = word.charAt(commonPrefixLength);
            PatriciaTrieNode child = current.getChildren().get(nextChar);
            if (child != null) {
                insertRec(word.substring(commonPrefixLength), child);
                return;
            }

            PatriciaTrieNode newNode = new PatriciaTrieNode(word.substring(commonPrefixLength));
            nbnodes++;
            newNode.setEndNode(true);
            current.addChild(newNode);
            return;
        } else if (commonPrefixLength < edge.length()) {
            PatriciaTrieNode newNode = new PatriciaTrieNode(edge.substring(commonPrefixLength));
            nbnodes++;
            newNode.setChildren(new HashMap<>(current.getChildren()));
            newNode.setEndNode(current.isEndNode());

            current.setChildren(new HashMap<>());
            current.setEdge(edge.substring(0, commonPrefixLength));
            current.addChild(newNode);
            current.setEndNode(false);

            if (commonPrefixLength < word.length()) {
                PatriciaTrieNode addWordNode = new PatriciaTrieNode(word.substring(commonPrefixLength));
                nbnodes++;
                addWordNode.setEndNode(true);
                current.addChild(addWordNode);
            } else {
                current.setEndNode(true);
            }
            return;
        }
    }

    /** Méthodes récursive de recherche utiliser dans 'searchWord'
     * @param word : La chaine de caratères à rechercher
     * @param current : Noeud courrant dans lequel on parcourt pour rechercher le @param word
     * @return : Retourne true si le @param word est trouvé, faux sinon.
     * 
     * Cette méthode parcours l'arbre à partir du noeud courant à la recherche de préfixe commun entre le mot à rechercher et les arêtes des noeuds fils.
     * Si un préfixe commun est détecté, on continue la recherche sur @param word \{préfixe}.
     */
    private boolean searchRec(String word, PatriciaTrieNode current){
        String edge = current.getEdge();
        int commonPrefixLength = commonPrefixLength(word, edge);

        if(commonPrefixLength == edge.length() && commonPrefixLength == word.length()) { 
            return current.isEndNode();
        }else if(commonPrefixLength == 0 || commonPrefixLength >= edge.length() && commonPrefixLength < word.length()){
            char nextChar = word.charAt(commonPrefixLength);
            PatriciaTrieNode child = current.getChildren().get(nextChar);
            if(child != null){
                return searchRec(word.substring(commonPrefixLength), child);
            }
        }

        return false;
    
    }

    /** Méthodes récursive de suppression utiliser dans 'deleteWord'
     * @param word : La chaine de caratères à supprimer
     * @param current : Noeud courrant dans lequel on parcourt pour supprimer le @param word
     * @return : Retourne true si on peut supprimer le noeud courant de l'arbre, faux sinon.
     * 
     * Cette méthode parcours l'arbre à partir du noeud courant à la recherche de préfixe commun entre le mot à rechercher et les arêtes des noeuds fils.
     * Si un préfixe commun est détecté, on continue la recherche sur @param word \{préfixe}.
     * Une fois la recherche terminer on vérifie si l'on peut supprimer les noeuds composants le @param word de l'arbre.
     */
    private boolean deleteRec(String word,PatriciaTrieNode current) throws Exception{
        String edge = current.getEdge();
        int commonPrefixLength = commonPrefixLength(word, edge);

        if(commonPrefixLength == edge.length() && commonPrefixLength == word.length()) {
            if(current.isEndNode()){
                current.setEndNode(false);
                return current.getChildren().isEmpty();
            }else{
                throw new Exception("Le mot n'est pas présent dans l'arbre");
            }
        }else if(commonPrefixLength == 0 || commonPrefixLength >= edge.length() && commonPrefixLength < word.length()){
            char nextChar = word.charAt(commonPrefixLength);
            PatriciaTrieNode child = current.getChildren().get(nextChar);
            if(child != null){
                boolean needDeleteChild = deleteRec(word.substring(commonPrefixLength), child);

                if(needDeleteChild){
                    current.getChildren().remove(nextChar);
                    compressRec(current);
                    return !current.isEndNode() && current.getChildren().isEmpty();
                }

                //child.compressNode();
                
                return false;
            }
        }
        
        throw new Exception("Le mot n'est pas présent dans l'arbre");

    }

    private int countWordsRec(PatriciaTrieNode current){
        int count = 0;

        if(current.isEndNode()){
            count++;
        }
        
        for(PatriciaTrieNode child : current.getChildren().values()){
            count+=countWordsRec(child);
        }

        return count;
    }

    private int countNilsRec(PatriciaTrieNode current){
        int count = 0;

        if(current.getChildren().isEmpty()){
            count++;
        }
        
        for(PatriciaTrieNode child : current.getChildren().values()){
            count+=countNilsRec(child);
        }

        return count;
    }

    private int heightRec(PatriciaTrieNode current, int height){
        int hauteurMax = 0;

        if(current.getChildren().isEmpty()){
            return height;
        }

        for(PatriciaTrieNode child : current.getChildren().values()) {
            int heightChild = heightRec(child, height + 1);
            if(hauteurMax < heightChild){
                hauteurMax = heightChild;
            }
        }

        return hauteurMax;
    }

    private List<String> getWordsRec(PatriciaTrieNode current, List<String> words, String prefix) {
        if (current.isEndNode()) {
            words.add(prefix);
        }
    
        current.getChildren().forEach((character, childNode) -> 
            getWordsRec(childNode, words, prefix + childNode.getEdge())
        );
    
        return words;
    }

    private int[] meanHeightRec(PatriciaTrieNode current, int height) {
        if (current.getChildren().isEmpty()) {
            return new int[]{height, 1};
        }

        int somme = 0;
        int nodes = 1;
    
        for (PatriciaTrieNode child : current.getChildren().values()) {
            int[] childResult = meanHeightRec(child, height + 1);
            somme += childResult[0];
            nodes += childResult[1];
        }

        return new int[]{somme, nodes};
    }

    private int prefixRec(PatriciaTrieNode current, String prefix, boolean found) throws Exception {
        String edge = current.getEdge();
        int count = 0;
        if(found){
            if(current.isEndNode()){
                count++;
            }

            for(PatriciaTrieNode child : current.getChildren().values()){
                count += prefixRec(child, "",true);
            }

            return count;

        }else{
            int commonPrefixLength = commonPrefixLength(prefix, edge);

            if (commonPrefixLength == edge.length() && commonPrefixLength == prefix.length()) {
                prefixRec(current,"",true);
            }else if(commonPrefixLength > 0 && commonPrefixLength == edge.length()){
                char nextChar = prefix.charAt(commonPrefixLength);
                PatriciaTrieNode child = current.getChildren().get(nextChar);
                if (child != null) {
                    return prefixRec(child,prefix.substring(commonPrefixLength),false);
                }
            }

            throw new Exception("Le préfixe n'est pas présent dans l'arbre");
        }
    }

    /*private void fusionRec(PatriciaTrieNode current1, String prefix, PatriciaTrieNode current2) {
        if (prefix.length() == 0) {
            // Si le préfixe est vide, on fusionne tous les enfants de current2 dans current1
            for (String edge : current2.getChildren().keySet()) {
                PatriciaTrieNode child2 = current2.getChildren().get(edge);
                fusionRec(current1, edge, child2);
            }
            // Copier également l'état de fin de mot si applicable
            if (current2.isEndNode()) {
                current1.setEndNode(true);
            }
            return;
        }
    
        for (String edge : current1.getChildren().keySet()) {
            int commonPrefixLength = commonPrefixLength(prefix, edge);
    
            if (commonPrefixLength > 0) {
                if (commonPrefixLength == edge.length()) {
                    // L'edge actuelle est entièrement couverte par le préfixe
                    fusionRec(current1.getChildren().get(edge), prefix.substring(commonPrefixLength), current2);
                    return;
                } else {
                    // Cas où le préfixe partage une partie avec l'edge actuelle
                    PatriciaTrieNode tmpNode = current1.getChildren().get(edge);
                    String remainingEdge = edge.substring(commonPrefixLength);
    
                    // Créer un nouveau nœud intermédiaire
                    PatriciaTrieNode newNode = new PatriciaTrieNode();
                    newNode.addChild(remainingEdge, tmpNode);
    
                    // Mettre à jour current1
                    current1.getChildren().remove(edge);
                    current1.addChild(edge.substring(0, commonPrefixLength), newNode);
    
                    if (commonPrefixLength < prefix.length()) {
                        // Ajouter la partie restante du préfixe
                        newNode.addChild(prefix.substring(commonPrefixLength), current2);
                    } else {
                        // Si le préfixe est entièrement couvert, marquer le nœud
                        newNode.setEndNode(true);
                        // Fusionner les enfants de current2 dans newNode
                        for (String edge2 : current2.getChildren().keySet()) {
                            newNode.addChild(edge2, current2.getChildren().get(edge2));
                        }
                        if (current2.isEndNode()) {
                            newNode.setEndNode(true);
                        }
                    }
                    return;
                }
            }
        }
    
        // Si aucun préfixe commun n'est trouvé, ajouter current2 comme un nouvel enfant
        current1.addChild(prefix, current2);
    }*/

    

    /******************************************************* Primitives(insert,search,delete) *******************************************************/
    
    /** Méthode qui permet d'insérer un mot dans un arbre Patricia (Patricia Trie).
     * @param word : La chaîne de caratères à inserer.
     * @exception InvalidArgumentException : Lancée si la chaîne contient des caractères non ASCII (< 128).
     * @return : Ne retourne rien.
     * 
     * Cette méthode utilise une vérification pour s'assurer que le mot est composé uniquement
     * de caractères ASCII valides. Si un caractère invalide est détecté, une exception est levée.
    
     * L'insertion est effectuée de manière récursive à l'aide de la méthode interne 'insertRec'.
     */
    public void insertWord(String word) {
        /* Vérifie si le mot entré ne comporte que des caratères du code ASCII < 128 charatères */
        byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if((b < 0 || b > 127 )){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }

        if(searchWord(word) == false){
            insertRec(word, this.root);
        }

        return;
    }

    /**
     * Méthode qui recherche un mot dans l'arbre Patricia.
     *
     * @param word : La chaîne de caractères à rechercher.
     * @exception IllegalArgumentException : Lancée si la chaîne contient des caractères non ASCII (< 128).
     * @return : Retourne vrai si le mot @param word est présent dans l'arbre, faux sinon.
     *
     * Cette méthode utilise une vérification pour s'assurer que le mot est composé uniquement
     * de caractères ASCII valides. Si un caractère invalide est détecté, une exception est levée.

     * La recherche est effectuée de manière récursive à l'aide de la méthode interne 'searchRec'.
     */
    public boolean searchWord(String word) {
        /* Vérifie si le mot à rechercher ne comporte que des caratères du code ASCII < 128 charatères */
        /*byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }*/

        return searchRec(word,this.root);
    }

    /* Méthode qui permet de supprimer un mot de l'arbre Patricia */
    /**
     * Méthode qui supprime un mot dans l'arbre Patricia (si il est présent).
     *
     * @param word : La chaîne de caractères à supprimer.
     * @exception IllegalArgumentException : Lancée si la chaîne @param word contient des caractères non ASCII (< 128).
     * @return : Ne retourne rien.
     *
     * Cette méthode utilise une vérification pour s'assurer que le mot est composé uniquement
     * de caractères ASCII valides. Si un caractère invalide est détecté, une exception est levée.

     * La suppression est effectuée de manière récursive à l'aide de la méthode interne 'deleteRec'.
     */
    public void deleteWord(String word) throws Exception {
        /*Vérifie si le mot à supprimer ne comporte que des caratères du code ASCII < 128 charatères*/
        /*byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }*/

        try{
            deleteRec(word, this.root);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /** Méthode qui permet de compter les mots présents dans l'arbre Patricia.
     *
     * @return : Retourne le nombre de mots présent dans l'arbre Patricia.
     *
     * Le comptage est effectué de manière récursive à l'aide de la méthode interne 'countWordsRec'.
     */
    public int countWords() {
        return countWordsRec(this.root);
    }

    /** Méthode qui permet de compter les nils (feuilles) présents dans l'arbre Patricia.
     *
     * @return : Retourne le nombre de nils dans l'arbre Patricia.
     *
     * Le comptage est effectué de manière récursive à l'aide de la méthode interne 'countNilsRec'.
     */
    public int countNils() {
        return countNilsRec(this.root);
    }

    /** Méthode qui permet de calculer la hauteur de l'arbre Patricia.
     *
     * @return : Retourne la hauteur de l'arbre Patricia.
     *
     * Le comptage est effectué de manière récursive à l'aide de la méthode interne 'heightRec'.
     */
    public int height(){
        return heightRec(this.root,0);
    }

    /** Méthode qui liste les mots présent dans l'arbre Patricia.
     *
     * @return : Retourne une liste de chaîne de caractères.
     *
     * Le listing est effectué de manière récursive à l'aide de la méthode interne 'getWordsRec'.
     */
    public List<String> getWords(){
        return getWordsRec(this.root, new ArrayList<>(), "");
    }
    
    public double MeanHeight(){
        int[] heightMean = meanHeightRec(this.root, 0);

        return (double) heightMean[0]/heightMean[1];
    }

    public int countWordsPrefix(String prefix) throws Exception {
        return prefixRec(this.root, prefix,false);
    }

    /*public void fusion(PatriciaTrie pat){
        fusionRec(this.root,"",pat.getRoot());
    }*/
}
