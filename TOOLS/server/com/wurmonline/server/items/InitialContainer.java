/*    */ package com.wurmonline.server.items;
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
/*    */ public class InitialContainer
/*    */ {
/*    */   final int templateId;
/*    */   final String name;
/*    */   final byte material;
/*    */   
/*    */   InitialContainer(int aTemplateId, String aName) {
/* 29 */     this.templateId = aTemplateId;
/* 30 */     this.name = aName;
/* 31 */     this.material = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   InitialContainer(int aTemplateId, String aName, byte aMaterial) {
/* 36 */     this.templateId = aTemplateId;
/* 37 */     this.name = aName;
/* 38 */     this.material = aMaterial;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getTemplateId() {
/* 43 */     return this.templateId;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getMaterial() {
/* 53 */     return this.material;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\InitialContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */