package tetris;


public class Matrix {
	/*
	 * 디버그 레벨에 따라서 출력여부를 결정한다.
	 * level이 높을수록 더 자세하게 정보를 출력한다.
	 * 예) currentDebugLevel의 값이 3이면, 모든 정보를 출력한다. 
	 * 예) currentDebugLevel의 값이 2이면, debugLevel 1 ~ 2까지의 정보를 출력한다. (특정 이벤트 및 흐름)
	 * 예) currentDebugLevel의 값이 1이면, debugLevel 1만 출력 ( 프로그램의 흐름에 대한 정보만 ) 
	 */
	private final static int currentDebugLevel = 3;	// 현재 디버그 레벨.
	private final static int debugLevel1 = 1;	// 프로그램의 흐름에 대한 정보. 
	private final static int debugLevel2 = 2;	// 프로그램에서 어떠한 이벤트에 대한 정보.
	private final static int debugLevel3 = 3;	// 특정 이벤트가 발생한 상황에서 변수의 변화 등에 대한 정보.
	
	private static int nAlloc = 0;
	private static int nFree = 0;
	protected void finalize() throws Throwable {
	   super.finalize();
	   nFree++;
	}
	public int get_nAlloc() { return nAlloc; }
	public int get_nFree() { return nFree; }
	private int dy = 0;
	public int get_dy() { return dy; }
	private int dx = 0;
	public int get_dx() { return dx; }
	private int[][] array = null;
	public int[][] get_array() { return array; }
	private void alloc(int cy, int cx) throws MatrixException {
	   if((cy < 0) || (cx < 0))
		   throw new MatrixException("wrong matrix size");
	      
	   dy = cy;
	   dx = cx;
	   array = new int[dy][dx];
	   nAlloc++;
	}
	public Matrix() throws MatrixException { alloc(0, 0); }
	public Matrix (int cy, int cx) throws MatrixException {
	   alloc(cy, cx);
	   for(int y = 0; y < dy; y++)
	      for(int x = 0; x < dx; x++)
	         array[y][x] = 0;
	}
	public Matrix(Matrix obj) throws MatrixException {
	   alloc(obj.dy, obj.dx);
	   for(int y = 0; y < dy; y++)
	      for(int x = 0; x < dx; x++)
	         array[y][x] = obj.array[y][x];
	}
	public Matrix(int[][] arr) throws MatrixException {
	   alloc(arr.length, arr[0].length);
	   for(int y = 0; y < dy; y++)
	      for(int x = 0; x < dx; x++)
	         array[y][x] = arr[y][x];
	}
	
	public Matrix clip(int top, int left, int bottom, int right) throws MatrixException {
	   int cy = bottom - top;
	   int cx = right - left;
	   Matrix temp = new Matrix(cy, cx);
	   for(int y = 0; y < cy; y++) {
	      for(int x = 0; x < cx; x++) {
	         if((top+y >= 0) && (left+x >= 0) && (top+y < dy) && (left+x < dx))
	            temp.array[y][x] = array[top+y][left+x];
	         else 
	        	 throw new MatrixException("invalid matrix range");
	      }
	   }
	   return temp;
	}
	
	public void paste(Matrix obj, int top, int left) throws MatrixException {
		// top : 14,left :  3 , y: 5 , x : 0
	   for(int y = 0; y < obj.dy; y++)
	      for(int x = 0; x <obj.dx; x++) {
	         if((top+y >= 0) && (left+x >= 0) && (top+y < dy) && (left+x < dx))
	            array[y+top][x+left] = obj.array[y][x];
	         else 
	        	 throw new MatrixException("invalid matrix range");
	      }
	}
	
	public Matrix add(Matrix obj) throws MatrixException {
	   if(dx != obj.dx && (dy != obj.dy))
		   throw new MismatchedMatrixException("matrix sizes mismatch");
	   Matrix temp = new Matrix(dy, dx);
	   for(int y = 0; y < obj.dy; y++)
	      for(int x = 0; x < obj.dx; x++)
	         temp.array[y][x] = array[y][x] + obj.array[y][x];
	   return temp;
	}
	
	public int sum() {
	   int total = 0;
	   for(int y = 0; y < dy; y++)
	      for(int x = 0; x < dx; x++)
	         total += array[y][x];
	   return total;
	}
	public void mulc(int coef) {
	   for(int y = 0; y < dy; y++)
	      for(int x = 0; x < dx; x++)
	         array[y][x] = coef * array[y][x];
	}
	public boolean anyGreaterThan(int val) {
	   for(int y = 0; y < array.length; y++) {
	      for(int x = 0; x < array[0].length; x++) {
	         if(array[y][x] > val) return true;
	      }
	   }
	   return false;
	}
	public void print() {
	   System.out.println("Matrix(" + dy + "," + dx + ")");
	   for(int y = 0; y < dy; y++) {
	      for(int x = 0; x < dx; x++)
	         System.out.print(array[y][x] + " ");
	      System.out.println();
	   }
	}
	public int findFullLine(int screenDw){
    	for(int i = dy - screenDw - 1; i >= 0; i--){	// 바닥에서 위로 올라오며 검사한다.
    		if(currentDebugLevel >= debugLevel3) System.out.println("풀라인 검사 : " + i + "번 행." );
    		boolean fullLineFlag = true; // FullLine이 검출되었는가 검사하는 flag
    		for(int j = screenDw; j < dx - screenDw; j++ ){
        		if(currentDebugLevel >= debugLevel3) System.out.println("풀라인 검사 : " + j + "번 열." );
    			if(array[i][j] == 0){
    				if(currentDebugLevel >= debugLevel3) System.out.println("FullLine 아님." );
    				fullLineFlag = false;
    			}
    		}
    		if(currentDebugLevel >= debugLevel3) System.out.println(i + "번 행은 FullLine? : " + fullLineFlag);
    		if(fullLineFlag == true) return i;	// 가장 먼저 만나는 FullLine을 리턴한다.
    	}
    	return -1; // -1을 리턴하는 경우라면 FullLine이 없다는 것임.
    }
	// end of Matrix
}
	
	class MatrixException extends Exception {
		public MatrixException() { super("Matrix Exception"); }
		public MatrixException(String msg) { super(msg); }
	}
	class MismatchedMatrixException extends MatrixException {
		public MismatchedMatrixException() { super("Mismatched Matrix Exception"); }
		public MismatchedMatrixException(String msg) { super(msg); }
	}

