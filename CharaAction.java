

public class CharaAction {
    public int[][] startToKeyCost;
    public int[][] keyToGoalCost;
    public static final int NONE       = 0;
    public static final int GOFORWARD  = 1;
    public static final int GOBACKWARD = 2;
    public static final int TURNLEFT   = 3;
    public static final int TURNRIGHT  = 4;
    private MapData mapData;
    private MoveChara chara;
    private int width;
    private int height;
    private final int[][] DIRINFO = {{0,1}, {-1,0}, {0,-1}, {1,0}};
    

    private void initalizeCost(){
	startToKeyCost = new int[height][width];
	keyToGoalCost  = new int[height][width];

	for(int x=0;x<width;x++){
	    for(int y=0;y<height;y++){
		startToKeyCost[y][x] = 100;
		keyToGoalCost[y][x]  = 100;
	    }
	}
    }
    
    private void setCost(int[][] costArray, int cx, int cy, int cost){
	int c = mapData.getMap(cx, cy);
	if (c == -1 || c == MapData.TYPE_WALL) return;
	if (costArray[cy][cx] != 100) return;
	costArray[cy][cx] = cost;
	for (int i=0;i<4;i++){
	    setCost(costArray, cx + DIRINFO[i][0], cy + DIRINFO[i][1], cost + 1);
	}
    }
    
    CharaAction(MoveChara chara, MapData mapData){
	this.mapData = mapData;
	this.chara   = chara;
	width  = mapData.getWidth();
	height = mapData.getHeight();
	
	initalizeCost();
	
	setCost(startToKeyCost, mapData.getKeyPosX() , mapData.getKeyPosY() , 0);
	setCost(keyToGoalCost , mapData.getGoalPosX(), mapData.getGoalPosY(), 0);

	for(int y=0;y<height;y++){
	    for(int x=0;x<width;x++){
		System.out.printf("%4d", startToKeyCost[y][x]);
	    }
	    System.out.print("\n");
	}
	System.out.print("========================\n");

    	for(int y=0;y<height;y++){
	    for(int x=0;x<width;x++){
		System.out.printf("%4d", keyToGoalCost[y][x]);
	    }
	    System.out.print("\n");
	}
    }

    public void runAction(int a){
	if (a == GOFORWARD) {
	    System.out.println("Select to forward");
	    chara.moveForward();
	} else if (a == GOBACKWARD) {
	    System.out.println("Select to go backward");
	    chara.moveBackward();
	} else if (a == TURNLEFT) {
	    System.out.println("Select to turn left");
	    chara.leftRotateCharaDir();
	} else if (a == TURNRIGHT) {
	    System.out.println("Select to turn right");
	    chara.rightRotateCharaDir();
	}
    }
    
    public void performActionRandom(){
	int r = (int)(Math.random() * 5) + 1;
	runAction(r);
    }

    public int performActionToGoal(){
	return performActionByCost(keyToGoalCost);
    }

    public int performActionToKey(){
	return performActionByCost(startToKeyCost);
    }

    public int performActionByCost(int[][] costArray){
	int minID = 0;
	int min = 100;
	int cx = chara.getPosX();
	int cy = chara.getPosY();
	if (costArray[cy][cx] == 0){
	    runAction(NONE);
	    return NONE;
	}
	for (int i=0;i<4;i++){
	    int x = cx + DIRINFO[i][0];
	    int y = cy + DIRINFO[i][1];
	    int c = costArray[y][x];
	    if (c < min) {
		minID = i;
		min = c;
	    }
	}
	int cd = chara.getCharaDir();
	int dd = (cd - minID + 4) % 4;
	//	System.out.println("x:"+cx+"/y:"+cy+"/minDir:" + minID + "/curDir:" + cd + "/dd:" + dd);
	if (dd == 3) { dd = -1; }
	int selectedAction = NONE;
	if (dd == 0) {
	    selectedAction = GOFORWARD;
	} else if (dd == 2) {
	    selectedAction = GOBACKWARD;
	} else if (dd == -1){
	    selectedAction = TURNRIGHT;
	} else if (dd == 1){
	    selectedAction = TURNLEFT;
	}
	runAction(selectedAction);
	return selectedAction;
    }
}

