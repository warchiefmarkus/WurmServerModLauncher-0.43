/*     */ package org.fourthline.cling.protocol.sync;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.UpnpService;
/*     */ import org.fourthline.cling.binding.xml.DescriptorBindingException;
/*     */ import org.fourthline.cling.binding.xml.DeviceDescriptorBinder;
/*     */ import org.fourthline.cling.binding.xml.ServiceDescriptorBinder;
/*     */ import org.fourthline.cling.model.message.StreamRequestMessage;
/*     */ import org.fourthline.cling.model.message.StreamResponseMessage;
/*     */ import org.fourthline.cling.model.message.UpnpRequest;
/*     */ import org.fourthline.cling.model.message.UpnpResponse;
/*     */ import org.fourthline.cling.model.message.header.ContentTypeHeader;
/*     */ import org.fourthline.cling.model.message.header.ServerHeader;
/*     */ import org.fourthline.cling.model.message.header.UpnpHeader;
/*     */ import org.fourthline.cling.model.meta.Device;
/*     */ import org.fourthline.cling.model.meta.Icon;
/*     */ import org.fourthline.cling.model.meta.LocalDevice;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.resource.DeviceDescriptorResource;
/*     */ import org.fourthline.cling.model.resource.IconResource;
/*     */ import org.fourthline.cling.model.resource.Resource;
/*     */ import org.fourthline.cling.model.resource.ServiceDescriptorResource;
/*     */ import org.fourthline.cling.protocol.ReceivingSync;
/*     */ import org.fourthline.cling.transport.RouterException;
/*     */ import org.seamless.util.Exceptions;
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
/*     */ public class ReceivingRetrieval
/*     */   extends ReceivingSync<StreamRequestMessage, StreamResponseMessage>
/*     */ {
/*  58 */   private static final Logger log = Logger.getLogger(ReceivingRetrieval.class.getName());
/*     */   
/*     */   public ReceivingRetrieval(UpnpService upnpService, StreamRequestMessage inputMessage) {
/*  61 */     super(upnpService, inputMessage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected StreamResponseMessage executeSync() throws RouterException {
/*  66 */     if (!((StreamRequestMessage)getInputMessage()).hasHostHeader()) {
/*  67 */       log.fine("Ignoring message, missing HOST header: " + getInputMessage());
/*  68 */       return new StreamResponseMessage(new UpnpResponse(UpnpResponse.Status.PRECONDITION_FAILED));
/*     */     } 
/*     */     
/*  71 */     URI requestedURI = ((UpnpRequest)((StreamRequestMessage)getInputMessage()).getOperation()).getURI();
/*     */     
/*  73 */     Resource foundResource = getUpnpService().getRegistry().getResource(requestedURI);
/*     */     
/*  75 */     if (foundResource == null) {
/*  76 */       foundResource = onResourceNotFound(requestedURI);
/*  77 */       if (foundResource == null) {
/*  78 */         log.fine("No local resource found: " + getInputMessage());
/*  79 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/*  83 */     return createResponse(requestedURI, foundResource);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected StreamResponseMessage createResponse(URI requestedURI, Resource resource) {
/*     */     StreamResponseMessage response;
/*     */     try {
/*  92 */       if (DeviceDescriptorResource.class.isAssignableFrom(resource.getClass()))
/*     */       {
/*  94 */         log.fine("Found local device matching relative request URI: " + requestedURI);
/*  95 */         LocalDevice device = (LocalDevice)resource.getModel();
/*     */ 
/*     */         
/*  98 */         DeviceDescriptorBinder deviceDescriptorBinder = getUpnpService().getConfiguration().getDeviceDescriptorBinderUDA10();
/*  99 */         String deviceDescriptor = deviceDescriptorBinder.generate((Device)device, 
/*     */             
/* 101 */             getRemoteClientInfo(), 
/* 102 */             getUpnpService().getConfiguration().getNamespace());
/*     */         
/* 104 */         response = new StreamResponseMessage(deviceDescriptor, new ContentTypeHeader(ContentTypeHeader.DEFAULT_CONTENT_TYPE));
/*     */ 
/*     */       
/*     */       }
/* 108 */       else if (ServiceDescriptorResource.class.isAssignableFrom(resource.getClass()))
/*     */       {
/*     */         
/* 111 */         log.fine("Found local service matching relative request URI: " + requestedURI);
/* 112 */         LocalService service = (LocalService)resource.getModel();
/*     */ 
/*     */         
/* 115 */         ServiceDescriptorBinder serviceDescriptorBinder = getUpnpService().getConfiguration().getServiceDescriptorBinderUDA10();
/* 116 */         String serviceDescriptor = serviceDescriptorBinder.generate((Service)service);
/* 117 */         response = new StreamResponseMessage(serviceDescriptor, new ContentTypeHeader(ContentTypeHeader.DEFAULT_CONTENT_TYPE));
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 122 */       else if (IconResource.class.isAssignableFrom(resource.getClass()))
/*     */       {
/* 124 */         log.fine("Found local icon matching relative request URI: " + requestedURI);
/* 125 */         Icon icon = (Icon)resource.getModel();
/* 126 */         response = new StreamResponseMessage(icon.getData(), icon.getMimeType());
/*     */       }
/*     */       else
/*     */       {
/* 130 */         log.fine("Ignoring GET for found local resource: " + resource);
/* 131 */         return null;
/*     */       }
/*     */     
/* 134 */     } catch (DescriptorBindingException ex) {
/* 135 */       log.warning("Error generating requested device/service descriptor: " + ex.toString());
/* 136 */       log.log(Level.WARNING, "Exception root cause: ", Exceptions.unwrap((Throwable)ex));
/* 137 */       response = new StreamResponseMessage(UpnpResponse.Status.INTERNAL_SERVER_ERROR);
/*     */     } 
/*     */     
/* 140 */     response.getHeaders().add(UpnpHeader.Type.SERVER, (UpnpHeader)new ServerHeader());
/*     */     
/* 142 */     return response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Resource onResourceNotFound(URI requestedURIPath) {
/* 152 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\protocol\sync\ReceivingRetrieval.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */