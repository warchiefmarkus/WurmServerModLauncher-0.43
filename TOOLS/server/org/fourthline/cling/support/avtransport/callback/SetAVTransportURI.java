/*    */ package org.fourthline.cling.support.avtransport.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*    */ public abstract class SetAVTransportURI
/*    */   extends ActionCallback
/*    */ {
/* 30 */   private static Logger log = Logger.getLogger(SetAVTransportURI.class.getName());
/*    */   
/*    */   public SetAVTransportURI(Service service, String uri) {
/* 33 */     this(new UnsignedIntegerFourBytes(0L), service, uri, null);
/*    */   }
/*    */   
/*    */   public SetAVTransportURI(Service service, String uri, String metadata) {
/* 37 */     this(new UnsignedIntegerFourBytes(0L), service, uri, metadata);
/*    */   }
/*    */   
/*    */   public SetAVTransportURI(UnsignedIntegerFourBytes instanceId, Service service, String uri) {
/* 41 */     this(instanceId, service, uri, null);
/*    */   }
/*    */   
/*    */   public SetAVTransportURI(UnsignedIntegerFourBytes instanceId, Service service, String uri, String metadata) {
/* 45 */     super(new ActionInvocation(service.getAction("SetAVTransportURI")));
/* 46 */     log.fine("Creating SetAVTransportURI action for URI: " + uri);
/* 47 */     getActionInvocation().setInput("InstanceID", instanceId);
/* 48 */     getActionInvocation().setInput("CurrentURI", uri);
/* 49 */     getActionInvocation().setInput("CurrentURIMetaData", metadata);
/*    */   }
/*    */ 
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 54 */     log.fine("Execution successful");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\SetAVTransportURI.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */