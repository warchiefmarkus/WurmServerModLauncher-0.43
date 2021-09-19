/*    */ package org.fourthline.cling.support.avtransport.callback;
/*    */ 
/*    */ import java.util.logging.Logger;
/*    */ import org.fourthline.cling.controlpoint.ActionCallback;
/*    */ import org.fourthline.cling.model.action.ActionInvocation;
/*    */ import org.fourthline.cling.model.meta.Service;
/*    */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*    */ import org.fourthline.cling.support.model.MediaInfo;
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
/*    */ public abstract class GetMediaInfo
/*    */   extends ActionCallback
/*    */ {
/* 32 */   private static Logger log = Logger.getLogger(GetMediaInfo.class.getName());
/*    */   
/*    */   public GetMediaInfo(Service service) {
/* 35 */     this(new UnsignedIntegerFourBytes(0L), service);
/*    */   }
/*    */   
/*    */   public GetMediaInfo(UnsignedIntegerFourBytes instanceId, Service service) {
/* 39 */     super(new ActionInvocation(service.getAction("GetMediaInfo")));
/* 40 */     getActionInvocation().setInput("InstanceID", instanceId);
/*    */   }
/*    */   
/*    */   public void success(ActionInvocation invocation) {
/* 44 */     MediaInfo mediaInfo = new MediaInfo(invocation.getOutputMap());
/* 45 */     received(invocation, mediaInfo);
/*    */   }
/*    */   
/*    */   public abstract void received(ActionInvocation paramActionInvocation, MediaInfo paramMediaInfo);
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\avtransport\callback\GetMediaInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */