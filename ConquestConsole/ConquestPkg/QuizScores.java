package ConquestPkg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class QuizScores {
    private String playerName;

    private class ScoreRecord {
        String player;
        String category;
        String score;
        String date;
        String time;

        ScoreRecord(String player, String category, String score, String date, String time) {
            this.player = player;
            this.category = category;
            this.score = score;
            this.date = date;
            this.time = time;
        }
    }

    public QuizScores(String playerName){
        this.playerName = playerName;
    }

    public void display() {
        Scanner input = new Scanner(System.in);
        ScoreRecord[] records = loadScores();

        showHeader();
        showScores(records);
        returnPrompt(input);
    }

    private void showHeader() {
        System.out.println("==================================================");
        System.out.println("Welcome to Conquest, " + playerName + "!");
        System.out.println();
        System.out.println("Here are your scores from your past quizzes!");
        System.out.println();
        System.out.printf("%-4s %-15s %-20s %-10s %-12s %-10s%n", "#", "PLAYER:", "CATEGORY:", "SCORE:", "DATE:", "TIME:");
    }
    
    private void showScores(ScoreRecord[] records) {
        if (records.length == 0) {
            System.out.println("No scores found. Play a quiz to record your first score!");
        }
        else {
            for (int i = 0; i < records.length; i++) {
                ScoreRecord r = records[i];
                System.out.printf("%-4s %-15s %-20s %-10s %-12s %-10s%n",
                    (i + 1) + ".",
                    r.player,
                    r.category,
                    r.score,
                    r.date,
                    r.time);
                    
            }
        }
        System.out.println();
        System.out.println("==================================================");
    }

    private ScoreRecord[] loadScores() {
        ArrayList<ScoreRecord> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("scores.txt"))){
            String line;

            //TEMPORARY HOLDERS FOR ONE RECORD
            String tempPlayer = "";
            String tempCategory = "";
            String tempScore = "";
            String tempDate = "";
            String tempTime = "";

            //FORMATTING SCORES.TXT TO QUIZSCORES
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("Player:")) {
                    tempPlayer = extractValue(line, "Player:");
                }
                else if (line.startsWith("Category:")) {
                    tempCategory = extractValue(line, "Category:");
                }
                else if (line.startsWith("Score:")){
                    String raw = extractValue(line, "Score:");
                    tempScore = raw.replace(" ","");
                }
                else if (line.startsWith("Date:")){
                    tempDate = extractValue(line, "Date:");
                }
                else if (line.startsWith("Time")){
                    tempTime = extractValue(line, "Time:");

                    //RESET TEMPORARIES
                    list.add(new ScoreRecord(tempPlayer, tempCategory, tempScore, tempDate, tempTime));
                    tempPlayer = tempCategory = tempScore = tempDate = tempTime = "";
                }
            }
        }
        catch (IOException e) {
            System.out.println("[INFO] scores.txt not found or could not be read.");
            System.out.println("Play a quiz first to generate score history.");
            System.out.println();
        }

        ScoreRecord[] records = new ScoreRecord[list.size()];
        list.toArray(records);
        return records;
    }

    private String extractValue(String line, String label) {
        return line.substring(label.length()).trim();
    }

    private void returnPrompt(Scanner input) {
        while (true) { 
            System.out.println("Type 0 to go back to Main Menu.");
            System.out.println("Number: ");
            String answer = input.nextLine().trim();

            if (answer.equals("0")) {
                System.out.println("==================================================");
                return;
            }
            else {
                System.out.println("[ERROR] Invalid Input.");
                System.out.println();
            }
        }
    }
}
