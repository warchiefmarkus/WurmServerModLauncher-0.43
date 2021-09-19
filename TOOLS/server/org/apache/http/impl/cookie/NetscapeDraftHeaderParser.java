/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.HeaderElement;
/*     */ import org.apache.http.NameValuePair;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.message.BasicHeaderElement;
/*     */ import org.apache.http.message.BasicNameValuePair;
/*     */ import org.apache.http.message.ParserCursor;
/*     */ import org.apache.http.protocol.HTTP;
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
/*     */ @Immutable
/*     */ public class NetscapeDraftHeaderParser
/*     */ {
/*  51 */   public static final NetscapeDraftHeaderParser DEFAULT = new NetscapeDraftHeaderParser();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HeaderElement parseHeader(CharArrayBuffer buffer, ParserCursor cursor) throws ParseException {
/*  60 */     if (buffer == null) {
/*  61 */       throw new IllegalArgumentException("Char array buffer may not be null");
/*     */     }
/*  63 */     if (cursor == null) {
/*  64 */       throw new IllegalArgumentException("Parser cursor may not be null");
/*     */     }
/*  66 */     NameValuePair nvp = parseNameValuePair(buffer, cursor);
/*  67 */     List<NameValuePair> params = new ArrayList<NameValuePair>();
/*  68 */     while (!cursor.atEnd()) {
/*  69 */       NameValuePair param = parseNameValuePair(buffer, cursor);
/*  70 */       params.add(param);
/*     */     } 
/*  72 */     return (HeaderElement)new BasicHeaderElement(nvp.getName(), nvp.getValue(), params.<NameValuePair>toArray(new NameValuePair[params.size()]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private NameValuePair parseNameValuePair(CharArrayBuffer buffer, ParserCursor cursor) {
/*  79 */     boolean terminated = false;
/*     */     
/*  81 */     int pos = cursor.getPos();
/*  82 */     int indexFrom = cursor.getPos();
/*  83 */     int indexTo = cursor.getUpperBound();
/*     */ 
/*     */     
/*  86 */     String name = null;
/*  87 */     while (pos < indexTo) {
/*  88 */       char ch = buffer.charAt(pos);
/*  89 */       if (ch == '=') {
/*     */         break;
/*     */       }
/*  92 */       if (ch == ';') {
/*  93 */         terminated = true;
/*     */         break;
/*     */       } 
/*  96 */       pos++;
/*     */     } 
/*     */     
/*  99 */     if (pos == indexTo) {
/* 100 */       terminated = true;
/* 101 */       name = buffer.substringTrimmed(indexFrom, indexTo);
/*     */     } else {
/* 103 */       name = buffer.substringTrimmed(indexFrom, pos);
/* 104 */       pos++;
/*     */     } 
/*     */     
/* 107 */     if (terminated) {
/* 108 */       cursor.updatePos(pos);
/* 109 */       return (NameValuePair)new BasicNameValuePair(name, null);
/*     */     } 
/*     */ 
/*     */     
/* 113 */     String value = null;
/* 114 */     int i1 = pos;
/*     */     
/* 116 */     while (pos < indexTo) {
/* 117 */       char ch = buffer.charAt(pos);
/* 118 */       if (ch == ';') {
/* 119 */         terminated = true;
/*     */         break;
/*     */       } 
/* 122 */       pos++;
/*     */     } 
/*     */     
/* 125 */     int i2 = pos;
/*     */     
/* 127 */     while (i1 < i2 && HTTP.isWhitespace(buffer.charAt(i1))) {
/* 128 */       i1++;
/*     */     }
/*     */     
/* 131 */     while (i2 > i1 && HTTP.isWhitespace(buffer.charAt(i2 - 1))) {
/* 132 */       i2--;
/*     */     }
/* 134 */     value = buffer.substring(i1, i2);
/* 135 */     if (terminated) {
/* 136 */       pos++;
/*     */     }
/* 138 */     cursor.updatePos(pos);
/* 139 */     return (NameValuePair)new BasicNameValuePair(name, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\cookie\NetscapeDraftHeaderParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */