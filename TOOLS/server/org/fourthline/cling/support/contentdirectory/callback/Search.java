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
/*     */ import org.fourthline.cling.support.model.DIDLContent;
/*     */ import org.fourthline.cling.support.model.SearchResult;
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
/*     */ public abstract class Search
/*     */   extends ActionCallback
/*     */ {
/*     */   public static final String CAPS_WILDCARD = "*";
/*     */   
/*     */   public enum Status
/*     */   {
/*  41 */     NO_CONTENT("No Content"),
/*  42 */     LOADING("Loading..."),
/*  43 */     OK("OK");
/*     */     
/*     */     private String defaultMessage;
/*     */     
/*     */     Status(String defaultMessage) {
/*  48 */       this.defaultMessage = defaultMessage;
/*     */     }
/*     */     
/*     */     public String getDefaultMessage() {
/*  52 */       return this.defaultMessage;
/*     */     }
/*     */   }
/*     */   
/*  56 */   private static Logger log = Logger.getLogger(Search.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Search(Service service, String containerId, String searchCriteria) {
/*  62 */     this(service, containerId, searchCriteria, "*", 0L, (Long)null, new SortCriterion[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Search(Service service, String containerId, String searchCriteria, String filter, long firstResult, Long maxResults, SortCriterion... orderBy) {
/*  70 */     super(new ActionInvocation(service.getAction("Search")));
/*     */     
/*  72 */     log.fine("Creating browse action for container ID: " + containerId);
/*     */     
/*  74 */     getActionInvocation().setInput("ContainerID", containerId);
/*  75 */     getActionInvocation().setInput("SearchCriteria", searchCriteria);
/*  76 */     getActionInvocation().setInput("Filter", filter);
/*  77 */     getActionInvocation().setInput("StartingIndex", new UnsignedIntegerFourBytes(firstResult));
/*  78 */     getActionInvocation().setInput("RequestedCount", new UnsignedIntegerFourBytes(((maxResults == null) ? 
/*     */           
/*  80 */           getDefaultMaxResults() : maxResults).longValue()));
/*     */     
/*  82 */     getActionInvocation().setInput("SortCriteria", SortCriterion.toString(orderBy));
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*  87 */     updateStatus(Status.LOADING);
/*  88 */     super.run();
/*     */   }
/*     */ 
/*     */   
/*     */   public void success(ActionInvocation actionInvocation) {
/*  93 */     log.fine("Successful search action, reading output argument values");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  99 */     SearchResult result = new SearchResult(actionInvocation.getOutput("Result").getValue().toString(), (UnsignedIntegerFourBytes)actionInvocation.getOutput("NumberReturned").getValue(), (UnsignedIntegerFourBytes)actionInvocation.getOutput("TotalMatches").getValue(), (UnsignedIntegerFourBytes)actionInvocation.getOutput("UpdateID").getValue());
/*     */     
/* 101 */     boolean proceed = receivedRaw(actionInvocation, result);
/*     */     
/* 103 */     if (proceed && result.getCountLong() > 0L && result.getResult().length() > 0) {
/*     */       try {
/* 105 */         DIDLParser didlParser = new DIDLParser();
/* 106 */         DIDLContent didl = didlParser.parse(result.getResult());
/* 107 */         received(actionInvocation, didl);
/* 108 */         updateStatus(Status.OK);
/* 109 */       } catch (Exception ex) {
/* 110 */         actionInvocation.setFailure(new ActionException(ErrorCode.ACTION_FAILED, "Can't parse DIDL XML response: " + ex, ex));
/*     */ 
/*     */         
/* 113 */         failure(actionInvocation, null);
/*     */       } 
/*     */     } else {
/* 116 */       received(actionInvocation, new DIDLContent());
/* 117 */       updateStatus(Status.NO_CONTENT);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Long getDefaultMaxResults() {
/* 127 */     return Long.valueOf(999L);
/*     */   }
/*     */   
/*     */   public boolean receivedRaw(ActionInvocation actionInvocation, SearchResult searchResult) {
/* 131 */     return true;
/*     */   }
/*     */   
/*     */   public abstract void received(ActionInvocation paramActionInvocation, DIDLContent paramDIDLContent);
/*     */   
/*     */   public abstract void updateStatus(Status paramStatus);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\contentdirectory\callback\Search.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */