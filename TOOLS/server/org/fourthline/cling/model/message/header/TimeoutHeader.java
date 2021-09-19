/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class TimeoutHeader
/*    */   extends UpnpHeader<Integer>
/*    */ {
/* 29 */   public static final Integer INFINITE_VALUE = Integer.valueOf(2147483647);
/*    */   
/* 31 */   public static final Pattern PATTERN = Pattern.compile("Second-(?:([0-9]+)|infinite)");
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public TimeoutHeader() {
/* 37 */     setValue(Integer.valueOf(1800));
/*    */   }
/*    */   
/*    */   public TimeoutHeader(int timeoutSeconds) {
/* 41 */     setValue(Integer.valueOf(timeoutSeconds));
/*    */   }
/*    */   
/*    */   public TimeoutHeader(Integer timeoutSeconds) {
/* 45 */     setValue(timeoutSeconds);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 50 */     Matcher matcher = PATTERN.matcher(s);
/* 51 */     if (!matcher.matches()) {
/* 52 */       throw new InvalidHeaderException("Can't parse timeout seconds integer from: " + s);
/*    */     }
/*    */     
/* 55 */     if (matcher.group(1) != null) {
/* 56 */       setValue(Integer.valueOf(Integer.parseInt(matcher.group(1))));
/*    */     } else {
/* 58 */       setValue(INFINITE_VALUE);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 64 */     return "Second-" + (getValue().equals(INFINITE_VALUE) ? "infinite" : (String)getValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\TimeoutHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */