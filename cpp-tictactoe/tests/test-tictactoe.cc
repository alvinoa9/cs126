#include <string>

#include <catch2/catch.hpp>
#include <tictactoe/tictactoe.h>

using tictactoe::Board;
using tictactoe::BoardState;

TEST_CASE("Check character sensitivity of input string provided to constructor") {
  SECTION("Check upper case") {
    REQUIRE(Board("XXXOOXOXO").EvaluateBoard() == BoardState::Xwins);
  }
  SECTION("Check lower case") {
    REQUIRE(Board("xxxooxoxo").EvaluateBoard() == BoardState::Xwins);
  }
  SECTION("Check mix upper and lower case") {
    REQUIRE(Board("xXxoOxOxo").EvaluateBoard() == BoardState::Xwins);
  }
  SECTION("Check other characters") {
    REQUIRE(Board("ooo.x.xx.").EvaluateBoard() == BoardState::Owins);
  }
}

TEST_CASE("Invalid string provided to constructor") {
  SECTION("String is too short (8 character)") {
    REQUIRE_THROWS_AS(Board("xxoooxox"), std::invalid_argument);
  }

  SECTION("String is too long (10 character)") {
    REQUIRE_THROWS_AS(Board("xxoooxoxox"), std::invalid_argument);
  }

  SECTION("String is empty") {
    REQUIRE_THROWS_AS(Board(""), std::invalid_argument);
  }
}

TEST_CASE("Boards with no winner") {
  SECTION("Full board with no winner") {
    REQUIRE(Board("xxoooxxxo").EvaluateBoard() == BoardState::NoWinner);
  }

  SECTION("Empty board with no winner") {
    REQUIRE(Board(".........").EvaluateBoard() == BoardState::NoWinner);
  }

  SECTION("Game in progress, X moved last") {
    REQUIRE(Board("xxoooxx..").EvaluateBoard() == BoardState::NoWinner);
  }

  SECTION("Game in progress, O moved last") {
    REQUIRE(Board("xxooox.xo").EvaluateBoard() == BoardState::NoWinner);
  }
}

TEST_CASE("O wins") {
  SECTION("O wins by horizontal") {
    REQUIRE(Board("ooo.x.xx.").EvaluateBoard() == BoardState::Owins);
  }
  SECTION("O wins by vertical") {
    REQUIRE(Board("o..ox.oxx").EvaluateBoard() == BoardState::Owins);
  }
  SECTION("O wins by first diagonal") {
    REQUIRE(Board("o...oxxxo").EvaluateBoard() == BoardState::Owins);
  }
  SECTION("O wins by second diagonal") {
    REQUIRE(Board("x.oxoxo..").EvaluateBoard() == BoardState::Owins);
  }
}

TEST_CASE("X wins") {
  SECTION("X wins by horizontal") {
    REQUIRE(Board("xxxooxoxo").EvaluateBoard() == BoardState::Xwins);
  }

  SECTION("X wins by vertical") {
    REQUIRE(Board(".xoox..x.").EvaluateBoard() == BoardState::Xwins);
  }

  SECTION("X wins by first diagonal") {
    REQUIRE(Board("x...xoo.x").EvaluateBoard() == BoardState::Xwins);
  }

  SECTION("X wins by second diagonal") {
    REQUIRE(Board("..xoxox..").EvaluateBoard() == BoardState::Xwins);
  }

  SECTION("X has two three-in-a-rows") {
    REQUIRE(Board("xxxxooxoo").EvaluateBoard() == BoardState::Xwins);
  }
}

TEST_CASE("Board with Unreachable State") {
  SECTION("Number of X is less than number of O") {
    REQUIRE(Board("ooxox....").EvaluateBoard() == BoardState::UnreachableState);
  }

  SECTION("Difference between number of X and number of O is more than 1") {
    REQUIRE(Board("xxoxxo...").EvaluateBoard() == BoardState::UnreachableState);
  }

  SECTION("Number of X is equal to number of O when X has three-in-a-row") {
    REQUIRE(Board("ooxox....").EvaluateBoard() == BoardState::UnreachableState);
  }

  SECTION("Number of X is equal to number of O + 1 when O has three-in-a-row") {
    REQUIRE(Board("oooxx.xx.").EvaluateBoard() == BoardState::UnreachableState);
  }
}