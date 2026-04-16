package ConquestPkg;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class DrivingQuiz {

    // ================= QUESTION CLASS =================
    static class Question {
        String text;
        String[] choices;
        char correct;
        String image;

        Question(String text, String[] choices, char correct, String image) {
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
    private String playerName;
    private javax.sound.sampled.Clip musicClip;

    public DrivingQuiz(JFrame frame, String playerName) {

        this.frame = frame;
        this.playerName = playerName;
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

    // ================= IMAGE LOADER =================
    // Tries multiple strategies so images work both in IDE and packaged JARs.
    // Also prints to System.err so you know exactly which path failed/succeeded.
    private ImageIcon loadImage(String path) {

        // Strategy 1: classpath root — works when images are inside src/ or a resources/ root
        java.net.URL url = getClass().getClassLoader().getResource(path);
        if (url != null) {
            System.out.println("[DrivingQuiz] Loaded via classloader: " + url);
            return new ImageIcon(url);
        }

        url = getClass().getResource("/" + path);
        if (url != null) {
            System.out.println("[DrivingQuiz] Loaded via getResource: " + url);
            return new ImageIcon(url);
        }

        // Strategy 2: relative to the JVM working directory
        // In NetBeans this is usually the project root folder (where build/ and src/ live)
        File f = new File(path);
        if (f.exists()) {
            System.out.println("[DrivingQuiz] Loaded via working dir: " + f.getAbsolutePath());
            return new ImageIcon(f.getAbsolutePath());
        }

        // Strategy 3: relative to the directory that contains the compiled .class / .jar
        try {
            File codeBase = new File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            );
            // codeBase may be a JAR file; use its parent directory
            File base = codeBase.isDirectory() ? codeBase : codeBase.getParentFile();
            File fromCode = new File(base, path);
            if (fromCode.exists()) {
                System.out.println("[DrivingQuiz] Loaded via code base: " + fromCode.getAbsolutePath());
                return new ImageIcon(fromCode.getAbsolutePath());
            }
        } catch (Exception ignored) {}

        // All strategies failed — report so you know exactly what to fix
        System.err.println("[DrivingQuiz] FAILED to load image: " + path);
        System.err.println("  Working dir : " + new File(".").getAbsolutePath());
        return null;
    }

    // ================= QUIZ =================
    private void startQuiz() {
        Collections.shuffle(allQuestions);
        quizQuestions = new ArrayList<>(allQuestions.subList(0, 30));

        currentIndex = 0;
        score = 0;

        playMusic();
        showQuestion();
    }

    private void showQuestion() {

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        Question q = quizQuestions.get(currentIndex);

        // ── Outer panel with padding ───────────────────────────────
        JPanel main = new JPanel(new BorderLayout());
        main.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // ══════════════════════════════════════════════════════════
        // TOP HALF — image on the left, question box on the right
        // ══════════════════════════════════════════════════════════
        JPanel topHalf = new JPanel(new BorderLayout(20, 0));

        // ── Question box (top-right) ───────────────────────────────
        JLabel questionNumber = new JLabel(
                "Question " + (currentIndex + 1) + " of " + quizQuestions.size() + ":"
        );
        questionNumber.setFont(new Font("Arial", Font.BOLD, 15));
        questionNumber.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JLabel questionText = new JLabel(
                "<html><div style='width:480px; font-size:18px;'>" + q.text + "</div></html>"
        );
        questionText.setFont(new Font("Arial", Font.PLAIN, 18));
        questionText.setVerticalAlignment(SwingConstants.TOP);

        JScrollPane scrollPane = new JScrollPane(questionText);
        scrollPane.setBorder(null);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel questionBox = new JPanel(new BorderLayout());
        questionBox.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(Color.GRAY, 2, 16),
                BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        questionBox.add(questionNumber, BorderLayout.NORTH);
        questionBox.add(scrollPane, BorderLayout.CENTER);

        topHalf.add(questionBox, BorderLayout.CENTER);

        // ── Image on the top-right (only if question has one) ──────
        if (q.image != null) {
            ImageIcon icon = loadImage(q.image);
            JLabel imgLabel;
            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(260, 260, Image.SCALE_SMOOTH);
                imgLabel = new JLabel(new ImageIcon(img));
            } else {
                imgLabel = new JLabel("<html><center>[Image not found]<br>" + q.image + "</center></html>");
                imgLabel.setFont(new Font("Arial", Font.ITALIC, 11));
                imgLabel.setForeground(Color.RED);
                imgLabel.setPreferredSize(new Dimension(260, 260));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imgLabel.setBorder(BorderFactory.createLineBorder(Color.RED));
            }
            imgLabel.setVerticalAlignment(SwingConstants.TOP);
            imgLabel.setBorder(new RoundedBorder(Color.GRAY, 2, 16));
            topHalf.add(imgLabel, BorderLayout.EAST);
        }

        // ══════════════════════════════════════════════════════════
        // BOTTOM HALF — 2x2 answer buttons
        // ══════════════════════════════════════════════════════════
        JPanel answers = new JPanel(new GridLayout(2, 2, 35, 31));
        answers.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JButton[] btnArray = new JButton[4];

        for (int i = 0; i < 4; i++) {
            char letter = (char) ('A' + i);
            String choiceHtml = "<html><div style='text-align:left; padding:2px;'>"
                    + "<b>" + letter + ".</b> " + q.choices[i]
                    + "</div></html>";

            JButton btn = new JButton(choiceHtml);
            btn.setFont(new Font("Arial", Font.PLAIN, 20));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMargin(new Insets(12, 18, 12, 18));
            btn.setPreferredSize(new Dimension(0, 90));
            btn.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(Color.GRAY, 2, 16),
                    BorderFactory.createEmptyBorder(12, 18, 12, 18)
            ));
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!btn.getBackground().equals(Color.GREEN) && !btn.getBackground().equals(Color.RED)) {
                        btn.setBackground(new Color(173, 216, 230));
                        btn.setContentAreaFilled(true);
                    }
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!btn.getBackground().equals(Color.GREEN) && !btn.getBackground().equals(Color.RED)) {
                        btn.setContentAreaFilled(false);
                    }
                }
            });

            btnArray[i] = btn;
            answers.add(btn);
        }

        for (int i = 0; i < 4; i++) {
            int selected = i;
            btnArray[i].addActionListener(e -> handleAnswer(selected, btnArray, q));
        }

        JPanel bottomHalf = new JPanel(new BorderLayout());
        bottomHalf.add(answers, BorderLayout.SOUTH);

        main.add(topHalf, BorderLayout.CENTER);
        main.add(bottomHalf, BorderLayout.SOUTH);

        frame.add(main);
        frame.revalidate();
        frame.repaint();
    }

    // ================= BACKGROUND MUSIC =================
    private void playMusic() {
        try {
            java.net.URL url = getClass().getClassLoader().getResource("ConquestPkg/music/quiz_music.wav");
            if (url == null) {
                java.io.File f = new java.io.File("music/quiz_music.wav");
                if (f.exists()) url = f.toURI().toURL();
            }
            if (url == null) { System.err.println("[Music] quiz_music.wav not found"); return; }
            javax.sound.sampled.AudioInputStream ais = javax.sound.sampled.AudioSystem.getAudioInputStream(url);
            musicClip = javax.sound.sampled.AudioSystem.getClip();
            musicClip.open(ais);
            javax.sound.sampled.FloatControl volume = (javax.sound.sampled.FloatControl)
                    musicClip.getControl(javax.sound.sampled.FloatControl.Type.MASTER_GAIN);
            volume.setValue(volume.getMinimum() + (volume.getMaximum() - volume.getMinimum()) * 0.65f);
            musicClip.loop(javax.sound.sampled.Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    // ================= CLICK SOUND =================
    private void playClick() {
        playSynthSound(800, 60, 40, 80);
    }

    private void playCorrect() {
        // Two rising tones: 600Hz then 900Hz
        new Thread(() -> {
            playSynthSoundBlocking(600, 120, 15, 90);
            playSynthSoundBlocking(900, 180, 10, 90);
        }).start();
    }

    private void playWrong() {
        // Two falling tones: 400Hz then 200Hz
        new Thread(() -> {
            playSynthSoundBlocking(400, 150, 10, 90);
            playSynthSoundBlocking(200, 200, 8, 90);
        }).start();
    }

    private void playSynthSound(int freq, int durationMs, double decay, int amplitude) {
        new Thread(() -> playSynthSoundBlocking(freq, durationMs, decay, amplitude)).start();
    }

    private void playSynthSoundBlocking(int freq, int durationMs, double decay, int amplitude) {
        try {
            float sampleRate = 44100;
            int samples = (int) (sampleRate * durationMs / 1000);
            byte[] buf = new byte[samples];
            for (int i = 0; i < samples; i++) {
                double t = i / sampleRate;
                double envelope = Math.exp(-t * decay);
                buf[i] = (byte) (envelope * Math.sin(2 * Math.PI * freq * t) * amplitude);
            }
            javax.sound.sampled.AudioFormat fmt =
                    new javax.sound.sampled.AudioFormat(sampleRate, 8, 1, true, false);
            javax.sound.sampled.DataLine.Info info =
                    new javax.sound.sampled.DataLine.Info(javax.sound.sampled.SourceDataLine.class, fmt);
            javax.sound.sampled.SourceDataLine line =
                    (javax.sound.sampled.SourceDataLine) javax.sound.sampled.AudioSystem.getLine(info);
            line.open(fmt);
            line.start();
            line.write(buf, 0, buf.length);
            line.drain();
            line.close();
        } catch (Exception ignored) {}
    }

    private void handleAnswer(int selected, JButton[] buttons, Question q) {

        // disable all buttons immediately
        for (JButton b : buttons) b.setEnabled(false);

        char selectedLetter = (char) ('A' + selected);
        int correctIndex = q.correct - 'A';
        boolean isCorrect = selectedLetter == q.correct;

        if (isCorrect) {
            score++;
            playCorrect();
            buttons[selected].setBackground(Color.GREEN);
            buttons[selected].setContentAreaFilled(true);
        } else {
            playWrong();
            buttons[selected].setBackground(Color.RED);
            buttons[selected].setContentAreaFilled(true);
            buttons[correctIndex].setBackground(Color.GREEN);
            buttons[correctIndex].setContentAreaFilled(true);
        }

        // wait 1.2 seconds then move on
        new javax.swing.Timer(1200, e -> {
            ((javax.swing.Timer) e.getSource()).stop();
            currentIndex++;
            if (currentIndex < quizQuestions.size()) {
                showQuestion();
            } else {
                stopMusic();
                JOptionPane.showMessageDialog(frame, "Score: " + score + "/30");
            }
        }).start();
    }

    // ================= QUESTIONS =================
    private void loadQuestions() {

        // QUESTION 1
        allQuestions.add(new Question(
                "A sixty year old senior citizen files an\n application for a driver's license. Is this allowed?",
                new String[]{
                    "Yes",
                    "No",
                    "It depends if the applicant will pass the medical, theoretical and practical examination",
                    "No, because applicants above 60 years old are no longer allowed to apply for a driver's license."
                },
                'C',
                null
        ));

        // QUESTION 2
        allQuestions.add(new Question(
                "You are driving on an expressway.\n You may stop only:",
                new String[]{
                    "To load or unload goods or passengers",
                    "To take a short rest if you feel slightly tired",
                    "On the lay-by to attend or call for an emergency",
                    "To ask for directions"
                },
                'C',
                null
        ));

        // QUESTION 3
        allQuestions.add(new Question(
                "You are planning a long trip." + "Do you need to plan rest stops?",
                new String[]{
                    "No, you will arrive to your destination faster",
                    "No, only fuel stops will be needed",
                    "Yes, but only if you are traveling at night",
                    "Yes, regular stops help avoid mental and physical fatigue"
                },
                'D',
                null
        ));

        // QUESTION 4
        allQuestions.add(new Question(
                "During period of illness, your ability"
                + "to drive may be impaired. You must:",
                new String[]{
                    "Only take smaller doses of any medicine at all times to ensure safety",
                    "See your doctor each time before you drive, especially for long trips",
                    "Ignore minor illnesses and continue driving as long as you feel okay",
                    "Be physically and mentally fit and do not drive aafter taking medicine"
                },
                'D',
                null
        ));

        // QUESTION 5
        allQuestions.add(new Question(
                "Under R.A. 10666 also known as Children's Safety on Motorcycle Act, the child must grasp the rider's waist, "
                + "can step and rest his/her feet comfortably on the foot peg, and ________.",
                new String[]{
                    "he/she must be at least five (5) feet in height",
                    "he/she must wear standard protective helmet",
                    "he/she must have parent's consent",
                    "he/she must be accompanied by another adult rider on a separate motorcycle"
                },
                'B',
                null
        ));

        // QUESTION 6
        allQuestions.add(new Question(
                "A blinking yellow traffic light means?",
                new String[]{
                    "Slow down and proceed if there is no danger",
                    "Wait for the green light",
                    "Stop and wait for the traffic light",
                    "Speed up to clear the intersection quickly"
                },
                'A',
                null
        ));

        // QUESTION 7
        allQuestions.add(new Question(
                "When do you need to check your engine oil level?",
                new String[]{
                    "When the engine is hot",
                    "When the engine is running",
                    "When the engine is cold",
                    "Only when the oil warning light turns on"
                },
                'C',
                null
        ));

        // QUESTION 8
        allQuestions.add(new Question(
                "Yellow box pavement marking is painted with intersections where no vehicle is allowed to _______?",
                new String[]{
                    "Pass",
                    "Blow horn",
                    "Stop",
                    "Turn left"
                },
                'C',
                null
        ));

        // QUESTION 9
        allQuestions.add(new Question(
                "What should the driver do if he/she is already within the intersection when the yellow traffic light comes?",
                new String[]{
                    "Slow down to allow other vehicles to cross",
                    "Stop automatically on the road",
                    "Reverse immediately to exit the intersection",
                    "The driver may continue driving with caution"
                },
                'D',
                null
        ));

        // QUESTION 10
        allQuestions.add(new Question(
                "Why are rumble strips installed across the road?",
                new String[]{
                    "To help you keep the correct separation distance",
                    "To help you choose the correct lane",
                    "To make you alert and aware of your speed",
                    "To improve tire traction during rainy weather"
                },
                'C',
                null
        ));

        // QUESTION 11
        allQuestions.add(new Question(
                "Which one is a mandate of LTO?",
                new String[]{
                    "Issue certificate of emission compliance",
                    "Register roadworthy and emission complaint motor vehicles",
                    "Issue certificate of public convenience",
                    "Regulate public transportation routes"
                },
                'B',
                null
        ));

        // QUESTION 12
        allQuestions.add(new Question(
                "How does alcohol affect you?",
                new String[]{
                    "It speeds up your reaction",
                    "It improves your coordination",
                    "Provide national passports for drivers",
                    "It reduces your concentration"
                },
                'D',
                null
        ));

        // QUESTION 13
        allQuestions.add(new Question(
                "What is the meaning of \"beating the red light\"?",
                new String[]{
                    "Driving faster upon seeing a yellow/amber light",
                    "Stopping on the green light",
                    "Stopping suddenly",
                    "Turning on hazard lights before crossing the intersection"
                },
                'A',
                null
        ));

        // QUESTION 14
        allQuestions.add(new Question(
                "Under R.A. 10666, which of the following prohibits the rider to convey a child?",
                new String[]{
                    "Where a speed limit of more than 40 kph is imposed",
                    "Where a speed limit of more than 50 kph is imposed",
                    "Where a speed limit of more than 60 kph is imposed",
                    "Where a speed limit of more than 80 kph is imposed"
                },
                'C',
                null
        ));

        // QUESTION 15
        allQuestions.add(new Question(
                "What should you do whenever you are driving on a highway with a lot of potholes?",
                new String[]{
                    "Increase speed",
                    "Decrease speed",
                    "Always change lane",
                    "Maintain the same speed"
                },
                'B',
                null
        ));

        // QUESTION 16
        allQuestions.add(new Question(
                "Using a backbone motorcycle, which stand do you need to use when parking overnight?",
                new String[]{
                    "Side stand",
                    "Cross stand",
                    "Center stand",
                    "Kick stand"
                },
                'C',
                null
        ));

        // QUESTION 17
        allQuestions.add(new Question(
                "Ahead of you is a vehicle with a flashing yellow light. This means it is:",
                new String[]{
                    "broken down vehicle",
                    "slow moving",
                    "a doctor's car",
                    "an emergency vehicle"
                },
                'B',
                null
        ));

        // QUESTION 18
        allQuestions.add(new Question(
                "What should you do if the road is wet?",
                new String[]{
                    "Increase your speed",
                    "Keep on swerving",
                    "Slow down",
                    "Use your hazard lights"
                },
                'C',
                null
        ));

        // QUESTION 19
        allQuestions.add(new Question(
                "Which of the traffic lights requires you to prepare for a stop?",
                new String[]{
                    "Yellow/Amber",
                    "Green",
                    "Red",
                    "Flashing Red"
                },
                'A',
                null
        ));

        // QUESTION 20
        allQuestions.add(new Question(
                "What is the authority granted by LTO to a person who desires to learn to operate a motor vehicle "
                        + "valid for a period of one year?",
                new String[]{
                    "Non-Professional Driver's License",
                    "Professional Driver's License",
                    "Provisional Driver's License",
                    "Student Driver's Permit"
                },
                'D',
                null
        ));

        // QUESTION 21
        allQuestions.add(new Question(
                "Signs that are round, inverted triangle or octagonal and with red colored border are called:",
                new String[]{
                    "regulatory signs",
                    "warning signs",
                    "informative signs",
                    "prohibitory signs"
                },
                'A',
                null
        ));

        // QUESTION 22
        allQuestions.add(new Question(
                "You arrive at a crossroad. You want to turn left, and you have a green arrow light. Can you proceed?",
                new String[]{
                    "Yes, you can turn left, as this is a \"protected\" turn where no other cars are allowed to enter the intersection",
                    "No, you cannot turn left unless you have a green left pointing arrow",
                    "Yes, you can turn left, however you may still need to yield to pedestrians and incoming traffic",
                    "No, you must wait for the regular green light before turning left"
                },
                'A',
                null
        ));

        // QUESTION 23
        allQuestions.add(new Question(
                "What should be done first before changing lanes?",
                new String[]{
                    "Make signal",
                    "Check the traffic ahead",
                    "Check mirrors for traffic behind",
                    "Increase your speed"
                },
                'A',
                null
        ));

        // QUESTION 24
        allQuestions.add(new Question(
                "When is it legal to use the shoulder of a road when overtaking another vehicle?",
                new String[]{
                    "When the vehicle in front of you is making a right turn",
                    "When the vehicle in front of you is visibly signaling to make a left turn",
                    "During normal circumstances, you are not legally allowed to pass using the shoulder of a road",
                    "When the road ahead is clear and free of pedestrians"
                },
                'C',
                null
        ));

        // QUESTION 25
        allQuestions.add(new Question(
                "This action can cause you to skid and lose control when you make an abrupt move "
                        + "especially on a wet and possibly slippery road.",
                new String[]{
                    "Improper braking",
                    "Turning too slow",
                    "Accelerating too slow",
                    "Oversteering"
                },
                'A',
                null
        ));

        // QUESTION 26
        allQuestions.add(new Question(
                "To help reduce air pollution using your brake, what should you do?",
                new String[]{
                    "Brake properly",
                    "Brake frequently",
                    "Brake suddenly",
                    "Brake minimally"
                },
                'A',
                null
        ));

        // QUESTION 27
        allQuestions.add(new Question(
                "You should not use a mobile phone while riding a motorcycle because:",
                new String[]{
                    "reception is poor when the engine is running",
                    "it will affect your vehicle's electronic system",
                    "it is prohibited by law and it distracts your attention while driving",
                    "it damages the phone due to vibration"
                },
                'C',
                null
        ));

        // QUESTION 28
        allQuestions.add(new Question(
                "What sign will constitute an offense if it will be disregarded?",
                new String[]{
                    "Regulatory sign",
                    "Warning sign",
                    "Informative sign",
                    "Prohibitory sign"
                },
                'A',
                null
        ));

        // QUESTION 29
        allQuestions.add(new Question(
                "A flashing green light means:",
                new String[]{
                    "full stop",
                    "proceed with caution",
                    "slow down and be ready to stop where a pedestrian cross the street",
                    "accelerate before the light changes"
                },
                'B',
                null
        ));

        // QUESTION 30
        allQuestions.add(new Question(
                "A pedestrian runs across the street when you are about to move off from a red light. What should you do?",
                new String[]{
                    "Blow horn once and allow the pedestrian to cross",
                    "Wait until the pedestrian crossed",
                    "Expect the pedestrian to walk back",
                    "Slowly move forward to signal the pedestrian to hurry"
                },
                'B',
                null
        ));

        // QUESTION 31 (IMAGE)
        allQuestions.add(new Question(
                "What is the meaning of this traffic light?",
                new String[]{
                    "Prepare to stop",
                    "Stop if the red light comes",
                    "Stop if necessary",
                    "Slow down and proceed with caution"
                },
                'A',
                "ConquestPkg/images/Driving/31.png"
        ));

        // QUESTION 32 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "Dangerous right curve",
                    "Dangerous left curve",
                    "Dangerous curve",
                    "Sharp right turn ahead"
                },
                'A',
                "ConquestPkg/images/Driving/32.png"
        ));

        // QUESTION 33 (IMAGE)
        allQuestions.add(new Question(
                "What comes next after this traffic light?",
                new String[]{
                    "Blue",
                    "Green",
                    "Red",
                    "Flashing red"
                },
                'C',
                "ConquestPkg/images/Driving/33.png"
        ));

        // QUESTION 34 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "No swerving",
                    "Change lane",
                    "Winding road ahead",
                    "Dangerous curve ahead"
                },
                'D',
                "ConquestPkg/images/Driving/34.png"
        ));

        // QUESTION 35 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "No U-turn",
                    "Dangerous left bend",
                    "Dangerous right bend",
                    "No turning allowed"
                },
                'A',
                "ConquestPkg/images/Driving/35.png"
        ));

        // QUESTION 36 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "Pedestrian crossing",
                    "Starting point for walking",
                    "Traffic light signals ahead",
                    "Road hazard ahead"
                },
                'C',
                "ConquestPkg/images/Driving/36.png"
        ));

        // QUESTION 37 (IMAGE)
        allQuestions.add(new Question(
                "Where would you see this sign?",
                new String[]{
                    "At the side of the road",
                    "At playground areas",
                    "At school pedestrian crossing",
                    "At hospital zones"
                },
                'C',
                "ConquestPkg/images/Driving/37.png"
        ));

        // QUESTION 38 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "Stop",
                    "Do not enter",
                    "No entry for all types of vehicles",
                    "Yield to oncoming traffic"
                },
                'A',
                "ConquestPkg/images/Driving/38.png"
        ));

        // QUESTION 39 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "No cars",
                    "Cars only",
                    "No motorcycles",
                    "Motorcycles only"
                },
                'C',
                "ConquestPkg/images/Driving/39.png"
        ));

        // QUESTION 40 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "No parking",
                    "No overtaking",
                    "No entry on all types of vehicles",
                    "No passing zone"
                },
                'B',
                "ConquestPkg/images/Driving/40.png"
        ));

        // QUESTION 41 (IMAGE)
        allQuestions.add(new Question(
                "It is the car's turn and brake signals have failed, so the driver is using hand gestures to signal instead. "
                        + "What is the driver's intention?",
                new String[]{
                    "Increase speed",
                    "Turn left",
                    "Turn right",
                    "Slow down"
                },
                'C',
                "ConquestPkg/images/Driving/41.png"
        ));

        // QUESTION 42 (IMAGE)
        allQuestions.add(new Question(
                "What is the meaning of this traffic sign?",
                new String[]{
                    "No entry for vehicles with 30 years age",
                    "Minimum speed limit",
                    "End of maximum speed limit",
                    "Maximum speed limit of 60 kph"
                },
                'C',
                "ConquestPkg/images/Driving/42.png"
        ));

        // QUESTION 43 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "Road works",
                    "Slippery road",
                    "Uneven light signals ahead",
                    "Construction zone ahead"
                },
                'A',
                "ConquestPkg/images/Driving/43.png"
        ));

        // QUESTION 44 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "One way ahead",
                    "Road wide ahead",
                    "Narrow bridge ahead",
                    "Straight road only"
                },
                'A',
                "ConquestPkg/images/Driving/44.png"
        ));

        // QUESTION 45 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "Dangerous right double bend",
                    "Dangerous left double bend",
                    "Dangerous curve",
                    "Winding road ahead"
                },
                'B',
                "ConquestPkg/images/Driving/45.png"
        ));

        // QUESTION 46 (IMAGE)
        allQuestions.add(new Question(
                "What is the meaning of this traffic sign?",
                new String[]{
                    "Speed limit on this area must not be less than 60 kph",
                    "Speed limit on this area is 60 kph",
                    "This zone is 60 km long",
                    "Maximum speed allowed is beyond 60 kph"
                },
                'B',
                "ConquestPkg/images/Driving/46.png"
        ));

        // QUESTION 47 (IMAGE)
        allQuestions.add(new Question(
                "What does this sign mean?",
                new String[]{
                    "No entry",
                    "No parking",
                    "No through road",
                    "Do not stop"
                },
                'A',
                "ConquestPkg/images/Driving/47.png"
        ));

        // QUESTION 48 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "Approach to intersection",
                    "Highway ahead",
                    "Merging traffic ahead",
                    "Crossroad ahead"
                },
                'A',
                "ConquestPkg/images/Driving/48.png"
        ));

        // QUESTION 49 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "Minimum speed 30 kph",
                    "End of maximum speed",
                    "End of minimum speed",
                    "No vehicles below 30 kph"
                },
                'C',
                "ConquestPkg/images/Driving/49.png"
        ));

        // QUESTION 50 (IMAGE)
        allQuestions.add(new Question(
                "What does this traffic sign mean?",
                new String[]{
                    "Downhill course",
                    "Uphill course",
                    "Humps ahead",
                    "Steep road ahead"
                },
                'B',
                "ConquestPkg/images/Driving/50.png"
        ));
    }
}