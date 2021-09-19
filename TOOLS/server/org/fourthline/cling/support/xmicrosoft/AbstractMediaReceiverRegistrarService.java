/*     */ package org.fourthline.cling.support.xmicrosoft;
/*     */ 
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import org.fourthline.cling.binding.annotations.UpnpAction;
/*     */ import org.fourthline.cling.binding.annotations.UpnpInputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpOutputArgument;
/*     */ import org.fourthline.cling.binding.annotations.UpnpService;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceId;
/*     */ import org.fourthline.cling.binding.annotations.UpnpServiceType;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariable;
/*     */ import org.fourthline.cling.binding.annotations.UpnpStateVariables;
/*     */ import org.fourthline.cling.model.types.UnsignedIntegerFourBytes;
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
/*     */ @UpnpService(serviceId = @UpnpServiceId(namespace = "microsoft.com", value = "X_MS_MediaReceiverRegistrar"), serviceType = @UpnpServiceType(namespace = "microsoft.com", value = "X_MS_MediaReceiverRegistrar", version = 1))
/*     */ @UpnpStateVariables({@UpnpStateVariable(name = "A_ARG_TYPE_DeviceID", sendEvents = false, datatype = "string"), @UpnpStateVariable(name = "A_ARG_TYPE_Result", sendEvents = false, datatype = "int"), @UpnpStateVariable(name = "A_ARG_TYPE_RegistrationReqMsg", sendEvents = false, datatype = "bin.base64"), @UpnpStateVariable(name = "A_ARG_TYPE_RegistrationRespMsg", sendEvents = false, datatype = "bin.base64")})
/*     */ public abstract class AbstractMediaReceiverRegistrarService
/*     */ {
/*     */   protected final PropertyChangeSupport propertyChangeSupport;
/*     */   @UpnpStateVariable(eventMinimumDelta = 1)
/*  66 */   private UnsignedIntegerFourBytes authorizationGrantedUpdateID = new UnsignedIntegerFourBytes(0L);
/*     */   
/*     */   @UpnpStateVariable(eventMinimumDelta = 1)
/*  69 */   private UnsignedIntegerFourBytes authorizationDeniedUpdateID = new UnsignedIntegerFourBytes(0L);
/*     */   
/*     */   @UpnpStateVariable
/*  72 */   private UnsignedIntegerFourBytes validationSucceededUpdateID = new UnsignedIntegerFourBytes(0L);
/*     */   
/*     */   @UpnpStateVariable
/*  75 */   private UnsignedIntegerFourBytes validationRevokedUpdateID = new UnsignedIntegerFourBytes(0L);
/*     */ 
/*     */   
/*     */   protected AbstractMediaReceiverRegistrarService() {
/*  79 */     this(null);
/*     */   }
/*     */   
/*     */   protected AbstractMediaReceiverRegistrarService(PropertyChangeSupport propertyChangeSupport) {
/*  83 */     this.propertyChangeSupport = (propertyChangeSupport != null) ? propertyChangeSupport : new PropertyChangeSupport(this);
/*     */   }
/*     */   
/*     */   public PropertyChangeSupport getPropertyChangeSupport() {
/*  87 */     return this.propertyChangeSupport;
/*     */   }
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "AuthorizationGrantedUpdateID")})
/*     */   public UnsignedIntegerFourBytes getAuthorizationGrantedUpdateID() {
/*  93 */     return this.authorizationGrantedUpdateID;
/*     */   }
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "AuthorizationDeniedUpdateID")})
/*     */   public UnsignedIntegerFourBytes getAuthorizationDeniedUpdateID() {
/*  98 */     return this.authorizationDeniedUpdateID;
/*     */   }
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "ValidationSucceededUpdateID")})
/*     */   public UnsignedIntegerFourBytes getValidationSucceededUpdateID() {
/* 103 */     return this.validationSucceededUpdateID;
/*     */   }
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "ValidationRevokedUpdateID")})
/*     */   public UnsignedIntegerFourBytes getValidationRevokedUpdateID() {
/* 108 */     return this.validationRevokedUpdateID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "Result", stateVariable = "A_ARG_TYPE_Result")})
/*     */   public int isAuthorized(@UpnpInputArgument(name = "DeviceID", stateVariable = "A_ARG_TYPE_DeviceID") String deviceID) {
/* 118 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "Result", stateVariable = "A_ARG_TYPE_Result")})
/*     */   public int isValidated(@UpnpInputArgument(name = "DeviceID", stateVariable = "A_ARG_TYPE_DeviceID") String deviceID) {
/* 128 */     return 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @UpnpAction(out = {@UpnpOutputArgument(name = "RegistrationRespMsg", stateVariable = "A_ARG_TYPE_RegistrationRespMsg")})
/*     */   public byte[] registerDevice(@UpnpInputArgument(name = "RegistrationReqMsg", stateVariable = "A_ARG_TYPE_RegistrationReqMsg") byte[] registrationReqMsg) {
/* 138 */     return new byte[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\xmicrosoft\AbstractMediaReceiverRegistrarService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */