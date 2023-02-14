package mineopoly_three.competition;

import mineopoly_three.action.TurnAction;
import mineopoly_three.game.Economy;
import mineopoly_three.item.InventoryItem;
import mineopoly_three.strategy.MinePlayerStrategy;
import mineopoly_three.strategy.PlayerBoardView;

import java.awt.*;
import java.util.Random;

public class FirstPlayerStrategy implements MinePlayerStrategy {
    HelperClass info;

    @Override
    public void initialize(int boardSize, int maxInventorySize, int maxCharge, int winningScore, PlayerBoardView startingBoard, Point startTileLocation, boolean isRedPlayer, Random random) {
        info = new HelperClass(boardSize, maxInventorySize, maxCharge, winningScore, startTileLocation, isRedPlayer);
    }

    @Override
    public TurnAction getTurnAction(PlayerBoardView boardView, Economy economy, int currentCharge, boolean isRedTurn) {
        info.setCurrentCharge(currentCharge);
        info.setRedTurn(isRedTurn);
        info.setBoardView(boardView);
        return info.getMoves();
    }

    @Override
    public void onReceiveItem(InventoryItem itemReceived) {
        info.addInventorySize();
    }

    @Override
    public void onSoldInventory(int totalSellPrice) {

    }

    @Override
    public String getName() {
        return "Red Player";
    }

    @Override
    public void endRound(int pointsScored, int opponentPointsScored) {

    }
}
