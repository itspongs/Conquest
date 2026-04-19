package ConquestPkg;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class GeneralKnowledgeQuiz {

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

    public GeneralKnowledgeQuiz(JFrame frame, String playerName) {

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

        JLabel catLabel = new JLabel("Category: General Knowledge");
        catLabel.setFont(new Font("Arial", Font.BOLD, 26));
        catLabel.setAlignmentX(Component.LEFT_ALIGNMENT);


        //Fun Fact added by: Frances C. Vega
        //"General knowledge strengthens memory, critical thinking, and helps you make better
        //connections across different fields of learning!"

        JLabel funFactText = new JLabel(
            "<html><br>Fun fact: General knowledge strengthens memory,<br>"
            + "critical thinking, and helps you make better<br>"
            + "connections across different fields of learning!</html>"
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

        JLabel category = new JLabel("Category: General Knowledge");
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
            System.out.println("[GeneralKnowledgeQuiz] Loaded via classloader: " + url);
            return new ImageIcon(url);
        }

        url = getClass().getResource("/" + path);
        if (url != null) {
            System.out.println("[GeneralKnowledgeQuiz] Loaded via getResource: " + url);
            return new ImageIcon(url);
        }

        File f = new File(path);
        if (f.exists()) {
            System.out.println("[GeneralKnowledgeQuiz] Loaded via working dir: " + f.getAbsolutePath());
            return new ImageIcon(f.getAbsolutePath());
        }

        try {
            File codeBase = new File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            );

            File base = codeBase.isDirectory() ? codeBase : codeBase.getParentFile();
            File fromCode = new File(base, path);
            if (fromCode.exists()) {
                System.out.println("[GeneralKnowledgeQuiz] Loaded via code base: " + fromCode.getAbsolutePath());
                return new ImageIcon(fromCode.getAbsolutePath());
            }
        } catch (Exception ignored) {}

        System.err.println("[GeneralKnowledgeQuiz] FAILED to load image: " + path);
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

        JLabel catLbl = new JLabel("CATEGORY: General Knowledge");
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
            playSynthSoundBlocking(600, 120, 15, 83);
            playSynthSoundBlocking(900, 180, 10, 83);
        }).start();
    }

    private void playWrong() {
        new Thread(() -> {
            playSynthSoundBlocking(400, 150, 10, 83);
            playSynthSoundBlocking(200, 200, 8, 83);
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
                new QuizResults(frame, playerName, score, quizQuestions.size(), "GeneralKnowledge");
            }
        }).start();
    }




    //ASSIGNED TASKER: FRANCES C. VEGA
    //ALL THE QUESTION 1-50 TO THE 2D ARRAYS ALREADY INPUTTED
    //FROM QUESTION 31-50. THE IMAGES ALREADY DOWNLOADED/SCREENSHOT FOR EACH QUESTION.
    //FROM QUESTION 31-50. ALL THE PICTURES ALREADY COMPILED AND RENAMED ACCORDINGLY AT (ConquestPkg/images/GeneralKnowledge)
    //FROM QUESTION 31-50. THE FORMAT BELOW ALREADY FOLLOWED, location => "ConquestPkg/images/GeneralKnowledge"
    //THE REFERENCE/GUIDE WAS FOLLOWED (DrivingQuiz.java)

    private void loadQuestions() {

        // 2D array: { question, choiceA, choiceB, choiceC, choiceD, correct, image (or "") }
        String[][] data = {
            // Q1 
            { "Which planet is closest to the Sun?", // Question
              "Venus", // Choice A
              "Mercury", // Choice B
              "Earth", // Choice C
              "Mars", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image
            // Q2
              { "Who painted the Mona Lisa?", // Question
              "Van Gogh", // Choice A
              "Picasso", // Choice B
              "Da Vinci", // Choice C
              "Monet", // Choice D
              "C", "" }, // Correct Letter   // LEAVE BLANK FOR image
            // Q3
              { "Which element has the symbol O?", // Question
              "Gold", // Choice A
              "Oxygen", // Choice B
              "Osmium", // Choice C
              "Zinc", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image
            // Q4
              { "Which galaxy do we live in?", // Question
              "Andromeda", // Choice A
              "Milky Way", // Choice B
              "Sombrero", // Choice C
              "Whirlpool", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image  
            // Q5
              { "What is the largest planet in the solar system?", // Question
              "Saturn", // Choice A
              "Jupiter", // Choice B
              "Neptune", // Choice C
              "Uranus", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image   
            // Q6
              { "What force keeps planets in orbit?", // Question
              "Magnetism", // Choice A
              "Gravity", // Choice B
              "Friction", // Choice C
              "Energy", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image   
            // Q7
              { "What is the Earth's natural satellite?", // Question
              "Titan", // Choice A
              "Europa", // Choice B
              "Moon", // Choice C
              "Phobos", // Choice D
              "C", "" }, // Correct Letter   // LEAVE BLANK FOR image    
            // Q8
              { "Which gas do plants use for photosynthesis?", // Question
              "Oxygen", // Choice A
              "Carbon Dioxide", // Choice B
              "Nitrogen", // Choice C
              "Hydrogen", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image   
            // Q9
              { "What is the chemical formula of water", // Question
              "CO2", // Choice A
              "H2O", // Choice B
              "O2", // Choice C
              "NaCl", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image   
             // Q10
              { "What part of the cell contains DNA?", // Question
              "Cytoplasm", // Choice A
              "Nucleus", // Choice B
              "Membrane", // Choice C
              "Ribosome", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image    
            // Q11
              { "Who developed the theory of relativity?", // Question
              "Newton", // Choice A
              "Einstein", // Choice B
              "Tesla", // Choice C
              "Darwin", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image     
            // Q12
              { "What is the speed of light?", // Question
              "300,000 km/s", // Choice A
              "150,000 km/s", // Choice B
              "1,000 km/s", // Choice C
              "30,000 km/s", // Choice D
              "A", "" }, // Correct Letter   // LEAVE BLANK FOR image    
            // Q13
              { "Which organ pumps blood?", // Question
              "Lungs", // Choice A
              "Brain", // Choice B
              "Heart", // Choice C
              "Kidney", // Choice D
              "C", "" }, // Correct Letter   // LEAVE BLANK FOR image   
            // Q14
              { "What is the rarest blood type in humans", // Question
              "AB negative", // Choice A
              "AB positive", // Choice B
              "A positive", // Choice C
              "B positive", // Choice D
              "A", "" }, // Correct Letter   // LEAVE BLANK FOR image 
            // Q15
              { "Which gas do humans breathe in?", // Question
              "Carbon dioxide", // Choice A
              "Oxygen", // Choice B
              "Nitrogen", // Choice C
              "Hydrogen", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image 
            // Q16
              { "Which scientist discovered gravity?", // Question
              "Einstein", // Choice A
              "Newton", // Choice B
              "Galileo", // Choice C
              "Curie", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image     
            // Q17
              { "What is the boiling point of water?", // Question
              "50 degree Celsius", // Choice A
              "100 degree Celsius", // Choice B
              "150 degree Celsius", // Choice C
              "200 degree Celsius", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image     
            // Q18
              { "Which vitamin comes from sunlight?", // Question
              "Vitamin A", // Choice A
              "Vitamin B", // Choice B
              "Vitamin C", // Choice C
              "Vitamin D", // Choice D
              "D", "" }, // Correct Letter   // LEAVE BLANK FOR image    
            // Q19
              { "Which blood cells fight infection", // Question
              "Red", // Choice A
              "White", // Choice B
              "Platelets", // Choice C
              "Plasma", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image    
             // Q20
              { "What is the largest organ in the human body?", // Question
              "Heart", // Choice A
              "Liver", // Choice B
              "Skin", // Choice C
              "Brain", // Choice D
              "C", "" }, // Correct Letter   // LEAVE BLANK FOR image  
             // Q21
              { "Who wrote 'Romeo and Juliet'?", // Question
              "Dickens", // Choice A
              "Shakespeare", // Choice B
              "Hemingway", // Choice C
              "Orwell", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image    
            // Q22
              { "What is the longest-running TV show (animated)?", // Question
              "Simpsons", // Choice A
              "Family Guy", // Choice B
              "South Park", // Choice C
              "SpongeBob", // Choice D
              "A", "" }, // Correct Letter   // LEAVE BLANK FOR image      
            // Q23
              { "Which instrument has 88 keys?", // Question
              "Guitar", // Choice A
              "Violin", // Choice B
              "Piano", // Choice C
              "Drums", // Choice D
              "C", "" }, // Correct Letter   // LEAVE BLANK FOR image      
            // Q24
              { "Who was the first man on the moon?", // Question
              "Buzz Aldrin", // Choice A
              "Yuri Gagarin", // Choice B
              "Neil Armstrong", // Choice C
              "John Glenn", // Choice D
              "C", "" }, // Correct Letter   // LEAVE BLANK FOR image    
            // Q25
              { "What year did World War II end?", // Question
              "1942", // Choice A
              "1945", // Choice B
              "1939", // Choice C
              "1950", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image              
            // Q26
              { "Who invented the telephone?", // Question
              "Edison", // Choice A
              "Bell", // Choice B
              "Tesla", // Choice C
              "Newton", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image      
            // Q27
              { "Which language has the most native speakers?", // Question
              "English", // Choice A
              "Spanish", // Choice B
              "Mandarin", // Choice C
              "Hindi", // Choice D
              "C", "" }, // Correct Letter   // LEAVE BLANK FOR image   
            // Q28
              { "What is the largest mammal?", // Question
              "Elephant", // Choice A
              "Blue whale", // Choice B
              "Giraffe", // Choice C
              "Hippo", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image     
            // Q29
              { "Which animal is known for changing colors?", // Question
              "Dog", // Choice A
              "Chameleon", // Choice B
              "Lion", // Choice C
              "Elephant", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image     
             // Q30
              { "What is the fastest land animal?", // Question
              "Lion", // Choice A
              "Cheetah", // Choice B
              "Horse", // Choice C
              "Tiger", // Choice D
              "B", "" }, // Correct Letter   // LEAVE BLANK FOR image     
            // Q31
            { "Which company created the iPhone?", // Question
              "Microsoft", // Choice A
              "Apple", // Choice B
              "Google", // Choice C
              "Samsung", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/31.png" }, //Correct Letter //number.png
            // Q32
            { "What does CPU stand for?", // Question
              "Central Process Unit", // Choice A
              "Central Processing Unit", // Choice B
              "Computer Power Unit", // Choice C
              "Core Processing Unit", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/32.png" }, //Correct Letter //number.png
            // Q33
            { "What is the main function of RAM?", // Question
              "Storage", // Choice A
              "Processing", // Choice B
              "Temporary memory", // Choice C
              "Graphics", // Choice D
              "C", "ConquestPkg/images/GeneralKnowledge/33.png" }, //Correct Letter //number.png
            // Q34
            { "Which programming language is known for Android apps?", // Question
              "Python", // Choice A
              "Java", // Choice B
              "C++", // Choice C
              "Swift", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/34.png" }, //Correct Letter //number.png
             // Q35
            { "What does HTTP stand for?", // Question
              "HyperText Transfer Protocol", // Choice A
              "High Transfer Text Process", // Choice B
              "Hyper Tool Protocol", // Choice C
              "Host Transfer Program", // Choice D
              "A", "ConquestPkg/images/GeneralKnowledge/35.png" }, //Correct Letter //number.png
            // Q36
            { "Which company developed Windows OS", // Question
              "Apple", // Choice A
              "Microsoft", // Choice B
              "Google", // Choice C
              "IBM", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/36.png" }, //Correct Letter //number.png
            // Q37
            { "Which device is used to input text?", // Question
              "Monitor", // Choice A
              "Keyboard", // Choice B
              "Printer", // Choice C
              "Speaker", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/37.png" }, //Correct Letter //number.png
            // Q38
            { "What does AI stand for?", // Question
              "Automated Input", // Choice A
              "Artificial Intelligence", // Choice B
              "Advanced Internet", // Choice C
              "Auto Interface", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/38.png" }, //Correct Letter //number.png
            // Q39
            { "Which storage is fastest?", // Question
              "HDD", // Choice A
              "SSD", // Choice B
              "USB", // Choice C
              "CD", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/39.png" }, //Correct Letter //number.png
            // Q40
            { "What is the hardest natural substance?", // Question
              "Gold", // Choice A
              "Iron", // Choice B
              "Diamond", // Choice C
              "Silver", // Choice D
              "C", "ConquestPkg/images/GeneralKnowledge/40.png" }, //Correct Letter //number.png
            // Q41
            { "What is the red planet?", // Question
              "Venus", // Choice A
              "Mars", // Choice B
              "Jupiter", // Choice C
              "Saturn", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/41.png" }, //Correct Letter //number.png
            // Q42
            { "What is the closest star to Earth?", // Question
              "Sirius", // Choice A
              "Alpha Centauri", // Choice B
              "Proxima Centauri", // Choice C
              "Sun", // Choice D
              "D", "ConquestPkg/images/GeneralKnowledge/42.png" }, //Correct Letter //number.png
            // Q43
            { "Which planet has rings?", // Question
              "Earth", // Choice A
              "Mars", // Choice B
              "Saturn", // Choice C
              "Venus", // Choice D
              "C", "ConquestPkg/images/GeneralKnowledge/43.png" }, //Correct Letter //number.png
            // Q44
            { "What is a supernova?", // Question
              "A planet", // Choice A
              "Exploding star", // Choice B
              "Galaxy", // Choice C
              "Asteroid", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/44.png" }, //Correct Letter //number.png 
            // Q45
            { "What do black holes do?", // Question
              "Emit light", // Choice A
              "Absorb everything", // Choice B
              "Reflect light", // Choice C
              "Freeze time", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/45.png" }, //Correct Letter //number.png
            // Q46
            { "What is the Sun made mostly of?", // Question
              "Oxygen", // Choice A
              "Hydrogen", // Choice B
              "Carbon", // Choice C
              "Iron", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/46.png" }, //Correct Letter //number.png
            // Q47
            { "What is zero gravity called?", // Question
              "No force", // Choice A
              "Microgravity", // Choice B
              "Hypergravity", // Choice C
              "Antigravity", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/47.png" }, //Correct Letter //number.png
            // Q48
            { "What tool is used to observe stars?", // Question
              "Microscope", // Choice A
              "Telescope", // Choice B
              "Periscope", // Choice C
              "Binoculars", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/48.png" }, //Correct Letter //number.png
            // Q49
            { "What causes day and night?", // Question
              "Moon rotation", // Choice A
              "Earth rotation", // Choice B
              "Sun movement", // Choice C
              "Stars", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/49.png" }, //Correct Letter //number.png
            // Q50
            { "What is the Milky Way?", // Question
              "Planet", // Choice A
              "Galaxy", // Choice B
              "Star", // Choice C
              "Comet", // Choice D
              "B", "ConquestPkg/images/GeneralKnowledge/50.png" }, //Correct Letter //number.png
        };


        //DON'T MIND THIS
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
