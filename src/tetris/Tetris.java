package tetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

enum TetrisState{
	Start(0), Running(1), NewBlock(2), End(3), Error(4);
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
	private final static int currentDebugLevel = 3;	// 현재 디버그 레벨.
	private final static int debugLevel1 = 1;	// 프로그램의 흐름에 대한 정보. 
	private final static int debugLevel2 = 2;	// 프로그램에서 어떠한 이벤트에 대한 정보.
	private final static int debugLevel3 = 3;	// 특정 이벤트가 발생한 상황에서 변수의 변화 등에 대한 정보.
	
	private static int numberOfBlockTypes;	
	private static int numberOfDegrees;	
	
	private static int iScreenDy;
    private static int iScreenDx;
    private static int iScreenDw = 4;
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static String line = null;
    private static int nKeys = 0;
    
    private Matrix iScreen = null;
    private Matrix oScreen = null;
    private Matrix currBlk = null;
    
    private int blkType = 0;
    private int blkDegree = 0;
    private int top;
    private int left;
    
    private TetrisState tetrisState;
	
   /*
    * Matrix iScreen = new Matrix(createArrayScreen(iScreenDy,iScreenDx, iScreenDw));
    * 이 방법은 좋지 않다. 이 함수가 불리는 시점에 createArrayScreen의 인자의 상태는 어떨까.. 생각..
    * 생성자가 더 좋음.
    */
    
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
    private static Matrix[][] setOfBlockObjects;

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
    	numberOfDegrees = setOfBlkArrays[0].length;
    	
    	setOfBlockObjects = new Matrix[numberOfBlockTypes][numberOfDegrees];
    	
