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
/*    */ public class MANHeader
/*    */   extends UpnpHeader<String>
/*    */ {
/* 26 */   public static final Pattern PATTERN = Pattern.compile("\"(.+?)\"(;.+?)??");
/* 27 */   public static final Pattern NAMESPACE_PATTERN = Pattern.compile(";\\s?ns\\s?=\\s?([0-9]{2})");
/*    */   
/*    */   public String namespace;
/*    */ 
/*    */   
/*    */   public MANHeader() {}
/*    */   
/*    */   public MANHeader(String value) {
/* 35 */     setValue(value);
/*    */   }
/*    */   
/*    */   public MANHeader(String value, String namespace) {
/* 39 */     this(value);
/* 40 */     this.namespace = namespace;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setString(String s) throws InvalidHeaderException {
/* 45 */     Matcher matcher = PATTERN.matcher(s);
/* 46 */     if (matcher.matches()) {
/* 47 */       setValue(matcher.group(1));
/*    */       
/* 49 */       if (matcher.group(2) != null) {
/* 50 */         Matcher nsMatcher = NAMESPACE_PATTERN.matcher(matcher.group(2));
/* 51 */         if (nsMatcher.matches()) {
/* 52 */           setNamespace(nsMatcher.group(1));
/*    */         } else {
/* 54 */           throw new InvalidHeaderException("Invalid namespace in MAN header value: " + s);
/*    */         } 
/*    */       } 
/*    */     } else {
/*    */       
/* 59 */       throw new InvalidHeaderException("Invalid MAN header value: " + s);
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getString() {
/* 64 */     if (getValue() == null) return null; 
/* 65 */     StringBuilder s = new StringBuilder();
/* 66 */     s.append("\"").append(getValue()).append("\"");
/* 67 */     if (getNamespace() != null) s.append("; ns=").append(getNamespace()); 
/* 68 */     return s.toString();
/*    */   }
/*    */   
/*    */   public String getNamespace() {
/* 72 */     return this.namespace;
/*    */   }
/*    */   
/*    */   public void setNamespace(String namespace) {
/* 76 */     this.namespace = namespace;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\header\MANHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */