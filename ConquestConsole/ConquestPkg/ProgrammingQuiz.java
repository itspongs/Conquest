package ConquestPkg;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ProgrammingQuiz {

    private final String playerName;

    private final String[][] questionBank = {
        {
            "What does JVM stand for?",
            "Java Variable Machine",
            "Java Virtual Machine",
            "Java Verified Method",
            "Java Visual Model",
            "B"
        },
        {
            "Which keyword is used to define a class in Java?",
            "new",
            "class",
            "void",
            "static",
            "B"
        },
        {
            "Which symbol is used to end a Java statement?",
            ".",
            ",",
            ";",
            ":",
            "C"
        },
        {
            "Which data type is used for whole numbers?",
            "double",
            "int",
            "String",
            "boolean",
            "B"
        },
        {
            "Which data type is used for decimal numbers?",
            "char",
            "boolean",
            "int",
            "double",
            "D"
        },
        {
            "Which method is the entry point of a Java application?",
            "start()",
            "run()",
            "main()",
            "execute()",
            "C"
        },
        {
            "What is the correct way to print text in Java?",
            "print.out.println(\"Hello\");",
            "System.out.println(\"Hello\");",
            "println.out.system(\"Hello\");",
            "System.print.out(\"Hello\");",
            "B"
        },
        {
            "Which operator is used for addition in Java?",
            "+",
            "-",
            "*",
            "/",
            "A"
        },
        {
            "What will happen if you divide a number by zero in Java using integers?",
            "The program will print 0",
            "The program will ignore it",
            "A runtime error will occur",
            "A logical error will occur only",
            "C"
        },
        {
            "Which class is commonly used for user input in console programs?",
            "Printer",
            "InputReader",
            "Scanner",
            "ConsoleReader",
            "C"
        },
        {
            "Which package must be imported to use Scanner?",
            "java.io",
            "java.lang",
            "java.text",
            "java.util",
            "D"
        },
        {
            "What is the correct declaration of a String variable?",
            "string name;",
            "String name;",
            "text name;",
            "char name;",
            "B"
        },
        {
            "Which loop is best used when the number of repetitions is known?",
            "for loop",
            "while loop",
            "do-while loop",
            "if statement",
            "A"
        },
        {
            "Which conditional statement is used for multiple exact choices?",
            "if",
            "if-else",
            "switch",
            "for",
            "C"
        },
        {
            "What is the value of 5 + 3 * 2?",
            "16",
            "11",
            "13",
            "10",
            "B"
        },
        {
            "Which of the following is a valid boolean value?",
            "yes",
            "1",
            "true",
            "T",
            "C"
        },
        {
            "What does an array store?",
            "Only one value",
            "A fixed number of values of the same type",
            "Only text values",
            "Only decimal values",
            "B"
        },
        {
            "What is the first index of an array in Java?",
            "1",
            "0",
            "-1",
            "Depends on the array",
            "B"
        },
        {
            "Which keyword is used to create an object?",
            "create",
            "class",
            "new",
            "object",
            "C"
        },
        {
            "What is the purpose of an if statement?",
            "To repeat code",
            "To compare values and make a decision",
            "To store data",
            "To import packages",
            "B"
        },
        {
            "Which of the following is a compile-time error?",
            "Dividing by zero",
            "Missing semicolon",
            "Wrong output",
            "Infinite loop during execution",
            "B"
        },
        {
            "Which loop always runs at least once?",
            "for",
            "while",
            "do-while",
            "foreach",
            "C"
        },
        {
            "What does the final keyword usually mean for a variable?",
            "It can be changed anytime",
            "It is temporary",
            "It cannot be changed after assignment",
            "It is automatically deleted",
            "C"
        },
        {
            "Which access modifier makes a member accessible only inside its class?",
            "public",
            "protected",
            "default",
            "private",
            "D"
        },
        {
            "Which keyword is used to return a value from a method?",
            "break",
            "return",
            "print",
            "void",
            "B"
        },
        {
            "Which symbol is used for single-line comments in Java?",
            "/*",
            "#",
            "//",
            "<!--",
            "C"
        },
        {
            "What is concatenation in Java?",
            "Subtracting numbers",
            "Joining values together",
            "Declaring variables",
            "Comparing strings",
            "B"
        },
        {
            "Which data type stores a single character?",
            "String",
            "char",
            "boolean",
            "int",
            "B"
        },
        {
            "What is the correct operator for 'equal to' in Java?",
            "=",
            "==",
            "!=",
            "<=",
            "B"
        },
        {
            "What is recursion?",
            "A loop that cannot stop",
            "A method calling itself",
            "A variable inside a class",
            "A type of array",
            "B"
        }
    };

    public ProgrammingQuiz(String playerName) {
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
        System.out.println("Fun Fact: Java follows the rule \"write once, run anywhere\" through the JVM.");
        System.out.println();
        System.out.println("Would you like to start the quiz?");
        System.out.println("Category: Programming");
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

            if (userAnswer.equals("A") || userAnswer.equals("B") || userAnswer.equals("C") || userAnswer.equals("D")) {
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
        System.out.println("Category:       Programming");
        System.out.println("Score:          " + score + "/ 10");
        System.out.println("Date:           " + getFormattedDate());
        System.out.println("Time:           " + getFormattedTime());
        System.out.println("==================================================");
    }

    private void saveResults(int score) {
        String entry =
            "==================================================\n" +
            "Player:        " + playerName + "\n" +
            "Category:      Programming\n" +
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