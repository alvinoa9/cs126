#pragma once

#include <math.h>
#include <algorithm>

#include <string>
#include <vector>

using namespace std;

namespace tictactoe {

/**
 * This enumeration specifies the possible results of the evaluation of a
 * Tic-Tac-Toe board.
 */
enum class BoardState {
  NoWinner,
  Xwins,
  Owins,
  UnreachableState,
};

/**
 * This class can store a Tic-Tac-Toe board and evaluate its state.
 */
class Board {
 public:
  /**
   * Constructs a Board from a string consisting of 9 characters in row-major
   * order (i.e. the first three characters specify the first row).
   *
   * An 'X' or 'x' represents that the corresponding square is filled with an X,
   * an 'O' or 'o' represents that the corresponding square is filled with an O,
   * and any other character represents that the corresponding square is empty.
   *
   * This method throws a std::invalid_argument exception if the string provided
   * is not a valid board.
   */
  Board(const std::string& board);

  /**
   * Evaluates the state of the board.
   */
  BoardState EvaluateBoard() const;

 private:
  /// TODO: add your helper functions and member variables here
  int countPiece(const vector<char>&, char) const;
  void checkVertical(const vector<char>&, bool&, bool&) const;
  void checkHorizontal(const vector<char>&, bool&, bool&) const;
  void checkDiagonal(const vector<char>&, bool&, bool&) const;

  string board;
  int n;
};

}  // namespace tictactoe
