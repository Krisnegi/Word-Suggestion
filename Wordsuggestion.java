import java.io.*;
import java.util.*;

public class Wordsuggestion {
    public static void main(String args[]) {
        // Word & Count to find
        String wordToFind = args[1];
        int count = 5;
        // Read Csv & get sorted words
        String[] words = new CSVReader().readCsv(args[0]);
        // Instantiate AutoComplete class with sorted words from csv
        AutoComplete autoComplete = new AutoComplete(words);
        ArrayList<String> results = new ArrayList<String>();
        // Loop until we find 5 results OR until the searched word is blank
        while (results.size() < 5 && wordToFind.length() > 0) {
            ArrayList<String> matchedWords = autoComplete.findMatchingWords(wordToFind, results, count - results.size());
            results.addAll(matchedWords);
            wordToFind = wordToFind.substring(0, wordToFind.length() - 1);
        }
        // print found words
        for (String foundWord : results) {
            System.out.print(foundWord+", ");
        }
    }
}

class AutoComplete {
    ArrayList<String> words;
    public AutoComplete(String[] words) {
        this.words = new ArrayList(Arrays.asList(words));
    }
    // Finds "wordToFind" in "words". "excluding" excludes items from the searched words
    ArrayList<String> findMatchingWords(String wordToFind, ArrayList<String> excluding, int count) {
        // Create a new list after excluding the words
        ArrayList<String> remainingWords = new ArrayList(words);
        remainingWords.removeAll(excluding);
        ArrayList<String> matchedWords = new ArrayList<String>();
        // Loop on the remainingWords list
        for (int i = 0; i < remainingWords.size(); i++) {
            // if number of found results = count, we can break and return
            if (matchedWords.size() >= count) {
                break;
            } else if (remainingWords.get(i).toLowerCase().startsWith(wordToFind.toLowerCase())) {
                // match by lowercase both words so its case insensitive
                matchedWords.add(remainingWords.get(i));
            }
        }
        return matchedWords;
    }
}

class CSVReader {
    public static ArrayList<ArrayList<String>> al=new ArrayList<ArrayList<String>>();
    String[] readCsv(String csvPath) {
        try {
            File file = new File(csvPath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line = "";
            String[] tempArr;
            int i=0;
            while((line = br.readLine()) != null) {
                ArrayList<String> char_list=new ArrayList<String>();
                tempArr = line.split(",");
                for(String tempStr : tempArr) {
                    char_list.add(tempStr);
                }
                al.add(char_list);
            }
            br.close();
        }
        catch(IOException ioe) {
            ioe.printStackTrace();
        }
        Collections.sort(al, new Comparator<List<String>> () {
            @Override
            public int compare(List<String> a, List<String> b) {
                return -(Integer.parseInt(a.get(1))-Integer.parseInt(b.get(1)));
            }
        });
        // remove counts and just return string[]
        String[] sortedWords = new String[al.size()];
        for (int i = 0; i < al.size(); i++) {
            sortedWords[i] = al.get(i).get(0);
        }
        return sortedWords;
    }
}