import java.util.Random;

public class Enemy extends Entity {
    private Random random;

    public Enemy() {
        random = new Random();

        this.life = random.nextInt(51) + 50;
        this.mana = random.nextInt(51) + 50;

        this.imfire = random.nextBoolean();
        this.imice = random.nextBoolean();
        this.imearth = random.nextBoolean();

        populateAbilities();
    }

    @Override
    public void receiveDamage(int incomingDamage) {
        if (Math.random() < 0.5) {
            System.out.println("Enemy avoided the attack!");
            return;
        }

        this.life -= incomingDamage;

        if (this.life < 0) {
            this.life = 0;
        }

        System.out.println("Enemy received " + incomingDamage + " damage. Remaining life: " + this.life);
    }

    @Override
    public int getDamage() {
        int baseDamage = random.nextInt(11) + 10;

        if (Math.random() < 0.5) {
            baseDamage *= 2;
            System.out.println("Enemy got lucky! Damage has doubled!");
        }

        return baseDamage;
    }
}
