package ConquestPkg;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GeneralKnowledgeQuiz {
    private String playerName;

    // Question Bank: [Question, A, B, C, D, Answer]

    private String[][] questionBank = {

    {
        "Which planet in our solar system is the only one that rotates clockwise?",
        "Mars",
        "Venus",
        "Jupiter",
        "Mercury",
        "B"
    },
    {
        "What is the rarest natural blood type in humans?",
        "O Positive",
        "AB Negative",
        "A Positive",
        "B Negative",
        "B"
    },
    {
        "Which country has the most natural lakes in the world?",
        "United States",
        "Russia",
        "Canada",
        "Brazil",
        "C"
    },
    {
        "What is the only fruit that has its seeds on the outside?",
        "Apple",
        "Strawberry",
        "Banana",
        "Mango",
        "B"
    },
    {
        "Which chemical element has the symbol Ag?",
        "Gold",
        "Silver",
        "Aluminum",
        "Argon",
        "B"
    },
    {
        "What was the first soft drink ever consumed in space?",
        "Pepsi",
        "Sprite",
        "Coca-Cola",
        "Fanta",
        "C"
    },
    {
        "In which city would you find the Statue of Liberty?",
        "Washington, D.C.",
        "Los Angeles",
        "New York City",
        "Chicago",
        "C"
    },
    {
        "How many hearts does an octopus have?",
        "One",
        "Two",
        "Three",
        "Four",
        "C"
    },
    {
        "Which artist painted the \"Mona Lisa\"?",
        "Vincent van Gogh",
        "Pablo Picasso",
        "Leonardo da Vinci",
        "Michelangelo",
        "C"
    },
    {
        "What is the smallest country in the world by land area?",
        "Monaco",
        "Malta",
        "Vatican City",
        "San Marino",
        "C"
    },
    {
        "Which organ in the human body is capable of regenerating itself?",
        "Heart",
        "Brain",
        "Liver",
        "Kidney",
        "C"
    },
    {
        "What is the hardest natural substance on Earth?",
        "Iron",
        "Quartz",
        "Diamond",
        "Steel",
        "C"
    },
    {
        "In what year did the Titanic sink?",
        "1905",
        "1912",
        "1920",
        "1898",
        "B"
    },
    {
        "Which animal is known as the \"Ship of the Desert\"?",
        "Horse",
        "Camel",
        "Elephant",
        "Donkey",
        "B"
    },
    {
        "What is the national flower of Japan?",
        "Rose",
        "Lotus",
        "Cherry Blossom",
        "Tulip",
        "C"
    },
    {
        "Which gas do plants absorb from the atmosphere for photosynthesis?",
        "Oxygen",
        "Nitrogen",
        "Carbon dioxide",
        "Hydrogen",
        "C"
    },
    {
        "Who is credited with inventing the telephone?",
        "Thomas Edison",
        "Nikola Tesla",
        "Alexander Graham Bell",
        "Isaac Newton",
        "C"
    },
    {
        "What is the largest ocean on Earth?",
        "Atlantic Ocean",
        "Indian Ocean",
        "Arctic Ocean",
        "Pacific Ocean",
        "D"
    },
    {
        "Which country is home to the Kangaroo?",
        "South Africa",
        "Australia",
        "India",
        "Brazil",
        "B"
    },
    {
        "What is the capital city of France?",
        "Rome",
        "Berlin",
        "Madrid",
        "Paris",
        "D"
    },
    {
        "How many bones are in an adult human body?",
        "201",
        "206",
        "210",
        "196",
        "B"
    },
    {
        "Which desert is the largest hot desert in the world?",
        "Gobi Desert",
        "Arabian Desert",
        "Sahara Desert",
        "Kalahari Desert",
        "C"
    },
    {
        "Who wrote the play \"Romeo and Juliet\"?",
        "Charles Dickens",
        "William Shakespeare",
        "Jane Austen",
        "Mark Twain",
        "B"
    },
    {
        "What is the main ingredient in hummus?",
        "Lentils",
        "Chickpeas",
        "Beans",
        "Peas",
        "B"
    },
    {
        "Which planet is known as the \"Red Planet\"?",
        "Venus",
        "Mars",
        "Saturn",
        "Neptune",
        "B"
    },
    {
        "What is the currency used in the United Kingdom?",
        "Euro",
        "Dollar",
        "Pound Sterling",
        "Yen",
        "C"
    },
    {
        "Which bird is the fastest runner in the world?",
        "Eagle",
        "Falcon",
        "Ostrich",
        "Penguin",
        "C"
    },
    {
        "What is the largest internal organ in the human body?",
        "Heart",
        "Brain",
        "Liver",
        "Lungs",
        "C"
    },
    {
        "In which year did the first man land on the moon?",
        "1959",
        "1965",
        "1969",
        "1972",
        "C"
    },
    {
        "Which element makes up approximately 78% of Earth's atmosphere?",
        "Oxygen",
        "Carbon dioxide",
        "Nitrogen",
        "Hydrogen",
        "C"
    }
};

    public GeneralKnowledgeQuiz(String playerName){
        this.playerName = playerName;
    }

    public void start(){
        Scanner input = new Scanner(System.in);
        showIntro();

        if (!confirmStart(input)) return;

        String [][] selected = pickRandomQuestions(10);
        int score = runQuiz(input, selected);
        showResults(score);
        saveResults(score);
        returnPrompt(input);
    }

    private void showIntro() {
        System.out.println("Welcome to Conquest, " + playerName + "!");
        System.out.println();
        System.out.println("Fun Fact: This quiz covers global knowledge across Earth and beyond!");
        System.out.println();
        System.out.println("Would you like to start the quiz?");
        System.out.println("Category: General Knowledge");
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
            } else if (answer.equals("2")) {
                System.out.println("Returning to main menu...");
                return false;
            } else {
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
            if (askQuestion(input, i + 1, questions[i])) score++;
        }
        return score;
    }

    private boolean askQuestion(Scanner input, int number, String[] q){
        System.out.println("\nQuestion " + number + ": " + q[0]);
        System.out.println("A. " + q[1]);
        System.out.println("B. " + q[2]);
        System.out.println("C. " + q[3]);
        System.out.println("D. " + q[4]);

        String correctLetter = q[5];

        while (true) {
            System.out.print("Answer: ");
            String answer = input.nextLine().toUpperCase();

            if (answer.matches("[ABCD]")) {
                if (answer.equals(correctLetter)) {
                    System.out.println("Correct!");
                } else {
                    System.out.println("Incorrect! Answer: " + correctLetter);
                }
                System.out.println("==================================================");
                return answer.equals(correctLetter);
            } else {
                System.out.println("[ERROR] Invalid input");
            }
        }
    }

    private void showResults(int score) {
        System.out.println("\n==================================================");
        System.out.println("RESULTS:");
        System.out.println("Player: " + playerName);
        System.out.println("Category: General Knowledge");
        System.out.println("Score: " + score + "/10");
        System.out.println("Date: " + getFormattedDate());
        System.out.println("Time: " + getFormattedTime());
        System.out.println("==================================================");
    }

    private void saveResults(int score) {
        String entry =
            "==================================================\n" +
            "Player: " + playerName + "\n" +
            "Category: General Knowledge\n" +
            "Score: " + score + "\n" +
            "Date: " + getFormattedDate() + "\n" +
            "Time: " + getFormattedTime() + "\n" +
            "==================================================";

        try (FileWriter fw = new FileWriter("scores.txt", true)) {
            fw.write(entry + "\n");
        } catch (IOException e){
            System.out.println("[ERROR] Could not save results");
        }
    }

    private void returnPrompt(Scanner input) {
        while (true) {
            System.out.println("\nType 0 to go back to menu.");
            System.out.print("Answer: ");
            if (input.nextLine().equals("0")) return;
        }
    }

    private String getFormattedDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    private String getFormattedTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
    }
}
