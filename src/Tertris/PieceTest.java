package Tertris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
 Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated;

	@Before
	public void setUp() throws Exception {

		pyr1 = Piece.getPieces()[6];
		pyr2 = pyr1.fastRotation();
		pyr3 = pyr2.fastRotation();
		pyr4 = pyr3.fastRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
	}

	// Here are some sample tests to get you started

	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());

		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());

		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
	}

	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not
		// work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] { 0, 0, 0 }, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] { 1, 0, 1 }, pyr3.getSkirt()));

		assertTrue(Arrays.equals(new int[] { 0, 0, 1 }, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] { 1, 0 }, sRotated.getSkirt()));
	}
	
	//--------->>>> MY TEST <<<<<---------
	@Test
	public void testEqual() {
		assertEquals(true, pyr1.equals(Piece.getPieces()[6]));
		assertEquals(true, pyr2.equals(pyr1.computeNextRotation()));
		assertEquals(true, pyr3.equals(pyr2.computeNextRotation()));
		assertEquals(true, pyr4.equals(pyr3.computeNextRotation()));
		
		assertEquals(true, s.equals(sRotated));
	}
}
