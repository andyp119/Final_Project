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
    private int speed;

    private ArrayList<BufferedImage> Rweapons;
    private ArrayList<BufferedImage> Lweapons;

    private ArrayList<Mob> mobs;

    private BufferedImage character;
    private int characterX;
    private int characterY;
    private boolean facingRight;

    private BufferedImage background;
    private Timer timer;
    private boolean[] pressedKeys;
    private int velocityY;
    private boolean onGround;
    private int ground;
    private int cameraX;
    private int gameW;
    private int gameH;
    private int spawnTimer;


    public DisplayPanel() {
        level = 0;
        health = 100;
        damage = 10;
        characterX = 350;
        characterY = 350;
        facingRight = true;
        pressedKeys = new boolean[128];
        velocityY = 0;
        onGround = true;
        ground = 350;
        cameraX = 0;
        gameW = 3000;
        gameH = 605;
        spawnTimer = 0;
        mobs = new ArrayList<>();

        Rweapons = new ArrayList<>();
        Lweapons = new ArrayList<>();
        timer = new Timer(50, this);
        try {
            background = ImageIO.read(new File("images/game.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            character = ImageIO.read(new File("images/rightCharacter.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
             Rweapons.add(ImageIO.read(new File("images/Rsword1.png")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            Lweapons.add(ImageIO.read(new File("images/Lsword1.png")));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            BufferedImage slimeImage = ImageIO.read(new File("images/slime.png"));
            mobs.add(new Mob(700, ground + 30, 50,50,slimeImage));
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
        g.drawImage(background, -cameraX, 0, gameW, gameH, null);
        g.drawImage(character, characterX-cameraX, characterY, 100,88,null);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.setColor(Color.BLACK);
        g.drawString("Level: " + level, 50,30);
        for (Mob m : mobs) {
            m.draw(g, cameraX);
        }
        if (facingRight) {
            g.drawImage(Rweapons.get(level), characterX-cameraX+75, characterY-38, 75, 75, null);
        } else {
            g.drawImage(Lweapons.get(level), characterX-cameraX-50, characterY-38, 75, 75, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && collision()) {

        }
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
            facingRight = false;
            try {
                character = ImageIO.read(new File("images/leftCharacter.png"));
            } catch (IOException error) { }
        }
        if (keyCode == KeyEvent.VK_D) {
            facingRight = true;
            try {
                character = ImageIO.read(new File("images/rightCharacter.png"));
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
        if (pressedKeys[KeyEvent.VK_A] && pressedKeys[KeyEvent.VK_SHIFT]) {
            characterX -= 10;

        }
        if (pressedKeys[KeyEvent.VK_D] && pressedKeys[KeyEvent.VK_SHIFT]) {
            characterX += 10;
        }
        if (pressedKeys[KeyEvent.VK_SPACE] && onGround) {
            velocityY -= 20;
            onGround = false;
        }

        characterY += velocityY;
        velocityY += 2;

        if (characterY >= ground) {
            characterY = ground;
            velocityY = 0;
            onGround = true;
        }
    }

    public void spawnMobs(int x, int y, String path) {
        try {
            BufferedImage image = ImageIO.read(new File(path));
            mobs.add(new Mob(x, y, 50,50,image));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public Rectangle characterRect() {
        int h = (int)(character.getHeight()*0.5);
        int w = (int)(character.getWidth()*0.5);
        return new Rectangle(characterX, characterY, w, h);
    }

    public boolean collision() {
        Rectangle characterRectangle = characterRect();
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).mobRectangle().intersects(characterRectangle)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveCharacter();
        if (getWidth() > 0) {
            cameraX = characterX - getWidth()/2;
        }
        if (cameraX < 0) {
            cameraX = 0;
        }
        if (cameraX > gameW-getWidth()) {
            cameraX = gameW - getWidth();
        }
        spawnTimer++;
        if (spawnTimer%100 == 0) {
            spawnMobs((int)(Math.random() * 3000), ground + 30,"images/slime.png");
            System.out.println("Spawned!");
        }
        for (Mob m : mobs) {
            m.move(characterX);
        }
        if (collision()) {
            health -= 1;
            System.out.println("Health: " + health);
        }
        if (health <= 0) {
            timer.stop();
        }
        repaint();
    }
}
