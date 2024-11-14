package Util;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Trie.Patricia.TrieNode;

public class ParseJson {
    public static void parseJsonToPatriciaTrieNode(String json, TrieNode currentNode) {
        Map<String, Object> map;
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            map = parseNode(json.substring(1, json.length() - 1));
            for (Map.Entry<String, Object> entry : map.entrySet()){
                System.out.println("key: " + entry.getKey());
                System.out.println("value: " + entry.getValue().toString());
                System.out.println("currentNode: " + currentNode);
                switch(entry.getKey()){
                    case "label": 
                    System.out.println("label case");
                        if(String.valueOf(entry.getValue()).length() >= 1){
                            TrieNode newNode = new TrieNode();
                            byte[] bytes = String.valueOf(entry.getValue()).getBytes(StandardCharsets.US_ASCII);
                            for(byte b : bytes){
                                if(b < 0 || b > 127){
                                    throw new IllegalArgumentException("Caractère non supporté : " + Byte.toString(b) + " (code ascii : " + b +")");
                                }
                            }
                            currentNode.addChild(String.valueOf(entry.getValue()), newNode);
                            currentNode = newNode;
                        }

                        break;
                    case "is_end_of_word" : 
                        System.out.println("is_end_of_word case");
                        if(String.valueOf(entry.getValue()) == "true"){
                            currentNode.setEndNode(true);
                        }else{
                            currentNode.setEndNode(false);
                        }
                        break;
                    case "children":
                        System.out.println("children case");
                        parseJsonToPatriciaTrieNode(String.valueOf(entry.getValue()), currentNode);
                    break;
                    default: 
                        System.out.println("default : key = " + String.valueOf(entry.getKey()));
                        parseJsonToPatriciaTrieNode(String.valueOf(entry.getValue()), currentNode);
                        break;
                }
            }

        }
    }

    private static Map<String, Object> parseNode(String jsonString) {
        String newJsonString = jsonString.replaceAll("\\s+", "");
        //System.out.println("json = " + newJsonString);
        Map<String, Object> map = new LinkedHashMap<>();
        int i = 0;
        //System.out.println("length: " + newJsonString.length());
        while (i < newJsonString.length()) {
            //System.out.println(i);
            // Find the key
            i = skipSpacesAndCommas(newJsonString, i);
            int keyStart = newJsonString.indexOf('"', i) + 1;
            int keyEnd = newJsonString.indexOf('"', keyStart);
            String key = newJsonString.substring(keyStart, keyEnd);

            //System.out.println("keyStart: " + keyStart);
            //System.out.println("keyEnd: " + keyEnd);
            //System.out.println("key =" + key.toString());
            
            // Find the separator `:`
            int colon = newJsonString.indexOf(':', keyEnd) + 1;

            // Find the value
            char firstChar = newJsonString.charAt(skipSpacesAndCommas(newJsonString, colon));
            //System.out.println("first char : " + firstChar +"(index: " + colon);

            i = skipSpacesAndCommas(newJsonString, colon);

            if (firstChar == '"') {
                // Case for String value
                //System.out.println("first char : " + firstChar);
                int valueStart = i + 1;
                int valueEnd = newJsonString.indexOf('"', valueStart);
                String value = newJsonString.substring(valueStart, valueEnd);
                map.put(key, value);
                i = valueEnd + 1;

            } else if (firstChar == '{') {
                // Case for nested object
                int bracketEnd = findClosingBracket(newJsonString, i, '{', '}');
                //System.out.println("bracketEnd : " + bracketEnd);
                String subObject = newJsonString.substring(i, bracketEnd + 1);
                map.put(key, subObject);
                i = bracketEnd + 1;
                //System.out.println("bracketEnd: " + bracketEnd+1);

            } else if (firstChar == 't' || firstChar == 'f') {
                //System.out.println("first char : " + firstChar);
                // Case for boolean (true/false)
                boolean value = newJsonString.startsWith("true", i);
                map.put(key, value);
                i += value ? 4 : 5;

            } else if (firstChar == 'n') {
                //System.out.println("first char : " + firstChar);
                // Case for null value
                map.put(key, null);
                i += 4;
            }

            // Skip any commas or spaces
            i = skipSpacesAndCommas(newJsonString, i);
        }
        return map;
    }

    private static int findClosingBracket(String json, int startIndex, char openBracket, char closeBracket) {
        int count = 0;
        for (int i = startIndex; i <= json.length(); i++) {
            //System.out.println("i : " + i);
            //System.out.println("chart: " + json.charAt(i));
            //System.out.println("count:"+ count);
            if (json.charAt(i) == openBracket) count++;
            else if (json.charAt(i) == closeBracket) --count;
            if (count == 0) return i;
        }
        return -1; // Should never happen if JSON is well-formed
    }

    private static int skipSpacesAndCommas(String json, int i) {
        while (i < json.length() && (json.charAt(i) == ' ' || json.charAt(i) == ',')) i++;
        return i;
    }

}
