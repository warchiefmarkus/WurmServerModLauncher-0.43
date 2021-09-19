/*     */ package org.fourthline.cling.mock;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.enterprise.inject.Alternative;
/*     */ import org.fourthline.cling.UpnpServiceConfiguration;
/*     */ import org.fourthline.cling.model.NetworkAddress;
/*     */ import org.fourthline.cling.model.message.IncomingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.OutgoingDatagramMessage;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ import org.fourthline.cling.transport.RouterException;
/*     */ import org.fourthline.cling.transport.spi.InitializationException;
/*     */ import org.fourthline.cling.transport.spi.UpnpStream;
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
/*     */ @Alternative
/*     */ public class MockRouter
/*     */   implements Router
/*     */ {
/*  52 */   public int counter = -1;
/*  53 */   public List<IncomingDatagramMessage> incomingDatagramMessages = new ArrayList<>();
/*  54 */   public List<OutgoingDatagramMessage> outgoingDatagramMessages = new ArrayList<>();
/*  55 */   public List<UpnpStream> receivedUpnpStreams = new ArrayList<>();
/*  56 */   public List<StreamRequestMessage> sentStreamRequestMessages = new ArrayList<>();
/*  57 */   public List<byte[]> broadcastedBytes = (List)new ArrayList<>();
/*     */   
/*     */   protected UpnpServiceConfiguration configuration;
/*     */   
/*     */   protected ProtocolFactory protocolFactory;
/*     */   
/*     */   public MockRouter(UpnpServiceConfiguration configuration, ProtocolFactory protocolFactory) {
/*  64 */     this.configuration = configuration;
/*  65 */     this.protocolFactory = protocolFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public UpnpServiceConfiguration getConfiguration() {
/*  70 */     return this.configuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public ProtocolFactory getProtocolFactory() {
/*  75 */     return this.protocolFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean enable() throws RouterException {
/*  80 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean disable() throws RouterException {
/*  85 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() throws RouterException {}
/*     */ 
/*     */   
/*     */   public boolean isEnabled() throws RouterException {
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleStartFailure(InitializationException ex) throws InitializationException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public List<NetworkAddress> getActiveStreamServers(InetAddress preferredAddress) throws RouterException {
/*     */     try {
/* 106 */       return Arrays.asList(new NetworkAddress[] { new NetworkAddress(
/*     */               
/* 108 */               InetAddress.getByName("127.0.0.1"), 0) });
/*     */ 
/*     */     
/*     */     }
/* 112 */     catch (UnknownHostException ex) {
/* 113 */       throw new RuntimeException(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void received(IncomingDatagramMessage msg) {
/* 118 */     this.incomingDatagramMessages.add(msg);
/*     */   }
/*     */   
/*     */   public void received(UpnpStream stream) {
/* 122 */     this.receivedUpnpStreams.add(stream);
/*     */   }
/*     */   
/*     */   public void send(OutgoingDatagramMessage msg) throws RouterException {
/* 126 */     this.outgoingDatagramMessages.add(msg);
/*     */   }
/*     */   
/*     */   public StreamResponseMessage send(StreamRequestMessage msg) throws RouterException {
/* 130 */     this.sentStreamRequestMessages.add(msg);
/* 131 */     this.counter++;
/* 132 */     return (getStreamResponseMessages() != null) ? 
/* 133 */       getStreamResponseMessages()[this.counter] : 
/* 134 */       getStreamResponseMessage(msg);
/*     */   }
/*     */   
/*     */   public void broadcast(byte[] bytes) {
/* 138 */     this.broadcastedBytes.add(bytes);
/*     */   }
/*     */   
/*     */   public void resetStreamRequestMessageCounter() {
/* 142 */     this.counter = -1;
/*     */   }
/*     */   
/*     */   public List<IncomingDatagramMessage> getIncomingDatagramMessages() {
/* 146 */     return this.incomingDatagramMessages;
/*     */   }
/*     */   
/*     */   public List<OutgoingDatagramMessage> getOutgoingDatagramMessages() {
/* 150 */     return this.outgoingDatagramMessages;
/*     */   }
/*     */   
/*     */   public List<UpnpStream> getReceivedUpnpStreams() {
/* 154 */     return this.receivedUpnpStreams;
/*     */   }
/*     */   
/*     */   public List<StreamRequestMessage> getSentStreamRequestMessages() {
/* 158 */     return this.sentStreamRequestMessages;
/*     */   }
/*     */   
/*     */   public List<byte[]> getBroadcastedBytes() {
/* 162 */     return this.broadcastedBytes;
/*     */   }
/*     */   
/*     */   public StreamResponseMessage[] getStreamResponseMessages() {
/* 166 */     return null;
/*     */   }
/*     */   
/*     */   public StreamResponseMessage getStreamResponseMessage(StreamRequestMessage request) {
/* 170 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\mock\MockRouter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */