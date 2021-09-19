/*    */ package org.apache.http.message;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.http.ProtocolVersion;
/*    */ import org.apache.http.RequestLine;
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
/*    */ @Immutable
/*    */ public class BasicRequestLine
/*    */   implements RequestLine, Cloneable, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 2810581718468737193L;
/*    */   private final ProtocolVersion protoversion;
/*    */   private final String method;
/*    */   private final String uri;
/*    */   
/*    */   public BasicRequestLine(String method, String uri, ProtocolVersion version) {
/* 54 */     if (method == null) {
/* 55 */       throw new IllegalArgumentException("Method must not be null.");
/*    */     }
/*    */     
/* 58 */     if (uri == null) {
/* 59 */       throw new IllegalArgumentException("URI must not be null.");
/*    */     }
/*    */     
/* 62 */     if (version == null) {
/* 63 */       throw new IllegalArgumentException("Protocol version must not be null.");
/*    */     }
/*    */     
/* 66 */     this.method = method;
/* 67 */     this.uri = uri;
/* 68 */     this.protoversion = version;
/*    */   }
/*    */   
/*    */   public String getMethod() {
/* 72 */     return this.method;
/*    */   }
/*    */   
/*    */   public ProtocolVersion getProtocolVersion() {
/* 76 */     return this.protoversion;
/*    */   }
/*    */   
/*    */   public String getUri() {
/* 80 */     return this.uri;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return BasicLineFormatter.DEFAULT.formatRequestLine((CharArrayBuffer)null, this).toString();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException {
/* 92 */     return super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BasicRequestLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */