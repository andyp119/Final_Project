import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class DisplayPanel extends JPanel implements MouseListener, KeyListener, ActionListener {
    private int level;
    private int health;
    private int damage;

    private ArrayList<BufferedImage> weapons;
    private BufferedImage character;
    private int characterX;
    private int characterY;
    private BufferedImage background;
    private Timer timer;
    private boolean[] pressedKeys;


    public DisplayPanel() {
        level = 0;
        health = 100;
        damage = 10;
        characterX = 800;
        characterY = 800;
        pressedKeys = new boolean[128];
        weapons = new ArrayList<>();
        timer = new Timer(50, this);
        try {
            background = ImageIO.read(new File("src/game.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            character = ImageIO.read(new File("src/rightCharacter.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
             weapons.add(ImageIO.read(new File("src/Rsword1.png")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
    }

    private void addMouseListener() {
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, null);
        g.drawImage(character, characterX, characterY, 100,88,null);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.drawString("Level: " + level, 50,30);
        g.setColor(Color.GREEN);
        g.drawLine(0, 900, 2000, 900);
        g.drawImage(weapons.get(level), characterX+75, characterY-38, 75, 75,null);
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

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        pressedKeys[keyCode] = true;
        if (keyCode == KeyEvent.VK_A) {
            try {
                character = ImageIO.read(new File("src/leftCharacter.png"));
            } catch (IOException error) { }
            try {
                weapons.add(ImageIO.read(new File("src/Lsword1.png")));
            } catch (IOException error) { }
        }
        if (keyCode == KeyEvent.VK_D) {
            try {
                character = ImageIO.read(new File("src/rightCharacter.png"));
            } catch (IOException error) { }
            try {
                weapons.add(ImageIO.read(new File("src/Rsword1.png")));
            } catch (IOException error) { }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        pressedKeys[key] = false;
    }

    public void moveCharacter() {
        if (pressedKeys[KeyEvent.VK_A]) {
            characterX -= 5;

        }
        if (pressedKeys[KeyEvent.VK_D]) {
            characterX += 5;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveCharacter();
        repaint();
    }
}
