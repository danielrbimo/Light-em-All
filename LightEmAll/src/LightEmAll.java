import java.awt.*;

import java.util.ArrayList;
import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import javalib.worldimages.StarImage;
import java.util.HashMap;
import java.util.Random;

class LightEmAll extends World {
  public final static int SIZE = 50;
  // a list of columns of GamePieces,
  // i.e., represents the board in column-major order
  ArrayList<ArrayList<GamePiece>> board;
  // a list of all nodes
  ArrayList<GamePiece> nodes;
  // a list of edges of the minimum spanning tree
  ArrayList<Edge> mst;
  ArrayList<Edge> walls;
  // the width and height of the board
  int width;
  int height;
  // the current location of the power station,
  // as well as its effective radius
  int powerRow;
  int powerCol;
  int radius;
  Random rand;
  ArrayList<Edge> sT = new ArrayList<Edge>();

  LightEmAll(int width, int height) {
    init(width,height,new Random());
            //this.rand = new Random();
            //this.width = width;
            //this.height = height;
            //this.board = new ArrayList<ArrayList<GamePiece>>();
            //this.powerCol = 0;
            //this.powerRow = 0;
            //this.radius = 0;
            //this.makeBasicBoard();
            //this.nodes = this.convertToNodes(board);
            //this.mst = this.gatherEdges(nodes);
            //this.nodes = this.kruskalAlgo(nodes);
            //walls = genWalls(nodes,mst);
  }

  // initialize the value 
  void init(int width, int height,Random rand) {
    this.board = new ArrayList<ArrayList<GamePiece>>();
    this.makeBasicBoard();
    this.width = width;
    this.height = height;
    this.rand = rand;

    nodes = convertToNodes(board);
    ArrayList<GamePiece> initVert = this.initialV();
    this.mst = this.gatherEdges(initVert);
    initVert = this.kruskalAlgo(initVert);
    walls = this.genWalls(initVert, this.mst);
    this.nodes = initVert;


    nodes.get(0).powerStation = true;
    linkKrusk();
  }

  ArrayList<GamePiece> convertToNodes(ArrayList<ArrayList<GamePiece>> al) {
    ArrayList<GamePiece> temp = new ArrayList<>();
    for (ArrayList<GamePiece> ag : al) {
      for (GamePiece g : ag) {
        temp.add(g);
      }
    }
    return temp;
  }

  //link the edge by krukal algorithm 
  void linkKrusk() {
    for (Edge e : walls) {
      if (e.to.col - e.from.col == 1) {
        e.from.right = true;
        e.to.left = true;
      }
      if (e.to.col - e.from.col == -1) {
        e.from.left = true;
        e.to.right = true;
      }
      if (e.to.row - e.from.row == 1) {
        e.from.bottom = true;
        e.to.top = true;
      }
      if (e.to.row - e.from.row == -1) {
        e.from.top = true;
        e.to.bottom = true;
      }
    }
  }

  ArrayList<GamePiece> initialV() {
    ArrayList<GamePiece> alv = nodes;
    for (int x = 0; x < this.width; x++) {
      for (int y = 0; y < this.height; y++) {
        alv.add(new GamePiece(x,y,false,false,false,false,false));
      }
    }

    for (GamePiece v : alv) {
      //not at first column
      if (v.col != 0) {
        v.edges.add(new Edge(v, alv.get((v.col - 1) * this.height + v.row),
            this.rand.nextInt(1000)));
      }
      //      //not at last column
      //            if (v.col != this.width - 1) {
      //              v.edges.add(new Edge(v, alv.get((v.col + 1) * this.height + v.row),
      //                  this.rand.nextInt(1000)));
      //            }
      //      //not at first row
      //            if (v.row != 0) {
      //              v.edges.add(new Edge(v, alv.get((v.row - 1) + v.col * this.height),
      //                  this.rand.nextInt(1000)));
      //            }
      //not at last row
      if (v.row != this.height - 1) {
        v.edges.add(new Edge(v, alv.get((v.row + 1) + v.col * this.height),
            this.rand.nextInt(1000)));
      }
    }
    return alv;
  }

  //gather the edges of the game piece
  ArrayList<Edge> gatherEdges(ArrayList<GamePiece> v) {
    ArrayList<Edge> ale = new ArrayList<Edge>();
    for (GamePiece vx : v) {
      for (Edge e : vx.edges) {
        ale.add(e);
      }
    }
    return ale;
  }

