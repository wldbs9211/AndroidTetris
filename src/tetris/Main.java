package tetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {
	/*
	 * 디버그 레벨에 따라서 출력여부를 결정한다.
	 * level이 높을수록 더 자세하게 정보를 출력한다.
	 * 예) currentDebugLevel의 값이 3이면, 모든 정보를 출력한다. 
	 * 예) currentDebugLevel의 값이 2이면, debugLevel 1 ~ 2까지의 정보를 출력한다. (특정 이벤트 및 흐름)
	 * 예) currentDebugLevel의 값이 1이면, debugLevel 1만 출력 ( 프로그램의 흐름에 대한 정보만 ) 
	 * 예) currentDebugLevel의 값이 0이면, 아무것도 출력하지 않음.
	 */
	private final static int currentDebugLevel = 0;	// 현재 디버그 레벨
	private final static int debugLevel1 = 1;	// 프로그램의 흐름에 대한 정보 
	private final static int debugLevel2 = 2;	// 프로그램에서 어떠한 이벤트에 대한 정보
	private final static int debugLevel3 = 3;	// 특정 이벤트가 발생한 상황에서 변수의 변화 등에 대한 정보
	
	private final static int numberOfBlockType = 7;	// 블록의 종류
	
	// 스크린 관련 
	private static int iScreenDy = 15;	// 스크린의 y축
    private static int iScreenDx = 10;	// 스크린의 x축
    private static int iScreenDw = 4;	// 벽 두께
	
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
    private static Matrix[][] setOfBlockObjects;
    
    /*
     * 입력 : 4차원 int형 배열 setOfBlkArrays, 블록 종류별로 각도 변화에 따른 모양이 입력되어있음.
     * 기능 : 각각의 2차원 int형 배열 -> Matrix로 변환함.
     * 		 이것을 Matrix[블록타입][각도] 형태로 2차원 Matrix형 배열에 저장.
     * 출력 : 2차원 Matrix 배열 
     */
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
    /*
    final static int[][] blockType0_degree0 = {
    		{0,0,0},
    		{0,1,0},
    		{1,1,1}
    };
    
    final static int[][] blockType0_degree1 = {
    		{0,1,0},
    		{0,1,1},
    		{0,1,0}
    };
    
    final static int[][] blockType0_degree2 = {
    		{0,0,0},
    		{1,1,1},
    		{0,1,0}
    };
    
    final static int[][] blockType0_degree3 = {
    		{0,1,0},
    		{1,1,0},
    		{0,1,0}
    };
    
    final static int[][] blockType1_degree0 = {	
    		{1,0,0},
    		{1,1,1},
    		{0,0,0}
    };
    
    final static int[][] blockType1_degree1 = {	
    		{0,1,1},
    		{0,1,0},
    		{0,1,0}
    };
    
    final static int[][] blockType1_degree2 = {	
    		{0,0,0},
    		{1,1,1},
    		{0,0,1}
    };
    
    final static int[][] blockType1_degree3 = {	
    		{0,1,0},
    		{0,1,0},
    		{1,1,0}
    };
    
    final static int[][] blockType2_degree0 = {	
    		{0,0,1},
    		{1,1,1},
    		{0,0,0}
    };
    
    final static int[][] blockType2_degree1 = {	
    		{0,1,0},
    		{0,1,0},
    		{0,1,1}
    };
    
    final static int[][] blockType2_degree2 = {	
    		{0,0,0},
    		{1,1,1},
    		{1,0,0}
    };
    
    final static int[][] blockType2_degree3 = {	
    		{1,1,0},
    		{0,1,0},
    		{0,1,0}
    };
    
    final static int[][] blockType3_degree0 = {	
    		{0,0,0,0},
    		{0,0,0,0},
    		{1,1,1,1},
    		{0,0,0,0}
    };
    
    final static int[][] blockType3_degree1 = {	
    		{0,1,0,0},
    		{0,1,0,0},
    		{0,1,0,0},
    		{0,1,0,0}
    };
    
    final static int[][] blockType3_degree2 = {	
    		{0,0,0,0},
    		{0,0,0,0},
    		{1,1,1,1},
    		{0,0,0,0}
    };
    
    final static int[][] blockType3_degree3 = {	
    		{0,1,0,0},
    		{0,1,0,0},
    		{0,1,0,0},
    		{0,1,0,0}
    };
    
    final static int[][] blockType4_degree0 = {	
    		{1,1},
    		{1,1}
    };
    
    final static int[][] blockType4_degree1 = {	
    		{1,1},
    		{1,1}
    };
    
    final static int[][] blockType4_degree2 = {	
    		{1,1},
    		{1,1}
    };
    
    final static int[][] blockType4_degree3 = {	
    		{1,1},
    		{1,1}
    };
    
    final static int[][] blockType5_degree0 = {
    		{0,1,1},
    		{1,1,0},
    		{0,0,0}
    };
    
    final static int[][] blockType5_degree1 = {
    		{0,1,0},
    		{0,1,1},
    		{0,0,1}
    };
    
    final static int[][] blockType5_degree2 = {
    		{0,0,0},
    		{0,1,1},
    		{1,1,0}
    };
    
    final static int[][] blockType5_degree3 = {
    		{1,0,0},
    		{1,1,0},
    		{0,1,0}
    };
    
    final static int[][] blockType6_degree0 = {
    		{0,0,0},
    		{1,1,0},
    		{0,1,1}
    };
    
    final static int[][] blockType6_degree1 = {
    		{0,0,1},
    		{0,1,1},
    		{0,1,0}
    };
    
    final static int[][] blockType6_degree2 = {
    		{0,0,0},
    		{1,1,0},
    		{0,1,1}
    };
    
    final static int[][] blockType6_degree3 = {
    		{0,1,0},
    		{1,1,0},
    		{1,0,0}
    };
    */
    
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
    
    public static void printScreen(Matrix screen) {
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

    public static void main(String[] args) throws Exception {
        /*
    	Matrix[][] setOfBlockObjects = {
        	{ new Matrix(blockType0_degree0), new Matrix(blockType0_degree1), new Matrix(blockType0_degree2), new Matrix(blockType0_degree3) },
        	{ new Matrix(blockType1_degree0), new Matrix(blockType1_degree1), new Matrix(blockType1_degree2), new Matrix(blockType1_degree3) },	
        	{ new Matrix(blockType2_degree0), new Matrix(blockType2_degree1), new Matrix(blockType2_degree2), new Matrix(blockType2_degree3) },
        	{ new Matrix(blockType3_degree0), new Matrix(blockType3_degree1), new Matrix(blockType3_degree2), new Matrix(blockType3_degree3) },
        	{ new Matrix(blockType4_degree0), new Matrix(blockType4_degree1), new Matrix(blockType4_degree2), new Matrix(blockType4_degree3) },
        	{ new Matrix(blockType5_degree0), new Matrix(blockType5_degree1), new Matrix(blockType5_degree2), new Matrix(blockType5_degree3) },
        	{ new Matrix(blockType6_degree0), new Matrix(blockType6_degree1), new Matrix(blockType6_degree2), new Matrix(blockType6_degree3) }
        };
        */
        init(setOfBlockArrays);	// 4차원 int형으로 선언된 배열 -> 2차원 Matrix 변환
    	
        boolean newBlockNeeded = false;
        int top = 0;
        int left = iScreenDw + iScreenDx/2 - 2;
        int[][] arrayScreen = createArrayScreen(iScreenDy, iScreenDx, iScreenDw);
        char key;
        Matrix iScreen = new Matrix(arrayScreen);
        
        Random random = new Random(); // 다음 블록을 결정할 난수생성기
        int idxBlockType = random.nextInt(numberOfBlockType);
        int idxBlockDegree = 0;	// 각도는 처음에 0으로 블록을 생성함.
        if(currentDebugLevel >= debugLevel3) System.out.println("다음 블록 번호 : " + idxBlockType);
        //다음 블록을 생성한다. 
        Matrix currBlk = setOfBlockObjects[idxBlockType][idxBlockDegree]; 
        Matrix tempBlk = iScreen.clip(top, left, top+currBlk.get_dy(), left+currBlk.get_dx());
        tempBlk = tempBlk.add(currBlk);
        Matrix oScreen = new Matrix(iScreen);
        oScreen.paste(tempBlk, top, left);
        printScreen(oScreen); System.out.println();

        while ((key = getKey()) != 'q') {
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
                	if(currentDebugLevel >= debugLevel3) System.out.println("블록 아래로 이동. top 변화 전 : " + top);
                    top++;
                    if(currentDebugLevel >= debugLevel3) System.out.println("블록 아래로 이동. top 변화 후: " + top);
                    break;
                case 'w':
                	if(currentDebugLevel >= debugLevel2) System.out.println("블록을 회전시킵니다.");
                	idxBlockDegree = (idxBlockDegree + 1) % 4; // degree 값은 0 ~ 3 을 반복하도록 만듬.
                	currBlk = setOfBlockObjects[idxBlockType][idxBlockDegree];
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
                    	idxBlockDegree = idxBlockDegree - 1;	// Degree를 이전으로 돌림.
                    	if(idxBlockDegree == -1) idxBlockDegree = 3; // 회전 : 3 -> 0 , 충돌 : 0 -> -1 케이스니 3으로 되돌림.
                    	currBlk = setOfBlockObjects[idxBlockType][idxBlockDegree];
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
            printScreen(oScreen);
            System.out.println();
          
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
                //한칸 내려오면 맨 윗칸은 모두 0으로 바꿔야함.
                int[][] emptyLine = createArrayScreen(1,iScreenDx, 0);	// 빈 라인을 생성. 인자 -> dy, dx, dw  
                Matrix emptyLineMatrix = new Matrix(emptyLine);
                if(currentDebugLevel >= debugLevel3) emptyLineMatrix.print();
                oScreen.paste(emptyLineMatrix, 0, iScreenDw); // 빈 라인을 맨 윗줄에 붙인다.	
                printScreen(oScreen); System.out.println();
                
                // 한번에 여러 줄이 삭제될 수도 있음. 따라서 계속 검사.
                fullLine = oScreen.findFullLine(iScreenDw); 
            	if(currentDebugLevel >= debugLevel3) System.out.println("FullLine검사, 해당되는 라인(-1이라면 없음) : " + fullLine);
            	// findFullLine 함수는 fullLine인 줄의 number를 리턴함. fullLine이 없다면 -1을 리턴함.
        	}
            
            
            if (newBlockNeeded) {	// 새 블록이 필요한 경우
                iScreen = new Matrix(oScreen);
                // top과 left 초기화
                top = 0;	
                left = iScreenDw + iScreenDx / 2 - 2;
                newBlockNeeded = false;
               
                random = new Random(); // 다음 블록을 결정할 난수생성기
                idxBlockType = random.nextInt(numberOfBlockType);
                idxBlockDegree = 0; // 각도 초기화.
                if(currentDebugLevel >= debugLevel3) System.out.println("다음 블록 번호 : " + idxBlockType);
                
                currBlk = setOfBlockObjects[idxBlockType][idxBlockDegree]; // 처음은 degree0으로 생성한다.
                tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
                tempBlk = tempBlk.add(currBlk);
                if (tempBlk.anyGreaterThan(1)){	// 새 블록이 나오자마자 충돌 -> 게임오버
                	System.out.println("Game Over!");
                	System.exit(0);
                }
                oScreen = new Matrix(iScreen);
                oScreen.paste(tempBlk, top, left);
                printScreen(oScreen);
                System.out.println();
            }
        }
    }
}

