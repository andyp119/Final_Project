import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

public class DisplayPanel extends JPanel implements MouseListener, KeyListener, ActionListener {
    private Player player;

    private ArrayList<Weapons> weapons;
    private boolean attacking;
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
    private boolean gameOver;
    private int spawnTimer;
    private int damageCD;

    private BufferedImage slimeImage;
    private BufferedImage skeletonImage;
    private BufferedImage zombieImage;
    private BufferedImage deadKirby;


    public DisplayPanel() throws IOException {
        player = new Player("Kirby", 100, 0, 0);
        characterX = 350;
        characterY = 350;
        facingRight = true;
        pressedKeys = new boolean[128];
        velocityY = 0;
        onGround = true;
        ground = 360;
        cameraX = 0;
        gameW = 3000;
        gameH = 605;
        gameOver = false;
        spawnTimer = 0;
        damageCD = 0;
        mobs = new ArrayList<>();
        slimeImage = ImageIO.read(new File("images/slime.png"));
        skeletonImage = ImageIO.read(new File("images/skeleton.png"));
        zombieImage = ImageIO.read(new File("images/zombie.png"));
        deadKirby = ImageIO.read(new File("images/deadCharacter.png"));
        weapons = new ArrayList<>();
        attacking = false;

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
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("You lost", 350, 240);
            g.drawImage(deadKirby, characterX-cameraX, characterY, 75, 75, null);
        } else {
            g.drawImage(character, characterX - cameraX, characterY, 100, 88, null);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.setColor(Color.BLACK);
            g.drawString("Level: " + player.getLevel(), 50, 30);
            g.drawString("Exp: " + player.getExp(), 50, 50);
            for (Mob m : mobs) {
                m.draw(g, cameraX);
            }
            int handX;
            int handY = characterY + 10;
            if (facingRight) {
                handX = characterX - cameraX + 85;
            } else {
                handX = characterX - cameraX - 25;
            }
            BufferedImage sword;
            if (facingRight) {
                sword = weapons.get(player.getWeaponIndex()).getRightImage();
            } else {
                sword = weapons.get(player.getWeaponIndex()).getLeftImage();
            }
            Graphics2D g2 = (Graphics2D) g.create();
            double angle = 0;
            if (attacking) {
                if (facingRight) {
                    angle = Math.toRadians(45);
                } else {
                    angle = Math.toRadians(-40);
                }
            }
            g2.rotate(angle, handX, handY);
            int swordWidth = 75;
            int swordHeight = 75;
            int swordX = handX - 20;
            int swordY = handY - 40;
            g2.drawImage(sword, swordX, swordY, swordWidth, swordHeight, null);
            g2.dispose();
            g.drawString("Health: " + player.getHealth(), 50, 70);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }


    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            attacking = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            attacking = false;
            attack();
            /*
            player.setLevel(player.getLevel() + 1);
            if (player.getLevel() >= 3) {
                player.setLevel(0);
            }
            */
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

    public void spawnSlime(int x, int y) {
        mobs.add(new Mob(x, y, 150, 8, 15,slimeImage));
    }

    public void spawnSkeleton(int x, int y) {
        mobs.add(new Mob(x, y, 500, 20, 20, skeletonImage));
    }

    public void spawnZombie(int x, int y) {
        mobs.add(new Mob(x, y, 1000, 32, 30, zombieImage));
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
                mob.takeDamage(weapons.get(player.getWeaponIndex()).getDamage());
                mob.knockback(characterX);
                if (mob.isDead()) {
                    player.setExp(player.getExp() + mob.getExp());
                    mobs.remove(i);
                }
            }
        }
    }

    public void levelUp() {
        if (player.getExp() >= 100) {
            player.setExp(0);
            player.setLevel(player.getLevel() + 1);
            player.setHealth(player.getHealth() + 20);
        }
    }

    public void spawnMobs() {
        if (spawnTimer%100 == 0) {
            int x = (int)(Math.random() * gameW);
            int random = (int)(Math.random() * 3);
            if (random == 0) {
                spawnSlime(x, ground + 30);
            } else if (random == 1) {
                spawnSkeleton(x, ground + 30);
            } else {
                spawnZombie(x, ground + 30);
            }
        }
    }

    public void regenRate() {
        if (spawnTimer % 20 == 0) {
            player.setHealth(Math.min(player.getHealth()+1, 100));
        }
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
        spawnMobs();
        for (Mob m : mobs) {
            m.move(characterX);
        }
        if (player.getHealth() <= 0) {
            timer.stop();
        }
        if (damageCD > 0) {
            damageCD--;
        }
        for (int i = 0; i < mobs.size(); i++) {
            Mob mob = mobs.get(i);
            if (mob.mobRectangle().intersects(characterRect()) && damageCD == 0) {
                player.setHealth(player.getHealth() - mob.getDamage());
                damageCD = 10;
                break;
            }
        }
        if (player.getHealth() <= 0) {
            gameOver = true;
            timer.stop();
        }
        levelUp();
        regenRate();
        repaint();
    }
}
