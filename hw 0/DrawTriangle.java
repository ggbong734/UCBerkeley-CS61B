public class DrawTriangle {
   public static void drawTriangle(int N){
       int c = 0;
       while (c < N) { 
          int inner_c = 0;
          while (inner_c < c) {
             System.out.print("*"); 
             inner_c = inner_c + 1;
          }
             System.out.println("*");
             c = c + 1;
       }   
   }   
   public static void main(String[] args) {
       drawTriangle(10);
   }
}
