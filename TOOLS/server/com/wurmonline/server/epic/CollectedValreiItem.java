/*    */ package com.wurmonline.server.epic;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class CollectedValreiItem
/*    */ {
/*    */   private final long id;
/*    */   private final String nameOfCollected;
/*    */   private final int typeOfCollected;
/*    */   
/*    */   public CollectedValreiItem(long _id, String _name, int _type) {
/* 35 */     this.id = _id;
/* 36 */     this.nameOfCollected = _name;
/* 37 */     this.typeOfCollected = _type;
/*    */   }
/*    */ 
/*    */   
/*    */   public final long getId() {
/* 42 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getName() {
/* 47 */     return this.nameOfCollected;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getType() {
/* 52 */     return this.typeOfCollected;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 62 */     if (!(obj instanceof CollectedValreiItem))
/* 63 */       return false; 
/* 64 */     CollectedValreiItem cmp = (CollectedValreiItem)obj;
/*    */     
/* 66 */     return (cmp.getId() == this.id && cmp.getName().equals(this.nameOfCollected) && cmp.typeOfCollected == this.typeOfCollected);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 77 */     int prime = 31;
/* 78 */     int result = 1;
/* 79 */     result = 31 * result + (int)(this.id ^ this.id >>> 32L);
/* 80 */     result = 31 * result + ((this.nameOfCollected == null) ? 0 : this.nameOfCollected.hashCode());
/* 81 */     result = 31 * result + this.typeOfCollected;
/* 82 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public static List<CollectedValreiItem> fromList(List<EpicEntity> entities) {
/* 87 */     List<CollectedValreiItem> list = new ArrayList<>();
/*    */     
/* 89 */     if (entities != null)
/*    */     {
/* 91 */       for (EpicEntity ent : entities)
/*    */       {
/* 93 */         list.add(new CollectedValreiItem(ent.getId(), ent.getName(), ent.getType()));
/*    */       }
/*    */     }
/*    */     
/* 97 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\epic\CollectedValreiItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */