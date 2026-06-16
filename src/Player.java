public class Player {
    private int health;
    private int maxHealth;
    private int level;
    private int exp;
    private String name;

    public Player(String name, int health, int level, int exp) {
        this.health = health;
        this.level = level;
        this.exp = exp;
        this.name = name;
        maxHealth = health;
    }

    public Player(int health, int level, int exp) {
        this.health = health;
        this.level = level;
        this.exp = exp;
    }

    public void takeDamage(int dmg) {
        health -= dmg;
    }

    public int getWeaponIndex() {
        if (level >= 7) {
            return 2;
        } else if (level >= 3) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getLevel() {
        return level;
    }

    public int getHealth() {
        return health;
    }

    public int getExp() {
        return exp;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void increaseMaxHP(int hp) {
        maxHealth += hp;
    }

    public int getMaxHealth() {
        return maxHealth;
    }
}
