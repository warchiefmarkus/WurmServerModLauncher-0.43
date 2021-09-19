/*     */ package org.fourthline.cling.support.contentdirectory.callback;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import org.fourthline.cling.controlpoint.ActionCallback;
/*     */ import org.fourthline.cling.model.action.ActionException;
/*     */ import org.fourthline.cling.model.action.ActionInvocation;
/*     */ import org.fourthline.cling.model.meta.Service;
/*     */ import org.fourthline.cling.model.types.ErrorCode;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
/*     */ import org.fourthline.cling.support.contentdirectory.DIDLParser;
/*     */ import org.fourthline.cling.support.model.BrowseFlag;
/*     */ import org.fourthline.cling.support.model.BrowseResult;
/*     */ import org.fourthline.cling.support.model.DIDLContent;
/*     */ import org.fourthline.cling.support.model.SortCriterion;
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
/*     */ public abstract class Browse
/*     */   extends ActionCallback
/*     */ {
/*     */   public static final String CAPS_WILDCARD = "*";
/*     */   
/*     */   public enum Status
/*     */   {
/*  42 */     NO_CONTENT("No Content"),
/*  43 */     LOADING("Loading..."),
/*  44 */     OK("OK");
/*     */     
/*     */     private String defaultMessage;
/*     */     
/*     */     Status(String defaultMessage) {
/*  49 */       this.defaultMessage = defaultMessage;
/*     */     }
/*     */     
/*     */     public String getDefaultMessage() {
/*  53 */       return this.defaultMessage;
/*     */     }
/*     */   }
/*     */   
/*  57 */   private static Logger log = Logger.getLogger(Browse.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Browse(Service service, String containerId, BrowseFlag flag) {
/*  63 */     this(service, containerId, flag, "*", 0L, (Long)null, new SortCriterion[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Browse(Service service, String objectID, BrowseFlag flag, String filter, long firstResult, Long maxResults, SortCriterion... orderBy) {
/*  72 */     super(new ActionInvocation(service.getAction("Browse")));
/*     */     
/*  74 */     log.fine("Creating browse action for object ID: " + objectID);
/*     */     
/*  76 */     getActionInvocation().setInput("ObjectID", objectID);
/*  77 */     getActionInvocation().setInput("BrowseFlag", flag.toString());
/*  78 */     getActionInvocation().setInput("Filter", filter);
/*  79 */     getActionInvocation().setInput("StartingIndex", new UnsignedIntegerFourBytes(firstResult));
/*  80 */     getActionInvocation().setInput("RequestedCount", new UnsignedIntegerFourBytes((maxResults == null) ? 
/*  81 */           getDefaultMaxResults() : maxResults.longValue()));
/*     */     
/*  83 */     getActionInvocation().setInput("SortCriteria", SortCriterion.toString(orderBy));
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  88 */     updateStatus(Status.LOADING);
/*  89 */     super.run();
/*     */   }
/*     */   
/*     */   public void success(ActionInvocation invocation) {
/*  93 */     log.fine("Successful browse action, reading output argument values");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     BrowseResult result = new BrowseResult(invocation.getOutput("Result").getValue().toString(), (UnsignedIntegerFourBytes)invocation.getOutput("NumberReturned").getValue(), (UnsignedIntegerFourBytes)invocation.getOutput("TotalMatches").getValue(), (UnsignedIntegerFourBytes)invocation.getOutput("UpdateID").getValue());
/*     */ 
/*     */     
/* 102 */     boolean proceed = receivedRaw(invocation, result);
/*     */     
/* 104 */     if (proceed && result.getCountLong() > 0L && result.getResult().length() > 0) {
/*     */ 
/*     */       
/*     */       try {
/* 108 */         DIDLParser didlParser = new DIDLParser();
/* 109 */         DIDLContent didl = didlParser.parse(result.getResult());
/* 110 */         received(invocation, didl);
/* 111 */         updateStatus(Status.OK);
/*     */       }
/* 113 */       catch (Exception ex) {
/* 114 */         invocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Can't parse DIDL XML response: " + ex, ex));
/*     */ 
/*     */         
/* 117 */         failure(invocation, null);
/*     */       } 
/*     */     } else {
/*     */       
/* 121 */       received(invocation, new DIDLContent());
/* 122 */       updateStatus(Status.NO_CONTENT);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDefaultMaxResults() {
/* 132 */     return 999L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean receivedRaw(ActionInvocation actionInvocation, BrowseResult browseResult) {
/* 143 */     return true;
/*     */   }
/*     */   
/*     */   public abstract void received(ActionInvocation paramActionInvocation, DIDLContent paramDIDLContent);
/*     */   
/*     */   public abstract void updateStatus(Status paramStatus);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirectory\callback\Browse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */