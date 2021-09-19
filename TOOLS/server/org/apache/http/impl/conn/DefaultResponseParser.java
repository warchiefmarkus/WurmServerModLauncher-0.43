/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.NoHttpResponseException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class DefaultResponseParser
/*     */   extends AbstractMessageParser<HttpMessage>
/*     */ {
/*  67 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private final HttpResponseFactory responseFactory;
/*     */   
/*     */   private final CharArrayBuffer lineBuf;
/*     */   
/*     */   private final int maxGarbageLines;
/*     */ 
/*     */   
/*     */   public DefaultResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params) {
/*  78 */     super(buffer, parser, params);
/*  79 */     if (responseFactory == null) {
/*  80 */       throw new IllegalArgumentException("Response factory may not be null");
/*     */     }
/*     */     
/*  83 */     this.responseFactory = responseFactory;
/*  84 */     this.lineBuf = new CharArrayBuffer(128);
/*  85 */     this.maxGarbageLines = getMaxGarbageLines(params);
/*     */   }
/*     */   
/*     */   protected int getMaxGarbageLines(HttpParams params) {
/*  89 */     return params.getIntParameter("http.connection.max-status-line-garbage", 2147483647);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected HttpMessage parseHead(SessionInputBuffer sessionBuffer) throws IOException, HttpException {
/*  98 */     int count = 0;
/*  99 */     ParserCursor cursor = null;
/*     */     
/*     */     while (true) {
/* 102 */       this.lineBuf.clear();
/* 103 */       int i = sessionBuffer.readLine(this.lineBuf);
/* 104 */       if (i == -1 && count == 0)
/*     */       {
/* 106 */         throw new NoHttpResponseException("The target server failed to respond");
/*     */       }
/* 108 */       cursor = new ParserCursor(0, this.lineBuf.length());
/* 109 */       if (this.lineParser.hasProtocolVersion(this.lineBuf, cursor)) {
/*     */         break;
/*     */       }
/* 112 */       if (i == -1 || count >= this.maxGarbageLines)
/*     */       {
/* 114 */         throw new ProtocolException("The server failed to respond with a valid HTTP response");
/*     */       }
/*     */       
/* 117 */       if (this.log.isDebugEnabled()) {
/* 118 */         this.log.debug("Garbage in response: " + this.lineBuf.toString());
/*     */       }
/* 120 */       count++;
/*     */     } 
/*     */     
/* 123 */     StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
/* 124 */     return (HttpMessage)this.responseFactory.newHttpResponse(statusline, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\conn\DefaultResponseParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */