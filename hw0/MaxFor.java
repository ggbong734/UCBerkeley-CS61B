public class MaxFor {
   public static int max(int[] m) {
      int high = 0;
      for (int c = 0; c < m.length; c += 1) {
         if (m[c] > high) {
         high = m[c];
         }
      }
      return high;
   }
   public static void main(String[] args) {
      int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};      
   }
}