package org.team2869;

/**
 *
 * @author thorn
 */
public class FRCTeam {

   private int number;
   private String name;
   private String town;

   /**
    * 
    * @param number The FRC Team #
    * @param teamName 
    * @param townOrSchool is the name of the school if this team is associated with a school.
    * Where the school is unknown or a team is not part of a school, just a town is listed.
    */
   public FRCTeam(int number, String teamName, String townOrSchool) {
      this.number = number;
      name = teamName;
      town = townOrSchool;
   }

   
   public int getNumber() {
      return number;
   }

   public void setNumber(int number) {
      this.number = number;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getTown() {
      return town;
   }

   public void setTown(String town) {
      this.town = town;
   }
   
   public String toString() {
      return "Team " + number + " : " + name + " from " + town;
   }
}
