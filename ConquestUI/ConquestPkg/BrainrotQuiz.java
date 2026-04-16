package ConquestPkg;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class BrainrotQuiz {

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

    public BrainrotQuiz(JFrame frame, String playerName) {

        this.frame = frame;
        this.playerName = playerName;
        loadQuestions();

        frame.getContentPane().removeAll();

        JPanel bg = makeBg();
        bg.setLayout(new BorderLayout());

        JLabel title = new JLabel("ConQuest", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 64));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(28, 0, 16, 0));

        JPanel boxRow = new JPanel(new GridLayout(1, 2, 40, 0));
        boxRow.setOpaque(false);
        boxRow.setBorder(BorderFactory.createEmptyBorder(0, 60, 40, 60));

        JPanel funFactBox = makeFrostedBox();
        JPanel funFactInner = new JPanel();
        funFactInner.setLayout(new BoxLayout(funFactInner, BoxLayout.Y_AXIS));
        funFactInner.setOpaque(false);

        JLabel catLabel = new JLabel("Category: Brainrot");
        catLabel.setFont(new Font("Arial", Font.BOLD, 26));
        catLabel.setForeground(Color.WHITE);
        catLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel funFactText = new JLabel(
            "<html><br>The term \"Brainrot\" refers to the mental state caused by consuming excessive amounts of low-quality internet content!</html>"
        );
        funFactText.setFont(new Font("Arial", Font.PLAIN, 18));
        funFactText.setForeground(new Color(255, 220, 220));
        funFactText.setAlignmentX(Component.LEFT_ALIGNMENT);

        funFactInner.add(catLabel);
        funFactInner.add(Box.createRigidArea(new Dimension(0, 14)));
        funFactInner.add(funFactText);
        funFactBox.add(funFactInner);

        JPanel confirmBox = makeFrostedBox();
        JPanel confirmInner = new JPanel();
        confirmInner.setLayout(new BoxLayout(confirmInner, BoxLayout.Y_AXIS));
        confirmInner.setOpaque(false);

        JLabel confirmText = new JLabel("<html><center>Would you like to<br>start the quiz?</center></html>", JLabel.CENTER);
        confirmText.setFont(new Font("Arial", Font.BOLD, 26));
        confirmText.setForeground(Color.WHITE);
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

        bg.add(title, BorderLayout.NORTH);
        bg.add(boxRow, BorderLayout.CENTER);

        frame.setContentPane(bg);

        yesBtn.addActionListener(e -> showLoadingScreen());
        noBtn.addActionListener(e -> new QuizMenu(frame, playerName));

        frame.revalidate();
        frame.repaint();
    }

    private JPanel makeBg() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(139, 0, 0),
                                              getWidth(), getHeight(), new Color(80, 0, 0)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                int cx = getWidth()/2, cy = getHeight()/2;
                float r = Math.max(getWidth(), getHeight()) * 0.6f;
                g2.setPaint(new RadialGradientPaint(cx, cy, r,
                    new float[]{0f, 0.5f, 1f},
                    new Color[]{new Color(200,50,50,80), new Color(139,0,0,40), new Color(80,0,0,0)}
                ));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
    }

    private JPanel makeFrostedBox() {
        JPanel box = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 24, 24);
                g2.dispose();
            }
        };
        box.setOpaque(false);
        return box;
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
                g2.setColor(hovered ? new Color(210, 110, 110) : new Color(188, 74, 74));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2.setColor(new Color(0,0,0,40));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 24, 24);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setForeground(Color.WHITE);
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

        frame.getContentPane().removeAll();

        JPanel bg = makeBg();
        bg.setLayout(new GridBagLayout());

        JPanel card = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 25));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(new Color(255, 255, 255, 60));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 28, 28);
                g2.dispose();
            }
        };
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(420, 300));
        card.setBorder(BorderFactory.createEmptyBorder(36, 50, 36, 50));

        JLabel titleLbl = new JLabel("ConQuest");
        titleLbl.setFont(new Font("Arial", Font.BOLD, 48));
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel category = new JLabel("Category: Brainrot");
        category.setFont(new Font("Arial", Font.BOLD, 20));
        category.setForeground(new Color(255, 200, 200));
        category.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel loading = new JLabel("Loading...");
        loading.setFont(new Font("Arial", Font.PLAIN, 18));
        loading.setForeground(new Color(255, 220, 220));
        loading.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel goodLuck = new JLabel("Good luck!");
        goodLuck.setFont(new Font("Arial", Font.BOLD, 22));
        goodLuck.setForeground(Color.WHITE);
        goodLuck.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(category);
        card.add(Box.createRigidArea(new Dimension(0, 36)));
        card.add(loading);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(goodLuck);
        card.add(Box.createVerticalGlue());

        bg.add(card);
        frame.setContentPane(bg);
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
            System.out.println("[BrainrotQuiz] Loaded via classloader: " + url);
            return new ImageIcon(url);
        }

        url = getClass().getResource("/" + path);
        if (url != null) {
            System.out.println("[BrainrotQuiz] Loaded via getResource: " + url);
            return new ImageIcon(url);
        }

        File f = new File(path);
        if (f.exists()) {
            System.out.println("[BrainrotQuiz] Loaded via working dir: " + f.getAbsolutePath());
            return new ImageIcon(f.getAbsolutePath());
        }

        try {
            File codeBase = new File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            );

            File base = codeBase.isDirectory() ? codeBase : codeBase.getParentFile();
            File fromCode = new File(base, path);
            if (fromCode.exists()) {
                System.out.println("[BrainrotQuiz] Loaded via code base: " + fromCode.getAbsolutePath());
                return new ImageIcon(fromCode.getAbsolutePath());
            }
        } catch (Exception ignored) {}

        System.err.println("[BrainrotQuiz] FAILED to load image: " + path);
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

        JLabel catLbl = new JLabel("CATEGORY: Brainrot");
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

        frame.setContentPane(bg);
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
                new QuizResults(frame, playerName, score, quizQuestions.size(), "Brainrot");
            }
        }).start();
    }




    private void loadQuestions() {

        // 2D array: { question, choiceA, choiceB, choiceC, choiceD, correct, image (or "") }
        String[][] data = {
              // Q1 TO Q30
              {"Why was 6 afraid of 7?",
              "Because 7 scared 6",
              "Because 7 threatened the numbers",
              "Because 7 ate 9",
              "Because 7 played fortnite",
              "C",""},

              {"What quote would you say if you are “focused”?",
              "Let’s focus",
              "Let’s remove distractions",
              "Let’s do this now",
              "Let’s lock in",
              "D",""},

              {"Where did brainrot images originate from?",
              "AI generation",
              "Human art",
              "Philippines",
              "Youtube kids",
              "A",""},

              {"What do you call a slang to call someone’s bottom “big”?",
              "GYAT",
              "FYP",
              "L.H.O.O.Q",
              "WOWOWIN",
              "A",""},

              {"Which social media is the primary source of brainrots?",
              "Facebook",
              "Instagram",
              "Youtube",
              "All of the above",
              "D",""},

              {"Who is Ballerina Cappuccina’s love interest?",
              "Cappuccino Assassino",
              "Crocodilo Bombardino",
              "Tralalelo Tralala",
              "Tung Tung Sahur",
              "D",""},

              {"Who are the target audiences for Brainrots?",
              "Kids",
              "Teens",
              "Adults",
              "Seniors",
              "A",""},

              {"Why do Adults find it difficult to understand Brainrots?",
              "Because it’s not their type",
              "Because kids make them",
              "Because they lack the proper context and meaning",
              "Because it’s just random words or pictures",
              "C",""},

              {"What was one of the earliest Brainrot?",
              "Bum",
              "Italian Brainrot",
              "Sigma",
              "Skibidi Toilet",
              "C",""},

              {"Which one of these games that were heavily influenced by Brainrot?",
              "Minecraft",
              "Roblox",
              "Valorant",
              "Project Sekai",
              "B",""},

              {"What do you call someone when they assume someone likes them or they are better?",
              "Sigma",
              "Ohio",
              "Delulu",
              "Rizz",
              "C",""},

              {"What does “Rizz” mean?",
              "Fighting skill",
              "Charisma",
              "Intelligence",
              "Luck",
              "B",""},

              {"What does “NPC” stand for?",
              "Non Player Character",
              "New Player Code",
              "Normal Person Code",
              "Network Player Control",
              "A",""},

              {"What does “Sus” mean?",
              "Funny",
              "Suspicious",
              "Smart",
              "Loud",
              "B",""},

              {"What does “W” mean?",
              "Wrong",
              "Win",
              "Weak",
              "Wild",
              "B",""},

              {"What does “L” mean?",
              "Loss",
              "Loud",
              "Lucky",
              "Late",
              "A",""},

              {"What is a Sigma?",
              "Leader type",
              "Lone, independent person",
              "Weak person",
              "Follower",
              "B",""},

              {"What does Ohio mean?",
              "A real place only",
              "Normal situation",
              "Weird/chaotic situation",
              "Boring content",
              "C",""},

              {"What is Fanum tax?",
              "Paying money",
              "Taking food from others",
              "Gaming tax",
              "Internet fee",
              "B",""},

              {"What does Gyatt express?",
              "Anger",
              "Suprise/attraction",
              "Fear",
              "Confusion",
              "B",""},

              {"What is Youtube’s short video feature?",
              "Youtube Clips",
              "Youtube Shorts",
              "Youtube Mini",
              "Youtube Quick",
              "B",""},

              {"Instagram’s short video feature is called:",
              "Stories",
              "Reels",
              "Clips",
              "Shorts",
              "B",""},

              {"Brainrot content is usually:",
              "Long videos",
              "Short videos",
              "Text only",
              "Audio only",
              "B",""},

              {"Why are short videos effective?",
              "Hard to watch",
              "Quick and addictive",
              "Expensive",
              "Slow",
              "B",""},

              {"What series made “skibidi” popular?",
              "Spongebob",
              "Skibidi Toilet",
              "Naruto",
              "Minecraft",
              "B",""},

              {"What is often in the background of Brainrot vids?",
              "News clips",
              "Subway Surfers gameplay",
              "Movies",
              "Sports",
              "B",""},

              {"Why is Minecraft parkour also used as background for Brainrot vids?",
              "Educational",
              "Keeps attention",
              "Serious content",
              "Slow gameplay",
              "B",""},

              {"What Minecraft content is used?",
              "Redstone tutorials",
              "Parkour gameplay",
              "Story mode",
              "Mods",
              "B",""},

              {"What is an NPC livestream?",
              "Normal livestream",
              "Repetitive scripted reactions",
              "Gaming stream",
              "Music stream",
              "B",""},

              {"Brainrot audio is usually:",
              "Quiet",
              "Random and loud",
              "Classical",
              "Lyrical",
              "B",""},

              {"Bass boosted means:",
              "Lower volume",
              "Stronger bass sound",
              "No sound",
              "Slower audio",
              "B",""},

              {"Why repeat sounds?",
              "Make it boring",
              "Make it memorable",
              "Save time",
              "Remove sound",
              "B",""},

              {"What is remix audio?",
              "Original audio",
              "Remove sound",
              "Edited/combined audio",
              "Background noise",
              "C",""},

              {"What does “Earworm” mean?",
              "Loud speaker",
              "Quiet music",
              "Broken sound",
              "Song stuck in your head",
              "D",""},

              {"Brainrot videos are:",
              "Fast paced",
              "Slow",
              "Silent",
              "Long",
              "A",""},

              {"Why use fast cuts?",
              "Keep attention",
              "Confuse viewers",
              "Waste time",
              "Save memory",
              "A",""},

              {"What does “Overstimulation” means?",
              "Too calm",
              "Too much input at once",
              "No content",
              "Slow content",
              "B",""},

              {"Brainrot affects attention by:",
              "Increasing focus",
              "Removing memory",
              "Shortening attention span",
              "Slowing thinking",
              "C",""},

              {"Why is Brainrot so addictive?",
              "Boring",
              "Quick dopamine hits",
              "Expensive",
              "Long",
              "B",""},

              {"In Brainrot, what is Ai usually used for?",
              "Generate images",
              "Generate voice overs",
              "Generate Ai stories",
              "All of the above",
              "D",""},

              // Q41 - 50
              { "Who is this kid known as?", // Question
                "The basketball kid",
                "The 6 7 kid",
                "The yelling kid",
                "The painter kid",
                "B", "ConquestPkg/images/Brainrot/41.png" },

              { "Who is this Brainrot known as?", // Question
                "Gangster Footera",
                "Trippi Troppi",
                "Tung Tung Sahur",
                "Boneca Ambalabu",
                "C", "ConquestPkg/images/Brainrot/42.png" },

              { "What does this hand gesture mean?", // Question
                "Help me",
                "Are you choking? In sign language",
                "I’m hungry",
                "6 7",
                "D", "ConquestPkg/images/Brainrot/43.png" },

              { "What is wrong with this brainrot?", // Question
                "Plane with teeth",
                "Missing missile dangling under the plane",
                "Fire effects removed",
                "Flying duck",
                "B", "ConquestPkg/images/Brainrot/44.png" },

              { "What does this guy say?", // Question
                "“I just hit the jackpot!”",
                "“Did you know?”",
                "“Fun fact!”",
                "“I got a high score!”",
                "A", "ConquestPkg/images/Brainrot/45.png" },

              { "What is the context of this brainrot?", // Question
                "The 6 7 kid vs “Adrian explain our friend group”",
                "Avengers",
                "The 6 7 kid with “Adrian explain our friend group”",
                "The meme gang",
                "A", "ConquestPkg/images/Brainrot/46.png" },

              { "After saying “Adrian, explain our friendgroup”, What did they say?", // Question
                "We use the power of friendship",
                "You should leave us alone",
                "We hate pedophiles!",
                "We are Ohio",
                "D", "ConquestPkg/images/Brainrot/47.png" },

              { "What is the main purpose of this Brainrot?", // Question
                "To educate about science",
                "To show realistic photography",
                "To entertain using absurd humor and randomness",
                "To advertise a product",
                "C", "ConquestPkg/images/Brainrot/48.png" },

              { "Why might someone who is not online be confused by this Brainrot?", // Question
                "It uses too many colors",
                "It requires internet culture context to understand",
                "It is too realistic",
                "It is too old",
                "B", "ConquestPkg/images/Brainrot/49.png" },

              { "What best describes the Brainrot?", // Question
                "Structured educational diagram",
                "Random or surreal internet meme (Brainrot content)",
                "Professional photography",
                "News report screenshot",
                "B", "ConquestPkg/images/Brainrot/50.png" },
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
