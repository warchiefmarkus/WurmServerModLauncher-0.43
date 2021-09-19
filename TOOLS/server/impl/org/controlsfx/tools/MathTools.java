/*    */ package impl.org.controlsfx.tools;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MathTools
/*    */ {
/*    */   public static boolean isInInterval(double lowerBound, double value, double upperBound) {
/* 49 */     return (lowerBound <= value && value <= upperBound);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static double inInterval(double lowerBound, double value, double upperBound) {
/* 67 */     if (value < lowerBound)
/* 68 */       return lowerBound; 
/* 69 */     if (upperBound < value)
/* 70 */       return upperBound; 
/* 71 */     return value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static double min(double... values) {
/* 86 */     Objects.requireNonNull(values, "The specified value array must not be null.");
/* 87 */     if (values.length == 0) {
/* 88 */       throw new IllegalArgumentException("The specified value array must contain at least one element.");
/*    */     }
/* 90 */     double min = Double.MAX_VALUE;
/* 91 */     for (double value : values)
/* 92 */       min = Math.min(value, min); 
/* 93 */     return min;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\impl\org\controlsfx\tools\MathTools.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */