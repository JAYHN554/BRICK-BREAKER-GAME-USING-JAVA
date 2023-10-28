import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class BrickBreakerGame extends JPanel implements ActionListener, KeyListener
{
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 100;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_RADIUS = 10;
    private static final int BRICK_WIDTH = 80;
    private static final int BRICK_HEIGHT = 20;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED_X = 5;
    private static final int BALL_SPEED_Y = -5;

    private Timer timer;
    private boolean gameRunning;
    private int paddleX;
    private int ballX, ballY;
    private int ballSpeedX, ballSpeedY;
    private List<Rectangle> bricks;

    public BrickBreakerGame()
    {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        bricks = new ArrayList<>();
        initBricks();
        paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
        ballX = WIDTH / 2 - BALL_RADIUS;
        ballY = HEIGHT / 2 - BALL_RADIUS;
        ballSpeedX = BALL_SPEED_X;
        ballSpeedY = BALL_SPEED_Y;

        timer = new Timer(16, this);
        timer.start();
    }

    private void initBricks()
    {
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < WIDTH / BRICK_WIDTH; col++) {
                int brickX = col * BRICK_WIDTH;
                int brickY = row * BRICK_HEIGHT + 50;
                bricks.add(new Rectangle(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT));
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.BLUE);
        g2d.fillRect(paddleX, HEIGHT - PADDLE_HEIGHT - 10, PADDLE_WIDTH, PADDLE_HEIGHT);

        g2d.setColor(Color.RED);
        g2d.fillOval(ballX, ballY, BALL_RADIUS * 2, BALL_RADIUS * 2);

        g2d.setColor(Color.GREEN);
        for (Rectangle brick : bricks)
        {
            g2d.fillRect(brick.x, brick.y, BRICK_WIDTH, BRICK_HEIGHT);
        }

        if (bricks.isEmpty())
        {
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("You win!", WIDTH / 2 - 70, HEIGHT / 2);
        }

        if (!gameRunning)
        {
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 30));
            g2d.drawString("Game Over", WIDTH / 2 - 80, HEIGHT / 2);
            g2d.setFont(new Font("Arial", Font.BOLD, 20));
            g2d.drawString("Press 'R' to restart", WIDTH / 2 - 80, HEIGHT / 2 + 30);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (gameRunning)
        {
            ballX += ballSpeedX;
            ballY += ballSpeedY;

            if (ballX <= 0 || ballX >= WIDTH - BALL_RADIUS * 2)
            {
                ballSpeedX = -ballSpeedX;
            }
            if (ballY <= 0)
            {
                ballSpeedY = -ballSpeedY;
            }

            if (ballY >= HEIGHT - BALL_RADIUS * 2)
            {
                gameRunning = false;
            }

            if (new Rectangle(ballX, ballY, BALL_RADIUS * 2, BALL_RADIUS * 2)
                    .intersects(new Rectangle(paddleX, HEIGHT - PADDLE_HEIGHT - 10, PADDLE_WIDTH, PADDLE_HEIGHT))
                    && ballSpeedY > 0)
            {
                ballSpeedY = -ballSpeedY;
            }

            List<Rectangle> bricksToRemove = new ArrayList<>();
            for (Rectangle brick : bricks)
            {
                if (new Rectangle(ballX, ballY, BALL_RADIUS * 2, BALL_RADIUS * 2).intersects(brick)) {
                    bricksToRemove.add(brick);
                    ballSpeedY = -ballSpeedY;
                }
            }
            bricks.removeAll(bricksToRemove);

            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT && paddleX > 0)
        {
            paddleX -= PADDLE_SPEED;
        }
        if (key == KeyEvent.VK_RIGHT && paddleX < WIDTH - PADDLE_WIDTH)
        {
            paddleX += PADDLE_SPEED;
        }

        if (key == KeyEvent.VK_R && !gameRunning)
        {
            bricks.clear();
            initBricks();
            paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
            ballX = WIDTH / 2 - BALL_RADIUS;
            ballY = HEIGHT / 2 - BALL_RADIUS;
            ballSpeedX = BALL_SPEED_X;
            ballSpeedY = BALL_SPEED_Y;
            gameRunning = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() ->
        {
            JFrame frame = new JFrame("Brick Breaker");
            BrickBreakerGame game = new BrickBreakerGame();
            frame.add(game);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
