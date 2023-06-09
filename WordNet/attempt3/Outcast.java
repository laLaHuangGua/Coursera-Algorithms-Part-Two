// Attempt3: get scores 100/100 without bouns points
package WordNet.attempt3;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
  private final WordNet wordNet;

  public Outcast(WordNet wordnet) {
    wordNet = wordnet;
  }

  public String outcast(String[] nouns) {
    String outcastString = "unknown";
    int maxDist = 0;
    for (var target : nouns) {
      int dist = 0;
      for (var noun : nouns)
        dist += wordNet.distance(target, noun);
      if (dist > maxDist) {
        maxDist = dist;
        outcastString = target;
      }
    }
    return outcastString;
  }

  public static void main(String[] args) {
    WordNet wordnet = new WordNet(args[0], args[1]);
    Outcast outcast = new Outcast(wordnet);
    for (int t = 2; t < args.length; t++) {
      In in = new In(args[t]);
      String[] nouns = in.readAllStrings();
      StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    }
  }
}
