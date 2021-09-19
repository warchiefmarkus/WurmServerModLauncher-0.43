/*    */ package 1.0.com.sun.tools.xjc.util;
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
/*    */ public class EditDistance
/*    */ {
/*    */   private int[] cost;
/*    */   private int[] back;
/*    */   private final String a;
/*    */   private final String b;
/*    */   
/*    */   public static int editDistance(String a, String b) {
/* 30 */     return (new com.sun.tools.xjc.util.EditDistance(a, b)).calc();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String findNearest(String key, String[] group) {
/* 40 */     int c = Integer.MAX_VALUE;
/* 41 */     String r = null;
/*    */     
/* 43 */     for (int i = 0; i < group.length; i++) {
/* 44 */       int ed = editDistance(key, group[i]);
/* 45 */       if (c > ed) {
/* 46 */         c = ed;
/* 47 */         r = group[i];
/*    */       } 
/*    */     } 
/* 50 */     return r;
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
/*    */   private EditDistance(String a, String b) {
/* 62 */     this.a = a;
/* 63 */     this.b = b;
/* 64 */     this.cost = new int[a.length() + 1];
/* 65 */     this.back = new int[a.length() + 1];
/*    */     
/* 67 */     for (int i = 0; i <= a.length(); i++) {
/* 68 */       this.cost[i] = i;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void flip() {
/* 75 */     int[] t = this.cost;
/* 76 */     this.cost = this.back;
/* 77 */     this.back = t;
/*    */   }
/*    */   
/*    */   private int min(int a, int b, int c) {
/* 81 */     return Math.min(a, Math.min(b, c));
/*    */   }
/*    */   
/*    */   private int calc() {
/* 85 */     for (int j = 0; j < this.b.length(); j++) {
/* 86 */       flip();
/* 87 */       this.cost[0] = j + 1;
/* 88 */       for (int i = 0; i < this.a.length(); i++) {
/* 89 */         int match = (this.a.charAt(i) == this.b.charAt(j)) ? 0 : 1;
/* 90 */         this.cost[i + 1] = min(this.back[i] + match, this.cost[i] + 1, this.back[i + 1] + 1);
/*    */       } 
/*    */     } 
/* 93 */     return this.cost[this.a.length()];
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 97 */     System.out.println(editDistance(args[0], args[1]));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\EditDistance.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */