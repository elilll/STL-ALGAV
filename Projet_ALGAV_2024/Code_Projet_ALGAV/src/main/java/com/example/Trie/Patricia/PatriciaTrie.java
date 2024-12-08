package com.example.Trie.Patricia;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;



public class PatriciaTrie {
    /************************************************* Attributs *************************************************/
    private PatriciaTrieNode root;

    /************************************************* Constructeur *************************************************/
    public PatriciaTrie() {
        root = new PatriciaTrieNode();
    }

    /************************************************* Getteur *************************************************/
    public PatriciaTrieNode getRoot() {
        return this.root;
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

    /* Méthode qui compresse les noeuds de l'arbre particia */
    private void compressRec(PatriciaTrieNode node) {
        if (node == null) return;

        for (String key : new HashSet<>(node.getChildren().keySet())) { 
            PatriciaTrieNode child = node.getChildren().get(key);
            compressRec(child); 
            if (child.getChildren().size() == 1 && !child.isEndNode()) {
                String grandChildKey = child.getChildren().keySet().iterator().next();
                PatriciaTrieNode grandChild = child.getChildren().get(grandChildKey);

                String mergedKey = key + grandChildKey.toString();
                node.addChild(mergedKey, grandChild);

                node.getChildren().remove(key);
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
    private void insertRec(String word, PatriciaTrieNode current){
        if(word.length() == 0){
            current.setEndNode(true);
            return;
        }

        for (String edge : current.getChildren().keySet()){
            int commonPrefixLength = commonPrefixLength(word, edge);

            if(commonPrefixLength > 0){
                if(commonPrefixLength == edge.length()){
                    insertRec(word.substring(commonPrefixLength), current.getChildren().get(edge));
                    return;
                }else{
                    PatriciaTrieNode tmpNode = current.getChildren().get(edge);
                    String remmainingEdge =edge.substring(commonPrefixLength);

                    PatriciaTrieNode newNode = new PatriciaTrieNode();
                    newNode.addChild(remmainingEdge, tmpNode);


                    current.getChildren().remove(edge);
                    current.addChild(word.substring(0,commonPrefixLength), newNode);
                
                    if(commonPrefixLength < word.length()){
                        PatriciaTrieNode newNodeWord = new PatriciaTrieNode();
                        newNodeWord.setEndNode(true);
                        newNode.addChild(word.substring(commonPrefixLength), newNodeWord);
                    }else{
                        newNode.setEndNode(true);
                    }
                    return;
                }
            }
        }

        PatriciaTrieNode newNode = new PatriciaTrieNode();
        newNode.setEndNode(true);
        current.addChild(word, newNode);
        return;
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
        if(word.length() == 0){
            if(current.isEndNode()){
                return true;
            }else{
                return false;
            }
        }

        for(String edge : current.getChildren().keySet()){
            int commonPrefixLength = commonPrefixLength(word, edge);

            if (commonPrefixLength > 0 && commonPrefixLength == edge.length() ) {
                return searchRec(word.substring(commonPrefixLength), current.getChildren().get(edge));
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
        if(word.length() == 0){
            if(current.isEndNode()){
                current.setEndNode(false);
                return current.getChildren().isEmpty();
            }else{
                throw new Exception("Le mot n'est pas présent dans l'arbre");
            }
        }

        int k = 0;
        for(String edge : current.getChildren().keySet()){
            int commonPrefixLength = commonPrefixLength(word, edge);

            if (commonPrefixLength > 0 && commonPrefixLength == edge.length() ) {
                boolean needDeleteChild = deleteRec(word.substring(commonPrefixLength), current.getChildren().get(edge));

                if(needDeleteChild){
                    current.getChildren().remove(edge);
                    compressRec(current);
                    return !current.isEndNode() && current.getChildren().isEmpty();
                }
                
                return false;
            }else if(k >= current.getChildren().size() + 1){
                throw new Exception("Le mot n'est pas présent dans l'arbre");
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

    private List<String> getWordsRec(PatriciaTrieNode current, List<String> words, String prefix){
        if(current.isEndNode()){
            words.add(prefix);
        }

        for(Map.Entry<String,PatriciaTrieNode> child : current.getChildren().entrySet()){
            getWordsRec(child.getValue(), words, prefix + child.getKey());
        }

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

    private int prefixRec(PatriciaTrieNode current, String prefix) throws Exception {
        int count = 0;
        if(prefix.length() == 0){
            if(current.isEndNode()){
                count++;
            }

            for(PatriciaTrieNode child : current.getChildren().values()){
                count += prefixRec(child, prefix);
            }

            return count;

        }else{
            for(String edge : current.getChildren().keySet()){
                int commonPrefixLength = commonPrefixLength(prefix, edge);
                
                if (commonPrefixLength > 0 && commonPrefixLength == edge.length()) {
                    return prefixRec(current.getChildren().get(edge),prefix.substring(commonPrefixLength));
                }
            }

            throw new Exception("Le préfixe n'est pas présent dans l'arbre");
        }
    }

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

        insertRec(word, this.root);
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
        byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }

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
        byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }

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
        return prefixRec(this.root, prefix);
    }
}
