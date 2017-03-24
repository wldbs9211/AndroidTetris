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
    
    static int[][] blockType1 = {	
    		{0,0,0},
    		{1,0,0},
    		{1,1,1}
    };
    
    static int[][] blockType2 = {	
    		{0,0,0},
    		{0,1,1},
    		{1,1,0}
    };
    
    static int[][] blockType3 = {	
    		{0,0,0,0},
    		{0,0,0,0},
    		{0,0,0,0},
    		{1,1,1,1}
    };
    
    static int[][] blockType4 = {	
    		{1,1},
    		{1,1}
    };
    
    static int[][] blockType5 = {
    		{0,0,0},
    		{0,1,0},
    		{1,1,1}
    };
    
    static int[][] blockType6 = {
    		{0,0,0},
    		{1,1,0},
    		{0,1,1}
    };
    
    static int[][] blockType7 = {
    		{0,0,0},
    		{0,0,1},
    		{1,1,1}
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
                else System.out.print("x");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        Matrix[] setOfBlockObjects = {
        	new Matrix(blockType1),	
        	new Matrix(blockType2),
        	new Matrix(blockType3),
        	new Matrix(blockType4),
        	new Matrix(blockType5),
        	new Matrix(blockType6),
        	new Matrix(blockType7)
        };
    	
        boolean newBlockNeeded = false;
        int top = 0;
        int left = iScreenDw + iScreenDx/2 - 2;
        int[][] arrayScreen = createArrayScreen(iScreenDy, iScreenDx, iScreenDw);
        char key;
        Matrix iScreen = new Matrix(arrayScreen);
        
        Random random = new Random(); // 다음 블록을 결정할 난수생성기
        int idxBlockType = random.nextInt(7);
        if(currentDebugLevel >= debugLevel3){
        	System.out.println("다음 블록 번호 : " + idxBlockType);
        }
        //지금은 이걸로 다음 블록을 생성한다. 이것을 랜덤으로 만들어야해.
        Matrix currBlk = setOfBlockObjects[idxBlockType]; 
        
        
        
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
                    break;
                case ' ':
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
                        break;
                    case ' ':
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
                if(currentDebugLevel >= debugLevel3){
                	System.out.println("다음 블록 번호 : " + idxBlockType);
                }
                currBlk = setOfBlockObjects[idxBlockType];
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