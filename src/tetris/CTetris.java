package tetris;

public class CTetris extends Tetris{
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
	
	interface CActionHandler extends ActionHandler{
		public void run(CTetris ct, char key) throws Exception;
	}
	
	class OnCLeft implements CActionHandler {
		public void run(CTetris ct, char key) { ct.left = ct.left - 1; }

		@Override
		public void run(Tetris t, char key) throws Exception {
			// TODO Auto-generated method stub
		}
	}
	
	class OnCRight implements CActionHandler {
		public void run(CTetris ct, char key) { ct.left = ct.left + 1; }

		@Override
		public void run(Tetris t, char key) throws Exception {
			// TODO Auto-generated method stub
		}
	}
	
	class OnCDown implements CActionHandler {
		public void run(CTetris ct, char key) { ct.top = ct.top + 1; }

		@Override
		public void run(Tetris t, char key) throws Exception {
			// TODO Auto-generated method stub
		}
	}
	
	class OnCUp implements CActionHandler {
		public void run(CTetris ct, char key) { ct.top = ct.top - 1; }

		@Override
		public void run(Tetris t, char key) throws Exception {
			// TODO Auto-generated method stub
		}
	}
	
	class OnCCW implements CActionHandler {		// 이름 주의! -> C가 하나 더 붙어서 OnCCW 지만 시계방향 회전임.
		public void run(CTetris ct, char key) { 
			ct.idxBlockDegree = (ct.idxBlockDegree + 1) % ct.nBlockDegrees;
			ct.currBlk = ct.setOfBlockObjects[ct.idxBlockType][ct.idxBlockDegree]; 
			}

		@Override
		public void run(Tetris t, char key) throws Exception {
			// TODO Auto-generated method stub
		}
	}
	
	class OnCCCW implements CActionHandler {	// 이름 주의!
		public void run(CTetris ct, char key) { 
			ct.idxBlockDegree = (ct.idxBlockDegree + 3) % ct.nBlockDegrees;
			ct.currBlk = ct.setOfBlockObjects[ct.idxBlockType][ct.idxBlockDegree];
		}

		@Override
		public void run(Tetris t, char key) throws Exception {
			// TODO Auto-generated method stub
		}
	}
	
	class OnCNewBlock implements CActionHandler {
		public void run(CTetris ct, char key) throws MatrixException { 
			// ???
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
		public void run(Tetris t, char key) throws Exception {
			// TODO Auto-generated method stub
		}
	}
	
	class OnCFinished implements CActionHandler {
		public void run(CTetris ct, char key) { 
			System.out.println("OnFinished.run(); called");
		}

		@Override
		public void run(Tetris t, char key) throws Exception {
			// TODO Auto-generated method stub
		}
	}
}