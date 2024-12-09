package com.example.Trie.Hybrid;

public class HybridTrieNode {
    //Variables globales
    /** Variables globales pour accéder aux pointeurs de l'arbre plus simplement, faciliter la lecture */
    public static final int INF = 0;
    public static final int EQ = 1;
    public static final int SUP = 2;

    //Attributs
    private char car;
    private int val;
    private HybridTrieNode[] pointeurs;

    //Constructeur
    public HybridTrieNode(char car) {
        this.car = car;
        this.val = -1; //Par défaut, le noeud n'est pas une fin de mot, on met -1 par convention
        this.pointeurs = new HybridTrieNode[3];
    }

    //Getters et Setters
    public char getCar(){
        return car;
    }

    public int getVal(){
        return val;
    }

    public HybridTrieNode[] getPointeurs (){
        return pointeurs;
    }

    public void setVal(int value){
        this.val = value;
    }

}
