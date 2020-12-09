import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.io.PrintWriter;

/**
* A class that counts the occurences of every word in an input text
*/
public class WordCounter {

  private WordCountMap wordMap = new WordCountMap();
  //A list storing all the WordCount objects for the words loaded
  //by this WordCounter
  private List<WordCount> wordList = new ArrayList<WordCount>();
  //A reference list to store Stop Words
  private List<String> stopWordsList = new ArrayList<String>();

  public WordCounter(String inputFilePath) {
    
    try {
      //Loading stopWords
      File stopWords = new File("StopWords.txt");
      Scanner stopScanner = new Scanner(stopWords);
      while (stopScanner.hasNextLine()) {
        stopWordsList.add(stopScanner.nextLine().strip());
      }
      stopScanner.close();
    } catch (FileNotFoundException e) {
      System.exit(0);
    }

    this.load(inputFilePath);
  }

  public void load(String textFile) {
    //Loading the text
    try {
      File text = new File(textFile);
      Scanner scanner = new Scanner(text);
      while (scanner.hasNext()) {
        String word = scanner.next();
        //Normalizing each word
        while (word.length() > 0 && !Character.isLetterOrDigit(word.charAt(0))) {
          //Stripping leading punctuation
          word = word.substring(1);
        }
        while (word.length() > 0 && !Character.isLetterOrDigit(word.charAt(word.length() - 1))) {
          //Stripping trailing punctuation
          word = word.substring(0, word.length() - 1);
        }
        word = word.toLowerCase();
        if (word.length() != 0 && !stopWordsList.contains(word)) {
          wordMap.incrementCount(word);
        }
        
      }
      scanner.close();
      wordList = wordMap.getWordCountsByCount();
      
    } catch (FileNotFoundException e) {
      System.out.println("Could not find a matching file");
      System.exit(0);
    } 
  }


  public static void main(String[] args) {
    
    if (args.length == 1) {

      WordCounter wordCounter = new WordCounter(args[0]);
      for (WordCount word : wordCounter.wordList) {
        System.out.println(word.getWord() + ":" + word.getCount());
      }

    } else if (args.length == 3) {

      WordCounter wordCounter = new WordCounter(args[0]);
      List<WordCount> wordCloudList = new ArrayList<WordCount>();
      String fileName = args[2];

      int numWords = Integer.parseInt(args[1]);
      if (numWords > wordCounter.wordList.size()) {
        for (WordCount wordCount : wordCounter.wordList) {
          //If the desired amount of words is greater than the total
          //number of words, just copy over the whole word list
          wordCloudList.add(wordCount);
        }
      } else {
        for (int i = 0; i < numWords; i++) {
          //Otherwise, copy over the desire number of words
          wordCloudList.add(wordCounter.wordList.get(i));
        }
      }
      PrintWriter wordCloudHTML = null;
      try {
        //Attempting to write to the file with the name passed in
        //as commandline argument 3
        wordCloudHTML = new PrintWriter(fileName);
      } catch (FileNotFoundException e) {
        System.out.println("Could not create a file with the specified name.");
        System.exit(0);
      }
      wordCloudHTML.print(WordCloudMaker.getWordCloudHTML("Word Cloud", wordCloudList));
      wordCloudHTML.close();

    } else {
      System.out.println("Usage: ");
      System.out.println("To display all words in the text with their respective counts, enter the file storing the text as the only commandline argument.");
      System.out.println("To create a wordcloud, the first argument should be the name of the text file, the second should be the number of words to be displayed in the cloud, and the third should be file to output the wordcloud to.");
      System.exit(0);
    }
  }
}