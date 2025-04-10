import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class BT2 extends JFrame {
    public BT2() {
        setTitle("Flappy Bird");
        setSize(360, 640);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new GamePanel());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BT2().setVisible(true);
        });
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Image backgroundImage;
    private Image birdImage;

    private int birdX = 100;
    private int birdY = 250;
    private int birdWidth = 50;
    private int birdHeight = 50;

    private int velocity = 0;
    private int gravity = 1;
    private Timer timer;

    public GamePanel() {
        setFocusable(true);
        addKeyListener(this);

        try {
            backgroundImage = ImageIO.read(new File("flappybirdbg.png")); 
            birdImage = ImageIO.read(new File("flappybird.png"));       
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
            g.drawImage(birdImage, birdX, birdY, birdWidth, birdHeight, this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Bird rơi xuống
        velocity += gravity;
        birdY += velocity;

        // Ngăn không cho bird ra khỏi khung
        if (birdY < 0) birdY = 0;
        if (birdY > getHeight() - birdHeight) birdY = getHeight() - birdHeight;

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Nhấn SPACE hoặc ENTER để nhảy lên
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
            velocity = -12; // Bird nhảy lên
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
