package WordNet.attempt3;

import java.util.ArrayList;
import java.util.HashMap;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
  private final HashMap<Integer, String[]> idToWordsMap;
  private final HashMap<String, ArrayList<Integer>> wordToIdsMap;
  private final SAP graphSap;

  public WordNet(String synsets, String hypernyms) {
    validateStringArgument(synsets);
    validateStringArgument(hypernyms);

    idToWordsMap = new HashMap<Integer, String[]>();
    wordToIdsMap = new HashMap<String, ArrayList<Integer>>();

    In synIn = new In(synsets);
    while (!synIn.isEmpty()) {
      String[] items = synIn.readLine().split(",");

      String[] words = items[1].split(" ");
      int id = Integer.parseInt(items[0]);

      idToWordsMap.put(id, words);
      for (var word : words) {
        ArrayList<Integer> ids = wordToIdsMap.get(word);
        if (ids == null)
          ids = new ArrayList<Integer>();
        ids.add(Integer.parseInt(items[0]));
        wordToIdsMap.put(word, ids);
      }
    }

    Digraph graph = new Digraph(idToWordsMap.size());

    int count = 0;
    In hyperIn = new In(hypernyms);
    while (!hyperIn.isEmpty()) {
      String[] items = hyperIn.readLine().split(",");
      if (items.length > 1) {
        int v = Integer.parseInt(items[0]);
        for (int i = 1; i < items.length; i++) {
          int w = Integer.parseInt(items[i]);
          if (v == w)
            throw new IllegalArgumentException(
                "The input to the constructor does not correspond to a DAG");
          graph.addEdge(v, w);
        }
      } else
        count++;
    }

    if (count < 1)
      throw new IllegalArgumentException(
          "The input to the constructor does not correspond to a rooted DAG");

    graphSap = new SAP(graph);
  }

  public Iterable<String> nouns() {
    return wordToIdsMap.keySet();
  }

  public boolean isNoun(String word) {
    validateStringArgument(word);
    return wordToIdsMap.containsKey(word);
  }

  public int distance(String nounA, String nounB) {
    validateWord(nounA);
    validateWord(nounB);
    return graphSap.length(wordToIdsMap.get(nounA), wordToIdsMap.get(nounB));
  }

  public String sap(String nounA, String nounB) {
    validateWord(nounA);
    validateWord(nounB);

    StringBuilder noun = new StringBuilder();
    int ancestor = graphSap.ancestor(wordToIdsMap.get(nounA), wordToIdsMap.get(nounB));

    for (var n : idToWordsMap.get(ancestor)) {
      if (noun.length() == 0) {
        noun.append(n);
        continue;
      }
      noun.append(" ").append(n);
    }
    return noun.toString();
  }

  private void validateStringArgument(String string) {
    if (string == null)
      throw new IllegalArgumentException("argument is null");
  }

  private void validateWord(String word) {
    validateStringArgument(word);
    if (!isNoun(word))
      throw new IllegalArgumentException("noun " + word + "is not a wordNet noun");
  }

  public static void main(String[] args) {
    System.out.println("No test here");
  }
}
