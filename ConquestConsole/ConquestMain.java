import ConquestPkg.*;
import java.util.Scanner;

public class ConquestMain {

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        String playerName = getPlayerName();
        mainMenu(playerName);
    }

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
                    showScores(playerName);
                    break;
                case "3":
                    exitGame(playerName);
                    break;
                default:
                    showError();
            }
        }
    }

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

        if (quizChoice.equals("0")) {
            System.out.println("Returning to Main Menu...");
            System.out.println("==================================================");
            return;
        }

        launchQuiz(quizChoice, playerName);
    }

    public static void launchQuiz(String quizChoice, String playerName) {
        switch (quizChoice) {
            case "1":
                //GeographyQuiz.start(playerName, input);
                GeographyQuiz geographyQuiz = new GeographyQuiz(playerName);
                geographyQuiz.start();
                break;
            case "2":
                DrivingQuiz drivingQuiz = new DrivingQuiz(playerName);
                drivingQuiz.start();
                break;
            case "3":
                //GeneralKnowledgeQuiz.start(playerName, input);
                System.out.println("TO BE IMPLEMENTED GeneralKnowledgeQuiz.java");
                System.out.println("==================================================");
                break;
            case "4":
                //MediaQuiz.start(playerName, input);
                MediaQuiz mediaQuiz = new MediaQuiz(playerName);
                mediaQuiz.start();
                break;
            case "5":
                //ProgrammingQuiz.start(playerName, input);
                ProgrammingQuiz programmingQuiz = new ProgrammingQuiz(playerName);
                programmingQuiz.start();
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

    public static void showScores(String playerName) {
        QuizScores quizScores = new QuizScores(playerName);
        quizScores.display();
    }

    public static void exitGame(String playerName) {
        System.out.println("==================================================");
        System.out.println("Thank you for Playing Conquest, " + playerName + "!");
        System.out.println("==================================================");
        input.close();
        System.exit(0);
    }

    public static void showError() {
        System.out.println("==================================================");
        System.out.println("[ERROR] Invalid Input.");
        System.out.println("==================================================");
    }
}