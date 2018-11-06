import javafx.scene.image.Image;

public class MapData {
    public static final int TYPE_WALL   = 10;
    public static final int TYPE_KEY    = 11;
    public static final int TYPE_NONE   = 0;
    public static final int TYPE_BLUE   = 1;
    public static final int TYPE_RED    = 2;
    public static final int TYPE_PURPLE = 3;
    public static final int TYPE_GOAL   = 9;
    public static final int TYPE_OTHERS = 99;
    public Image[] mapImage;
    private int[] map;
    private int[] map2image;
    private int width;
    private int height;
    private boolean isExistKey;
    private int keyPosX;
    private int keyPosY;
    private int goalPosX;
    private int goalPosY;
    
    MapData(int x, int y){
	mapImage = new Image[12];
	mapImage[TYPE_NONE]   = new Image("SPACE.png");
	mapImage[TYPE_BLUE]   = new Image("SPACE-BLUE.png");
	mapImage[TYPE_RED]    = new Image("SPACE-RED.png");
	mapImage[TYPE_PURPLE] = new Image("SPACE-PURPLE.png");
	mapImage[TYPE_WALL]   = new Image("WALL.png");
	mapImage[TYPE_KEY]    = new Image("KEY.png");
	mapImage[TYPE_GOAL]   = new Image("GOAL.png");
	width  = x;
	height = y;
	map = new int[y*x];
	initMap();
    }

    public void putGoal(){
	int rx = 0;
	int ry = 0;

	while (getMap(rx,ry) != MapData.TYPE_NONE) {
	    rx = (int) (Math.random() * width/2  ) * 2 + 1;
	    ry = (int) (Math.random() * height/2 ) * 2 + 1;
	}
	goalPosX = rx;
	goalPosY = ry;

	setMap(rx, ry, MapData.TYPE_GOAL);
    }
    public void putKey(){
	int rx = 0;
	int ry = 0;

	while (getMap(rx,ry) != MapData.TYPE_NONE) {
	    rx = (int) (Math.random() * width/2  ) * 2 + 1;
	    ry = (int) (Math.random() * height/2 ) * 2 + 1;
	}
	keyPosX = rx;
	keyPosY = ry;
	setMap(rx, ry, MapData.TYPE_KEY);
	isExistKey = true;
    }
    
    public void initMap(){
	fillMap(MapData.TYPE_WALL);
	digMap(1, 3);
	putKey();
	putGoal();
    }
    
    public int getHeight(){
	return height;
    }

    public int getWidth(){
	return width;
    }

    public int getKeyPosX(){
	return keyPosX;
    }

    public int getKeyPosY(){
	return keyPosY;
    }

    public int getGoalPosX(){
	return goalPosX;
    }

    public int getGoalPosY(){
	return goalPosY;
    }

    public int toIndex(int x, int y){
	return x + y * width;
    }

    public boolean getIsExistKey(){
	return isExistKey;
    }
    
    public int getMap(int x, int y) {
	if (x < 0 || width <= x || y < 0 || height <= y) {
	    return -1;
	}
	return map[toIndex(x,y)];
    }

    public Image getImage(int x, int y) {
	return mapImage[getMap(x,y)];
    }

    public void setMap(int x, int y, int type){
	if (x < 1 || width <= x-1 || y < 1 || height <= y-1) {
	    return;
	}
	map[toIndex(x,y)] = type;
    }
    
    public void fillMap(int type){
	for (int y=0; y<height; y++){
	    for (int x=0; x<width; x++){
		map[toIndex(x,y)] = type;
	    }
	}
    }

    public void digMap(int x, int y){
	setMap(x, y, MapData.TYPE_NONE);
	int[][] dl = {{0,1},{0,-1},{-1,0},{1,0}};
	int[] tmp;

	for (int i=0; i<dl.length; i++) {
	    int r = (int)(Math.random()*dl.length);
	    tmp = dl[i];
	    dl[i] = dl[r];
	    dl[r] = tmp;
	}

	for (int i=0; i<dl.length; i++){
	    int dx = dl[i][0];
	    int dy = dl[i][1];
	    if (getMap(x+dx*2, y+dy*2) == MapData.TYPE_WALL){
		setMap(x+dx, y+dy, MapData.TYPE_NONE);
		digMap(x+dx*2, y+dy*2);
		
	    }
	}
    }

    public void getKey(int x, int y, int type){
	setMap(x, y, type);
	isExistKey = false;
    }

    public void printMap(){
	for (int y=0; y<height; y++){
	    for (int x=0; x<width; x++){
		if (getMap(x,y) == MapData.TYPE_WALL){
		    System.out.print("++");
		}else{
		    System.out.print("  ");
		}
	    }
	    System.out.print("\n");
	}
    }
}
