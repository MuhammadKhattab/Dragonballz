package dragonball.view;

import java.awt.GridLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.player.Player;

@SuppressWarnings("serial")
public class AssignAttackView extends JFrame {

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

	public AssignAttackView(Player player) {
		super("DragonBallZ");
		setLayout(new GridLayout(0, 4));
		setBounds(175, 300, 750, 100);
		this.player = player;

		oldSupersLabel = new JLabel("choose old super attack");
		newSupersLabel = new JLabel("choose new super attack");
		oldUltimatesLabel = new JLabel("choose old ultimate attack");
		newUltimatesLabel = new JLabel("choose new ultimate attack");

		PlayableFighter active = player.getActiveFighter();
		
		String[] a = new String[active.getSuperAttacks().size() + 1];
		if (active.getSuperAttacks() != null) {
			for (int i = 1; i < a.length; i++) {
				a[i] = active.getSuperAttacks().get(i - 1).getName();
			}
		}
		a[0] = "...";
		oldSupersBox = new JComboBox<>(a);

		String[] x = new String[player.getSuperAttacks().size()];
		if (player.getSuperAttacks() != null) {
			for (int i = 0; i < x.length; i++) {
				x[i] = player.getSuperAttacks().get(i).getName();
			}
		}
		newSupersBox = new JComboBox<>(x);

		String[] b = new String[active.getUltimateAttacks().size() + 1];
		if (player.getActiveFighter().getUltimateAttacks() != null) {
			for (int i = 1; i < b.length; i++) {
				b[i] = active.getUltimateAttacks().get(i - 1).getName();
			}
		}
		b[0] = "...";
		oldUltimatesBox = new JComboBox<>(b);

		String[] y = new String[player.getUltimateAttacks().size()];
		if (player.getUltimateAttacks() != null) {
			for (int i = 0; i < y.length; i++) {
				y[i] = player.getUltimateAttacks().get(i).getName();
			}
		}

		newUltimatesBox = new JComboBox<>(y);
		
		ok = new JButton("Assign attack");
		ok.setName("ok");

		add(oldSupersLabel);
		add(newSupersLabel);
		add(oldUltimatesLabel);
		add(newUltimatesLabel);
		add(oldSupersBox);
		add(newSupersBox);
		add(oldUltimatesBox);
		add(newUltimatesBox);
		add(ok);

		validate();
		setVisible(true);
	}

	public void update() {
		String[] a = new String[player.getActiveFighter().getSuperAttacks().size() + 1];
		if (player.getActiveFighter().getSuperAttacks().size() > 0) {
			for (int i = 1; i < a.length; i++) {
				SuperAttack x = player.getActiveFighter().getSuperAttacks().get(i - 1);
				if(x!=null)
					a[i]=x.getName();
			}
		}
		a[0] = "...";
		oldSupersBox.setModel(new DefaultComboBoxModel<String>(a));
		
		String[] b = new String[player.getActiveFighter().getUltimateAttacks().size() + 1];
		if (player.getActiveFighter().getUltimateAttacks().size() > 0) {
			for (int i = 1; i < b.length; i++) {
				UltimateAttack x = player.getActiveFighter().getUltimateAttacks().get(i - 1);
				if(x!=null)
					b[i]=x.getName();
			}
		}
		b[0] = "...";
		oldUltimatesBox.setModel(new DefaultComboBoxModel<String>(b));
		validate();
		repaint();
	}

	public JLabel getOldsuper() {
		return oldSupersLabel;
	}

	public void setOldsuper(JLabel oldsuper) {
		this.oldSupersLabel = oldsuper;
	}

	public JLabel getNewsuper() {
		return newSupersLabel;
	}

	public void setNewsuper(JLabel newsuper) {
		this.newSupersLabel = newsuper;
	}

	public JLabel getOldultimate() {
		return oldUltimatesLabel;
	}

	public void setOldultimate(JLabel oldultimate) {
		this.oldUltimatesLabel = oldultimate;
	}

	public JLabel getNewultimate() {
		return newUltimatesLabel;
	}

	public void setNewultimate(JLabel newultimate) {
		this.newUltimatesLabel = newultimate;
	}

	public JButton getOk() {
		return ok;
	}

	public void setOk(JButton ok) {
		this.ok = ok;
	}

	public JComboBox<String> getNewSupers() {
		return newSupersBox;
	}

	public void setNewSupers(JComboBox<String> newSupers) {
		this.newSupersBox = newSupers;
	}

	public JComboBox<String> getNewUltimates() {
		return newUltimatesBox;
	}

	public void setNewUltimates(JComboBox<String> newUltimates) {
		this.newUltimatesBox = newUltimates;
	}

	public JComboBox<String> getOldSupers() {
		return oldSupersBox;
	}

	public void setOldSupers(JComboBox<String> oldSupers) {
		this.oldSupersBox = oldSupers;
	}

	public JComboBox<String> getOldUltimates() {
		return oldUltimatesBox;
	}

	public void setOldUltimates(JComboBox<String> oldUltimates) {
		this.oldUltimatesBox = oldUltimates;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}