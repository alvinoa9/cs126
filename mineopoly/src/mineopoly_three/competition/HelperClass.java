package mineopoly_three.competition;

import mineopoly_three.action.TurnAction;
import mineopoly_three.strategy.PlayerBoardView;
import mineopoly_three.tiles.TileType;

import java.awt.*;

import static mineopoly_three.action.TurnAction.*;
import static mineopoly_three.util.DistanceUtil.getManhattanDistance;

public class HelperClass {
    private int boardSize;
    private int maxInventorySize;
    private int winningScore;
    private int maxCharge;
    private boolean isRedPlayer;
    private Point startTileLocation;
    private int currentCharge;
    private int numCharge;
    private boolean isRedTurn;
    private PlayerBoardView boardView;
    private TileType ore;
    private int inventorySize = 0;
    private int mineCount;
    private int count;
    private TileType market;
    private boolean needCharge;
    private Point targetPoint = null;
    private boolean mined = true;

    public HelperClass(int boardSize, int maxInventorySize, int maxCharge, int winningScore, Point startTileLocation, boolean isRedPlayer) {
        this.boardSize = boardSize;
        this.maxInventorySize = maxInventorySize;
        this.maxCharge = maxCharge;
        this.winningScore = winningScore;
        this.startTileLocation = startTileLocation;
        this.isRedPlayer = isRedPlayer;
        ore = TileType.RESOURCE_DIAMOND;
        mineCount = 3;
        count = 0;
    }

