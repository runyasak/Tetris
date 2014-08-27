package Tertris;
import static org.junit.Assert.*;

import org.junit.Test;


public class My_Test {

	private Board board;
	private Piece STICK, L1, L2, S1, S2, SQR, PYR;
	@Test
	public void test_checkBoard() {
		board = new Board(10, 20);
		
		assertEquals(10, board.getWidth());
		assertEquals(20, board.getHeight());
		assertEquals(0, board.getColumnHeight(0));
		assertEquals(0, board.getRowWidth(4));
	}
	
	@Test
	public void test_placeOK() {
		board = new Board(10, 20);
		STICK = new Piece(Piece.STICK_STR);
		SQR = new Piece(Piece.SQUARE_STR);
		
		assertEquals(Board.PLACE_OK,board.place(STICK, 0, 0));
		assertEquals(Board.PLACE_OK,board.place(SQR, 0, 4));
		assertEquals(6, board.getMaxHeight());
		assertEquals(1, board.getRowWidth(0));
	}
	
	@Test
	public void test_placeBad() {
		board = new Board(10, 20);
		L1 = new Piece(Piece.L1_STR);
		PYR = new Piece(Piece.PYRAMID_STR);
		
		assertEquals(Board.PLACE_OK, board.place(L1, 0, 0));
		assertEquals(Board.PLACE_BAD, board.place(PYR, 0, 0));
		
		//The piece that place bad will be disappeared.
		assertEquals(3, board.getMaxHeight());
		assertEquals(2, board.getRowWidth(0));
	}
	
	@Test
	public void test_placeOutBound() {
		board = new Board(10, 20);
		S1 = new Piece(Piece.S1_STR);
		S2 = new Piece(Piece.S2_STR);
		
		assertEquals(Board.PLACE_OUT_BOUNDS, board.place(S1, -1, 0));
		assertEquals(Board.PLACE_OUT_BOUNDS, board.place(S2, 11, 0));
		assertEquals(Board.PLACE_OUT_BOUNDS, board.place(S1, 5, -9));
		
		//The piece that place out bound will be disappeared.
		assertEquals(0, board.getMaxHeight());
	}
	
	@Test
	public void test_placeRowFilled() {
		board = new Board(4, 8);
		L1 = new Piece(Piece.STICK_STR);
		L1 = L1.computeNextRotation();
		
		assertEquals(Board.PLACE_ROW_FILLED, board.place(L1, 0, 0));
	}
}
