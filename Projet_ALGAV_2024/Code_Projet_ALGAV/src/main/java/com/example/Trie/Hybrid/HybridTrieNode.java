package com.example.Trie.Hybrid;

public class HybridTrieNode {
    //Variables globales
    /** Variables globales pour accéder aux pointeurs de l'arbre plus simplement, permet de faciliter la lecture */
    public static final int INF = 0;
    public static final int EQ = 1;
    public static final int SUP = 2;
    public static final int ENDWORD = 3;
    public static final int NOTENDWORD = -1; // On met -1 par convention

    //Attributs
    private char car;
    private int val;
    private HybridTrieNode[] pointeurs;

    //Constructeur
    public HybridTrieNode(char car) {
        this.car = car;
        this.val = NOTENDWORD; //Par défaut, le noeud n'est pas une fin de mot
        this.pointeurs = new HybridTrieNode[3];
    }

    //Getters et Setters
    public char getCar(){
        return car;
    }

    public void setCar(char c){
        this.car = c;
    }

    public HybridTrieNode[] getPointeurs (){
        return pointeurs;
    }

    public void setPointeurs(HybridTrieNode[] pointeurs){
        this.pointeurs = pointeurs;
    }

    public int getVal(){
        return val;
    }

    public void setVal(int value){
        this.val = value;
    }

}
