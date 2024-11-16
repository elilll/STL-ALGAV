package Trie.Patricia;
import java.util.HashMap;

public class TrieNode {
    private HashMap<String,TrieNode> children = new HashMap<String,TrieNode>(); //Assiociations d'un arc venant de ce noeud à un des ses fils
    private boolean endNode; //SI c'est un noeud final

    public TrieNode(){
        this.endNode = false;
    }

    public boolean isEndNode(){
        return endNode;
    }

    public void setEndNode(boolean endNode){
        this.endNode = endNode;
    }

    public void addChild(String key, TrieNode node){
        this.children.put(key, node);
    }

    public HashMap<String,TrieNode> getChildren(){
        return this.children;
    }

}