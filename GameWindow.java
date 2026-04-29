import javax.swing.*;
import java.awt.*;

public class GameWindow extends JPanel {
    private Image currentImage;
    private JFrame frame;

    public GameWindow() {
        frame = new JFrame("Sapphic Matcha Bar - Visuals");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.add(this);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null); 
    }

    public void updateDisplay(String filename) {
        // Using Toolkit is what allows GIFs to animate automatically
        this.currentImage = Toolkit.getDefaultToolkit().getImage(filename);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentImage != null) {
            g.drawImage(currentImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}