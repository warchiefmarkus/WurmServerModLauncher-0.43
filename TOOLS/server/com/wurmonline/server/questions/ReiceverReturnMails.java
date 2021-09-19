/*    */ package com.wurmonline.server.questions;
/*    */ 
/*    */ import com.wurmonline.server.items.Item;
/*    */ import com.wurmonline.server.items.WurmMail;
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
/*    */ final class ReiceverReturnMails
/*    */ {
/* 28 */   private final Set<WurmMail> returnWurmMailSet = new HashSet<>();
/* 29 */   private final Set<Item> returnItemSet = new HashSet<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private int serverId;
/*    */ 
/*    */ 
/*    */   
/*    */   private long receiverId;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void addMail(WurmMail mail, Item item) {
/* 44 */     if (!this.returnWurmMailSet.contains(mail))
/* 45 */       this.returnWurmMailSet.add(mail); 
/* 46 */     this.returnItemSet.add(item);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   int getServerId() {
/* 56 */     return this.serverId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setServerId(int aServerId) {
/* 67 */     this.serverId = aServerId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void setReceiverId(long aReceiverId) {
/* 78 */     this.receiverId = aReceiverId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Set<WurmMail> getReturnWurmMailSet() {
/* 88 */     return this.returnWurmMailSet;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   Item[] getReturnItemSetAsArray() {
/* 98 */     return this.returnItemSet.<Item>toArray(new Item[this.returnItemSet.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ReiceverReturnMails.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */