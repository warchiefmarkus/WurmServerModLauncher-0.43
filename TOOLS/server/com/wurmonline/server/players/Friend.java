/*     */ package com.wurmonline.server.players;
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
/*     */ public class Friend
/*     */   implements Comparable<Friend>
/*     */ {
/*     */   private final long id;
/*     */   private final Category cat;
/*     */   private final String note;
/*     */   
/*     */   public enum Category
/*     */   {
/*  31 */     Other(0),
/*  32 */     Contacts(1),
/*  33 */     Friends(2),
/*  34 */     Trusted(3);
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
/*     */     private final byte cat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  55 */     private static final Category[] cats = values();
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int getCatLength() {
/*  60 */       return cats.length;
/*     */     } Category(int numb) {
/*     */       this.cat = (byte)numb;
/*     */     }
/*     */     public static final Category[] getCategories() {
/*  65 */       return cats;
/*     */     }
/*     */     public byte getCatId() {
/*     */       return this.cat;
/*     */     }
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     public static Category catFromInt(int typeId) {
/*  75 */       if (typeId >= getCatLength())
/*  76 */         return cats[0]; 
/*  77 */       return cats[typeId & 0xFF];
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Category catFromName(String catName) {
/*  87 */       for (Category c : cats) {
/*     */         
/*  89 */         if (c.name().toLowerCase().startsWith(catName.toLowerCase()))
/*  90 */           return c; 
/*     */       } 
/*  92 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Friend(long aId, byte catId, String note) {
/* 102 */     this(aId, Category.catFromInt(catId), note);
/*     */   }
/*     */ 
/*     */   
/*     */   public Friend(long aId, Category category, String note) {
/* 107 */     this.id = aId;
/* 108 */     this.cat = category;
/* 109 */     this.note = note;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFriendId() {
/* 118 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Category getCategory() {
/* 127 */     return this.cat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getCatId() {
/* 136 */     return this.cat.getCatId();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 142 */     return PlayerInfoFactory.getPlayerName(this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNote() {
/* 147 */     return this.note;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Friend otherFriend) {
/* 157 */     if (getCatId() < otherFriend.getCatId())
/* 158 */       return 1; 
/* 159 */     if (getCatId() > otherFriend.getCatId()) {
/* 160 */       return -1;
/*     */     }
/* 162 */     return getName().compareTo(otherFriend.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Friend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */