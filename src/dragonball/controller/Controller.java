package dragonball.controller;

import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import dragonball.model.attack.*;
import dragonball.model.battle.*;
import dragonball.model.cell.Collectible;
import dragonball.model.character.fighter.*;
import dragonball.model.dragon.*;
import dragonball.model.exceptions.*;
import dragonball.model.game.*;
import dragonball.view.*;

@SuppressWarnings("serial")
public class Controller implements GameListener, MouseListener, KeyListener, Serializable {

	// TODO
	// save then lose a battle -> battle view won't disappear

	// TODO icons:
	// physical damage
	// blast damage
	// ki
	// stamina
	// abiltiy points
	// level
	// fighter name
	// ultimate attack
	// transformed saiyan
	// current turn

	private Game game;
	private Dragon dragon;
	private Battle battle;
	private String check;

	private WorldView worldView;
	private BattleView battleView;
	private DragonView dragonView;
	private AssignAttackView assignAttackView;

	public Controller(Game game) {
		this.game = game;
		game.setListener(this);

		System.out.println(game.getWorld());

		check = "";

		worldView = new WorldView(game.getWorld(), game.getPlayer());
		addButtonsListeners();
	}

	public void addButtonsListeners() {
		ArrayList<JButton> a = worldView.getCellz();
		for (JButton x : a)
			x.addKeyListener(this);
		worldView.getSwitchFighter().addMouseListener(this);
		worldView.getNewFighter().addMouseListener(this);
		worldView.update();
		worldView.getUpgrade().addMouseListener(this);
		worldView.getAssignAttacks().addMouseListener(this);
		worldView.getSave().addMouseListener(this);
	}

	@Override
	public void onDragonCalled(Dragon dragon) {
		dragonView = new DragonView(dragon);
		this.dragon = dragon;
		dragonView.getAbility().addMouseListener(this);
		dragonView.getSenzu().addMouseListener(this);
		dragonView.getSuperAttack().addMouseListener(this);
		dragonView.getUltimateAttack().addMouseListener(this);
		worldView.setVisible(false);
	}

	@Override
	public void onCollectibleFound(Collectible collectible) {
		String s = "";
		if (collectible.equals(Collectible.SENZU_BEAN))
			s = "Senzu bean";
		else
			s = "Dragon Ball";
		worldView.drawCollectible(s);
	}

	@Override
	public void onBattleEvent(BattleEvent e) {
		if (e.getType() == BattleEventType.STARTED) {
			int row = game.getWorld().getPlayerRow();
			int col = game.getWorld().getPlayerColumn();
			int index = row * 10 + col;
			JButton button = worldView.getCellz().get(index);
			button.setIcon(null);

			worldView.setVisible(false);
			battle = ((Battle) e.getSource());
			battleView = new BattleView(battle);
			battleView.getBlock().addMouseListener(this);
			battleView.getUse().addMouseListener(this);
			battleView.getPhysical().addMouseListener(this);
			battleView.getZuper().addMouseListener(this);
			battleView.getUltimate().addMouseListener(this);
		} else {
			if (e.getType() == BattleEventType.NEW_TURN) {
				battleView.update();
				if (((Battle) e.getSource()).getAttacker() == (((Battle) e.getSource()).getFoe())) {
					while (true) {
						try {
							battle.play();
							battleView.foePhysicalAttack();
							break;
						} catch (Exception e1) {
						}
					}
				}
				battleView.update();
			} else {
				if (e.getType() == BattleEventType.ENDED) {
					if (e.getWinner().equals(game.getPlayer().getActiveFighter())) {
						battleView.update();
						JOptionPane.showMessageDialog(null, "You Win!");

						if (battleView.getPrevLevel() < game.getPlayer().getActiveFighter().getLevel())
							JOptionPane.showMessageDialog(null,
									String.format("You Leveled Up!\nLevel: %d\nAbility Points: %d",
											game.getPlayer().getActiveFighter().getLevel(),
											game.getPlayer().getActiveFighter().getAbilityPoints()));

						JOptionPane.showMessageDialog(null,
								String.format("EXP: %d/%d", game.getPlayer().getActiveFighter().getXp(),
										game.getPlayer().getActiveFighter().getTargetXp()));

						JTextArea text = new JTextArea(game.getGainedSkills());
						text.setEditable(false);
						JScrollPane scrollPane = new JScrollPane(text);
						JOptionPane.showMessageDialog(null, scrollPane);

						NonPlayableFighter foe = ((NonPlayableFighter) (battle.getFoe()));
						if (foe.isStrong()) {
							JOptionPane.showMessageDialog(null,
									"You defeated the boss?\nYou defeated the boss!!\nWhy you do this!");
							worldView = new WorldView(game.getWorld(), game.getPlayer());
							addButtonsListeners();
						} else {
							worldView.setVisible(true);
						}
					} else {
						// if (!game.getLastSavedFile().equals(check)) {
						// worldView = new WorldView(game.getWorld(),
						// game.getPlayer());
						// addButtonsListeners();
						// JOptionPane.showMessageDialog(null, "Loading last
						// checkpoint...");
						// } else {
						battleView.update();
						JOptionPane.showMessageDialog(null, "You lose!");
						worldView = new WorldView(game.getWorld(), game.getPlayer());
						addButtonsListeners();
						// }
					}
					battleView.setVisible(false);
					worldView.update();
				}
			}

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getComponent() instanceof JLabel) {
			if (((JLabel) e.getSource()).getName().equals("senzuDragon")) {
				DragonWish w = dragon.getWishes()[0];
				game.getPlayer().chooseWish(w);
				JOptionPane.showMessageDialog(null, "Senzu beans increased by: " + dragon.getSenzuBeans());
				exitDragonView();
			} else {
				if (((JLabel) e.getSource()).getName().equals("abilityDragon")) {
					DragonWish w = dragon.getWishes()[1];
					game.getPlayer().chooseWish(w);
					JOptionPane.showMessageDialog(null, "ability points increased by: " + dragon.getAbilityPoints());
					exitDragonView();
				} else {
					if (((JLabel) e.getSource()).getName().equals("ultimateDragon")) {
						DragonWish w = dragon.getWishes()[3];
						game.getPlayer().chooseWish(w);
						JOptionPane.showMessageDialog(null,
								String.format("You earned: %s", w.getUltimateAttack().getName()));
						exitDragonView();
					} else {
						if (((JLabel) e.getSource()).getName().equals("superDragon")) {
							DragonWish w = dragon.getWishes()[2];
							game.getPlayer().chooseWish(w);
							JOptionPane.showMessageDialog(null,
									String.format("You earned: %s", w.getSuperAttack().getName()));
							exitDragonView();
						}
					}
				}
			}
		} else {
			if (e.getComponent() instanceof JButton) {
				if (((JButton) e.getSource()).getName().equals("switch")) {
					String s = worldView.switchFighter();
					if (s != null && s.length() > 0) {
						ArrayList<PlayableFighter> fighters = game.getPlayer().getFighters();
						for (int i = 0; i < fighters.size(); i++) {
							if (s.equals(fighters.get(i).getName())) {
								game.getPlayer().setActiveFighter(fighters.get(i));
								break;
							}
						}
						worldView.update();
						worldView.updateRace();
					}
				} else {
					if (((JButton) e.getSource()).getName().equals("assign")) {
						assignAttackView = new AssignAttackView(game.getPlayer());
						assignAttackView.getOk().addMouseListener(this);
						assignAttackView.getNewSupers().addMouseListener(this);
						assignAttackView.getOldSupers().addMouseListener(this);
						assignAttackView.getNewUltimates().addMouseListener(this);
						assignAttackView.getOldUltimates().addMouseListener(this);
					} else {
						if (((JButton) e.getSource()).getName().equals("new")) {
							worldView.addFihgter();
						} else {
							if (((JButton) e.getSource()).getName().equals("upgrade")) {
								UpgradeFighterView up = new UpgradeFighterView(game.getPlayer().getActiveFighter());
								worldView.setUpgradeFighter(up);
								up.getBack().addMouseListener(this);
								up.getbDamage().addMouseListener(this);
								up.getpDamage().addMouseListener(this);
								up.getmKi().addMouseListener(this);
								up.getmStamina().addMouseListener(this);
								up.getMaxHP().addMouseListener(this);
								worldView.setVisible(false);
							} else if (((JButton) e.getSource()).getName().equals("back")) {
								worldView.getUpgradeFighter().setVisible(false);
								worldView.setVisible(true);
								worldView.update();
							} else if (((JButton) e.getSource()).getName().equals("hp")) {
								upgrade('H');
							} else if (((JButton) e.getSource()).getName().equals("phy")) {
								upgrade('P');
							} else if (((JButton) e.getSource()).getName().equals("blast")) {
								upgrade('B');
							} else if (((JButton) e.getSource()).getName().equals("ki")) {
								upgrade('K');
							} else if (((JButton) e.getSource()).getName().equals("stamina")) {
								upgrade('S');
							} else if (((JButton) e.getSource()).getName().equals("save")) {
								try {
									String file = JOptionPane.showInputDialog("Enter file name: ");
									check = "data/" + file + ".csv";
									game.save(check);
									ArrayList<String> saves = new ArrayList<String>();
									try {
										saves = Main.loadSaves();
										saves.add(file);

										ObjectOutputStream oos = new ObjectOutputStream(
												new FileOutputStream(new File("data/all saved files.csv")));
										oos.writeObject(saves);
										oos.close();
									} catch (ClassNotFoundException e1) {
										JOptionPane.showMessageDialog(null, e1.getMessage());
									}
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								}
							} else if (((JButton) e.getSource()).getName().equals("block")) {
								battle.block();
								battleView.update();
							} else if (((JButton) e.getSource()).getName().equals("use")) {
								try {
									battle.use(game.getPlayer(), Collectible.SENZU_BEAN);
								} catch (NotEnoughSenzuBeansException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								}
								battleView.update();
							} else if (((JButton) e.getSource()).getName().equals("phyAttack")) {
								try {
									battleView.physicalAttack();
									battle.attack(new PhysicalAttack());
								} catch (NotEnoughKiException e1) {
								}

							} else if (((JButton) e.getSource()).getName().equals("supAttack")) {
								String x = battleView.showSuper();
								Attack att = (Attack) getsuper(x,
										game.getPlayer().getActiveFighter().getSuperAttacks());
								try {
									if (att != null) {
										battle.attack(att);
									} else {
										JOptionPane.showMessageDialog(null,
												"You didn't assign any super attacks to this fighter!");
									}
								} catch (NotEnoughKiException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								}
							} else if (((JButton) e.getSource()).getName().equals("ultAttack")) {
								String x = battleView.showUltimate();
								try {
									Attack att = (Attack) getultimate(x,
											game.getPlayer().getActiveFighter().getUltimateAttacks());
									if (att != null) {
										battle.attack(att);
									} else {
										JOptionPane.showMessageDialog(null,
												"You didn't assign any ultimate attacks to this fighter!");
									}

								} catch (NotEnoughKiException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								}
							} else if (((JButton) e.getSource()).getName().equals("ok")) {

								PlayableFighter active = game.getPlayer().getActiveFighter();
								try {
									String nName = (String) assignAttackView.getNewSupers().getSelectedItem();
									if (nName != null) {
										String oName = (String) assignAttackView.getOldSupers().getSelectedItem();

										SuperAttack nAttack = (SuperAttack) getsuper(nName,
												game.getPlayer().getSuperAttacks());
										SuperAttack oAttack = (SuperAttack) getsuper(oName, active.getSuperAttacks());

										game.getPlayer().assignAttack(active, nAttack, oAttack);
										assignAttackView.update();
										JOptionPane.showMessageDialog(null,
												"You assigned a new super attack: " + nName);
									}

								} catch (MaximumAttacksLearnedException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								} catch (DuplicateAttackException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								}

								try {

									String nName = (String) assignAttackView.getNewUltimates().getSelectedItem();
									if (nName != null) {
										String oName = (String) assignAttackView.getOldUltimates().getSelectedItem();
										UltimateAttack nAttack = (UltimateAttack) getultimate(nName,
												game.getPlayer().getUltimateAttacks());

										UltimateAttack oAttack = (UltimateAttack) getultimate(oName,
												active.getUltimateAttacks());

										game.getPlayer().assignAttack(active, nAttack, oAttack);
										assignAttackView.update();
										JOptionPane.showMessageDialog(null,
												"You assigned a new ultimate attack: " + nName);
									}
								} catch (MaximumAttacksLearnedException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								} catch (DuplicateAttackException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								} catch (NotASaiyanException e1) {
									JOptionPane.showMessageDialog(null, e1.getMessage());
								}
							}
						}
					}
				}
			}
		}
	}

