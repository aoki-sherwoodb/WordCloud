import java.util.List;
import java.util.ArrayList;
/**
* A class mapping different words to their counts.
*/
public class WordCountMap {

  private Node root;

  /**
  * A subclass mapping a letter to a value and the letters following
  * it in words added to this {@link WordCountMap}
  */
  private class Node {

    private String letter;
    private int count;
    private List<Node> children = new ArrayList<Node>();
    
    private Node(String letter, int count) {
      this.letter = letter;
      this.count = count;
    } 
    private Node addChild(Node newNode) {
      children.add(newNode);
      return newNode;
    }
    private Node removeChild(Node childNode) {
      children.remove(childNode);
      return childNode;
    }
    private boolean hasChildren() {
      if (!children.isEmpty()) {
        return true;
      } else {
        return false;
      }
    }
    /**
    * Finds and returns the child node with the @param letter, if 
    * it exists. Otherwise returns null.
    */
    private Node getChildWithLetter(String letter) {
      for (Node child : children) {
        if (child.letter.equals(letter)) {
          return child;
        }
      }
      return null;
    }
  }

  /**
  * Constructs an empty WordCountMap.
  */         
  public WordCountMap() {
    root = new Node("", 0);
  }
  
  /**
  * Adds 1 to the existing count for word, or adds word to the WordCountMap
  * with a count of 1 if it was not already present.
  * Implementation must be recursive, not iterative.
  */
  public void incrementCount(String word) {
    String[] newLetter = step("", word);
    String letter = newLetter[0];
    String restOfWord = newLetter[1];
    incrementCountHelper(letter, restOfWord, root);
  }

  /**
  * A helper for incrementCount
  */
  private void incrementCountHelper(String letter, String restOfWord, Node curNode) {
    Node letterNode = curNode.getChildWithLetter(letter);
    boolean isLastLetter = restOfWord.equals("");
    if (letterNode == null) {
      if (isLastLetter) {
        //At the end of a new word
        curNode.addChild(new Node(letter, 1));
      } else {
        //In the middle of a new word
        curNode = curNode.addChild(new Node(letter, 0));
        String[] nextLetter = step(letter, restOfWord);
        letter = nextLetter[0];
        restOfWord = nextLetter[1];
        incrementCountHelper(letter, restOfWord, curNode);
      }
    } else {
      if (isLastLetter) {
        //At the end of an existing word
        letterNode.count++;
      } else {
        //In the middle of an existing word
        String[] newLetter = step(letter, restOfWord);
        letter = newLetter[0];
        restOfWord = newLetter[1];
        curNode = letterNode;
        incrementCountHelper(letter, restOfWord, curNode);
      }
    }
  }
   
  /**
  * Remove 1 to the existing count for word. If word is not present, does
  * nothing. If word is present and this decreases its count to 0, removes
  * any nodes in the tree that are no longer necessary to represent the
  * remaining words.
  */
  public void decrementCount(String word) {
    if (!this.contains(word)) {
      return;
    } else {
      String[] newLetter = step("", word);
      String letter = newLetter[0];
      String restOfWord = newLetter[1];
      decrementCount(letter, restOfWord, root);
    }
  }

  /**
  * Helper for decrementCount. Returns true if the word should be 
  * removed from the true.
  */
  private boolean decrementCount(String letter, String restOfWord, Node curNode) {
    Node letterNode = curNode.getChildWithLetter(letter);
    boolean isLastLetter = restOfWord.equals("");
    if (isLastLetter) {
      //Reached the last letter
      letterNode.count--;
      if (letterNode.count == 0) {
        if (!letterNode.hasChildren()) {
          curNode.removeChild(letterNode);
          return true;
        }
      } else {
        return false;
      }
    } else {
      //Not yet at the last letter
      String[] newLetter = step(letter, restOfWord);
      letter = newLetter[0];
      restOfWord = newLetter[1];
      boolean removingNodes = decrementCount(letter, restOfWord, letterNode);
      if (removingNodes && letterNode.count == 0 && !letterNode.hasChildren()) {
        //If the only word that this node belonged to was removed,
        //remove the node.
        curNode.removeChild(letterNode);
        return removingNodes;
      } else {
        return false;
      }
    }
    return false;
  }
  
  
  /**
  * Returns true if word is stored in this WordCountMap with
  * a count greater than 0, and false otherwise.
  * Implementation must be recursive, not iterative.
  */
  public boolean contains(String word) {
    String[] newLetter = step("", word);
    String letter = newLetter[0];
    String restOfWord = newLetter[1];
    Node curNode = root;
    Node letterNode = curNode.getChildWithLetter(letter);
    if (letterNode != null) {
      return contains(letter, restOfWord, letterNode);
    } else {
      return false;
    }
  }

  /**
  * A helper method for contains
  */
  private boolean contains(String letter, String restOfWord, Node curNode) {
    if (restOfWord.equals("") && curNode.letter.equals(letter)) {
      //Base case: reached the node with the last letter of the word
      //and it has a positive count
      if (curNode.count > 0) {
        return true;
      } else {
        return false;
      }
    } else {
      String[] newLetter = step(letter, restOfWord);
      letter = newLetter[0];
      restOfWord = newLetter[1];
      curNode = curNode.getChildWithLetter(letter);
      if (curNode == null) {
        return false;
      } else {
        return contains(letter, restOfWord, curNode);
      }
    }
  }
  
