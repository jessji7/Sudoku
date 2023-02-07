import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JButton;

/*
 * Move class for the Sudoku game 
 */
public class Move extends JButton
{
	private static final ArrayList<Integer> VALID = new ArrayList<Integer> (initValid());
	private int row;
	private int col;
	private boolean modifiable;
	
	public Move (int row, int col)
	{
		this.row = row;
		this.col = col;
		this.modifiable = true;
	}

	public int getValue()
	{
		int i = 0;
		
		if(super.getText().equals(""))
		{
			i = 0;
		}
		else
		{
			i = Integer.parseInt(super.getText());
		}
		
		return i;
	}
	
	public int getCol() 
	{
		return col;
	}

	public int getRow() 
	{
		return row;
	}
	
	public boolean isModifiable() 
	{
		return modifiable;
	}

	public void setUnMod()
	{
		modifiable = false;
	}
	
	public void clear()
	{
		if(modifiable)
		{
			super.setText("");
		}
	}
	
	public void setValue(int i)
	{
		if(modifiable)
		{
			if(i == 0)
			{
				super.setText("");
			}
			else if(VALID.contains(i))
			{
				super.setText(String.valueOf(i));
			}
		}
	}
	
	public void reset()
	{
		modifiable = true;
		this.setForeground(Color.MAGENTA);
		clear();
	}
	
	protected static ArrayList<Integer> initValid()
	{
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for(int i = 1; i <= 9; i++)
		{
			temp.add(i);
		}
		
		return temp;
	}
}