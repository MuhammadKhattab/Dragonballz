package dragonball.controller;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import dragonball.model.attack.MaximumCharge;
import dragonball.model.attack.SuperAttack;
import dragonball.model.attack.SuperSaiyan;
import dragonball.model.attack.UltimateAttack;
import dragonball.model.character.fighter.PlayableFighter;
import dragonball.model.character.fighter.Saiyan;
import dragonball.model.exceptions.MissingFieldException;
import dragonball.model.exceptions.UnknownAttackTypeException;
import dragonball.model.game.Game;
import dragonball.model.player.Player;
import dragonball.view.WindowDestroyer;

@SuppressWarnings("serial")
public class Main extends JFrame implements MouseListener {

	private Controller controller;

	public Main() {

		setBounds(50, 50, 1000, 650);
		setTitle("DragonBallZ");
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		setLayout(new BorderLayout());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowDestroyer());

		JLabel startGame = new JLabel("START", JLabel.CENTER);
		startGame.setName("start");
		startGame.setIcon(new ImageIcon("resources/images/start.png"));
		startGame.setFont(new Font("ariel", Font.BOLD, 40));
		startGame.setForeground(Color.DARK_GRAY);
		startGame.addMouseListener(this);

		JLabel load = new JLabel("LOAD", JLabel.CENTER);
		load.setName("load");
		load.setIcon(new ImageIcon("resources/images/load.png"));
		load.setFont(new Font("ariel", Font.BOLD, 40));
		load.setForeground(Color.DARK_GRAY);
		load.addMouseListener(this);

		JLabel code = new JLabel("CHEAT", JLabel.CENTER);
		code.setName("cheat");
		code.setIcon(new ImageIcon("resources/images/cheat.png"));
		code.setFont(new Font("ariel", Font.BOLD, 40));
		code.setForeground(Color.DARK_GRAY);
		code.addMouseListener(this);

		JPanel pan = new JPanel();
		pan.setLayout(new GridLayout(3, 0));
		pan.setBackground(new Color(179, 204, 255));
		pan.add(startGame);
		pan.add(load);
		pan.add(code);

		add(pan, BorderLayout.CENTER);

		setVisible(true);
	}

	public void startGame() throws UnknownAttackTypeException, MissingFieldException {
		Game game = new Game();

		Player p = game.getPlayer();
		p.setName(JOptionPane.showInputDialog("Enter the player's name"));

		controller = new Controller(game);
		dispose();
	}

	public void load() throws ClassNotFoundException, IOException {
		Game game = new Game();

		ArrayList<String> savedFiles = loadSaves();
		String[] saves = new String[savedFiles.size() + 1];
		for (int i = 0; i < savedFiles.size(); i++)
			saves[i] = savedFiles.get(i);
		saves[savedFiles.size()] = "...";
		JFrame frame = new JFrame("Choose a saved game:");
		String file = (String) JOptionPane.showInputDialog(frame, "Choose a saved game", "Choose a saved game",
				JOptionPane.QUESTION_MESSAGE, null, saves, saves[0]);
		if (file != null && file.length() > 0)
			game.load("data/" + file + ".csv");

		new Controller(game);
		dispose();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		JLabel x = (JLabel) e.getSource();
		switch (x.getName()) {
		case "start":
			try {
				startGame();
			} catch (UnknownAttackTypeException | MissingFieldException e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage());
			}
			break;
		case "load":
			try {
				load();
			} catch (ClassNotFoundException | IOException e2) {
				JOptionPane.showMessageDialog(null, e2.getMessage());
			}
			break;
		case "cheat":
			String input = JOptionPane.showInputDialog("Write the cheat code:");
			try {
				cheat(input);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage());
			}
			break;
		}
	}

	public void cheat(String input) throws IOException, Exception {
		if (input != null) {
			input = input.toUpperCase();
			if (input.equals("OVER9000")) {
				startGame();

				Player p = controller.getGame().getPlayer();
				p.setDragonBalls(9000);

				PlayableFighter f = new Saiyan("Deadpool");
				p.getFighters().add(f);
				p.setActiveFighter(f);

				f.setAbilityPoints(9000);
//				f.setMaxHealthPoints(13);

				ArrayList<UltimateAttack> x = new ArrayList<>();
				x.add(new SuperSaiyan());
				p.setUltimateAttacks(x);

				ArrayList<SuperAttack> y = new ArrayList<>();
				y.add(new MaximumCharge());
				p.setSuperAttacks(y);

			} else
				JOptionPane.showMessageDialog(null, "No, that's wrong!");
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		e.getComponent().setForeground(Color.RED);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		e.getComponent().setForeground(Color.DARK_GRAY);
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<String> loadSaves() throws ClassNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("data/all saved files.csv")));
		ArrayList<String> x = (ArrayList<String>) ois.readObject();
		ois.close();
		return x;
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		new Main();
		// ObjectOutputStream oos = new ObjectOutputStream(new
		// FileOutputStream(new File("data/all saved files.csv")));
		// ArrayList<String> x = new ArrayList<String>();
		// oos.writeObject(x);
		// oos.close();
	}

}