  /**
  * A helper method for incrementing through letters of a word and
  * tracking the corresponding nodes in the tree.
  */
  private String[] step(String letter, String restOfWord) {
    if (restOfWord.length() == 1) {
        letter = restOfWord;
        restOfWord = "";
    } else {
        letter = restOfWord.substring(0, 1);
        restOfWord = restOfWord.substring(1);
    }
    String[] newStrings = new String[] {letter, restOfWord};
    return newStrings;
  }

  /**
  * Returns the count of word, or -1 if word is not in the WordCountMap.
  * Implementation must be recursive, not iterative.
  */
  public int getCount(String word) {
    if (!this.contains(word)) {
      return -1;
    } else {
      String letter = word.substring(0, 1);
      return getCountHelper(letter, word, root);
    }
  }

  /**
  * Recursive helper for getCount taking in the current letter, the 
  * rest of the word, and the current node.
  */
  private int getCountHelper(String letter, String restOfWord, Node curNode) {
    if (restOfWord.equals("")) {
      //Base case: the current letter is the last letter of the word
      return curNode.count;
    } else {
      //There are more letters remaining in the word
      String[] newLetter = step(letter, restOfWord);
      letter = newLetter[0];
      restOfWord = newLetter[1];
      curNode = curNode.getChildWithLetter(letter);
      return getCountHelper(letter, restOfWord, curNode);
    }
  }
  
  /** 
  * Returns a list of WordCount objects, one per word stored in this 
  * WordCountMap, sorted in decreasing order by count. 
  */
  public List<WordCount> getWordCountsByCount() {
    List<WordCount> wordCounts = new ArrayList<WordCount>();
    getWordCounts(root, "", wordCounts);
    return wordCounts;
  }

  /**
  * A recursive helper for getWordCountsByCount
  */
  private void getWordCounts(Node root, String curWord, List<WordCount> wordCounts) {
    if (!root.hasChildren() && !root.letter.equals("")) {
      //Reached a leaf
      WordCount newWordCount = new WordCount(curWord.concat(root.letter), root.count);
      addInOrder(wordCounts, newWordCount);
    } else {
      //Handling a non-leaf node
      if (root.count > 0) {
        //Reached the end of a word
        WordCount newWordCount = new WordCount(curWord.concat(root.letter), root.count);
        addInOrder(wordCounts, newWordCount);
      }
      for (Node child : root.children) {
        getWordCounts(child, curWord.concat(root.letter), wordCounts);
      }
    }
  }

  /**
  * A helper for inserting WordCounts in the right order
  */
  private void addInOrder(List<WordCount> wordCounts, WordCount newWord) {
    if (wordCounts.isEmpty()) {
      wordCounts.add(newWord);
    } else {
      boolean added = false;
      int index = 0;
      while (!added) {
        if (index == wordCounts.size()) {
          //Reached the end of the list
          wordCounts.add(newWord);
          return;
        } else if (newWord.getCount() > wordCounts.get(index).getCount()) {
          //Inserting the WordCount into the proper place
          wordCounts.add(index, newWord);
          return;
        } else {
          index++;
        } 
      }
    }
  }
    
  /** 
  * Returns a count of the total number of nodes in the tree. 
  * A tree with only a root is a tree with one node; it is an acceptable
  * implementation to have a tree that represents no words have either
  * 1 node (the root) or 0 nodes.
  * Implementation must be recursive, not iterative.
  */
  public int getNodeCount() {
    return getNodeCountHelper(root);
  }

  /**
  * A recursive helper for getNodeCount()
  */
  private int getNodeCountHelper(Node node) {
    if (!node.hasChildren()) {
      return 1;
    } else {
      int count = 0;
      for (Node child : node.children) {
        count += getNodeCountHelper(child);
      }
      return 1 + count;
    }
  }

  public static void main(String[] args) {
    WordCountMap testMap = new WordCountMap();
    for (int i = 0; i < 5; i++) {
      testMap.incrementCount("then");
    }
    testMap.incrementCount("there");
    testMap.incrementCount("cat");
    for (int i = 0; i < 3; i++) {
      testMap.incrementCount("cathode");
      testMap.incrementCount("them");
    }
    testMap.incrementCount("map");

    System.out.println("Incrementing");
    System.out.println("# NODES: " + testMap.getNodeCount());
    System.out.println("count for then: " + testMap.getCount("then"));
    System.out.println("count for there: " + testMap.getCount("there"));
    System.out.println("count for cat: " + testMap.getCount("cat"));
    System.out.println("count for cathode: " + testMap.getCount("cathode"));
    System.out.println("count for them: " + testMap.getCount("them"));
    System.out.println("count for map: " + testMap.getCount("map"));
    
    List<WordCount> wordCounts = testMap.getWordCountsByCount();
    for (WordCount word : wordCounts) {
      System.out.println(word.getWord() + "," + word.getCount());
    }

    testMap.decrementCount("then");
    for (int i = 0; i < 3; i++) {
      testMap.decrementCount("them");
    }
    testMap.decrementCount("cat");

    System.out.println("Decrementing");
    System.out.println("# NODES: " + testMap.getNodeCount());
    System.out.println("count for then: " + testMap.getCount("then"));
    System.out.println("count for there: " + testMap.getCount("there"));
    System.out.println("count for cat: " + testMap.getCount("cat"));
    System.out.println("count for cathode: " + testMap.getCount("cathode"));
    System.out.println("count for them: " + testMap.getCount("them"));
    System.out.println("count for map: " + testMap.getCount("map"));

  }
}