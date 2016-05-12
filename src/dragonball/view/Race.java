package dragonball.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import dragonball.model.character.fighter.PlayableFighter;

@SuppressWarnings("serial")
public class Race extends JPanel {

	private JLabel maxHealthPoints;
	private JLabel physicalDamage;
	private JLabel blastDamage;
	private JLabel maxKi;
	private JLabel maxStamina;
	private JLabel fighterIcon;
	private JLabel raceIcon;

	public Race(String race, int HP, int physical_Damage, int blast_Damage, int ki, int stamina,
			ImageIcon fighter_icon) {
		Color color = new Color(179, 202, 230);
		setLayout(new GridLayout(0, 1));
		setBackground(color);

		ImageIcon icon = WorldView.resizeIcon("physical damage.png", 50, 50);
		physicalDamage = new JLabel("Physical Damage: " + physical_Damage);
		physicalDamage.setIcon(icon);

		icon = WorldView.resizeIcon("hpb.png", 50, 50);

		maxHealthPoints = new JLabel("HP:" + HP);
		maxHealthPoints.setIcon(icon);

		icon = WorldView.resizeIcon("blast damage.png", 50, 50);
		blastDamage = new JLabel("Blast Damage: " + blast_Damage);
		blastDamage.setIcon(icon);

		icon = WorldView.resizeIcon("stamina.png", 50, 50);
		maxStamina = new JLabel("Stamina: " + stamina);
		maxStamina.setIcon(icon);

		icon = WorldView.resizeIcon("ki.png", 50, 50);
		maxKi = new JLabel("Ki: " + ki);
		maxKi.setIcon(icon);

		fighterIcon = new JLabel(fighter_icon);

		raceIcon = new JLabel(WorldView.resizeIcon(race + ".png", 50, 50));

		JPanel info = new JPanel(new GridLayout(0, 1));
		info.setBackground(color);

		info.add(raceIcon);
		info.add(maxHealthPoints);
		info.add(physicalDamage);
		info.add(blastDamage);
		info.add(maxKi);
		info.add(maxStamina);

		JPanel chara = new JPanel();
		chara.add(fighterIcon);
		chara.setBackground(color);

		setLayout(new BorderLayout());
		add(chara, BorderLayout.CENTER);
		add(info, BorderLayout.WEST);
	}

	public JLabel getMaxHealthPoints() {
		return maxHealthPoints;
	}

	public JLabel getPhysicalDamage() {
		return physicalDamage;
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

	public JLabel getFighterIcon() {
		return fighterIcon;
	}

	public JLabel getRaceIcon() {
		return raceIcon;
	}

	public void update(PlayableFighter fighter) {
		physicalDamage.setText("Physical Damage: " + fighter.getPhysicalDamage());
		blastDamage.setText("Blast Damage: " + fighter.getBlastDamage());
		maxHealthPoints.setText("HP: " + fighter.getMaxHealthPoints());
		maxStamina.setText("Stamina: " + fighter.getMaxStamina());
		maxKi.setText("Ki: " + fighter.getMaxKi());

		raceIcon.setIcon(WorldView.resizeIcon(String.format("%s.png", WorldView.race(fighter)), 50, 50));

		fighterIcon.setIcon(WorldView.resizeIcon(String.format("%s.png", WorldView.charRace(fighter)),
				WorldView.width(fighter), WorldView.height(fighter)));
	}

}
