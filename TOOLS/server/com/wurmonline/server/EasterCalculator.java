/*    */ package com.wurmonline.server;
/*    */ 
/*    */ import java.util.Calendar;
/*    */ import java.util.GregorianCalendar;
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
/*    */ public final class EasterCalculator
/*    */ {
/*    */   public static Calendar findHolyDay(int year) {
/* 41 */     if (year <= 1582)
/*    */     {
/* 43 */       throw new IllegalArgumentException("Algorithm invalid before April 1583");
/*    */     }
/*    */     
/* 46 */     int golden = year % 19 + 1;
/* 47 */     int century = year / 100 + 1;
/* 48 */     int x = 3 * century / 4 - 12;
/* 49 */     int z = (8 * century + 5) / 25 - 5;
/* 50 */     int d = 5 * year / 4 - x - 10;
/* 51 */     int epact = (11 * golden + 20 + z - x) % 30;
/* 52 */     if ((epact == 25 && golden > 11) || epact == 24)
/* 53 */       epact++; 
/* 54 */     int n = 44 - epact;
/* 55 */     n += 30 * ((n < 21) ? 1 : 0);
/* 56 */     n += 7 - (d + n) % 7;
/* 57 */     if (n > 31) {
/* 58 */       return new GregorianCalendar(year, 3, n - 31);
/*    */     }
/* 60 */     return new GregorianCalendar(year, 2, n);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\EasterCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */