/*    */ package org.fourthline.cling.support.model.dlna.message.header;
/*    */ 
/*    */ import java.util.regex.Pattern;
/*    */ import org.fourthline.cling.model.message.header.InvalidHeaderException;
/*    */ import org.fourthline.cling.support.model.dlna.types.ScmsFlagType;
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
/*    */ public class ScmsFlagHeader
/*    */   extends DLNAHeader<ScmsFlagType>
/*    */ {
/* 26 */   static final Pattern pattern = Pattern.compile("^[01]{2}$", 2);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 33 */     if (pattern.matcher(s).matches()) {
/* 34 */       setValue(new ScmsFlagType((s.charAt(0) == '0'), (s.charAt(1) == '0')));
/*    */       return;
/*    */     } 
/* 37 */     throw new InvalidHeaderException("Invalid ScmsFlag header value: " + s);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 42 */     ScmsFlagType v = (ScmsFlagType)getValue();
/* 43 */     return (v.isCopyright() ? "0" : "1") + (v.isOriginal() ? "0" : "1");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\model\dlna\message\header\ScmsFlagHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */