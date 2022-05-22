package org.team2869;

import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 *
 * This class is used to generate multiple choice questions about FRC team names
 * and numbers.
 *
 * @author LaSpina
 */
public class FRCTeamQuestionGenerator {
   static TreeMap<Integer,FRCTeam> teamMap;
   static ArrayList<Integer> teamNumberList;
   private static String sourceFile = "FRCTeams.csv";
   
   /**
    * Loads in team data from a text file.
    * @param filename should be a tab delimited three column spreadsheet.
    * The columns are Name /t town or school /t number.
    */
   public FRCTeamQuestionGenerator(String filename) {
      teamMap = new TreeMap<>();
      teamNumberList = new ArrayList<>(100);
      loadData(filename); //initial file has 55 teams. 
   }

   public FRCTeamQuestionGenerator() {
      this(sourceFile);
   }   
   
   private void loadData(String f) {
      //read file into stream, try-with-resources
      try (Stream<String> stream = Files.lines(Paths.get(f))) {

         stream.forEach(this::addLine);

      } catch (IOException e) {
         System.out.println("Could not open file: " + f);
         System.out.println(e);
         e.printStackTrace();
      }
      
   }
   
   private void addLine(String line) {
      String[] parts = line.split("\t");
      if(parts.length < 3 || parts[0].equals("Team Name"))
         return;
      String name = parts[0];
      String school = parts[1];
      int num = Integer.parseInt(parts[2].trim());
      FRCTeam team = new FRCTeam(num, name, school);
      teamMap.put(num, team);
      teamNumberList.add(num);
   }


/**
 * Generates a multiple choice question where the question is a team name
 * and the choices are team numbers.
 * 
 * @param numChoices should be between 2 and 6, the number of choices for the
 * generated question.
 * @return a Multple Choice question with the specified number of choices.
 */   
   public MCQuestion generateQuestion(int numChoices) {
      //first we pick the correct answer: index is the index of the right answer.
      int index = (int) (Math.random()*teamNumberList.size());
      int teamNum = teamNumberList.get(index); //The correct team number
      FRCTeam t = teamMap.get(teamNum);
      MCQuestion mcq = new MCQuestion(t.getName() + " from " + t.getTown());
      int correct = (int) (Math.random()*numChoices) + 1;  //choice 1 through 4
      mcq.setCorrectChoice(correct);
      int r;
      //place choices into a Set to make sure we don't have any duplicate choices.
      //Include 4 team numbers from the list and one random 3 or 4 digit number for fun.
      TreeSet<Integer> choiceSet = new TreeSet<>();
      choiceSet.add(teamNum);
      for(int i=1; i<numChoices-1; i++) {
         r = (int) (Math.random()*teamNumberList.size());
         choiceSet.add(teamNumberList.get(r));
      }
      //We will usually have numChoices-1 numbers in the set.
      //Occasionally, the same number will be randomly chosen twice, so we will have less.
      int randomTeamNumber;
      while(choiceSet.size() < numChoices) {
         randomTeamNumber = (int)(Math.random()*3500 + 200);
         choiceSet.add(randomTeamNumber);
         
      }
      //We should now have 
      int choiceIndex = 1;
      for(int team : choiceSet) {
         mcq.addChoice("" + team);
         if(team==teamNum) {
            mcq.setCorrectChoice(choiceIndex);
         }
         choiceIndex++;
      }
      /*
      First draft of code occassionally would place duplicate choices.
      for(int i=1; i<=numChoices; i++) {
         if(i==correct)
            mcq.addChoice("" + teamNum);
         else {
            r = (int) (Math.random()*teamNumberList.size());
            if(r==index)
               r = (r+5) % teamNumberList.size();
            mcq.addChoice("" + teamNumberList.get(r));
         }  
      }      
      */

      return mcq;
   }
   
   /**
    * Generates a multiple choice question where the question is a team name
    * and the choices are team numbers.
    * @return a Multple Choice question with 4 choices.
    */
    public MCQuestion generateQuestion() {
       return generateQuestion(5);
    }
   //need to read data from a table and store in a map.
   public static void main(String[] args) {
      String fileName = "FRCTeams.csv";
      FRCTeamQuestionGenerator qgen = new FRCTeamQuestionGenerator(fileName);
      
      //for(int key : teamMap.keySet())
      //   System.out.println(teamMap.get(key));
      //System.out.println(teamMap.size());
      MCQuestion q1 = qgen.generateQuestion();
      System.out.println(q1);
      System.out.println("     Correct choice:" + q1.getCorrectChoice());
      MCQuestion q2 = qgen.generateQuestion(5);
      System.out.println(q2);
      System.out.println("     Correct choice:" + q2.getCorrectChoice());
      
   }
}
