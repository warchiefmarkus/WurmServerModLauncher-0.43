/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.ConnectionClosedException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpRequest;
/*    */ import org.apache.http.HttpRequestFactory;
/*    */ import org.apache.http.ParseException;
/*    */ import org.apache.http.RequestLine;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.SessionInputBuffer;
/*    */ import org.apache.http.message.LineParser;
/*    */ import org.apache.http.message.ParserCursor;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ @NotThreadSafe
/*    */ public class DefaultHttpRequestParser
/*    */   extends AbstractMessageParser<HttpRequest>
/*    */ {
/*    */   private final HttpRequestFactory requestFactory;
/*    */   private final CharArrayBuffer lineBuf;
/*    */   
/*    */   public DefaultHttpRequestParser(SessionInputBuffer buffer, LineParser parser, HttpRequestFactory requestFactory, HttpParams params) {
/* 78 */     super(buffer, parser, params);
/* 79 */     if (requestFactory == null) {
/* 80 */       throw new IllegalArgumentException("Request factory may not be null");
/*    */     }
/* 82 */     this.requestFactory = requestFactory;
/* 83 */     this.lineBuf = new CharArrayBuffer(128);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpRequest parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
/* 91 */     this.lineBuf.clear();
/* 92 */     int i = sessionBuffer.readLine(this.lineBuf);
/* 93 */     if (i == -1) {
/* 94 */       throw new ConnectionClosedException("Client closed connection");
/*    */     }
/* 96 */     ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
/* 97 */     RequestLine requestline = this.lineParser.parseRequestLine(this.lineBuf, cursor);
/* 98 */     return this.requestFactory.newHttpRequest(requestline);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\DefaultHttpRequestParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */