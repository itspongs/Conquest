package ConquestPkg;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class DrivingQuiz {

    private String playerName;

    //Question Bank: [Question: Choice A, Choice B, Choice C, Choice D, Answer]

    private String[][] questionBank = {
 
    // Question 1
    {
        "A sixty year old senior citizen files an application for a driver's license. Is this allowed?",
        "Yes",
        "No",
        "It depends if the applicant will pass the medical, theoretical and practical examination",
        "No, because applicants above 60 years old are no longer allowed to apply for a driver's license.",
        "C"
    },
 
    // Question 2
    {
        "You are driving on an expressway. You may stop only:",
        "To load or unload goods or passengers",
        "To take a short rest if you feel slightly tired",
        "On the lay-by to attend or call for an emergency",
        "To ask for directions",
        "C"
    },
 
    // Question 3
    {
        "You are planning a long trip. Do you need to plan rest stops?",
        "No, you will arrive to your destination faster",
        "No, only fuel stops will be needed",
        "Yes, but only if you are traveling at night",
        "Yes, regular stops help avoid mental and physical fatigue",
        "D"
    },
 
    // Question 4
    {
        "During period of illness, your ability to drive may be impaired. You must:",
        "Only take smaller doses of any medicine at all times to ensure safety",
        "See your doctor each time before you drive, especially for long trips",
        "Ignore minor illnesses and continue driving as long as you feel okay",
        "Be physically and mentally fit and do not drive after taking medicine",
        "D"
    },
 
    // Question 5
    {
        "Under R.A. 10666 also known as Children's Safety on Motorcycle Act, the child must grasp the rider's waist, can step and rest his/her feet comfortably on the foot peg, and ________.",
        "he/she must be at least five (5) feet in height",
        "he/she must wear standard protective helmet",
        "he/she must have parent's consent",
        "he/she must be accompanied by another adult rider on a separate motorcycle",
        "B"
    },
 
    // Question 6
    {
        "A blinking yellow traffic light means?",
        "Slow down and proceed if there is no danger",
        "Wait for the green light",
        "Stop and wait for the traffic light",
        "Speed up to clear the intersection quickly",
        "A"
    },
 
    // Question 7
    {
        "When do you need to check your engine oil level?",
        "When the engine is hot",
        "When the engine is running",
        "When the engine is cold",
        "Only when the oil warning light turns on",
        "C"
    },
 
    // Question 8
    {
        "Yellow box pavement marking is painted with intersections where no vehicle is allowed to _______?",
        "Pass",
        "Blow horn",
        "Stop",
        "Turn left",
        "C"
    },
 
    // Question 9
    {
        "What should the driver do if he/she is already within the intersection when the yellow traffic light comes?",
        "Slow down to allow other vehicles to cross",
        "Stop automatically on the road",
        "Reverse immediately to exit the intersection",
        "The driver may continue driving with caution",
        "D"
    },
 
    // Question 10
    {
        "Why are rumble strips installed across the road?",
        "To help you keep the correct separation distance",
        "To help you choose the correct lane",
        "To make you alert and aware of your speed",
        "To improve tire traction during rainy weather",
        "C"
    },
 
    // Question 11
    {
        "Which one is a mandate of LTO?",
        "Issue certificate of emission compliance",
        "Register roadworthy and emission complaint motor vehicles",
        "Issue certificate of public convenience",
        "Regulate public transportation routes",
        "B"
    },
 
    // Question 12
    {
        "How does alcohol affect you?",
        "It speeds up your reaction",
        "It improves your coordination",
        "Provide national passports for drivers",
        "It reduces your concentration",
        "D"
    },
 
    // Question 13
    {
        "What is the meaning of \"beating the red light\"?",
        "Driving faster upon seeing a yellow/amber light",
        "Stopping on the green light",
        "Stopping suddenly",
        "Turning on hazard lights before crossing the intersection",
        "A"
    },
 
    // Question 14
    {
        "Under R.A. 10666, which of the following prohibits the rider to convey a child?",
        "Where a speed limit of more than 40 kph is imposed",
        "Where a speed limit of more than 50 kph is imposed",
        "Where a speed limit of more than 60 kph is imposed",
        "Where a speed limit of more than 80 kph is imposed",
        "C"
    },
 
    // Question 15
    {
        "What should you do whenever you are driving on a highway with a lot of potholes?",
        "Increase speed",
        "Decrease speed",
        "Always change lane",
        "Maintain the same speed",
        "B"
    },
 
    // Question 16
    {
        "Using a backbone motorcycle, which stand do you need to use when parking overnight?",
        "Side stand",
        "Cross stand",
        "Center stand",
        "Kick stand",
        "C"
    },
 
    // Question 17
    {
        "Ahead of you is a vehicle with a flashing yellow light. This means it is:",
        "Broken down vehicle",
        "Slow moving",
        "A doctor's car",
        "An emergency vehicle",
        "B"
    },
 
    // Question 18
    {
        "What should you do if the road is wet?",
        "Increase your speed",
        "Keep on swerving",
        "Slow down",
        "Use your hazard lights",
        "C"
    },
 
    // Question 19
    {
        "Which of the traffic lights requires you to prepare for a stop?",
        "Yellow/Amber",
        "Green",
        "Red",
        "Flashing Red",
        "A"
    },
 
    // Question 20
    {
        "What is the authority granted by LTO to a person who desires to learn to operate a motor vehicle valid for a period of one year?",
        "Non-Professional Driver's License",
        "Professional Driver's License",
        "Provisional Driver's License",
        "Student Driver's Permit",
        "D"
    },
 
    // Question 21
    {
        "Signs that are round, inverted triangle or octagonal and with red colored border are called:",
        "Regulatory signs",
        "Warning signs",
        "Informative signs",
        "Prohibitory signs",
        "A"
    },
 
    // Question 22
    {
        "You arrive at a crossroad. You want to turn left, and you have a green arrow light. Can you proceed?",
        "Yes, you can turn left, as this is a protected turn where no other cars are allowed to enter the intersection",
        "No, you cannot turn left unless you have a green left pointing arrow",
        "Yes, you can turn left, however you may still need to yield to pedestrians and incoming traffic",
        "No, you must wait for the regular green light before turning left",
        "A"
    },
 
    // Question 23
    {
        "What should be done first before changing lanes?",
        "Make signal",
        "Check the traffic ahead",
        "Check mirrors for traffic behind",
        "Increase your speed",
        "A"
    },
 
    // Question 24
    {
        "When is it legal to use the shoulder of a road when overtaking another vehicle?",
        "When the vehicle in front of you is making a right turn",
        "When the vehicle in front of you is visibly signaling to make a left turn",
        "During normal circumstances, you are not legally allowed to pass using the shoulder of a road",
        "When the road ahead is clear and free of pedestrians",
        "C"
    },
 
    // Question 25
    {
        "This action can cause you to skid and lose control when you make an abrupt move especially on a wet and possibly slippery road.",
        "Improper braking",
        "Turning too slow",
        "Accelerating too slow",
        "Oversteering",
        "A"
    },
 
    // Question 26
    {
        "To help reduce air pollution using your brake, what should you do?",
        "Brake properly",
        "Brake frequently",
        "Brake suddenly",
        "Brake minimally",
        "A"
    },
 
    // Question 27
    {
        "You should not use a mobile phone while riding a motorcycle because:",
        "Reception is poor when the engine is running",
        "It will affect your vehicle's electronic system",
        "It is prohibited by law and it distracts your attention while driving",
        "It damages the phone due to vibration",
        "C"
    },
 
    // Question 28
    {
        "What sign will constitute an offense if it will be disregarded?",
        "Regulatory sign",
        "Warning sign",
        "Informative sign",
        "Prohibitory sign",
        "A"
    },
 
    // Question 29
    {
        "A flashing green light means:",
        "Full stop",
        "Proceed with caution",
        "Slow down and be ready to stop where a pedestrian crosses the street",
        "Accelerate before the light changes",
        "B"
    }, 
 
    // Question 30
    {
        "A pedestrian runs across the street when you are about to move off from a red light. What should you do?",
        "Blow horn once and allow the pedestrian to cross",
        "Wait until the pedestrian crossed",
        "Expect the pedestrian to walk back",
        "Slowly move forward to signal the pedestrian to hurry",
        "B"
    }
};

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
        System.out.println("Welcome to Conquest, " + playerName + "!");
        System.out.println();
        System.out.println("Fun Fact: 28% of these questions are 100% Accurate from the LTO Theoretical Examination as of July 2025.");
        System.out.println();
        System.out.println("Would you like to start the quiz?");
        System.out.println("Category: Driving");
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
                    System.out.println(answer + " is incorrect! THe correct answer is " + correctLetter + ".");
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
        System.out.println("Category:       Driving");
        System.out.println("Score:          " + score + "/ 10");
        System.out.println("Date:           " + getFormattedDate());
        System.out.println("Time:           " + getFormattedTime());
        System.out.println("==================================================");
    }

    private void saveResults(int score) {
        String entry =
            "==================================================\n" +
            "Player:        " + playerName + "\n" +
            "Category:      Driving" + "\n" +
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