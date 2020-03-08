import tester.Tester;
import java.util.ArrayList;
import java.util.HashMap;

//The example class
class Example {
  GamePiece g1;
  GamePiece g2;
  GamePiece g3;

  //initialize 
  void initVals() {
    g1 = new GamePiece(0, 0, false, false, false, true, false);
    g2 = new GamePiece(4, 0, false, true, true, true, false);
    g3 = new GamePiece(1, 0, false, false, true, true, false);

  }

  //rendering the game 
  void testBigBang(Tester t) {
    LightEmAll w = new LightEmAll(16, 16);
    w.addPowerStation();
    w.onKeyEvent("key");
    w.bigBang(w.width * LightEmAll.SIZE, w.height * LightEmAll.SIZE , 0.5);
  }

  //test the rotate method 
  void testRotate(Tester t) {
    this.initVals();
    g1.rotate();
    t.checkExpect(g1.left, false);
    g2.rotate();
    t.checkExpect(g2.left && g2.right && g2.bottom, false);
    g3.rotate();
    t.checkExpect(g3.left && g3.right, true);
  }

  //test the power method 
  void testpower(Tester t) {
    this.initVals();
    t.checkExpect(g1.isPowerOfTwo(10), false);
  }

  //test the kruskalAlgo method 
  void testKruskalAlgo(Tester t) {
    this.initVals();
    ArrayList<GamePiece> a1 = new ArrayList<GamePiece>();
    LightEmAll w = new LightEmAll(16, 16);
    t.checkExpect(w.kruskalAlgo(a1), a1);
  }

  //test the union method 
  void testUnion(Tester t) {
    this.initVals();
    LightEmAll w = new LightEmAll(16, 16);
    HashMap<Integer, Integer> rep = new HashMap<Integer, Integer>();
    w.union(rep, 2, 3);
    t.checkExpect(rep, rep);
  }

  //test the find method 
  void testFind(Tester t) {
    this.initVals();
    LightEmAll w = new LightEmAll(16, 16);
    HashMap<Integer, Integer> rep = new HashMap<Integer, Integer>();
    t.checkExpect(w.find(rep, 2), 2);
  }
}
