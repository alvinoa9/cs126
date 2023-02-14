package com.example;
import java.lang.Math;
/**
 * Takes in and evaluates a string representing a tic tac toe board.
 */
public class TicTacToeBoard {
  private String board;
  private int n;

  /**
   * This method should load a string into your TicTacToeBoard class.
   * @param board The string representing the board
   * @throws IllegalArgumentException if board dimensions are not square
   */
  public TicTacToeBoard(String board) throws IllegalArgumentException{
    this.board = board;
    n = (int)Math.sqrt(board.length());
    if(board.length() != n*n) throw new IllegalArgumentException(); //Checks if board dimensions are correct
  }

  /**
   * Checks the state of the board (unreachable, no winner, X wins, or O wins)
   * @return an enum value corresponding to the board evaluation
   */
  public Evaluation evaluate() {
    //Board counter
    int x = 0, o = 0;

    this.board = this.board.toLowerCase();  //Lower case the board
    Character[] mBoard = new Character[n*n];  //Initialize new board matrix

    //Counts board
    for(int i = 0; i < this.board.length(); i++){
      mBoard[i] = this.board.charAt(i); //Put board into new matrix
      if('x' == this.board.charAt(i)) x++; //Counts number of 'x' in board
      else if('o' == this.board.charAt(i)) o++;  //Counts number of 'o' in board
    }

    //If the difference of the amount of x and o is greater than 1, return UnreachableState
    if((x-o) > 1) return Evaluation.UnreachableState;

    boolean result = false; //Sets outcome to false
    //Check vertical
    for(int i = 0; i < n; i++){ //Row loop
      for(int j = i + n; j < n*n; j = j + n){
        result = mBoard[i] == mBoard[j];
      }
      if((result) && (mBoard[i] == 'x')) return Evaluation.Xwins;
      if((result) && (mBoard[i] == 'o')) return Evaluation.Owins;
    }

    //Check horizontal
    for(int i = 0; i < n*n; i = i + n){ //Column loop
      for(int j = i + 1; j < i + n; j++){
        result = mBoard[i] == mBoard[j];
      }
      if((result) && (mBoard[i] == 'x')) return Evaluation.Xwins;
      if((result) && (mBoard[i] == 'o')) return Evaluation.Owins;
    }
    //Check diagonal
    //First condition
    int i;  //Start counter
    for(i = n; i < n*n; i = i + n){
      result = mBoard[0] == mBoard[i];
    }
    if((result) && (mBoard[n] == 'x')) return Evaluation.Xwins;
    if((result) && (mBoard[n] == 'o')) return Evaluation.Owins;

    //Second Condition
    for(i = n-1; i < n*(n-1)+1; i = i + n-1){
      result = mBoard[n-1] == mBoard[i];
    }
    if((result) && (mBoard[n-1] == 'x')) return Evaluation.Xwins;
    if((result) && (mBoard[n-1] == 'o')) return Evaluation.Owins;

    return Evaluation.NoWinner;
  }
}
