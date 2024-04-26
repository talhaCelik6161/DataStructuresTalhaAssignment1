package wordle;

import com.sun.net.httpserver.Filter;
import project20280.hashtable.ChainHashMap;
import project20280.interfaces.Entry;
import project20280.interfaces.Position;
import project20280.priorityqueue.HeapPriorityQueue;
import project20280.tree.BinaryTreePrinter;
import project20280.tree.LinkedBinaryTree;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Wordle {

    String fileName = "wordle/resources/dictionary.txt";
    //String fileName = "wordle/resources/extended-dictionary.txt";
    List<String> dictionary = null;
    final int num_guesses = 5;
    final long seed = 42;
    //Random rand = new Random(seed);
    Random rand = new Random();

    static final String winMessage = "CONGRATULATIONS! YOU WON! :)";
    static final String lostMessage = "YOU LOST :( THE WORD CHOSEN BY THE GAME IS: ";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GREY_BACKGROUND = "\u001B[100m";

    Wordle() {

        this.dictionary = readDictionary(fileName);

        Huffman huffman = new Huffman();

        LinkedBinaryTree<String> tree = huffman.getHuffmanTree(dictionary);

        BinaryTreePrinter<String> printer = new BinaryTreePrinter<>(tree);
        String treeString = printer.print();
        System.out.println(treeString);

        Huffman.getCharEncoding(tree,"",tree.root());
        int asciiLength = 0;
        int huffmanLength = 0;
        for(String word : dictionary){
            for(char c : word.toCharArray()){
                asciiLength += 8;
                String string = Character.toString(c);
                huffmanLength += Huffman.charEncode.get(string).length();
            }
        }
        System.out.println("Huffman encoding length: " + huffmanLength);
        System.out.println("Ascii Encoding length: " + asciiLength);
        System.out.println("The ratio is: " + (double) huffmanLength/asciiLength);
    }

    public static void main(String[] args) {
        Wordle game = new Wordle();

        String target = game.getRandomTargetWord();

        //System.out.println("target: " + target);
/*
        int winNumber = 0;
        int loseNumber = 0;
        for(int k = 0; k < 1000; k++) {
            target = game.getRandomTargetWord();
            if(game.machinePlay(target)==2){
                winNumber++;
            }
            else if(game.machinePlay(target) == 1) {
                loseNumber++;
            }
        }
        System.out.println("Number of Wins: " + winNumber);
        System.out.println("Number of Losses: " + loseNumber);
        System.out.println("Your winrate is " + 100*winNumber/(winNumber+loseNumber) + "%");
    */
        game.play(target);
    }


    public double machinePlay(String target) {
        int count = 0;
        Huffman huffman = new Huffman();
        ChainHashMap<String , Integer> wordFrequencyMap = huffman.getWordFrequency(dictionary);

        // TODO
        // TODO: You have to fill in the code
        String maxKey = null;
        String guess;
        for(int i = 0; i < num_guesses; ++i) {
            if(i==0){
                guess = "abbey";
            }
            else{
                guess = maxKey;
            }
            if(Objects.equals(guess, target)) { // you won!
                count = 1;
                break;
            }

            // the hint is a string where green="+", yellow="o", grey="_"
            // didn't win ;(
            String [] hint = {"_", "_", "_", "_", "_"};
            String temp = target;
            for (int k = 0; k < 5; k++) {
                if(guess.charAt(k) == temp.charAt(k)){
                    hint[k] = "+";
                    char [] c = temp.toCharArray();
                    c[k] = '!';
                    temp = String.copyValueOf(c);
                }
                // TODO:
            }

            // set the arrays for yellow (present but not in right place), grey (not present)
            // loop over each entry:
            //  if hint == "+" (green) skip it
            //  else check if the letter is present in the target word. If yes, set to "o" (yellow)
            for (int k = 0; k < 5; k++) {
                int check = 0;
                if(hint[k].equals("+"))
                    check = 2;
                else{
                    for(int j = 0; j<5; j++){
                        if(guess.charAt(k)==temp.charAt(j)){
                            check = 1;
                            char [] c = temp.toCharArray();
                            c[j] = '!';
                            temp = String.copyValueOf(c);
                            break;
                        }
                    }
                }
                if(check==1){
                    hint[k] = "o";
                }
                else if (check == 0){
                    hint[k] = "_";
                }
                // TODO:

            }
            // after setting the yellow and green positions, the remaining hint positions must be "not present" or "_"
            for(int a = 0; a < 5; a++){
                ChainHashMap<String , Integer> wordsToRemove = new ChainHashMap<>();
                if(Objects.equals(hint[a], "+")) {
                    for (Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
                        if (!Objects.equals(entry.getKey().charAt(a), guess.charAt(a))) {
                            wordsToRemove.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                else if(Objects.equals(hint[a], "_")){
                    int numOfDuplicates = 1;
                    char letter = '!';
                    for(int c = 0; c < 5; c++){
                        if(guess.charAt(c) == guess.charAt(a) && a!=c){
                            if(hint[c].charAt(0)!='_') {
                                numOfDuplicates++;
                                letter = guess.charAt(c);
                            }
                        }
                    }
                    for(Entry<String,Integer> entry : wordFrequencyMap.entrySet()){
                        int numOfDictionaryDuplicates = 1;
                        for(int d = 0; d < 5; d++){
                            if(entry.getKey().charAt(d) == letter) {
                                numOfDictionaryDuplicates++;
                            }
                        }
                        if(numOfDictionaryDuplicates>numOfDuplicates ){
                            wordsToRemove.put(entry.getKey(), entry.getValue());
                        }

                        else if (entry.getKey().indexOf(guess.charAt(a)) != -1 && letter == '!') {
                            wordsToRemove.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                else if(Objects.equals(hint[a],"o")){
                    for(Entry<String,Integer> entry : wordFrequencyMap.entrySet()){

                        if (Objects.equals(entry.getKey().charAt(a), guess.charAt(a))) {
                            wordsToRemove.put(entry.getKey(), entry.getValue());
                        }

                        if(entry.getKey().indexOf(guess.charAt(a)) == -1){
                            wordsToRemove.put(entry.getKey(),entry.getValue());
                        }


                    }
                }


                for(Entry<String,Integer> entry : wordsToRemove.entrySet()){
                    wordFrequencyMap.remove(entry.getKey());
                }
            }

            //System.out.println("hint: " + Arrays.toString(hint));
            // check for a win
            int num_green = 0;
            for(int k = 0; k < 5; ++k) {
                if(Objects.equals(hint[k], "+"))
                    num_green += 1;
            }
            if(num_green == 5) {
                win(target);
            }
            /*
            int countt = 1;
            for(Entry<String,Integer> entry : wordFrequencyMap.entrySet()){
                System.out.println(countt + ": Word: " + entry.getKey() + "\nFrequency: " + entry.getValue());
                countt++;
            }
            */

            int maxValue = Integer.MIN_VALUE;
            for(Entry<String,Integer> entry : wordFrequencyMap.entrySet()){
                if(entry.getValue()>maxValue){
                    maxValue = entry.getValue();
                    maxKey = entry.getKey();
                }
            }
            //System.out.println("Your next guess should be: " + maxKey);
        }
        if(count == 0) {
            return 1;
        }
        else{
            return 2;
        }
    }
    public void play(String target){
        Huffman huffman = new Huffman();
        ChainHashMap<String , Integer> wordFrequencyMap = huffman.getWordFrequency(dictionary);

        // TODO
        // TODO: You have to fill in the code
        String maxKey = null;
        String guess;
        for(int i = 0; i < num_guesses; ++i) {
            guess = getGuess();
            if(Objects.equals(guess, target)) { // you won!
                win(target);
                return;
            }

            // the hint is a string where green="+", yellow="o", grey="_"
            // didn't win ;(
            String [] hint = {"_", "_", "_", "_", "_"};
            String temp = target;
            for (int k = 0; k < 5; k++) {
                if(guess.charAt(k) == temp.charAt(k)){
                    hint[k] = "+";
                    char [] c = temp.toCharArray();
                    c[k] = '!';
                    temp = String.copyValueOf(c);
                }
                // TODO:
            }

            // set the arrays for yellow (present but not in right place), grey (not present)
            // loop over each entry:
            //  if hint == "+" (green) skip it
            //  else check if the letter is present in the target word. If yes, set to "o" (yellow)
            for (int k = 0; k < 5; k++) {
                int check = 0;
                if(hint[k].equals("+"))
                    check = 2;
                else{
                    for(int j = 0; j<5; j++){
                        if(guess.charAt(k)==temp.charAt(j)){
                            check = 1;
                            char [] c = temp.toCharArray();
                            c[j] = '!';
                            temp = String.copyValueOf(c);
                            break;
                        }
                    }
                }
                if(check==1){
                    hint[k] = "o";
                }
                else if (check == 0){
                    hint[k] = "_";
                }
                // TODO:

            }
            // after setting the yellow and green positions, the remaining hint positions must be "not present" or "_"
            for(int a = 0; a < 5; a++){
                ChainHashMap<String , Integer> wordsToRemove = new ChainHashMap<>();
                if(Objects.equals(hint[a], "+")) {
                    for (Entry<String, Integer> entry : wordFrequencyMap.entrySet()) {
                        if (!Objects.equals(entry.getKey().charAt(a), guess.charAt(a))) {
                            wordsToRemove.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                else if(Objects.equals(hint[a], "_")){
                    int numOfDuplicates = 1;
                    char letter = '!';
                    for(int c = 0; c < 5; c++){
                        if(guess.charAt(c) == guess.charAt(a) && a!=c){
                            if(hint[c].charAt(0)!='_') {
                                numOfDuplicates++;
                                letter = guess.charAt(c);
                            }
                        }
                    }
                    for(Entry<String,Integer> entry : wordFrequencyMap.entrySet()){
                        int numOfDictionaryDuplicates = 1;
                        for(int d = 0; d < 5; d++){
                            if(entry.getKey().charAt(d) == letter) {
                                numOfDictionaryDuplicates++;
                            }
                        }
                        if(numOfDictionaryDuplicates>numOfDuplicates ){
                            wordsToRemove.put(entry.getKey(), entry.getValue());
                        }

                        else if (entry.getKey().indexOf(guess.charAt(a)) != -1 && letter == '!') {
                            wordsToRemove.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                else if(Objects.equals(hint[a],"o")){
                    for(Entry<String,Integer> entry : wordFrequencyMap.entrySet()){

                        if (Objects.equals(entry.getKey().charAt(a), guess.charAt(a))) {
                            wordsToRemove.put(entry.getKey(), entry.getValue());
                        }

                        if(entry.getKey().indexOf(guess.charAt(a)) == -1){
                            wordsToRemove.put(entry.getKey(),entry.getValue());
                        }


                    }
                }


                for(Entry<String,Integer> entry : wordsToRemove.entrySet()){
                    wordFrequencyMap.remove(entry.getKey());
                }
            }

            //System.out.println("hint: " + Arrays.toString(hint));
            // check for a win
            int num_green = 0;
            for(int k = 0; k < 5; ++k) {
                if(Objects.equals(hint[k], "+"))
                    num_green += 1;
            }
            if(num_green == 5) {
                win(target);
            }

            int count = 1;
            for(Entry<String,Integer> entry : wordFrequencyMap.entrySet()){
                System.out.println(count + ": Word: " + entry.getKey() + "\nFrequency: " + entry.getValue());
                count++;
            }


            int maxValue = Integer.MIN_VALUE;
            for(Entry<String,Integer> entry : wordFrequencyMap.entrySet()){
                if(entry.getValue()>maxValue){
                    maxValue = entry.getValue();
                    maxKey = entry.getKey();
                }
            }
            System.out.println("Your next guess should be: " + maxKey);
        }
        lost(target);
    }

    public void lost(String target) {
        System.out.println();
        System.out.println(lostMessage + target.toUpperCase() + ".");
        System.out.println();

    }
    public void win(String target) {
        System.out.println(ANSI_GREEN_BACKGROUND + target.toUpperCase() + ANSI_RESET);
        System.out.println();
        System.out.println(winMessage);
        System.out.println();
    }

    public String getGuess() {
        Scanner myScanner = new Scanner(System.in, StandardCharsets.UTF_8.displayName());  // Create a Scanner object
        System.out.println("Guess:");

        String userWord = myScanner.nextLine();  // Read user input
        userWord = userWord.toLowerCase(); // covert to lowercase

        // check the length of the word and if it exists
       /* while ((userWord.length() != 5) || !(dictionary.contains(userWord))) {
           if ((userWord.length() != 5)) {
                System.out.println("The word " + userWord + " does not have 5 letters.");
            } else {
                System.out.println("The word " + userWord + " is not in the word list.");
            }

            // Ask for a new word
            System.out.println("Please enter a new 5-letter word.");
            userWord = myScanner.nextLine();
        }
        */
        return userWord;
    }

    public String getRandomTargetWord() {
        // generate random values from 0 to dictionary size
        return dictionary.get(rand.nextInt(dictionary.size()));
    }
    public List<String> readDictionary(String fileName) {
        List<String> wordList = new ArrayList<>();

        try {
            // Open and read the dictionary file
            InputStream in = this.getClass().getClassLoader().getResourceAsStream(fileName);
            assert in != null;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String strLine;

            //Read file line By line
            while ((strLine = reader.readLine()) != null) {
                wordList.add(strLine.toLowerCase());
            }
            //Close the input stream
            in.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
        return wordList;
    }

}

