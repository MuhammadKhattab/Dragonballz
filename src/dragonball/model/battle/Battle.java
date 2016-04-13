package dragonball.model.battle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import dragonball.model.attack.Attack;
import dragonball.model.attack.PhysicalAttack;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.cell.Collectible;
import dragonball.model.character.fighter.Fighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.NotEnoughSenzuBeansException;
import dragonball.model.player.Player;

@SuppressWarnings("serial")
public class Battle implements Serializable{
	private BattleOpponent me;
	private BattleOpponent foe;
	private BattleOpponent attacker;
	private boolean meBlocking;
	private boolean foeBlocking;
	private BattleListener listener;

	public Battle(BattleOpponent me, BattleOpponent foe) {
		this.me = me;
		this.foe = foe;
		this.attacker = me;
		Fighter meFighter = (Fighter) me;
		meFighter.setHealthPoints(meFighter.getMaxHealthPoints());
		meFighter.setKi(0);
		meFighter.setStamina(meFighter.getMaxStamina());
		Fighter foeFighter = (Fighter) foe;
		foeFighter.setHealthPoints(foeFighter.getMaxHealthPoints());
		foeFighter.setKi(0);
		foeFighter.setStamina(foeFighter.getMaxStamina());
		if (meFighter instanceof Saiyan)
			((Saiyan) meFighter).setTransformed(false);
	}

	public BattleListener getListener() {
		return listener;
	}

	public BattleOpponent getMe() {
		return me;
	}

	public BattleOpponent getFoe() {
		return foe;
	}

	public BattleOpponent getAttacker() {
		return attacker;
	}

	public boolean isMeBlocking() {
		return meBlocking;
	}

	public boolean isFoeBlocking() {
		return foeBlocking;
	}

	public BattleListener getBattleListener() {
		return listener;
	}

	public void setListener(BattleListener battleListener) {
		this.listener = battleListener;
	}

	public ArrayList<Attack> getAssignedAttacks() {
		ArrayList<Attack> x = new ArrayList<Attack>();
		x.add(new PhysicalAttack());
		Fighter cur = (Fighter) attacker;
		x.addAll(cur.getSuperAttacks());
		x.addAll(cur.getUltimateAttacks());
		return x;
	}

	public void attack(Attack attack) throws Exception {
		if (attacker.equals(foe))
			attack.onUse(attacker, me, meBlocking);
		else
			attack.onUse(attacker, foe, foeBlocking);
		if (listener != null)
			listener.onBattleEvent(new BattleEvent(this, BattleEventType.ATTACK, attack));
		endTurn();
	}

	public void block() {
		if (attacker.equals(me))
			meBlocking = true;
		else
			foeBlocking = true;
		if (listener != null)
			listener.onBattleEvent(new BattleEvent(this, BattleEventType.BLOCK));
		endTurn();
	}

	public void use(Player player, Collectible collectible) throws NotEnoughSenzuBeansException {
		int senzu = player.getSenzuBeans();
		if (collectible.equals(Collectible.SENZU_BEAN)) {
			if (senzu>0) {
				Fighter x = player.getActiveFighter();
				x.setHealthPoints(x.getMaxHealthPoints());
				x.setStamina(x.getMaxStamina());
				player.setSenzuBeans(senzu - 1);
				if (listener != null)
					listener.onBattleEvent(new BattleEvent(this, BattleEventType.USE, collectible));
				endTurn();
			}
			else {
				throw new NotEnoughSenzuBeansException();
			}
		}
	}

	public BattleOpponent getDefender() {
		return attacker.equals(me) ? foe : me;
	}

	public void play() throws Exception {
		// TODO
		Random x = new Random();
		int i = x.nextInt(10);
		if (i == 1)
			block();
		else {
			Fighter fo = (Fighter) foe;
			int k = fo.getKi();
			Attack z = getAssignedAttacks().get(x.nextInt(getAssignedAttacks().size()));
			if (z instanceof SuperAttack && k > 1) {
				attack(z);
			} else if (z instanceof UltimateAttack && k > 3) {
				attack(z);
			} else {
				attack(new PhysicalAttack());
			}
		}
	}

	public void start() {
		if (listener != null) {
			listener.onBattleEvent(new BattleEvent(this, BattleEventType.STARTED));
			listener.onBattleEvent((new BattleEvent(this, BattleEventType.NEW_TURN)));
		}
	}

	public void endTurn() {
		// TODO
		// reset block mode
		if (attacker == me && foeBlocking)
			foeBlocking = false;
		else if (attacker == foe && meBlocking)
			meBlocking = false;
		if (((Fighter) me).getHealthPoints() == 0) {
			if (listener != null)
				listener.onBattleEvent(new BattleEvent(this, BattleEventType.ENDED, foe));
		} else {
			if (((Fighter) foe).getHealthPoints() == 0) {
				if (listener != null)
					listener.onBattleEvent(new BattleEvent(this, BattleEventType.ENDED, me));
			} else {
				if (listener != null)
					listener.onBattleEvent(new BattleEvent(this, BattleEventType.NEW_TURN));
				attacker.onAttackerTurn();
				getDefender().onDefenderTurn();
				switchTurn();
			}
		}

	}

	public void switchTurn() {
		attacker = getDefender();
	}

}
