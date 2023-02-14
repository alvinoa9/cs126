#include "tictactoe/tictactoe.h"

#include <stdexcept>
#include <iostream>
using namespace std;

namespace tictactoe {

using std::string;

/**
 * This method should load a string into your TicTacToeBoard class.
 * @param board The string representing the board
 * @throws IllegalArgumentException if board dimensions are not square
 */
Board::Board(const string& board) {
  this->board = board;
  this->n = static_cast<int>(sqrt(board.length()));
  if (board.length() != n*n || n == 0) {
    throw invalid_argument("The string provided is not a valid board.");
  }
}

/**
 * Checks the state of the board (unreachable, no winner, X wins, or O wins)
 * @return an enum value corresponding to the board evaluation
 */
BoardState Board::EvaluateBoard() const {
  //Win state
  bool xwin = false, owin = false;

  //Initialize new board as vector
  vector<char> newBoard;

  //Puts boards data into new vector board while lower case
  for (unsigned i = 0; i < this->board.length(); i++) {
    newBoard.push_back(tolower(this->board.at(i)));
  }

  //Board counter
  int x_ = countPiece(newBoard, 'x');
  int o_ = countPiece(newBoard, 'o');

  //If the difference of the amount of x and o is greater than 1, return UnreachableState
  if(x_ < o_ || x_-o_ > 1) return BoardState::UnreachableState;

  //Run checks
  checkVertical(newBoard, xwin, owin);
  checkHorizontal(newBoard, xwin, owin);
  checkDiagonal(newBoard, xwin, owin);

  //Determine board state
  if (o_ + 1 == x_ && owin && !xwin) {
    return BoardState::UnreachableState;
  }
  if (xwin && owin) {
    return BoardState::UnreachableState;
  }
  if (xwin && !owin) {
    return BoardState::Xwins;
  }
  if (!xwin && owin) {
    return BoardState::Owins;
  }

  return BoardState::NoWinner;
}

/**
 * Count number of characters in the board
 * @param newBoard the game board
 * @param c assigned character 'x' or 'o'
 * @return int number of character in the board
 */
int Board::countPiece(const vector<char> &newBoard, char c) const {
  int count = 0;

  for (unsigned i = 0; i < newBoard.size(); i++) {
    if(c == newBoard[i]) {
      count++;
    }
  }

  return count;
}

/**
 * Checks if win by vertical
 * @param newBoard the game board
 * @param xwin state of x win
 * @param owin state of o win
 */
void Board::checkVertical(const vector<char> &newBoard, bool &xwin, bool &owin) const {
  for(int i = 0; i < n; i++){ //Row loop
    bool result = true;
    for(int j = i + n; j < n*n; j = j + n){
      result = result && newBoard[i] == newBoard[j];
    }

    if((result) && (newBoard[i] == 'x')) {
      xwin = true;
    }
    if((result) && (newBoard[i] == 'o')) {
      owin = true;
    }
  }
}

/**
 * Checks if win by horizontal
 * @param newBoard the game board
 * @param xwin state of x win
 * @param owin state of o win
 */
void Board::checkHorizontal(const vector<char> &newBoard, bool &xwin, bool &owin) const {
  for(int i = 0; i < n*n; i = i + n){ //Row loop
    bool result = true;
    for(int j = i; j < i + n; j++){
      result = result && newBoard[i] == newBoard[j];
    }

    if((result) && (newBoard[i] == 'x')) {
      xwin = true;
    }
    if((result) && (newBoard[i] == 'o')) {
      owin = true;
    }
  }
}

/**
 * Checks if win by diagonal
 * @param newBoard the game board
 * @param xwin state of x win
 * @param owin state of o win
 */
void Board::checkDiagonal(const vector<char> &newBoard, bool &xwin, bool &owin) const {
  bool result = true;

  //Start counter
  int i;

  //First condition
  for(i = 0; i < n*n; i = i + n + 1){
    result = result && (newBoard[0] == newBoard[i]);
  }

  if(result && (newBoard[0] == 'x')) {
    xwin = true;
  }
  if(result && (newBoard[0] == 'o')) {
    owin = true;
  }

  result = true;

  for(i = n-1; i < n*n - 1; i = i + n-1){
    result = result && (newBoard[n-1] == newBoard[i]);
  }

  if((result) && (newBoard[n-1] == 'x')) {
    xwin = true;
  }
  if((result) && (newBoard[n-1] == 'o')) {
    owin = true;
  }
}

}  // namespace tictactoe
