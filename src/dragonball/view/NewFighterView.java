package dragonball.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class NewFighterView extends JFrame {

	private JTextArea text;
	private JTextArea name;
	private String chosenRace;

	private JLabel enter;

	private JButton csaiyan;
	private JButton cmajin;
	private JButton cfrieza;
	private JButton cearthling;
	private JButton cnamekian;
	private JButton create;

	private Race racePanel;

	public NewFighterView() {
		super("DragonBallZ");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());
		Color color = new Color(179, 202, 230);
		chosenRace = "Saiyan";

		JPanel buttons = new JPanel();
		buttons.setBackground(color);
		buttons.setLayout(new GridLayout(1, 0));

		JPanel turnPanel = new JPanel();
		turnPanel.setBackground(color);

		enter = new JLabel("Enter fighter's name");

		name = new JTextArea();
		name.setPreferredSize(new Dimension(100, 20));

		create = new JButton("Create");
		create.setName("create");
		create.setBackground(new Color(61, 92, 92));
		create.setForeground(new Color(240, 245, 245));

		JPanel right = new JPanel(new GridLayout(0, 1));
		right.setBackground(color);

		JPanel creating = new JPanel();
		creating.setBackground(color);

		creating.add(enter);
		creating.add(name);
		creating.add(create);

		right.add(turnPanel);
		right.add(creating);

		add(right, BorderLayout.EAST);

		csaiyan = new JButton("Saiyan");
		csaiyan.setName("saiyan choose");
		csaiyan.setBackground(new Color(61, 92, 92));
		csaiyan.setForeground(new Color(240, 245, 245));
		csaiyan.setFont(new Font("ariel", Font.BOLD, 25));

		cmajin = new JButton("Majin");
		cmajin.setName("majin choose");
		cmajin.setBackground(new Color(61, 92, 92));
		cmajin.setForeground(new Color(240, 245, 245));
		cmajin.setFont(new Font("ariel", Font.BOLD, 25));

		cearthling = new JButton("Earthling");
		cearthling.setName("earthling choose");
		cearthling.setBackground(new Color(61, 92, 92));
		cearthling.setForeground(new Color(240, 245, 245));
		cearthling.setFont(new Font("ariel", Font.BOLD, 25));

		cfrieza = new JButton("Frieza");
		cfrieza.setName("frieza choose");
		cfrieza.setBackground(new Color(61, 92, 92));
		cfrieza.setForeground(new Color(240, 245, 245));
		cfrieza.setFont(new Font("ariel", Font.BOLD, 25));

		cnamekian = new JButton("Namekian");
		cnamekian.setName("namekian choose");
		cnamekian.setBackground(new Color(61, 92, 92));
		cnamekian.setForeground(new Color(240, 245, 245));
		cnamekian.setFont(new Font("ariel", Font.BOLD, 25));

		buttons.add(csaiyan);
		buttons.add(cmajin);
		buttons.add(cearthling);
		buttons.add(cfrieza);
		buttons.add(cnamekian);

		add(buttons, BorderLayout.SOUTH);

		text = new JTextArea(info());
		text.setEditable(false);
		text.setBackground(color);
		text.setFont(new Font("ariel", Font.BOLD, 20));

		turnPanel.add(text);

		ImageIcon icon = WorldView.resizeIcon("Saiyan choose.png", 300, 500);

		racePanel = new Race(chosenRace, hp(), pd(), bd(), ki(), st(), icon);
		add(racePanel, BorderLayout.CENTER);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		setBounds(50, 50, 500, 500);

		repaint();
		validate();
		setVisible(true);

	}

	private int pd() {
		if (chosenRace.equals("Majin"))
			return 50;
		if (chosenRace.equals("Saiyan"))
			return 100;
		if (chosenRace.equals("Frieza"))
			return 75;
		if (chosenRace.equals("Earthling"))
			return 50;
		if (chosenRace.equals("Namekian"))
			return 50;
		return 0;
	}

	private int st() {
		if (chosenRace.equals("Majin"))
			return 6;
		if (chosenRace.equals("Saiyan"))
			return 3;
		if (chosenRace.equals("Frieza"))
			return 4;
		if (chosenRace.equals("Earthling"))
			return 4;
		if (chosenRace.equals("Namekian"))
			return 5;
		return 0;
	}

	private int ki() {
		if (chosenRace.equals("Majin"))
			return 3;
		if (chosenRace.equals("Saiyan"))
			return 5;
		if (chosenRace.equals("Frieza"))
			return 4;
		if (chosenRace.equals("Earthling"))
			return 4;
		if (chosenRace.equals("Namekian"))
			return 3;
		return 0;
	}

	private int bd() {
		if (chosenRace.equals("Majin"))
			return 50;
		if (chosenRace.equals("Saiyan"))
			return 150;
		if (chosenRace.equals("Frieza"))
			return 75;
		if (chosenRace.equals("Earthling"))
			return 50;
		if (chosenRace.equals("Namekian"))
			return 0;
		return 0;
	}

	int hp() {
		if (chosenRace.equals("Majin"))
			return 1500;
		if (chosenRace.equals("Saiyan"))
			return 1000;
		if (chosenRace.equals("Frieza"))
			return 1100;
		if (chosenRace.equals("Earthling"))
			return 1250;
		if (chosenRace.equals("Namekian"))
			return 1350;
		return 0;
	}

	public void update(String s) {
		chosenRace = s;

		text.setText(info());

		racePanel.getPhysicalDamage().setText("Physical Damage: " + pd());
		racePanel.getBlastDamage().setText("Blast Damage: " + bd());
		racePanel.getMaxHealthPoints().setText("HP: " + hp());
		racePanel.getMaxStamina().setText("Stamina: " + st());
		racePanel.getMaxKi().setText("Ki: " + ki());
		racePanel.getRaceIcon().setIcon(WorldView.resizeIcon(String.format("%s.png", s), 50, 50));
		racePanel.getFighterIcon().setIcon(WorldView.resizeIcon(String.format("%s choose.png", s), width(), height()));
	}

	public String getChosenRace() {
		return chosenRace;
	}

	public JButton getCreate() {
		return create;
	}

	public String getFighterName() {
		return name.getText();
	}

	public JButton getCsaiyan() {
		return csaiyan;
	}

	public JButton getCmajin() {
		return cmajin;
	}

	public JButton getCfrieza() {
		return cfrieza;
	}

	public JButton getCearthling() {
		return cearthling;
	}

	public JButton getCnamekian() {
		return cnamekian;
	}

	public String info() {
		if (chosenRace.equals("Majin"))
			return "+1 stamina/foe turn";
		if (chosenRace.equals("Saiyan"))
			return "+1 stamina/turn";
		if (chosenRace.equals("Frieza"))
			return "+1 stamina/turn";
		if (chosenRace.equals("Earthling"))
			return "+1 stamina/turn\n+1 ki/my turn";
		if (chosenRace.equals("Namekian"))
			return "+1 stamina/turn\n+50 health/turn";
		return "";
	}

	public int width() {
		if (chosenRace.equals("Majin"))
			return 426;
		if (chosenRace.equals("Saiyan"))
			return 300;
		if (chosenRace.equals("Frieza"))
			return 280;
		if (chosenRace.equals("Earthling"))
			return 187;
		if (chosenRace.equals("Namekian"))
			return 287;
		return 0;
	}

	public int height() {
		if (chosenRace.equals("Majin"))
			return 475;
		if (chosenRace.equals("Saiyan"))
			return 500;
		if (chosenRace.equals("Frieza"))
			return 444;
		if (chosenRace.equals("Earthling"))
			return 474;
		if (chosenRace.equals("Namekian"))
			return 380;
		return 0;
	}

}