    	for(int t = 0; t < numberOfBlockTypes; t++){
    		for(int d = 0; d < numberOfDegrees; d++){
    			setOfBlockObjects[t][d] = new Matrix(setOfBlkArrays[t][d]);
    		}
    	}
    }
    
    public Tetris(int cy, int cx) throws MatrixException{	
    	iScreenDy = cy;
    	iScreenDx = cx;
    	iScreen = new Matrix(createArrayScreen(iScreenDy,iScreenDx, iScreenDw));
    	oScreen = new Matrix(iScreen);
        top = 0;
        left = iScreenDw + iScreenDx/2 - 2;
        tetrisState = TetrisState.Start;
    }
    
    
    /*	입력 : key
     *	기능 : 키가 새로운 블록을 요청하는 키인지, 현재 블록을 조작하는 키인지 판별.
     *		  0 ~ numberOfBlockTypes에 해당하는 숫자가 들어오면 새로운 블록을 요청하는 키임.
     *	출력 : 새로운 블록을 요청하는 키라면 True 리턴, 조작 혹은 잘못된 키라면 False리턴.
     */ 
    public boolean isKeyNewBlock(char key){	
    	int intKeyValue = key - '0';	
    	return (0 <= intKeyValue) && (intKeyValue < numberOfBlockTypes); 
    }
    
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
     * - 결과로 새로운 블록이 필요한지 리턴함. 
     */
    public boolean accept(char key) throws Exception {
        boolean newBlockNeeded = false;
        int idxType;
        
        /*
         *  일단 키가 들어오면 NewBlock(새 블록) / Running(그 외의 키) 로 분류한다.
         *  여기서 예외 키 처리를 해버리면 새로운 키가 추가되는 경우 하나하나 처리가 복잡.. 
         *  Running 상태에서 switch 문을 사용하니 거기서 State Error를 분류한다.  
         */
        if(currentDebugLevel >= debugLevel3) System.out.println("isKeyNewBlock : " + isKeyNewBlock(key));
        if(isKeyNewBlock(key)) tetrisState = TetrisState.NewBlock;	// 새로운 블록을 요청하는 키라면 NewBlock 상태로.
        else tetrisState = TetrisState.Running;	// 그것이 아니라면 Running 상태로 변경하여 조작을 한다.
        
        if(currentDebugLevel >= debugLevel3) System.out.println("tetrisState : " + tetrisState.value() );
        
        switch(tetrisState.value()){	// Start(0), Running(1), NewBlock(2), End(3), Error(4)
        	case 0 :	
        		if(currentDebugLevel >= debugLevel2) System.out.println("Start 상태에 대해 accept 함수를 진행합니다.");
        		break;
        	case 1 :
        		if(currentDebugLevel >= debugLevel2) System.out.println("Running 상태에 대해 accept 함수를 진행합니다.");
        		break;
        	case 2 :
        		if(currentDebugLevel >= debugLevel2) System.out.println("NewBlock 상태에 대해 accept 함수를 진행합니다.");
        		break;
        	case 3 :
        		if(currentDebugLevel >= debugLevel2) System.out.println("End 상태에 대해 accept 함수를 진행합니다.");
        		break;
        	case 4 :
        		if(currentDebugLevel >= debugLevel2) System.out.println("Error 상태에 대해 accept 함수를 진행합니다.");
        		break;
        	default :
        		if(currentDebugLevel >= debugLevel2) System.out.println("잘못된 key 값이 들어왔습니다.");
        		tetrisState = TetrisState.Error;
        		break;
        }
        
        idxType = key - '0';
        if(currentDebugLevel >= debugLevel3) System.out.println("idxType : " + idxType);

        // 게임의 상태니 위로 올림.
        //int top = 0;
        //int left = iScreenDw + iScreenDx/2 - 2;
        //int[][] arrayScreen = createArrayScreen(iScreenDy, iScreenDx, iScreenDw);
        //char key;
        //Matrix iScreen = new Matrix(arrayScreen);
        
        blkType = idxType; // 외부에서 받아온 블록 모양 	
        currBlk = setOfBlockObjects[blkType][blkDegree];
        
        // 기존의 난수 생성은 삭제한다.
        //Random random = new Random(); // 다음 블록을 결정할 난수생성기
        //int idxBlockType = random.nextInt(numberOfBlockType);
        
        if(currentDebugLevel >= debugLevel3) System.out.println("다음 블록 번호 : " + blkType);
        
        //다음 블록을 생성한다. 
        Matrix currBlk = setOfBlockObjects[blkType][blkDegree];	
        Matrix tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
        tempBlk = tempBlk.add(currBlk);

        // 이 부분을 추가하는 이유는 블록이 생성되자마자 다른 블록과 충돌(게임오버 조건) 검사하기 위함임. 여기서 검사하지 않으면 화면에 x자가 출력된다. 아래 부분을 실행 말고 종료.
        if(tempBlk.anyGreaterThan(1)){
        	tetrisState = TetrisState.End;
        	return true;
        }
        
        // Matrix oScreen = new Matrix(iScreen);
        oScreen.paste(tempBlk, top, left);
        //printScreen(); System.out.println();

        //while ((key = getKey()) != 'q') {
            switch (key) {
                case 'a':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 왼쪽 이동.");
                    left--;
                    break;
                case 'd':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 오른쪽 이동.");
                    left++;
                    break;
                case 's':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록 아래로 이동. top 변화 전 : " + top);
                    top++;
                    if(currentDebugLevel >= debugLevel2) System.out.println("블록 아래로 이동. top 변화 후: " + top);
                    break;
                case 'w':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록을 회전시킵니다.");
                	blkDegree = (blkDegree + 1) % 4;
                	if(currentDebugLevel >= debugLevel3) System.out.println("blkDegree : " + blkDegree);
                	currBlk = setOfBlockObjects[blkType][blkDegree];
                    break;
                case ' ':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록을 끝까지 내립니다.");
                	// 바닥에 닿기 전까지 한칸씩 내려가며 충돌하나 비교한다.
                	tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx()); // 블록이 생성될 위치 초기 세팅.
                    tempBlk = tempBlk.add(currBlk); // 현재 블록 넣고.
                    while (!tempBlk.anyGreaterThan(1)){ // 충돌까지 내린다.
                    	top ++;	// 내리고.
                    	tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx()); // 갱신하고.
                        tempBlk = tempBlk.add(currBlk); // 현재 블록 넣고.
                    	if(currentDebugLevel >= debugLevel3) System.out.println("블록을 하나 내려봅니다. top : " + top );
                    }
                    // 여기까지 왔다면 블록은 현재 충돌된 상태임. 아래 최종 작업에서 top을 하나 빼주어야함.
                    break;
                case '0':
                	if(currentDebugLevel >= debugLevel2) System.out.println("처음 시작");
                	break;
                default:
                	if(currentDebugLevel >= debugLevel2) System.out.println("잘못된 키의 입력!");
            }
            tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
            tempBlk = tempBlk.add(currBlk);
            if (tempBlk.anyGreaterThan(1)) {
                switch (key) {
                    case 'a':
                        left++;
                        break;
                    case 'd':
                        left--;
                        break;
                    case 's':
                        top--;
                        newBlockNeeded = true;
                        break;
                    case 'w':
                    	if(currentDebugLevel >= debugLevel2) System.out.println("블록이 회전 과정에서 충돌하였음. 이전으로 돌아감");
                    	blkDegree = blkDegree - 1;	// Degree를 이전으로 돌림.
                    	if(blkDegree == -1) blkDegree = 3; // 회전 : 3 -> 0 , 충돌 : 0 -> -1 케이스니 3으로 되돌림.
                    	currBlk = setOfBlockObjects[blkType][blkDegree];
                        break;
                    case ' ':
                    	// 이미 충돌된 상태임. top을 하나 빼주어 충돌 직전 위치로 이동. 
                    	top--;
                    	if(currentDebugLevel >= debugLevel3) System.out.println("블록이 바닥에 충돌하였음. 최종 top의 값 : " + top);
                    	newBlockNeeded = true;
                        break;
                }
                tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
                tempBlk = tempBlk.add(currBlk);
            }
            oScreen = new Matrix(iScreen);
            oScreen.paste(tempBlk, top, left);
            //printScreen(); System.out.println();
          
            // 여기에 FullLineDetect 
        	int fullLine;
        	fullLine = oScreen.findFullLine(iScreenDw); 
        	if(currentDebugLevel >= debugLevel3) System.out.println("FullLine검사, 해당되는 라인(-1이라면 없음) : " + fullLine);
        	// findFullLine 함수는 fullLine인 줄의 number를 리턴함. fullLine이 없다면 -1을 리턴함.
        	while(fullLine > 0){	// fullLine이 검출된 경우, 루프를 돌면서 FullLine이 사라질 때까지 검사.
        		// 잘라내는 작업.
        		if(currentDebugLevel >= debugLevel3) System.out.println("iScreenDw : " + iScreenDw + " iScreenDx : " + iScreenDx);
                tempBlk = oScreen.clip(0, iScreenDw, fullLine, iScreenDw + iScreenDx);	 // 세로 : 0(맨 위) ~ fullLine(아래) 모두 잘라낸다. 가로 : 벽은 복사 안함.
        		// 겉으로는 동작을 하나 사용하지 않는다.
                // tempBlk = oScreen.clip(0, 0, fullLine, 2*iScreenDw + iScreenDx);	 // 세로 : 0(맨 위) ~ fullLine(아래) 모두 잘라낸다. 가로 : 벽 포함.
                // -> 아래 주석된 부분은 복수 객체 검증에서 에러가 발생. 이유는 iScreenDx가 static이기 때문에 screenDx가 다른 객체가 만들어지면 영향을 줌. 
                
        		// 자른 블록 표시.
                if(currentDebugLevel >= debugLevel3) tempBlk.print();	
                // 붙이는 작업.
                oScreen.paste(tempBlk, 1, iScreenDw);	// 잘랐던 블록들을 붙여넣는다. 한칸 아래로 가니까 인자 2번 1, left는 iScreenDw
        		// 겉으로는 동작을 하나 사용하지 않는다.
                //oScreen.paste(tempBlk, 1, 0);	// 잘랐던 블록들을 붙여넣는다. 벽 포함.
                // -> 아래 주석된 부분은 복수 객체 검증에서 에러가 발생.
                
                //printScreen(oScreen); System.out.println();        
                
                // 맨 윗줄 처리와 관련된 부분이다.
                if(currentDebugLevel >= debugLevel3) System.out.println("맨 윗줄 처리.");
                // 한칸 내려오면 맨 윗칸은 모두 0으로 바꿔야함.
                int[][] emptyLine = createArrayScreen(1,iScreenDx, 0);	// 빈 라인을 생성. 인자 -> dy, dx, dw  
                Matrix emptyLineMatrix = new Matrix(emptyLine);
                if(currentDebugLevel >= debugLevel3) emptyLineMatrix.print();
                oScreen.paste(emptyLineMatrix, 0, iScreenDw); // 빈 라인을 맨 윗줄에 붙인다.	
                // printScreen(); System.out.println();
                
                // 한번에 여러 줄이 삭제될 수도 있음. 따라서 계속 검사.
                fullLine = oScreen.findFullLine(iScreenDw); 
            	if(currentDebugLevel >= debugLevel3) System.out.println("FullLine검사, 해당되는 라인(-1이라면 없음) : " + fullLine);
            	// findFullLine 함수는 fullLine인 줄의 number를 리턴함. fullLine이 없다면 -1을 리턴함.
        	}
        	
        	if(newBlockNeeded){	// 새로운 블록이 필요한 경우라면..
        		iScreen = new Matrix(oScreen);	// 마지막 상태 iScreen에 저장.
        		top = 0;	
                left = iScreenDw + iScreenDx/2 - 2;
                blkDegree = 0;	// 각도 0으로.
        	}
        	
            return newBlockNeeded;
    }
}

