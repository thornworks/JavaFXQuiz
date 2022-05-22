package org.team2869;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;

/**
 * This class contains the main quiz application and houses all the GUI code.
 * @author LaSpina
 */
public class FRCQuizFX extends Application {
   
   TextArea questionArea;
   Button[] choiceButtons;
   Button startButton;
   FRCTeamQuestionGenerator questionGen;
   MCQuestion currentQuestion;
   int questionCounter,correctCounter;
   Label scoreLabel;

   static int WAITTIME = 1500; //1.5 seconds
   Timer nextQuestiontimer;
   
   //CSS below adapted from here: http://fxexperience.com/2011/12/styling-fx-buttons-with-css/
   
   static final String roundRedButtonCSS = "-fx-background-color: linear-gradient(#ff5400, #be1d00);"+
      "-fx-background-radius: 20;"+
      "-fx-background-insets: 0;"+
      "-fx-text-fill: white;";

   static final String buttonCSS = "-fx-background-color: linear-gradient(#888888, #333355);"+
      "-fx-background-radius: 20;"+
      "-fx-background-insets: 0;"+
      "-fx-text-fill: white;";

   static final String roundGreenButtonCSS = "-fx-background-color: linear-gradient(#00ff55, #003300);"+
      "-fx-background-radius: 20;"+
      "-fx-background-insets: 0;"+
      "-fx-text-fill: white;";
   
   static final String fontCSS = "-fx-font-size:14pt; -fx-font-weight:bold; -fx-font-family:Century Gothic, sans-serif";

   /**
    * Creates the BorderPane and buttons
    * @param primaryStage 
    */
   private Pane buildGUI(Stage primaryStage) {
      BorderPane mainPane = new BorderPane();
      questionArea = new TextArea("Click Start to take the FRC team number quiz.\n"
              + "Given a team name or school, pick their team number.");
      questionArea.setStyle(fontCSS);
      mainPane.setCenter(questionArea);   
      
      choiceButtons = new Button[5];
      //create the panel for the buttons with horizontal and vertical gaps between buttons
      FlowPane choiceButtonPane = new FlowPane(Orientation.HORIZONTAL, 8, 6);
      choiceButtonPane.setPadding(new Insets(5,15,5,15));
      for(int i=0; i<choiceButtons.length; i++) {
         choiceButtons[i] = new Button("Choice " + (char)('A' + i));
         choiceButtonPane.getChildren().add(choiceButtons[i]);
         //choiceButtonPane.setMargin(button1, new Insets(20, 0, 20, 20)); 
         choiceButtons[i].setOnAction((ActionEvent e) -> {
            choiceButtonAction(e);   
         });
      }
      
      mainPane.setBottom(choiceButtonPane);
      
      FlowPane topPane = new FlowPane(Orientation.HORIZONTAL, 10, 6);
      startButton = new Button("Start");
      scoreLabel = new Label("Score:");
      topPane.getChildren().add(startButton);
      //TODO: Add some blank space in between.
      topPane.getChildren().add(scoreLabel);
      mainPane.setTop(topPane);
      
      //add actions / eventListeners
      startButton.setOnAction((ActionEvent event) -> {
         startQuiz();
      });
      return mainPane;
   }
   
   private void startQuiz() {
      System.out.println("Starting Quiz");
      questionCounter = correctCounter = 0;
      displayNextQuestion();
   }
   
   /**
    * Calls the question generator to update the currentQuestion object to
    * store the next question. This question is then displayed in the main text area
    * and the buttons are updated to reflect the choices.
    */
   private void displayNextQuestion() {
      currentQuestion = questionGen.generateQuestion();
      questionArea.setText(currentQuestion.getQuestion());
      System.out.println(currentQuestion);
      for(int c=1; c<=currentQuestion.numChoices(); c++) {
         choiceButtons[c-1].setText(currentQuestion.getChoice(c));
         //remove any red or green style classes from the previous question choices.
         choiceButtons[c-1].getStyleClass().clear();
         
         //choiceButtons[c-1].getStyleClass().add("");
         //choiceButtons[c-1].setStyle(buttonCSS + fontCSS);
      }
   }
   
   /**
    * Handles the click action for the various choice buttons.
    * This is then used to score the button.
    * @param buttonNumber represents the choice number, starting with choice 1.
    * This should correspond with the buttons placement on the screen, so button 1
    * is on the top or to the left of button 2 and so on.
    */
   private void choiceButtonAction(ActionEvent e) {
      Button clicked = (Button) e.getSource();
      //determine if the user clicked the correct button or not.
      //If yes, turn the button green. Wait for a short time, then display the next answer.
      //If not, turn the button red and let the user click another answer.
      this.questionCounter++;
      String userAnswer = clicked.getText();
      
      //Note that we only advance to the next question afer the user answers it correctly.
      if(userAnswer.equals(currentQuestion.getCorrectAnswer())) {
         //clicked.setStyle(roundGreenButtonCSS + fontCSS);
         clicked.getStyleClass().add("rightAnswerButton");
         this.questionArea.appendText("\nCorrect!");
         this.correctCounter++;
         this.nextQuestiontimer.schedule(new NextQuestionTask(), WAITTIME);
      }
      else {
         //clicked.setStyle(roundRedButtonCSS + fontCSS);
         clicked.getStyleClass().add("wrongAnswerButton");
      }
      
      //       questionArea.setStyle(fontCSS);
      //clicked.setStyle("");
      //roundRedButtonCSS
      scoreLabel.setText("Correct: " + correctCounter + " / " + questionCounter);
   }
   
   
   
   @Override
   public void start(Stage primaryStage) {
      Pane root = buildGUI(primaryStage);
      Scene scene = new Scene(root, 300, 250);
      
      scene.getStylesheets().add(getClass().getResource("quizStyle.css").toExternalForm());
      primaryStage.setTitle("Know your Teams");
      primaryStage.setScene(scene);
      primaryStage.setWidth(700);
      primaryStage.show();
      questionGen = new FRCTeamQuestionGenerator();
      nextQuestiontimer = new Timer();
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
      launch(args);
   }
   
   /**
    * Handles the timer task of disaplying the next question after a brief delay.
    * The delay time is specified in a a constant field and the java.util.Timer object
    * which schedules this nextquestiontimer is triggered after a question is graded.
    */
   private class NextQuestionTask extends TimerTask {
      
      @Override
      public void run() {
         //This needs to run on the application thread instead of a new thread.
         Platform.runLater(() -> { displayNextQuestion(); });
      }
   }
}


      /*
      btn.setOnAction((ActionEvent event) -> {
         System.out.println("Hello World!");
      });

      btn.setOnAction(new EventHandler<ActionEvent>() {
         
         @Override
         public void handle(ActionEvent event) {
            System.out.println("Hello World!");
         }
      });
      */
