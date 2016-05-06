package dragonball.view;

import java.awt.*;

import javax.swing.*;

import dragonball.model.character.fighter.PlayableFighter;

@SuppressWarnings("serial")
public class UpgradeFighterView extends JFrame {

	private PlayableFighter fighter;

	private JPanel upper;
	private JPanel buttons;

	private JLabel maxHealthPoints;
	private JLabel physicalDamage;
	private JLabel blastDamage;
	private JLabel maxKi;
	private JLabel maxStamina;
	private JLabel abilityPoints;

	private JButton maxHP;
	private JButton phyDamage;
	private JButton blDamage;
	private JButton mKi;
	private JButton mStamina;
	private JButton back;

	public UpgradeFighterView(PlayableFighter fighter) {
		super("DragonBallZ");
		// setContentPane(new JLabel(new ImageIcon("resources/images/")));
		setLayout(new GridLayout(0, 3));
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		this.fighter = fighter;

		ImageIcon icon = WorldView.resizeIcon("hp.png", 50, 50);

		maxHealthPoints = new JLabel("Max HP: " + fighter.getMaxHealthPoints());
		maxHealthPoints.setIcon(icon);

		maxHP = new JButton(icon);
		maxHP.setName("hp");
		maxHP.setBackground(new Color(204, 230, 255));

		icon = WorldView.resizeIcon("physical damage.png", 50, 50);

		physicalDamage = new JLabel("Physical Damage: " + fighter.getPhysicalDamage());
		physicalDamage.setIcon(icon);

		phyDamage = new JButton(icon);
		phyDamage.setName("phy");
		phyDamage.setBackground(new Color(204, 230, 255));

		icon = WorldView.resizeIcon("blast damage.png", 50, 50);
		
		blastDamage = new JLabel("Blast Damage: " + fighter.getBlastDamage());
		blastDamage.setIcon(icon);

		blDamage = new JButton(icon);
		blDamage.setName("blast");
		blDamage.setBackground(new Color(204, 230, 255));

		icon = WorldView.resizeIcon("ki.png", 50, 50);
		
		maxKi = new JLabel("Max Ki: " + fighter.getMaxKi());
		maxKi.setIcon(icon);

		mKi = new JButton(icon);
		mKi.setName("ki");
		mKi.setBackground(new Color(204, 230, 255));

		icon = WorldView.resizeIcon("stamina.png", 50, 50);
		
		maxStamina = new JLabel("Max Stamina: " + fighter.getMaxStamina());
		maxStamina.setIcon(icon);
		
		mStamina = new JButton(icon);
		mStamina.setName("stamina");
		mStamina.setBackground(new Color(204, 230, 255));

//		icon = WorldView.resizeIcon("ability points.png", 50, 50);
		
		abilityPoints = new JLabel("Ability Points: " + fighter.getAbilityPoints());

		icon = WorldView.resizeIcon("back.png", 50, 50);
		
		back = new JButton(icon);
		back.setName("back");
		back.setBackground(new Color(179, 218, 255));

		upper = new JPanel(new GridLayout(2, 3));

		upper.add(maxHealthPoints);
		upper.add(physicalDamage);
		upper.add(blastDamage);
		upper.add(maxKi);
		upper.add(maxStamina);
		upper.add(abilityPoints);

		upper.setBackground(new Color(179, 179, 204));

		buttons = new JPanel(new GridLayout(2, 3));

		buttons.add(maxHP);
		buttons.add(phyDamage);
		buttons.add(blDamage);
		buttons.add(mKi);
		buttons.add(mStamina);
		buttons.add(back);

		buttons.setBackground(new Color(184, 184, 148));

		setLayout(new BorderLayout());

		add(upper, BorderLayout.NORTH);
		add(buttons, BorderLayout.SOUTH);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		setBounds(50, 50, 500, 500);

		repaint();
		validate();
		setVisible(true);

	}

	public void update() {
		maxHealthPoints.setText("Max HP: " + fighter.getMaxHealthPoints());
		physicalDamage.setText("Physical Damage: " + fighter.getPhysicalDamage());
		blastDamage.setText("Blast Damage: " + fighter.getBlastDamage());
		maxKi.setText("Max Ki: " + fighter.getMaxKi());
		maxStamina.setText("Max Stamina: " + fighter.getMaxStamina());
		abilityPoints.setText("Ability Points: " + fighter.getAbilityPoints());
		setVisible(true);
		repaint();
		validate();
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

}