  //link the subdivision board 
  void linkSubBoard(ArrayList<ArrayList<GamePiece>> al, int rBeg, int rEnd, int cBeg, int cEnd) {
    int oRBeg = rBeg;
    int oREnd = rEnd;
    int oCBeg = cBeg;
    int oCEnd = cEnd;
    int boardSize = al.size() * al.size();
    if (boardSize == 4) {
      linkSidesBottom(al,rBeg, rEnd - 1, cBeg, cEnd - 1);
    }
    else {
      //int temp = boardSize / 4;
      ArrayList<ArrayList<GamePiece>> f1 = new ArrayList<ArrayList<GamePiece>>();
      ArrayList<ArrayList<GamePiece>> f2 = new ArrayList<ArrayList<GamePiece>>();
      ArrayList<ArrayList<GamePiece>> f3 = new ArrayList<ArrayList<GamePiece>>();
      ArrayList<ArrayList<GamePiece>> f4 = new ArrayList<ArrayList<GamePiece>>();

      rEnd = (oREnd + oRBeg) / 2;
      cEnd = (oCEnd + oCBeg) / 2;
      getSub(f1, rBeg, rEnd, cBeg, cEnd);
      linkSubBoard(f1,rBeg, rEnd, cBeg, cEnd);
      linkSidesBottom(f1,rBeg, rEnd - 1, cBeg, cEnd - 1);
      //      int cTBeg = cEnd / 2;
      //      int cTEnd = width;
      //      cBeg = cTBeg;
      //      cEnd = cTEnd;
      cBeg = cEnd;
      cEnd = oCEnd;
      System.out.println(cBeg);
      System.out.println(cEnd);
      getSub(f2, rBeg, rEnd,cBeg,cEnd);
      System.out.println(f2.size());
      System.out.println(f2.get(0).size());
      System.out.println(f2.get(0).get(0).col);
      System.out.println(f2.get(0).get(1).col);
      System.out.println(f2.get(1).get(0).col);
      System.out.println(f2.get(1).get(1).col);
      linkSubBoard(f2,rBeg, rEnd , cBeg, cEnd);
      linkSidesBottom(f2,rBeg, rEnd - 1, cBeg, cEnd - 1);
      int rTBeg = rEnd + 1;
      System.out.println(rTBeg);
      int rTEnd = height - 1;
      System.out.println(rTEnd);
      System.out.println(cBeg);
      System.out.println(cEnd);
      rBeg = rEnd;
      rEnd = oREnd;
      getSub(f3, rBeg, rEnd,cBeg,cEnd);
      System.out.println(f3.size());
      System.out.println(f3.get(0).size());
      System.out.println(f3.get(0).get(0).row);
      System.out.println(f3.get(0).get(1).row);
      System.out.println(f3.get(1).get(0).row);
      System.out.println(f3.get(1).get(1).row);
      linkSubBoard(f3,rBeg, rEnd, cBeg, cEnd);
      linkSidesBottom(f3,rBeg,rEnd - 1,cBeg,cEnd - 1);
      cBeg = oCBeg;
      cEnd = (cEnd + cBeg) / 2;
      rBeg = (oREnd + oRBeg) / 2;
      rEnd = oREnd;
      getSub(f4, rBeg, rEnd,cBeg,cEnd);
      linkSubBoard(f4,rBeg, rEnd,cBeg,cEnd);
      linkSidesBottom(f4,rBeg, rEnd - 1, cBeg, cEnd - 1);
    }
  }

  //link the side and bottom
  void linkSidesBottom(ArrayList<ArrayList<GamePiece>> al,
      int rBeg, int rEnd, int cBeg, int cEnd) {
    for (ArrayList<GamePiece> a : al) {
      for (GamePiece g : a) {
        if (g.col == cBeg || g.col == cEnd) {
          if (g.row < rEnd && g.row > rBeg) {
            g.bottom = true;
            g.top = true;
          }
        }
        if (g.row == rEnd) {
          //not first and last row
          if (g.col < (cEnd) && g.col != cBeg) {
            g.left = true;
            g.right = true;
          }
          //the last row
          else if (g.col == cEnd) {
            g.left = true;
          }
          //the first row
          else if (g.col == cBeg) {
            g.right = true;
          }
        }
        if (g.row == rBeg && g.col == cBeg) {
          g.bottom = true;
        }
        if (g.row == rBeg && g.col == cEnd) {
          g.bottom = true;
        }
        if (g.row == rEnd && g.col == cBeg) {
          g.top = true;
        }
        if (g.row == rEnd && g.col == cEnd) {
          g.top = true;
        }
      }
    }
  }

