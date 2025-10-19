public class Fire extends Spell {
    public Fire(String name, int damage, int cost) {
        super(name, damage, cost);
    }

    @Override
    public String toString() {
        return "Fire Spell: Damage=" + getDamage() + ", Cost=" + getCost();
    }
}
