import java.awt.image.BufferedImage;

public class Weapons {
    private int damage;
    private BufferedImage rightImage;
    private BufferedImage leftImage;

    public Weapons(int damage, BufferedImage r, BufferedImage l) {
        this.damage = damage;
        rightImage = r;
        leftImage = l;
    }

    public int getDamage() {
        return damage;
    }

    public BufferedImage getLeftImage() {
        return leftImage;
    }

    public BufferedImage getRightImage() {
        return rightImage;
    }

}
