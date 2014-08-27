package Tertris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated;

	// This shows how to build things in setUp() to re-use
	// across tests.

	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(3, 6);

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		b.place(pyr1, 0, 0);
	}

	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	// Make more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	//--------->>>> MY TEST <<<<<---------
	Board my_board;
	Piece STICK, L1, L2, S1, S2, SQR, PYR;
	@Before
	public void my_setUp() throws Exception {
		my_board = new Board(10, 20);
		
		STICK = new Piece(Piece.STICK_STR);
		L1 = new Piece(Piece.L1_STR);
		L2 = new Piece(Piece.L2_STR);
		S1 = new Piece(Piece.S1_STR);
		S2 = new Piece(Piece.S2_STR);
		SQR = new Piece(Piece.SQUARE_STR);
		PYR = new Piece(Piece.PYRAMID_STR);
	}
	
	@Test
	public void test_placeOK() {
		assertEquals(Board.PLACE_OK,my_board.place(STICK, 0, 0));
		assertEquals(Board.PLACE_OK,my_board.place(SQR, 0, 4));
		assertEquals(6, my_board.getMaxHeight());
		assertEquals(1, my_board.getRowWidth(0));
	}
	
	@Test
	public void test_placeBad() {
		assertEquals(Board.PLACE_OK, my_board.place(L1, 0, 0));
		assertEquals(Board.PLACE_BAD, my_board.place(PYR, 0, 0));
		
		//The piece that place bad will be disappeared.
		assertEquals(3, my_board.getMaxHeight());
		assertEquals(2, my_board.getRowWidth(0));
	}
	
	@Test
	public void test_placeOutBound() {
		assertEquals(Board.PLACE_OUT_BOUNDS, my_board.place(S1, -1, 0));
		assertEquals(Board.PLACE_OUT_BOUNDS, my_board.place(S2, 11, 0));
		assertEquals(Board.PLACE_OUT_BOUNDS, my_board.place(S1, 5, -9));
		
		//The piece that place out bound will be disappeared.
		assertEquals(0, my_board.getMaxHeight());
	}
	
	@Test
	public void test_placeRowFilled_clearRow() {
		STICK = STICK.computeNextRotation();
		
		assertEquals(Board.PLACE_OK, my_board.place(STICK, 0, 0));
		assertEquals(Board.PLACE_OK, my_board.place(STICK, 4, 0));
		
		//Row 0 is full
		assertEquals(Board.PLACE_ROW_FILLED, my_board.place(SQR, 8, 0));
		
		assertEquals(1 ,my_board.clearRows());
	}
	
}
