    package ConquestPkg;

    import java.awt.*;
    import java.util.ArrayList;
    import java.util.Collections;
    import java.util.Random;
    import javax.swing.*;

    public class DrivingQuiz {

        static class Question {
            String text;
            String[] choices;
            int correct;
            String image;

            Question(String text, String[] choices, int correct, String image) {
                this.text = text;
                this.choices = choices;
                this.correct = correct;
                this.image = image;
            }
        }

        private ArrayList<Question> allQuestions = new ArrayList<>();
        private ArrayList<Question> quizQuestions;
        private int currentIndex = 0;
        private int score = 0;
        private JFrame frame;

        public DrivingQuiz(JFrame frame, String playerName) {

            this.frame = frame;
            loadQuestions();

            frame.getContentPane().removeAll();
            frame.setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

            JLabel title = new JLabel("ConQuest", JLabel.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 48));
            mainPanel.add(title, BorderLayout.NORTH);

            JPanel centerPanel = new JPanel(new GridLayout(1, 2, 60, 0));

            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
            leftPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            JLabel category = new JLabel("Category: Driving");
            category.setFont(new Font("Arial", Font.BOLD, 24));

            JLabel funFact = new JLabel(
                    "<html><br>Fun Fact: 28% of these questions are<br>"
                            + "100% Accurate from the LTO Theoretical<br>"
                            + "Examination as of July 2025.</html>"
            );
            funFact.setFont(new Font("Arial", Font.PLAIN, 18));

            leftPanel.add(category);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            leftPanel.add(funFact);

            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

            rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));

            JLabel question = new JLabel("<html>Would you like to<br>Start the quiz?</html>");
            question.setFont(new Font("Arial", Font.BOLD, 24));
            question.setAlignmentX(Component.CENTER_ALIGNMENT);

            JButton yesBtn = new JButton("Yes");
            JButton noBtn = new JButton("No");

            Font btnFont = new Font("Arial", Font.BOLD, 18);
            yesBtn.setFont(btnFont);
            noBtn.setFont(btnFont);

            Dimension btnSize = new Dimension(180, 50);
            yesBtn.setMaximumSize(btnSize);
            noBtn.setMaximumSize(btnSize);

            yesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            noBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

            rightPanel.add(question);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 30)));
            rightPanel.add(yesBtn);
            rightPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            rightPanel.add(noBtn);

            centerPanel.add(leftPanel);
            centerPanel.add(rightPanel);
            mainPanel.add(centerPanel, BorderLayout.CENTER);

            frame.add(mainPanel, BorderLayout.CENTER);

            yesBtn.addActionListener(e -> showLoadingScreen());
            noBtn.addActionListener(e -> new QuizMenu(frame, playerName));

            frame.revalidate();
            frame.repaint();
        }

        // ================= LOADING SCREEN =================
        private void showLoadingScreen() {

            JPanel loadingPanel = new JPanel(new BorderLayout());

            JPanel textPanel = new JPanel();
            textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));

            JLabel title = new JLabel("ConQuest");
            title.setFont(new Font("Arial", Font.BOLD, 42));

            JLabel category = new JLabel("Category: Driving");
            category.setFont(new Font("Arial", Font.BOLD, 20));

            JLabel loading = new JLabel("Loading...");
            loading.setFont(new Font("Arial", Font.PLAIN, 20));

            JLabel goodLuck = new JLabel("Good luck!");
            goodLuck.setFont(new Font("Arial", Font.PLAIN, 20));

            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            category.setAlignmentX(Component.CENTER_ALIGNMENT);
            loading.setAlignmentX(Component.CENTER_ALIGNMENT);
            goodLuck.setAlignmentX(Component.CENTER_ALIGNMENT);

            textPanel.add(Box.createVerticalGlue());
            textPanel.add(title);
            textPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            textPanel.add(category);
            textPanel.add(Box.createRigidArea(new Dimension(0, 40)));
            textPanel.add(loading);
            textPanel.add(Box.createRigidArea(new Dimension(0, 15)));
            textPanel.add(goodLuck);
            textPanel.add(Box.createVerticalGlue());

            loadingPanel.add(textPanel, BorderLayout.CENTER);

            frame.getContentPane().removeAll();
            frame.add(loadingPanel);
            frame.revalidate();
            frame.repaint();

            new javax.swing.Timer(3000, e -> {
                ((javax.swing.Timer) e.getSource()).stop();
                startQuiz();
            }).start();
        }

        // ================= QUIZ =================
        private void startQuiz() {
            Collections.shuffle(allQuestions);
            quizQuestions = new ArrayList<>(allQuestions.subList(0, 30));

            currentIndex = 0;
            score = 0;

            showQuestion();
        }

        private void showQuestion() {

            frame.getContentPane().removeAll();
            frame.setLayout(new BorderLayout());

            Question q = quizQuestions.get(currentIndex);

            // 🔥 MAIN CONTAINER WITH MARGIN
            JPanel main = new JPanel(new BorderLayout());
            main.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

            // 🔥 BIGGER QUESTION TEXT + MARGIN
            JLabel question = new JLabel(
                    "<html><div style='width:600px;'>"
                            + "<b style='font-size:22px;'>Question " + (currentIndex + 1) + ":</b><br><br>"
                            + "<span style='font-size:20px;'>" + q.text + "</span>"
                            + "</div></html>"
            );

            JPanel top = new JPanel(new BorderLayout());
            top.add(question, BorderLayout.CENTER);

            // 🔥 IMAGE WITH SPACING
            if (q.image != null) {
                ImageIcon icon = new ImageIcon(q.image);
                Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(img));
                imgLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
                top.add(imgLabel, BorderLayout.EAST);
            }

            // 🔥 ANSWERS WITH MORE SPACE
            JPanel answers = new JPanel(new GridLayout(2, 2, 20, 20));
            answers.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));

            for (int i = 0; i < 4; i++) {
                JButton btn = new JButton(q.choices[i]);
                btn.setFont(new Font("Arial", Font.BOLD, 16));
                btn.setPreferredSize(new Dimension(200, 50));

                int selected = i;
                btn.addActionListener(e -> handleAnswer(selected));

                answers.add(btn);
            }

            main.add(top, BorderLayout.NORTH);
            main.add(answers, BorderLayout.CENTER);

            frame.add(main);

            frame.revalidate();
            frame.repaint();
        }

        private void handleAnswer(int selected) {

            if (selected == quizQuestions.get(currentIndex).correct) {
                score++;
            }

            currentIndex++;

            if (currentIndex < quizQuestions.size()) {
                showQuestion();
            } else {
                JOptionPane.showMessageDialog(frame, "Score: " + score + "/30");
            }
        }

        private void loadQuestions() {

            allQuestions.add(new Question(
                    "What does the fox say?",
                    new String[]{"mimimi", "pwapwap", "yakee", "gyaki"},
                    2,
                    null
            ));

            allQuestions.add(new Question(
                    "What is this?",
                    new String[]{"idk", "joe", "yo mom", "skibidi"},
                    0,
                    "images/ugandan_knuckles.png"
            ));

            for (int i = 0; i < 48; i++) {
                allQuestions.add(new Question(
                        "Sample Question " + (i + 3),
                        new String[]{"A", "B", "C", "D"},
                        new Random().nextInt(4),
                        null
                ));
            }
        }
    }