/*    */ package 1.0.com.sun.codemodel.util;
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
/*    */ class Surrogate
/*    */ {
/*    */   public static final char MIN_HIGH = '?';
/*    */   public static final char MAX_HIGH = '?';
/*    */   public static final char MIN_LOW = '?';
/*    */   public static final char MAX_LOW = '?';
/*    */   public static final char MIN = '?';
/*    */   public static final char MAX = '?';
/*    */   public static final int UCS4_MIN = 65536;
/*    */   public static final int UCS4_MAX = 1114111;
/*    */   
/*    */   public static boolean isHigh(int c) {
/* 41 */     return (55296 <= c && c <= 56319);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean isLow(int c) {
/* 48 */     return (56320 <= c && c <= 57343);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean is(int c) {
/* 55 */     return (55296 <= c && c <= 57343);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static boolean neededFor(int uc) {
/* 63 */     return (uc >= 65536 && uc <= 1114111);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static char high(int uc) {
/* 70 */     return (char)(0xD800 | uc - 65536 >> 10 & 0x3FF);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static char low(int uc) {
/* 77 */     return (char)(0xDC00 | uc - 65536 & 0x3FF);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int toUCS4(char c, char d) {
/* 84 */     return ((c & 0x3FF) << 10 | d & 0x3FF) + 65536;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemode\\util\Surrogate.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */