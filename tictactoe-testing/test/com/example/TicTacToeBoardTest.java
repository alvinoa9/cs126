package com.example;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TicTacToeBoardTest {
  @Test
  public void testValidBoardNoWinner() {
    TicTacToeBoard board = new TicTacToeBoard("O...X.X..");
    assertEquals(Evaluation.NoWinner, board.evaluate());
  }
  @Test
  public void testValidBoardXwins() {
    TicTacToeBoard board = new TicTacToeBoard("O.X.XoX..");
    assertEquals(Evaluation.Xwins, board.evaluate());
  }
  @Test
  public void testValidBoardOwins() {
    TicTacToeBoard board = new TicTacToeBoard("OOO.X.X..");
    assertEquals(Evaluation.Owins, board.evaluate());
  }
  @Test
  public void testUnreachableState()  {
    TicTacToeBoard board = new TicTacToeBoard("O...X.XXX");
    assertEquals(Evaluation.UnreachableState, board.evaluate());
  }
}
