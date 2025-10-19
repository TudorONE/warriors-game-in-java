public class Warrior extends Hero {
    public Warrior(String hname, int experience, int level) {
        super(hname, "Warrior", experience, level);
        this.imfire = true;
    }

    @Override
    public void receiveDamage(int incomingDamage) {
        int dexterityReduction = incomingDamage * this.dexterity / 100;
        int charismaReduction = incomingDamage * this.charisma / 100;
        int reducedDamage = incomingDamage - dexterityReduction - charismaReduction;

        if (reducedDamage < 0) {
            reducedDamage = 0;
        }

        if (Math.random() < 0.5) {
            reducedDamage /= 2;
            System.out.println(hname + " got lucky! Damage has halved!");
        }

        this.life -= reducedDamage;

        if (this.life < 0) {
            this.life = 0;
        }

        System.out.println(hname + " received " + reducedDamage + " damage. Remaining life: " + this.life);
    }

    @Override
    public int getDamage() {
        int baseDamage = this.strength + this.level;

        if (Math.random() < 0.5) {
            baseDamage *= 2;
            System.out.println(hname + " got lucky! Damage has doubled!");
        }

        System.out.println(hname + " will get " + baseDamage + " damage.");
        return baseDamage;
    }
}
