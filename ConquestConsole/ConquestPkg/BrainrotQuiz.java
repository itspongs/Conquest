package ConquestPkg;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class BrainrotQuiz {
    private String playerName;
    private String[][] questionBank = {

    // Question 1
    {
        "Why was 6 afraid of 7?",
        "Because 7 scared 6",
        "Because 7 threatened the numbers",
        "Because 7 ate 9",
        "Because 7 played Fortnite",
        "C"
    },

    // Question 2
    {
        "What quote would you say if you are \"focused\"?",
        "Let's focus",
        "Let's remove distractions",
        "Let's do this now",
        "Let's lock in",
        "D"
    },

    // Question 3
    {
        "Where did brainrot images originate from?",
        "AI generation",
        "Human art",
        "Philippines",
        "YouTube Kids",
        "A"
    },

    // Question 4
    {
        "What do you call a slang to call someone's bottom \"big\"?",
        "GYAT",
        "FYP",
        "L.H.O.O.Q",
        "WOWOWIN",
        "A"
    },

    // Question 5
    {
        "Which social media is the primary source of brainrots?",
        "Facebook",
        "Instagram",
        "YouTube",
        "All of the above",
        "D"
    },

    // Question 6
    {
        "Who is Ballerina Cappuccina's love interest?",
        "Cappuccino Assassino",
        "Crocodilo Bombardino",
        "Tralalelo Tralala",
        "Tung Tung Sahur",
        "D"
    },

    // Question 7
    {
        "Who are the target audiences for Brainrots?",
        "Kids",
        "Teens",
        "Adults",
        "Seniors",
        "A"
    },

    // Question 8
    {
        "Why do Adults find it difficult to understand Brainrots?",
        "Because it's not their type",
        "Because kids make them",
        "Because they lack the proper context and meaning",
        "Because it's just random words or pictures",
        "C"
    },

    // Question 9
    {
        "What was one of the earliest Brainrots?",
        "Bum",
        "Italian Brainrot",
        "Sigma",
        "Skibidi Toilet",
        "C"
    },

    // Question 10
    {
        "Which one of these games was heavily influenced by Brainrot?",
        "Minecraft",
        "Roblox",
        "Valorant",
        "Project Sekai",
        "B"
    },

    // Question 11
    {
        "What do you call someone when they assume someone likes them or they are better?",
        "Sigma",
        "Ohio",
        "Delulu",
        "Rizz",
        "C"
    },

    // Question 12
    {
        "What does \"Rizz\" mean?",
        "Fighting skill",
        "Charisma",
        "Intelligence",
        "Luck",
        "B"
    },

    // Question 13
    {
        "What does \"NPC\" stand for?",
        "Non Player Character",
        "New Player Code",
        "Normal Person Code",
        "Network Player Control",
        "A"
    },

    // Question 14
    {
        "What does \"Sus\" mean?",
        "Funny",
        "Suspicious",
        "Smart",
        "Loud",
        "B"
    },

    // Question 15
    {
        "What does \"W\" mean?",
        "Wrong",
        "Win",
        "Weak",
        "Wild",
        "B"
    },

    // Question 16
    {
        "What does \"L\" mean?",
        "Loss",
        "Loud",
        "Lucky",
        "Late",
        "A"
    },

    // Question 17
    {
        "What is a Sigma?",
        "Leader type",
        "Lone, independent person",
        "Weak person",
        "Follower",
        "B"
    },

    // Question 18
    {
        "What does Ohio mean?",
        "A real place only",
        "Normal situation",
        "Weird/chaotic situation",
        "Boring content",
        "C"
    },

    // Question 19
    {
        "What is Fanum tax?",
        "Paying money",
        "Taking food from others",
        "Gaming tax",
        "Internet fee",
        "B"
    },

    // Question 20
    {
        "What does Gyatt express?",
        "Anger",
        "Surprise/attraction",
        "Fear",
        "Confusion",
        "B"
    },

    // Question 21
    {
        "What is YouTube's short video feature?",
        "YouTube Clips",
        "YouTube Shorts",
        "YouTube Mini",
        "YouTube Quick",
        "B"
    },

    // Question 22
    {
        "Instagram's short video feature is called:",
        "Stories",
        "Reels",
        "Clips",
        "Shorts",
        "B"
    },

    // Question 23
    {
        "Brainrot content is usually:",
        "Long videos",
        "Short videos",
        "Text only",
        "Audio only",
        "B"
    },

    // Question 24
    {
        "Why are short videos effective?",
        "Hard to watch",
        "Quick and addictive",
        "Expensive",
        "Slow",
        "B"
    },

    // Question 25
    {
        "What series made \"skibidi\" popular?",
        "Spongebob",
        "Skibidi Toilet",
        "Naruto",
        "Minecraft",
        "B"
    },

    // Question 26
    {
        "What is often in the background of Brainrot videos?",
        "News clips",
        "Subway Surfers gameplay",
        "Movies",
        "Sports",
        "B"
    },

    // Question 27
    {
        "Why is Minecraft parkour also used as background for Brainrot videos?",
        "Educational",
        "Keeps attention",
        "Serious content",
        "Slow gameplay",
        "B"
    },

    // Question 28
    {
        "What Minecraft content is used?",
        "Redstone tutorials",
        "Parkour gameplay",
        "Story mode",
        "Mods",
        "B"
    },

    // Question 29
    {
        "What is an NPC livestream?",
        "Normal livestream",
        "Repetitive scripted reactions",
        "Gaming stream",
        "Music stream",
        "B"
    },

    // Question 30
    {
        "Brainrot audio is usually:",
        "Quiet",
        "Random and loud",
        "Classical",
        "Lyrical",
        "B"
    },

    // Question 31
    {
        "Bass boosted means:",
        "Lower volume",
        "Stronger bass sound",
        "No sound",
        "Slower audio",
        "B"
    },

    // Question 32
    {
        "Why repeat sounds?",
        "Make it boring",
        "Make it memorable",
        "Save time",
        "Remove sound",
        "B"
    },

    // Question 33
    {
        "What is remix audio?",
        "Original audio",
        "Remove sound",
        "Edited/combined audio",
        "Background noise",
        "C"
    },

    // Question 34
    {
        "What does \"Earworm\" mean?",
        "Loud speaker",
        "Quiet music",
        "Broken sound",
        "Song stuck in your head",
        "D"
    },

    // Question 35
    {
        "Brainrot videos are:",
        "Fast paced",
        "Slow",
        "Silent",
        "Long",
        "A"
    },

    // Question 36
    {
        "Why use fast cuts?",
        "Keep attention",
        "Confuse viewers",
        "Waste time",
        "Save memory",
        "A"
    },

    // Question 37
    {
        "What does \"Overstimulation\" mean?",
        "Too calm",
        "Too much input at once",
        "No content",
        "Slow content",
        "B"
    },

    // Question 38
    {
        "Brainrot affects attention by:",
        "Increasing focus",
        "Removing memory",
        "Shortening attention span",
        "Slowing thinking",
        "C"
    },

    // Question 39
    {
        "Why is Brainrot so addictive?",
        "Boring",
        "Quick dopamine hits",
        "Expensive",
        "Long",
        "B"
    },

    // Question 40
    {
        "In Brainrot, what is AI usually used for?",
        "Generate images",
        "Generate voice overs",
        "Generate AI stories",
        "All of the above",
        "D"
    },

    // Question 41
    {
        "AI voiceovers are used to:",
        "Replace humans",
        "Slow videos",
        "Remove sound",
        "Add humor/content",
        "D"
    },

    // Question 42
    {
        "What does \"Deepfried\" edit mean?",
        "Over-edited image/video",
        "Cooking",
        "Clean image",
        "HD video",
        "A"
    },

    // Question 43
    {
        "Random clips are combined to:",
        "Make sense",
        "Be serious",
        "Create chaos humor",
        "Educate",
        "C"
    },

    // Question 44
    {
        "Absurd humor is:",
        "Random and nonsense",
        "Logical",
        "Educational",
        "Serious",
        "A"
    },

    // Question 45
    {
        "\"Only in Ohio\" implies:",
        "Normal event",
        "Boring event",
        "Strange situation",
        "School",
        "C"
    },

    // Question 46
    {
        "A Sigma edit shows:",
        "Weakness",
        "Fear",
        "Confusion",
        "Independence/coolness",
        "D"
    },

    // Question 47
    {
        "Brainrot videos make no sense because:",
        "Intentional humor",
        "Error",
        "Mistake",
        "No editing",
        "A"
    },

    // Question 48
    {
        "A meme becomes brainrot when:",
        "It disappears",
        "It slows down",
        "It is deleted",
        "It becomes repetitive/addictive",
        "D"
    },

    // Question 49
    {
        "People repeat Brainrot phrases because:",
        "They stick in the brain",
        "Forgetting",
        "Required",
        "Rules",
        "A"
    },

    // Question 50
    {
        "Brainrot can make studying feel:",
        "Easier",
        "More exciting",
        "More boring",
        "Faster",
        "C"
    }

};

    public BrainrotQuiz(String playerName){
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
        System.out.println("Welcome to Conquest, " + playerName + "!");
        System.out.println();
        System.out.println("Fun Fact: The term \"Brainrot\" refers to the mental state caused by consuming excessive amounts of low-quality internet content!");
        System.out.println();
        System.out.println("Would you like to start the quiz?");
        System.out.println("Category: Brainrot");
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
        System.out.println("Category:       Brainrot");
        System.out.println("Score:          " + score + "/ 10");
        System.out.println("Date:           " + getFormattedDate());
        System.out.println("Time:           " + getFormattedTime());
        System.out.println("==================================================");
    }

    private void saveResults(int score) {
        String entry =
            "==================================================\n" +
            "Player:        " + playerName + "\n" +
            "Category:      Brainrot" + "\n" +
            "Score:         " + score + "\n" +
            "Date:          " + getFormattedDate() + "\n" +
            "Time:          " + getFormattedTime() + "\n" +
            "==================================================";

        try (FileWriter fw = new FileWriter("scores.txt", true)) {
            fw.write(entry + "\n");
            System.out.println("Results saved to scores.txt.");
        } catch (IOException e){
            System.out.println("[ERROR] Could not save results: " + e.getMessage());
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