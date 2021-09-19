/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HeaderIterator;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.HttpMessageWriter;
/*    */ import org.apache.http.io.SessionOutputBuffer;
/*    */ import org.apache.http.message.BasicLineFormatter;
/*    */ import org.apache.http.message.LineFormatter;
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
/*    */ @NotThreadSafe
/*    */ public abstract class AbstractMessageWriter<T extends HttpMessage>
/*    */   implements HttpMessageWriter<T>
/*    */ {
/*    */   protected final SessionOutputBuffer sessionBuffer;
/*    */   protected final CharArrayBuffer lineBuf;
/*    */   protected final LineFormatter lineFormatter;
/*    */   
/*    */   public AbstractMessageWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
/* 68 */     if (buffer == null) {
/* 69 */       throw new IllegalArgumentException("Session input buffer may not be null");
/*    */     }
/* 71 */     this.sessionBuffer = buffer;
/* 72 */     this.lineBuf = new CharArrayBuffer(128);
/* 73 */     this.lineFormatter = (formatter != null) ? formatter : (LineFormatter)BasicLineFormatter.DEFAULT;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract void writeHeadLine(T paramT) throws IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(T message) throws IOException, HttpException {
/* 87 */     if (message == null) {
/* 88 */       throw new IllegalArgumentException("HTTP message may not be null");
/*    */     }
/* 90 */     writeHeadLine(message);
/* 91 */     for (HeaderIterator it = message.headerIterator(); it.hasNext(); ) {
/* 92 */       Header header = it.nextHeader();
/* 93 */       this.sessionBuffer.writeLine(this.lineFormatter.formatHeader(this.lineBuf, header));
/*    */     } 
/*    */     
/* 96 */     this.lineBuf.clear();
/* 97 */     this.sessionBuffer.writeLine(this.lineBuf);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\AbstractMessageWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */