import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.text.DecimalFormat;
import javax.swing.JLabel;


public class Sudoku extends JPanel implements ActionListener
{
	//final instance fields
	private static final int N = 9;
	private static final int MED = 3 * N, EASY = 5 * N;
	
	//instance fields
	private Move[][] board = new Move[N][N];
	private JFrame f;
	private Container cp;
	private Menu m;
	private int clickCount = 0;
    long StartTime;
	long EndTime;
	long timeElapsed;
	long seconds;
	long minutes;


	/*
	 * Creates a new Sudoku object
	 */
	public static void main(String[] args)
	{
        new Sudoku();
	}
	
	/*
	 * Constructs a new Sudoku game
	 */
	public Sudoku()
	{
		init();
		
		f = new JFrame("Sudoku");
		cp = f.getContentPane();
		m = new Menu(this);
		f.setJMenuBar(m.createMenuBar());
		cp.add(this);

		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 530);
		f.setResizable(false);
		f.setVisible(true);	
		
		GridLayout grid = new GridLayout(N, N); 
		grid.setVgap(2);
		grid.setHgap(2);

		this.setLayout(grid);
        this.reset();
	}
	
	//Implemented Methods
	
	/*
	 * Method from the ActionPerformed interface
     * Called whenever an action is performed
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() instanceof Move)
		{
			Move temp = (Move)e.getSource();
            if (!temp.isModifiable())
                return;
			String in = JOptionPane.showInputDialog(null);
		
			clickCount++;
		
			try
			{
                int row = temp.getRow();
                int col = temp.getCol();
                int val = Integer.parseInt(in);
                if (validMove(row, col, val))
                {
                    temp.setForeground(Color.MAGENTA);
                    temp.setValue(val);
                }
                else
                    JOptionPane.showMessageDialog(this, "Invalid Move", "", JOptionPane.INFORMATION_MESSAGE);
			}
			catch (NumberFormatException err)
			{
			
			}
		}
	}
	
	//Public Methods
	
	/*
	 * The about method displays an about dialog to the user
	 */
	public void about()
	{
		String str = "Author: Jessica Ji and Tina Li + \n + CS3 Project";
		String title = "About - Sudoku";
		
		JOptionPane.showMessageDialog(this, str, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	/*
	 * The clear method clears all user specified entries from the board
	 */
	public void clear()
	{
		for(int row = 0; row < 9; row++)
		{
			for(int col = 0; col < 9; col++)
			{
				board[row][col].reset();
			}
		}
	}
	
	/*
	 * The help method displays a help dialog for the user
	 */
	public void help()
	{
		String str = "Fill in the grid so that\n     every row,\n" +
				"     every column, and\n     every 3 x 3 box\ncontains" +
				" the digits 1 through 9.\n\nTo create custom game\n    1) " +
				"Reset the Board\n    2) Set values\n    3) Select custom from the new game menu";
		String title = "Sudoku" + " - Help";
		
		JOptionPane.showMessageDialog(this, str, title, JOptionPane.INFORMATION_MESSAGE);		
	}
	
	/*
	 * The new custom game allows the user to specify a number of
	 * elements to be non modifiable. Allows recreation of Sudoku
	 * puzzles seen elsewhere
	 */
	public void newCustom()
	{
		for(int row = 0; row < N; row++)
		{
			for(int col = 0; col < N; col++)
			{
				if(board[row][col].getValue() != 0)
				{
					board[row][col].setUnMod();
					board[row][col].setForeground(Color.BLACK);
				}
				board[row][col].clear();
			}
		}
		StartTime = System.currentTimeMillis();		
	}
	
	/*
	 * Creates a new game with the number of pre filled squares
	 * determined by the difficulty. 1 - Easy, 2 - Medium
	 */
	public void newGame(int diff)
	{
		reset();
        generator();
		
		ArrayList<Integer> valid = new ArrayList<Integer>(spaceCollection());
		int randRow = 0, randCol = 0, count = 0;
		
		switch(diff)
		{
			case 1:
				count = EASY;
				break;
			
			case 2:
				count = MED;
				break;
		}
		
		while(count > 0)
		{
			randRow = valid.get((int)(Math.random() * N));
			randCol = valid.get((int)(Math.random() * N));
			
            if (board[randRow][randCol].isModifiable())
            {
                board[randRow][randCol].setForeground(Color.BLACK);
                board[randRow][randCol].setUnMod();
                count--;
            }
		}

        for(int row = 0; row < 9; row++)
		{
			for(int col = 0; col < 9; col++)
			{
				if (board[row][col].isModifiable())
                    board[row][col].clear();
			}
		}

		//starts the timer
    	StartTime = System.currentTimeMillis();
	}

	/*
	 * Generates random values to place in random spots
	 * Puts the values into their corresponding spot
	 */
    public void generator()
    {
        clear();
        ArrayList<Integer> valid = new ArrayList<Integer>(spaceCollection());
        int r = valid.get((int)(Math.random() * N));
		int c = valid.get((int)(Math.random() * N));
        r = 0;
        c = 0;
		int value = 1 + (int)(Math.random() * N);
        board[r][c].setForeground(Color.BLACK);
		board[r][c].setValue(value);
		board[r][c].setUnMod();
        solve(0);
        output();
    }
	
	/*
	 * The quit method exits the game
	 */
	public void quit()
	{
		f.dispose();
	}

	/*
	 * The reset method is used to clear all squares on the board
	 * including the unchangable game squares
	 */
	public void reset()
	{
		for(int row = 0; row < 9; row++)
		{
			for(int col = 0; col < 9; col++)
			{
				board[row][col].reset();
			}
		}
	}
	
	

	/*
	 * The verifySudoku verifies that a solution is correct and 
	 * displays a message to the user
	 * 
	 * returns true if the solution is valid and false if not
	 */
	public boolean verifySudoku()
	{
		boolean result = true;
		
		if (checkEmpty())
		{
			result = false;
		}

        if (result == true)
        {
            for(int i = 0; i < N; i++)
            {
                if(!validRow(i) || !validCol(i) || !validCube(i))
                {
                    result = false;
                    break;
                }
            }
        }
		
		if(result)
		{
			EndTime = System.currentTimeMillis();
			timeElapsed = (StartTime - EndTime)/1000;
			seconds = timeElapsed%60;
			minutes = timeElapsed/60;
			JOptionPane.showMessageDialog(this, "Congratulations!\nYou took " + clickCount + " moves \nand\n " + minutes + " minutes " + seconds + " seconds", "", JOptionPane.INFORMATION_MESSAGE);			
			
		}
		else
		{
			JOptionPane.showMessageDialog(this, "Not Quite There Yet", "", JOptionPane.ERROR_MESSAGE);
		}
		
		return result;
	}

	/*
	 * Completes the rest of the board
	 * Fills in the missing values with valid ones
	 */
    public boolean solve(int at) {
        if (at == N*N)
            return true;
        int r = at / N;
        int c = at % N;
        if (!board[r][c].isModifiable())
            return solve(at + 1);
        ArrayList<Integer> num = new ArrayList<Integer> (Move.initValid());
        for (int i = 0; i < 9; i++) {
            int index = (int)(Math.random() * num.size());
            int value = num.remove(index);
            if (validMove(r, c, value)) {
                board[r][c].setForeground(Color.GREEN);
				board[r][c].setValue(value);
                if (solve(at + 1))
                    return true;
                board[r][c].clear();
            }
        }
        return false;
    }
	
	//Private methods
	
	/*
	 * The spaceCollection method returns an Integer ArrayList containing
	 * the values 0 through N - 1 corresponding to indexes for the rows,
	 * columns, and cubes
	 */
	private ArrayList<Integer> spaceCollection ()
	{
		ArrayList<Integer> spaceList = new ArrayList<Integer>();
		for(int i = 0; i < N; i++)
		{
			spaceList.add(i);
		}
		
		return spaceList;
	}
	
	/*
	 * The validMove method is used to check if a certain move is valid
	 *
     * row is the row on the board
	 * col is the column on the board
	 * move is the value to place in the spot with the specified row and col
	 * 
     * returns true if the move is valid and false otherwise
	 */
	private boolean validMove(int row, int col, int move)
	{
		int orig = 0;
		boolean result = false;
		
		if(board[row][col].isModifiable())
		{
			orig = board[row][col].getValue();
					
			board[row][col].setValue(move);
		
			if(validRow(row) && validCol(col) && validCube(findCube(row, col)))
			{
				result = true;
			}
		
			board[row][col].setValue(orig);
		}
		
		return result;
	}
	
	/*
	 * The validRow method is used to determine if the current row is
	 * valid, ignoring empty squares
	 * 
	 * row is the row index
	 *
     * returns true if the row is valid and false otherwise
	 */
	private boolean validRow(int row)
	{
		ArrayList<Integer> num = new ArrayList<Integer> (Move.initValid());
		boolean valid = true;
		
		for(int i = 0; i < 9; i++)
		{
			Integer temp = board[row][i].getValue();
			if(num.contains(temp))
			{
				num.remove(temp);
			}
			else if (temp != 0)
			{
				valid = false;
				break;
			}
		}
		
		return valid;
	}
	
	/*
	 * The validCol method is used to determine if the current column
	 * is valid, ignoring empty squares
	 * 
	 * col is the column index
     * 
	 * returns true if the column is valid and false otherwise
	 */	
	private boolean validCol(int col)
	{
		ArrayList<Integer> num = new ArrayList<Integer> (Move.initValid());
		boolean valid = true;
		
		for(int i = 0; i < 9; i++)
		{
			Integer temp = board[i][col].getValue();
			if(num.contains(temp))
			{
				num.remove(temp);
			}
			else if (temp != 0)
			{
				valid = false;
				break;
			}
		}
		
		return valid;
	}
	
	/*
	 * The validCube method is used to determine if the current cube is
	 * valid, ignoring empty squares
	 * 
	 * cube is the cube index
	 * returns true if the cube is valid
	 */
	private boolean validCube(int cube)
	{
		ArrayList<Integer> num = new ArrayList<Integer> (Move.initValid());
		int enterRow = 0, enterCol = 0;
		boolean valid = true;
		
		switch(cube)
		{
			case 1:
				enterCol = 3;
				break;
				
			case 2: 
				enterCol = 6;
				break;
				
			case 3:
				enterRow = 3;
				break;
				
			case 4:
				enterRow = 3;
				enterCol = 3;
				break;
				
			case 5:
				enterRow = 3;
				enterCol = 6;
				break;
				
			case 6:
				enterRow = 6;
				break;
				
			case 7: 
				enterRow = 6;
				enterCol = 3;
				break;
				
			case 8:
				enterRow = 6;
				enterCol = 6;
				break;
		}
		
		for(int i = enterRow; i < enterRow + 3 ; i++)
		{
			for(int j = enterCol; j < enterCol + 3 ; j++)
			{
				Integer temp = board[i][j].getValue();
				if(temp != 0)
				{
					if(num.contains(temp))
					{
						num.remove(temp);
					}
					else
					{
						valid = false;
						break;
					}
				}
			}
		}
		
		return valid;
	}
	
	/*
	 * The find cube method is used to determine the cube based on 
	 * the row and column
	 * 
     * r is the row
	 * c is the column
	 * 
     * returns the cube number
	 */
	private int findCube(int r, int c)
	{
		int cube = 0;
		
		if(r < 3 && c < 3)
		{
			cube = 0;
		}
		else if (r < 3 && c < 6)
		{
			cube = 1;
		}
		else if (r < 3)
		{
			cube = 2;
		}
		else if(r < 6 && c < 3)
		{
			cube = 3;
		}
		else if (r < 6 && c < 6)
		{
			cube = 4;
		}
		else if (r < 6)
		{
			cube = 5;
		}
		else if(c < 3)
		{
			cube = 6;
		}
		else if (c < 6)
		{
			cube = 7;
		}
		else
		{
			cube = 8;
		}
		
		return cube;
	}
	
	/*
	 * The checkEmpty method is used to determine if there are any empty
	 * squares on the board
	 * 
     * returns true if any empty squares are found
	 */
	private boolean checkEmpty()
	{
		boolean result = false;
		int row, col;
		
		for(row = 0; row < 9; row++)
		{
			for(col = 0; col < 9; col++)
			{
				if(board[row][col].getValue() == 0)
				{
					result = true;
					break;
				}
			}
			if(col < 9)
			{
				break;
			}
		}
		
		return result;
	}
	
	/*
	 * The init method is called once to set up the board
	 */
	private void init()
	{
		Font font = new Font("Ariel", Font.PLAIN, 31);
		Move m;
		
		for(int row = 0; row < 9; row++)
		{
			for(int col = 0; col < 9; col++)
			{
				m = new Move(row, col);
				m.setBackground(Color.BLUE);
				
				if (row <= 2 && (col <= 2 || col > 5)){
					m.setBackground (Color.RED);
				}
				if (row >2 && row <= 5 &&  col > 2 && col  <= 5){
					m.setBackground (Color.RED);
				}
				if (row > 5 && ((col <= 2) || (col > 5))){
					m.setBackground (Color.RED);
				}
				
				m.setValue(0);
                m.setOpaque(true);
				m.addActionListener(this);
				m.setFont(font);
				m.setForeground(Color.BLACK);
				m.setFocusable(false);
				
				add(m);
				board[row][col] = m;
			}
		}
	}

    /*
     *displays the board with numbers
     */
    public void output()
    {
        for(int row = 0; row < 9; row++)
		{
			for(int col = 0; col < 9; col++)
			{
				System.out.print(board[row][col].getValue() + " ");
			}
            System.out.println();
		}
    }
}