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
		setBounds(50, 50, 1000, 650);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		this.dragon = dragon;

		JPanel x = new JPanel(new GridLayout(0, 1));
		x.setBackground(new Color(179, 204, 204));

		JLabel y = new JLabel("CHOOSE A WISH");
		y.setIcon(new ImageIcon("resources/images/wish.png"));
		y.setFont(new Font("ariel", Font.BOLD, 50));
		y.setForeground(Color.black);
		x.add(y);

		ImageIcon icon = WorldView.resizeIcon("senzu beans.png", 50, 50);

		senzu = new JLabel("SENZU BEANS", JLabel.CENTER);
		senzu.setName("senzuDragon");
		senzu.setForeground(Color.BLACK);
		senzu.setFont(new Font("ariel", Font.BOLD, 40));
		senzu.setIcon(icon);

		if (dragon.getSenzuBeans() > 0)
			x.add(senzu);

		icon = WorldView.resizeIcon("ability points.png", 50, 50);

		ability = new JLabel("ABILITY POINTS", JLabel.CENTER);
		ability.setName("abilityDragon");
		ability.setForeground(Color.BLACK);
		ability.setFont(new Font("ariel", Font.BOLD, 40));
		ability.setIcon(icon);

		if (dragon.getAbilityPoints() > 0)
			x.add(ability);

		icon = WorldView.resizeIcon("ultimate attack.png", 50, 50);

		ultimateAttack = new JLabel("ULTIMATE ATTACK", JLabel.CENTER);
		ultimateAttack.setName("ultimateDragon");
		ultimateAttack.setForeground(Color.BLACK);
		ultimateAttack.setFont(new Font("ariel", Font.BOLD, 40));
		ultimateAttack.setIcon(icon);

		if (dragon.getUltimateAttacks() != null && dragon.getUltimateAttacks().size() > 0)
			x.add(ultimateAttack);

		icon = WorldView.resizeIcon("super attack.png", 50, 50);

		superAttack = new JLabel("SUPER ATTACK", JLabel.CENTER);
		superAttack.setName("superDragon");
		superAttack.setForeground(Color.BLACK);
		superAttack.setFont(new Font("ariel", Font.BOLD, 40));
		superAttack.setIcon(icon);

		if (dragon.getSuperAttacks() != null && dragon.getSuperAttacks().size() > 0)
			x.add(superAttack);

		add(x, BorderLayout.CENTER);

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
}
