/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.HttpResponseFactory;
/*    */ import org.apache.http.NoHttpResponseException;
/*    */ import org.apache.http.ParseException;
/*    */ import org.apache.http.StatusLine;
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
/*    */ public class DefaultHttpResponseParser
/*    */   extends AbstractMessageParser<HttpResponse>
/*    */ {
/*    */   private final HttpResponseFactory responseFactory;
/*    */   private final CharArrayBuffer lineBuf;
/*    */   
/*    */   public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
/* 78 */     super(buffer, parser, params);
/* 79 */     if (responseFactory == null) {
/* 80 */       throw new IllegalArgumentException("Response factory may not be null");
/*    */     }
/* 82 */     this.responseFactory = responseFactory;
/* 83 */     this.lineBuf = new CharArrayBuffer(128);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected HttpResponse parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException, ParseException {
/* 91 */     this.lineBuf.clear();
/* 92 */     int i = sessionBuffer.readLine(this.lineBuf);
/* 93 */     if (i == -1) {
/* 94 */       throw new NoHttpResponseException("The target server failed to respond");
/*    */     }
/*    */     
/* 97 */     ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
/* 98 */     StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
/* 99 */     return this.responseFactory.newHttpResponse(statusline, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\DefaultHttpResponseParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */