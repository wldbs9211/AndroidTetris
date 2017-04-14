package tetris;

enum TetrisState{
	Start(0), Running(1), NewBlock(2), Finished(3), Error(4);
	private final int value;
	private TetrisState(int value){ this.value = value; }
	public int value() { return value; }
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
	private final static int currentDebugLevel = 0;	// 현재 디버그 레벨.
	private final static int debugLevel1 = 1;	// 프로그램의 흐름에 대한 정보. 
	private final static int debugLevel2 = 2;	// 프로그램에서 어떠한 이벤트에 대한 정보.
	private final static int debugLevel3 = 3;	// 특정 이벤트가 발생한 상황에서 변수의 변화 등에 대한 정보.

    private TetrisState tetrisState;
	protected boolean tetrisActionsInitialized = false;
	protected static int nBlockTypes;
	
	protected static int iScreenDw;
    protected static int nBlockDegrees;
    protected static Matrix[][] setOfBlockObjects;
	protected boolean isJustStarted; 
	protected int iScreenDy;
	protected int iScreenDx;
	protected int idxBlockType = 0;
	protected int idxBlockDegree;  
    protected int top;
    protected int left;    
    // Matrix iScreen = new Matrix(createArrayScreen(iScreenDy,iScreenDx, iScreenDw));
    // 이 방법은 좋지 않다. 이 함수가 불리는 시점에 createArrayScreen의 인자의 상태는 어떨까.. 생각.. 생성자가 더 좋음.
    protected Matrix iScreen = null;
    protected Matrix oScreen = null;
    protected Matrix currBlk = null;

    protected static int[][] createArrayScreen(int dy, int dx, int dw) {
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
    	nBlockTypes = setOfBlkArrays.length;
    	nBlockDegrees = setOfBlkArrays[0].length;
    	
    	setOfBlockObjects = new Matrix[nBlockTypes][nBlockDegrees];
    	
    	for(int t = 0; t < nBlockTypes; t++){
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
    	protected ActionHandler hDo, hUndo;
    	public TetrisAction(ActionHandler d, ActionHandler u){
    		hDo = d;
    		hUndo = u;
    	}
    	public boolean run(Tetris t, char key, boolean update) throws Exception{
    		boolean anyConflict = false;
    		hDo.run(t, key);
    		Matrix tempBlk;
    		tempBlk = iScreen.clip(top, left, top+currBlk.get_dy(), left+currBlk.get_dx());	// 배경 뜯어옴
    		tempBlk = tempBlk.add(currBlk);	// 배경에 현재 블록을 삽입
    		if ((anyConflict = tempBlk.anyGreaterThan(1)) == true){	// 충돌검사
    			hUndo.run(t, key);	// 되돌리기
    			tempBlk = iScreen.clip(top, left, top+currBlk.get_dy(), left+currBlk.get_dx());	// 배경 다시 뜯고
    			tempBlk = tempBlk.add(currBlk);	// 다시 이전처럼 붙임
    		}
    		if(update == true){
    			oScreen = new Matrix(iScreen);
    			oScreen.paste(tempBlk, top, left);
    		}
    		return anyConflict;
    	}
    }
    protected TetrisAction moveLeft, moveRight, moveDown, rotateCW, insertBlk;
    protected void setTetrisActions(){
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
    		if(insertBlk.run(this, key, true) == true){	// 새 블록이 바로 충돌을 하면 게임종료 조건임.
    			tetrisState = TetrisState.Finished;	// 게임 끝.
    		}
    		else{
    			tetrisState = TetrisState.Running;	// 진행.
    		}
    		return tetrisState;
    	}

            switch (key) {
                case 'a':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 왼쪽 이동.");
                	moveLeft.run(this, key, true);
                    break;
                case 'd':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 오른쪽 이동.");
                	moveRight.run(this, key,  true);
                    break;
                case 's':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 아래로 이동. top 변화 전 : " + top);
                	if(moveDown.run(this, key,  true) == true) tetrisState = TetrisState.NewBlock;	// 아래로 보내보고, 충돌이 있다면 새 블록 상태로.
                    if(currentDebugLevel >= debugLevel2) System.out.println("블록 아래로 이동. top 변화 후: " + top);
                    break;
                case 'w':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록을 회전시킵니다.");
                	rotateCW.run(this, key, true);
                	if(currentDebugLevel >= debugLevel3) System.out.println("idxBlockDegree : " + idxBlockDegree);
                    break;
                case ' ':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록을 끝까지 내립니다.");
                	while(moveDown.run(this, key, false) == false);	// 바닥 충돌을 안했다면 계속 내린다. 화면 업데이트는 하지 않음.
                	moveDown.run(this, key,  true);
                	tetrisState = TetrisState.NewBlock;
                    break;
                default :
                	// 잘못된 key의 경우 이쪽이 실행된다.
                	if( (0 <= key - '0') && ( key - '0' < nBlockTypes)){ // 숫자의 경우는 새로운 블록을 요청이니 제외함.
                		if(currentDebugLevel >= debugLevel2) System.out.println("BlockType이 입력으로 들어옴");
                	}else{	// 숫자가 아닌 경우는 State를 Error로 변경함.
	                	if(currentDebugLevel >= debugLevel2) System.out.println("잘못된 key의 입력, key : " + key);
	                	tetrisState = TetrisState.Error;
	                	if(currentDebugLevel >= debugLevel3) System.out.println("accept 처리 후 State : " + tetrisState);
	                	return tetrisState;	// 에러인 경우에는 아래를 실행하지 않고 끝내자.
                	}
            }
        	if(currentDebugLevel >= debugLevel3) System.out.println("accept 처리 후 State : " + tetrisState);
            return tetrisState;
    }    
    
