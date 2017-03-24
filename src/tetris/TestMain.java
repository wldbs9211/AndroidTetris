package tetris;

/*
//[Lab1v1] TestMain.java
public class TestMain {
	public static void main(String[] args) {
	   String s1 = "Hello, Java!";
	   System.out.println(s1);
	   String s2 = String.format("%s, %s!", "Hello", "Java");
	   System.out.println(s2);
	   
	   int len1 = s1.length();
	   int len2 = s2.length();
	   boolean b1 = s1.equals(s1);	// equals 는 내용을 보는 것이고, 
	   boolean b2 = s2.equals(s2);
	   boolean b3 = (s1 == s2);		// ==는 객체가 동일한지(주소)를 보는 것이다.
	   
	   System.out.println("s1=[" + s1 + "], len=" + len1 + ", (s1 equals s1)=" + b1);
	   System.out.println("s2=[" + s2 + "], len=" + len2 + ", (s1 equals s2)=" + b2);
	   System.out.println(" (s1 == s2)=" + b3);
	}
}
*/

/*
//[Lab1v2] TestMain.java
public class TestMain {
	public static void main(String[] args) {
	   String istr = "1234";
	   String dstr = "12.34";
	   
	   int ival = Integer.parseInt(istr);
	   double dval = Double.parseDouble(dstr);
	   System.out.println("before : " + ival + ", " + dval);
	   
	   ival = ival + 1111;
	   dval = dval + 11.11;
	   String istr2 = String.valueOf(ival);
	   String dstr2 = String.valueOf(dval);
	   
	   System.out.println("after : " + istr2 + ", " + dstr2);
	}
}
*/

/*
//[Lab1v3] TestMain.java
import java.util.Arrays;

public class TestMain {
	public static void main(String[] args) {
	   int A1[] = null;
	   int A2[] = { 1, 2, 3, 4, 5 };
	   int[] A3 = new int[5];
	   int A4[] = new int[] { 1, 2, 3, 4, 5 };
	   
	   System.out.print("A1:"); printArray(A1);
	   System.out.print("A2:"); printArray(A2);
	   System.out.print("A3:"); printArray(A3);
	   System.out.print("A4:"); printArray(A4);
	   System.out.println("A2.equals(A3)=" + A2.equals(A3)); // false
	   System.out.println("A2.equals(A4)=" + A2.equals(A4)); // false
	   System.out.println("Arrays.equals(A2, A3)=" + Arrays.equals(A2, A3)); // false
	   System.out.println("Arrays.equals(A2, A4)=" + Arrays.equals(A2, A4)); // true
	    // 주의! - String의 equals랑 반대였음.
	    // array1.equals(array2) is the same as array1 == array2, i.e. is it the same array.객체가 같은가?
		// Arrays.equals(array1, array2) compares the contents of the arrays.
	    
	   
	   printArray(A4);
	}
	public static void printArray(int A[]) {
	   if (A == null) {
	      System.out.println();
	      return;
	   }
	   for (int i = 0; i < A.length; i++)
	      System.out.print(A[i] + " ");
	   System.out.println();
	}
}
*/

/*
인간

학생(protected 학번)

환오

학생이랑 환오 클래스에서만 학번에 접근할 수 있다.
인간은 학번에 접근을 못해
 */

/*
//[Lab1v4] TestMain.java
public class TestMain {
	public static void main(String[] args) {
	   Matrix m1 = new Matrix(3,3);
	   m1.print(); System.out.println();
	   int A[][] = { { 0, 1, 0, }, { 1, 1, 1, }, { 0, 0, 0, } };
	   Matrix m2 = new Matrix(A);
	   m2.print(); System.out.println();
	   MyMatrix m3 = new MyMatrix(3,3);
	   m3.print(); System.out.println();
	   MyMatrix m4 = new MyMatrix(A);
	   m4.print(); System.out.println();
	   m2 = m4;
	   m2.print(); System.out.println();
	   
	}
	}
	class MyMatrix extends Matrix {
	public MyMatrix() { super(); }
	public MyMatrix(int cy, int cx) { super(cy, cx); }
	public MyMatrix(int[][] arr) { super(arr); }
	public void print() {
	   int dy = get_dy();
	   int dx = get_dx();
	   int array[][] = get_array();
	   for (int y=0; y < dy; y++) {
	      for (int x=0; x < dx; x++) {
	         if (array[y][x] == 0) System.out.print("□ ");
	         else if (array[y][x] == 1) System.out.print("■ ");
	         else System.out.print("X ");
	      }
	      System.out.println();
	   }
	}
}
*/

/*
//[Lab1v5] TestMain.java
public class TestMain {
	public static void main(String[] args) {
	   Nested m1 = new Nested(1, 2);
	   Nested m2 = new Nested(3, 4);
	   System.out.println("m1.get_dy()=" + m1.get_dy() + ", m1.get_dx()=" + m1.get_dx());
	   System.out.println("m2.get_dy()=" + m2.get_dy() + ", m2.get_dx()=" + m2.get_dx());
	   Nested.DynInner d1 = m1.new DynInner();
	   Nested.StaInner s1 = new Nested.StaInner();
	   Nested.DynInner d2 = m2.new DynInner();
	   Nested.StaInner s2 = new Nested.StaInner();
	   System.out.println("d1.get_dy()=" + d1.get_dy() + ", s1.get_dx()=" + s1.get_dx());
	   System.out.println("d1.get_dy()=" + d2.get_dy() + ", s1.get_dx()=" + s2.get_dx());
	}
}
class Nested {
	private int dy;
	private static int dx;
	public int get_dy() { return dy; }
	public static int get_dx() { return dx; }
	public Nested(int cy, int cx) { dy = cy; dx = cx; }
	public class DynInner { public int get_dy() { return dy; } }
	public static class StaInner { public int get_dx() { return dx; } }
}
*/

/*
//[Lab2v2] TestMain.java
public class TestMain {
	public static void main(String[] args) throws Exception {
	   int[][] blkArray = {
	         { 0, 1, 0 },
	         { 1, 1, 1 },
	         { 0, 0, 0 }
	   };
	   Matrix currBlk = new Matrix(blkArray);
	   try {
	      Matrix tempBlk = new Matrix(5,5);
	      tempBlk = tempBlk.add(currBlk);
	      
	   } catch(MismatchedMatrixException e) {
	      e.printStackTrace();
	      System.out.println(e.getMessage());
	      System.out.println("at first catch");
	   } catch(MatrixException e) {
	      e.printStackTrace();
	      System.out.println(e.getMessage());
	      System.out.println("at second catch");
	   }
	}
}
*/


//[Lab2v3] TestMain.java
public class TestMain {
	public static void main(String[] args) throws Exception {
	 Matrix m = new Matrix(10, 10);;
	 for(int i=0; i<999; i++)
	    m = new Matrix(10, 10);
	 System.gc();
	 System.out.println("nAlloc=" + m.get_nAlloc());
	 System.out.println("nFree=" + m.get_nFree());
	}
}

