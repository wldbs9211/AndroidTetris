package tetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Tetris {
	/*
	 * 디버그 레벨에 따라서 출력여부를 결정한다.
	 * level이 높을수록 더 자세하게 정보를 출력한다.
	 * 예) currentDebugLevel의 값이 3이면, 모든 정보를 출력한다. 
	 * 예) currentDebugLevel의 값이 2이면, debugLevel 1 ~ 2까지의 정보를 출력한다. (특정 이벤트 및 흐름)
	 * 예) currentDebugLevel의 값이 1이면, debugLevel 1만 출력 ( 프로그램의 흐름에 대한 정보만 ) 
	 */
	private final static int currentDebugLevel = 0;	// 현재 디버그 레벨.
	private final static int debugLevel1 = 1;	// 프로그램의 흐름에 대한 정보. 
	private final static int debugLevel2 = 2;	// 프로그램에서 어떠한 이벤트에 대한 정보.
	private final static int debugLevel3 = 3;	// 특정 이벤트가 발생한 상황에서 변수의 변화 등에 대한 정보.
	
	private final static int numberOfBlockType = 7;	// 블록의 종류
	
	private static int iScreenDy = 15;
    private static int iScreenDx = 10;
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
    				{0,0,0,0},
    				{0,0,0,0},
    				{1,1,1,1},
    				{0,0,0,0},
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
    
    public static void init(int[][][][] setOfBlkArrays) throws MatrixException{
    	int nTypes = setOfBlkArrays.length;
    	int nDegrees = setOfBlkArrays[0].length;
    	
    	setOfBlockObjects = new Matrix[nTypes][nDegrees];
    	
    	for(int t = 0; t < nTypes; t++){
    		for(int d = 0; d < nDegrees; d++){
    			setOfBlockObjects[t][d] = new Matrix(setOfBlkArrays[t][d]);
    		}
    	}
    }
    
    public Tetris(int cy, int cx) throws MatrixException{	// 생성자에서 무엇을 초기화하는가 .. 보기
    	iScreenDy = cy;
    	iScreenDx = cx;
    	iScreen = new Matrix(createArrayScreen(iScreenDy,iScreenDx, iScreenDw));
    	oScreen = new Matrix(iScreen);
        top = 0;
        left = iScreenDw + iScreenDx/2 - 2;
        
    }
    

    /*
     * accept의 2가지 인자.
     * 1. 키 값 -> key
     * 2. 난수 발생 -> idxType
     * 난수를 발생하는 것은 Tetris 클래스 내에서 처리하지 않고, 다른 곳에서 넘겨줌.
     * 따라서 이 함수에서는 외부의 들어온 입력에 대해서 처리를 진행함.
     * 새로운 블록이 필요하다면(newBlockNeeded) 이것을 리턴하여, 외부에서 또 랜덤으로 난수 생성 후 넘기도록 만들자.
     */
    public boolean accept(char key, int idxType) throws Exception {
      
        boolean newBlockNeeded = false;

        // 게임의 상태니 위로 올림.
        //int top = 0;
        //int left = iScreenDw + iScreenDx/2 - 2;
        //int[][] arrayScreen = createArrayScreen(iScreenDy, iScreenDx, iScreenDw);
        //char key;
        //Matrix iScreen = new Matrix(arrayScreen);
        
        blkType = idxType; // 외부에서 받아온 	
        currBlk = setOfBlockObjects[blkType][blkDegree];
        
        // 기존의 난수 생성은 삭제한다.
        //Random random = new Random(); // 다음 블록을 결정할 난수생성기
        //int idxBlockType = random.nextInt(numberOfBlockType);
        
        if(currentDebugLevel >= debugLevel3){
        	System.out.println("다음 블록 번호 : " + blkType);
        }
        //다음 블록을 생성한다. 
        Matrix currBlk = setOfBlockObjects[blkType][blkDegree];	
        Matrix tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
        tempBlk = tempBlk.add(currBlk);
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
                	currBlk = setOfBlockObjects[idxType][blkDegree];
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
                    	currBlk = setOfBlockObjects[idxType][blkDegree];
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
                //tempBlk = oScreen.clip(0, iScreenDw, fullLine, iScreenDw + iScreenDx);	 // 0(맨 위) ~ fullLine(아래) 모두 잘라낸다. 벽은 복사 안함.
        		tempBlk = oScreen.clip(0, 0, fullLine, 2*iScreenDw + iScreenDx);	 // 0(맨 위) ~ fullLine(아래) 모두 잘라낸다. 벽 포함.
        		// 자른 블록 표시.
                if(currentDebugLevel >= debugLevel3) tempBlk.print();	
                // 붙이는 작업.
                //oScreen.paste(tempBlk, 1, iScreenDw);	// 잘랐던 블록들을 붙여넣는다. 한칸 아래로 가니까 인자 2번 1, left는 iScreenDw
                oScreen.paste(tempBlk, 1, 0);	// 잘랐던 블록들을 붙여넣는다. 벽 포함.
                //printScreen(oScreen); System.out.println();        
                
                
                // 맨 윗줄 처리와 관련된 부분이다.
                if(currentDebugLevel >= debugLevel3) System.out.println("맨 윗줄 처리.");
                // ?? 한칸 내려오면 맨 윗칸은 모두 0으로 바꿔야함.
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
        	
        	if(newBlockNeeded){
        		iScreen = new Matrix(oScreen);
        		top = 0;
                left = iScreenDw + iScreenDx/2 - 2;
        	}
        	
            return newBlockNeeded;
    }
}

