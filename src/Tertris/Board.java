// Board.java
package Tertris;

/**
 * CS108 Tetris Board. Represents a Tetris board -- essentially a 2-d grid of
 * booleans. Supports tetris pieces and row clearing. Has an "undo" feature that
 * allows clients to add and remove pieces efficiently. Does not do any drawing
 * or have any idea of pixels. Instead, just represents the abstract 2-d board.
 */
public class Board {
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	
	// My contribute
	private int my_currentY;
	
	private boolean[][] xGrid;

	// Here a few trivial methods are provided:

	/**
	 * Creates an empty board of the given width and height measured in blocks.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;

		// YOUR CODE HERE
		xGrid = new boolean[width][height];
	}

	/**
	 * Returns the width of the board in blocks.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Returns the height of the board in blocks.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the max column height present in the board. For an empty board
	 * this is 0.
	 */
	public int getMaxHeight() {
		int max_height = 0;
		for (int i = 0; i < width; i++) {
			int count = 0;
			for (int j = 0; j < height; j++) {
				if (grid[i][j] == true) {
					count++;
				}
			}
			if (i == 0 || count > max_height) {
				max_height = count;
			}
		}
		return max_height; // YOUR CODE HERE
	}

	/**
	 * Checks the board for internal consistency -- used for debugging.
	 */
	public void sanityCheck() {
		if (DEBUG) {
			// YOUR CODE HERE
		}
	}

	/**
	 * Given a piece and an x, returns the y value where the piece would come to
	 * rest if it were dropped straight down at that x.
	 * 
	 * <p>
	 * Implementation: use the skirt and the col heights to compute this fast --
	 * O(skirt length).
	 */
	public int dropHeight(Piece piece, int x) {
		int[] skirt_arr = piece.getSkirt();
		int drop_y = 0;
		for(int i = 0; i < skirt_arr.length; i++){
			if(getColumnHeight(x+i) - skirt_arr[i] > drop_y){
				drop_y = getColumnHeight(x+i) - skirt_arr[i];
			}
		}
		
		if(drop_y >= get_myCurrentY()){
			drop_y = 0;
			for(int i = 0; i < my_currentY; i++){
				if(grid[x][i] == true){
					drop_y = i;
				}
			}
			drop_y++;
		}

		return drop_y; // YOUR CODE HERE
		
	}

	/**
	 * Returns the height of the given column -- i.e. the y value of the highest
	 * block + 1. The height is 0 if the column contains no blocks.
	 */
	public int getColumnHeight(int x) {
		int column_height=-1;
		for (int i = 0; i < height; i++) {
			if (grid[x][i] == true) {
				column_height = i;
			}
		}
		column_height++;
		return column_height; // YOUR CODE HERE
	}

	/**
	 * Returns the number of filled blocks in the given row.
	 */
	public int getRowWidth(int y) {	
		int row_width = 0;
		for (int i = 0; i < width; i++) {
			if (grid[i][y] == true) {
				row_width++;
			}
		}
		return row_width; // YOUR CODE HERE
	}

	/**
	 * Returns true if the given block is filled in the board. Blocks outside of
	 * the valid width/height area always return true.
	 */
	public boolean getGrid(int x, int y) {
		if (grid[x][y] == true || x < 0 || x >= width || y < 0 || y >= height) {
			return true;
		}
		return false; // YOUR CODE HERE
	}

	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 * Attempts to add the body of a piece to the board. Copies the piece blocks
	 * into the board grid. Returns PLACE_OK for a regular placement, or
	 * PLACE_ROW_FILLED for a regular placement that causes at least one row to
	 * be filled.
	 * 
	 * <p>
	 * Error cases: A placement may fail in two ways. First, if part of the
	 * piece may falls out of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 * Or the placement may collide with existing blocks in the grid in which
	 * case PLACE_BAD is returned. In both error cases, the board may be left in
	 * an invalid state. The client can use undo(), to recover the valid,
	 * pre-place state.
	 */
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed)
			throw new RuntimeException("place commit problem");

		int result = PLACE_OK;
		
		set_myCurrentY(y);
		// YOUR CODE HERE
		// back up
		for (int i = 0; i < grid.length; i++) {
			boolean[] aGrid = grid[i];
			int aLength = aGrid.length;
			System.arraycopy(aGrid, 0, xGrid[i], 0, aLength);
		}

		TPoint[] body = piece.getBody();
		
		// MY CHECK
		for (int i = 0; i < body.length; i++) {
			int check_x = x + body[i].x;
			int check_y = y + body[i].y;

			if (check_x >= width || check_x < 0 || check_y >= height
					|| check_y < 0) {
				result = PLACE_OUT_BOUNDS;
				break;
			}

			if (grid[check_x][check_y] == true) {
				result = PLACE_BAD;
				break;
			}
		}
		// MY PLACE
		if (result == PLACE_OK) {
			
			for (int i = 0; i < body.length; i++) {
				int check_x = x + body[i].x;
				int check_y = y + body[i].y;
				grid[check_x][check_y] = true;
			}
			
			for (int i = 0; i < piece.getHeight(); i++) {
				if (getRowWidth(i+y) == width ){
					return PLACE_ROW_FILLED;
				}
			}
		}
				
		return result;
	}
	
	//My Method
		public void set_myCurrentY(int newY){
			my_currentY = newY;
		}
		
		public int get_myCurrentY(){
			return my_currentY;
		}

	/**
	 * Deletes rows that are filled all the way across, moving things above
	 * down. Returns the number of rows cleared.
	 */
	public int clearRows() {
		int rowsCleared = 0;
		// YOUR CODE HERE
		for(int i = 0; i < getMaxHeight(); i++){
			if(getRowWidth(i) == width){
				rowsCleared++;
				for(int j = 0; j < width; j++){
					grid[j][i] = false;
				}
				for(int k = i; k < getMaxHeight(); k++){
					for(int m = 0; m < width; m++){
						grid[m][k] = grid[m][k+1];
					}
				}
				i--;
			}
		}
		sanityCheck();
		return rowsCleared;
	}
	
	/**
	 * Reverts the board to its state before up to one place and one
	 * clearRows(); If the conditions for undo() are not met, such as calling
	 * undo() twice in a row, then the second undo() does nothing. See the
	 * overview docs.
	 */
	public void undo() {
		// YOUR CODE HERE
		for (int i = 0; i < grid.length; i++) {
			boolean[] aGrid = xGrid[i];
			int aLength = aGrid.length;
			System.arraycopy(aGrid, 0, grid[i], 0, aLength);
		}
	}

	/**
	 * Puts the board in the committed state.
	 */
	public void commit() {
		committed = true;
	}

	/*
	 * Renders the board state as a big String, suitable for printing. This is
	 * the sort of print-obj-state utility that can help see complex state
	 * change over time. (provided debugging utility)
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height - 1; y >= 0; y--) {
			buff.append('|');
			for (int x = 0; x < width; x++) {
				if (getGrid(x, y))
					buff.append('+');
				else
					buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x = 0; x < width + 2; x++)
			buff.append('-');
		return (buff.toString());
	}
}
