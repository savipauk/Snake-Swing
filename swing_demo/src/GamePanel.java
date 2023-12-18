import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Random;

class GamePanel extends JPanel {
    // Tiles from 0 to 7, x,y
    int gridSpaces = 8;
    int gridSize = gridSpaces * gridSpaces; // == 512/8
    boolean[][] grid = new boolean[gridSpaces][gridSpaces];
    boolean EMPTY = false, TAKEN = true;

    Timer step;

    LinkedList<Snake> snake = new LinkedList<>();
    // snake head at 0

    Random ran = new Random();
    int foodX, foodY;
    private Graphics2D graphics;

    private GameManager parent;

    public GamePanel(GameManager _parent) {
        parent = _parent;

        setPreferredSize(new Dimension(GameManager.SIZE, GameManager.SIZE));
        setBackground(Color.decode("#0a1120"));

        step = new Timer(250, new RedrawGameListener(this));
        step.start();

        snake.add(new Snake(3, 4, Snake.RIGHT));
        addSnake();
        addSnake();
        addSnake();

        updateGrid();

        updateFood();
    }

    public void updateGrid() {
//        if (!step.isRunning()) return;

        for (int i = 0; i < gridSpaces; i++) {
            for (int j = 0; j < gridSpaces; j++) {
                grid[i][j] = EMPTY;
            }
        }

        for (Snake s : snake) {
            grid[s.x][s.y] = TAKEN;
        }
    }
    public void addSnake() {
        Snake lastSnake = snake.getLast();
        Snake newSnake = new Snake(0, 0, lastSnake.direction);
        switch (lastSnake.direction) {
            case Snake.UP -> {
                newSnake.x = lastSnake.x;
                newSnake.y = lastSnake.y + 1;
            }
            case Snake.DOWN -> {
                newSnake.x = lastSnake.x;
                newSnake.y = lastSnake.y - 1;
            }
            case Snake.LEFT -> {
                newSnake.x = lastSnake.x + 1;
                newSnake.y = lastSnake.y;
            }
            case Snake.RIGHT -> {
                newSnake.x = lastSnake.x - 1;
                newSnake.y = lastSnake.y;
            }
        }
        snake.add(newSnake);
    }

    void drawFilledRect(int tileX, int tileY, Color color) {
        graphics.setPaint(color);
        graphics.fillRect(tileX * gridSize, tileY * gridSize, gridSize, gridSize);
    }

    void drawGame() {
        for (int i = 0; i < snake.size(); i++) {
            Snake s = snake.get(i);
            if (i == 0) {
                drawFilledRect(s.x, s.y, Color.decode("#1c6a1f"));
            } else {
                drawFilledRect(s.x, s.y, Color.green);
            }
        }

        drawFilledRect(foodX, foodY, Color.red);
    }

    public void paint(Graphics g) {
        super.paint(g);
        graphics = (Graphics2D) g;

        for (int i = 0; i < 9; i++) {
            int newCoord = i * gridSize;
            graphics.drawLine(newCoord, 0, newCoord, GameManager.SIZE);
            graphics.drawLine(0, newCoord, GameManager.SIZE, newCoord);
        }

        drawGame();
    }

    public void updatePlayer() {
//        if (!step.isRunning()) return;

        if (snake.size() >= 63) gameOver(); // win
        for (int i = 0; i < snake.size(); i++) {
            Snake s = snake.get(i);

            if (i != 0) {
                s.direction = s.nextDirection;
                Snake previous = snake.get(i - 1);
                s.nextDirection = previous.direction;
            }

            switch (s.direction) {
                case Snake.LEFT -> --s.x;
                case Snake.RIGHT -> ++s.x;
                case Snake.UP -> --s.y;
                case Snake.DOWN -> ++s.y;
            }

            if (s.x == foodX && s.y == foodY) {
                System.out.println("eat");
                parent.addToScore();
                updateFood();
                addSnake();
            }

            if (s.x == 8 || s.x == -1 || s.y == -1 || s.y == 8) {
                gameOver();
            }
        }

        Snake head = snake.get(0);

        for (int i = 1; i < snake.size(); i++) {
            Snake s = snake.get(i);
            if (head.x == s.x && head.y == s.y) {
                gameOver();
            }
        }
    }

    public void gameOver() {

        /*

        stop step
        reset scores
        - GameManager needs to repaint MainPanel and hide GamePanel

         */

        System.out.println("game over");

        parent.gameOver();

        step.stop();

        snake.clear();
        snake.add(new Snake(3, 4, Snake.RIGHT));
        addSnake();
        addSnake();
        addSnake();

        updateGrid();
        updateFood();

        step.start();
    }

    public void updateKeyPress(int key) {
        if (!step.isRunning()) return;

        int direction = snake.get(0).direction;

        int directionBeforeKeyPress = direction;

        if (key == KeyEvent.VK_LEFT) {
            System.out.println("left arrow");

            direction = Snake.LEFT;
        } else if (key == KeyEvent.VK_RIGHT) {
            System.out.println("right arrow");

            direction = Snake.RIGHT;
        } else if (key == KeyEvent.VK_UP) {
            System.out.println("up arrow");

            direction = Snake.UP;
        } else if (key == KeyEvent.VK_DOWN) {
            System.out.println("down arrow");

            direction = Snake.DOWN;
        }

        // They are opposite so it's an impossible move
        if (Math.abs(direction - directionBeforeKeyPress) == 2) {
            direction = directionBeforeKeyPress;
        }

        snake.get(0).direction = direction;

        if (direction != directionBeforeKeyPress) {
            updatePlayer();
            repaint();
            step.restart();
        }
    }

    public void updateFood() {
        do {
            foodX = ran.nextInt(gridSpaces);
            foodY = ran.nextInt(gridSpaces);
        } while (grid[foodX][foodY] == TAKEN);
    }

    static class RedrawGameListener implements ActionListener {
        GamePanel game;
        public RedrawGameListener(GamePanel gamePanel) {
            game = gamePanel;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            game.updatePlayer();
            game.updateGrid();
            game.repaint();
        }
    }
}