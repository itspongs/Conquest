package ConquestPkg;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

//Change the Name of the Category Quiz based on your assigned task (Example: GeographyQuiz)
public class DrivingQuiz {

    private String playerName;

    //Question Bank: [Question: Choice A, Choice B, Choice C, Choice D, Answer]

    private String[][] questionBank = {
 
    // Question 1
    {
        "", //Write the Question here
        "", //Write the Choice A
        "", //Write the Choice B
        "", //Write the Choice C
        "", //Write the Choice D
        ""  //Write the Correct Letter Answer
    },
 
    // Question 2
    {
        "",
        "",
        "",
        "",
        "",
        ""
    },
 
    // Question 3
    {
        "",
        "",
        "",
        "",
        "",
        ""
    },

    //Up until Questions 30
};

    //Change the Name of the Category Quiz based on your assigned task (Example: GeographyQuiz)
    public DrivingQuiz(String playerName){
        this.playerName = playerName;
    }

    public void start(){
        Scanner input = new Scanner(System.in);
        showIntro();
    
        if (!confirmStart(input)) {
            return;
        }

        String [][] selected = pickRandomQuestions(10);
        int score = runQuiz(input, selected);
        showResults(score);
        saveResults(score);
        returnPrompt(input);
    }

    private void showIntro() {

        //Change What is needed to be changed here!

        System.out.println("Welcome to Conquest, " + playerName + "!");
        System.out.println();

        
        System.out.println("Fun Fact: "); //Put a fun fact, write one fun fact about your questions.
        System.out.println();
        System.out.println("Would you like to start the quiz?"); 
        System.out.println("Category: Driving"); //Change the Category
        System.out.println();
        System.out.println("1. Yes");
        System.out.println("2. No");
    }

    private boolean confirmStart(Scanner input) {
        while (true) {
            System.out.print("Answer: ");
            String answer = input.nextLine();

            if (answer.equals("1")) {
                System.out.println("==================================================");
                return true;
            } 
            else if (answer.equals ("2")) {
                System.out.println("==================================================");
                System.out.println("Returning to main menu...");
                System.out.println("==================================================");
                return false;
            } 
            else {
                System.out.println("[ERROR] Invalid Input");
            }
        }
    }

    private String[][] pickRandomQuestions(int n) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (int i = 0; i < questionBank.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);

        String[][] selected = new String[n][];
        for (int i = 0; i < n; i++) {
            selected[i] = questionBank[indices.get(i)];
        }
        return selected;
    }

    private int runQuiz(Scanner input, String[][] questions) {
        int score = 0;
        for (int i = 0; i < questions.length; i++) {
            boolean correct = askQuestion (input, i + 1, questions[i]);
            if (correct) 
                score++;
            }
        return score;   
    }

    private boolean askQuestion(Scanner input, int number, String[] q){
        //q = [question, A, B, C, D, correctLetter]
        System.out.println();
        System.out.println("Question " + number + ": " + q[0]);
        System.out.println();
        System.out.println("A. " + q[1]);
        System.out.println("B. " + q[2]);
        System.out.println("C. " + q[3]);
        System.out.println("D. " + q[4]);
        System.out.println();

        String correctLetter = q[5];

        while (true) { 
            System.out.print("Answer: ");
            String answer = input.nextLine().toUpperCase();

            if (answer.equals("A") || answer.equals("B") || answer.equals("C") || answer.equals("D")) {
                if (answer.equals(correctLetter)) {
                    System.out.println(answer + " is correct!");
                    System.out.println("==================================================");
                }
                else {
                    System.out.println(answer + " is incorrect! The correct answer is " + correctLetter + ".");
                    System.out.println("==================================================");
                }
                return answer.equals(correctLetter);
            }else {
                System.out.println("[ERROR] Invalid input");
            }
            System.out.println("==================================================");
        }
        
    }

    private void showResults(int score) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("RESULTS:");
        System.out.println();
        System.out.println("Player:         " + playerName);
        System.out.println("Category:       Driving"); //Change the category here!!
        System.out.println("Score:          " + score + "/ 10");
        System.out.println("Date:           " + getFormattedDate());
        System.out.println("Time:           " + getFormattedTime());
        System.out.println("==================================================");
    }

    private void saveResults(int score) {
        String entry =
            "==================================================\n" +
            "Player:        " + playerName + "\n" +
            "Category:      Driving" + "\n" + //Change the category here!!
            "Score:         " + score + "\n" +
            "Date:          " + getFormattedDate() + "\n" +
            "Time:          " + getFormattedTime() + "\n" +
            "==================================================";

        try (FileWriter fw = new FileWriter("scores.txt", true)) {
            fw.write(entry + "\n");
            System.out.println("Results saved to scores.txt.");
        } catch (IOException e){
            System.out.println("[ERROR] Could not save results" + e.getMessage());
        }
    }

    private void returnPrompt(Scanner input) {
        while (true) { 
            System.out.println();
            System.out.println("Type 0 to go back to the main menu.");
            System.out.print("Answer: ");
            String answer = input.nextLine();

            if (answer.equals("0")) {
                System.out.println("==================================================");
                return;
            }
            else {
                System.out.println("[ERROR] Invalid Input");
            }
        }
    }

    private String getFormattedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    private String getFormattedTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
}