import java.util.ArrayList;
import java.util.Random;

public abstract class Entity implements Battle, Element<Entity> {
    protected ArrayList<Spell> abilities = new ArrayList<>();
    protected int life;
    public static int MAX_LIFE = 100;
    protected int mana;
    public static int MAX_MANA = 100;
    protected boolean imfire = false, imice = false, imearth = false;

    public Entity() {
        this.life = MAX_LIFE;
        this.mana = MAX_MANA;
    }

    public void regenLife(int life) {
        this.life += life;
        if (this.life > MAX_LIFE) {
            this.life = MAX_LIFE;
        }
    }

    public void regenMana(int mana) {
        this.mana += mana;
        if (this.mana > MAX_MANA) {
            this.mana = MAX_MANA;
        }
    }

    public void addAbility(Spell ability) {
        abilities.add(ability);
    }

    public ArrayList<Spell> getAbilities() {
        return abilities;
    }

    public void populateAbilities() {
        Random random = new Random();
        abilities.clear();

        int numAbilities = random.nextInt(4) + 3;

        int damage = random.nextInt(21) + 20;
        int cost = random.nextInt(7) + 10;

        addAbility(new Fire("Fire", damage, cost));
        addAbility(new Ice("Ice", damage, cost));
        addAbility(new Earth("Earth", damage, cost));

        for (int i = 3; i < numAbilities; i++) {
            int type = random.nextInt(3);
            switch (type) {
                case 0 -> addAbility(new Fire("Fire", damage, cost));
                case 1 -> addAbility(new Ice("Ice", damage, cost));
                case 2 -> addAbility(new Earth("Earth", damage, cost));
            }
        }
    }

    public boolean useAbility(Spell ability, Entity enemy) {
        if (mana < ability.getCost()) {
            System.out.println("Insufficient mana! Attack normally!");
            return false;
        }

        mana -= ability.getCost();
        System.out.println("Ability " + ability.getName() + " used. Remaining mana: " + mana);
        abilities.remove(ability);

        enemy.accept(ability);
        return true;
    }

    @Override
    public void accept(Visitor<Entity> visitor) {
        visitor.visit(this);
    }

    public int getLife() {
        return life;
    }

    public int getMana() {
        return mana;
    }

    public boolean isDead() {
        return life <= 0;
    }
}
