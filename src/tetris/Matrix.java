package tetris;

/*
// [Lab1v1] Matrix.java
public class Matrix {
   private int dy = 0;
   public int get_dy() { return dy; }
   private int dx = 0;
   public int get_dx() { return dx; }
   private int[][] array = null;
   public int[][] get_array() { return array; }
   private void alloc(int cy, int cx) {
      if((cy < 0) || (cx < 0)) {
         System.out.println("wrong matrix size");
         System.exit(0);
      }
      dy = cy;
      dx = cx;
      array = new int[dy][dx];
   }
   public Matrix() { alloc(0, 0); }
   public Matrix(int cy, int cx) {
      alloc(cy, cx);
      for(int y = 0; y < dy; y++)
         for(int x = 0; x < dx; x++)
            array[y][x] = 0;
   }
   public Matrix(Matrix obj) {
      alloc(obj.dy, obj.dx);
      for(int y = 0; y < dy; y++)
         for(int x = 0; x < dx; x++)
            array[y][x] = obj.array[y][x];
   }
   public Matrix(int[][] arr) {
      alloc(arr.length, arr[0].length);
      for(int y = 0; y < dy; y++)
         for(int x = 0; x < dx; x++)
            array[y][x] = arr[y][x];
   }
   
   public Matrix clip(int top, int left, int bottom, int right) {
      int cy = bottom - top;
      int cx = right - left;
      Matrix temp = new Matrix(cy, cx);
      for(int y = 0; y < cy; y++) {
         for(int x = 0; x < cx; x++) {
            if((top+y >= 0) && (left+x >= 0) && (top+y < dy) && (left+x < dx))
               temp.array[y][x] = array[top+y][left+x];
            else {
               System.out.println("invalid matrix range");
               System.exit(0);
            }
         }
      }
      return temp;
   }
   
   public void paste(Matrix obj, int top, int left) {
      for(int y = 0; y < obj.dy; y++)
         for(int x = 0; x <obj.dx; x++) {
            if((top+y >= 0) && (left+x >= 0) && (top+y < dy) && (left+x < dx))
               array[y+top][x+left] = obj.array[y][x];
            else {
               System.out.println("invalid matrix range");
               System.exit(0);
            }
         }
   }
   
   public Matrix add(Matrix obj) {
      if((dx != obj.dx && (dy != obj.dy))) {
         System.out.println("matrix sizes mismatch");
         System.exit(0);
      }
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
   // end of Matrix
}
*/

//[Lab2v1] Matrix.java
public class Matrix {
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