    public TurnAction getMoves() {
        int range = 0;
        boolean found = false;
        //System.out.println(inventorySize);
        Point market1 = startTileLocation;
        Point market2 = null;
        needCharge = false;
        if (isRedPlayer) {
            market2 = new Point(market1.x + boardSize/2, market1.y - boardSize/2);
            market = TileType.RED_MARKET;
        }
        else {
            market2 = new Point(market1.x - boardSize/2, market1.y - boardSize/2);
            market = TileType.BLUE_MARKET;
        }

        Point currentLocation = boardView.getYourLocation();
        Point nearCharge;
        if (boardView.getTileTypeAtLocation(currentLocation) == market) {
             inventorySize = 0;
             numCharge++;
        }
        if (boardView.getTileTypeAtLocation(currentLocation) == ore) {
            //System.out.println("MINE");
            count++;
            return MINE;
        }
        if (mineCount == count) {
            //System.out.println("pickup");
            count = 0;
            mined = true;
            return PICK_UP_RESOURCE;
        }
        if (currentLocation.x < boardSize/2 && currentLocation.y < boardSize/2) {
            nearCharge = new Point(boardSize/2-1, boardSize/2-1);
        }
        else if (currentLocation.x < boardSize && currentLocation.y < boardSize/2) {
            nearCharge = new Point(boardSize/2, boardSize/2-1);
        }
        else if (currentLocation.x < boardSize/2 && currentLocation.y < boardSize) {
            nearCharge = new Point(boardSize/2-1, boardSize/2);
        }
        else {
            nearCharge = new Point(boardSize/2, boardSize/2);
        }

        if (currentCharge <= boardSize) {
            //System.out.println("needcharge");
            needCharge = true;
            count = 0;
        }
        else {
            needCharge = false;
        }
        if (numCharge > 2 && numCharge < 5) {
            ore = TileType.RESOURCE_EMERALD;
            mineCount = 2;
            count = 0;
        }
        else if (numCharge >= 5) {
            ore = TileType.RESOURCE_RUBY;
            mineCount = 1;
            count = 0;
        }
        //System.out.println("works1");
        if (boardView.getTileTypeAtLocation(currentLocation) == TileType.RECHARGE && currentCharge != maxCharge) {
            //System.out.println("null");
            return null;
        }
        else {
            //System.out.println("works2");
            if (needCharge) {
                //System.out.println("works3");
                //System.out.println(nearCharge.x + ", " + nearCharge.y);
                //System.out.println(currentLocation.x + ", " + currentLocation.y);
                if (nearCharge.x - currentLocation.x  < 0) {
                    //System.out.println("left1");
                    return MOVE_LEFT;
                }
                else if (nearCharge.y - currentLocation.y  < 0) {
                    //System.out.println("down1");
                    return MOVE_DOWN;
                }
                else if (nearCharge.y - currentLocation.y  > 0) {
                    //System.out.println("up1");
                    return MOVE_UP;
                }
                else if (nearCharge.x - currentLocation.x  > 0) {
                    //System.out.println("right1");
                    return MOVE_RIGHT;
                }

            }
            else if (inventorySize == maxInventorySize) {
                //System.out.println("works4");
                if (getManhattanDistance(market1, currentLocation) < getManhattanDistance(market2, currentLocation)) {
                    //System.out.println("works5");
                    //System.out.println(market1.x + ", " + market1.y);
                    //System.out.println(market2.x + ", " + market2.y);
                    //System.out.println(currentLocation.x + ", " + currentLocation.y);
                    if (market1.x - currentLocation.x  < 0) {
                        //System.out.println("left3");
                        return MOVE_LEFT;
                    }
                    else if (market1.x - currentLocation.x  > 0) {
                        //System.out.println("right3");
                        return MOVE_RIGHT;
                    }
                    else if (market1.y - currentLocation.y  < 0) {
                        //System.out.println("down3");
                        return MOVE_DOWN;
                    }
                    else if (market1.y - currentLocation.y  > 0) {
                        //System.out.println("up3");
                        return MOVE_UP;
                    }
                }
                else {
                    //System.out.println("works6");
                    if (market2.x - currentLocation.x  < 0) {
                        //System.out.println("left4");
                        return MOVE_LEFT;
                    }
                    else if (market2.x - currentLocation.x  > 0) {
                        //System.out.println("right4");
                        return MOVE_RIGHT;
                    }
                    else if (market2.y - currentLocation.y  < 0) {
                        //System.out.println("down4");
                        return MOVE_DOWN;
                    }
                    else if (market2.y - currentLocation.y  > 0) {
                        //System.out.println("up4");
                        return MOVE_UP;
                    }
                }
            }
            else {
                //System.out.println("works7");
                if (mined) {
                    while (!found) {
                        //System.out.println("works8");
                        outer:
                        for (int i = currentLocation.x - (range); i <= currentLocation.x + (2 * range); i++) {
                            for (int j = currentLocation.y - (range); j <= currentLocation.x + (2 * range); j++) {
                                if (boardView.getTileTypeAtLocation(i, j) == ore) {
                                    targetPoint = new Point(i, j);
                                    mined = false;
                                    found = true;
                                    break outer;
                                }
                            }
                        }
                        range++;
                    }
                }
                //System.out.println("works9");
                range = 0;
                //System.out.println(targetPoint.x + ", " + targetPoint.y);
                //System.out.println(currentLocation.x + ", " + currentLocation.y);
                if ((targetPoint.x - currentLocation.x) < 0) {
                    //System.out.println("left2");
                    return MOVE_LEFT;
                }
                if ((targetPoint.y - currentLocation.y)  < 0) {
                    //System.out.println("down2");
                    return MOVE_DOWN;
                }
                if ((targetPoint.y - currentLocation.y)  > 0) {
                    //System.out.println("up2");
                    return MOVE_UP;
                }
                if ((targetPoint.x - currentLocation.x)  > 0) {
                    //System.out.println("right2");
                    return MOVE_RIGHT;
                }
            }
        }
        //System.out.println("works10");
        return null;
    }

    public void setCurrentCharge(int currentCharge) {
        this.currentCharge = currentCharge;
    }

    public void setRedTurn(boolean redTurn) {
        isRedTurn = redTurn;
    }

    public void setBoardView(PlayerBoardView boardView) {
        this.boardView = boardView;
    }

    public void addInventorySize() {
        this.inventorySize++;
    }
}