	public int findFullLine(Matrix screen){
		if(currentDebugLevel >= debugLevel3) System.out.println("iScreenDy : " + this.iScreenDy); 
		if(currentDebugLevel >= debugLevel3) System.out.println("iScreenDw : " + this.iScreenDw); 
    	for(int i = this.iScreenDy -1; i >= 0; i--){	// 바닥에서 위로 올라오며 검사한다.
    		if(currentDebugLevel >= debugLevel3) System.out.println("풀라인 검사 : " + i + "번 행." );
    		boolean fullLineFlag = true; // FullLine이 검출되었는가 검사하는 flag
    		if(currentDebugLevel >= debugLevel3) System.out.println("iScreenDx : " + iScreenDx); 
    		for(int j = this.iScreenDw; j < this.iScreenDx + this.iScreenDw; j++ ){
        		if(currentDebugLevel >= debugLevel3) System.out.println("j : " + j );
    			if(screen.get_array()[i][j] == 0){
    				if(currentDebugLevel >= debugLevel3) System.out.println("FullLine 아님." );
    				fullLineFlag = false;
    			}
    		}
    		if(currentDebugLevel >= debugLevel3) System.out.println(i + "번 행은 FullLine? : " + fullLineFlag);
    		if(fullLineFlag == true) return i;	// 가장 먼저 만나는 FullLine을 리턴한다.
    	}
    	return -1; // -1을 리턴하는 경우라면 FullLine이 없다는 것임.
    }
	
	public Matrix fullLineDelete(Matrix screen){
		// 여기에 FullLineDetect
		if(currentDebugLevel >= debugLevel3) System.out.println("fullLineDelete");
		int fullLine = findFullLine(screen);
		if(currentDebugLevel >= debugLevel3) System.out.println("FullLine검사, 해당되는 라인(-1이라면 없음) : " + fullLine);
		// findFullLine 함수는 fullLine인 줄의 number를 리턴함. fullLine이 없다면 -1을 리턴함.
		
		while(fullLine > 0){	// fullLine이 검출된 경우, 루프를 돌면서 FullLine이 사라질 때까지 검사.
			try{
				Matrix tempBlk;
				tempBlk = screen.clip(0, 0, fullLine, 2*iScreenDw + iScreenDx);	 // 0(맨 위) ~ fullLine(아래) 모두 잘라낸다. 벽 포함.
				// 자른 블록 표시.
		        if(currentDebugLevel >= debugLevel3) tempBlk.print();	
		        screen.paste(tempBlk, 1, 0);	// 잘랐던 블록들을 붙여넣는다. 벽 포함.
		                
		        // 맨 윗줄 처리와 관련된 부분이다.
		        if(currentDebugLevel >= debugLevel3) System.out.println("맨 윗줄 처리.");
		        
		        // 예) Matrix(1,10) , 0 0 0 0 0 0 0 0 0 0, 빈 라인 만들기 위함.  
		        int[][] emptyLine = new int[1][iScreenDx];
		        for(int i = 0; i < iScreenDx; i++) emptyLine[0][i] = 0;
		        
		        Matrix emptyLineMatrix = new Matrix(emptyLine);
		        if(currentDebugLevel >= debugLevel3) emptyLineMatrix.print();
		        screen.paste(emptyLineMatrix, 0, iScreenDw); // 빈 라인을 맨 윗줄에 붙인다.	
		        
		        // 한번에 여러 줄이 삭제될 수도 있음. 따라서 계속 검사.
		        fullLine = findFullLine(screen); 
		    	if(currentDebugLevel >= debugLevel3) System.out.println("FullLine검사, 해당되는 라인(-1이라면 없음) : " + fullLine);
		    	// findFullLine 함수는 fullLine인 줄의 number를 리턴함. fullLine이 없다면 -1을 리턴함.
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return screen;
	}
}

class TetrisException extends Exception {
	public TetrisException() { super("Tetris Exception"); }
	public TetrisException(String msg) { super(msg); }
}

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
			t.oScreen = t.fullLineDelete(t.oScreen);

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