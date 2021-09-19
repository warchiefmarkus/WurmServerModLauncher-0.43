/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.HttpMessageParser;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.message.BasicLineParser;
/*     */ import org.apache.http.message.LineParser;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractMessageParser<T extends HttpMessage>
/*     */   implements HttpMessageParser<T>
/*     */ {
/*     */   private static final int HEAD_LINE = 0;
/*     */   private static final int HEADERS = 1;
/*     */   private final SessionInputBuffer sessionBuffer;
/*     */   private final int maxHeaderCount;
/*     */   private final int maxLineLen;
/*     */   private final List<CharArrayBuffer> headerLines;
/*     */   protected final LineParser lineParser;
/*     */   private int state;
/*     */   private T message;
/*     */   
/*     */   public AbstractMessageParser(SessionInputBuffer buffer, LineParser parser, HttpParams params) {
/*  88 */     if (buffer == null) {
/*  89 */       throw new IllegalArgumentException("Session input buffer may not be null");
/*     */     }
/*  91 */     if (params == null) {
/*  92 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  94 */     this.sessionBuffer = buffer;
/*  95 */     this.maxHeaderCount = params.getIntParameter("http.connection.max-header-count", -1);
/*     */     
/*  97 */     this.maxLineLen = params.getIntParameter("http.connection.max-line-length", -1);
/*     */     
/*  99 */     this.lineParser = (parser != null) ? parser : (LineParser)BasicLineParser.DEFAULT;
/* 100 */     this.headerLines = new ArrayList<CharArrayBuffer>();
/* 101 */     this.state = 0;
/*     */   }
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
/*     */   public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser) throws HttpException, IOException {
/*     */     BasicLineParser basicLineParser;
/* 129 */     if (parser == null) {
/* 130 */       basicLineParser = BasicLineParser.DEFAULT;
/*     */     }
/* 132 */     List<CharArrayBuffer> headerLines = new ArrayList<CharArrayBuffer>();
/* 133 */     return parseHeaders(inbuffer, maxHeaderCount, maxLineLen, (LineParser)basicLineParser, headerLines);
/*     */   }
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
/*     */   public static Header[] parseHeaders(SessionInputBuffer inbuffer, int maxHeaderCount, int maxLineLen, LineParser parser, List<CharArrayBuffer> headerLines) throws HttpException, IOException {
/* 168 */     if (inbuffer == null) {
/* 169 */       throw new IllegalArgumentException("Session input buffer may not be null");
/*     */     }
/* 171 */     if (parser == null) {
/* 172 */       throw new IllegalArgumentException("Line parser may not be null");
/*     */     }
/* 174 */     if (headerLines == null) {
/* 175 */       throw new IllegalArgumentException("Header line list may not be null");
/*     */     }
/*     */     
/* 178 */     CharArrayBuffer current = null;
/* 179 */     CharArrayBuffer previous = null;
/*     */     while (true) {
/* 181 */       if (current == null) {
/* 182 */         current = new CharArrayBuffer(64);
/*     */       } else {
/* 184 */         current.clear();
/*     */       } 
/* 186 */       int l = inbuffer.readLine(current);
/* 187 */       if (l == -1 || current.length() < 1) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 194 */       if ((current.charAt(0) == ' ' || current.charAt(0) == '\t') && previous != null) {
/*     */ 
/*     */         
/* 197 */         int j = 0;
/* 198 */         while (j < current.length()) {
/* 199 */           char ch = current.charAt(j);
/* 200 */           if (ch != ' ' && ch != '\t') {
/*     */             break;
/*     */           }
/* 203 */           j++;
/*     */         } 
/* 205 */         if (maxLineLen > 0 && previous.length() + 1 + current.length() - j > maxLineLen)
/*     */         {
/* 207 */           throw new IOException("Maximum line length limit exceeded");
/*     */         }
/* 209 */         previous.append(' ');
/* 210 */         previous.append(current, j, current.length() - j);
/*     */       } else {
/* 212 */         headerLines.add(current);
/* 213 */         previous = current;
/* 214 */         current = null;
/*     */       } 
/* 216 */       if (maxHeaderCount > 0 && headerLines.size() >= maxHeaderCount) {
/* 217 */         throw new IOException("Maximum header count exceeded");
/*     */       }
/*     */     } 
/* 220 */     Header[] headers = new Header[headerLines.size()];
/* 221 */     for (int i = 0; i < headerLines.size(); i++) {
/* 222 */       CharArrayBuffer buffer = headerLines.get(i);
/*     */       try {
/* 224 */         headers[i] = parser.parseHeader(buffer);
/* 225 */       } catch (ParseException ex) {
/* 226 */         throw new ProtocolException(ex.getMessage());
/*     */       } 
/*     */     } 
/* 229 */     return headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T parseHead(SessionInputBuffer paramSessionInputBuffer) throws IOException, HttpException, ParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T parse() throws IOException, HttpException {
/*     */     Header[] headers;
/*     */     T result;
/* 250 */     int st = this.state;
/* 251 */     switch (st) {
/*     */       case 0:
/*     */         try {
/* 254 */           this.message = parseHead(this.sessionBuffer);
/* 255 */         } catch (ParseException px) {
/* 256 */           throw new ProtocolException(px.getMessage(), px);
/*     */         } 
/* 258 */         this.state = 1;
/*     */       
/*     */       case 1:
/* 261 */         headers = parseHeaders(this.sessionBuffer, this.maxHeaderCount, this.maxLineLen, this.lineParser, this.headerLines);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 267 */         this.message.setHeaders(headers);
/* 268 */         result = this.message;
/* 269 */         this.message = null;
/* 270 */         this.headerLines.clear();
/* 271 */         this.state = 0;
/* 272 */         return result;
/*     */     } 
/* 274 */     throw new IllegalStateException("Inconsistent parser state");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\AbstractMessageParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */