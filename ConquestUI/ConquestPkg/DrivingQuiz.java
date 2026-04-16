package ConquestPkg;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class DrivingQuiz {

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


        JLabel title = new JLabel("ConQuest", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 64));
        title.setBorder(BorderFactory.createEmptyBorder(28, 0, 16, 0));

        JPanel boxRow = new JPanel(new GridLayout(1, 2, 40, 0));
        boxRow.setOpaque(false);
        boxRow.setBorder(BorderFactory.createEmptyBorder(0, 60, 40, 60));

        JPanel funFactBox = new JPanel(new GridBagLayout());
        funFactBox.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(Color.DARK_GRAY, 2, 24),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));
        funFactBox.setOpaque(false);

        JPanel funFactInner = new JPanel();
        funFactInner.setLayout(new BoxLayout(funFactInner, BoxLayout.Y_AXIS));
        funFactInner.setOpaque(false);

        JLabel catLabel = new JLabel("Category: Driving");
        catLabel.setFont(new Font("Arial", Font.BOLD, 26));
        catLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel funFactText = new JLabel(
            "<html><br>Fun Fact: 28% of these questions are<br>"
            + "100% Accurate from the LTO Theoretical<br>"
            + "Examination as of July 2025.</html>"
        );
        funFactText.setFont(new Font("Arial", Font.PLAIN, 20));
        funFactText.setAlignmentX(Component.LEFT_ALIGNMENT);

        funFactInner.add(catLabel);
        funFactInner.add(Box.createRigidArea(new Dimension(0, 14)));
        funFactInner.add(funFactText);
        funFactBox.add(funFactInner);

        JPanel confirmBox = new JPanel(new GridBagLayout());
        confirmBox.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(Color.DARK_GRAY, 2, 24),
                BorderFactory.createEmptyBorder(24, 28, 24, 28)
        ));
        confirmBox.setOpaque(false);

        JPanel confirmInner = new JPanel();
        confirmInner.setLayout(new BoxLayout(confirmInner, BoxLayout.Y_AXIS));
        confirmInner.setOpaque(false);

        JLabel confirmText = new JLabel("<html><center>Would you like to<br>start the quiz?</center></html>", JLabel.CENTER);
        confirmText.setFont(new Font("Arial", Font.BOLD, 26));
        confirmText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton yesBtn = makeRoundBtn("Yes");
        JButton noBtn  = makeRoundBtn("No");

        confirmInner.add(confirmText);
        confirmInner.add(Box.createRigidArea(new Dimension(0, 28)));
        confirmInner.add(yesBtn);
        confirmInner.add(Box.createRigidArea(new Dimension(0, 14)));
        confirmInner.add(noBtn);
        confirmBox.add(confirmInner);

        boxRow.add(funFactBox);
        boxRow.add(confirmBox);

        frame.add(title, BorderLayout.NORTH);
        frame.add(boxRow, BorderLayout.CENTER);

        yesBtn.addActionListener(e -> showLoadingScreen());
        noBtn.addActionListener(e -> new QuizMenu(frame, playerName));

        frame.revalidate();
        frame.repaint();
    }

    private JButton makeRoundBtn(String text) {
        JButton btn = new JButton(text) {
            private boolean hovered = false;
            {
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent e) { hovered = true;  repaint(); }
                    public void mouseExited(java.awt.event.MouseEvent e)  { hovered = false; repaint(); }
                });
            }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = hovered ? new Color(173, 216, 230) : UIManager.getColor("Panel.background");
                if (bg == null) bg = new Color(238, 238, 238);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(Color.DARK_GRAY);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setPreferredSize(new Dimension(200, 56));
        btn.setMaximumSize(new Dimension(200, 56));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showLoadingScreen() {

        MusicPlayer.stop();

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


    // IMAGE LOADER PER QUESTION
    private ImageIcon loadImage(String path) {

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

        File f = new File(path);
        if (f.exists()) {
            System.out.println("[DrivingQuiz] Loaded via working dir: " + f.getAbsolutePath());
            return new ImageIcon(f.getAbsolutePath());
        }

        try {
            File codeBase = new File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            );

            File base = codeBase.isDirectory() ? codeBase : codeBase.getParentFile();
            File fromCode = new File(base, path);
            if (fromCode.exists()) {
                System.out.println("[DrivingQuiz] Loaded via code base: " + fromCode.getAbsolutePath());
                return new ImageIcon(fromCode.getAbsolutePath());
            }
        } catch (Exception ignored) {}

        System.err.println("[DrivingQuiz] FAILED to load image: " + path);
        System.err.println("  Working dir : " + new File(".").getAbsolutePath());
        return null;
    }

    private void startQuiz() {
        Collections.shuffle(allQuestions);
        quizQuestions = new ArrayList<>(allQuestions.subList(0, 30));

        currentIndex = 0;
        score = 0;

        playMusic();
        showQuestion();
    }

    //THEME COLORS
    private static final Color BG_DARK    = new Color(139, 0, 0);      
    private static final Color BG_DARKER  = new Color(110, 0, 0);      
    private static final Color BTN_NORMAL = new Color(188, 74, 74);    
    private static final Color BTN_HOVER  = new Color(210, 110, 110);  
    private static final Color BTN_CORRECT= new Color(34, 139, 34);
    private static final Color BTN_WRONG  = new Color(80, 0, 0);

    private void showQuestion() {

        frame.getContentPane().removeAll();
        frame.setLayout(new BorderLayout());

        Question q = quizQuestions.get(currentIndex);

        JPanel main = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, BG_DARK, getWidth(), getHeight(), BG_DARKER));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        main.setOpaque(false);
        main.setBorder(BorderFactory.createEmptyBorder(20, 36, 24, 36));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setOpaque(false);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));

        JLabel titleLbl = new JLabel("ConQuest");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 32));
        titleLbl.setForeground(Color.WHITE);

        JLabel catLbl = new JLabel("CATEGORY: DRIVING");
        catLbl.setFont(new Font("Arial", Font.BOLD, 16));
        catLbl.setForeground(Color.WHITE);

        topBar.add(titleLbl, BorderLayout.WEST);
        topBar.add(catLbl,   BorderLayout.EAST);

        JLabel imgLabel = null;
        if (q.image != null) {
            ImageIcon icon = loadImage(q.image);
            if (icon != null) {
                Image img = icon.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                imgLabel = new JLabel(new ImageIcon(img));
                imgLabel.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(new Color(255, 255, 255, 80), 2, 16),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
                imgLabel.setVerticalAlignment(SwingConstants.TOP);
            }
        }

        JLabel questionLabel = new JLabel(
            "<html><div style='width:460px'><b>Question " + (currentIndex + 1) + " of " + quizQuestions.size() + ":</b><br><br>" + q.text + "</div></html>"
        );
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 19));
        questionLabel.setForeground(Color.WHITE);
        questionLabel.setVerticalAlignment(SwingConstants.TOP);

        JPanel questionBox = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                g2.dispose();
            }
        };
        questionBox.setOpaque(false);
        questionBox.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        questionBox.add(questionLabel, BorderLayout.CENTER);
        if (imgLabel != null) questionBox.add(imgLabel, BorderLayout.EAST);

        JPanel answers = new JPanel(new GridLayout(2, 2, 24, 18));
        answers.setOpaque(false);
        answers.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        JButton[] btnArray = new JButton[4];
        boolean[] filled   = new boolean[4];
        boolean[] locked   = new boolean[]{ false };

        for (int i = 0; i < 4; i++) {
            char letter = (char) ('A' + i);
            String choiceHtml = "<html><div style='padding:2px'><b>" + letter + ".</b> " + q.choices[i] + "</div></html>";
            final int idx = i;

            JButton btn = new JButton(choiceHtml) {
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                    g2.setColor(new Color(0, 0, 0, 60));
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setBackground(BTN_NORMAL);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.PLAIN, 19));
            btn.setHorizontalAlignment(SwingConstants.CENTER);
            btn.setPreferredSize(new Dimension(0, 80));
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (!locked[0]) { btn.setBackground(BTN_HOVER); btn.repaint(); }
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    if (!locked[0]) { btn.setBackground(BTN_NORMAL); btn.repaint(); }
                }
            });

            btnArray[i] = btn;
            answers.add(btn);
        }

        for (int i = 0; i < 4; i++) {
            int selected = i;
            btnArray[i].addActionListener(e -> handleAnswer(selected, btnArray, filled, locked, q));
        }

        JPanel north = new JPanel(new BorderLayout());
        north.setOpaque(false);
        north.add(topBar,       BorderLayout.NORTH);
        north.add(questionBox,  BorderLayout.CENTER);

        main.add(north,   BorderLayout.NORTH);
        main.add(answers, BorderLayout.SOUTH);

        JPanel bg = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, BG_DARK, getWidth(), getHeight(), BG_DARKER));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        bg.add(main);

        frame.add(bg);
        frame.revalidate();
        frame.repaint();
    }

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
            volume.setValue(volume.getMinimum() + (volume.getMaximum() - volume.getMinimum()) * 0.75f);
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

    private void playClick() {
        playSynthSound(800, 60, 40, 80);
    }

    private void playCorrect() {
        new Thread(() -> {
            playSynthSoundBlocking(600, 120, 15, 95);
            playSynthSoundBlocking(900, 180, 10, 95);
        }).start();
    }

    private void playWrong() {
        new Thread(() -> {
            playSynthSoundBlocking(400, 150, 10, 95);
            playSynthSoundBlocking(200, 200, 8, 95);
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

    private void handleAnswer(int selected, JButton[] buttons, boolean[] filled, boolean[] locked, Question q) {

        locked[0] = true;
        for (JButton b : buttons) b.setEnabled(false);

        char selectedLetter = (char) ('A' + selected);
        int correctIndex = q.correct - 'A';
        boolean isCorrect = selectedLetter == q.correct;

        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setBackground(BTN_NORMAL);
            buttons[i].setForeground(Color.WHITE);
            buttons[i].repaint();
        }

        if (isCorrect) {
            score++;
            playCorrect();
            buttons[selected].setBackground(BTN_CORRECT);
            buttons[selected].setFont(buttons[selected].getFont().deriveFont(Font.BOLD));
            buttons[selected].repaint();
        } else {
            playWrong();
            buttons[selected].setBackground(BTN_WRONG);
            buttons[selected].setFont(buttons[selected].getFont().deriveFont(Font.BOLD));
            buttons[selected].repaint();
            buttons[correctIndex].setBackground(BTN_CORRECT);
            buttons[correctIndex].setFont(buttons[correctIndex].getFont().deriveFont(Font.BOLD));
            buttons[correctIndex].repaint();
        }

        new javax.swing.Timer(1200, e -> {
            ((javax.swing.Timer) e.getSource()).stop();
            currentIndex++;
            if (currentIndex < quizQuestions.size()) {
                showQuestion();
            } else {
                stopMusic();
                new QuizResults(frame, playerName, score, quizQuestions.size(), "Driving");
            }
        }).start();
    }

    private void loadQuestions() {

        // 2D array: { question, choiceA, choiceB, choiceC, choiceD, correct, image (or "") }
        String[][] data = {
            // Q1
            { "A sixty year old senior citizen files an application for a driver's license. Is this allowed?",
              "Yes",
              "No",
              "It depends if the applicant will pass the medical, theoretical and practical examination",
              "No, because applicants above 60 years old are no longer allowed to apply for a driver's license.",
              "C", "" },
            // Q2
            { "You are driving on an expressway. You may stop only:",
              "To load or unload goods or passengers",
              "To take a short rest if you feel slightly tired",
              "On the lay-by to attend or call for an emergency",
              "To ask for directions",
              "C", "" },
            // Q3
            { "You are planning a long trip. Do you need to plan rest stops?",
              "No, you will arrive to your destination faster",
              "No, only fuel stops will be needed",
              "Yes, but only if you are traveling at night",
              "Yes, regular stops help avoid mental and physical fatigue",
              "D", "" },
            // Q4
            { "During period of illness, your ability to drive may be impaired. You must:",
              "Only take smaller doses of any medicine at all times to ensure safety",
              "See your doctor each time before you drive, especially for long trips",
              "Ignore minor illnesses and continue driving as long as you feel okay",
              "Be physically and mentally fit and do not drive after taking medicine",
              "D", "" },
            // Q5
            { "Under R.A. 10666 also known as Children's Safety on Motorcycle Act, the child must grasp the rider's waist, can step and rest his/her feet comfortably on the foot peg, and ________.",
              "he/she must be at least five (5) feet in height",
              "he/she must wear standard protective helmet",
              "he/she must have parent's consent",
              "he/she must be accompanied by another adult rider on a separate motorcycle",
              "B", "" },
            // Q6
            { "A blinking yellow traffic light means?",
              "Slow down and proceed if there is no danger",
              "Wait for the green light",
              "Stop and wait for the traffic light",
              "Speed up to clear the intersection quickly",
              "A", "" },
            // Q7
            { "When do you need to check your engine oil level?",
              "When the engine is hot",
              "When the engine is running",
              "When the engine is cold",
              "Only when the oil warning light turns on",
              "C", "" },
            // Q8
            { "Yellow box pavement marking is painted with intersections where no vehicle is allowed to _______?",
              "Pass", "Blow horn", "Stop", "Turn left",
              "C", "" },
            // Q9
            { "What should the driver do if he/she is already within the intersection when the yellow traffic light comes?",
              "Slow down to allow other vehicles to cross",
              "Stop automatically on the road",
              "Reverse immediately to exit the intersection",
              "The driver may continue driving with caution",
              "D", "" },
            // Q10
            { "Why are rumble strips installed across the road?",
              "To help you keep the correct separation distance",
              "To help you choose the correct lane",
              "To make you alert and aware of your speed",
              "To improve tire traction during rainy weather",
              "C", "" },
            // Q11
            { "Which one is a mandate of LTO?",
              "Issue certificate of emission compliance",
              "Register roadworthy and emission complaint motor vehicles",
              "Issue certificate of public convenience",
              "Regulate public transportation routes",
              "B", "" },
            // Q12
            { "How does alcohol affect you?",
              "It speeds up your reaction",
              "It improves your coordination",
              "Provide national passports for drivers",
              "It reduces your concentration",
              "D", "" },
            // Q13
            { "What is the meaning of \"beating the red light\"?",
              "Driving faster upon seeing a yellow/amber light",
              "Stopping on the green light",
              "Stopping suddenly",
              "Turning on hazard lights before crossing the intersection",
              "A", "" },
            // Q14
            { "Under R.A. 10666, which of the following prohibits the rider to convey a child?",
              "Where a speed limit of more than 40 kph is imposed",
              "Where a speed limit of more than 50 kph is imposed",
              "Where a speed limit of more than 60 kph is imposed",
              "Where a speed limit of more than 80 kph is imposed",
              "C", "" },
            // Q15
            { "What should you do whenever you are driving on a highway with a lot of potholes?",
              "Increase speed", "Decrease speed", "Always change lane", "Maintain the same speed",
              "B", "" },
            // Q16
            { "Using a backbone motorcycle, which stand do you need to use when parking overnight?",
              "Side stand", "Cross stand", "Center stand", "Kick stand",
              "C", "" },
            // Q17
            { "Ahead of you is a vehicle with a flashing yellow light. This means it is:",
              "broken down vehicle", "slow moving", "a doctor's car", "an emergency vehicle",
              "B", "" },
            // Q18
            { "What should you do if the road is wet?",
              "Increase your speed", "Keep on swerving", "Slow down", "Use your hazard lights",
              "C", "" },
            // Q19
            { "Which of the traffic lights requires you to prepare for a stop?",
              "Yellow/Amber", "Green", "Red", "Flashing Red",
              "A", "" },
            // Q20
            { "What is the authority granted by LTO to a person who desires to learn to operate a motor vehicle valid for a period of one year?",
              "Non-Professional Driver's License",
              "Professional Driver's License",
              "Provisional Driver's License",
              "Student Driver's Permit",
              "D", "" },
            // Q21
            { "Signs that are round, inverted triangle or octagonal and with red colored border are called:",
              "regulatory signs", "warning signs", "informative signs", "prohibitory signs",
              "A", "" },
            // Q22
            { "You arrive at a crossroad. You want to turn left, and you have a green arrow light. Can you proceed?",
              "Yes, you can turn left, as this is a \"protected\" turn where no other cars are allowed to enter the intersection",
              "No, you cannot turn left unless you have a green left pointing arrow",
              "Yes, you can turn left, however you may still need to yield to pedestrians and incoming traffic",
              "No, you must wait for the regular green light before turning left",
              "A", "" },
            // Q23
            { "What should be done first before changing lanes?",
              "Make signal", "Check the traffic ahead", "Check mirrors for traffic behind", "Increase your speed",
              "A", "" },
            // Q24
            { "When is it legal to use the shoulder of a road when overtaking another vehicle?",
              "When the vehicle in front of you is making a right turn",
              "When the vehicle in front of you is visibly signaling to make a left turn",
              "During normal circumstances, you are not legally allowed to pass using the shoulder of a road",
              "When the road ahead is clear and free of pedestrians",
              "C", "" },
            // Q25
            { "This action can cause you to skid and lose control when you make an abrupt move especially on a wet and possibly slippery road.",
              "Improper braking", "Turning too slow", "Accelerating too slow", "Oversteering",
              "A", "" },
            // Q26
            { "To help reduce air pollution using your brake, what should you do?",
              "Brake properly", "Brake frequently", "Brake suddenly", "Brake minimally",
              "A", "" },
            // Q27
            { "You should not use a mobile phone while riding a motorcycle because:",
              "reception is poor when the engine is running",
              "it will affect your vehicle's electronic system",
              "it is prohibited by law and it distracts your attention while driving",
              "it damages the phone due to vibration",
              "C", "" },
            // Q28
            { "What sign will constitute an offense if it will be disregarded?",
              "Regulatory sign", "Warning sign", "Informative sign", "Prohibitory sign",
              "A", "" },
            // Q29
            { "A flashing green light means:",
              "full stop",
              "proceed with caution",
              "slow down and be ready to stop where a pedestrian cross the street",
              "accelerate before the light changes",
              "B", "" },
            // Q30
            { "A pedestrian runs across the street when you are about to move off from a red light. What should you do?",
              "Blow horn once and allow the pedestrian to cross",
              "Wait until the pedestrian crossed",
              "Expect the pedestrian to walk back",
              "Slowly move forward to signal the pedestrian to hurry",
              "B", "" },
            // Q31
            { "What is the meaning of this traffic light?",
              "Prepare to stop", "Stop if the red light comes", "Stop if necessary", "Slow down and proceed with caution",
              "A", "ConquestPkg/images/Driving/31.png" },
            // Q32
            { "What does this traffic sign mean?",
              "Dangerous right curve", "Dangerous left curve", "Dangerous curve", "Sharp right turn ahead",
              "A", "ConquestPkg/images/Driving/32.png" },
            // Q33
            { "What comes next after this traffic light?",
              "Blue", "Green", "Red", "Flashing red",
              "C", "ConquestPkg/images/Driving/33.png" },
            // Q34
            { "What does this traffic sign mean?",
              "No swerving", "Change lane", "Winding road ahead", "Dangerous curve ahead",
              "D", "ConquestPkg/images/Driving/34.png" },
            // Q35
            { "What does this traffic sign mean?",
              "No U-turn", "Dangerous left bend", "Dangerous right bend", "No turning allowed",
              "A", "ConquestPkg/images/Driving/35.png" },
            // Q36
            { "What does this traffic sign mean?",
              "Pedestrian crossing", "Starting point for walking", "Traffic light signals ahead", "Road hazard ahead",
              "C", "ConquestPkg/images/Driving/36.png" },
            // Q37
            { "Where would you see this sign?",
              "At the side of the road", "At playground areas", "At school pedestrian crossing", "At hospital zones",
              "C", "ConquestPkg/images/Driving/37.png" },
            // Q38
            { "What does this traffic sign mean?",
              "Stop", "Do not enter", "No entry for all types of vehicles", "Yield to oncoming traffic",
              "A", "ConquestPkg/images/Driving/38.png" },
            // Q39
            { "What does this traffic sign mean?",
              "No cars", "Cars only", "No motorcycles", "Motorcycles only",
              "C", "ConquestPkg/images/Driving/39.png" },
            // Q40
            { "What does this traffic sign mean?",
              "No parking", "No overtaking", "No entry on all types of vehicles", "No passing zone",
              "B", "ConquestPkg/images/Driving/40.png" },
            // Q41
            { "It is the car's turn and brake signals have failed, so the driver is using hand gestures to signal instead. What is the driver's intention?",
              "Increase speed", "Turn left", "Turn right", "Slow down",
              "C", "ConquestPkg/images/Driving/41.png" },
            // Q42
            { "What is the meaning of this traffic sign?",
              "No entry for vehicles with 30 years age", "Minimum speed limit", "End of maximum speed limit", "Maximum speed limit of 60 kph",
              "C", "ConquestPkg/images/Driving/42.png" },
            // Q43
            { "What does this traffic sign mean?",
              "Road works", "Slippery road", "Uneven light signals ahead", "Construction zone ahead",
              "A", "ConquestPkg/images/Driving/43.png" },
            // Q44
            { "What does this traffic sign mean?",
              "One way ahead", "Road wide ahead", "Narrow bridge ahead", "Straight road only",
              "A", "ConquestPkg/images/Driving/44.png" },
            // Q45
            { "What does this traffic sign mean?",
              "Dangerous right double bend", "Dangerous left double bend", "Dangerous curve", "Winding road ahead",
              "B", "ConquestPkg/images/Driving/45.png" },
            // Q46
            { "What is the meaning of this traffic sign?",
              "Speed limit on this area must not be less than 60 kph",
              "Speed limit on this area is 60 kph",
              "This zone is 60 km long",
              "Maximum speed allowed is beyond 60 kph",
              "B", "ConquestPkg/images/Driving/46.png" },
            // Q47
            { "What does this sign mean?",
              "No entry", "No parking", "No through road", "Do not stop",
              "A", "ConquestPkg/images/Driving/47.png" },
            // Q48
            { "What does this traffic sign mean?",
              "Approach to intersection", "Highway ahead", "Merging traffic ahead", "Crossroad ahead",
              "A", "ConquestPkg/images/Driving/48.png" },
            // Q49
            { "What does this traffic sign mean?",
              "Minimum speed 30 kph", "End of maximum speed", "End of minimum speed", "No vehicles below 30 kph",
              "C", "ConquestPkg/images/Driving/49.png" },
            // Q50
            { "What does this traffic sign mean?",
              "Downhill course", "Uphill course", "Humps ahead", "Steep road ahead",
              "B", "ConquestPkg/images/Driving/50.png" },
        };

        for (String[] row : data) {
            allQuestions.add(new Question(
                row[0],
                new String[]{ row[1], row[2], row[3], row[4] },
                row[5].charAt(0),
                row[6].isEmpty() ? null : row[6]
            ));
        }
    }
}
