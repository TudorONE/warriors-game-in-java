public abstract class Spell implements Visitor<Entity> {
    private String name;
    private int damage = 10;
    private int cost;

    public Spell(String name, int damage, int cost) {
        this.name = name;
        this.damage = damage;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public int getCost() {
        return cost;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override
    public void visit(Entity entity) {
        if ((this instanceof Fire && entity.imfire) ||
                (this instanceof Ice && entity.imice) ||
                (this instanceof Earth && entity.imearth)) {
            System.out.println("Target is immune to " + this.getName() + "!");
        } else {
            entity.receiveDamage(this.getDamage());
            System.out.println("Used " + this.getName() + " dealing " + this.getDamage() + " damage.");
        }
    }

    @Override
    public String toString() {
        return name + " Spell: Damage=" + damage + ", Cost=" + cost;
    }
}
