public class max {
   public static int max(int[] m) {
      int c = 0;
      int high = -999;
         while (c < m.length) {
            if (m[c] > high) {
                high = m[c];
            }
            c = c + 1;
      }
      return high;
   }
   public static void main(String[] args) {
      int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6}; 
      System.out.println(max(numbers));
   }
}