  //link the base case
  void linkBase() {
    for (ArrayList<GamePiece> al : this.board) {
      for (GamePiece g : al) {
        if (g.row == 1) {
          if (g.col == 0) {
            g.right = true;
          }
        }
        if (g.col > 0 && g.col <= 1) {
          if (g.row == 0) {
            g.bottom = true;
          }
        }
        if (g.col > 0 && g.col <= 1) {
          if (g.row > 0 && g.row <= 1) {
            g.top = true;
            g.left = true;
          }
        }
        if (g.col == this.width - 2) {
          if (g.row == 1) {
            g.top = true;
            g.right = true;
          }
        }
        if (g.col == this.width - 1) {
          if (g.row == 1) {
            g.left = true;
          }
        }
        if (g.col == this.width - 2) {
          if (g.row == 0) {
            g.bottom = true;
          }
        }
        if (g.col == 1) {
          if (g.row == this.height - 1) {
            g.top = true;
          }
        }
        if (g.col == 1) {
          if (g.row == this.height - 2) {
            g.bottom = true;
          }
        }
        if (g.col == this.width - 2) {
          if (g.row == this.height - 1) {
            g.top = true;
          }
        }
        if (g.col == this.width - 2) {
          if (g.row == this.height - 2) {
            g.bottom = true;
          }
        }
      }
    }
  }

  //link the rows
  void linkRows() {
    for (ArrayList<GamePiece> al : this.board) {
      for (GamePiece g : al) {
        //not first and last row
        if (g.row < (this.height - 1) && g.row > 0) {
          g.bottom = true;
          g.top = true;
        }
        //the last row
        else if (g.row == this.height - 1) {
          g.top = true;
        }
        //the first row
        else {
          g.bottom = true;
        }
      }
    }
  }

  //link the coloumn
  void linkColoumn() {
    for (ArrayList<GamePiece> al : this.board) {
      for (GamePiece g : al) {
        if (g.row == Math.round(height / 2)) {
          //not first and last row
          if (g.col < (this.width - 1) && g.col != 0) {
            g.left = true;
            g.right = true;
          }
          //the last row
          else if (g.col == this.width - 1) {
            g.left = true;
          }
          //the first row
          else {
            g.right = true;
          }
        }
      }
    }
  }

  //sub division of the board
  void getSub(ArrayList<ArrayList<GamePiece>> al,
      int rBeg, int rEnd, int cBeg, int cEnd) {
    for (int r = rBeg; r <= rEnd - 1; r++) {
      al.add(new ArrayList<GamePiece>());
      for (int c = cBeg; c <= cEnd - 1 ; c++) {
        al.get(r - rBeg).add(board.get(r).get(c));
      }
    }
  }

  //generate the walls for each game piece
  ArrayList<Edge> genWalls(ArrayList<GamePiece> v, ArrayList<Edge> e) {
    ArrayList<Edge> ale = new ArrayList<Edge>();
    for (Edge ed : e) {
      boolean cond = true;
      for (GamePiece vt : v) {
        for (Edge e2 : vt.edges) {
          if ((ed.to == e2.from && ed.from == e2.to)
              || ed.equals(e2)) {
            cond = false;
          }
        }
      }
      if (cond) {
        ale.add(ed);
      }
    }
    return ale;
  }

