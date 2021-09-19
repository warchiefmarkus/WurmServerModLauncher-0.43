/*    */ package org.fourthline.cling.model.message.header;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ public class MaxAgeHeader
/*    */   extends UpnpHeader<Integer>
/*    */ {
/* 30 */   public static final Pattern MAX_AGE_REGEX = Pattern.compile(".*max-age\\s*=\\s*([0-9]+).*");
/*    */   
/*    */   public MaxAgeHeader(Integer maxAge) {
/* 33 */     setValue(maxAge);
/*    */   }
/*    */   
/*    */   public MaxAgeHeader() {
/* 37 */     setValue(Integer.valueOf(1800));
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 42 */     Matcher matcher = MAX_AGE_REGEX.matcher(s.toLowerCase(Locale.ROOT));
/* 43 */     if (!matcher.matches()) {
/* 44 */       throw new InvalidHeaderException("Invalid cache-control value, can't parse max-age seconds: " + s);
/*    */     }
/*    */     
/* 47 */     Integer maxAge = Integer.valueOf(Integer.parseInt(matcher.group(1)));
/* 48 */     setValue(maxAge);
/*    */   }
/*    */   
/*    */   public String getString() {
/* 52 */     return "max-age=" + getValue().toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\MaxAgeHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */