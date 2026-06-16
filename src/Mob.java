import java.awt.*;
import java.awt.image.BufferedImage;

public class Mob {
    private int posX;
    private int posY;
    private int health;
    private int maxHealth;
    private int damage;
    private int exp;
    private BufferedImage image;
    private int width;
    private int height;

    public Mob(int x, int y, int width, int height, int health, int damage, int exp, BufferedImage image) {
        posX = x;
        posY = y;
        this.height = height;
        this.width = width;
        this.health = health;
        maxHealth = health;
        this.damage = damage;
        this.exp = exp;
        this.image = image;
    }

    public void move(int playerX) {
        if (playerX < posX) {
            posX -= 2;
        }
        if (playerX > posX) {
            posX += 2;
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public void takeDamage(int dmg) {
        health -= dmg;
    }

    public Rectangle mobRectangle() {
        return new Rectangle(posX, posY, width, height);
    }

    public void knockback(int playerX) {
        if (posX > playerX) {
            posX += 30;
        } else {
            posX -= 30;
        }
    }

    public void draw(Graphics g, int cameraX) {
        g.drawImage(image, posX - cameraX, posY, width, height, null);
        int barWidth = (int)(width *0.7);
        int barHeight = 6;
        int barX = posX - cameraX + 15;
        int barY = posY - 10;
        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);
        int currentWidth = (int)((double)health / maxHealth * barWidth);
        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, currentWidth, barHeight);
        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);
    }

    public int getDamage() {
        return damage;
    }

    public int getHealth() {
        return health;
    }

    public int getExp() {
        return exp;
    }
}
