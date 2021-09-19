/*    */ package org.fourthline.cling.model.message;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.net.URL;
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
/*    */ public class StreamRequestMessage
/*    */   extends UpnpMessage<UpnpRequest>
/*    */ {
/*    */   protected Connection connection;
/*    */   
/*    */   public StreamRequestMessage(StreamRequestMessage source) {
/* 31 */     super(source);
/* 32 */     this.connection = source.getConnection();
/*    */   }
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest operation) {
/* 36 */     super(operation);
/*    */   }
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest.Method method, URI uri) {
/* 40 */     super(new UpnpRequest(method, uri));
/*    */   }
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest.Method method, URL url) {
/* 44 */     super(new UpnpRequest(method, url));
/*    */   }
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest operation, String body) {
/* 48 */     super(operation, UpnpMessage.BodyType.STRING, body);
/*    */   }
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest.Method method, URI uri, String body) {
/* 52 */     super(new UpnpRequest(method, uri), UpnpMessage.BodyType.STRING, body);
/*    */   }
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest.Method method, URL url, String body) {
/* 56 */     super(new UpnpRequest(method, url), UpnpMessage.BodyType.STRING, body);
/*    */   }
/*    */ 
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest operation, byte[] body) {
/* 61 */     super(operation, UpnpMessage.BodyType.BYTES, body);
/*    */   }
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest.Method method, URI uri, byte[] body) {
/* 65 */     super(new UpnpRequest(method, uri), UpnpMessage.BodyType.BYTES, body);
/*    */   }
/*    */   
/*    */   public StreamRequestMessage(UpnpRequest.Method method, URL url, byte[] body) {
/* 69 */     super(new UpnpRequest(method, url), UpnpMessage.BodyType.BYTES, body);
/*    */   }
/*    */   
/*    */   public URI getUri() {
/* 73 */     return getOperation().getURI();
/*    */   }
/*    */   
/*    */   public void setUri(URI uri) {
/* 77 */     getOperation().setUri(uri);
/*    */   }
/*    */   
/*    */   public void setConnection(Connection connection) {
/* 81 */     this.connection = connection;
/*    */   }
/*    */   
/*    */   public Connection getConnection() {
/* 85 */     return this.connection;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\message\StreamRequestMessage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */