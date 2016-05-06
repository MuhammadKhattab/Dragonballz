package dragonball.view;

import java.awt.*;

import javax.swing.*;

import dragonball.model.dragon.Dragon;

@SuppressWarnings("serial")
public class DragonView extends JFrame {

	private Dragon dragon;

	private JLabel senzu;
	private JLabel ability;
	private JLabel ultimateAttack;
	private JLabel superAttack;

	public DragonView(Dragon dragon) {
		super(dragon.getName());
		// setContentPane(new JLabel(new
		// ImageIcon("resources/images/dragon.jpg")));
		setBounds(50, 50, 1000, 650);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		setLayout(new GridLayout(0, 2));

		this.dragon = dragon;

		JLabel y = new JLabel("CHOOSE A WISH");
		y.setIcon(new ImageIcon("resources/images/wish.png"));
		y.setFont(new Font("ariel", Font.BOLD, 50));
		y.setForeground(Color.black);
		add(y);

		senzu = new JLabel("SENZU BEANS", JLabel.CENTER);
		senzu.setName("senzuDragon");
		senzu.setForeground(Color.BLACK);
		senzu.setFont(new Font("ariel", Font.BOLD, 40));

		if (dragon.getSenzuBeans() > 0)
			add(senzu);

		ability = new JLabel("ABILITY POINTS", JLabel.CENTER);
		ability.setName("abilityDragon");
		ability.setForeground(Color.BLACK);
		ability.setFont(new Font("ariel", Font.BOLD, 40));

		if (dragon.getAbilityPoints() > 0)
			add(ability);

		ultimateAttack = new JLabel("ULTIMATE ATTACK", JLabel.CENTER);
		ultimateAttack.setName("ultimateDragon");
		ultimateAttack.setForeground(Color.BLACK);
		ultimateAttack.setFont(new Font("ariel", Font.BOLD, 40));

		if (dragon.getUltimateAttacks() != null && dragon.getUltimateAttacks().size() > 0)
			add(ultimateAttack);

		superAttack = new JLabel("SUPER ATTACK", JLabel.CENTER);
		superAttack.setName("superDragon");
		superAttack.setForeground(Color.BLACK);
		superAttack.setFont(new Font("ariel", Font.BOLD, 40));

		if (dragon.getSuperAttacks() != null && dragon.getSuperAttacks().size() > 0)
			add(superAttack);

		setVisible(true);
		validate();
		repaint();
	}

	public Dragon getDragon() {
		return dragon;
	}

	public void setDragon(Dragon dragon) {
		this.dragon = dragon;
	}

	public JLabel getSenzu() {
		return senzu;
	}

	public void setSenzu(JLabel senzu) {
		this.senzu = senzu;
	}

	public JLabel getAbility() {
		return ability;
	}

	public void setAbility(JLabel ability) {
		this.ability = ability;
	}

	public JLabel getUltimateAttack() {
		return ultimateAttack;
	}

	public void setUltimateAttack(JLabel ultimateAttack) {
		this.ultimateAttack = ultimateAttack;
	}

	public JLabel getSuperAttack() {
		return superAttack;
	}

	public void setSuperAttack(JLabel superAttack) {
		this.superAttack = superAttack;
	}

	public static void main(String[] args) {
		new DragonView(new Dragon("", null, null, 9, 7));
	}
}
