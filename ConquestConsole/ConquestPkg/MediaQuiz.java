package ConquestPkg;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class MediaQuiz {

    private final String playerName;

    private final String[][] questionBank = {
        {"What is media?", "Hardware", "Communication tools", "Software only", "Internet only", "B"},
        {"Which is an example of media?", "Chair", "TV", "Table", "Pen", "B"},
        {"What is digital media?", "Printed books", "Online content", "Radio only", "Paper", "B"},
        {"Which is social media?", "Microsoft Word", "Facebook", "Notepad", "Excel", "B"},
        {"What is multimedia?", "One media type", "Combination of media", "Only video", "Only audio", "B"},
        {"Which is an audio format?", "MP3", "JPG", "PNG", "MP4", "A"},
        {"Which is an image format?", "MP3", "JPG", "MP4", "DOCX", "B"},
        {"Which is a video format?", "JPG", "MP4", "TXT", "PDF", "B"},
        {"What is streaming?", "Downloading", "Watching online without saving", "Editing", "Recording", "B"},
        {"What is resolution?", "Sound", "Image clarity", "File size", "Speed", "B"},
        {"What is editing?", "Recording", "Improving content", "Deleting", "Uploading", "B"},
        {"Which app is for video editing?", "Chrome", "CapCut", "Excel", "Notepad", "B"},
        {"What is content creation?", "Watching videos", "Making media content", "Downloading", "Sharing only", "B"},
        {"Which is a media device?", "Hammer", "Computer", "Chair", "Spoon", "B"},
        {"What is copyright?", "Free content", "Ownership of work", "Editing", "Sharing", "B"},
        {"What is a file format?", "Storage type", "File structure/type", "Folder", "App", "B"},
        {"Which is NOT media?", "Audio", "Video", "Text", "Tree", "D"},
        {"What is audio?", "Sound", "Image", "Video", "Text", "A"},
        {"What is video?", "Sound", "Moving images", "Text", "Code", "B"},
        {"What is image?", "Sound", "Visual picture", "Code", "Video", "B"},
        {"What is internet media?", "Offline files", "Online content", "Books", "Paper", "B"},
        {"Which platform is for videos?", "YouTube", "Excel", "Word", "Notepad", "A"},
        {"What is sharing?", "Keeping files", "Sending content to others", "Deleting", "Editing", "B"},
        {"What is download?", "Upload", "Save from internet", "Delete", "Share", "B"},
        {"What is upload?", "Download", "Send to internet", "Delete", "Copy", "B"},
        {"What is file size?", "Length", "Storage amount", "Width", "Height", "B"},
        {"What is bitrate?", "Speed of data", "Size", "Length", "Color", "A"},
        {"What is animation?", "Still image", "Moving graphics", "Sound", "Text", "B"},
        {"What is graphic design?", "Coding", "Visual creation", "Audio editing", "Networking", "B"},
        {"What is UI?", "User Interface", "Universal Input", "User Internet", "Unique Interface", "A"}
    };

    public MediaQuiz(String playerName) {
        this.playerName = playerName;
    }

    public void start() {
        Scanner input = new Scanner(System.in);

        showIntro();

        if (!confirmStart(input)) {
            return;
        }

        String[][] selectedQuestions = pickRandomQuestions(10);
        int score = runQuiz(input, selectedQuestions);

        showResults(score);
        saveResults(score);
        returnPrompt(input);
    }

    private void showIntro() {
        System.out.println("Welcome to Conquest, " + playerName + "!");
        System.out.println();
        System.out.println("Fun Fact: Media includes audio, video, images, and text used for communication.");
        System.out.println();
        System.out.println("Would you like to start the quiz?");
        System.out.println("Category: Media");
        System.out.println();
        System.out.println("1. Yes");
        System.out.println("2. No");
    }

    private boolean confirmStart(Scanner input) {
        while (true) {
            System.out.print("Answer: ");
            String answer = input.nextLine().trim();

            if (answer.equals("1")) {
                System.out.println("==================================================");
                return true;
            } else if (answer.equals("2")) {
                System.out.println("==================================================");
                System.out.println("Returning to main menu...");
                System.out.println("==================================================");
                return false;
            } else {
                System.out.println("[ERROR] Invalid Input");
            }
        }
    }

    private String[][] pickRandomQuestions(int numberOfQuestions) {
        ArrayList<Integer> indices = new ArrayList<>();

        for (int i = 0; i < questionBank.length; i++) {
            indices.add(i);
        }

        Collections.shuffle(indices);

        String[][] selectedQuestions = new String[numberOfQuestions][];

        for (int i = 0; i < numberOfQuestions; i++) {
            selectedQuestions[i] = questionBank[indices.get(i)];
        }

        return selectedQuestions;
    }

    private int runQuiz(Scanner input, String[][] questions) {
        int score = 0;

        for (int i = 0; i < questions.length; i++) {
            boolean isCorrect = askQuestion(input, i + 1, questions[i]);

            if (isCorrect) {
                score++;
            }
        }

        return score;
    }

    private boolean askQuestion(Scanner input, int questionNumber, String[] questionData) {
        System.out.println();
        System.out.println("Question " + questionNumber + ": " + questionData[0]);
        System.out.println();
        System.out.println("A. " + questionData[1]);
        System.out.println("B. " + questionData[2]);
        System.out.println("C. " + questionData[3]);
        System.out.println("D. " + questionData[4]);
        System.out.println();

        String correctAnswer = questionData[5];

        while (true) {
            System.out.print("Answer: ");
            String userAnswer = input.nextLine().trim().toUpperCase();

            if (userAnswer.equals("A") || userAnswer.equals("B")
                    || userAnswer.equals("C") || userAnswer.equals("D")) {

                if (userAnswer.equals(correctAnswer)) {
                    System.out.println(userAnswer + " is correct!");
                    System.out.println("==================================================");
                } else {
                    System.out.println(userAnswer + " is incorrect! The correct answer is " + correctAnswer + ".");
                    System.out.println("==================================================");
                }

                return userAnswer.equals(correctAnswer);
            } else {
                System.out.println("[ERROR] Invalid Input");
                System.out.println("==================================================");
            }
        }
    }

    private void showResults(int score) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("RESULTS:");
        System.out.println();
        System.out.println("Player:         " + playerName);
        System.out.println("Category:       Media");
        System.out.println("Score:          " + score + "/10");
        System.out.println("Date:           " + getFormattedDate());
        System.out.println("Time:           " + getFormattedTime());
        System.out.println("==================================================");
    }

    private void saveResults(int score) {
        String entry =
            "==================================================\n" +
            "Player:        " + playerName + "\n" +
            "Category:      Media\n" +
            "Score:         " + score + "\n" +
            "Date:          " + getFormattedDate() + "\n" +
            "Time:          " + getFormattedTime() + "\n" +
            "==================================================\n";

        try (FileWriter fileWriter = new FileWriter("scores.txt", true)) {
            fileWriter.write(entry);
            System.out.println("Results saved to scores.txt.");
        } catch (IOException e) {
            System.out.println("[ERROR] Could not save results: " + e.getMessage());
        }
    }

    private void returnPrompt(Scanner input) {
        while (true) {
            System.out.println();
            System.out.println("Type 0 to go back to the main menu.");
            System.out.print("Answer: ");
            String answer = input.nextLine().trim();

            if (answer.equals("0")) {
                System.out.println("==================================================");
                return;
            } else {
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