package Trie.Patricia;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;

import Util.ConvertJson;

public class PatriciaTrie {
    private TrieNode root;

    /* Constructeur */
    public PatriciaTrie() {
        root = new TrieNode();
    }

    /* Getteur */
    public TrieNode getRoot() {
        return this.root;
    }

    /* ******************************************************Méthode private (interne à la classe)****************************************************** */
    /* Méthode qui permet de calculer le nombre de caractères préfixes en commun entre deux chaînes de caractères */
     private int commonPrefixLength(String s1, String s2) {
        int i = 0;
        while(i < s1.length() && i < s2.length() && s1.charAt(i) == s2.charAt(i)) {
            i++;
        }
        return i;
    }

    /* ******************************************************Primitives(insert,search,delete) du Particia Trie****************************************************** */
    /* Méthode qui permet d'insérer un mot dans un arbre Patricia (Patricia Trie) */
    public void insertWord(String word) {
        /*Vérifie si le mot entré ne comporte que des caratères du code ASCII < 128 charatères*/
        byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }

        TrieNode currentNode = root;
        int i = 0;

        /*Tant qu'on a pas fini d'inserer le mot, 
        si un des fils du noeud courant à une arête qui à un préfixe commun au mot (ou une sous chaine du mot), 
            si l'arête est le préfixe commun,
            on enlève le préfixe au mot et le noeud lié à l'arête devient le nouveau noeud courant,
            sinon on met a jour l'arête avec le préfixe commun trouvé et l'arête de ses fils puis on lui ajoute un nouveau fils qui à pour arête le mot\{préfixe_commun}
        sinon on ajoute un nouveau fils au noeud courant ayant pour arête le mot/sous-mot*/
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

        /*On met a jour la fin du mot sur le dernier noeud*/
        currentNode.setEndNode(true);
        return;
    }

    /* Méthode qui recherche un mot dans l'arbre Patricia, renvoie vrai s'il y est, faux sinon */
    public boolean search(String word) {
        TrieNode currentNode = root;
        int i = 0;

        /*Tant qu'on a pas fini de lire le mot, 
        si le noeud courant a des fils :
            si un des fils du noeud courant à une arête qui à un préfixe commun au mot (ou une sous chaine du mot), 
                si l'arête est le préfixe commun,
                on enlève le préfixe au mot et on continue
                sinon le mot n'est pas dans l'arbre
        sinon le mot n'est pas dans l'arbre*/
        while(i < word.length()) {
            int j = 0;
            for(String edge : currentNode.getChildren().keySet()){

                int commonPrefixLength = commonPrefixLength(word.substring(i), edge);

                if (commonPrefixLength > 0 && commonPrefixLength == edge.length() ) {
                    currentNode = currentNode.getChildren().get(edge);
                    i+=commonPrefixLength;
                    break;
                }else if(j >= currentNode.getChildren().size() -1){
                    return false;
                }
                j++;
            }
            if(i < word.length() && currentNode.getChildren().isEmpty()){
                return false;
            }
        }

        /*On vérifie si le noeud courant est un noeud de fin de mot ou non*/
        return currentNode.isEndNode();
    }

    /* Méthode qui permet de supprimer un mot de l'arbre Patricia */
    public void deleteWord(String word) throws Exception {
        /*Vérifie si le mot entré ne comporte que des caratères du code ASCII < 128 charatères*/
        byte[] bytes = word.getBytes(StandardCharsets.US_ASCII);
        for(byte b : bytes){
            if(b < 0 || b > 127){
                throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
            }
        }
        
        TrieNode currentNode = root;
        int i = 0;
        ArrayList<TrieNode> pathWordNode = new ArrayList<TrieNode>();
        ArrayList<String> pathWordEgde = new ArrayList<String>();

        pathWordNode.add(root);

        while(i < word.length()) {
            int k = 0;
            for(String edge : currentNode.getChildren().keySet()){
                int commonPrefixLength = commonPrefixLength(word.substring(i), edge);

                if (commonPrefixLength == edge.length()) {
                    currentNode = currentNode.getChildren().get(edge);

                    i+=commonPrefixLength;

                    pathWordNode.add(currentNode);
                    pathWordEgde.add(edge);

                    break;
                }else if(k >= currentNode.getChildren().size() - 1){
                    throw new Exception("Le mot : " + word +" n\'est pas présent dans l\'arbre Patricia");
                }
                k++;
            }
            if(i < word.length() && currentNode.getChildren().isEmpty()){
                throw new Exception("Le mot : " + word +" n\'est pas présent dans l\'arbre Patricia");
            }
        }

        TrieNode tmpNode = root;

        if(currentNode.isEndNode() && pathWordNode.size() == (pathWordEgde.size() + 1)) {
            for(int j = pathWordNode.size() - 1; 0 <= j; j--){
                tmpNode = pathWordNode.get(j);
                if(pathWordNode.get(j).equals(currentNode)){
                    if(pathWordNode.get(j).getChildren().isEmpty()){
                        pathWordNode.get(j - 1).getChildren().remove(pathWordEgde.get(j - 1));
                    }else{
                        pathWordNode.get(j).setEndNode(false);
                    }
                }else if(pathWordNode.get(j).getChildren().isEmpty() && !pathWordNode.get(j).equals(root)){
                    if(!pathWordNode.get(j).isEndNode()){
                        pathWordNode.get(j - 1).getChildren().remove(pathWordEgde.get(j - 1));
                    }
                }else{
                    break;
                }
            }
            compress(tmpNode);
        }else{
            throw new Exception("Le mot : " + word +" n\'est pas présent dans l\'arbre Patricia");
        }
        
    }

    private void compress(TrieNode node) {
        if (node == null) return;

        for (String key : new HashSet<>(node.getChildren().keySet())) { 
            TrieNode child = node.getChildren().get(key);
            compress(child); 
            if (child.getChildren().size() == 1 && !child.isEndNode()) {
                String grandChildKey = child.getChildren().keySet().iterator().next();
                TrieNode grandChild = child.getChildren().get(grandChildKey);

                String mergedKey = key + grandChildKey.toString();
                node.addChild(mergedKey, grandChild);

                node.getChildren().remove(key);
            }
        }
    }

    public void deleteWords(String words){
        try {
            for(String word : words.split(" ")){
                deleteWord(word);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        ConvertJson.convertPatriciaToJson(this);
    }
}
