// Applied Technical Requirements:

// METHODS
// - getPlayerName
// - mainMenu
// - playMenu
// - launchQuiz
// - showScores
// - exitGame
// - ErrorHandler

// Loops
// - while-loop

// Objects OOP
// - GeographyQuiz
// - DrivingQuiz
// - GeneralKnowledge
// - MediaQuiz
// - ProgrammingQuiz
// - BrainrotQuiz



import java.util.Scanner;

public class ConquestMain {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String playerName = getPlayerName();
        mainMenu(playerName);
    }

    // =========================================================================
    // METHOD 1: Get Player Name
    // =========================================================================
    public static String getPlayerName() {
        String name = "";
        while (name.isEmpty()) {
            System.out.print("Input Player Name: ");
            name = input.nextLine().trim();
            if (name.isEmpty()) {
                showError();
            }
        }

        System.out.println("==================================================");
        return name;
    }

    // =========================================================================
    // METHOD 2: Main Menu
    // =========================================================================
    public static void mainMenu(String playerName) {
        while (true) {
            System.out.println("Welcome to Conquest, " + playerName + "!");
            System.out.println();
            System.out.println("Choose Option: ");
            System.out.println("1. Play");
            System.out.println("2. Score");
            System.out.println("3. Exit");
            System.out.println();
            System.out.print("Option: ");

            String option = input.nextLine().trim();

            switch (option) {
                case "1":
                    playMenu(playerName);
                    break;
                case "2":
                    showScores();
                    break;
                case "3":
                    exitGame(playerName);
                    break;
                default:
                    showError();
            }
        }
    }

    // =========================================================================
    // METHOD 3: Play Menu
    // =========================================================================
    public static void playMenu(String playerName) {
        System.out.println("==================================================");
        System.out.println("Choose which quiz will you be taking:");
        System.out.println("1. Geography");
        System.out.println("2. Driving");
        System.out.println("3. General Knowledge");
        System.out.println("4. Media");
        System.out.println("5. Programming");
        System.out.println("6. Brainrot");
        System.out.println("0. Back");
        System.out.println();

        String quizChoice;

        while (true) {
            System.out.print("Quiz: ");
            quizChoice = input.nextLine().trim();

            if (quizChoice.matches("[0-6]")) {
                break;
            } else {
                showError();
            }
        }

        System.out.println("==================================================");

        // 0 = go back to main menu
        if (quizChoice.equals("0")) {
            System.out.println("Returning to Main Menu...");
            System.out.println("==================================================");
            return;
        }

        launchQuiz(quizChoice, playerName);
    }

    // =========================================================================
    // METHOD 4: Launch Quiz  (calls the correct Quiz class)
    // =========================================================================
    public static void launchQuiz(String quizChoice, String playerName) {
        switch (quizChoice) {
            case "1":
                //GeographyQuiz.start(playerName, input);
                System.out.println("TO BE IMPLEMENTED GeographyQuiz.java");
                System.out.println("==================================================");
                break;
            case "2":
                //DrivingQuiz.start(playerName, input);
                System.out.println("TO BE IMPLEMENTED DrivingQuiz.java");
                System.out.println("==================================================");
                break;
            case "3":
                //GeneralKnowledgeQuiz.start(playerName, input);
                System.out.println("TO BE IMPLEMENTED GeneralKnowledgeQuiz.java");
                System.out.println("==================================================");
                break;
            case "4":
                //MediaQuiz.start(playerName, input);
                System.out.println("TO BE IMPLEMENTED MediaQuiz.java");
                System.out.println("==================================================");
                break;
            case "5":
                //ProgrammingQuiz.start(playerName, input);
                System.out.println("TO BE IMPLEMENTED ProgrammingQuiz.java");
                System.out.println("==================================================");
                break;
            case "6":
                //BrainrotQuiz.start(playerName, input);
                System.out.println("TO BE IMPLEMENTED BrainrotQuiz.java");
                System.out.println("==================================================");
                break;
            default:
                showError();
        }
    }

    // =========================================================================
    // METHOD 5: Show Scores
    // =========================================================================
    public static void showScores() {
        //QuizScores.show(input);
        System.out.println("==================================================");
        System.out.println("TO BE IMPLEMENTED QuizScores.java");
        System.out.println("==================================================");
    }

    // =========================================================================
    // METHOD 6: Exit
    // =========================================================================
    public static void exitGame(String playerName) {
        System.out.println("==================================================");
        System.out.println("Thank you for Playing Conquest, " + playerName + "!");
        System.out.println("==================================================");
        input.close();
        System.exit(0);
    }

    // =========================================================================
    // METHOD 7: Error Handler
    // =========================================================================
    public static void showError() {
        System.out.println("==================================================");
        System.out.println("[ERROR] Invalid Input.");
        System.out.println("==================================================");
    }
}