package com.example.Trie.Patricia;

import java.util.Map;
import java.util.TreeMap;

public class PatriciaTrieNode {
    /************************************************* Attribut *************************************************/
    private Map<String,PatriciaTrieNode> children = new TreeMap<String,PatriciaTrieNode>(); //Utilisation d'une treeMap 
    //pour trier les couples (arête, noeud) par orbre ASCII
    private boolean endNode; 

    /* ************************************************** Constructeur **************************************************
     * 
     * Par défaut le noeud n'est pas une fin de mot
    */
    public PatriciaTrieNode(){
        this.endNode = false;
    }

    /************************************************* Getteur et setteur *************************************************/
    public boolean isEndNode(){
        return endNode;
    }

    public Map<String,PatriciaTrieNode> getChildren(){
        return this.children;
    }

    public void setEndNode(boolean endNode){
        this.endNode = endNode;
    }

    /************************************************* Fonction de base *************************************************/

    /** Fonction qui ajoute un couple fils (arête, noeud) au PatriciaTrieNode.
     * @param key : L'arête du couple à ajouter.
     * @param node : Le noeud du couple à ajouter.
     * 
     * @return : Ne retourne rien.
    */
    public void addChild(String key, PatriciaTrieNode node){
        this.children.put(key, node);
    }

}