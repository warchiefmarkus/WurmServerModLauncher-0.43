/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.NoHttpResponseException;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.ProtocolException;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.impl.io.AbstractMessageParser;
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
/*     */ @ThreadSafe
/*     */ public class DefaultHttpResponseParser
/*     */   extends AbstractMessageParser<HttpResponse>
/*     */ {
/*  64 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final HttpResponseFactory responseFactory;
/*     */ 
/*     */   
/*     */   private final CharArrayBuffer lineBuf;
/*     */ 
/*     */   
/*     */   public DefaultHttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
/*  74 */     super(buffer, parser, params);
/*  75 */     if (responseFactory == null) {
/*  76 */       throw new IllegalArgumentException("Response factory may not be null");
/*     */     }
/*     */     
/*  79 */     this.responseFactory = responseFactory;
/*  80 */     this.lineBuf = new CharArrayBuffer(128);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpResponse parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException {
/*  87 */     int count = 0;
/*  88 */     ParserCursor cursor = null;
/*     */     
/*     */     while (true) {
/*  91 */       this.lineBuf.clear();
/*  92 */       int i = sessionBuffer.readLine(this.lineBuf);
/*  93 */       if (i == -1 && count == 0)
/*     */       {
/*  95 */         throw new NoHttpResponseException("The target server failed to respond");
/*     */       }
/*  97 */       cursor = new ParserCursor(0, this.lineBuf.length());
/*  98 */       if (this.lineParser.hasProtocolVersion(this.lineBuf, cursor)) {
/*     */         break;
/*     */       }
/* 101 */       if (i == -1 || reject(this.lineBuf, count))
/*     */       {
/* 103 */         throw new ProtocolException("The server failed to respond with a valid HTTP response");
/*     */       }
/*     */       
/* 106 */       if (this.log.isDebugEnabled()) {
/* 107 */         this.log.debug("Garbage in response: " + this.lineBuf.toString());
/*     */       }
/* 109 */       count++;
/*     */     } 
/*     */     
/* 112 */     StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
/* 113 */     return this.responseFactory.newHttpResponse(statusline, null);
/*     */   }
/*     */   
/*     */   protected boolean reject(CharArrayBuffer line, int count) {
/* 117 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\DefaultHttpResponseParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */