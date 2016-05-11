package dragonball.view;

import java.awt.*;

import javax.swing.*;

import dragonball.model.attack.*;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.player.Player;

@SuppressWarnings("serial")
public class UpgradeFighterView extends JFrame {

	private PlayableFighter fighter;

	private JPanel upper;
	private JPanel assignment;
	private JPanel buttons;
	private JPanel right;
	private JPanel left;

	private JLabel maxHealthPoints;
	private JLabel physicalDamage;
	private JLabel blastDamage;
	private JLabel maxKi;
	private JLabel maxStamina;
	private JLabel abilityPoints;
	// TODO
	// private JLabel fighterIcon;

	private JButton maxHP;
	private JButton phyDamage;
	private JButton blDamage;
	private JButton mKi;
	private JButton mStamina;
	private JButton back;

	private Player player;

	private JLabel oldSupersLabel;
	private JLabel newSupersLabel;
	private JLabel oldUltimatesLabel;
	private JLabel newUltimatesLabel;

	private JButton ok;

	private JComboBox<String> newSupersBox;
	private JComboBox<String> newUltimatesBox;
	private JComboBox<String> oldSupersBox;
	private JComboBox<String> oldUltimatesBox;

	public UpgradeFighterView(Player player) {
		super("DragonBallZ");
		// setContentPane(new JLabel(new ImageIcon("resources/images/")));
		setLayout(new GridLayout(0, 3));
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		this.player = player;
		fighter = player.getActiveFighter();

		oldSupersLabel = new JLabel("choose old super attack");
		newSupersLabel = new JLabel("choose new super attack");
		oldUltimatesLabel = new JLabel("choose old ultimate attack");
		newUltimatesLabel = new JLabel("choose new ultimate attack");

		ImageIcon icon = WorldView.resizeIcon("hp.png", 50, 50);

		maxHealthPoints = new JLabel("Max HP: " + fighter.getMaxHealthPoints());
		maxHealthPoints.setIcon(icon);

		maxHP = new JButton(icon);
		maxHP.setName("hp");
		maxHP.setBackground(new Color(179, 218, 255));

		icon = WorldView.resizeIcon("physical damage.png", 50, 50);

		physicalDamage = new JLabel("Physical Damage: " + fighter.getPhysicalDamage());
		physicalDamage.setIcon(icon);

		phyDamage = new JButton(icon);
		phyDamage.setName("phy");
		phyDamage.setBackground(new Color(179, 218, 255));

		icon = WorldView.resizeIcon("blast damage.png", 50, 50);

		blastDamage = new JLabel("Blast Damage: " + fighter.getBlastDamage());
		blastDamage.setIcon(icon);

		blDamage = new JButton(icon);
		blDamage.setName("blast");
		blDamage.setBackground(new Color(179, 218, 255));
		;

		icon = WorldView.resizeIcon("ki.png", 50, 50);

		maxKi = new JLabel("Max Ki: " + fighter.getMaxKi());
		maxKi.setIcon(icon);

		mKi = new JButton(icon);
		mKi.setName("ki");
		mKi.setBackground(new Color(179, 218, 255));
		;

		icon = WorldView.resizeIcon("stamina.png", 50, 50);

		maxStamina = new JLabel("Max Stamina: " + fighter.getMaxStamina());
		maxStamina.setIcon(icon);

		mStamina = new JButton(icon);
		mStamina.setName("stamina");
		mStamina.setBackground(new Color(179, 218, 255));
		;

		icon = WorldView.resizeIcon("ability points.png", 50, 50);

		abilityPoints = new JLabel("Ability Points: " + fighter.getAbilityPoints());
		abilityPoints.setIcon(icon);

		icon = WorldView.resizeIcon("back.png", 50, 50);

		back = new JButton(icon);
		back.setName("back");
		back.setBackground(new Color(179, 218, 255));

		upper = new JPanel(new GridLayout(0, 1));

		upper.add(maxHealthPoints);
		upper.add(physicalDamage);
		upper.add(blastDamage);
		upper.add(maxKi);
		upper.add(maxStamina);
		upper.add(abilityPoints);

		upper.setBackground(new Color(179, 179, 204));

		setAssignPart();

		assignment = new JPanel(new GridLayout(0, 2));
		assignment.setBackground(new Color(198, 216, 236));

		assignment.add(oldSupersLabel);
		assignment.add(oldSupersBox);

		assignment.add(newSupersLabel);
		assignment.add(newSupersBox);

		assignment.add(oldUltimatesLabel);
		assignment.add(oldUltimatesBox);

		assignment.add(newUltimatesLabel);
		assignment.add(newUltimatesBox);

		buttons = new JPanel(new GridLayout(0, 1));
		buttons.setBackground(new Color(184, 184, 148));

		buttons.add(ok);
		buttons.add(maxHP);
		buttons.add(phyDamage);
		buttons.add(blDamage);
		buttons.add(mKi);
		buttons.add(mStamina);
		buttons.add(back);

		setLayout(new GridLayout());

		right = new JPanel(new BorderLayout());
		right.add(buttons, BorderLayout.SOUTH);
		right.add(assignment, BorderLayout.CENTER);

		left = new JPanel(new BorderLayout());
		left.add(upper, BorderLayout.CENTER);

		add(left, BorderLayout.WEST);
		add(right, BorderLayout.EAST);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		setBounds(50, 50, 500, 500);

		repaint();
		validate();
		setVisible(true);

	}

	public void setAssignPart() {
		String[] a = new String[fighter.getSuperAttacks().size() + 1];
		if (fighter.getSuperAttacks() != null) {
			for (int i = 1; i < a.length; i++) {
				a[i] = fighter.getSuperAttacks().get(i - 1).getName();
			}
		}
		a[0] = "...";
		oldSupersBox = new JComboBox<>(a);
		oldSupersBox.setBackground(new Color(180, 203, 228));

		String[] x = new String[player.getSuperAttacks().size()];
		if (player.getSuperAttacks() != null) {
			for (int i = 0; i < x.length; i++) {
				x[i] = player.getSuperAttacks().get(i).getName();
			}
		}
		newSupersBox = new JComboBox<>(x);
		newSupersBox.setBackground(new Color(180, 203, 228));

		String[] b = new String[fighter.getUltimateAttacks().size() + 1];
		if (player.getActiveFighter().getUltimateAttacks() != null) {
			for (int i = 1; i < b.length; i++) {
				b[i] = fighter.getUltimateAttacks().get(i - 1).getName();
			}
		}
		b[0] = "...";
		oldUltimatesBox = new JComboBox<>(b);
		oldUltimatesBox.setBackground(new Color(180, 203, 228));

		String[] y = new String[player.getUltimateAttacks().size()];
		if (player.getUltimateAttacks() != null) {
			for (int i = 0; i < y.length; i++) {
				y[i] = player.getUltimateAttacks().get(i).getName();
			}
		}
		newUltimatesBox = new JComboBox<>(y);
		newUltimatesBox.setBackground(new Color(180, 203, 228));

		ok = new JButton("Assign attacks");
		ok.setName("ok");
		ok.setBackground(new Color(180, 203, 228));

		ImageIcon icon = WorldView.resizeIcon("ok.png", 50, 50);
		ok.setIcon(icon);

	}

	public void update() {
		maxHealthPoints.setText("Max HP: " + fighter.getMaxHealthPoints());
		physicalDamage.setText("Physical Damage: " + fighter.getPhysicalDamage());
		blastDamage.setText("Blast Damage: " + fighter.getBlastDamage());
		maxKi.setText("Max Ki: " + fighter.getMaxKi());
		maxStamina.setText("Max Stamina: " + fighter.getMaxStamina());
		abilityPoints.setText("Ability Points: " + fighter.getAbilityPoints());

		updateAssignPart();

		setVisible(true);
		repaint();
		validate();
	}

	public void updateAssignPart() {
		String[] a = new String[player.getActiveFighter().getSuperAttacks().size() + 1];
		if (player.getActiveFighter().getSuperAttacks().size() > 0) {
			for (int i = 1; i < a.length; i++) {
				SuperAttack x = player.getActiveFighter().getSuperAttacks().get(i - 1);
				if (x != null)
					a[i] = x.getName();
			}
		}
		a[0] = "...";
		oldSupersBox.setModel(new DefaultComboBoxModel<String>(a));

		String[] b = new String[player.getActiveFighter().getUltimateAttacks().size() + 1];
		if (player.getActiveFighter().getUltimateAttacks().size() > 0) {
			for (int i = 1; i < b.length; i++) {
				UltimateAttack x = player.getActiveFighter().getUltimateAttacks().get(i - 1);
				if (x != null)
					b[i] = x.getName();
			}
		}
		b[0] = "...";
		oldUltimatesBox.setModel(new DefaultComboBoxModel<String>(b));
		validate();
		repaint();
	}

	public JButton getMaxHP() {
		return maxHP;
	}

	public JButton getpDamage() {
		return phyDamage;
	}

	public JButton getbDamage() {
		return blDamage;
	}

	public JButton getmKi() {
		return mKi;
	}

	public JButton getmStamina() {
		return mStamina;
	}

	public JButton getBack() {
		return back;
	}

	public PlayableFighter getFighter() {
		return fighter;
	}

	public JPanel getAssignment() {
		return assignment;
	}

	public JPanel getButtons() {
		return buttons;
	}

	public JLabel getMaxHealthPoints() {
		return maxHealthPoints;
	}

	public JLabel getBlastDamage() {
		return blastDamage;
	}

	public JLabel getMaxKi() {
		return maxKi;
	}

	public JLabel getMaxStamina() {
		return maxStamina;
	}

	public JLabel getAbilityPoints() {
		return abilityPoints;
	}

	public JButton getBlDamage() {
		return blDamage;
	}

	public JButton getOk() {
		return ok;
	}

	public JComboBox<String> getNewSupersBox() {
		return newSupersBox;
	}

	public JComboBox<String> getNewUltimatesBox() {
		return newUltimatesBox;
	}

	public JComboBox<String> getOldSupersBox() {
		return oldSupersBox;
	}

	public JComboBox<String> getOldUltimatesBox() {
		return oldUltimatesBox;
	}

}
