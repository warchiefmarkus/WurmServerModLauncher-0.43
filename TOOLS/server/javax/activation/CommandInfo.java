/*     */ package javax.activation;
/*     */ 
/*     */ import java.beans.Beans;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandInfo
/*     */ {
/*     */   private String verb;
/*     */   private String className;
/*     */   
/*     */   public CommandInfo(String verb, String className) {
/*  70 */     this.verb = verb;
/*  71 */     this.className = className;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCommandName() {
/*  80 */     return this.verb;
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
/*     */ 
/*     */   
/*     */   public String getCommandClass() {
/*  94 */     return this.className;
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
/*     */   public Object getCommandObject(DataHandler dh, ClassLoader loader) throws IOException, ClassNotFoundException {
/* 129 */     Object new_bean = null;
/*     */ 
/*     */     
/* 132 */     new_bean = Beans.instantiate(loader, this.className);
/*     */ 
/*     */     
/* 135 */     if (new_bean != null) {
/* 136 */       if (new_bean instanceof CommandObject) {
/* 137 */         ((CommandObject)new_bean).setCommandContext(this.verb, dh);
/* 138 */       } else if (new_bean instanceof Externalizable && 
/* 139 */         dh != null) {
/* 140 */         InputStream is = dh.getInputStream();
/* 141 */         if (is != null) {
/* 142 */           ((Externalizable)new_bean).readExternal(new ObjectInputStream(is));
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 149 */     return new_bean;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\activation\CommandInfo.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */