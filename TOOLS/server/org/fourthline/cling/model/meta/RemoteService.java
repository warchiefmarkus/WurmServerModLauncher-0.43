/*     */ package org.fourthline.cling.model.meta;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.fourthline.cling.model.ValidationError;
/*     */ import org.fourthline.cling.model.ValidationException;
/*     */ import org.fourthline.cling.model.types.ServiceId;
/*     */ import org.fourthline.cling.model.types.ServiceType;
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
/*     */ public class RemoteService
/*     */   extends Service<RemoteDevice, RemoteService>
/*     */ {
/*     */   private final URI descriptorURI;
/*     */   private final URI controlURI;
/*     */   private final URI eventSubscriptionURI;
/*     */   
/*     */   public RemoteService(ServiceType serviceType, ServiceId serviceId, URI descriptorURI, URI controlURI, URI eventSubscriptionURI) throws ValidationException {
/*  44 */     this(serviceType, serviceId, descriptorURI, controlURI, eventSubscriptionURI, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RemoteService(ServiceType serviceType, ServiceId serviceId, URI descriptorURI, URI controlURI, URI eventSubscriptionURI, Action<RemoteService>[] actions, StateVariable<RemoteService>[] stateVariables) throws ValidationException {
/*  50 */     super(serviceType, serviceId, actions, stateVariables);
/*     */     
/*  52 */     this.descriptorURI = descriptorURI;
/*  53 */     this.controlURI = controlURI;
/*  54 */     this.eventSubscriptionURI = eventSubscriptionURI;
/*     */     
/*  56 */     List<ValidationError> errors = validateThis();
/*  57 */     if (errors.size() > 0) {
/*  58 */       throw new ValidationException("Validation of device graph failed, call getErrors() on exception", errors);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Action getQueryStateVariableAction() {
/*  64 */     return new QueryStateVariableAction<>(this);
/*     */   }
/*     */   
/*     */   public URI getDescriptorURI() {
/*  68 */     return this.descriptorURI;
/*     */   }
/*     */   
/*     */   public URI getControlURI() {
/*  72 */     return this.controlURI;
/*     */   }
/*     */   
/*     */   public URI getEventSubscriptionURI() {
/*  76 */     return this.eventSubscriptionURI;
/*     */   }
/*     */   
/*     */   public List<ValidationError> validateThis() {
/*  80 */     List<ValidationError> errors = new ArrayList<>();
/*     */     
/*  82 */     if (getDescriptorURI() == null) {
/*  83 */       errors.add(new ValidationError(
/*  84 */             getClass(), "descriptorURI", "Descriptor location (SCPDURL) is required"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     if (getControlURI() == null) {
/*  91 */       errors.add(new ValidationError(
/*  92 */             getClass(), "controlURI", "Control URL is required"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     if (getEventSubscriptionURI() == null) {
/*  99 */       errors.add(new ValidationError(
/* 100 */             getClass(), "eventSubscriptionURI", "Event subscription URL is required"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 106 */     return errors;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     return "(" + getClass().getSimpleName() + ") Descriptor: " + getDescriptorURI();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\model\meta\RemoteService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */