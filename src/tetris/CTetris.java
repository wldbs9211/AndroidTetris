package tetris;

public class CTetris extends Tetris{
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
	
	// 컬러용 스크린 추가
	private Matrix iCScreen = null;	
	private Matrix oCScreen = null;
	
	private static Matrix[][] setOfCBlockObjects;
	
	public CTetris(int cy, int cx, int[][][][] setOfBlkArrays) throws Exception {
		super(cy, cx, setOfBlkArrays);
		// TODO Auto-generated constructor stub
		iCScreen = new Matrix(createArrayScreen(iScreenDy,iScreenDx, iScreenDw));	// 추가
	}
	
	public static void init(int[][][][] setOfBlkArrays, int[][][][] setOfCBlkArrays) throws MatrixException{
    	nBlockTypes = setOfBlkArrays.length;
    	nBlockDegrees = setOfBlkArrays[0].length;
    	
    	setOfBlockObjects = new Matrix[nBlockTypes][nBlockDegrees];
    	setOfCBlockObjects = new Matrix[nBlockTypes][nBlockDegrees];	// 추가
    	
    	for(int t = 0; t < nBlockTypes; t++){
    		for(int d = 0; d < nBlockDegrees; d++){
    			setOfBlockObjects[t][d] = new Matrix(setOfBlkArrays[t][d]);
    			setOfCBlockObjects[t][d] = new Matrix(setOfCBlkArrays[t][d]);	// 추가	
    		}
    	}
    }
	
	// 모양을 다르게 출력
	public void printCScreen() {
    	Matrix screen = iCScreen;
        int dy = iCScreen.get_dy();
        int dx = iCScreen.get_dx();
        int dw = iScreenDw;
        int array[][] = iCScreen.get_array();
        for (int y = 0; y < dy - dw + 1; y++) {
            for (int x = dw -1; x < dx - dw + 1; x++) {
            	// 여기에 블록의 타입별로 다르게 뷰에 뿌려줘야함.
            	printCharacter(array[y][x]);	// 변경
            	/* 기존
                if (array[y][x] == 0) System.out.print("□ ");
                else if (array[y][x] == 1) System.out.print("■ ");
                else System.out.print("x ");
                */
            }
            System.out.println();
        }
    }
	// ◀ ▷ ▶ ♤ ♠ ♡ ♥ ♧ ♣ ⊙ ◈ ◘ ◙ ⌂ ☺ ☻ 
	public void printCharacter(int blockType){	// 추가
		switch(blockType){	// 블록별로 다르게 출력
		case 0 :	// 빈 경우는 기존 것과 동일하게 출력
			System.out.print("ㅁ ");
			break;
		case 1 :	// 벽
			System.out.print("◙ ");
			break;
		// 여기서부터 블록임. 7가지 종류 블록 타입 -> 2 ~ 8	
		case 2 :
			System.out.print("◈ ");
			break;
		case 3 :
			System.out.print("♣ ");
			break;
		case 4 :
			System.out.print("♥ ");
			break;
		case 5 :
			System.out.print("☺ ");
			break;
		case 6 :
			System.out.print("⊙ ");	
			break;
		case 7 :
			System.out.print("◈ ");	
			break;
		case 8 :
			System.out.print("◈ ");	
			break;
		default :
			System.out.println("등록되지 않은 블록의 출력, CTetris printCharacter 함수");
		}
	}
	
	private OnCLeft onCLeft = new OnCLeft();
    private OnCRight onCRight = new OnCRight();
    private OnCDown onCDown = new OnCDown();
    private OnCUp onCUp = new OnCUp();
    private OnColorCW onColorCW = new OnColorCW();
    private OnColorCCW onColorCCW = new OnColorCCW();
    private OnCNewBlock onCNewBlock = new OnCNewBlock();
    private OnCFinished onCFinished = new OnCFinished();
	
    class CTetrisAction extends TetrisAction{
    	public CTetrisAction(CActionHandler d, CActionHandler u) {
			super(d, u);
			// TODO Auto-generated constructor stub
			if(currentDebugLevel >= debugLevel3) System.out.println("CTetrisAction 생성자 호출");
		}
    	public boolean run(Tetris t, char key, boolean update) throws Exception{
    		System.out.println("CTetrisAction 호출, key : " + key);
    		
    		boolean anyConflict = false;
    		hDo.run(t, key);
    		Matrix tempBlk; 
    		tempBlk = iScreen.clip(top, left, top+currBlk.get_dy(), left+currBlk.get_dx());
    		tempBlk = tempBlk.add(currBlk);
    		if ((anyConflict = tempBlk.anyGreaterThan(1)) == true){	// 충돌이 있음.
    			hUndo.run(t, key);	// 취소
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
    
    protected void setTetrisActions(){
    	if(currentDebugLevel >= debugLevel3) System.out.println("CTetris의 setTetrisActions() 호출");
    	moveLeft = new CTetrisAction(onCLeft, onCRight);
    	moveRight = new CTetrisAction(onCRight, onCLeft);
    	moveDown = new CTetrisAction(onCDown, onCUp);
    	rotateCW = new CTetrisAction(onColorCW, onColorCCW);
    	insertBlk = new CTetrisAction(onCNewBlock, onCFinished);
    	tetrisActionsInitialized = true;
    }
    
    public void setOnLeftListener(OnCLeft listener) { tetrisActionsInitialized = false; onCLeft = listener; }
    public void setOnRightListener(OnCRight listener) { tetrisActionsInitialized = false; onCRight = listener; }
    public void setOnDownListener(OnCDown listener) { tetrisActionsInitialized = false; onCDown = listener; }
    public void setOnUpListener(OnCUp listener) { tetrisActionsInitialized = false; onCUp = listener; }
    public void setOnCWListener(OnColorCW listener) { tetrisActionsInitialized = false; onColorCW = listener; }
    public void setOnCCWListener(OnColorCCW listener) { tetrisActionsInitialized = false; onColorCCW = listener; }
    public void setOnNewBlockListener(OnCNewBlock listener) { tetrisActionsInitialized = false; onCNewBlock = listener; }
    public void setOnFinishedListener(OnCFinished listener) { tetrisActionsInitialized = false; onCFinished = listener; }
}

interface CActionHandler extends ActionHandler{
	public void run(CTetris ct, char key) throws Exception;	
}

class OnCLeft implements CActionHandler {
	
	@Override
	public void run(CTetris ct, char key) throws Exception { ct.left = ct.left - 1; }

	@Override
	public void run(Tetris t, char key) throws Exception {
		// TODO Auto-generated method stub
	}
}

class OnCRight implements CActionHandler {
	@Override
	public void run(CTetris ct, char key) throws Exception { ct.left = ct.left + 1; }

	@Override
	public void run(Tetris t, char key) throws Exception {
		// TODO Auto-generated method stub
	}
}

class OnCDown implements CActionHandler {
	@Override
	public void run(CTetris ct, char key) throws Exception { ct.top = ct.top + 1; }

	@Override
	public void run(Tetris t, char key) throws Exception {
		// TODO Auto-generated method stub
	}
}

class OnCUp implements CActionHandler {
	@Override
	public void run(CTetris ct, char key) throws Exception { ct.top = ct.top - 1; }

	@Override
	public void run(Tetris t, char key) throws Exception {
		// TODO Auto-generated method stub
	}
}

class OnColorCW implements CActionHandler {		// 이름 주의! -> C가 하나 더 붙어서 OnCCW 지만 시계방향 회전임.
	@Override
	public void run(CTetris ct, char key) throws Exception { 
		ct.idxBlockDegree = (ct.idxBlockDegree + 1) % ct.nBlockDegrees;
		ct.currBlk = ct.setOfBlockObjects[ct.idxBlockType][ct.idxBlockDegree]; 
		}

	@Override
	public void run(Tetris t, char key) throws Exception {
		// TODO Auto-generated method stub
	}
}

class OnColorCCW implements CActionHandler {	// 이름 주의!
	@Override
	public void run(CTetris ct, char key) throws Exception { 
		ct.idxBlockDegree = (ct.idxBlockDegree + 3) % ct.nBlockDegrees;
		ct.currBlk = ct.setOfBlockObjects[ct.idxBlockType][ct.idxBlockDegree];
	}

	@Override
	public void run(Tetris t, char key) throws Exception {
		// TODO Auto-generated method stub
	}
}

class OnCNewBlock implements CActionHandler {
	@Override
	public void run(CTetris ct, char key) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("OnCNewBlock 호출");	// ??
		if(ct.isJustStarted == false)	// 첫 시작이 아닌 경우에, 새 블록이 필요하다면 fullLineDelete를 진행한다.
			ct.oScreen = ct.fullLineDelete(ct.oScreen);

		ct.isJustStarted = false;
		ct.iScreen = new Matrix(ct.oScreen);
		ct.top = 0;
		ct.left = ct.iScreenDw + ct.iScreenDx/2 - 2;
		ct.idxBlockType = key - '0';
		ct.idxBlockDegree = 0;
		ct.currBlk = ct.setOfBlockObjects[ct.idxBlockType][ct.idxBlockDegree];
	}
	
	@Override
	public void run(Tetris t, char key) throws MatrixException{
		System.out.println("test22");
		if(t.isJustStarted == false)	// 첫 시작이 아닌 경우에, 새 블록이 필요하다면 fullLineDelete를 진행한다.
			fullLineDelete(t);	// 이전과는 다르게 아래 새롭게 만든 fullLineDelete를 사용함.
		t.isJustStarted = false;
		
		t.iScreen = new Matrix(t.oScreen);
		t.top = 0;
		t.left = t.iScreenDw + t.iScreenDx/2 - 2;
		t.idxBlockType = key - '0';
		t.idxBlockDegree = 0;
		t.currBlk = t.setOfBlockObjects[t.idxBlockType][t.idxBlockDegree];   
	}
	
	//deltefullLine
	public void fullLineDelete(Tetris t){
		int fullLine = t.findFullLine(t.oScreen);
		while(fullLine > 0){
			try{
				Matrix tempBlk;
				tempBlk = t.oScreen.clip(0, 0, fullLine, 2*t.iScreenDw + t.iScreenDx);	
				t.oScreen.paste(tempBlk, 1, 0);	  
		        int[][] emptyLine = new int[1][t.iScreenDx];
		        for(int i = 0; i < t.iScreenDx; i++) emptyLine[0][i] = 0;
		        Matrix emptyLineMatrix = new Matrix(emptyLine);
		        t.oScreen.paste(emptyLineMatrix, 0, t.iScreenDw); 	
		        fullLine = t.findFullLine(t.oScreen); 
			}catch(Exception e){
				System.out.println(e);
			}
		}
		return ;
	}
}

class OnCFinished implements CActionHandler {
	@Override
	public void run(Tetris t, char key) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void run(CTetris ct, char key) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("OnFinished.run(); called");
	}
}
