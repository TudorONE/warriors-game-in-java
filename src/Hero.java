public abstract class Hero extends Entity {
    protected String hname;
    protected String profession;
    protected int experience;
    protected int level;

    protected int strength;
    protected int charisma;
    protected int dexterity;

    public Hero(String hname, String profession, int experience, int level) {
        super();
        this.hname = hname;
        this.profession = profession;
        this.experience = experience;
        this.level = level;
        this.strength = 20;
        this.charisma = 20;
        this.dexterity = 20;

        populateAbilities();
    }

    public void addExperience(int expGained) {
        this.experience += expGained;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int expNeeded = 100 * this.level;
        while (this.experience >= expNeeded) {
            levelUp();
            expNeeded = 100 * this.level;
        }
    }

    private void levelUp() {
        this.level++;
        this.strength += this.level * 3;
        this.charisma += this.level;
        this.dexterity += this.level * 2;
        System.out.println(hname + " leveled up to level " + this.level  + "!");
        System.out.println("Stats: Strength " + this.strength +
                ", Charisma " + this.charisma +
                ", Dexterity " + this.dexterity);
        for (Spell ability : this.abilities) {
            ability.setDamage(this.level * 5 + ability.getDamage());
        }
    }

    public String getHname() {
        return hname;
    }

    public String getProfession() {
        return profession;
    }

    public int getExperience() {
        return experience;
    }

    public int getLevel() {
        return level;
    }
}
