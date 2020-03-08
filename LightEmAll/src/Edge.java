import java.awt.*;

import javalib.worldimages.OutlineMode;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.WorldImage;

class Edge {
  GamePiece from;
  GamePiece to;
  int weight;

  Edge(GamePiece from, GamePiece to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  WorldImage drawEdgeV() {
    return new RectangleImage(LightEmAll.SIZE / LightEmAll.SIZE, LightEmAll.SIZE,
            OutlineMode.SOLID, Color.black);
  }

  WorldImage drawEdgeH() {
    return new RectangleImage(LightEmAll.SIZE, LightEmAll.SIZE / LightEmAll.SIZE,
            OutlineMode.SOLID, Color.black);
  }
}