  //the kruskal algorithem 
  ArrayList<GamePiece> kruskalAlgo(ArrayList<GamePiece> v) {
    HashMap<Integer,Integer> rep = new HashMap<Integer,Integer>();
    ArrayList<Edge> ale = gatherEdges(v);
    ArrayList<Edge> spanTree = new ArrayList<Edge>();
    ArrayList<Edge> worklist = ale;
    this.mergesort(worklist);
    this.sT = spanTree;
    for (GamePiece vert : v) {
      vert.edges = new ArrayList<Edge>();
    }

    for (int i = 0; i <= (this.height * 10000) + this.width; i++) {
      rep.put(i, i);
    }

    while (spanTree.size() < v.size() - 1) {
      Edge next = worklist.get(0);
      if (find(rep, next.from.getID()) == find(rep, next.to.getID())) {
        worklist.remove(next);
      }
      else {
        spanTree.add(next);
        next.from.edges.add(next);
        next.to.edges.add(next);
        union(rep,
            find(rep, next.from.getID()),
            find(rep, next.to.getID()));
      }
    }
    return v;
  }

  //helper union method 
  void union(HashMap<Integer,Integer> rep, Integer v1, Integer v2) {
    rep.remove(v1);
    rep.put(v1, v2);
  }

  //helper find method 
  int find(HashMap<Integer, Integer> rep, int x) {
    if (rep.get(x) == x) {
      return x;
    }
    else {
      return find(rep, rep.get(x));
    }
  }

  //merge sort the arraylist 
  void mergesort(ArrayList<Edge> arr) {
    ArrayList<Edge> temp = new ArrayList<Edge>();
    for (int i = 0; i < arr.size(); i++) {
      temp.add(arr.get(i));
    }
    mergeSortHelp(arr,temp,0,arr.size());
  }

  //merge sort helper 
  void mergeSortHelp(ArrayList<Edge> source, ArrayList<Edge> temp, int loidx, int highidx) {
    if (highidx - loidx <= 1) {
      return;
    }
    int middleidx = (loidx + highidx) / 2;
    mergeSortHelp(source, temp, loidx, middleidx);
    mergeSortHelp(source, temp, middleidx, highidx);
    merge(source, temp, loidx, middleidx, highidx);
  }

  // Merges the two sorted regions [loIdx, midIdx) and [midIdx, hiIdx) from source
  // into a single sorted region according to the given comparator
  // EFFECT: modifies the region [loIdx, hiIdx) in both source and temp
  void merge(ArrayList<Edge> source, ArrayList<Edge> temp,
      int loIdx, int midIdx, int hiIdx) {
    int curLo = loIdx;   // where to start looking in the lower half-list
    int curHi = midIdx;  // where to start looking in the upper half-list
    int curCopy = loIdx; // where to start copying into the temp storage
    while (curLo < midIdx && curHi < hiIdx) {
      if (source.get(curLo).weight < source.get(curHi).weight) {
        // the value at curLo is smaller, so it comes first
        temp.set(curCopy, source.get(curLo));
        curLo = curLo + 1; // advance the lower index
      }
      else {
        // the value at curHi is smaller, so it comes first
        temp.set(curCopy, source.get(curHi));
        curHi = curHi + 1; // advance the upper index
      }
      curCopy = curCopy + 1; // advance the copying index
    }
    // copy everything that's left -- at most one of the two half-lists still has items in it
    while (curLo < midIdx) {
      temp.set(curCopy, source.get(curLo));
      curLo = curLo + 1;
      curCopy = curCopy + 1;
    }
    while (curHi < hiIdx) {
      temp.set(curCopy, source.get(curHi));
      curHi = curHi + 1;
      curCopy = curCopy + 1;
    }
    // copy everything back from temp into source
    for (int i = loIdx; i < hiIdx; i = i + 1) {
      source.set(i, temp.get(i));
    }
  }

  //if the wire is not connected, the color of the wire stay gray
  //if the wire is connected, change it to yellow
  Color colorOpt(GamePiece v) {
    if (v.col == 0 && v.row == 0) {
      return Color.yellow;
    }
    else if (v.row == (this.height - 1) && v.col == (this.width - 1)) {
      return Color.YELLOW;
    }
    else {
      return Color.gray;
    }
  }

