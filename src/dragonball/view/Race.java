package dragonball.view;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Race extends JPanel {

	private JLabel maxHealthPoints;
	private JLabel physicalDamage;
	private JLabel blastDamage;
	private JLabel maxKi;
	private JLabel maxStamina;
	private JLabel fighterIcon;

	public Race(int HP, int phyDamage, int blast_Damage, int ki, int stamina, String file,Color color) {
		
		setLayout(new GridLayout(0, 1));
		setBackground(color);
		
		maxHealthPoints = new JLabel("Max HP:" + HP);
		physicalDamage = new JLabel("Physical Damage:" + phyDamage);
		blastDamage = new JLabel("Blast Damage:" + blast_Damage);
		maxKi = new JLabel("Max Ki:" + ki);
		maxStamina = new JLabel("Max Stamina:" + stamina);

		fighterIcon = new JLabel(file);
		
	}

	public JLabel getMaxHealthPoints() {
		return maxHealthPoints;
	}

	public void setMaxHealthPoints(JLabel maxHealthPoints) {
		this.maxHealthPoints = maxHealthPoints;
	}

	public JLabel getPhysicalDamage() {
		return physicalDamage;
	}

	public void setPhysicalDamage(JLabel physicalDamage) {
		this.physicalDamage = physicalDamage;
	}

	public JLabel getBlastDamage() {
		return blastDamage;
	}

	public void setBlastDamage(JLabel blastDamage) {
		this.blastDamage = blastDamage;
	}

	public JLabel getMaxKi() {
		return maxKi;
	}

	public void setMaxKi(JLabel maxKi) {
		this.maxKi = maxKi;
	}

	public JLabel getMaxStamina() {
		return maxStamina;
	}

	public void setMaxStamina(JLabel maxStamina) {
		this.maxStamina = maxStamina;
	}

	public JLabel getFighterIcon() {
		return fighterIcon;
	}

	public void setFighterIcon(JLabel fighterIcon) {
		this.fighterIcon = fighterIcon;
	}

}