	private void exitDragonView() {
		dragonView.setVisible(false);
		worldView.setVisible(true);
		worldView.update();

	}

	public SuperAttack getsuper(String x, ArrayList<SuperAttack> a) {
		for (int i = 0; i < a.size(); i++)
			if (x.equals(a.get(i).getName()))
				return a.get(i);
		return null;
	}

	public UltimateAttack getultimate(String x, ArrayList<UltimateAttack> a) {
		for (UltimateAttack u : a)
			if (x.equals(u.getName()))
				return u;
		return null;
	}

	public void upgrade(char c) {
		try {
			game.getPlayer().upgradeFighter(game.getPlayer().getActiveFighter(), c);
		} catch (NotEnoughAbilityPointsException e1) {
			JOptionPane.showMessageDialog(null, e1.getMessage());
		}
		worldView.getUpgradeFighter().update();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int col = game.getWorld().getPlayerColumn();
		int row = game.getWorld().getPlayerRow();
		int index = row * 10 + col;
		JButton button;
		try {
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				button = worldView.getCellz().get(index);
				button.setIcon(null);
				game.getWorld().moveUp();
			} else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				button = worldView.getCellz().get(index);
				button.setIcon(null);
				game.getWorld().moveLeft();
			} else {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					button = worldView.getCellz().get(index);
					button.setIcon(null);
					game.getWorld().moveRight();
				} else {
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						button = worldView.getCellz().get(index);
						button.setIcon(null);
						game.getWorld().moveDown();
					}
				}
			}
		} catch (Exception exc) {
		}
		worldView.update();
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public WorldView getWorldView() {
		return worldView;
	}

	public void setWorldView(WorldView worldView) {
		this.worldView = worldView;
	}

	public BattleView getBattleView() {
		return battleView;
	}

	public void setBattleView(BattleView battleView) {
		this.battleView = battleView;
	}

	public DragonView getDragonModeView() {
		return dragonView;
	}

	public void setDragonModeView(DragonView dragonModeView) {
		this.dragonView = dragonModeView;
	}

	public Dragon getDragon() {
		return dragon;
	}

	public void setDragon(Dragon dragon) {
		this.dragon = dragon;
	}

}
