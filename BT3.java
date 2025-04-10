import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BT3 extends JFrame {
    public BT3() {
        setTitle("Flappy Bird");
        setSize(360, 640);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new GamePanel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BT3().setVisible(true));
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Image backgroundImage, birdImage, topPipeImage, bottomPipeImage;
    private int birdX = 100, birdY = 250, birdW = 50, birdH = 50;
    private int velocity = 0, gravity = 1;
    private Timer timer;
    private ArrayList<Pipe> pipes = new ArrayList<>();
    private Random rand = new Random();
    private int pipeGap = 150, pipeWidth = 60;
    private int pipeSpeed = 3;
    private int pipeSpawnTimer = 0;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);
        try {
            backgroundImage = ImageIO.read(new File("flappybirdbg.png"));
            birdImage = ImageIO.read(new File("flappybird.png"));
            topPipeImage = ImageIO.read(new File("toppipe.png"));
            bottomPipeImage = ImageIO.read(new File("bottompipe.png"));
        } catch (IOException e) {
            System.out.println("Không thể tải ảnh: " + e.getMessage());
        }

        timer = new Timer(20, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null)
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        if (birdImage != null)
            g.drawImage(birdImage, birdX, birdY, birdW, birdH, this);

        for (Pipe p : pipes) {
            if (topPipeImage != null)
                g.drawImage(topPipeImage, p.x, 0, pipeWidth, p.topHeight, this);
            if (bottomPipeImage != null)
                g.drawImage(bottomPipeImage, p.x, p.topHeight + pipeGap, pipeWidth, getHeight() - (p.topHeight + pipeGap), this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        velocity += gravity;
        birdY += velocity;
        if (birdY < 0) birdY = 0;
        if (birdY > getHeight() - birdH) birdY = getHeight() - birdH;

        
        pipeSpawnTimer++;
        if (pipeSpawnTimer >= 90) { // tạo pipe mỗi ~1.8s
            int topHeight = rand.nextInt(300) + 50;
            pipes.add(new Pipe(getWidth(), topHeight));
            pipeSpawnTimer = 0;
        }

        
        for (int i = 0; i < pipes.size(); i++) {
            Pipe p = pipes.get(i);
            p.x -= pipeSpeed;
            if (p.x + pipeWidth < 0) {
                pipes.remove(i);
                i--;
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            velocity = -12;
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    class Pipe {
        int x;
        int topHeight;

        Pipe(int x, int topHeight) {
            this.x = x;
            this.topHeight = topHeight;
        }
    }
}