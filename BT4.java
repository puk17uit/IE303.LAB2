import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BT4 extends JFrame {
    public BT4() {
        setTitle("Flappy Bird");
        setSize(360, 640);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new GamePanel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BT4().setVisible(true));
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Image backgroundImage, birdImage, topPipeImage, bottomPipeImage;
    private int birdX = 100, birdY = 250, birdW = 50, birdH = 50;
    private int velocity = 0, gravity = 1;
    private Timer timer;
    private ArrayList<Pipe> pipes = new ArrayList<>();
    private Random rand = new Random();
    private int pipeGap = 150, pipeWidth = 60, pipeSpeed = 3;
    private int pipeSpawnTimer = 0;
    private int score = 0;
    private boolean gameOver = false;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        loadImages();
        startGame();
    }

    private void loadImages() {
        try {
            backgroundImage = ImageIO.read(new File("flappybirdbg.png"));
            birdImage = ImageIO.read(new File("flappybird.png"));
            topPipeImage = ImageIO.read(new File("toppipe.png"));
            bottomPipeImage = ImageIO.read(new File("bottompipe.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh: " + e.getMessage());
        }
    }

    private void startGame() {
        birdY = 250;
        velocity = 0;
        score = 0;
        pipes.clear();
        pipeSpawnTimer = 0;
        gameOver = false;

        timer = new Timer(20, this);
        timer.start();
    }

    private void endGame() {
        gameOver = true;
        timer.stop();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        if (birdImage != null)
            g.drawImage(birdImage, birdX, birdY, birdW, birdH, this);

        for (Pipe p : pipes) {
            g.drawImage(topPipeImage, p.x, 0, pipeWidth, p.topHeight, this);
            g.drawImage(bottomPipeImage, p.x, p.topHeight + pipeGap, pipeWidth,
                        getHeight() - (p.topHeight + pipeGap), this);
        }

        // Điểm
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);

        // Game Over
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("GAME OVER", 80, getHeight() / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press R to Restart", 100, getHeight() / 2 + 40);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

      
        velocity += gravity;
        birdY += velocity;
        if (birdY < 0) birdY = 0;
        if (birdY > getHeight() - birdH) {
            birdY = getHeight() - birdH;
            endGame();
        }

        
        pipeSpawnTimer++;
        if (pipeSpawnTimer >= 90) {
            int topHeight = rand.nextInt(300) + 50;
            pipes.add(new Pipe(getWidth(), topHeight));
            pipeSpawnTimer = 0;
        }

       
        for (int i = 0; i < pipes.size(); i++) {
            Pipe p = pipes.get(i);
            p.x -= pipeSpeed;

            
            Rectangle birdRect = new Rectangle(birdX, birdY, birdW, birdH);
            Rectangle topRect = new Rectangle(p.x, 0, pipeWidth, p.topHeight);
            Rectangle bottomRect = new Rectangle(p.x, p.topHeight + pipeGap, pipeWidth, getHeight() - (p.topHeight + pipeGap));

            if (birdRect.intersects(topRect) || birdRect.intersects(bottomRect)) {
                endGame();
            }

            // Tính điểm: chim vừa bay qua chính giữa pipe
            if (!p.passed && p.x + pipeWidth < birdX) {
                score++;
                p.passed = true;
            }

            // Xóa pipe cũ
            if (p.x + pipeWidth < 0) {
                pipes.remove(i);
                i--;
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)) {
            velocity = -12;
        } else if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            startGame();
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    class Pipe {
        int x, topHeight;
        boolean passed = false;

        Pipe(int x, int topHeight) {
            this.x = x;
            this.topHeight = topHeight;
        }
    }
}
