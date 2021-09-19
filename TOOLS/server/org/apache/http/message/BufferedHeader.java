/*     */ package org.apache.http.message;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.http.FormattedHeader;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public class BufferedHeader
/*     */   implements FormattedHeader, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -2768352615787625448L;
/*     */   private final String name;
/*     */   private final CharArrayBuffer buffer;
/*     */   private final int valuePos;
/*     */   
/*     */   public BufferedHeader(CharArrayBuffer buffer) throws ParseException {
/*  78 */     if (buffer == null) {
/*  79 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/*     */     
/*  82 */     int colon = buffer.indexOf(58);
/*  83 */     if (colon == -1) {
/*  84 */       throw new ParseException("Invalid header: " + buffer.toString());
/*     */     }
/*     */     
/*  87 */     String s = buffer.substringTrimmed(0, colon);
/*  88 */     if (s.length() == 0) {
/*  89 */       throw new ParseException("Invalid header: " + buffer.toString());
/*     */     }
/*     */     
/*  92 */     this.buffer = buffer;
/*  93 */     this.name = s;
/*  94 */     this.valuePos = colon + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  99 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 103 */     return this.buffer.substringTrimmed(this.valuePos, this.buffer.length());
/*     */   }
/*     */   
/*     */   public HeaderElement[] getElements() throws ParseException {
/* 107 */     ParserCursor cursor = new ParserCursor(0, this.buffer.length());
/* 108 */     cursor.updatePos(this.valuePos);
/* 109 */     return BasicHeaderValueParser.DEFAULT.parseElements(this.buffer, cursor);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getValuePos() {
/* 114 */     return this.valuePos;
/*     */   }
/*     */   
/*     */   public CharArrayBuffer getBuffer() {
/* 118 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 123 */     return this.buffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException {
/* 130 */     return super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\message\BufferedHeader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */