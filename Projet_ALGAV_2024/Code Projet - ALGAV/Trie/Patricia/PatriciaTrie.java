package Trie.Patricia;

import java.nio.charset.StandardCharsets;
import Util.ConvertJson;

public class PatriciaTrie {
    private TrieNode root;

    public PatriciaTrie() {
        root = new TrieNode();
    }

    public TrieNode getRoot() {
        return this.root;
    }

    /* Primitives insert du Particia Trie */
    /* Méthode qui permet d'insérer un mot dans un arbre radix (Patricia Trie) */
    public void insertWord(String word) {
        byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }

        TrieNode currentNode = root;
        int i = 0;

        while(i < word.length()) {
            boolean found = false;
            for(String edge : currentNode.getChildren().keySet()){
                int commonPrefixLength = commonPrefixLength(word.substring(i), edge);

                if (commonPrefixLength > 0) {
                    if(commonPrefixLength == edge.length() ){
                        currentNode = currentNode.getChildren().get(edge);
                        i+=commonPrefixLength;
                        found = true;
                        break;
                    }else{
                        TrieNode tmpNode = currentNode.getChildren().get(edge);
                        String remmainingEdge =edge.substring(commonPrefixLength);

                        TrieNode newNode = new TrieNode();
                        newNode.addChild(remmainingEdge, tmpNode);


                        currentNode.getChildren().remove(edge);
                        currentNode.addChild(word.substring(i, i+commonPrefixLength), newNode);
                    
                        if(i+commonPrefixLength < word.length()){
                            TrieNode newNodeWord = new TrieNode();
                            newNodeWord.setEndNode(true);
                            newNode.addChild(word.substring(i + commonPrefixLength), newNodeWord);
                        }else{
                            newNode.setEndNode(true);
                        }

                        return;
                    }

                }

            }

            if (!found) {
                TrieNode newNode = new TrieNode();
                newNode.setEndNode(true);
                currentNode.addChild(word.substring(i), newNode);
                return;
            }
        }

        currentNode.setEndNode(true);
    }

    /* Méthode qui insert chaque mot d'une phrase dans un arbre Patricia, puis écrit l'arbre au format .json dans le fichier pat.json */
    public void insertSentence(String sentence){
        for(String word : sentence.split(" ")){
            System.out.println(word);
            insertWord(word);
        }

        ConvertJson.convertPatriciaToJson(this);
    }

    /* Méthode qui permet de calculer le nombre de caractères préfixes en commun entre deux chaînes de caractères */
    private int commonPrefixLength(String s1, String s2) {
        int i = 0;
        while(i < s1.length() && i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }

    /* Méthode qui recherche un mot dans l'arbre Patricia, renvoie vrai s'il y est, faux sinon */
    public boolean search(String word) {
        TrieNode currentNode = root;
        int i = 0;

        while(i < word.length()) {

            for(String edge : currentNode.getChildren().keySet()){
                int commonPrefixLength = commonPrefixLength(word, edge);

                if (commonPrefixLength > 0 && commonPrefixLength == edge.length() ) {
                    currentNode = currentNode.getChildren().get(edge);
                    i+=commonPrefixLength;
                    break;
                }else{
                    return false;
                }

            }

        }
        
        return currentNode.isEndNode();
    }

    /* Méthode qui permet de supprimer un mot de l'arbre Radix */
    public boolean delete(String word) {
        return false;
    }
}
