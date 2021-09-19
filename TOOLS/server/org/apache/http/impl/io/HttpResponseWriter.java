/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.io.SessionOutputBuffer;
/*    */ import org.apache.http.message.LineFormatter;
/*    */ import org.apache.http.params.HttpParams;
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
/*    */ public class HttpResponseWriter
/*    */   extends AbstractMessageWriter<HttpResponse>
/*    */ {
/*    */   public HttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
/* 50 */     super(buffer, formatter, params);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeHeadLine(HttpResponse message) throws IOException {
/* 55 */     this.lineFormatter.formatStatusLine(this.lineBuf, message.getStatusLine());
/* 56 */     this.sessionBuffer.writeLine(this.lineBuf);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\impl\io\HttpResponseWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */