/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;

import static java.util.Arrays.sort;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength = DEFAULT_WORD_LENGTH;
    public  ArrayList<String> wordList = new ArrayList<String>();
    public HashSet<String> wordset = new HashSet<String>();
    public  HashMap<String,ArrayList<String> > lettertoword=new HashMap<String,ArrayList<String>>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();


    private Random random = new Random();
    private List<String> sorted = new ArrayList<String>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();

            wordset.add(word);
            wordList.add(word);

            if(lettertoword.containsKey(sortLetters(word))){
                ArrayList<String> anagram = lettertoword.get(sortLetters(word));
                anagram.add(word);
                lettertoword.put(sortLetters(word),anagram);

            }else
            {
                ArrayList<String> anagram = new ArrayList<String>();
                anagram.add(word);
                lettertoword.put(sortLetters(word),anagram);
            }
            if(sizeToWords.containsKey(word.length()))
            {
                sizeToWords.get(word.length()).add(word);
            }
            else
            {
                ArrayList<String> temp = new ArrayList<String>();
                temp.add(word);
                sizeToWords.put(word.length(),temp);

            }
        }


    }

    public boolean isGoodWord(String word, String base) {
        if (word.contains(base)){
            return false;
        }
        if(wordset.contains(word)){
            return true;
        }

        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String temp = sortLetters(targetWord);
      /*  for(int i=0;i<wordList.size();i++)
        {
            if(wordList.get(i).length() == targetWord.length())
            {
                if(temp.equals(sortLetters(wordList.get(i))))
                    result.add(wordList.get(i));
            }
        }*/
        return lettertoword.get(temp);

    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(char alphabet = 'a'; alphabet <= 'z';alphabet++) {
            if (lettertoword.containsKey(sortLetters(word + alphabet))) {
                // get list of angrams
                ArrayList<String> listAnagrams = lettertoword.get(sortLetters(word + alphabet));
                // add each anagram

                for (int i = 0; i < listAnagrams.size(); i++) {
                    if (isGoodWord(word, listAnagrams.get(i))) {
                        result.add(listAnagrams.get(i));
                    }
                }

            }
        }

        return result;
    }

    public String pickGoodStarterWord() {
        int randomNumber;
        int numAnagrams = 0;

        ArrayList<String> listWordsMaxLength = sizeToWords.get(wordLength);
        int arraySize = listWordsMaxLength.size();

        while (numAnagrams < MIN_NUM_ANAGRAMS) {


            randomNumber = random.nextInt(arraySize);
            String randomWord = listWordsMaxLength.get(randomNumber);


            numAnagrams = getAnagramsWithOneMoreLetter(randomWord).size();
            if (numAnagrams >= MIN_NUM_ANAGRAMS) {
                if (wordLength < MAX_WORD_LENGTH) {
                    wordLength++;
                }
                return randomWord;
            }
        }
        return "stop";
    }




    public String sortLetters(String word)
    {
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

}