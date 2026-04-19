package ConquestPkg;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class GeographyQuiz {

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

    public GeographyQuiz(JFrame frame, String playerName) {

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

        JLabel catLabel = new JLabel("Category: Geography");
        catLabel.setFont(new Font("Arial", Font.BOLD, 26));
        catLabel.setForeground(Color.WHITE);
        catLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel funFactText = new JLabel(
            "<html><br>Fun Fact: Geography questions cover<br>"
            + "capitals, landmarks, flags, rivers,<br>"
            + "oceans, and world geography.</html>"
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

        JLabel title = new JLabel("ConQuest");
        title.setFont(new Font("Arial", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel category = new JLabel("Category: Geography");
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
        card.add(title);
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
            System.out.println("[GeographyQuiz] Loaded via classloader: " + url);
            return new ImageIcon(url);
        }

        url = getClass().getResource("/" + path);
        if (url != null) {
            System.out.println("[GeographyQuiz] Loaded via getResource: " + url);
            return new ImageIcon(url);
        }

        File f = new File(path);
        if (f.exists()) {
            System.out.println("[GeographyQuiz] Loaded via working dir: " + f.getAbsolutePath());
            return new ImageIcon(f.getAbsolutePath());
        }

        try {
            File codeBase = new File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            );

            File base = codeBase.isDirectory() ? codeBase : codeBase.getParentFile();
            File fromCode = new File(base, path);
            if (fromCode.exists()) {
                System.out.println("[GeographyQuiz] Loaded via code base: " + fromCode.getAbsolutePath());
                return new ImageIcon(fromCode.getAbsolutePath());
            }
        } catch (Exception ignored) {}

        System.err.println("[GeographyQuiz] FAILED to load image: " + path);
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

        JLabel catLbl = new JLabel("CATEGORY: GEOGRAPHY");
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
                new QuizResults(frame, playerName, score, quizQuestions.size(), "Geography");
            }
        }).start();
    }

    private void loadQuestions() {

        // 2D array: { question, choiceA, choiceB, choiceC, choiceD, correct, image (or "") }
        String[][] data = {


            // Q1
            { "What is the largest continent by land area?",
              "Africa", "Asia", "North America", "Antarctica",
              "B", "" },
            // Q2
            { "Which river is traditionally considered the longest in the world?",
              "Amazon", "Nile", "Yangtze", "Mississippi",
              "B", "" },
            // Q3
            { "Mount Everest is located in which mountain range?",
              "Himalayas", "Andes", "Rockies", "Alps",
              "A", "" },
            // Q4
            { "Which country has the largest population in the world?",
              "USA", "India", "China", "Indonesia",
              "B", "" },
            // Q5
            { "What is the capital of Australia?",
              "Sydney", "Melbourne", "Canberra", "Perth",
              "C", "" },
            // Q6
            { "Which is the largest ocean on Earth?",
              "Atlantic Ocean", "Indian Ocean", "Pacific Ocean", "Arctic Ocean",
              "C", "" },
            // Q7
            { "In which country is the Great Barrier Reef located?",
              "Australia", "Fiji", "Brazil", "Indonesia",
              "A", "" },
            // Q8
            { "What is the smallest country in the world by land area?",
              "Vatican City", "Monaco", "Nauru", "San Marino",
              "A", "" },
            // Q9
            { "The Sahara Desert is located on which continent?",
              "Asia", "Africa", "South America", "Australia",
              "B", "" },
            // Q10
            { "Which city is known as the \"City of Canals\"?",
              "Amsterdam", "Venice", "Paris", "Bangkok",
              "B", "" },
            // Q11
            { "What is the capital of Japan?",
              "Tokyo", "Osaka", "Kyoto", "Seoul",
              "A", "" },
            // Q12
            { "Which of these is the deepest point in the world's oceans?",
              "Java Trench", "Mariana Trench", "Puerto Rico Trench", "Tonga Trench",
              "B", "" },
            // Q13
            { "Which country is also a continent?",
              "Greenland", "Iceland", "Australia", "Madagascar",
              "C", "" },
            // Q14
            { "What is the capital of Canada?",
              "Toronto", "Vancouver", "Ottawa", "Montreal",
              "C", "" },
            // Q15
            { "Which line of latitude divides the Earth into the Northern and Southern Hemispheres?",
              "Equator", "Prime Meridian", "Tropic of Cancer", "Tropic of Capricorn",
              "A", "" },
            // Q16
            { "Which European country is shaped like a boot?",
              "Greece", "Italy", "Spain", "Portugal",
              "B", "" },
            // Q17
            { "What is the largest desert in the world (including polar deserts)?",
              "Antarctica", "Sahara", "Gobi", "Arabian",
              "A", "" },
            // Q18
            { "In which country would you find the ancient city of Petra?",
              "Egypt", "Jordan", "Turkey", "Iraq",
              "B", "" },
            // Q19
            { "What is the capital of France?",
              "Lyon", "Marseille", "Paris", "Bordeaux",
              "C", "" },
            // Q20
            { "Which river flows through the Grand Canyon?",
              "Colorado River", "Rio Grande", "Missouri River", "Yukon River",
              "A", "" },
            // Q21
            { "Which country has the most natural lakes?",
              "Canada", "Russia", "USA", "Finland",
              "A", "" },
            // Q22
            { "What is the capital of Brazil?",
              "Rio de Janeiro", "Sao Paulo", "Brasilia", "Salvador",
              "C", "" },
            // Q23
            { "Which mountain is the highest in Africa?",
              "Mount Kilimanjaro", "Mount Kenya", "Mount Sinai", "Atlas Mountains",
              "A", "" },
            // Q24
            { "Which sea separates Europe from Africa?",
              "Red Sea", "Mediterranean Sea", "Black Sea", "Caspian Sea",
              "B", "" },
            // Q25
            { "What is the largest island in the world?",
              "Australia", "Borneo", "Greenland", "New Guinea",
              "C", "" },
            // Q26
            { "Which country is known as the \"Land of the Rising Sun\"?",
              "Japan", "China", "Thailand", "South Korea",
              "A", "" },
            // Q27
            { "What is the capital of Egypt?",
              "Cairo", "Alexandria", "Giza", "Luxor",
              "A", "" },
            // Q28
            { "In which ocean is the island of Madagascar located?",
              "Atlantic", "Pacific", "Indian", "Arctic",
              "C", "" },
            // Q29
            { "What is the official language of Brazil?",
              "Spanish", "Portuguese", "English", "French",
              "B", "" },
            // Q30
            { "Which U.S. state is the largest by land area?",
              "Alaska", "Texas", "California", "Montana",
              "A", "" },
            // Q31
            { "Which canal connects the Atlantic and Pacific Oceans?",
              "Suez Canal", "Panama Canal", "Erie Canal", "Kiel Canal",
              "B", "" },
            // Q32
            { "What is the capital of Germany?",
              "Munich", "Frankfurt", "Berlin", "Hamburg",
              "C", "" },
            // Q33
            { "Which mountain range runs along the western coast of South America?",
              "Andes", "Rockies", "Appalachians", "Urals",
              "A", "" },
            // Q34
            { "What is the largest country in the world by land area?",
              "Russia", "Canada", "China", "USA",
              "A", "" },
            // Q35
            { "Which city is the capital of Thailand?",
              "Phuket", "Bangkok", "Chiang Mai", "Pattaya",
              "B", "" },
            // Q36
            { "Which of the Great Lakes is the largest by surface area?",
              "Lake Superior", "Lake Michigan", "Lake Huron", "Lake Erie",
              "A", "" },
            // Q37
            { "What is the capital of Italy?",
              "Milan", "Florence", "Rome", "Naples",
              "C", "" },
            // Q38
            { "Which country is famous for the Eiffel Tower?",
              "Italy", "France", "Spain", "United Kingdom",
              "B", "" },
            // Q39
            { "What is the capital of Russia?",
              "Moscow", "Saint Petersburg", "Novosibirsk", "Kiev",
              "A", "" },
            // Q40
            { "Which desert is the driest place on Earth?",
              "Atacama Desert", "Sahara Desert", "Kalahari Desert", "Mojave Desert",
              "A", "" },
            // Q41
            { "What is the capital of Spain?",
              "Madrid", "Barcelona", "Seville", "Valencia",
              "A", "" },
            // Q42
            { "Which country does the Rhine River primarily flow through?",
              "Germany", "France", "Italy", "Poland",
              "A", "" },
            // Q43
            { "In which city is the Burj Khalifa located?",
              "Riyadh", "Doha", "Dubai", "Abu Dhabi",
              "C", "" },
            // Q44
            { "What is the capital of India?",
              "Mumbai", "New Delhi", "Bangalore", "Kolkata",
              "B", "" },
            // Q45
            { "Which of these countries is not in Europe?",
              "Poland", "Austria", "Thailand", "Belgium",
              "C", "" },
            // Q46
            { "What is the capital of Argentina?",
              "Buenos Aires", "Santiago", "Lima", "Bogota",
              "A", "" },
            // Q47
            { "Which volcano destroyed Pompeii in 79 AD?",
              "Mount Etna", "Mount Vesuvius", "Mount St. Helens", "Krakatoa",
              "B", "" },
            // Q48
            { "What is the capital of South Korea?",
              "Busan", "Incheon", "Seoul", "Pyongyang",
              "C", "" },
            // Q49
            { "The Danube River flows into which sea?",
              "Mediterranean Sea", "Caspian Sea", "Black Sea", "North Sea",
              "C", "" },
            // Q50
            { "What is the capital of the United Kingdom?",
              "London", "Edinburgh", "Cardiff", "Belfast",
              "A", "" },
              

            // Q51 — Landmark (Eiffel Tower area)
            { "Where is this landmark located?",
              "Italy", "Spain", "France", "Germany",
              "C", "ConquestPkg/images/Geography/51.png" }, //image

            // Q52 — Landmark (Great Wall / China)
            { "In which country is this located?",
              "Japan", "China", "Korea", "Thailand",
              "B", "ConquestPkg/images/Geography/52.png" }, //image

            // Q53 — Landmark (Statue of Liberty / USA)
            { "Where is this found?",
              "USA", "Canada", "UK", "France",
              "A", "ConquestPkg/images/Geography/53.png" }, //image

            // Q54 — Landmark (Taj Mahal / India)
            { "Which country is this in?",
              "Pakistan", "India", "Iran", "Turkey",
              "B", "ConquestPkg/images/Geography/54.png" }, //image

            // Q55 — Landmark (Colosseum / Italy)
            { "Which country is this landmark in?",
              "Greece", "Italy", "Spain", "France",
              "B", "ConquestPkg/images/Geography/55.png" }, //image

            // Q56 — Mountain (Everest)
            { "What mountain is shown?",
              "K2", "Everest", "Fuji", "Kilimanjaro",
              "B", "ConquestPkg/images/Geography/56.png" }, //image

            // Q57 — Landmark (Grand Canyon / USA)
            { "Where is this located?",
              "USA", "Mexico", "Canada", "Brazil",
              "A", "ConquestPkg/images/Geography/57.png" }, //image

            // Q58 — Desert (Sahara)
            { "Which desert is shown?",
              "Gobi", "Sahara", "Arctic", "Kalahari",
              "B", "ConquestPkg/images/Geography/58.png" }, //image

            // Q59 — Landmark (Amazon / South America)
            { "Where is this found?",
              "Africa", "Asia", "South America", "Australia",
              "C", "ConquestPkg/images/Geography/59.png" }, //image

            // Q60 — Niagara Falls border (USA & Canada)
            { "Which country borders this landmark?",
              "USA & Canada", "USA & Mexico", "Canada & Greenland", "Brazil & Argentina",
              "A", "ConquestPkg/images/Geography/60.png" }, //image

            // Q61 — Flag (Japan)
            { "Which country does this flag belong to?",
              "China", "Japan", "Korea", "Vietnam",
              "B", "ConquestPkg/images/Geography/61.png" }, //image

            // Q62 — Flag (USA)
            { "Which country is this?",
              "UK", "USA", "Australia", "Canada",
              "B", "ConquestPkg/images/Geography/62.png" }, //image

            // Q63 — Flag (France)
            { "Which country is this?",
              "Italy", "France", "Spain", "Netherlands",
              "B", "ConquestPkg/images/Geography/63.png" }, //image

            // Q64 — Flag (Brazil)
            { "Which country is this?",
              "Argentina", "Brazil", "Mexico", "Peru",
              "B", "ConquestPkg/images/Geography/64.png" }, //image

            // Q65 — Flag (Germany)
            { "Which country is this?",
              "Belgium", "Germany", "Austria", "Poland",
              "B", "ConquestPkg/images/Geography/65.png" }, //image

            // Q66 — Flag (Australia)
            { "Which country is shown?",
              "USA", "Australia", "Canada", "India",
              "B", "ConquestPkg/images/Geography/66.png" }, //image

            // Q67 — Continent map (Africa)
            { "Which continent is shown?",
              "Asia", "Africa", "Europe", "Australia",
              "B", "ConquestPkg/images/Geography/67.png" }, //image

            // Q68 — Flag (Italy)
            { "Which country is this?",
              "Spain", "Italy", "Greece", "Portugal",
              "B", "ConquestPkg/images/Geography/68.png" }, //image

            // Q69 — Flag (Japan again / context: countries)
            { "Which country is this?",
              "Korea", "Japan", "China", "Philippines",
              "B", "ConquestPkg/images/Geography/69.png" }, //image

            // Q70 — Continent map (Africa)
            { "Which continent is shown?",
              "Africa", "Philippines", "Europe", "Asia",
              "A", "ConquestPkg/images/Geography/70.png" }, //image

            // Q71 — City skyline (New York)
            { "Which city is shown?",
              "Los Angeles", "Chicago", "New York", "Miami",
              "C", "ConquestPkg/images/Geography/71.png" }, //image

            // Q72 — City skyline (Dubai)
            { "Which city is this?",
              "Doha", "Riyadh", "Dubai", "Abu Dhabi",
              "C", "ConquestPkg/images/Geography/72.png" }, //image

            // Q73 — City skyline (Paris)
            { "Which city is this?",
              "Rome", "Paris", "Madrid", "Berlin",
              "B", "ConquestPkg/images/Geography/73.png" }, //image

            // Q74 — City skyline (Tokyo)
            { "Which city is shown?",
              "Seoul", "Tokyo", "Beijing", "Bangkok",
              "B", "ConquestPkg/images/Geography/74.png" }, //image

            // Q75 — City skyline (London)
            { "Which city is this?",
              "Dublin", "London", "Edinburgh", "Glasgow",
              "B", "ConquestPkg/images/Geography/75.png" }, //image

            // Q76 — Structure (Canal)
            { "What is this structure?",
              "Dam", "Canal", "River", "Bridge",
              "B", "ConquestPkg/images/Geography/76.png" }, //image

            // Q77 — Continent (Antarctica)
            { "Which continent is this?",
              "Arctic", "Antarctica", "Europe", "Asia",
              "B", "ConquestPkg/images/Geography/77.png" }, //image

            // Q78 — Ecosystem (Coral Reef)
            { "What is this ecosystem?",
              "Desert", "Coral Reef", "Forest", "Glacier",
              "B", "ConquestPkg/images/Geography/78.png" }, //image

            // Q79 — Natural feature (Volcano)
            { "What natural feature is shown?",
              "Mountain", "Volcano", "Canyon", "Plateau",
              "B", "ConquestPkg/images/Geography/79.png" }, //image

            // Q80 — Landform (Delta)
            { "What landform is shown?",
              "Valley", "Delta", "Plateau", "Hill",
              "B", "ConquestPkg/images/Geography/80.png" }, //image
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