/*    */ package org.fourthline.cling.support.messagebox.model;
/*    */ 
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import org.fourthline.cling.support.messagebox.parser.MessageElement;
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
/*    */ public class DateTime
/*    */   implements ElementAppender
/*    */ {
/*    */   private final String date;
/*    */   private final String time;
/*    */   
/*    */   public DateTime() {
/* 32 */     this(getCurrentDate(), getCurrentTime());
/*    */   }
/*    */   
/*    */   public DateTime(String date, String time) {
/* 36 */     this.date = date;
/* 37 */     this.time = time;
/*    */   }
/*    */   
/*    */   public String getDate() {
/* 41 */     return this.date;
/*    */   }
/*    */   
/*    */   public String getTime() {
/* 45 */     return this.time;
/*    */   }
/*    */   
/*    */   public void appendMessageElements(MessageElement parent) {
/* 49 */     ((MessageElement)parent.createChild("Date")).setContent(getDate());
/* 50 */     ((MessageElement)parent.createChild("Time")).setContent(getTime());
/*    */   }
/*    */   
/*    */   public static String getCurrentDate() {
/* 54 */     SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
/* 55 */     return fmt.format(new Date());
/*    */   }
/*    */   
/*    */   public static String getCurrentTime() {
/* 59 */     SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
/* 60 */     return fmt.format(new Date());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\messagebox\model\DateTime.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */