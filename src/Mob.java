import java.awt.*;
import java.awt.image.BufferedImage;

public class Mob {
    private int posX;
    private int posY;
    private int health;
    private int damage;
    private BufferedImage image;

    public Mob(int x, int y, int health, int damage, BufferedImage image) {
        posX = x;
        posY = y;
        this.health = health;
        this.damage = damage;
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

    public Rectangle mobRectangle() {
        int h = (int)(image.getHeight() * 0.4);
        int w = (int)(image.getWidth() * 0.4);
        return new Rectangle(posX, posY, w, h);
    }

    public void draw(Graphics g, int cameraX) {
        g.drawImage(image, posX-cameraX, posY, 80,80,null);
    }
}
