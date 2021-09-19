/*    */ package org.apache.http.message;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HeaderElement;
/*    */ import org.apache.http.ParseException;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.util.CharArrayBuffer;
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
/*    */ @Immutable
/*    */ public class BasicHeader
/*    */   implements Header, Cloneable, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -5427236326487562174L;
/*    */   private final String name;
/*    */   private final String value;
/*    */   
/*    */   public BasicHeader(String name, String value) {
/* 58 */     if (name == null) {
/* 59 */       throw new IllegalArgumentException("Name may not be null");
/*    */     }
/* 61 */     this.name = name;
/* 62 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 66 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 70 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 76 */     return BasicLineFormatter.DEFAULT.formatHeader((CharArrayBuffer)null, this).toString();
/*    */   }
/*    */   
/*    */   public HeaderElement[] getElements() throws ParseException {
/* 80 */     if (this.value != null)
/*    */     {
/* 82 */       return BasicHeaderValueParser.parseElements(this.value, (HeaderValueParser)null);
/*    */     }
/* 84 */     return new HeaderElement[0];
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 90 */     return super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicHeader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */