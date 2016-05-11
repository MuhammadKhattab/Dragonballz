package dragonball.model.battle;

import java.io.Serializable;
import java.util.*;

import dragonball.model.attack.*;
import dragonball.model.cell.Collectible;
import dragonball.model.character.fighter.*;
import dragonball.model.exceptions.*;
import dragonball.model.player.Player;

@SuppressWarnings("serial")
public class Battle implements Serializable {
	private BattleOpponent me;
	private BattleOpponent foe;
	private BattleOpponent attacker;
	private boolean meBlocking;
	private boolean foeBlocking;
	private BattleListener listener;

	private String turn;

	public Battle(BattleOpponent me, BattleOpponent foe) {
		this.me = me;
		this.foe = foe;
		this.attacker = me;

		setTurn("");

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

	public void attack(Attack attack) throws NotEnoughKiException {
		attack.onUse(attacker, getDefender(), (attacker == me && foeBlocking) || (attacker == foe && meBlocking));

		setTurn(String.format("%s used %s!\nExpected Damage: %d", ((Fighter) attacker).getName(), attack.getName(),
				attack.getAppliedDamage(attacker)));

		notifyOnBattleEvent(new BattleEvent(this, BattleEventType.ATTACK, attack));

		endTurn();
	}

	public void block() {
		if (attacker == me) {
			meBlocking = true;
		} else if (attacker == foe) {
			foeBlocking = true;
		}
		setTurn(String.format("%s chose to block!", ((Fighter) attacker).getName()));

		notifyOnBattleEvent(new BattleEvent(this, BattleEventType.BLOCK));

		endTurn();
	}

	public void use(Player player, Collectible collectible) throws NotEnoughSenzuBeansException {
		int senzu = player.getSenzuBeans();
		if (collectible.equals(Collectible.SENZU_BEAN)) {
			if (senzu > 0) {
				Fighter x = player.getActiveFighter();
				x.setHealthPoints(x.getMaxHealthPoints());
				x.setStamina(x.getMaxStamina());
				player.setSenzuBeans(senzu - 1);

				setTurn("You used a senzu bean!");

				notifyOnBattleEvent(new BattleEvent(this, BattleEventType.USE, collectible));

				endTurn();
			} else {
				throw new NotEnoughSenzuBeansException();
			}
		}
	}

	public BattleOpponent getDefender() {
		return attacker == me ? foe : me;
	}

	public void play() throws NotEnoughKiException {

		Fighter foe = (Fighter) attacker;
		Attack randomAttack;

		if (foe.getKi() > 2) {
			ArrayList<UltimateAttack> a = foe.getUltimateAttacks();
			if (a.size() > 0) {
				randomAttack = a.get(new Random().nextInt(a.size()));
				attack(randomAttack);
			} else
				attack(new PhysicalAttack());
		} else if (foe.getKi() > 1) {
			if (new Random().nextBoolean()) {
				ArrayList<SuperAttack> a = foe.getSuperAttacks();
				if (a.size() > 0) {
					randomAttack = a.get(new Random().nextInt(a.size()));
					attack(randomAttack);
				} else
					attack(new PhysicalAttack());
			} else {
				if (((Fighter) getDefender()).getKi() > 1) {
					if (new Random().nextBoolean()) {
						if (foe.getStamina() > 0) {
							block();
						} else {
							attack(new PhysicalAttack());
						}
					} else {
						attack(new PhysicalAttack());
					}
				} else {
					attack(new PhysicalAttack());
				}
			}
		} else if (new Random().nextBoolean()) {
			block();
		} else {
			attack(new PhysicalAttack());
		}

	}

	public void start() {
		notifyOnBattleEvent(new BattleEvent(this, BattleEventType.STARTED));
		notifyOnBattleEvent((new BattleEvent(this, BattleEventType.NEW_TURN)));
	}

	public void endTurn() {
		// reset block mode
		if (attacker == me && foeBlocking)
			foeBlocking = false;
		else if (attacker == foe && meBlocking)
			meBlocking = false;

		if (((Fighter) me).getHealthPoints() == 0) {
			notifyOnBattleEvent(new BattleEvent(this, BattleEventType.ENDED, foe));
		} else {
			if (((Fighter) foe).getHealthPoints() == 0) {
				notifyOnBattleEvent(new BattleEvent(this, BattleEventType.ENDED, me));
			} else {
				switchTurn();
				notifyOnBattleEvent(new BattleEvent(this, BattleEventType.NEW_TURN));
			}
		}

	}

	public void notifyOnBattleEvent(BattleEvent e) {
		if (listener != null) {
			listener.onBattleEvent(e);
		}
	}

	public void switchTurn() {
		attacker = getDefender();
		attacker.onDefenderTurn();
		getDefender().onAttackerTurn();

	}

	public String getTurn() {
		return turn;
	}

	public void setTurn(String turn) {
		this.turn = turn;
	}

}