  //draw the world
  public WorldScene makeScene() {
    WorldScene w = new WorldScene(this.width * SIZE, this.height * SIZE);
    for (GamePiece g : nodes) {
      w.placeImageXY(g.drawGamePiece(), ( g.col * SIZE) + (SIZE * 1 / 2), (g.row * SIZE) +
          (SIZE * 1 / 2));
      w.placeImageXY(new RectangleImage(1,50, OutlineMode.SOLID, Color.BLACK),
          g.col * SIZE,(g.row * SIZE) +
          (SIZE * 1 / 2));
      w.placeImageXY(new RectangleImage(50, 1, OutlineMode.SOLID, Color.BLACK),
          (g.col * SIZE) + (SIZE * 1 / 2), g.row * SIZE );

      if (g.top) {
        w.placeImageXY(new RectangleImage(1, SIZE / 2, OutlineMode.SOLID, Color.YELLOW),
            (g.col * SIZE) + (SIZE * 1 / 2), (g.row * SIZE) + (SIZE * 1 / 2) / 2);
      }
      if (g.bottom) {
        w.placeImageXY(new RectangleImage(1, SIZE / 2, OutlineMode.SOLID, Color.YELLOW),
            (g.col * SIZE) + (SIZE * 1 / 2),
            (g.row * SIZE) + (SIZE * 1 / 2) + (SIZE * 1 / 2) / 2);
      }
      if (g.left) {
        w.placeImageXY(new RectangleImage(SIZE / 2, 1, OutlineMode.SOLID, Color.YELLOW),
            (g.col * SIZE) + (SIZE * 1 / 2) / 2,
            (g.row * SIZE) + (SIZE * 1 / 2));
      }
      if (g.right) {
        w.placeImageXY(new RectangleImage(SIZE / 2, 1, OutlineMode.SOLID, Color.YELLOW),
            (g.col * SIZE) + (SIZE * 1 / 2) + (SIZE * 1 / 2) / 2,
            (g.row * SIZE) + (SIZE * 1 / 2));
      }
      if (g.powerStation) {
        w.placeImageXY(new StarImage(25.0, OutlineMode.SOLID, Color.BLUE),
            (g.col * SIZE) + (SIZE * 1 / 2), (g.row * SIZE) + (SIZE * 1 / 2));
      }
    }
    return w;
  }

  //to make the row and columns
  void makeBasicBoard() {
    for (int y = 0; y < this.height ; y++) {
      ArrayList<GamePiece> temp = new ArrayList<>();
      for (int x = 0; x < this.width; x++) {
        temp.add(new GamePiece(x, y, false, false, false, false, false));
      }
      this.board.add(temp);
    }
  }

  //put the power station on the GamePiece
  void addPowerStation() {
    int midV = 0;
    int midH = 0;
    if (height % 2 == 0) {
      midV = (height / 2) + 1 - 1;
    }
    else if ((height % 2) > 0) {
      midV = (height + 1) / 2 - 1;
    }
    if (width % 2 == 0) {
      midH = (width / 2) + 1 - 1;
    }
    else if ((width % 2) > 0) {
      midH = (width + 1) / 2 - 1;
    }
    for (ArrayList<GamePiece> al : this.board) {
      for (GamePiece g : al) {
        if (g.row == midV && g.col == midH) {
          g.powerStation = true;
        }
      }
    }
  }

  //rotates a GamePiece
  public void onMouseClicked(Posn pos, String s) {
    for (int j = 0; j < height * width; j++) {
      GamePiece currCell = nodes.get(j);
      if ((pos.x <= ((currCell.col * SIZE) + 45)
          && pos.x >= (currCell.col * SIZE + 15)
          && pos.y <= ((currCell.row * SIZE) + 45)
          && pos.y >= (currCell.row * SIZE + 15))
          && s.equals("LeftButton")) {
        currCell.rotate();
      }
    }
  }

  //on key event
  public void onKeyEvent(String key) {
    for (GamePiece g : nodes) {
      if (key.equals("left")
          && g.powerStation && g.left) {
        g.powerStation = false;
        nodes.get(nodes.indexOf(g) - height).powerStation = true;
        return;
      }
      else if (key.equals("down")
          && g.bottom && g.powerStation) {
        g.powerStation = false;

        nodes.get(nodes.indexOf(g) + 1 ).powerStation = true;
        return;
      }
      else if (key.equals("up")
          && g.top && g.powerStation) {
        g.powerStation = false;
        nodes.get(nodes.indexOf(g) - 1 ).powerStation = true;
        return;
      }
      else if (key.equals("right")
          && g.powerStation && g.right) {
        g.powerStation = false;
        nodes.get(nodes.indexOf(g) + height ).powerStation = true;
        return;
      }
    }
  }
}
