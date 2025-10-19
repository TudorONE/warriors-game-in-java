public class Earth extends Spell {
    public Earth(String name, int damage, int cost) {
        super(name, damage, cost);
    }

    @Override
    public String toString() {
        return "Earth Spell: Damage=" + getDamage() + ", Cost=" + getCost();
    }
}
