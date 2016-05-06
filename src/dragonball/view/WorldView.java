package dragonball.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dragonball.model.character.fighter.Earthling;
import dragonball.model.character.fighter.Frieza;
import dragonball.model.character.fighter.Majin;
import dragonball.model.character.fighter.Namekian;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.player.Player;
import dragonball.model.world.World;

@SuppressWarnings("serial")
public class WorldView extends JFrame {

	private Player player;
	private World world;
	private UpgradeFighterView upgradeFighter;

	private JLabel playerName;
	private JLabel senzu;
	private JLabel dragonBalls;
	private JLabel exploredMaps;

	private JLabel fighterName;
	private JLabel level;
	private JLabel abilityPoints;
	private JLabel exp;
	private JLabel race;
	private JLabel fighterIcon;

	private JPanel playerPanel;
	private JPanel fighterPanel;
	private JPanel bossPanel;
	private JPanel cellzPanel;
	private JPanel centerPanel;
	private JPanel eastPanel;

	private JLabel bossName;
	private JLabel bossLevel;
	private JLabel bossHP;
	private JLabel bossStamina;
	private JLabel bossKi;
	private JLabel bossIcon;

	private ArrayList<JButton> cellz;

	private JButton switchFighter;
	private JButton assignAttacks;
	private JButton upgrade;
	private JButton newFighter;
	private JButton save;

	public WorldView(World world, Player player) {
		super("DragonBallZ");
		setBounds(150, 50, 800, 600);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		this.world = world;
		this.player = player;

		ImageIcon icon = resizeIcon("save.png", 50, 50);

		save = new JButton("Save");
		save.setName("save");
		save.setBackground(new Color(255, 121, 77));
		save.setIcon(icon);

		icon = resizeIcon("switch.png", 50, 50);

		switchFighter = new JButton("Switch Fighter");
		switchFighter.setName("switch");
		switchFighter.setBackground(new Color(255, 121, 77));
		switchFighter.setToolTipText("Hey! Wait!");
		switchFighter.setIcon(icon);

		icon = resizeIcon("assign.png", 50, 50);

		assignAttacks = new JButton("Assign Attack");
		assignAttacks.setName("assign");
		assignAttacks.setBackground(new Color(255, 148, 77));
		assignAttacks.setIcon(icon);

		icon = resizeIcon("upgrade.png", 50, 50);

		upgrade = new JButton("Upgrade Fighter");
		upgrade.setName("upgrade");
		upgrade.setBackground(new Color(255, 121, 77));
		upgrade.setIcon(icon);

		icon = resizeIcon("new.png", 50, 50);

		newFighter = new JButton("New Fighter");
		newFighter.setName("new");
		newFighter.setBackground(new Color(255, 148, 77));
		newFighter.setToolTipText("New Friends!");
		newFighter.setIcon(icon);

		PlayableFighter fi = player.getActiveFighter();

		icon = resizeIcon("senzu beans.png", 50, 50);

		senzu = new JLabel("Senzu Beans: " + player.getSenzuBeans());
		senzu.setIcon(icon);

		icon = resizeIcon("ability points.png", 50, 50);

		abilityPoints = new JLabel("Ability Points: " + player.getActiveFighter().getAbilityPoints());
		abilityPoints.setIcon(icon);

		icon = resizeIcon("dragon ball.png", 50, 50);

		dragonBalls = new JLabel("Dragon Balls: " + player.getDragonBalls());
		dragonBalls.setIcon(icon);

		icon = resizeIcon("level.png", 50, 50);

		level = new JLabel("Active Fighter Level: " + fi.getLevel());
		level.setIcon(icon);

		bossLevel = new JLabel(String.format("Boss Level: %s", world.getBoss().getLevel()));
		bossLevel.setIcon(icon);

		icon = resizeIcon("name.png", 50, 50);

		playerName = new JLabel("Player Name: " + player.getName());
		playerName.setIcon(icon);

		fighterName = new JLabel("Fighter Name: " + fi.getName());
		fighterName.setIcon(icon);

		bossName = new JLabel(String.format("Boss Name: %s", world.getBoss().getName()));
		bossName.setIcon(icon);

		icon = resizeIcon("exp.png", 50, 50);

		exp = new JLabel("EXP: " + fi.getXp() + "/" + fi.getTargetXp());
		exp.setIcon(icon);

		icon = resizeIcon(String.format("%s.png", race()), 50, 50);

		race = new JLabel("Race: " + race());
		race.setIcon(icon);

		icon = resizeIcon(String.format("%s.png", cRace()), width(), height());

		fighterIcon = new JLabel(icon);

		icon = resizeIcon("nimbus.png", 50, 50);

		exploredMaps = new JLabel("Explored Maps: " + player.getExploredMaps());
		exploredMaps.setIcon(icon);

		playerPanel = new JPanel(new GridLayout(0, 4));
		playerPanel.setBackground(new Color(255, 179, 128));

		playerPanel.add(playerName);
		playerPanel.add(senzu);
		playerPanel.add(dragonBalls);
		playerPanel.add(exploredMaps);

		fighterPanel = new JPanel(new GridLayout(5, 0));
		fighterPanel.setBackground(new Color(255, 179, 128));

		fighterPanel.add(fighterName);
		fighterPanel.add(race);
		fighterPanel.add(level);
		fighterPanel.add(exp);
		fighterPanel.add(abilityPoints);

		eastPanel = new JPanel(new BorderLayout());
		eastPanel.setBackground(new Color(255, 179, 128));

		eastPanel.add(fighterPanel, BorderLayout.CENTER);
		eastPanel.add(fighterIcon, BorderLayout.SOUTH);

		cellzPanel = new JPanel();
		cellzPanel.setLayout(new GridLayout(10, 10));
		cellzPanel.setBackground(new Color(255, 179, 128));

		cellz = new ArrayList<JButton>();
		for (int i = 0; i < 100; i++) {
			JButton b = new JButton("");
			b.setName("" + i);
			b.setBackground(new Color(255, 204, 102));
			cellzPanel.add(b);
			cellz.add(b);
		}

		icon = resizeIcon(String.format("%s.png", race()), 50, 50);

		icon = resizeIcon("hp.png", 50, 50);

		bossHP = new JLabel(String.format("Boss HP: %s", world.getBoss().getMaxHealthPoints()));
		bossHP.setIcon(icon);

		icon = resizeIcon("stamina.png", 50, 50);

		bossStamina = new JLabel(String.format("Boss Stamina: %s", world.getBoss().getMaxStamina()));
		bossStamina.setIcon(icon);

		icon = resizeIcon("ki.png", 50, 50);

		bossKi = new JLabel(String.format("Boss Ki: %s", world.getBoss().getMaxKi()));
		bossKi.setIcon(icon);

		String bs = boss();
		icon = resizeIcon(String.format("%s.png", bs), 50, 50);

		bossIcon = new JLabel(bs, icon, JLabel.CENTER);

		bossPanel = new JPanel(new GridLayout(6, 0));
		bossPanel.setBackground(new Color(255, 179, 128));

		bossPanel.add(bossIcon);
		bossPanel.add(bossName);
		bossPanel.add(bossLevel);
		bossPanel.add(bossHP);
		bossPanel.add(bossStamina);
		bossPanel.add(bossKi);

		icon = resizeIcon("boss.png", 50, 50);

		cellz.get(0).setBackground(new Color(255, 204, 102));
		cellz.get(0).setIcon(icon);

		JPanel footer = new JPanel((new GridLayout(0, 5)));
		footer.setBackground(new Color(255, 204, 102));
		footer.add(switchFighter);
		footer.add(assignAttacks);
		footer.add(upgrade);
		footer.add(newFighter);
		footer.add(save);

		centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(eastPanel, BorderLayout.EAST);
		centerPanel.add(cellzPanel, BorderLayout.CENTER);
		centerPanel.add(bossPanel, BorderLayout.WEST);

		add(playerPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);
		add(footer, BorderLayout.SOUTH);

		int col = world.getPlayerColumn();
		int row = world.getPlayerRow();
		int index = row * 10 + col;
		JButton button = cellz.get(index);

		icon = resizeIcon("goku flying.png", 60, 60);

		button.setIcon(icon);

		validate();
		setVisible(true);
	}

	public static ImageIcon resizeIcon(String file, int width, int height) {
		ImageIcon icon = new ImageIcon("resources/images/" + file);
		Image image = icon.getImage();
		Image nimage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(nimage);
		return icon;
	}

	public int width() {
		PlayableFighter fi = player.getActiveFighter();
		if (fi instanceof Majin)
			return 200;
		if (fi instanceof Frieza)
			return 200;
		if (fi instanceof Saiyan)
			return 180;
		if (fi instanceof Earthling)
			return 100;
		if (fi instanceof Namekian)
			return 200;
		return 0;
	}

	public int height() {
		PlayableFighter fi = player.getActiveFighter();
		if (fi instanceof Majin)
			return 200;
		if (fi instanceof Frieza)
			return 200;
		if (fi instanceof Saiyan)
			return 200;
		if (fi instanceof Earthling)
			return 200;
		if (fi instanceof Namekian)
			return 200;
		return 0;
	}

	public String boss() {
		int n = (new Random().nextInt(5));
		if (n == 0)
			return "The Red Ribbon Army";
		if (n == 1)
			return "Ginyu Special Corps";
		if (n == 2)
			return "Garlic Jr.";
		if (n == 3)
			return "Coolers Armored Squadron";
		return "Unknown";
	}

	public String race() {
		PlayableFighter fi = player.getActiveFighter();
		if (fi instanceof Majin)
			return "Majin";
		if (fi instanceof Frieza)
			return "Frieza";
		if (fi instanceof Saiyan)
			return "Saiyan";
		if (fi instanceof Earthling)
			return "Earthling";
		if (fi instanceof Namekian)
			return "Namekian";
		return "";
	}

	public JButton getSave() {
		return save;
	}

	public UpgradeFighterView getUpgradeFighter() {
		return upgradeFighter;
	}

	public void setUpgradeFighter(UpgradeFighterView upgradeFighter) {
		this.upgradeFighter = upgradeFighter;
	}

	public JLabel getAbilityPoints() {
		return abilityPoints;
	}

	public JLabel getXp() {
		return exp;
	}

	public JPanel getUpper() {
		return playerPanel;
	}

	public JButton getSwitchFighter() {
		return switchFighter;
	}

	public JButton getAssignAttacks() {
		return assignAttacks;
	}

	public JButton getUpgrade() {
		return upgrade;
	}

	public JButton getNewFighter() {
		return newFighter;
	}

	public void drawCollectible(String s) {
		JOptionPane.showMessageDialog(null, "You found a :" + s);
		update();
	}

	public void update() {
		PlayableFighter fi = player.getActiveFighter();
		senzu.setText("Senzu Beans: " + player.getSenzuBeans());
		dragonBalls.setText("Dragon Balls: " + player.getDragonBalls());
		level.setText("Active Fighter Level: " + fi.getLevel());
		fighterName.setText("Fighter Name: " + fi.getName());
		abilityPoints.setText("Ability Points: " + fi.getAbilityPoints());
		exp.setText("EXP: " + fi.getXp() + "/" + fi.getTargetXp());

		exploredMaps = new JLabel("Explored Maps: " + player.getExploredMaps());

		int col = world.getPlayerColumn();
		int row = world.getPlayerRow();
		int index = row * 10 + col;
		JButton button = getCellz().get(index);

		ImageIcon icon = resizeIcon("goku flying.png", 60, 60);
		button.setIcon(icon);

		repaint();
		validate();
	}

	public void updateRace() {
		race.setText("Race: " + race());
		ImageIcon icon = resizeIcon(String.format("%s.png", race()), 60, 60);

		race.setIcon(icon);

		icon = resizeIcon(String.format("%s.png", cRace()), width(), height());

		fighterIcon.setIcon(icon);
	}

	public String cRace() {
		PlayableFighter fi = player.getActiveFighter();
		if (fi instanceof Majin)
			return "Maj";
		if (fi instanceof Frieza)
			return "Freezer";
		if (fi instanceof Saiyan)
			return "Goku Sleeping";
		if (fi instanceof Earthling)
			return "Krillin staring";
		if (fi instanceof Namekian)
			return "Namekian";
		return "";

	}

	public void addFihgter() {
		String name = JOptionPane.showInputDialog("Enter the fighter name");
		final String[] races = { "Saiyan", "Namekian", "Majin", "Earthling", "Frieza" };
		JFrame frame = new JFrame("choose a race");
		String race = (String) JOptionPane.showInputDialog(frame, "Choose a race", "Choose a race",
				JOptionPane.QUESTION_MESSAGE, null, races, races[0]);
		if (race != null && race.length() > 0)
			player.createFighter(race.charAt(0), name);
	}

	public Player getPlayer() {
		return player;
	}

	public World getWorld() {
		return world;
	}

	public JLabel getSenzu() {
		return senzu;
	}

	public JLabel getBalls() {
		return dragonBalls;
	}

	public JLabel getLevel() {
		return level;
	}

	public JLabel getPname() {
		return playerName;
	}

	public JLabel getFname() {
		return fighterName;
	}

	public ArrayList<JButton> getCellz() {
		return cellz;
	}

	public String switchFighter() {
		ArrayList<PlayableFighter> fighters = player.getFighters();
		String[] x = new String[fighters.size()];
		for (int i = 0; i < fighters.size(); i++) {
			x[i] = fighters.get(i).getName();
		}
		JFrame frame = new JFrame("Choose a fighter");
		String newFighter = (String) JOptionPane.showInputDialog(frame, "Choose a fighter", "Choose a fighter",
				JOptionPane.QUESTION_MESSAGE, null, x, x[0]);
		return newFighter;
	}

}
