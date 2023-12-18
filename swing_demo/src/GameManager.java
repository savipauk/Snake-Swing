import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameManager extends JFrame implements KeyListener {
    private GamePanel game;
    private final MainPanel mainMenu;
    boolean alreadySetListener = false;

    static final int SIZE = 512;

    private int score = 0;

    private JPanel myJPanel;

    public void addToScore() {
        score++;
        setTitle("Swing Snake Demo - Score " + score);
    }

    public void gameOver() {
        score = 0;
        setTitle("Swing Snake Demo");
//
//        remove(game);
//
//        add(mainMenu);
//        mainMenu.setVisible(true);
//
//        mainMenu.button.addActionListener(e -> {
//            mainMenu.setVisible(false);
//            remove(mainMenu);
//
//            game = new GamePanel(this);
//            add(game);
//        });
    }

    public GameManager() {
        super("Swing Snake Demo");
        super.setLayout(new FlowLayout());

        super.setResizable(false);

        getContentPane().setBackground(Color.white);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        mainMenu = new MainPanel();
        super.add(mainMenu);

        pack();

        // set keyboard focus
        setFocusable(true);
        addKeyListener(this);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // goes off twice in the start
        mainMenu.button.addActionListener(e -> {
            if (alreadySetListener) return;

            mainMenu.setVisible(false);
            super.remove(mainMenu);

            game = new GamePanel(this);
            super.add(game);

            alreadySetListener = true;
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameManager().setVisible(true));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        game.updateKeyPress(e.getKeyCode());
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}