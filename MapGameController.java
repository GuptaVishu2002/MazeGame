import java.net.URL;
import javafx.application.Platform;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;

    
public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public CharaAction charaAction;
    public ImageView[] mapImageView;
    private static final int NONE = 0;
    private static final int GOAL = 1;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
	initialize();
    }

    public void initialize() {
	int x = 0;
	int y = 0;
	
	mapData = new MapData(21,15);
	
	while (mapData.getMap(x,y) != MapData.TYPE_NONE) {
	    x = (int) (Math.random() * mapData.getWidth()/2  ) * 2 + 1;
	    y = (int) (Math.random() * mapData.getHeight()/2 ) * 2 + 1;
	}
	
	chara = new MoveChara(x,y,mapData);
	mapImageView = new ImageView[mapData.getHeight()*mapData.getWidth()];
	for(y=0; y<mapData.getHeight(); y++){
	    for(x=0; x<mapData.getWidth(); x++){
		int index = y*mapData.getWidth() + x;
		mapImageView[index] =new ImageView();
		mapGrid.add(mapImageView[index], x, y);
	    }
	}
	mapPrint(chara, mapData);
	charaAction = new CharaAction(chara, mapData);
	System.out.println("ca finished");
    }

    public void mapPrint(MoveChara c, MapData m){
	for(int y=0; y<mapData.getHeight(); y++){
	    for(int x=0; x<mapData.getWidth(); x++){
		int index = y*mapData.getWidth() + x;
		if (x==c.getPosX() && y==c.getPosY()){
		    mapImageView[index].setImage(c.getImage());
		} else {
		    mapImageView[index].setImage(m.getImage(x,y));
		}
	    }
	}
	
    }

    public void goal(){
	Alert alert = new Alert( AlertType.NONE , "" , ButtonType.NO, ButtonType.YES); 
	alert.getDialogPane().setContentText("GOAL!\nNEW GAME?");
	ButtonType b = alert.showAndWait().orElse( ButtonType.CANCEL);
	if (b == ButtonType.YES){
	    initialize();
	} else {
	    System.exit(0);
	}
    }
    
    public void func1ButtonAction(ActionEvent event) {
	int r;
	charaAction.performActionRandom();
	mapPrint(chara, mapData);
	r = moveCheck();
	if (r == GOAL) {
	    goal();
	}
    }
    
    public void func2ButtonAction(ActionEvent event) {
	int i,r;
	i = charaAction.performActionToKey();
	mapPrint(chara, mapData);
	r = moveCheck();
	if (r == GOAL) {
	    goal();
	}
	System.out.println("KEY!");
    }

    public void func3ButtonAction(ActionEvent event) {
	int i,r;
	i = charaAction.performActionToGoal();
	mapPrint(chara, mapData);
	r = moveCheck();
	if (r == GOAL) {
	    goal();
	}
	mapPrint(chara, mapData);

	System.out.println("GOAL!");
    }
    
    public void func4ButtonAction(ActionEvent event) {
    }

    public void func5ButtonAction(ActionEvent event) {
    }

    public void func6ButtonAction(ActionEvent event) {
	initialize();
    }

    public void func7ButtonAction(ActionEvent event) {
	int i = -1;
	int r = NONE;
	while ( i != CharaAction.NONE){
	    i = charaAction.performActionToKey();
	    mapPrint(chara, mapData);
	    moveCheck();
	}
	mapPrint(chara, mapData);
	r = moveCheck();
	if (r == GOAL) {
	    goal();
	}
	System.out.println("KEY!");
    }

    public void func8ButtonAction(ActionEvent event) {
	int i = -1;
	int r = NONE;
	while ( i != CharaAction.NONE && r != GOAL){
	    i = charaAction.performActionToGoal();
	    mapPrint(chara, mapData);
	    r = moveCheck();
	}
	if (r == GOAL) {
	    goal();
	}
	mapPrint(chara, mapData);

	System.out.println("GOAL!");
    }

    public void func9ButtonAction(ActionEvent event) {
    }

    public void func10ButtonAction(ActionEvent event) {
    }
    
    public int moveCheck(){
	int cx = chara.getPosX();
	int cy = chara.getPosY();
	int cm = mapData.getMap(cx, cy);
	if (cm == MapData.TYPE_KEY) {
	    System.out.println("GET KEY");
	    mapData.getKey(cx, cy, MapData.TYPE_RED);
	} else if (cm == MapData.TYPE_NONE || cm == MapData.TYPE_BLUE) {
	    if (mapData.getIsExistKey()) {
		mapData.setMap(cx, cy, MapData.TYPE_BLUE);
	    } else {
		if (cm == MapData.TYPE_NONE) {
		    mapData.setMap(cx, cy, MapData.TYPE_RED);
		} else {
		    mapData.setMap(cx, cy, MapData.TYPE_PURPLE);
		}
	    }
	} else if (cm == MapData.TYPE_GOAL && ! mapData.getIsExistKey()){
	    return GOAL;
	}
	return NONE;
    }
    
    public void keyAction(KeyEvent event){
	KeyCode key = event.getCode();
	if (key == KeyCode.DOWN){
	    downButtonAction();
	}else if (key == KeyCode.UP){
	    upButtonAction();
	}else if (key == KeyCode.LEFT){
	    leftButtonAction();
	}else if (key == KeyCode.RIGHT){
	    rightButtonAction();
	}
    }
    
    public void upButtonAction(){
	int r;
	System.out.println("FORWARD");
	//	chara.setCharaDir(MoveChara.TYPE_UP);
	chara.moveForward();
	//	chara.move(0,-1);
	mapPrint(chara, mapData);
	r = moveCheck();
	if (r == GOAL) {
	    goal();
	}
    }
    
    public void upButtonAction(ActionEvent event) {
	upButtonAction();
    }
    
    public void downButtonAction(){
	int r;
	System.out.println("BACKWARD");
	//	chara.setCharaDir(MoveChara.TYPE_DOWN);
	chara.moveBackward();
	//chara.move(0, 1);
	mapPrint(chara, mapData);
	r = moveCheck();
	if (r == GOAL) {
	    goal();
	}
    }
    
    public void downButtonAction(ActionEvent event) {
	downButtonAction();
    }
    
    public void rightButtonAction(){
	int r;
	System.out.println("RIGHT");
	//	chara.setCharaDir(MoveChara.TYPE_RIGHT);
	//chara.move( 1, 0);
	chara.rightRotateCharaDir();
	mapPrint(chara, mapData);
	r = moveCheck();
	if (r == GOAL) {
	    goal();
	}
    }
    
    public void rightButtonAction(ActionEvent event) {
	rightButtonAction();
    }

    public void leftButtonAction(){
	int r;
	System.out.println("LEFT");
	//chara.setCharaDir(MoveChara.TYPE_RIGHT);
	//chara.move(-1, 0);
	chara.leftRotateCharaDir();
	mapPrint(chara, mapData);
	r = moveCheck();
	if (r == GOAL) {
	    goal();
	}
    }
    
    public void leftButtonAction(ActionEvent event) {
	leftButtonAction();
    }
}
