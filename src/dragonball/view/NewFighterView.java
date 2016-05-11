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
	private JLabel fighterIcon;

	private JButton csaiyan;
	private JButton cmajin;
	private JButton cfrieza;
	private JButton cearthling;
	private JButton cnamekian;
	private JButton create;

	public NewFighterView() {
		super("DragonBallZ");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLayout(new BorderLayout());

		chosenRace = "Saiyan";

		
		JPanel image = new JPanel();
		image.setBackground(new Color(179, 255, 230));
		
		add(image,BorderLayout.CENTER);

		JPanel buttons = new JPanel();
		buttons.setBackground(new Color(179, 255, 230));
		buttons.setLayout(new GridLayout(1, 0));

		JPanel textbar = new JPanel();
		textbar.setBackground(new Color(179, 255, 230));

		
		enter = new JLabel("Enter fighter's name");

		name = new JTextArea();
		name.setPreferredSize(new Dimension(100, 20));

		create = new JButton("Create");
		create.setName("create");
		create.setBackground(new Color(61, 92, 92));
		create.setForeground(new Color(240, 245, 245));

//		JPanel left = new JPanel(new BorderLayout());
		JPanel left = new JPanel(new GridLayout(0, 1));
		left.setBackground(new Color(179, 255, 230));
		
		JPanel creating = new JPanel();
		creating.setBackground(new Color(179, 255, 230));
		creating.add(enter);
		creating.add(name);
		creating.add(create);
		
		left.add(creating);
		left.add(textbar);
		
//		left.add(creating, BorderLayout.NORTH);
//		left.add(textbar, BorderLayout.CENTER);
		
		add(left, BorderLayout.WEST);

		ImageIcon icon = WorldView.resizeIcon("Saiyan choose.png", 300, 500);
		fighterIcon = new JLabel(icon);
		image.add(fighterIcon);

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

		text = new JTextArea(info("Saiyan"));
		text.setEditable(false);
		text.setBackground(new Color(179, 255, 230));
		text.setFont(new Font("ariel", Font.BOLD, 20));

		textbar.add(text);

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		setBounds(50, 50, 500, 500);

		repaint();
		validate();
		setVisible(true);

	}

	public void update(String s) {
		chosenRace = s;
		text.setText(info(s));
		fighterIcon.setIcon(WorldView.resizeIcon(s + " choose.png", width(), height()));
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

	public JTextArea getText() {
		return text;
	}

	public JLabel getFighterIcon() {
		return fighterIcon;
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

	public String info(String race) {
		if (race.equals("Saiyan"))
			return "Saiyan\nMax HP: 1000\nBlast Damage:150\nPhysical Damage: 100\nMax ki: 5\nMax Stamina: 3";
		if (race.equals("Majin"))
			return "Majin\nMax HP: 1500\nBlast Damage:50\nPhysical Damage: 50\nMax ki: 3\nMax Stamina: 6";
		if (race.equals("Namekian"))
			return "Namekian\nMax HP: 1350\nBlast Damage:0\nPhysical Damage: 50\nMax ki: 3\nMax Stamina: 6";
		if (race.equals("Earthling"))
			return "Earthling\nMax HP: 1250\nBlast Damage:50\nPhysical Damage: 50\nMax ki: 4\nMax Stamina: 4";
		if (race.equals("Frieza"))
			return "Frieza\nMax HP: 1100\nBlast Damage:75\nPhysical Damage: 75\nMax ki: 4\nMax Stamina: 4";
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
