/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.message.LineParser;
/*     */ import org.apache.http.message.ParserCursor;
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
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @NotThreadSafe
/*     */ public class HttpRequestParser
/*     */   extends AbstractMessageParser<HttpMessage>
/*     */ {
/*     */   private final HttpRequestFactory requestFactory;
/*     */   private final CharArrayBuffer lineBuf;
/*     */   
/*     */   public HttpRequestParser(SessionInputBuffer buffer, LineParser parser, HttpRequestFactory requestFactory, HttpParams params) {
/*  82 */     super(buffer, parser, params);
/*  83 */     if (requestFactory == null) {
/*  84 */       throw new IllegalArgumentException("Request factory may not be null");
/*     */     }
/*  86 */     this.requestFactory = requestFactory;
/*  87 */     this.lineBuf = new CharArrayBuffer(128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpMessage parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
/*  95 */     this.lineBuf.clear();
/*  96 */     int i = sessionBuffer.readLine(this.lineBuf);
/*  97 */     if (i == -1) {
/*  98 */       throw new ConnectionClosedException("Client closed connection");
/*     */     }
/* 100 */     ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
/* 101 */     RequestLine requestline = this.lineParser.parseRequestLine(this.lineBuf, cursor);
/* 102 */     return (HttpMessage)this.requestFactory.newHttpRequest(requestline);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\HttpRequestParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */