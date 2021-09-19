/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import java.util.Date;
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
/*     */ public final class RecruitmentAd
/*     */   implements Comparable<RecruitmentAd>
/*     */ {
/*     */   private int villageId;
/*     */   private long contactId;
/*     */   private String description;
/*     */   private Date created;
/*     */   private int kingdom;
/*     */   
/*     */   public RecruitmentAd(int _villageId, long _contactId, String _description, Date _created, int _kingdom) {
/*  40 */     this.villageId = _villageId;
/*  41 */     this.contactId = _contactId;
/*  42 */     this.description = _description;
/*  43 */     this.created = _created;
/*  44 */     this.kingdom = _kingdom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(RecruitmentAd ad1) {
/*  55 */     if (ad1.getVillageId() == getVillageId())
/*  56 */       return 1; 
/*  57 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getContactId() {
/*  62 */     return this.contactId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getContactName() {
/*  67 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(this.contactId);
/*  68 */     if (info == null)
/*  69 */       return ""; 
/*  70 */     return info.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public final Date getCreated() {
/*  75 */     return this.created;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getDescription() {
/*  80 */     return this.description;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getKingdom() {
/*  85 */     return this.kingdom;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getVillageId() {
/*  90 */     return this.villageId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getVillageName() {
/*     */     try {
/*  97 */       Village village = Villages.getVillage(this.villageId);
/*  98 */       return village.getName();
/*     */     }
/* 100 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 102 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setContactId(long _contactId) {
/* 108 */     this.contactId = _contactId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreated(Date date) {
/* 113 */     this.created = date;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDescription(String text) {
/* 118 */     this.description = text;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\RecruitmentAd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */