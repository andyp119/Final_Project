import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class DisplayPanel extends JPanel implements MouseListener, KeyListener, ActionListener {
    private int level;
    private int exp;
    private int health;
    private int damage;

    private ArrayList<Weapons> weapons;
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
    private int damageCD;


    public DisplayPanel() {
        level = 0;
        health = 500;
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
        damageCD = 0;
        mobs = new ArrayList<>();
        weapons = new ArrayList<>();

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
            BufferedImage weaponRight1 = ImageIO.read(new File("images/Rsword1.png"));
            BufferedImage weaponLeft1 = ImageIO.read(new File("images/Lsword1.png"));
            BufferedImage weaponRight2 = ImageIO.read(new File("images/Rsword2.png"));
            BufferedImage weaponLeft2 = ImageIO.read(new File("images/Lsword2.png"));
            BufferedImage weaponRight3 = ImageIO.read(new File("images/Rsword3.png"));
            BufferedImage weaponLeft3 = ImageIO.read(new File("images/Lsword3.png"));
            weapons.add(new Weapons(25, weaponRight1, weaponLeft1));
            weapons.add(new Weapons(50, weaponRight2, weaponLeft2));
            weapons.add(new Weapons(100, weaponRight3, weaponLeft3));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        try {
            BufferedImage slimeImage = ImageIO.read(new File("images/slime.png"));
            BufferedImage skeletonImage = ImageIO.read(new File("images/skeleton.png"));
            BufferedImage zombieImage = ImageIO.read(new File("images/zombie.png"));
            mobs.add(new Mob((int) (Math.random() * 1000), ground + 30, 50,2,slimeImage));
            mobs.add(new Mob((int) (Math.random() * 1000), ground + 30, 100,5,skeletonImage));
            mobs.add(new Mob((int) (Math.random() * 1000), ground + 30, 250,8,zombieImage));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        timer.start();
    }

    private void addMouseListener() { }

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
            g.drawImage(weapons.get(level).getRightImage(), characterX-cameraX+75, characterY-38, 75, 75, null);
        } else {
            g.drawImage(weapons.get(level).getLeftImage(), characterX-cameraX-50, characterY-38, 75, 75, null);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }


    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            attack();
        }

    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void keyTyped(KeyEvent e) { }

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
        return new Rectangle(characterX, characterY, 100, 88);
    }

    public Rectangle attackRect() {
        if (facingRight) {
            return new Rectangle(characterX, characterY - 10, 150, 108);
        } else {
            return new Rectangle(characterX - 50, characterY - 10, 150, 108);
        }
    }

    public void attack() {
        Rectangle hitbox = attackRect();
        for (int i = mobs.size() - 1; i >= 0; i--) {
            Mob mob = mobs.get(i);
            if (hitbox.intersects(mob.mobRectangle())) {
                mob.takeDamage(damage);
                if (mob.isDead()) {
                    mobs.remove(i);
                }
            }
        }
    }

    public Mob collidingMob() {
        Rectangle characterRectangle = characterRect();
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).mobRectangle().intersects(characterRectangle)) {
                return mobs.get(i);
            }
        }
        return null;
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
        if (health <= 0) {
            timer.stop();
        }
        if (damageCD > 0) {
            damageCD--;
        }
        for (int i = 0; i < mobs.size(); i++) {
            Mob mob = mobs.get(i);
            if (mob.mobRectangle().intersects(characterRect()) && damageCD == 0) {
                health -= mob.getDamage();
                damageCD = 10;
                System.out.println("Health: " + health);
                break;
            }
        }
        if (spawnTimer % 20 == 0) {
            health += 2;
        }
        repaint();
    }
}
