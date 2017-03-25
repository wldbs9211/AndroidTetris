package tetris;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Main {
	/*
	 * 디버그 레벨에 따라서 출력여부를 결정한다.
	 * level이 높을수록 더 자세하게 정보를 출력한다. 
	 * 예) currentDebugLevel의 값이 2라면, debugLevel 1 ~ 2까지의 정보를 출력한다.
	 * 1 ~ 2는 매우 중요부터 평범한 정보. 
	 */
	private final static int currentDebugLevel = 3;	// 현재 디버그 레벨.
	private final static int debugLevel1 = 1;	// 매우 중요.
	private final static int debugLevel2 = 2;	// 평범.
	private final static int debugLevel3 = 3;	// 중요하지 않음.
	
	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    private static String line = null;
    private static int nKeys = 0;
	
    int[][] test = {{1,2,3},{1,2,3},{1,2,3}};
    
    static int[][] blockType0_degree0 = {
    		{0,0,0},
    		{0,1,0},
    		{1,1,1}
    };
    
    static int[][] blockType0_degree1 = {
    		{0,1,0},
    		{0,1,1},
    		{0,1,0}
    };
    
    static int[][] blockType0_degree2 = {
    		{0,0,0},
    		{1,1,1},
    		{0,1,0}
    };
    
    static int[][] blockType0_degree3 = {
    		{0,1,0},
    		{1,1,0},
    		{0,1,0}
    };
    
    static int[][] blockType1_degree0 = {	
    		{1,0,0},
    		{1,1,1},
    		{0,0,0}
    };
    
    static int[][] blockType1_degree1 = {	
    		{0,1,1},
    		{0,1,0},
    		{0,1,0}
    };
    
    static int[][] blockType1_degree2 = {	
    		{0,0,0},
    		{1,1,1},
    		{0,0,1}
    };
    
    static int[][] blockType1_degree3 = {	
    		{0,1,0},
    		{0,1,0},
    		{1,1,0}
    };
    
    static int[][] blockType2_degree0 = {	
    		{0,0,1},
    		{1,1,1},
    		{0,0,0}
    };
    
    static int[][] blockType2_degree1 = {	
    		{0,1,0},
    		{0,1,0},
    		{0,1,1}
    };
    
    static int[][] blockType2_degree2 = {	
    		{0,0,0},
    		{1,1,1},
    		{1,0,0}
    };
    
    static int[][] blockType2_degree3 = {	
    		{1,1,0},
    		{0,1,0},
    		{0,1,0}
    };
    
    static int[][] blockType3_degree0 = {	
    		{0,0,0,0},
    		{0,0,0,0},
    		{1,1,1,1},
    		{0,0,0,0}
    };
    
    static int[][] blockType3_degree1 = {	
    		{0,1,0,0},
    		{0,1,0,0},
    		{0,1,0,0},
    		{0,1,0,0}
    };
    
    static int[][] blockType3_degree2 = {	
    		{0,0,0,0},
    		{0,0,0,0},
    		{1,1,1,1},
    		{0,0,0,0}
    };
    
    static int[][] blockType3_degree3 = {	
    		{0,1,0,0},
    		{0,1,0,0},
    		{0,1,0,0},
    		{0,1,0,0}
    };
    
    static int[][] blockType4_degree0 = {	
    		{1,1},
    		{1,1}
    };
    
    static int[][] blockType4_degree1 = {	
    		{1,1},
    		{1,1}
    };
    
    static int[][] blockType4_degree2 = {	
    		{1,1},
    		{1,1}
    };
    
    static int[][] blockType4_degree3 = {	
    		{1,1},
    		{1,1}
    };
    
    static int[][] blockType5_degree0 = {
    		{0,1,1},
    		{1,1,0},
    		{0,0,0}
    };
    
    static int[][] blockType5_degree1 = {
    		{0,1,0},
    		{0,1,1},
    		{0,0,1}
    };
    
    static int[][] blockType5_degree2 = {
    		{0,0,0},
    		{0,1,1},
    		{1,1,0}
    };
    
    static int[][] blockType5_degree3 = {
    		{1,0,0},
    		{1,1,0},
    		{0,1,0}
    };
    
    static int[][] blockType6_degree0 = {
    		{0,0,0},
    		{1,1,0},
    		{0,1,1}
    };
    
    static int[][] blockType6_degree1 = {
    		{0,0,1},
    		{0,1,1},
    		{0,1,0}
    };
    
    static int[][] blockType6_degree2 = {
    		{0,0,0},
    		{1,1,0},
    		{0,1,1}
    };
    
    static int[][] blockType6_degree3 = {
    		{0,1,0},
    		{1,1,0},
    		{1,0,0}
    };
    
	static int[][] arrayScreen = {
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 }, 
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
    };
	static int[][] arrayBlk = {
            {1, 0, 0, 0},
            {1, 0, 0, 0},
            {1, 0, 0, 0},
            {1, 0, 0, 0},
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
	
    private static int iScreenDy = 15;
    private static int iScreenDx = 10;
    private static int iScreenDw = 4;
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
        Matrix[][] setOfBlockObjects = {
        	{ new Matrix(blockType0_degree0), new Matrix(blockType0_degree1), new Matrix(blockType0_degree2), new Matrix(blockType0_degree3) },
        	{ new Matrix(blockType1_degree0), new Matrix(blockType1_degree1), new Matrix(blockType1_degree2), new Matrix(blockType1_degree3) },	
        	{ new Matrix(blockType2_degree0), new Matrix(blockType2_degree1), new Matrix(blockType2_degree2), new Matrix(blockType2_degree3) },
        	{ new Matrix(blockType3_degree0), new Matrix(blockType3_degree1), new Matrix(blockType3_degree2), new Matrix(blockType3_degree3) },
        	{ new Matrix(blockType4_degree0), new Matrix(blockType4_degree1), new Matrix(blockType4_degree2), new Matrix(blockType4_degree3) },
        	{ new Matrix(blockType5_degree0), new Matrix(blockType5_degree1), new Matrix(blockType5_degree2), new Matrix(blockType5_degree3) },
        	{ new Matrix(blockType6_degree0), new Matrix(blockType6_degree1), new Matrix(blockType6_degree2), new Matrix(blockType6_degree3) }
        };
    	
        boolean newBlockNeeded = false;
        int top = 0;
        int left = iScreenDw + iScreenDx/2 - 2;
        int[][] arrayScreen = createArrayScreen(iScreenDy, iScreenDx, iScreenDw);
        char key;
        Matrix iScreen = new Matrix(arrayScreen);
        
        Random random = new Random(); // 다음 블록을 결정할 난수생성기
        int idxBlockType = random.nextInt(7);
        int idxBlockDegree = 0;	// 각도는 처음에 0으로 블록을 생성함.
        if(currentDebugLevel >= debugLevel2){
        	System.out.println("다음 블록 번호 : " + idxBlockType);
        }
        //지금은 이걸로 다음 블록을 생성한다. 이것을 랜덤으로 만들어야해.
        Matrix currBlk = setOfBlockObjects[idxBlockType][idxBlockDegree];	// 처음은 degree0 으로 생성한다. 
        
        
        
        Matrix tempBlk = iScreen.clip(top, left, top+currBlk.get_dy(), left+currBlk.get_dx());
        tempBlk = tempBlk.add(currBlk);
        Matrix oScreen = new Matrix(iScreen);
        oScreen.paste(tempBlk, top, left);
        printScreen(oScreen); System.out.println();

        while ((key = getKey()) != 'q') {
            switch (key) {
                case 'a':
                    left--;
                    break;
                case 'd':
                    left++;
                    break;
                case 's':
                    top++;
                    break;
                case 'w':
                	if(currentDebugLevel >= debugLevel2){
                		System.out.println("블록을 회전시킵니다.");
                	}
                	idxBlockDegree = (idxBlockDegree + 1) % 4;
                	currBlk = setOfBlockObjects[idxBlockType][idxBlockDegree];
                    break;
                case ' ':
                	// 바닥에 닿기 전까지 한칸씩 내려가며 충돌하나 비교한다.
                	tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx()); // 블록이 생성될 위치 초기 세팅.
                    tempBlk = tempBlk.add(currBlk); // 현재 블록 넣고.
                    while (!tempBlk.anyGreaterThan(1)){ // 충돌까지 내린다. 
                    	top ++;	// 내리고.
                    	tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx()); // 갱신하고.
                        tempBlk = tempBlk.add(currBlk); // 현재 블록 넣고.
                    	if(currentDebugLevel >= debugLevel2){
                    		System.out.println("블록을 하나 내려봅니다. top : " + top );
                    	}
                    }
                    // 여기까지 왔다면 블록은 현재 충돌된 상태임. 아래 최종 작업에서 top을 하나 빼주어야함.
                    break;
                default:
                    System.out.println("unknown key!");
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
                    	if(currentDebugLevel >= debugLevel2){
                    		System.out.println("블록이 회전 과정에서 충돌하였음. 이전으로 돌아감");
                    	}
                    	idxBlockDegree = idxBlockDegree - 1;	// Degree를 이전으로 돌림.
                    	if(idxBlockDegree == -1) idxBlockDegree = 3; // 회전 : 3 -> 0 , 충돌 : 0 -> -1 케이스니 3으로 되돌림.
                    	currBlk = setOfBlockObjects[idxBlockType][idxBlockDegree];
                        break;
                    case ' ':
                    	// 이미 충돌된 상태임. top을 하나 빼주어 충돌 직전 위치로 이동. 
                    	top--;
                    	if(currentDebugLevel >= debugLevel2){
                    		System.out.println("블록이 바닥에 충돌하였음. 최종 top의 값 : " + top);
                    	}
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
            if (newBlockNeeded) {
                iScreen = new Matrix(oScreen);
                top = 0;
                left = iScreenDw + iScreenDx / 2 - 2;
                newBlockNeeded = false;
                
               
                random = new Random(); // 다음 블록을 결정할 난수생성기
                idxBlockType = random.nextInt(7);
                idxBlockDegree = 0; // 각도 초기화.
                if(currentDebugLevel >= debugLevel2){
                	System.out.println("다음 블록 번호 : " + idxBlockType);
                }
                currBlk = setOfBlockObjects[idxBlockType][idxBlockDegree]; // 처음은 degree0으로 생성한다.
                tempBlk = iScreen.clip(top, left, top + currBlk.get_dy(), left + currBlk.get_dx());
                tempBlk = tempBlk.add(currBlk);
                if (tempBlk.anyGreaterThan(1)){
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