/**
* A simple class holding a word and its respective count.
*/
public class WordCount {

  private String word;
  private int count;

  public WordCount(String word, int count) {
    this.word = word;
    this.count = count;
  }

  /**
  * Gets the word stored by this WordCount
  */
  public String getWord() {
    return word;
  }
  
  /** 
  * Gets the count stored by this WordCount
  */
  public int getCount() {
    return count;
  }

  /**
  * Sets the word to be stored by this WordCount
  */
  public void setWord(String word) {
    this.word = word;
  }
  
  /** 
  * Sets the count to be stored by this WordCount
  */
  public void setCount(int count) {
    this.count = count;
  }
}