/*     */ package org.fourthline.cling.support.shared;
/*     */ import java.util.ArrayList;
/*     */ import java.util.logging.Level;
/*     */ import org.fourthline.cling.binding.xml.ServiceDescriptorBinder;
/*     */ import org.fourthline.cling.model.DefaultServiceManager;
/*     */ import org.fourthline.cling.model.message.UpnpHeaders;
/*     */ import org.fourthline.cling.protocol.ProtocolFactory;
/*     */ import org.fourthline.cling.protocol.RetrieveRemoteDescriptors;
/*     */ import org.fourthline.cling.protocol.sync.ReceivingEvent;
/*     */ import org.fourthline.cling.protocol.sync.ReceivingRetrieval;
/*     */ import org.fourthline.cling.protocol.sync.ReceivingSubscribe;
/*     */ import org.fourthline.cling.protocol.sync.SendingAction;
/*     */ import org.fourthline.cling.protocol.sync.SendingEvent;
/*     */ import org.fourthline.cling.protocol.sync.SendingRenewal;
/*     */ import org.fourthline.cling.protocol.sync.SendingSubscribe;
/*     */ import org.fourthline.cling.protocol.sync.SendingUnsubscribe;
/*     */ import org.fourthline.cling.registry.Registry;
/*     */ import org.fourthline.cling.transport.Router;
/*     */ import org.fourthline.cling.transport.spi.DatagramProcessor;
/*     */ import org.fourthline.cling.transport.spi.GENAEventProcessor;
/*     */ import org.fourthline.cling.transport.spi.MulticastReceiver;
/*     */ import org.fourthline.cling.transport.spi.SOAPActionProcessor;
/*     */ import org.fourthline.cling.transport.spi.StreamClient;
/*     */ import org.fourthline.cling.transport.spi.UpnpStream;
/*     */ import org.seamless.swing.logging.LogCategory;
/*     */ 
/*     */ public class CoreLogCategories extends ArrayList<LogCategory> {
/*     */   public CoreLogCategories() {
/*  29 */     super(10);
/*     */     
/*  31 */     add(new LogCategory("Network", new LogCategory.Group[] { new LogCategory.Group("UDP communication", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(DatagramIO.class
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  36 */                   .getName(), Level.FINE), new LogCategory.LoggerLevel(MulticastReceiver.class
/*  37 */                   .getName(), Level.FINE) }), new LogCategory.Group("UDP datagram processing and content", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(DatagramProcessor.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  44 */                   .getName(), Level.FINER) }), new LogCategory.Group("TCP communication", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(UpnpStream.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  51 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(StreamServer.class
/*  52 */                   .getName(), Level.FINE), new LogCategory.LoggerLevel(StreamClient.class
/*  53 */                   .getName(), Level.FINE) }), new LogCategory.Group("SOAP action message processing and content", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(SOAPActionProcessor.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  60 */                   .getName(), Level.FINER) }), new LogCategory.Group("GENA event message processing and content", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(GENAEventProcessor.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  67 */                   .getName(), Level.FINER) }), new LogCategory.Group("HTTP header processing", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(UpnpHeaders.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  74 */                   .getName(), Level.FINER) }) }));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  80 */     add(new LogCategory("UPnP Protocol", new LogCategory.Group[] { new LogCategory.Group("Discovery (Notification & Search)", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(ProtocolFactory.class
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  85 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel("org.fourthline.cling.protocol.async", Level.FINER) }), new LogCategory.Group("Description", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(ProtocolFactory.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*  93 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(RetrieveRemoteDescriptors.class
/*  94 */                   .getName(), Level.FINE), new LogCategory.LoggerLevel(ReceivingRetrieval.class
/*  95 */                   .getName(), Level.FINE), new LogCategory.LoggerLevel(DeviceDescriptorBinder.class
/*  96 */                   .getName(), Level.FINE), new LogCategory.LoggerLevel(ServiceDescriptorBinder.class
/*  97 */                   .getName(), Level.FINE) }), new LogCategory.Group("Control", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(ProtocolFactory.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 104 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(ReceivingAction.class
/* 105 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(SendingAction.class
/* 106 */                   .getName(), Level.FINER) }), new LogCategory.Group("GENA ", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel("org.fourthline.cling.model.gena", Level.FINER), new LogCategory.LoggerLevel(ProtocolFactory.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 114 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(ReceivingEvent.class
/* 115 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(ReceivingSubscribe.class
/* 116 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(ReceivingUnsubscribe.class
/* 117 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(SendingEvent.class
/* 118 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(SendingSubscribe.class
/* 119 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(SendingUnsubscribe.class
/* 120 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel(SendingRenewal.class
/* 121 */                   .getName(), Level.FINER) }) }));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 126 */     add(new LogCategory("Core", new LogCategory.Group[] { new LogCategory.Group("Router", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(Router.class
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 131 */                   .getName(), Level.FINER) }), new LogCategory.Group("Registry", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel(Registry.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 138 */                   .getName(), Level.FINER) }), new LogCategory.Group("Local service binding & invocation", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel("org.fourthline.cling.binding.annotations", Level.FINER), new LogCategory.LoggerLevel(LocalService.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/* 146 */                   .getName(), Level.FINER), new LogCategory.LoggerLevel("org.fourthline.cling.model.action", Level.FINER), new LogCategory.LoggerLevel("org.fourthline.cling.model.state", Level.FINER), new LogCategory.LoggerLevel(DefaultServiceManager.class
/*     */ 
/*     */                   
/* 149 */                   .getName(), Level.FINER) }), new LogCategory.Group("Control Point interaction", new LogCategory.LoggerLevel[] { new LogCategory.LoggerLevel("org.fourthline.cling.controlpoint", Level.FINER) }) }));
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\CoreLogCategories.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */