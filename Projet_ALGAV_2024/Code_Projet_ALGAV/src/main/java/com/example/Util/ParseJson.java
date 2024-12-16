package com.example.Util;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

import com.example.Trie.Hybrid.HybridTrieNode;
import com.example.Trie.Patricia.PatriciaTrieNode;

@SuppressWarnings("CallToPrintStackTrace")

public class ParseJson {
 
    public static void parseJsonToPatriciaTrieNode(String json, PatriciaTrieNode currentNode) throws Exception {
        Map<String, Object> map;
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            try{
                map = parseNode(json.substring(1, json.length() - 1));
                for (Map.Entry<String, Object> entry : map.entrySet()){
                    switch(entry.getKey()){
                        case "label": 
                            if(String.valueOf(entry.getValue()).length() >= 1){
                                PatriciaTrieNode newNode = new PatriciaTrieNode();

                                byte[] bytes = String.valueOf(entry.getValue()).getBytes(StandardCharsets.US_ASCII);
                                for(byte b : bytes){
                                    if(b < 0 || b > 127){
                                        throw new Exception("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
                                    }
                                }

                                currentNode.addChild(String.valueOf(entry.getValue()), newNode);
                                currentNode = newNode;
                            }
                            break;
                        case "is_end_of_word" : 
                            if(null == String.valueOf(entry.getValue())){
                                throw new Exception("Value invalide pour 'is_end_of_word' : " + String.valueOf(entry.getValue()));
                            }else switch (String.valueOf(entry.getValue())) {
                            case "true":
                                currentNode.setEndNode(true);
                                break;
                            case "false":
                                currentNode.setEndNode(false);
                                break;
                            default:
                                throw new Exception("Value invalide pour 'is_end_of_word' : " + String.valueOf(entry.getValue()));
                        }
                            break;

                        case "children":
                            parseJsonToPatriciaTrieNode(String.valueOf(entry.getValue()), currentNode);
                        break;
                        default: 
                            if(entry.getKey().length() < 1 || entry.getKey().length() > 1){
                                throw new Exception("Key invalide : " + entry.getKey());
                            }
                            parseJsonToPatriciaTrieNode(String.valueOf(entry.getValue()), currentNode);
                            break;
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            throw new Exception("Format json invalide : " + json);
        }
    }

    /* Méthode qui permet de parser les noeuds des Tries au format json (key : value). Retourne une table de hachage (<key,value>) ordonné par ordre d'insertion. */
    private static Map<String, Object> parseNode(String jsonString) throws Exception {
        String newJsonString = jsonString.replaceAll("\\s+", "");
        Map<String, Object> map = new LinkedHashMap<>();
        int i = 0;

        while (i < newJsonString.length()) {
            // récupère le label de la key 
            int keyStart = newJsonString.indexOf('"', i) + 1;
            int keyEnd = newJsonString.indexOf('"', keyStart);
            String key = newJsonString.substring(keyStart, keyEnd);

            int colon = newJsonString.indexOf(':', keyEnd) + 1;

            char firstChar = newJsonString.charAt(skipSpacesAndCommas(newJsonString, colon));

            i = skipSpacesAndCommas(newJsonString, colon);

            switch (firstChar) {
                case '"':
                    int valueStart = i + 1;
                    int valueEnd = newJsonString.indexOf('"', valueStart);
                    String valueString = newJsonString.substring(valueStart, valueEnd);
                    map.put(key, valueString);
                    i = valueEnd + 1;
                    break;
                case '{':
                    int bracketEnd = findClosingBracket(newJsonString, i, '{', '}');
                    if(bracketEnd == -1) {
                        throw new Exception("Arbre de Patricia non conforme au format json (Oublie de { ou } (non égale)) (" + key + " : " + newJsonString.substring(i)+ ")");
                    }

                    String subObject = newJsonString.substring(i, bracketEnd + 1);
                    map.put(key, subObject);
                    i = bracketEnd + 1;
                    break;
                case 't': case 'f':
                    boolean valueBoolean = newJsonString.startsWith("true", i);
                    map.put(key, valueBoolean);
                    i += valueBoolean ? 4 : 5;
                    break;
                case 'n':
                    if(newJsonString.substring(i).equalsIgnoreCase("null")){
                        map.put(key, null);
                        i += 4;
                    }else{
                        throw new Exception("Arbre de Patricia non conforme au format json (key : value) (les values ne peuvent être que soit une chaine de caractères(entre \"\"), un boolean (true,false), un sous-noeud (entre {}) ou (une feuille vide (null)(trieHybryd)) (" + key + " : " + newJsonString.substring(i)+ ")");
                    }
                default:
                    throw new Exception("Arbre de Patricia non conforme au format json (key : value) (les values ne peuvent être que soit une chaine de caractères(entre \"\"), un boolean (true,false), un sous-noeud (entre {}) ou (une feuille vide (null)(trieHybryd)) (" + key + " : " + newJsonString.substring(i)+ ")");
            }
            if(i >= newJsonString.length()){
                break;
            }
            if(i == skipSpacesAndCommas(newJsonString, i)){
                throw new Exception("Arbre de Patricia non conforme au format json (key : value) (après chaque value mettre une \",\" (sauf si c'est la dernier value d'un noeud) (" + key + " : " + newJsonString.substring(i)+ ")");
                
            }
            i = skipSpacesAndCommas(newJsonString, i);
        }
        return map;
    }

    /* Méthodes qui renvoie l'index de la paranthèse fermantes (si elle existe) d'un bloc  */
    private static int findClosingBracket(String json, int startIndex, char openBracket, char closeBracket) {
        int count = 0;
        for (int i = startIndex; i <= json.length(); i++) {
            if (json.charAt(i) == openBracket) count++;
            else if (json.charAt(i) == closeBracket) --count;
            if (count == 0) return i;
        }
        return -1; 
    }

    /* Méthodes qui ignore les "," et les " " dans une chaine de caractère. Incrémente l'index en fonction du nombre de " " ou "," ignorés. */
    private static int skipSpacesAndCommas(String json, int i) {
        while (i < json.length() && (json.charAt(i) == ' ' || json.charAt(i) == ',')) i++;
        return i;
    }

    public static void parseJsonToHybridTrieNode(String json, HybridTrieNode currentNode) throws Exception {
        Map<String, Object> map;
        json = json.trim();
        
        if (json.startsWith("{") && json.endsWith("}")) {
            try {
                map = parseNode(json.substring(1, json.length() - 1));
    
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    switch (entry.getKey()) {
                        case "char":
                            // Vérification et affectation du caractère
                            if (String.valueOf(entry.getValue()).length() == 1) {
                                char c = String.valueOf(entry.getValue()).charAt(0);
                                currentNode.setCar(c);
                            } else {
                                throw new Exception("Valeur invalide pour 'char' : " + entry.getValue());
                            }
                            break;
    
                        case "is_end_of_word":
                            if (null == String.valueOf(entry.getValue())) {
                                throw new Exception("Valeur invalide pour 'is_end_of_word' : " + entry.getValue());
                            } else // Définir si le nœud est une fin de mot
                        switch (String.valueOf(entry.getValue())) {
                            case "true":
                                currentNode.setVal(HybridTrieNode.END_WORD); // Marque la fin d'un mot
                                break;
                            case "false":
                                currentNode.setVal(HybridTrieNode.NOT_END_WORD); // Pas une fin de mot
                                break;
                            default:
                                throw new Exception("Valeur invalide pour 'is_end_of_word' : " + entry.getValue());
                        }
                            break;

    
                        case "left":
                            // Traiter le sous-arbre gauche
                            if (!"null".equals(String.valueOf(entry.getValue()))) {
                                String value = String.valueOf(entry.getValue());
                                if (value.length() == 1) {
                                    char c = value.charAt(0); // Convertir en char
                                    HybridTrieNode leftNode = new HybridTrieNode(c); // Création avec char
                                    currentNode.getPointeurs()[HybridTrieNode.INF] = leftNode;
                                    parseJsonToHybridTrieNode(String.valueOf(entry.getValue()), leftNode);
                                } else {
                                    throw new Exception("Valeur invalide pour 'left' : " + value + ". Un seul caractère attendu.");
                                }
                            }
                            break;
    
                        case "middle":
                            // Traiter le sous-arbre du milieu
                            if (!"null".equals(String.valueOf(entry.getValue()))) {
                                String value = String.valueOf(entry.getValue());
                                if (value.length() == 1) {
                                    char c = value.charAt(0); 
                                    HybridTrieNode middleNode = new HybridTrieNode(c); 
                                    currentNode.getPointeurs()[HybridTrieNode.INF] = middleNode;
                                    parseJsonToHybridTrieNode(String.valueOf(entry.getValue()), middleNode);
                                } else {
                                    throw new Exception("Valeur invalide pour 'middle' : " + value + ". Un seul caractère attendu.");
                                }
                            }
                            break;
    
                        case "right":
                            // Traiter le sous-arbre droit
                            if (!"null".equals(String.valueOf(entry.getValue()))) {
                                String value = String.valueOf(entry.getValue());
                                if (value.length() == 1) {
                                    char c = value.charAt(0); 
                                    HybridTrieNode rightNode = new HybridTrieNode(c); 
                                    currentNode.getPointeurs()[HybridTrieNode.INF] = rightNode;
                                    parseJsonToHybridTrieNode(String.valueOf(entry.getValue()), rightNode);
                                } else {
                                    throw new Exception("Valeur invalide pour 'right' : " + value + ". Un seul caractère attendu.");
                                }
                            }
                            break;
    
                        default:
                            throw new Exception("Clé inconnue dans le JSON : " + entry.getKey());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new Exception("Format JSON invalide : " + json);
        }
    }
    

}
