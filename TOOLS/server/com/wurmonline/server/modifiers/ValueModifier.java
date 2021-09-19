/*    */ package com.wurmonline.server.modifiers;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ValueModifier
/*    */   implements ModifierTypes
/*    */ {
/*    */   private final int type;
/*    */   private Set<ValueModifiedListener> listeners;
/*    */   
/*    */   ValueModifier() {
/* 38 */     this.type = 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ValueModifier(int typ) {
/* 47 */     this.type = typ;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public final int getType() {
/* 56 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   public final void addListener(ValueModifiedListener list) {
/* 61 */     if (this.listeners == null)
/* 62 */       this.listeners = new HashSet<>(); 
/* 63 */     this.listeners.add(list);
/*    */   }
/*    */ 
/*    */   
/*    */   public final void removeListener(ValueModifiedListener list) {
/* 68 */     if (this.listeners == null) {
/* 69 */       this.listeners = new HashSet<>();
/*    */     } else {
/* 71 */       this.listeners.remove(list);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Set<ValueModifiedListener> getListeners() {
/* 81 */     return this.listeners;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\modifiers\ValueModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */