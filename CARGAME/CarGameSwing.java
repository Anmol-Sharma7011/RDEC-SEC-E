import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class CarGameSwing extends JPanel implements ActionListener, KeyListener {
    
    // Game constants
    private final int PANEL_WIDTH = 400;
    private final int PANEL_HEIGHT = 600;
    private final int CAR_WIDTH = 50;
    private final int CAR_HEIGHT = 100;
    
    // Player's car position
    private int playerX = PANEL_WIDTH / 2 - CAR_WIDTH / 2;
    private final int playerY = PANEL_HEIGHT - CAR_HEIGHT - 20;

    // Incoming cars
    private ArrayList<Rectangle> incomingCars = new ArrayList<>();
    private int carSpeed = 5;
    private int spawnCounter = 0;

    // Game state
    private boolean gameOver = false;
    private int score = 0;

    // Timer for game loop
    private Timer timer;

    // Constructor
    public CarGameSwing() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.GRAY);
        setFocusable(true);
        addKeyListener(this);

        // Timer to refresh game state every 20ms
        timer = new Timer(20, this);
        timer.start();
    }

    // Method to spawn new incoming cars
    private void spawnCar() {
        Random rand = new Random();
        int x = rand.nextInt(PANEL_WIDTH - CAR_WIDTH);
        incomingCars.add(new Rectangle(x, 0, CAR_WIDTH, CAR_HEIGHT));
    }

    // Game rendering
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw player's car
        g.setColor(Color.RED);
        g.fillRect(playerX, playerY, CAR_WIDTH, CAR_HEIGHT);

        // Draw incoming cars
        g.setColor(Color.BLUE);
        for (Rectangle car : incomingCars) {
            g.fillRect(car.x, car.y, car.width, car.height);
        }

        // Draw score
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 20);

        // Draw game over screen
        if (gameOver) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER", PANEL_WIDTH / 2 - 100, PANEL_HEIGHT / 2 - 20);
            g.drawString("Score: " + score, PANEL_WIDTH / 2 - 70, PANEL_HEIGHT / 2 + 20);

            timer.stop(); // Stop the game loop
        }
    }

    // Game loop logic
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            // Move incoming cars
            for (int i = 0; i < incomingCars.size(); i++) {
                Rectangle car = incomingCars.get(i);
                car.y += carSpeed;

                // Remove cars that go off-screen
                if (car.y > PANEL_HEIGHT) {
                    incomingCars.remove(i);
                    score++;
                }
            }

            // Spawn new cars periodically
            spawnCounter++;
            if (spawnCounter % 50 == 0) {
                spawnCar();
            }

            // Check for collisions
            for (Rectangle car : incomingCars) {
                if (car.intersects(new Rectangle(playerX, playerY, CAR_WIDTH, CAR_HEIGHT))) {
                    gameOver = true;
                }
            }

            repaint();
        }
    }

    // Key events to control player's car
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            playerX -= 15;
            if (playerX < 0) playerX = 0;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            playerX += 15;
            if (playerX > PANEL_WIDTH - CAR_WIDTH) playerX = PANEL_WIDTH - CAR_WIDTH;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
    @Override
    public void keyTyped(KeyEvent e) { }

    // Main method to start the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Car Game");
        CarGameSwing game = new CarGameSwing();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }
}
