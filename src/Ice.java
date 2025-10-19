public class Ice extends Spell {
    public Ice(String name, int damage, int cost) {
        super(name, damage, cost);
    }

    @Override
    public String toString() {
        return "Ice Spell: Damage=" + getDamage() + ", Cost=" + getCost();
    }
}
