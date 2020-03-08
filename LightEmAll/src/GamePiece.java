import java.awt.*;
import java.util.ArrayList;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

class GamePiece {
  // in logical coordinates, with the origin
  // at the top-left corner of the screen
  int row;
  int col;
  // whether this GamePiece is connected to the
  // adjacent left, right, top, or bottom pieces
  boolean left;
  boolean right;
  boolean top;
  boolean bottom;
  // whether the power station is on this piece
  boolean powerStation;
  ArrayList<Edge> edges;

  GamePiece(
      int col, 
      int row, 
      boolean left, 
      boolean right, 
      boolean top, 
      boolean bottom, 
      boolean powerStation) {
    this.row = row;
    this.col = col;
    this.left = left;
    this.right = right;
    this.top = top;
    this.bottom = bottom;
    this.powerStation = powerStation;
    this.edges = new ArrayList<>();
  }

  //draw the game piece image 
  WorldImage drawGamePiece() {
    return new RectangleImage(50,50, OutlineMode.SOLID, Color.GRAY);
  }

  //rotate the wires 
  void rotate() {
    boolean temp = this.bottom;
    bottom = left;
    left = top;
    top = right;
    right = temp;
  }

  //check if the number is the power of 2
  boolean isPowerOfTwo(int n) {
    if (n == 0) {
      return false;
    }
    else {
      return (int) 
          (Math.ceil(Math.log(n) / Math.log(2))) == (int) 
          (Math.floor(Math.log(n) / Math.log(2)));
    }
  }

  //get the current Index 
  int getID() {
    return col + 100 * row;
  }
}
