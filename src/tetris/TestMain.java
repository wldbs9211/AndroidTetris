package tetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class TestMain {
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
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static String line = null;
    private static int nKeys = 0;
  
    private static int[][][][] setOfBlockArrays = { // [7][4][?][?]  -> [종류][회전][가로][세로] 
    		{
    			{
    				{0,0,0,0},
    				{0,0,0,0},
    				{1,1,1,1},
    				{0,0,0,0},
    			},
    			{
    				{0,1,0,0},
    				{0,1,0,0},
    				{0,1,0,0},
    				{0,1,0,0},
    			},
    			{
    				{0,0,0,0},
    				{0,0,0,0},
    				{1,1,1,1},
    				{0,0,0,0},
    			},
    			{
    				{0,1,0,0},
    				{0,1,0,0},
    				{0,1,0,0},
    				{0,1,0,0},
    			}
    		},
    		{
    			{
    				{0,0,0},
        			{0,1,0},
        			{1,1,1},
    			},
    			{
    				{0,1,0},
        			{0,1,1},
        			{0,1,0},
    			},
    			{
    				{0,0,0},
        			{1,1,1},
        			{0,1,0},
    			},
    			{
    				{0,1,0},
        			{1,1,0},
        			{0,1,0},
    			}
    		},
    		{
    			{
    				{1,0,0},
        			{1,1,1},
        			{0,0,0},
    			},
    			{
    				{0,1,1},
        			{0,1,0},
        			{0,1,0},
    			},
    			{
    				{0,0,0},
        			{1,1,1},
        			{0,0,1},
    			},
    			{
    				{0,1,0},
        			{0,1,0},
        			{1,1,0},
    			}
    		},
    		{
    			{
    				{0,0,1},
    				{1,1,1},
    				{0,0,0},
    			},
    			{
    				{0,1,0},
    				{0,1,0},
    				{0,1,1},
    			},
    			{
    				{0,0,0},
    				{1,1,1},
    				{1,0,0},
    			},
    			{
    				{1,1,0},
    				{0,1,0},
    				{0,1,0},
    			}
    		},
    		{
    			{
    				{1,1},
    	    		{1,1},
    			},
    			{
    				{1,1},
    	    		{1,1},
    			},
    			{
    				{1,1},
    	    		{1,1},
    			},
    			{
    				{1,1},
    	    		{1,1},
    			}
    		},
    		{
    			{
    				{0,1,1},
    	    		{1,1,0},
    	    		{0,0,0},
    			},
    			{
    				{0,1,0},
    	    		{0,1,1},
    	    		{0,0,1},
    			},
    			{
    				{0,0,0},
    	    		{0,1,1},
    	    		{1,1,0},
    			},
    			{
    				{1,0,0},
    	    		{1,1,0},
    	    		{0,1,0},
    			}
    		},
    		{
    			{
    				{0,0,0},
    	    		{1,1,0},
    	    		{0,1,1},
    			},
    			{
    	    		{0,0,1},
    	    		{0,1,1},
    	    		{0,1,0},
    			},
    			{
    				{0,0,0},
    	    		{1,1,0},
    	    		{0,1,1},
    			},
    			{
    				{0,1,0},
    	    		{1,1,0},
    	    		{1,0,0},
    			}
    		}
    };
    
    private static int[][][][] setOfCBlockArrays = { // [7][4][?][?]  -> [종류][회전][가로][세로] 
    		{
    			{
    				{0,0,0,0},
    				{0,0,0,0},
    				{2,2,2,2},
    				{0,0,0,0},
    			},
    			{
    				{0,2,0,0},
    				{0,2,0,0},
    				{0,2,0,0},
    				{0,2,0,0},
    			},
    			{
    				{0,0,0,0},
    				{0,0,0,0},
    				{2,2,2,2},
    				{0,0,0,0},
    			},
    			{
    				{0,2,0,0},
    				{0,2,0,0},
    				{0,2,0,0},
    				{0,2,0,0},
    			}
    		},
    		{
    			{
    				{0,0,0},
        			{0,3,0},
        			{3,3,3},
    			},
    			{
    				{0,3,0},
        			{0,3,3},
        			{0,3,0},
    			},
    			{
    				{0,0,0},
        			{3,3,3},
        			{0,3,0},
    			},
    			{
    				{0,3,0},
        			{3,3,0},
        			{0,3,0},
    			}
    		},
    		{
    			{
    				{4,0,0},
        			{4,4,4},
        			{0,0,0},
    			},
    			{
    				{0,4,4},
        			{0,4,0},
        			{0,4,0},
    			},
    			{
    				{0,0,0},
        			{4,4,4},
        			{0,0,4},
    			},
    			{
    				{0,4,0},
        			{0,4,0},
        			{4,4,0},
    			}
    		},
    		{
    			{
    				{0,0,5},
    				{5,5,5},
    				{0,0,0},
    			},
    			{
    				{0,5,0},
    				{0,5,0},
    				{0,5,5},
    			},
    			{
    				{0,0,0},
    				{5,5,5},
    				{5,0,0},
    			},
    			{
    				{5,5,0},
    				{0,5,0},
    				{0,5,0},
    			}
    		},
    		{
    			{
    				{6,6},
    	    		{6,6},
    			},
    			{
    				{6,6},
    	    		{6,6},
    			},
    			{
    				{6,6},
    	    		{6,6},
    			},
    			{
    				{6,6},
    	    		{6,6},
    			}
    		},
    		{
    			{
    				{0,7,7},
    	    		{7,7,0},
    	    		{0,0,0},
    			},
    			{
    				{0,7,0},
    	    		{0,7,7},
    	    		{0,0,7},
    			},
    			{
    				{0,0,0},
    	    		{0,7,7},
    	    		{7,7,0},
    			},
    			{
    				{7,0,0},
    	    		{7,7,0},
    	    		{0,7,0},
    			}
    		},
    		{
    			{
    				{0,0,0},
    	    		{8,8,0},
    	    		{0,8,8},
    			},
    			{
    	    		{0,0,8},
    	    		{0,8,8},
    	    		{0,8,0},
    			},
    			{
    				{0,0,0},
    	    		{8,8,0},
    	    		{0,8,8},
    			},
    			{
    				{0,8,0},
    	    		{8,8,0},
    	    		{8,0,0},
    			}
    		}
    };
    private static char getKey() throws IOException {
        char ch;
        if (nKeys != 0) {
            ch = line.charAt(line.length() - nKeys);
            nKeys--;
            return ch;
        }
        do {
            line = br.readLine();
            nKeys = line.length();
        } while (nKeys == 0);
        ch = line.charAt(0);
        nKeys--;
        return ch;
    }
	
    public static void main(String[] args) throws Exception {
    	// 손가락을 만드는 과정. 자유롭게 기능을 바꿀 수 있도록..
    	OnCLeft myOnCLeft = new OnCLeft(){
        	public void run(CTetris ct, char key) { ct.left = ct.left - 1; }
        };
    	OnCRight myOnCRight = new OnCRight(){
        	public void run(CTetris ct, char key) { ct.left = ct.left + 1; }
        };
    	OnCDown myOnCDown = new OnCDown(){
        	public void run(CTetris ct, char key) { ct.top = ct.top + 1; }
        };
    	OnCUp myOnCUp = new OnCUp(){
    		public void run(CTetris ct, char key) { ct.top = ct.top - 1; }
        };
    	OnColorCW myOnColorCW = new OnColorCW(){
        	public void run(CTetris ct, char key) {
        		ct.idxBlockDegree = (ct.idxBlockDegree + 1) % ct.nBlockDegrees;
        		ct.currBlk = ct.setOfBlockObjects[ct.idxBlockType][ct.idxBlockDegree];
        	}
        };
    	OnColorCCW myOnColorCCW = new OnColorCCW(){
    		public void run(CTetris ct, char key) {
        		ct.idxBlockDegree = (ct.idxBlockDegree + 3) % ct.nBlockDegrees;
        		ct.currBlk = ct.setOfBlockObjects[ct.idxBlockType][ct.idxBlockDegree];
        	}
        };
    	OnCNewBlock myOnCNewBlock = new OnCNewBlock() {
    		public void run(CTetris ct, char key) throws MatrixException{
        		if(ct.isJustStarted == false)	// 첫 시작이 아닌 경우에, 새 블록이 필요하다면 fullLineDelete를 진행한다.
        			fullLineDelete(ct);	// 이전과는 다르게 아래 새롭게 만든 fullLineDelete를 사용함.
        		ct.isJustStarted = false;
        		
        		ct.iScreen = new Matrix(ct.oScreen);
        		ct.top = 0;
        		ct.left = ct.iScreenDw + ct.iScreenDx/2 - 2;
        		ct.idxBlockType = key - '0';
        		ct.idxBlockDegree = 0;
        		ct.currBlk = ct.setOfBlockObjects[ct.idxBlockType][ct.idxBlockDegree];        		
        	}
    		
    		//deltefullLine
    		public void fullLineDelete(CTetris ct){
    			int fullLine = ct.findFullLine(ct.oScreen);
    			while(fullLine > 0){
    				try{
    					Matrix tempBlk;
    					tempBlk = ct.oScreen.clip(0, 0, fullLine, 2*ct.iScreenDw + ct.iScreenDx);	
    					ct.oScreen.paste(tempBlk, 1, 0);	  
    			        int[][] emptyLine = new int[1][ct.iScreenDx];
    			        for(int i = 0; i < ct.iScreenDx; i++) emptyLine[0][i] = 0;
    			        Matrix emptyLineMatrix = new Matrix(emptyLine);
    			        ct.oScreen.paste(emptyLineMatrix, 0, ct.iScreenDw); 	
    			        fullLine = ct.findFullLine(ct.oScreen); 
    				}catch(Exception e){
    					System.out.println(e);
    				}
    			}
    			return ;
    		}
    	};
    	
    	OnCFinished myOnCFinished = new OnCFinished() {
    		public void run(CTetris ct, char key){
    			System.out.println("OnFinished.run(); called");
    		}
    	};
    	
        char key;
        int idxType;
        TetrisState state;
        CTetris.init(setOfBlockArrays, setOfCBlockArrays);
        CTetris board = new CTetris(15, 10, setOfBlockArrays);
        
        board.setOnLeftListener(myOnCLeft);
        board.setOnRightListener(myOnCRight);
        board.setOnDownListener(myOnCDown);
        board.setOnUpListener(myOnCUp);
        board.setOnCWListener(myOnColorCW);
        board.setOnCCWListener(myOnColorCCW);
        board.setOnNewBlockListener(myOnCNewBlock);
        board.setOnFinishedListener(myOnCFinished);
        
        Random random = new Random();
        idxType = random.nextInt(7);
        key = (char) ('0' + random.nextInt(7));
        state = board.accept(key);
        //board2.accept('0', idxType);	//board , board2에 동일한 key 전달
        board.printScreen(); System.out.println();
        //board2.printScreen(); System.out.println();
        
        while((key = getKey()) != 'q'){
        	state = board.accept(key);
        	//board2.accept(key, idxType);	//board , board2에 동일한 key 전달
        	
        	board.printScreen(); System.out.println();
        	//board2.printScreen(); System.out.println();
        	
        	// 새로운 블록이 필요하지 않다면 idxType을 계속 저장하여 유지한다. degree는 Tetris 클래스에서 내부적으로 저장.
        	if (state == TetrisState.NewBlock) {
        		key = (char) ('0' + random.nextInt(7));
        		state = board.accept(key);
        		//board2.accept('0', idxType); //board , board2에 동일한 key 전달
        		
        		board.printScreen(); System.out.println();
        		//board2.printScreen(); System.out.println();
        		if (state == TetrisState.Finished){ // 새 블록이 필요한 상태에서 또 중복되었다면 게임종료.
        			System.out.println("Game Over!");
                	System.exit(0);
        		}
        	}
        }
        System.out.println("Program Terminated!");
    }
}

