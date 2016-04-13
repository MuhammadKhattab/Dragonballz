package dragonball.model.dragon;

import java.io.Serializable;
import java.util.*;

import dragonball.model.attack.*;

@SuppressWarnings("serial")
public class Dragon implements Serializable {

	private String name;
	private ArrayList<SuperAttack> superAttacks;
	private ArrayList<UltimateAttack> ultimateAttacks;
	private int senzuBeans;
	private int abilityPoints;

	public Dragon(String name, ArrayList<SuperAttack> superAttacks, ArrayList<UltimateAttack> ultimateAttacks,
			int senzuBeans, int abilityPoints) {
		this.name = name;
		this.superAttacks = superAttacks;
		this.ultimateAttacks = ultimateAttacks;
		this.senzuBeans = senzuBeans;
		this.abilityPoints = abilityPoints;
	}

	public String getName() {
		return name;
	}

	public ArrayList<SuperAttack> getSuperAttacks() {
		return superAttacks;
	}

	public ArrayList<UltimateAttack> getUltimateAttacks() {
		return ultimateAttacks;
	}

	public int getSenzuBeans() {
		return senzuBeans;
	}

	public int getAbilityPoints() {
		return abilityPoints;
	}

	public DragonWish[] getWishes() {
		ArrayList<DragonWish> z = new ArrayList<DragonWish>();
		if (senzuBeans > 0)
			z.add(new DragonWish(this, DragonWishType.SENZU_BEANS, senzuBeans));
		if (abilityPoints > 0)
			z.add(new DragonWish(this, DragonWishType.ABILITY_POINTS, abilityPoints));
		Random j = new Random();
		z.add(new DragonWish(this, DragonWishType.SUPER_ATTACK, superAttacks.get(j.nextInt(superAttacks.size()))));
		z.add(new DragonWish(this, DragonWishType.ULTIMATE_ATTACK,
				ultimateAttacks.get(j.nextInt(ultimateAttacks.size()))));
		return z.toArray(new DragonWish[z.size()]);
	}
}
