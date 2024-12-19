package com.example.Trie.Patricia;

import java.util.Map;
import java.util.TreeMap;

public class PatriciaTrieNode {
    /************************************************* Attribut *************************************************/
    private String edge;
    private Map<Character,PatriciaTrieNode> children; //Utilisation d'une treeMap 
    //pour trier les couples (arête, noeud) par orbre ASCII
    private boolean endNode; 

    /* ************************************************** Constructeur **************************************************
     * 
     * Par défaut le noeud n'est pas une fin de mot
    */
    public PatriciaTrieNode(String edge){
        this.endNode = false;
        this.edge = edge;
        this.children  = new TreeMap<Character,PatriciaTrieNode>();
    }

    /************************************************* Getteur et setteur *************************************************/
    public boolean isEndNode(){
        return endNode;
    }

    public void setEndNode(boolean endNode){
        this.endNode = endNode;
    }

    public Map<Character,PatriciaTrieNode> getChildren(){
        return this.children;
    }

    public void setChildren(Map<Character, PatriciaTrieNode> children) {
        this.children = children;
    }

    public String getEdge() {
        return edge;
    }

    public void setEdge(String edge) {
        this.edge = edge;
    }

    /************************************************* Fonction de base *************************************************/

    /** Fonction qui ajoute un couple fils (arête, noeud) au PatriciaTrieNode.
     * @param node : Le noeud du couple à ajouter.
     *
     * @return : Ne retourne rien.
    */
    public void addChild(PatriciaTrieNode node) {
        if (node != null && node.getEdge() != null) {
            char firstChar = node.getEdge().charAt(0);
            this.children.put(firstChar, node);
        }
    }

    public void compressNode() {
        // Vérifie si le nœud peut être compressé (il ne doit pas être une fin de mot et avoir un seul enfant)
        if (!this.endNode && this.children.size() == 1) {
            // Récupère l'unique enfant
            Map.Entry<Character, PatriciaTrieNode> singleChild = this.children.entrySet().iterator().next();
            PatriciaTrieNode childNode = singleChild.getValue();
            childNode.compressNode();

            // Combine l'arête actuelle avec l'arête de l'enfant
            this.edge = this.edge + childNode.getEdge();

            // Met à jour les enfants et l'état de fin du nœud
            this.children = childNode.getChildren();
            this.endNode = childNode.isEndNode();
            this.children.remove(singleChild.getKey());
        }
    }

}