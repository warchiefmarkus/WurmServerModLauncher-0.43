/*     */ package org.fourthline.cling.support.lastchange;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import org.fourthline.cling.model.DefaultServiceManager;
/*     */ import org.fourthline.cling.model.meta.LocalService;
/*     */ import org.fourthline.cling.model.meta.StateVariable;
/*     */ import org.fourthline.cling.model.state.StateVariableValue;
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
/*     */ public class LastChangeAwareServiceManager<T extends LastChangeDelegator>
/*     */   extends DefaultServiceManager<T>
/*     */ {
/*     */   protected final LastChangeParser lastChangeParser;
/*     */   
/*     */   public LastChangeAwareServiceManager(LocalService<T> localService, LastChangeParser lastChangeParser) {
/*  49 */     this(localService, (Class<T>)null, lastChangeParser);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public LastChangeAwareServiceManager(LocalService<T> localService, Class<T> serviceClass, LastChangeParser lastChangeParser) {
/*  55 */     super(localService, serviceClass);
/*  56 */     this.lastChangeParser = lastChangeParser;
/*     */   }
/*     */   
/*     */   protected LastChangeParser getLastChangeParser() {
/*  60 */     return this.lastChangeParser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void fireLastChange() {
/*  72 */     lock();
/*     */     try {
/*  74 */       ((LastChangeDelegator)getImplementation()).getLastChange().fire(getPropertyChangeSupport());
/*     */     } finally {
/*  76 */       unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Collection<StateVariableValue> readInitialEventedStateVariableValues() throws Exception {
/*  86 */     LastChange lc = new LastChange(getLastChangeParser());
/*     */ 
/*     */     
/*  89 */     UnsignedIntegerFourBytes[] ids = ((LastChangeDelegator)getImplementation()).getCurrentInstanceIds();
/*  90 */     if (ids.length > 0) {
/*  91 */       for (UnsignedIntegerFourBytes instanceId : ids)
/*     */       {
/*  93 */         ((LastChangeDelegator)getImplementation()).appendCurrentState(lc, instanceId);
/*     */       }
/*     */     } else {
/*     */       
/*  97 */       ((LastChangeDelegator)getImplementation()).appendCurrentState(lc, new UnsignedIntegerFourBytes(0L));
/*     */     } 
/*     */ 
/*     */     
/* 101 */     StateVariable variable = getService().getStateVariable("LastChange");
/* 102 */     Collection<StateVariableValue> values = new ArrayList<>();
/* 103 */     values.add(new StateVariableValue(variable, lc.toString()));
/* 104 */     return values;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\lastchange\LastChangeAwareServiceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */