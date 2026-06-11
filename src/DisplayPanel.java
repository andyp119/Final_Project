import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.awt.event.MouseListener;

public class DisplayPanel extends JPanel implements MouseListener{
    private int level;
    private int health;
    private int damage;
    private BufferedImage[] weapons;
    private BufferedImage character;
    private BufferedImage background;

    public DisplayPanel() {
        level = 0;
        health = 100;
        damage = 10;
        try {
            background = ImageIO.read(new File("src/game.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        addMouseListener(this);
    }

    private void addMouseListener() {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.drawString("Level: " + level, 50,30);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
