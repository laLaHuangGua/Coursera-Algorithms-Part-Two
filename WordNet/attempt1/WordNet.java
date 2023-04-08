package WordNet.attempt1;

import java.util.TreeMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;

public class WordNet {
  private final Digraph graph;
  private final TreeMap<Integer, String> wordList;
  private final SAP graphSap;

  public WordNet(String synsets, String hypernyms) {
    validateStringArgument(synsets);
    validateStringArgument(hypernyms);

    wordList = new TreeMap<Integer, String>();

    In synIn = new In(synsets);
    while (!synIn.isEmpty()) {
      String[] items = synIn.readLine().split(",");
      wordList.put(Integer.parseInt(items[0]), items[1]);
    }

    graph = new Digraph(wordList.size());

    int count = 0;
    In hyperIn = new In(hypernyms);
    while (!hyperIn.isEmpty()) {
      String[] items = hyperIn.readLine().split(",");
      if (items.length > 1) {
        for (int i = 1; i < items.length; i++)
          graph.addEdge(Integer.parseInt(items[0]), Integer.parseInt(items[i]));
      } else
        count++;
    }

    if (count < 1)
      throw new IllegalArgumentException(
          "The input to the constructor does not correspond to a rooted DAG");

    graphSap = new SAP(graph);
  }

  public Iterable<String> nouns() {
    return wordList.values();
  }

  public boolean isNoun(String word) {
    validateStringArgument(word);
    return wordList.containsValue(word);
  }

  public int distance(String nounA, String nounB) {
    validateWord(nounA);
    validateWord(nounB);
    return graphSap.length(getKeysBy(nounA), getKeysBy(nounB));
  }

  public String sap(String nounA, String nounB) {
    validateWord(nounA);
    validateWord(nounB);
    return wordList.get(
        graphSap.ancestor(getKeysBy(nounA), getKeysBy(nounB)));
  }

  private void validateStringArgument(String string) {
    if (string == null)
      throw new IllegalArgumentException("argument is null");
  }

  private void validateWord(String word) {
    validateStringArgument(word);
    if (!wordList.containsValue(word))
      throw new IllegalArgumentException("word " + word + " is not a wordNet noun");
  }

  private Iterable<Integer> getKeysBy(String word) {
    SET<Integer> keys = new SET<Integer>();
    for (var entry : wordList.entrySet()) {
      if (entry.getValue().equals(word))
        keys.add(entry.getKey());
    }
    return keys;
  }

  public static void main(String[] args) {
  }
}
