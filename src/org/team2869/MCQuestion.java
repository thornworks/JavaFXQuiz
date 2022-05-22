package org.team2869;

import java.util.ArrayList;
/**
 * Stores a (plain) text only multiple choice question without any formatting.
 * 
 * @author LaSpina
 */
public class MCQuestion {
   private String question;
   private ArrayList<String> answerChoices; 
   private int correctChoice; //indexed from 1 (first choice is correct)

   public MCQuestion(String question, ArrayList<String> choices) {
      this.question = question;
      answerChoices = choices;
   }

   public MCQuestion(String question) {
      this.question = question;
      answerChoices = new ArrayList<>();
   }

   public String getQuestion() {
      return question;
   }

   public int getCorrectChoice() {
      return correctChoice;
   }

   /**
    * 
    * @return  The correct answer response (not the choice number but the choice String)
    */
   public String getCorrectAnswer() {
      return getChoice(correctChoice);
      
   }
   public void setCorrectChoice(int correctChoice) {
      this.correctChoice = correctChoice;
   }
   
   /**
    * Adds another answer choice to the list of possible choices.
    * @param s the new answer choice to add 
    */
   public void addChoice(String s) {
      this.answerChoices.add(s);
   }
   
   public int numChoices() {
      return this.answerChoices.size();
   }
   public String getChoice(int c) {
      if(c<=0 || c > answerChoices.size())
         throw new IllegalArgumentException("Answer choices range from 1 to the number of "
                 + " choices (1 to " + answerChoices.size() + ").\n" +
                 "Invalid getChoice parameter: " + c);
      return answerChoices.get(c-1);
   }
   
   public String toString() {
      StringBuilder sb = new StringBuilder(this.question);
      sb.append("\n");
      char choice = 'A';
      for(String s : this.answerChoices) {
         sb.append("  ").append(choice);
         choice++;
         sb.append(". ").append(s);
      }
      sb.append("\n correct: ").append(correctChoice).append(" ").append(getCorrectAnswer());

      return sb.toString();
   }
}
