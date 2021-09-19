/*     */ package org.controlsfx.control.action;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Objects;
/*     */ import javafx.event.ActionEvent;
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
/*     */ public class AnnotatedAction
/*     */   extends Action
/*     */ {
/*     */   private final Method method;
/*     */   private final WeakReference<Object> target;
/*     */   
/*     */   public AnnotatedAction(String text, Method method, Object target) {
/*  52 */     super(text);
/*  53 */     Objects.requireNonNull(method);
/*  54 */     Objects.requireNonNull(target);
/*     */     
/*  56 */     setEventHandler(this::handleAction);
/*     */     
/*  58 */     this.method = method;
/*  59 */     this.method.setAccessible(true);
/*  60 */     this.target = new WeakReference(target);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getTarget() {
/*  69 */     return this.target.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleAction(ActionEvent ae) {
/*     */     try {
/*  78 */       Object actionTarget = getTarget();
/*  79 */       if (actionTarget == null) {
/*  80 */         throw new IllegalStateException("Action target object is no longer reachable");
/*     */       }
/*     */       
/*  83 */       int paramCount = this.method.getParameterCount();
/*  84 */       if (paramCount == 0) {
/*  85 */         this.method.invoke(actionTarget, new Object[0]);
/*     */       }
/*  87 */       else if (paramCount == 1) {
/*  88 */         this.method.invoke(actionTarget, new Object[] { ae });
/*     */       }
/*  90 */       else if (paramCount == 2) {
/*  91 */         this.method.invoke(actionTarget, new Object[] { ae, this });
/*     */       } 
/*  93 */     } catch (Throwable e) {
/*  94 */       handleActionException(ae, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleActionException(ActionEvent ae, Throwable ex) {
/* 104 */     ex.printStackTrace();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     return getText();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\action\AnnotatedAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */