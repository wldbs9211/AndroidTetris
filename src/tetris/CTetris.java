package tetris;

public class CTetris extends Tetris{
	private Matrix cScreen = null;
	
	// 생성자는 부모의 것과 동일하게..
	public CTetris(int cy, int cx, int[][][][] setOfBlkArrays) throws Exception {
		super(cy, cx, setOfBlkArrays);
		// TODO Auto-generated constructor stub
	}

	// 모양을 다르게 출력
	public void printCScreen() {
    	Matrix screen = cScreen;
        int dy = cScreen.get_dy();
        int dx = cScreen.get_dx();
        int dw = iScreenDw;
        int array[][] = cScreen.get_array();
        for (int y = 0; y < dy - dw + 1; y++) {
            for (int x = dw -1; x < dx - dw + 1; x++) {
            	// 여기에 블록의 타입별로 다르게 뷰에 뿌려줘야함.
            	printCharacter(array[y][x]);
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
	public void printCharacter(int blockType){
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
}
