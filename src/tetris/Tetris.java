package tetris;

enum TetrisState{
	Start(0), Running(1), NewBlock(2), Finished(3), Error(4);
	private final int value;
	private TetrisState(int value){ this.value = value; }
	public int value() { return value; }
}

//lab 5.1
interface ActionHandler {
	public void run(Tetris t, char key) throws Exception;
}
class OnLeft implements ActionHandler {
	public void run(Tetris t, char key) { t.left = t.left - 1; }
}
class OnRight implements ActionHandler {
	public void run(Tetris t, char key) { t.left = t.left + 1; }
}
class OnDown implements ActionHandler {
	public void run(Tetris t, char key) { t.top = t.top + 1; }
}
class OnUp implements ActionHandler {
	public void run(Tetris t, char key) { t.top = t.top - 1; }
}
class OnCW implements ActionHandler {
	public void run(Tetris t, char key) {
		t.idxBlockDegree = (t.idxBlockDegree + 1) % t.nBlockDegrees;
		t.currBlk = t.setOfBlockObjects[t.idxBlockType][t.idxBlockDegree];
	}
}
class OnCCW implements ActionHandler {
	public void run (Tetris t, char key) {
		t.idxBlockDegree = (t.idxBlockDegree + 3) % t.nBlockDegrees;
		t.currBlk = t.setOfBlockObjects[t.idxBlockType][t.idxBlockDegree];
	}
}
class OnNewBlock implements ActionHandler {
	public void run(Tetris t, char key) throws Exception {
		if(t.isJustStarted == false)	// 첫 시작이 아닌 경우에, 새 블록이 필요하다면 fullLineDelete를 진행한다.
			t.oScreen.fullLineDelete(t.oScreen, t.iScreenDw, t.iScreenDx);
		t.isJustStarted = false;
		
		t.iScreen = new Matrix(t.oScreen);
		t.top = 0;
		t.left = t.iScreenDw + t.iScreenDx/2 - 2;
		t.idxBlockType = key - '0';
		t.idxBlockDegree = 0;
		t.currBlk = t.setOfBlockObjects[t.idxBlockType][t.idxBlockDegree];
	}
}
class OnFinished implements ActionHandler{
	public void run(Tetris t, char key) {
		System.out.println("OnFinished.run(); called");
	}
}

public class Tetris {
	/*
	 * 디버그 레벨에 따라서 출력여부를 결정한다.
	 * level이 높을수록 더 자세하게 정보를 출력한다.
	 * 예) currentDebugLevel의 값이 3이면, 모든 정보를 출력한다. 
	 * 예) currentDebugLevel의 값이 2이면, debugLevel 1 ~ 2까지의 정보를 출력한다. (특정 이벤트 및 흐름)
	 * 예) currentDebugLevel의 값이 1이면, debugLevel 1만 출력 ( 프로그램의 흐름에 대한 정보만 )
	 * 예) currentDebugLevel의 값이 0이면, 아무것도 출력하지 않음. 
	 */
	private final static int currentDebugLevel = 3;	// 현재 디버그 레벨.
	private final static int debugLevel1 = 1;	// 프로그램의 흐름에 대한 정보. 
	private final static int debugLevel2 = 2;	// 프로그램에서 어떠한 이벤트에 대한 정보.
	private final static int debugLevel3 = 3;	// 특정 이벤트가 발생한 상황에서 변수의 변화 등에 대한 정보.

    private static int numberOfBlockTypes;
    private TetrisState tetrisState;
	private boolean tetrisActionsInitialized = false;

	protected static int iScreenDw;
    protected static int nBlockDegrees;
    protected static Matrix[][] setOfBlockObjects;
	protected boolean isJustStarted; // ??
	protected int iScreenDy;
	protected int iScreenDx;
	protected int idxBlockType = 0;
	protected int idxBlockDegree; // ??
    protected int top;
    protected int left;    
    // Matrix iScreen = new Matrix(createArrayScreen(iScreenDy,iScreenDx, iScreenDw));
    // 이 방법은 좋지 않다. 이 함수가 불리는 시점에 createArrayScreen의 인자의 상태는 어떨까.. 생각.. 생성자가 더 좋음.
    protected Matrix iScreen = null;
    protected Matrix oScreen = null;
    protected Matrix currBlk = null;

    private static int[][] createArrayScreen(int dy, int dx, int dw) {
        int y, x;
        int[][] array = new int[dy + dw][dx + 2 * dw];
        for (y = 0; y < array.length; y++)
            for (x = 0; x < dw; x++)
                array[y][x] = 1;
        for (y = 0; y < array.length; y++)
            for (x = dw + dx; x < array[0].length; x++)
                array[y][x] = 1;
        for (y = dy; y < array.length; y++)
            for (x = 0; x < array[0].length; x++)
                array[y][x] = 1;
        return array;
    }
    
    public void printScreen() {
    	Matrix screen = oScreen;
        int dy = screen.get_dy();
        int dx = screen.get_dx();
        int dw = iScreenDw;
        int array[][] = screen.get_array();
        for (int y = 0; y < dy - dw + 1; y++) {
            for (int x = dw -1; x < dx - dw + 1; x++) {
                if (array[y][x] == 0) System.out.print("□ ");
                else if (array[y][x] == 1) System.out.print("■ ");
                else System.out.print("x ");
            }
            System.out.println();
        }
    }
    
    /*
     * 입력 : 4차원 int형 배열 setOfBlkArrays, 블록 종류별로 각도 변화에 따른 모양이 입력되어있음.
     * 기능 : 각각의 2차원 int형 배열 -> Matrix로 변환함.
     * 		 이것을 Matrix[블록타입][각도] 형태로 2차원 Matrix형 배열에 저장.
     * 출력 : 2차원 Matrix 배열 
     */
    public static void init(int[][][][] setOfBlkArrays) throws MatrixException{
    	numberOfBlockTypes = setOfBlkArrays.length;
    	nBlockDegrees = setOfBlkArrays[0].length;
    	
    	setOfBlockObjects = new Matrix[numberOfBlockTypes][nBlockDegrees];
    	
    	for(int t = 0; t < numberOfBlockTypes; t++){
    		for(int d = 0; d < nBlockDegrees; d++){
    			setOfBlockObjects[t][d] = new Matrix(setOfBlkArrays[t][d]);
    		}
    	}
    }
    
    // public Tetris(int cy, int cx) throws MatrixException, TetrisException{	// 아래에서 Exception 하나로 통합.
    public Tetris(int cy, int cx, int[][][][] setOfBlkArrays) throws Exception{
    	checkSquareMatrix(setOfBlkArrays);
    	iScreenDw = findLargestBlockSize(setOfBlkArrays);
    	
    	if(cy < iScreenDw || cx < iScreenDw)
    		throw new TetrisException("too small screen");
    	
    	iScreenDy = cy;
    	iScreenDx = cx;
    	iScreen = new Matrix(createArrayScreen(iScreenDy,iScreenDx, iScreenDw));
    	oScreen = new Matrix(iScreen);
        top = 0;
        left = iScreenDw + iScreenDx/2 - 2;
        tetrisState = TetrisState.Start;
        
        //?? 
        idxBlockDegree = 0;
        isJustStarted = true;
    }
    
    // 정방행렬인지도 체크 -> 아니면 exception 날림
    private void checkSquareMatrix(int[][][][] setOfBlkArrays) throws Exception{
    	for(int blkType = 0; blkType < setOfBlkArrays.length; blkType++){
    		for(int blkDegree = 0; blkDegree < setOfBlkArrays[0].length; blkDegree++){
    			if(setOfBlkArrays[blkType][blkDegree].length != setOfBlkArrays[blkType][blkDegree][0].length)
        			throw new TetrisException("정방행렬이 아닙니다!");
    		}
    	}
    }
    
    // iScreenDw 결정
    private int findLargestBlockSize(int[][][][] setOfBlkArrays){
    	int maxSize = 0;
    	for(int i = 0; i < setOfBlkArrays.length; i++){	// 종류
    		if(currentDebugLevel >= debugLevel3) System.out.println("블록 길이 : " + setOfBlkArrays[i][0].length);
    		if(maxSize < setOfBlkArrays[i][0].length) maxSize = setOfBlkArrays[i][0].length; // maxSize 갱신
    	}
    	if(currentDebugLevel >= debugLevel3) System.out.println("maxBlockSize : " + maxSize);
    	return maxSize;
    }
    
