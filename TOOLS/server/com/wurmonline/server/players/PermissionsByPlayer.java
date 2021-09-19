/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import java.util.logging.Logger;
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
/*     */ public class PermissionsByPlayer
/*     */   implements MiscConstants, Comparable<PermissionsByPlayer>
/*     */ {
/*  38 */   private static Logger logger = Logger.getLogger(PermissionsByPlayer.class.getName());
/*     */   
/*     */   private long id;
/*     */   
/*     */   private Permissions permissions;
/*     */ 
/*     */   
/*     */   PermissionsByPlayer(long aPlayerId, int aSettings) {
/*  46 */     this.id = aPlayerId;
/*  47 */     this.permissions = new Permissions();
/*  48 */     this.permissions.setPermissionBits(aSettings);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getPlayerId() {
/*  53 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   Permissions getPermissions() {
/*  58 */     return this.permissions;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPermission(int bit) {
/*  63 */     return this.permissions.hasPermission(bit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSettings() {
/*  71 */     return this.permissions.getPermissions();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  76 */     return getPlayerOrGroupName(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(PermissionsByPlayer pbp) {
/*  87 */     return getName().compareTo(pbp.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getPlayerOrGroupName(long playerOrGroupId) {
/*     */     try {
/*  94 */       if (playerOrGroupId == -20L)
/*  95 */         return "Allies"; 
/*  96 */       if (playerOrGroupId == -30L)
/*  97 */         return "Citizens"; 
/*  98 */       if (playerOrGroupId == -40L)
/*  99 */         return "Kingdom"; 
/* 100 */       if (playerOrGroupId == -50L)
/* 101 */         return "Everyone"; 
/* 102 */       if (playerOrGroupId == -60L) {
/* 103 */         return "Brand Group";
/*     */       }
/* 105 */       return Players.getInstance().getNameFor(playerOrGroupId);
/*     */     }
/* 107 */     catch (NoSuchPlayerException|java.io.IOException e) {
/*     */       
/* 109 */       return "Unknown";
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PermissionsByPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */