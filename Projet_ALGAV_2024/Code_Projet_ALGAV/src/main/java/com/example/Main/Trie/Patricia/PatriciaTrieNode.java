package src.main.java.com.example.Main.Trie.Patricia;
import java.util.HashMap;

public class PatriciaTrieNode {
    private HashMap<String,PatriciaTrieNode> children = new HashMap<String,PatriciaTrieNode>(); //Assiociations d'un arc venant de ce noeud à un des ses fils
    private boolean endNode; //SI c'est un noeud final

    public PatriciaTrieNode(){
        this.endNode = false;
    }

    public boolean isEndNode(){
        return endNode;
    }

    public void setEndNode(boolean endNode){
        this.endNode = endNode;
    }

    public void addChild(String key, PatriciaTrieNode node){
        this.children.put(key, node);
    }

    public HashMap<String,PatriciaTrieNode> getChildren(){
        return this.children;
    }

}