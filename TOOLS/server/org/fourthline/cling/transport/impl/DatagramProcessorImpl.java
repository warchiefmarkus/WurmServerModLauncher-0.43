/*     */ package org.fourthline.cling.transport.impl;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Locale;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.model.UnsupportedDataException;
/*     */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.model.message.UpnpOperation;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.transport.spi.DatagramProcessor;
/*     */ import org.seamless.http.Headers;
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
/*     */ public class DatagramProcessorImpl
/*     */   implements DatagramProcessor
/*     */ {
/*  44 */   private static Logger log = Logger.getLogger(DatagramProcessor.class.getName());
/*     */ 
/*     */ 
/*     */   
/*     */   public IncomingDatagramMessage read(InetAddress receivedOnAddress, DatagramPacket datagram) throws UnsupportedDataException {
/*     */     try {
/*  50 */       if (log.isLoggable(Level.FINER)) {
/*  51 */         log.finer("===================================== DATAGRAM BEGIN ============================================");
/*  52 */         log.finer(new String(datagram.getData(), "UTF-8"));
/*  53 */         log.finer("-===================================== DATAGRAM END =============================================");
/*     */       } 
/*     */       
/*  56 */       ByteArrayInputStream is = new ByteArrayInputStream(datagram.getData());
/*     */       
/*  58 */       String[] startLine = Headers.readLine(is).split(" ");
/*  59 */       if (startLine[0].startsWith("HTTP/1.")) {
/*  60 */         return readResponseMessage(receivedOnAddress, datagram, is, Integer.valueOf(startLine[1]).intValue(), startLine[2], startLine[0]);
/*     */       }
/*  62 */       return readRequestMessage(receivedOnAddress, datagram, is, startLine[0], startLine[2]);
/*     */     
/*     */     }
/*  65 */     catch (Exception ex) {
/*  66 */       throw new UnsupportedDataException("Could not parse headers: " + ex, ex, datagram.getData());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public DatagramPacket write(OutgoingDatagramMessage message) throws UnsupportedDataException {
/*  72 */     StringBuilder statusLine = new StringBuilder();
/*     */     
/*  74 */     UpnpOperation operation = message.getOperation();
/*     */     
/*  76 */     if (operation instanceof UpnpRequest) {
/*     */       
/*  78 */       UpnpRequest requestOperation = (UpnpRequest)operation;
/*  79 */       statusLine.append(requestOperation.getHttpMethodName()).append(" * ");
/*  80 */       statusLine.append("HTTP/1.").append(operation.getHttpMinorVersion()).append("\r\n");
/*     */     }
/*  82 */     else if (operation instanceof UpnpResponse) {
/*  83 */       UpnpResponse responseOperation = (UpnpResponse)operation;
/*  84 */       statusLine.append("HTTP/1.").append(operation.getHttpMinorVersion()).append(" ");
/*  85 */       statusLine.append(responseOperation.getStatusCode()).append(" ").append(responseOperation.getStatusMessage());
/*  86 */       statusLine.append("\r\n");
/*     */     } else {
/*  88 */       throw new UnsupportedDataException("Message operation is not request or response, don't know how to process: " + message);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  94 */     StringBuilder messageData = new StringBuilder();
/*  95 */     messageData.append(statusLine);
/*     */     
/*  97 */     messageData.append(message.getHeaders().toString()).append("\r\n");
/*     */     
/*  99 */     if (log.isLoggable(Level.FINER)) {
/* 100 */       log.finer("Writing message data for: " + message);
/* 101 */       log.finer("---------------------------------------------------------------------------------");
/* 102 */       log.finer(messageData.toString().substring(0, messageData.length() - 2));
/* 103 */       log.finer("---------------------------------------------------------------------------------");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 109 */       byte[] data = messageData.toString().getBytes("US-ASCII");
/*     */       
/* 111 */       log.fine("Writing new datagram packet with " + data.length + " bytes for: " + message);
/* 112 */       return new DatagramPacket(data, data.length, message.getDestinationAddress(), message.getDestinationPort());
/*     */     }
/* 114 */     catch (UnsupportedEncodingException ex) {
/* 115 */       throw new UnsupportedDataException("Can't convert message content to US-ASCII: " + ex
/* 116 */           .getMessage(), ex, messageData);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IncomingDatagramMessage readRequestMessage(InetAddress receivedOnAddress, DatagramPacket datagram, ByteArrayInputStream is, String requestMethod, String httpProtocol) throws Exception {
/* 128 */     UpnpHeaders headers = new UpnpHeaders(is);
/*     */ 
/*     */ 
/*     */     
/* 132 */     UpnpRequest upnpRequest = new UpnpRequest(UpnpRequest.Method.getByHttpName(requestMethod));
/* 133 */     upnpRequest.setHttpMinorVersion(httpProtocol.toUpperCase(Locale.ROOT).equals("HTTP/1.1") ? 1 : 0);
/* 134 */     IncomingDatagramMessage requestMessage = new IncomingDatagramMessage((UpnpOperation)upnpRequest, datagram.getAddress(), datagram.getPort(), receivedOnAddress);
/*     */     
/* 136 */     requestMessage.setHeaders(headers);
/*     */     
/* 138 */     return requestMessage;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected IncomingDatagramMessage readResponseMessage(InetAddress receivedOnAddress, DatagramPacket datagram, ByteArrayInputStream is, int statusCode, String statusMessage, String httpProtocol) throws Exception {
/* 149 */     UpnpHeaders headers = new UpnpHeaders(is);
/*     */ 
/*     */ 
/*     */     
/* 153 */     UpnpResponse upnpResponse = new UpnpResponse(statusCode, statusMessage);
/* 154 */     upnpResponse.setHttpMinorVersion(httpProtocol.toUpperCase(Locale.ROOT).equals("HTTP/1.1") ? 1 : 0);
/* 155 */     IncomingDatagramMessage responseMessage = new IncomingDatagramMessage((UpnpOperation)upnpResponse, datagram.getAddress(), datagram.getPort(), receivedOnAddress);
/*     */     
/* 157 */     responseMessage.setHeaders(headers);
/*     */     
/* 159 */     return responseMessage;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\transport\impl\DatagramProcessorImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */