package dragonball.view;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import dragonball.model.attack.*;
import dragonball.model.battle.*;
import dragonball.model.character.fighter.*;

@SuppressWarnings("serial")
public class BattleView extends JFrame {

	private Battle battle;
	private int prevLevel;

	private JScrollPane scrollPane;
	private JTextArea comments;

	private JPanel fighter;
	private JPanel foe;
	private JPanel upper;
	private JPanel buttons;

	private JLabel fighterLevel;
	private JLabel fighterName;
	private JLabel fighterHealth;
	private JLabel fighterKi;
	private JLabel fighterStamina;
	private JLabel fighterState;
	private JLabel transformed;

	private JLabel fighterIcon;

	private JButton block;
	private JButton use;

	private JLabel foeLevel;
	private JLabel foeName;
	private JLabel foeHealth;
	private JLabel foeKi;
	private JLabel foeStamina;
	private JLabel foeState;
	private JLabel foeIcon;

	private ArrayList<SuperAttack> superAttacks;
	private ArrayList<UltimateAttack> ultimateAttacks;

	private JButton physical;
	private JButton zuper;
	private JButton ultimate;

	private Attack attack;

	public BattleView(Battle battle) {
		super("DragonBallZ");
		// setContentPane(new JLabel(new ImageIcon("resources/images/")));
		setLayout(new BorderLayout());
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		this.battle = battle;
		prevLevel = ((PlayableFighter) battle.getMe()).getLevel();

		ImageIcon icon = new ImageIcon(String.format("resources/images/%s idle.png", race()));

		fighterIcon = new JLabel(icon);

		add(fighterIcon, BorderLayout.WEST);

		icon = WorldView.resizeIcon("save.png", 50, 50);

		superAttacks = ((Fighter) battle.getMe()).getSuperAttacks();
		ultimateAttacks = ((Fighter) battle.getMe()).getUltimateAttacks();

		fighterName = new JLabel("Fighter name: " + ((Fighter) battle.getMe()).getName());
		fighterLevel = new JLabel("Fighter level: " + ((Fighter) battle.getMe()).getLevel());
		fighterKi = new JLabel(
				"Fighter ki: " + ((Fighter) battle.getMe()).getKi() + "/" + ((Fighter) battle.getMe()).getMaxKi());
		fighterStamina = new JLabel("Fighter stamina: " + ((Fighter) battle.getMe()).getStamina() + "/"
				+ ((Fighter) battle.getMe()).getMaxStamina());
		fighterHealth = new JLabel("Fighter health: " + ((Fighter) battle.getMe()).getHealthPoints() + "/"
				+ ((Fighter) battle.getMe()).getMaxHealthPoints());
		fighterState = new JLabel("Fighter State: Attacker");

		fighter = new JPanel();
		fighter.setLayout(new GridLayout(0, 1));
		fighter.setBackground(new Color(179, 217, 255));
		fighter.add(fighterName);
		fighter.add(fighterLevel);
		fighter.add(fighterKi);
		fighter.add(fighterStamina);
		fighter.add(fighterHealth);
		fighter.add(fighterState);

		foeName = new JLabel("Foe name: " + ((Fighter) battle.getFoe()).getName());
		foeLevel = new JLabel("Foe level: " + ((Fighter) battle.getFoe()).getLevel());
		foeKi = new JLabel(
				"Foe ki: " + ((Fighter) battle.getFoe()).getKi() + "/" + ((Fighter) battle.getFoe()).getMaxKi());
		foeStamina = new JLabel("Foe stamina: " + ((Fighter) battle.getFoe()).getStamina() + "/"
				+ ((Fighter) battle.getMe()).getMaxStamina());
		foeHealth = new JLabel("Foe health: " + ((Fighter) battle.getFoe()).getHealthPoints() + "/"
				+ ((Fighter) battle.getFoe()).getMaxHealthPoints());
		foeState = new JLabel("Foe State: Defender");

		icon = new ImageIcon(String.format("resources/images/%s idle.png", foe()));
		foeIcon = new JLabel(icon);

		add(foeIcon, BorderLayout.EAST);

		foe = new JPanel();
		foe.setLayout(new GridLayout(0, 1));
		foe.setBackground(new Color(209, 179, 255));
		foe.add(foeName);
		foe.add(foeLevel);
		foe.add(foeKi);
		foe.add(foeStamina);
		foe.add(foeHealth);
		foe.add(foeState);

		if ((PlayableFighter) (battle.getMe()) instanceof Saiyan) {
			transformed = new JLabel("Transformed :  No");
			fighter.add(transformed);
			foe.add(new JLabel(""));
		}

		icon = WorldView.resizeIcon("block.png", 50, 50);

		block = new JButton("Block");
		block.setName("block");
		block.setIcon(icon);
		block.setBackground(new Color(204, 102, 102));

		icon = WorldView.resizeIcon("eat.png", 50, 50);

		use = new JButton("Use");
		use.setName("use");
		use.setIcon(icon);
		use.setBackground(new Color(204, 102, 102));
		
		icon = WorldView.resizeIcon("physical attack.png", 50, 50);

		physical = new JButton("Physical Attack");
		physical.setName("phyAttack");
		physical.setIcon(icon);
		physical.setBackground(new Color(204, 102, 102));

		icon = WorldView.resizeIcon("super attack.png", 50, 50);

		zuper = new JButton("Super Attack");
		zuper.setName("supAttack");
		zuper.setIcon(icon);
		zuper.setBackground(new Color(204, 102, 102));
		
		icon = WorldView.resizeIcon("ultimate attack.png", 50, 50);

		ultimate = new JButton("Ultimate Attack");
		ultimate.setName("ultAttack");
		ultimate.setIcon(icon);
		ultimate.setBackground(new Color(204, 102, 102));

		comments = new JTextArea("Fight!");
		comments.setEditable(false);
		scrollPane = new JScrollPane(comments);

		buttons = new JPanel();
		buttons.add(physical);
		buttons.add(zuper);
		buttons.add(ultimate);
		buttons.add(block);
		buttons.add(use);

		upper = new JPanel(new GridLayout(0, 2));
		upper.add(fighter);
		upper.add(foe);

		add(upper, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(buttons, BorderLayout.SOUTH);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		setBounds(50, 50, 800, 600);

		repaint();
		validate();
		setVisible(true);
	}

	public String race() {
		PlayableFighter fi = (PlayableFighter) battle.getMe();
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

	public int getPrevLevel() {
		return prevLevel;
	}

	public String showUltimate() {
		if (!ultimateAttacks.isEmpty()) {
			String[] supers = new String[ultimateAttacks.size()];
			for (int i = 0; i < supers.length; i++) {
				supers[i] = ultimateAttacks.get(i).getName();
			}
			JFrame frame = new JFrame("choose a super attack");
			String attack = (String) JOptionPane.showInputDialog(frame, "Choose a super attack",
					"Choose a super attack", JOptionPane.QUESTION_MESSAGE, null, supers, supers[0]);
			return attack;
		}
		return "...";
	}

	public String showSuper() {
		if (!superAttacks.isEmpty()) {
			String[] ultimates = new String[superAttacks.size()];
			for (int i = 0; i < ultimates.length; i++) {
				ultimates[i] = superAttacks.get(i).getName();
			}
			JFrame frame = new JFrame("choose a super attack");
			String attack = (String) JOptionPane.showInputDialog(frame, "Choose a super attack",
					"Choose a super attack", JOptionPane.QUESTION_MESSAGE, null, ultimates, ultimates[0]);
			return attack;
		}
		return "...";
	}

	// public void paint(Graphics g) {
	// super.paintComponents(g);
	// int x = 10, y = 50, fighterHealth = 200, length = 200, width = 20;
	// g.setColor(Color.gray);
	// g.fillRect(x, y, length, width);
	//
	// g.setColor(Color.green);
	// g.fillRect(x, y, fighterHealth, width);
	//
	// g.setColor(Color.white);
	// g.drawRect(x, y, length, width);
	// }

	public void update() {

		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				foeIdle();

				String fighter = "Fighter State: ";
				String foe = "Foe State: ";
				if (battle.getMe().equals(battle.getAttacker())) {
					fighter += "Attacker";
					foe += "Defender";
				} else {
					fighter += "Defender";
					foe += "Attacker";
				}
				fighterState.setText(fighter);
				foeState.setText(foe);
			}
		}, 1000);

		String turn = battle.getTurn();
		if (turn.length() > 0)
			comments.setText(String.format("%s\n%s\n---", comments.getText(), battle.getTurn()));
		battle.setTurn("");

		fighterLevel.setText("Fighter level: " + ((Fighter) battle.getMe()).getLevel());
		fighterKi.setText(
				"Fighter ki: " + ((Fighter) battle.getMe()).getKi() + "/" + ((Fighter) battle.getMe()).getMaxKi());
		fighterStamina.setText("Fighter stamina: " + ((Fighter) battle.getMe()).getStamina() + "/"
				+ ((Fighter) battle.getMe()).getMaxStamina());
		fighterHealth.setText("Fighter health: " + ((Fighter) battle.getMe()).getHealthPoints() + "/"
				+ ((Fighter) battle.getMe()).getMaxHealthPoints());

		if ((PlayableFighter) (battle.getMe()) instanceof Saiyan) {
			boolean s = ((Saiyan) (battle.getMe())).isTransformed();
			if (s)
				superSaiyan();
			else
				idle();
			transformed.setText("Transformed :  " + (s ? "Yes" : "No"));
		}

		foeKi.setText(
				"Foe ki: " + ((Fighter) battle.getFoe()).getKi() + "/" + ((Fighter) battle.getFoe()).getMaxStamina());
		foeStamina.setText("Foe stamina: " + ((Fighter) battle.getFoe()).getStamina() + "/"
				+ ((Fighter) battle.getFoe()).getMaxStamina());
		foeHealth.setText("Foe health: " + ((Fighter) battle.getFoe()).getHealthPoints() + "/"
				+ ((Fighter) battle.getFoe()).getMaxHealthPoints());

		repaint();
		validate();
	}

	public void idle() {
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				ImageIcon icon = new ImageIcon(String.format("resources/images/%s idle.png", race()));
				fighterIcon.setIcon(icon);
			}
		}, 1000);

	}

	public Battle getBattle() {
		return battle;
	}

	public JButton getPhysical() {
		return physical;
	}

	public JButton getZuper() {
		return zuper;
	}

	public JButton getUltimate() {
		return ultimate;
	}

	public Attack getAttack() {
		return attack;
	}

	public JButton getBlock() {
		return block;
	}

	public JButton getUse() {
		return use;
	}

	public void superSaiyan() {
		fighterIcon.setIcon(new ImageIcon("resources/images/super saiyan.gif"));
	}

	public void physicalAttack() {
		ImageIcon icon = new ImageIcon(String.format("resources/images/%s attacks.png", race()));
		fighterIcon.setIcon(icon);
	}

	public void foePhysicalAttack() {
		ImageIcon icon = new ImageIcon(String.format("resources/images/%s attacks.png", foe()));
		foeIcon.setIcon(icon);
	}

	public void foeIdle() {
		ImageIcon icon = new ImageIcon(String.format("resources/images/%s idle.png", foe()));
		foeIcon.setIcon(icon);
	}

	public String foe() {
		return "foe1";
	}

	// public static void main(String[] args) {
	// BattleView x = new BattleView(new Battle(new Saiyan("S"), new
	// Saiyan("d")));
	// }

}
