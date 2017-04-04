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
	
	private final static int numberOfBlockType = 7;	// 블록의 종류
	
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
        char key;
        int idxType;
        boolean newBlockNeeded;
        TetrisState state;
        
        Tetris.init(setOfBlockArrays);	// static method임. 객체를 생성한 적이 없지만 바로 접근이 가능. 공유, 정적
        
        Tetris board = new Tetris(15, 10);	// 이것은 공유하지 않음. 동적
        //Tetris board2 = new Tetris(15, 12);	// 복수 객체 검증을 위함.
        
        Random random = new Random();
        idxType = random.nextInt(7);
        key = (char) ('0' + random.nextInt(7));
        state = board.accept(key);
        //board2.accept('0', idxType);	//board , board2에 동일한 key 전달
        board.printScreen(); System.out.println();
        //board2.printScreen(); System.out.println();
        
        while(((key = getKey()) != 'q') && (state != TetrisState.End)){
        	state = board.accept(key); 
        	board.printScreen(); System.out.println();
        	switch(state){
	        	case NewBlock:
	        		key = (char) ('0' + random.nextInt(7));
	        		state = board.accept(key);
	        		board.printScreen(); System.out.println();
	        		break;
	        	case Error:
	        		System.out.println("잘못된 key의 입력");
	        		break;
	        	case End:
	        		System.out.println("Game Over!");
	        		break;
        	}
        }
        
        /*
        while((key = getKey()) != 'q'){
        	newBlockNeeded = board.accept(key);
        	//board2.accept(key, idxType);	//board , board2에 동일한 key 전달
        	
        	board.printScreen(); System.out.println();
        	//board2.printScreen(); System.out.println();
        	
        	// 새로운 블록이 필요하지 않다면 idxType을 계속 저장하여 유지한다. degree는 Tetris 클래스에서 내부적으로 저장.
        	if (newBlockNeeded) {
        		idxType = random.nextInt(7);
        		key = (char) ('0' + random.nextInt(7));
        		newBlockNeeded = board.accept(key);
        		//board2.accept('0', idxType); //board , board2에 동일한 key 전달
        		
        		board.printScreen(); System.out.println();
        		//board2.printScreen(); System.out.println();
        		if (newBlockNeeded){ // 새 블록이 필요한 상태에서 또 중복되었다면 게임종료.
        			System.out.println("Game Over!");
                	System.exit(0);
        		}
        	}
        }
        */
        System.out.println("Program Terminated!");
    }
}