    private OnLeft onLeft = new OnLeft();
    private OnRight onRight = new OnRight();
    private OnDown onDown = new OnDown();
    private OnUp onUp = new OnUp();
    private OnCW onCW = new OnCW();
    private OnCCW onCCW = new OnCCW();
    private OnNewBlock onNewBlock = new OnNewBlock();
    private OnFinished onFinished = new OnFinished();
    
    
    class TetrisAction{
    	private ActionHandler hDo, hUndo;
    	public TetrisAction(ActionHandler d, ActionHandler u){
    		hDo = d;
    		hUndo = u;
    	}
    	public boolean run(Tetris t, char key, boolean update) throws Exception{
    		boolean anyConflict = false;
    		hDo.run(t, key);
    		Matrix tempBlk;
    		tempBlk = iScreen.clip(top, left, top+currBlk.get_dy(), left+currBlk.get_dx());
    		tempBlk = tempBlk.add(currBlk);
    		if ((anyConflict = tempBlk.anyGreaterThan(1)) == true){
    			hUndo.run(t, key);
    			tempBlk = iScreen.clip(top, left, top+currBlk.get_dy(), left+currBlk.get_dx());
    			tempBlk = tempBlk.add(currBlk);
    		}
    		if(update == true){
    			oScreen = new Matrix(iScreen);
    			oScreen.paste(tempBlk, top, left);
    		}
    		return anyConflict;
    	}
    }
    private TetrisAction moveLeft, moveRight, moveDown, rotateCW, insertBlk;
    private void setTetrisActions(){
    	moveLeft = new TetrisAction(onLeft, onRight);
    	moveRight = new TetrisAction(onRight, onLeft);
    	moveDown = new TetrisAction(onDown, onUp);
    	rotateCW = new TetrisAction(onCW, onCCW);
    	insertBlk = new TetrisAction(onNewBlock, onFinished);
    	tetrisActionsInitialized = true;
    }
    public void setOnLeftListener(OnLeft listener) { tetrisActionsInitialized = false; onLeft = listener; }
    public void setOnRightListener(OnRight listener) { tetrisActionsInitialized = false; onRight = listener; }
    public void setOnDownListener(OnDown listener) { tetrisActionsInitialized = false; onDown = listener; }
    public void setOnUpListener(OnUp listener) { tetrisActionsInitialized = false; onUp = listener; }
    public void setOnCWListener(OnCW listener) { tetrisActionsInitialized = false; onCW = listener; }
    public void setOnCCWListener(OnCCW listener) { tetrisActionsInitialized = false; onCCW = listener; }
    public void setOnNewBlockListener(OnNewBlock listener) { tetrisActionsInitialized = false; onNewBlock = listener; }
    public void setOnFinishedListener(OnFinished listener) { tetrisActionsInitialized = false; onFinished = listener; }
    
    /*
     * 입력
     * 1. 입력한 키 값 -> key
     * 2. 난수 발생하여 들어온 블록번호 -> idxType
     * 
     * 목적
     * - 게임 엔진에 불확정적인 것을 포함시키지 않기 위함. 이전 버전에서 랜덤 관련 처리 등.. 
     * - 게임의 입력이 바뀌어도 상관없이 쓸 수 있도록.. 
     * 
     * 참고
     * - 난수를 발생하는 것은 Tetris 클래스 내에서 처리하지 않고, Main 함수에서 넘겨줌.
     * - 따라서 이 함수에서는 외부의 들어온 입력에 대해서 처리를 진행함.
     * - 게임의 상태에 대해서 top, left, blkType, blkDegree, screen 등으로 위치, 회전, 모양, 상태 등을 저장해야함.
     * 
     * 리턴
     * - 현재 프로그램의 상태 리턴 
     */
    public TetrisState accept(char key) throws Exception {
    	if(currentDebugLevel >= debugLevel3) System.out.println("Key 들어오기 전 State : " + tetrisState);
        if(currentDebugLevel >= debugLevel3) System.out.println("들어온 key : " + key);
    	int idxType;
    	// TetrisActions이 초기화 되지 않았다면 초기화 시켜줌.
        if(tetrisActionsInitialized == false){	
        	setTetrisActions();
        	tetrisActionsInitialized = true;
        	tetrisState = TetrisState.NewBlock;
        }

        // NewBlock 상태에서 동작
        if(tetrisState == TetrisState.NewBlock){											   
    		if(insertBlk.run(this, key, true) == true)	// 새 블록이 바로 충돌을 하면 게임종료 조건임.
    			tetrisState = TetrisState.Finished;	// 게임 끝.
    		else
    			tetrisState = TetrisState.Running;	// 진행.
    		return tetrisState;
    	}
        
        /*
        // Start 혹은 NewBlock 상태에서만 현재 블록을 변경시킴, 나머지는 조작 상태이기 때문에 idxType을 바꿀 필요가 없음.
        if(tetrisState == TetrisState.Start || tetrisState == TetrisState.NewBlock){
        	idxType = key - '0';
        	if(currentDebugLevel >= debugLevel3) System.out.println("idxType : " + idxType);
        	
        	blkType = idxType; // 외부에서 받아온 블록 모양 	
            currBlk = setOfBlockObjects[blkType][blkDegree];
        }
        
        // 상태는 일단 Running으로 변경, 예외(Error, Stop 상황)가 발생하면 그 시점에서 동작을 중단하고 상태를 변경 후 바로 리턴함. 
        tetrisState = TetrisState.Running;	

        // 게임의 상태니 위로 올림.
        //int top = 0;
        //int left = iScreenDw + iScreenDx/2 - 2;
        //int[][] arrayScreen = createArrayScreen(iScreenDy, iScreenDx, iScreenDw);
        /1/char key;
        //Matrix iScreen = new Matrix(arrayScreen);
        
        // 기존의 난수 생성은 삭제한다.
        //Random random = new Random(); // 다음 블록을 결정할 난수생성기
        //int idxBlockType = random.nextInt(numberOfBlockType);
        
        if(currentDebugLevel >= debugLevel3) System.out.println("다음 블록 번호 : " + blkType);
        //다음 블록을 생성한다. 
        currBlk = setOfBlockObjects[blkType][blkDegree];	
        Matrix tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
        tempBlk = tempBlk.add(currBlk);

        // 이 부분을 추가하는 이유는 블록이 생성되자마자 다른 블록과 충돌(게임오버 조건) 검사하기 위함임. 여기서 검사하지 않으면 화면에 x자가 출력된다. 아래 부분을 실행 말고 종료.
        if(tempBlk.anyGreaterThan(1)){
        	if(currentDebugLevel >= debugLevel3) System.out.println("종료조건 충족");
        	tetrisState = TetrisState.Finished;
        	if(currentDebugLevel >= debugLevel3) System.out.println("accept 처리 후 State : " + tetrisState);
        	return tetrisState;
        }
        
        // Matrix oScreen = new Matrix(iScreen);
        oScreen.paste(tempBlk, top, left);
        //printScreen(); System.out.println();
		*/

        //while ((key = getKey()) != 'q') {
            switch (key) {
                case 'a':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 왼쪽 이동.");
                    //left--;
                	//onLeft.run(key);
                	moveLeft.run(this, key, true);
                    break;
                case 'd':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 오른쪽 이동.");
                    //left++;
                	//onRight.run(key);
                	moveRight.run(this, key,  true);
                    break;
                case 's':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 아래로 이동. top 변화 전 : " + top);
                    //top++;
                    //onDown.run(key);
                	if(moveDown.run(this, key,  true) == true) tetrisState = TetrisState.NewBlock;	// 아래로 보내보고, 충돌이 있다면 새 블록 상태로.
                    if(currentDebugLevel >= debugLevel2) System.out.println("블록 아래로 이동. top 변화 후: " + top);
                    break;
                case 'w':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록을 회전시킵니다.");
                	/*
                	blkDegree = (blkDegree + 1) % 4;
                	currBlk = setOfBlockObjects[blkType][blkDegree];
                	*/
                	//onCW.run(key);
                	rotateCW.run(this, key, true);
                	if(currentDebugLevel >= debugLevel3) System.out.println("idxBlockDegree : " + idxBlockDegree);
                    break;
                case ' ':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록을 끝까지 내립니다.");
                    /*
                    // 바닥에 닿기 전까지 한칸씩 내려가며 충돌하나 비교한다.
                	while (!tempBlk.anyGreaterThan(1)){ // 충돌까지 내린다.
                    	//top ++;	// 내리고.
                    	onDown.run(key);
                    	tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx()); // 갱신하고.
                        tempBlk = tempBlk.add(currBlk); // 현재 블록 넣고.
                    	if(currentDebugLevel >= debugLevel3) System.out.println("블록을 하나 내려봅니다. top : " + top );
                    }
					// 여기까지 왔다면 블록은 현재 충돌된 상태임. 아래 최종 작업에서 top을 하나 빼주어야함.
                    */
                	while(moveDown.run(this, key, false) == false);	// 바닥 충돌을 안했다면 계속 내린다. 화면 업데이트는 하지 않음.
                	moveDown.run(this, key,  true);
                	tetrisState = TetrisState.NewBlock;
                    break;
                default :
                	// 잘못된 key의 경우 이쪽이 실행된다.
                	if( (0 <= key - '0') && ( key - '0' < numberOfBlockTypes)){ // 숫자의 경우는 새로운 블록을 요청이니 제외함.
                		if(currentDebugLevel >= debugLevel2) System.out.println("BlockType이 입력으로 들어옴");
                	}else{	// 숫자가 아닌 경우는 State를 Error로 변경함.
	                	if(currentDebugLevel >= debugLevel2) System.out.println("잘못된 key의 입력, key : " + key);
	                	tetrisState = TetrisState.Error;
	                	if(currentDebugLevel >= debugLevel3) System.out.println("accept 처리 후 State : " + tetrisState);
	                	return tetrisState;	// 에러인 경우에는 아래를 실행하지 않고 끝내자.
                	}
            }
            /*
            tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
            tempBlk = tempBlk.add(currBlk);
            if (tempBlk.anyGreaterThan(1)) {
                switch (key) {
                    case 'a':
                        //left++;
                    	onRight.run(key);
                        break;
                    case 'd':
                        //left--;
                    	onLeft.run(key);
                        break;
                    case 's':
                        //top--;
                    	onUp.run(key);
                    	tetrisState = TetrisState.NewBlock;
                        break;
                    case 'w':
                    	if(currentDebugLevel >= debugLevel2) System.out.println("블록이 회전 과정에서 충돌하였음. 이전으로 돌아감");
                    	
                    	//blkDegree = blkDegree - 1;	// Degree를 이전으로 돌림.
                    	//if(blkDegree == -1) blkDegree = 3; // 회전 : 3 -> 0 , 충돌 : 0 -> -1 케이스니 3으로 되돌림.
                    	//currBlk = setOfBlockObjects[blkType][blkDegree];
                    	
                    	onCCW.run(key);
                        break;
                    case ' ':
                    	// 이미 충돌된 상태임. top을 하나 빼주어 충돌 직전 위치로 이동. 
                    	//top--;
                    	onUp.run(key);
                    	if(currentDebugLevel >= debugLevel3) System.out.println("블록이 바닥에 충돌하였음. 최종 top의 값 : " + top);
                    	tetrisState = TetrisState.NewBlock;
                        break;
                }
                tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
                tempBlk = tempBlk.add(currBlk);
            }
            
            oScreen = new Matrix(iScreen);
            oScreen.paste(tempBlk, top, left);
            //printScreen(); System.out.println();
          
            oScreen.fullLineDelete(oScreen, iScreenDw, tempBlk, iScreenDx);
        	
        	if(tetrisState == TetrisState.NewBlock){	// 새로운 블록이 필요한 경우라면..
        		iScreen = new Matrix(oScreen);	// 마지막 상태 iScreen에 저장.
        		top = 0;	
                left = iScreenDw + iScreenDx/2 - 2;
                blkDegree = 0;	// 각도 0으로.
        	}
        	*/
        	if(currentDebugLevel >= debugLevel3) System.out.println("accept 처리 후 State : " + tetrisState);
            return tetrisState;
    }
}

class TetrisException extends Exception {
	public TetrisException() { super("Tetris Exception"); }
	public TetrisException(String msg) { super(msg); }
}