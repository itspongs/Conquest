package ConquestPkg;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GeographyQuiz {
    private String playerName;
    private String[][] questionBank = {
 
    // Question 1
    {
        "What is the largest continent by land area?",
        "Africa",
        "Asia",
        "North America",
        "Antarctica",
        "B"
    },
 
    // Question 2
    {
        "Which river is traditionally considered the longest in the world?",
        "Amazon",
        "Nile",
        "Yangtze",
        "Mississippi",
        "B"
    },
 
    // Question 3
    {
        "Mount Everest is located in which mountain range?",
        "Himalayas",
        "Andes",
        "Rockies",
        "Alps",
        "A"
    },
 
    // Question 4
    {
        "Which country has the largest population in the world?",
        "USA",
        "India",
        "China",
        "Indonesia",
        "B"
    },
 
    // Question 5
    {
        "What is the capital of Australia?",
        "Sydney",
        "Melbourne",
        "Canberra",
        "Perth",
        "C"
    },
 
    // Question 6
    {
        "Which is the largest ocean on Earth?",
        "Atlantic Ocean",
        "Indian Ocean",
        "Pacific Ocean",
        "Arctic Ocean",
        "C"
    },
 
    // Question 7
    {
        "In which country is the Great Barrier Reef located?",
        "Australia",
        "Fiji",
        "Brazil",
        "Indonesia",
        "A"
    },
 
    // Question 8
    {
        "What is the smallest country in the world by land area?",
        "Vatican City",
        "Monaco",
        "Nauru",
        "San Marino",
        "A"
    },
 
    // Question 9
    {
        "The Sahara Desert is located on which continent?",
        "Asia",
        "Africa",
        "South America",
        "Australia",
        "B"
    },
 
    // Question 10
    {
        "Which city is known as the \"City of Canals\"?",
        "Amsterdam",
        "Venice",
        "Paris",
        "Bangkok",
        "B"
    },
 
    // Question 11
    {
        "What is the capital of Japan?",
        "Tokyo",
        "Osaka",
        "Kyoto",
        "Seoul",
        "A"
    },
 
    // Question 12
    {
        "Which of these is the deepest point in the world's oceans?",
        "Java Trench",
        "Mariana Trench",
        "Puerto Rico Trench",
        "Tonga Trench",
        "B"
    },
 
    // Question 13
    {
        "Which country is also a continent?",
        "Greenland",
        "Iceland",
        "Australia",
        "Madagascar",
        "C"
    },
 
    // Question 14
    {
        "What is the capital of Canada?",
        "Toronto",
        "Vancouver",
        "Ottawa",
        "Montreal",
        "C"
    },
 
    // Question 15
    {
        "Which line of latitude divides the Earth into the Northern and Southern Hemispheres?",
        "Equator",
        "Prime Meridian",
        "Tropic of Cancer",
        "Tropic of Capricorn",
        "A"
    },
 
    // Question 16
    {
        "Which European country is shaped like a boot?",
        "Greece",
        "Italy",
        "Spain",
        "Portugal",
        "B"
    },
 
    // Question 17
    {
        "What is the largest desert in the world (including polar deserts)?",
        "Antarctica",
        "Sahara",
        "Gobi",
        "Arabian",
        "A"
    },
 
    // Question 18
    {
        "In which country would you find the ancient city of Petra?",
        "Egypt",
        "Jordan",
        "Turkey",
        "Iraq",
        "B"
    },
 
    // Question 19
    {
        "What is the capital of France?",
        "Lyon",
        "Marseille",
        "Paris",
        "Bordeaux",
        "C"
    },
 
    // Question 20
    {
        "Which river flows through the Grand Canyon?",
        "Colorado River",
        "Rio Grande",
        "Missouri River",
        "Yukon River",
        "A"
    },
 
    // Question 21
    {
        "Which country has the most natural lakes?",
        "Canada",
        "Russia",
        "USA",
        "Finland",
        "A"
    },
 
    // Question 22
    {
        "What is the capital of Brazil?",
        "Rio de Janeiro",
        "Sao Paulo",
        "Brasilia",
        "Salvador",
        "C"
    },
 
    // Question 23
    {
        "Which mountain is the highest in Africa?",
        "Mount Kilimanjaro",
        "Mount Kenya",
        "Mount Sinai",
        "Atlas Mountains",
        "A"
    },
 
    // Question 24
    {
        "Which sea separates Europe from Africa?",
        "Red Sea",
        "Mediterranean Sea",
        "Black Sea",
        "Caspian Sea",
        "B"
    },
 
    // Question 25
    {
        "What is the largest island in the world?",
        "Australia",
        "Borneo",
        "Greenland",
        "New Guinea",
        "C"
    },
 
    // Question 26
    {
        "Which country is known as the \"Land of the Rising Sun\"?",
        "Japan",
        "China",
        "Thailand",
        "South Korea",
        "A"
    },
 
    // Question 27
    {
        "What is the capital of Egypt?",
        "Cairo",
        "Alexandria",
        "Giza",
        "Luxor",
        "A"
    },
 
    // Question 28
    {
        "In which ocean is the island of Madagascar located?",
        "Atlantic",
        "Pacific",
        "Indian",
        "Arctic",
        "C"
    },
 
    // Question 29
    {
        "What is the official language of Brazil?",
        "Spanish",
        "Portuguese",
        "English",
        "French",
        "B"
    }, 
 
    // Question 30
    {
        "Which U.S. state is the largest by land area?",
        "Alaska",
        "Texas",
        "California",
        "Montana",
        "A"
    },

    // Question 31
    {
        "Which canal connects the Atlantic and Pacific Oceans?",
        "Suez Canal",
        "Panama Canal",
        "Erie Canal",
        "Kiel Canal",
        "B"
    },

    // Question 32
    {
        "What is the capital of Germany?",
        "Munich",
        "Frankfurt",
        "Berlin",
        "Hamburg",
        "C"
    },

    // Question 33
    {
        "Which mountain range runs along the western coast of South America?",
        "Andes",
        "Rockies",
        "Appalachians",
        "Urals",
        "A"
    },

    // Question 34
    {
        "What is the largest country in the world by land area?",
        "Russia",
        "Canada",
        "China",
        "USA",
        "A"
    },

    // Question 35
    {
        "Which city is the capital of Thailand?",
        "Phuket",
        "Bangkok",
        "Chiang Mai",
        "Pattaya",
        "B"
    },

    // Question 36
    {
        "Which of the Great Lakes is the largest by surface area?",
        "Lake Superior",
        "Lake Michigan",
        "Lake Huron",
        "Lake Erie",
        "A"
    },

    // Question 37
    {
        "What is the capital of Italy?",
        "Milan",
        "Florence",
        "Rome",
        "Naples",
        "C"
    },

    // Question 38
    {
        "Which country is famous for the Eiffel Tower?",
        "Italy",
        "France",
        "Spain",
        "United Kingdom",
        "B"
    },

    // Question 39
    {
        "What is the capital of Russia?",
        "Moscow",
        "Saint Petersburg",
        "Novosibirsk",
        "Kiev",
        "A"
    },

    // Question 40
    {
        "Which desert is the driest place on Earth?",
        "Atacama Desert",
        "Sahara Desert",
        "Kalahari Desert",
        "Mojave Desert",
        "A"
    },

    // Question 41
    {
        "What is the capital of Spain?",
        "Madrid",
        "Barcelona",
        "Seville",
        "Valencia",
        "A"
    },

    // Question 42
    {
        "Which country does the Rhine River primarily flow through?",
        "Germany",
        "France",
        "Italy",
        "Poland",
        "A"
    },

    // Question 43
    {
        "In which city is the Burj Khalifa located?",
        "Riyadh",
        "Doha",
        "Dubai",
        "Abu Dhabi",
        "C"
    },

    // Question 44
    {
        "What is the capital of India?",
        "Mumbai",
        "New Delhi",
        "Bangalore",
        "Kolkata",
        "B"
    },

    // Question 45
    {
        "Which of these countries is not in Europe?",
        "Poland",
        "Austria",
        "Thailand",
        "Belgium",
        "C"
    },

    // Question 46
    {
        "What is the capital of Argentina?",
        "Buenos Aires",
        "Santiago",
        "Lima",
        "Bogota",
        "A"
    },

    // Question 47
    {
        "Which volcano destroyed Pompeii in 79 AD?",
        "Mount Etna",
        "Mount Vesuvius",
        "Mount St. Helens",
        "Krakatoa",
        "B"
    },

    // Question 48
    {
        "What is the capital of South Korea?",
        "Busan",
        "Incheon",
        "Seoul",
        "Pyongyang",
        "C"
    },

    // Question 49
    {
        "The Danube River flows into which sea?",
        "Mediterranean Sea",
        "Caspian Sea",
        "Black Sea",
        "North Sea",
        "C"
    },

    // Question 50
    {
        "What is the capital of the United Kingdom?",
        "London",
        "Edinburgh",
        "Cardiff",
        "Belfast",
        "A"
    }

};

    public GeographyQuiz(String playerName){
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
        System.out.println("Fun Fact: Russia spans 11 time zones, making it the country with the most time zones in the world!");
        System.out.println();
        System.out.println("Would you like to start the quiz?");
        System.out.println("Category: Geography");
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
        System.out.println("Category:       Geography");
        System.out.println("Score:          " + score + "/ 10");
        System.out.println("Date:           " + getFormattedDate());
        System.out.println("Time:           " + getFormattedTime());
        System.out.println("==================================================");
    }

    private void saveResults(int score) {
        String entry =
            "==================================================\n" +
            "Player:        " + playerName + "\n" +
            "Category:      Geography" + "\n" +
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