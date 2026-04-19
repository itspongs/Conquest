package ConquestPkg;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.*;

public class ProgrammingQuiz {

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

    public ProgrammingQuiz(JFrame frame, String playerName) {

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

        JLabel catLabel = new JLabel("Category: Programming");
        catLabel.setFont(new Font("Arial", Font.BOLD, 26));
        catLabel.setForeground(Color.WHITE);
        catLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel funFactText = new JLabel(
            "<html><br>Fun Fact: Programming uses logic,<br>"
            + "syntax, and problem-solving to tell a computer<br>"
            + "exactly what to do.</html>"
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

        JLabel category = new JLabel("Category: Programming");
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
            System.out.println("[ProgrammingQuiz] Loaded via classloader: " + url);
            return new ImageIcon(url);
        }

        url = getClass().getResource("/" + path);
        if (url != null) {
            System.out.println("[ProgrammingQuiz] Loaded via getResource: " + url);
            return new ImageIcon(url);
        }

        File f = new File(path);
        if (f.exists()) {
            System.out.println("[ProgrammingQuiz] Loaded via working dir: " + f.getAbsolutePath());
            return new ImageIcon(f.getAbsolutePath());
        }

        try {
            File codeBase = new File(
                getClass().getProtectionDomain().getCodeSource().getLocation().toURI()
            );

            File base = codeBase.isDirectory() ? codeBase : codeBase.getParentFile();
            File fromCode = new File(base, path);
            if (fromCode.exists()) {
                System.out.println("[ProgrammingQuiz] Loaded via code base: " + fromCode.getAbsolutePath());
                return new ImageIcon(fromCode.getAbsolutePath());
            }
        } catch (Exception ignored) {}

        System.err.println("[ProgrammingQuiz] FAILED to load image: " + path);
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

        JLabel catLbl = new JLabel("CATEGORY: Programming");
        catLbl.setFont(new Font("Arial", Font.BOLD, 16));
        catLbl.setForeground(Color.WHITE);

        topBar.add(titleLbl, BorderLayout.WEST);
        topBar.add(catLbl,   BorderLayout.EAST);

        JLabel imgLabel = null;
        if (q.image != null) {
            ImageIcon icon = loadImage(q.image);
            if (icon != null) {
                int origW = icon.getIconWidth();
                int origH = icon.getIconHeight();
                int maxSize = 220;
                int newW, newH;
                if (origW >= origH) {
                    newW = maxSize;
                    newH = (int)((double) origH / origW * maxSize);
                } else {
                    newH = maxSize;
                    newW = (int)((double) origW / origH * maxSize);
                }
                Image img = icon.getImage().getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
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
                new QuizResults(frame, playerName, score, quizQuestions.size(), "Programming");
            }
        }).start();
    }




    //TO THE ASSIGNED TASKER
    //PUT ALL THE QUESTION 1-50 TO THE 2D ARRAYS
    //FROM QUESTION 31-50. DOWNLOAD OR SCREENSHOT THE IMAGES FOR EACH QUESTION.
    //FROM QUESTION 31-50. ALL THE PICTURES SHOULD BE COMPILED AT (ConquestPkg/images/'categoryName') MUST BE RENAMED USING NUMBERS (31.png)
    //FROM QUESTION 31-50. FOLLOW THE FORMAT BELOW, location => "ConquestPkg/images/'categoryName"
    //FOLLOW THE REFERENCE/GUIDE (DrivingQuiz.java)

    private void loadQuestions() {

        // 2D array: { question, choiceA, choiceB, choiceC, choiceD, correct, image (or "") }
        String[][] data = {

            // Q1
            { "What is a Java program?",
                "Hardware",
                "Sequence of statements",
                "Network",
                "Browser",
                "B", "" },
            // Q2
            { "Which method is required in Java?",
                "run()",
                "main()",
                "start()",
                "execute()",
                "B", "" },
            // Q3
            { "What symbol ends a statement?",
                ",",
                ";",
                ":",
                ".",
                "B", "" },
            // Q4
            { "Which is a valid data type?",
                "integer",
                "int",
                "number",
                "decimal",
                "B", "" },
            // Q5
            { "What is a boolean value?",
                "yes",
                "1",
                "true",
                "ok",
                "C", "" },
            // Q6
            { "What does // mean?",
                "Divide",
                "Comment",
                "Loop",
                "Error",
                "B", "" },
            // Q7
            { "Which is used for addition?",
                "*",
                "+",
                "%",
                "/",
                "B", "" },
            // Q8
            { "What is 10 / 0?",
                "0",
                "Error",
                "10",
                "1",
                "B", "" },
            // Q9
            { "Which error is found before running?",
                "Runtime",
                "Logical",
                "Compile-time",
                "Syntax run",
                "C", "" },
            // Q10
            { "Which error gives wrong output?",
                "Compile",
                "Runtime",
                "Logical",
                "Syntax",
                "C", "" },
            // Q11
            { "What is a variable?",
                "Fixed value",
                "Changing value holder",
                "Function",
                "Class",
                "B", "" },
            // Q12
            { "What is a constant?",
                "Changes value",
                "Fixed value",
                "Loop",
                "Class",
                "B", "" },
            // Q13
            { "Keyword for constant?",
                "const",
                "static",
                "final",
                "fixed",
                "C", "" },
            // Q14
            { "What is an array?",
                "Single value",
                "Multiple values in one variable",
                "Function",
                "Object",
                "B", "" },
            // Q15
            { "First index of array?",
                "1",
                "0",
                "-1",
                "10",
                "B", "" },
            // Q16
            { "What does loop do?",
                "Stop program",
                "Repeat code",
                "Print",
                "Delete",
                "B", "" },
            // Q17
            { "Which loop checks first?",
                "do-while",
                "while",
                "repeat",
                "none",
                "B", "" },
            // Q18
            { "Which loop runs at least once?",
                "while",
                "for",
                "do-while",
                "if",
                "C", "" },
            // Q19
            { "What is a method?",
                "Variable",
                "Block of code",
                "Loop",
                "Data",
                "B", "" },
            // Q20
            { "What is return used for?",
                "Exit method with value",
                "Loop",
                "Print",
                "Error",
                "A", "" },
            // Q21
            { "What is casting?",
                "Loop",
                "Convert data type",
                "Error",
                "Class",
                "B", "" },
            // Q22
            { "Which is string?",
                "123",
                "\"Hello\"",
                "true",
                "5.6",
                "B", "" },
            // Q23
            { "What is concatenation?",
                "Multiply",
                "Join values",
                "Divide",
                "Subtract",
                "B", "" },
            // Q24
            { "Which is correct?",
                "int x = \"Hello\"",
                "String x = \"Hello\"",
                "double x = \"Hi\"",
                "char x = \"A\"",
                "B", "" },
            // Q25
            { "What is Scanner used for?",
                "Output",
                "Input",
                "Loop",
                "Error",
                "B", "" },
            // Q26
            { "What is IDE?",
                "Software for coding",
                "Hardware",
                "Game",
                "OS",
                "A", "" },
            // Q27
            { "What is debugging?",
                "Writing code",
                "Fixing errors",
                "Compiling",
                "Running",
                "B", "" },
            // Q28
            { "What is class?",
                "Variable",
                "Blueprint of object",
                "Loop",
                "Input",
                "B", "" },
            // Q29
            { "What is object?",
                "Instance of class",
                "Function",
                "Loop",
                "Error",
                "A", "" },
            // Q30
            { "What is syntax error?",
                "Wrong output",
                "Wrong format",
                "Crash",
                "Loop",
                "B", "" },
            // Q31
            { "What is the error?",
                "Runtime",
                "Missing ;",
                "Logic",
                "Loop",
                "B", "ConquestPkg/images/Programming/31.png" },
            // Q32
            { "What happens?",
                "Works",
                "Error",
                "Output 10",
                "Output 0",
                "B", "ConquestPkg/images/Programming/32.png" },
            // Q33
            { "What error?",
                "Compile",
                "Runtime",
                "Array out of bounds",
                "Logic",
                "C", "ConquestPkg/images/Programming/33.png" },
            // Q34
            { "What does it do?",
                "Repeat code",
                "Stop",
                "Error",
                "Print once",
                "A", "ConquestPkg/images/Programming/34.png" },
            // Q35
            { "When does it stop?",
                "Always",
                "When false",
                "Never",
                "Random",
                "B", "ConquestPkg/images/Programming/35.png" },
            // Q36
            { "How many times run?",
                "0",
                "At least 1",
                "2",
                "Infinite",
                "B", "ConquestPkg/images/Programming/36.png" },
            // Q37
            { "What happens?",
                "Add",
                "Concatenate",
                "Error",
                "Multiply",
                "B", "ConquestPkg/images/Programming/37.png" },
            // Q38
            { "What is result type?",
                "int",
                "double",
                "depends",
                "casted type",
                "D", "ConquestPkg/images/Programming/38.png" },
            // Q39
            { "Which is correct?",
                "int x = 5;",
                "int = x 5",
                "x int = 5",
                "5 = x",
                "A", "ConquestPkg/images/Programming/39.png" },
            // Q40
            { "What keyword used?",
                "static",
                "final",
                "const",
                "fixed",
                "B", "ConquestPkg/images/Programming/40.png" },
            // Q41
            { "What does it do?",
                "Output",
                "Input",
                "Loop",
                "Error",
                "B", "ConquestPkg/images/Programming/41.png" },
            // Q42
            { "What is this?",
                "Input",
                "Output",
                "Loop",
                "Variable",
                "B", "ConquestPkg/images/Programming/42.png" },
            // Q43
            { "What is it?",
                "Method",
                "Class",
                "Loop",
                "Array",
                "B", "ConquestPkg/images/Programming/43.png" },
            // Q44
            { "What is size?",
                "depends",
                "fixed",
                "random",
                "none",
                "B", "ConquestPkg/images/Programming/44.png" },
            // Q45
            { "What does it do?",
                "Decrease",
                "Increase by 1",
                "Multiply",
                "Reset",
                "B", "ConquestPkg/images/Programming/45.png" },
            // Q46
            { "What is it?",
                "Loop",
                "Condition",
                "Class",
                "Error",
                "B", "ConquestPkg/images/Programming/46.png" },
            // Q47
            { "What happens?",
                "Single run",
                "Loop inside loop",
                "Error",
                "Skip",
                "B", "ConquestPkg/images/Programming/47.png" },
            // Q48
            { "What is it?",
                "Declaration",
                "Call",
                "Loop",
                "Input",
                "B", "ConquestPkg/images/Programming/48.png" },
            // Q49
            { "What is the purpose?",
                "Loop",
                "Handle errors",
                "Print",
                "Input",
                "B", "ConquestPkg/images/Programming/49.png" },
            // Q50
            { "What is the result?",
                "depends",
                "Output value",
                "Error",
                "Loop",
                "B", "ConquestPkg/images/Programming/50.png" }
        };


        for (String[] row : data) {
            String[] choices = new String[]{ row[1], row[2], row[3], row[4] };
            int correctIdx = row[5].charAt(0) - 'A';

            java.util.List<Integer> indices = new java.util.ArrayList<>(java.util.Arrays.asList(0, 1, 2, 3));
            java.util.Collections.shuffle(indices);

            String[] shuffled = new String[4];
            int newCorrectIdx = 0;
            for (int i = 0; i < 4; i++) {
                shuffled[i] = choices[indices.get(i)];
                if (indices.get(i) == correctIdx) newCorrectIdx = i;
            }

            allQuestions.add(new Question(
                row[0],
                shuffled,
                (char)('A' + newCorrectIdx),
                row[6].isEmpty() ? null : row[6]
            ));
        }
    }
}
