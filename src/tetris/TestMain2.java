package tetris;

//[Lab2v1] TestMain.java
public class TestMain2 {
public static void main(String[] args) {
   int[][] arrayScreen = {
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 }, 
         { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }, 
   };
   int[][] arrayBlk = {
         { 0, 1, 0 },
         { 1, 1, 1 },
         { 0, 0, 0 },
   };
   /*
   Matrix oScreen = new Matrix(arrayScreen);
   printMatrix(oScreen); System.out.println();
   
   Matrix currBlk = new Matrix(arrayBlk);
   printMatrix(currBlk); System.out.println();
   
   Matrix tempBlk = oScreen.clip(1,  4,  4,  7);
   printMatrix(tempBlk); System.out.println();
   
   tempBlk = tempBlk.add(currBlk);
   printMatrix(tempBlk); System.out.println();
   
   oScreen.paste(tempBlk, 1, 4);
   printMatrix(oScreen); System.out.println();

   System.out.println("currBlk.sum()=" + currBlk.sum());
   System.out.println();
   
   tempBlk.mulc(2);
   tempBlk.print(); System.out.println();
   
   System.out.println("currBlk.anyGreaterThan(1)=" + currBlk.anyGreaterThan(1));
   System.out.println("tempBlk.anyGreaterThan(1)=" + tempBlk.anyGreaterThan(1));
   */
}
public static void printMatrix(Matrix blk) {
   int dy = blk.get_dy();
   int dx = blk.get_dx();
   int array[][] = blk.get_array();
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

