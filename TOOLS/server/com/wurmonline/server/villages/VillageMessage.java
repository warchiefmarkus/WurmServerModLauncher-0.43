/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.players.PlayerInfo;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import java.text.DateFormat;
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
/*     */ public final class VillageMessage
/*     */   implements Comparable<VillageMessage>
/*     */ {
/*  32 */   private static final DateFormat df = DateFormat.getDateTimeInstance();
/*     */   
/*     */   private int villageId;
/*     */   
/*     */   private long posterId;
/*     */   
/*     */   private long toId;
/*     */   private String message;
/*     */   private int penColour;
/*     */   private long posted;
/*     */   private boolean everyone;
/*     */   
/*     */   public VillageMessage(int aVillageId, long aPosterId, long aToId, String aMessage, int thePenColour, long aPosted, boolean everyone) {
/*  45 */     this.villageId = aVillageId;
/*  46 */     this.posterId = aPosterId;
/*  47 */     this.toId = aToId;
/*  48 */     this.message = aMessage;
/*  49 */     this.penColour = thePenColour;
/*  50 */     this.posted = aPosted;
/*  51 */     this.everyone = everyone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(VillageMessage villageMsg) {
/*  62 */     if (getVillageId() == villageMsg.getVillageId()) {
/*     */       
/*  64 */       if (getToId() < villageMsg.getToId())
/*  65 */         return -1; 
/*  66 */       if (getToId() > villageMsg.getToId())
/*  67 */         return 1; 
/*  68 */       if (getPostedTime() < villageMsg.getPostedTime())
/*  69 */         return 1; 
/*  70 */       if (getPostedTime() > villageMsg.getPostedTime())
/*  71 */         return -1; 
/*  72 */       return 0;
/*     */     } 
/*  74 */     if (getVillageId() < villageMsg.getVillageId())
/*  75 */       return -1; 
/*  76 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPenColour() {
/*  81 */     return this.penColour;
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getPosterId() {
/*  86 */     return this.posterId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getPosterName() {
/*  91 */     return getPlayerName(this.posterId);
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getToId() {
/*  96 */     return this.toId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getToNmae() {
/* 101 */     return getPlayerName(this.toId);
/*     */   }
/*     */ 
/*     */   
/*     */   private final String getPlayerName(long id) {
/* 106 */     PlayerInfo info = PlayerInfoFactory.getPlayerInfoWithWurmId(id);
/* 107 */     if (info == null)
/* 108 */       return ""; 
/* 109 */     return info.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getPostedTime() {
/* 114 */     return this.posted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDate() {
/* 125 */     return df.format(new Date(this.posted));
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getMessage() {
/* 130 */     return this.message;
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getVillageId() {
/* 135 */     return this.villageId;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isForEveryone() {
/* 140 */     return this.everyone;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getVillageName() {
/*     */     try {
/* 147 */       Village village = Villages.getVillage(this.villageId);
/* 148 */       return village.getName();
/*     */     }
/* 150 */     catch (NoSuchVillageException nsv) {
/*     */       
/* 152 */       return "";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\VillageMessage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */