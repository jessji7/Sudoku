import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.KeyStroke;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/*
 * Creates the menu for the game
 */
public class Menu implements ActionListener, ItemListener {
    
    JTextArea output;
    JScrollPane scrollPane;
    Sudoku s;
    
    public Menu (Sudoku s)
    {
    	this.s = s;
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem;

        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the file menu.
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);
        
        //New Game Sub Menu
        submenu = new JMenu("New Game");
        submenu.setMnemonic(KeyEvent.VK_N);

        menuItem = new JMenuItem("Easy");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        submenu.add(menuItem);

        menuItem = new JMenuItem("Medium");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        submenu.add(menuItem);

        menuItem = new JMenuItem("Custom");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        submenu.add(menuItem);       
        
        menu.add(submenu);
        
        //Clear Option
        menuItem = new JMenuItem("Clear Board",KeyEvent.VK_C);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("Reset All",KeyEvent.VK_R);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menu.addSeparator();
        
        //Solve Option
        menuItem = new JMenuItem("Solve Puzzle",KeyEvent.VK_S);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);

        //Check Option
        menuItem = new JMenuItem("Check Solution",KeyEvent.VK_K);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menu.addSeparator();
        
        //Quit Option
        menuItem = new JMenuItem("Quit",KeyEvent.VK_Q);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
             
        //Build the Help menu.
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuItem.addActionListener(this);
        menuBar.add(menu);
        
        //Help Option
        menuItem = new JMenuItem("Contents",KeyEvent.VK_F1);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        menu.addSeparator();
        
        //About Option
        menuItem = new JMenuItem("About",KeyEvent.VK_A);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.ALT_MASK));
        menuItem.addActionListener(this);
        menu.add(menuItem);
        
        return menuBar;
    }

	public void itemStateChanged(ItemEvent e) 
	{

	}

	public void actionPerformed(ActionEvent e) 
	{
		JMenuItem source = (JMenuItem)(e.getSource());
		String act = source.getActionCommand();
		
		if(act.equalsIgnoreCase("easy"))
		{
			s.newGame(1);
		} 
		else if(act.equalsIgnoreCase("medium"))
		{
			s.newGame(2);
		}
		else if(act.equalsIgnoreCase("custom"))
		{
			s.newCustom();
		}
		else if(act.equalsIgnoreCase("reset all"))
		{
			s.reset();
		}
		else if(act.equalsIgnoreCase("clear board"))
		{
			s.clear();
		}
		else if(act.equalsIgnoreCase("solve puzzle"))
		{
			s.solve(0);
		}
		else if(act.equalsIgnoreCase("check solution"))
		{
			s.verifySudoku();
		}
		else if(act.equalsIgnoreCase("quit"))
		{
			s.quit();
		}
		else if(act.equalsIgnoreCase("contents"))
		{
			s.help();
		}
		else if(act.equalsIgnoreCase("about"))
		{
			s.about();
		}
	}
}