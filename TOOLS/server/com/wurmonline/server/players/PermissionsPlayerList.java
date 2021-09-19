/*     */ package com.wurmonline.server.players;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class PermissionsPlayerList
/*     */   implements MiscConstants
/*     */ {
/* 230 */   private Map<Long, PermissionsByPlayer> playerPermissions = new ConcurrentHashMap<>();
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
/*     */   public void remove(long aPlayerId) {
/* 242 */     this.playerPermissions.remove(Long.valueOf(aPlayerId));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 247 */     return this.playerPermissions.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 252 */     return this.playerPermissions.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionsByPlayer[] getPermissionsByPlayer() {
/* 257 */     return (PermissionsByPlayer[])this.playerPermissions.values().toArray((Object[])new PermissionsByPlayer[this.playerPermissions.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PermissionsByPlayer add(long aPlayerId, int aPermissions) {
/* 268 */     return this.playerPermissions.put(Long.valueOf(aPlayerId), new PermissionsByPlayer(aPlayerId, aPermissions));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPermission(long playerId, int bit) {
/* 273 */     Long id = Long.valueOf(playerId);
/* 274 */     PermissionsByPlayer playerPerm = this.playerPermissions.get(id);
/* 275 */     if (playerPerm == null)
/*     */     {
/*     */       
/* 278 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 282 */     return playerPerm.hasPermission(bit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PermissionsByPlayer getPermissionsByPlayer(long playerId) {
/* 288 */     Long id = Long.valueOf(playerId);
/* 289 */     return this.playerPermissions.get(id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Permissions getPermissionsFor(long playerId) {
/* 299 */     Long id = Long.valueOf(playerId);
/* 300 */     PermissionsByPlayer playerPerm = this.playerPermissions.get(id);
/*     */     
/* 302 */     if (playerPerm == null) {
/*     */ 
/*     */       
/* 305 */       PermissionsByPlayer everyone = this.playerPermissions.get(Long.valueOf(-10L));
/* 306 */       if (everyone == null)
/*     */       {
/*     */         
/* 309 */         return new Permissions();
/*     */       }
/*     */ 
/*     */       
/* 313 */       return everyone.getPermissions();
/*     */     } 
/*     */     
/* 316 */     return playerPerm.getPermissions();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean exists(long playerId) {
/* 321 */     return this.playerPermissions.containsKey(Long.valueOf(playerId));
/*     */   }
/*     */   
/*     */   public static interface ISettings {
/*     */     void addDefaultCitizenPermissions();
/*     */     
/*     */     void addGuest(long param1Long, int param1Int);
/*     */     
/*     */     boolean canAllowEveryone();
/*     */     
/*     */     boolean canChangeOwner(Creature param1Creature);
/*     */     
/*     */     boolean canChangeName(Creature param1Creature);
/*     */     
/*     */     boolean canHavePermissions();
/*     */     
/*     */     String getAllianceName();
/*     */     
/*     */     String getKingdomName();
/*     */     
/*     */     int getMaxAllowed();
/*     */     
/*     */     String getObjectName();
/*     */     
/*     */     String getOwnerName();
/*     */     
/*     */     PermissionsPlayerList getPermissionsPlayerList();
/*     */     
/*     */     String getRolePermissionName();
/*     */     
/*     */     String getSettlementName();
/*     */     
/*     */     String getTypeName();
/*     */     
/*     */     String getWarning();
/*     */     
/*     */     long getWurmId();
/*     */     
/*     */     int getTemplateId();
/*     */     
/*     */     boolean isActualOwner(long param1Long);
/*     */     
/*     */     boolean isAllied(Creature param1Creature);
/*     */     
/*     */     boolean isCitizen(Creature param1Creature);
/*     */     
/*     */     boolean isGuest(Creature param1Creature);
/*     */     
/*     */     boolean isGuest(long param1Long);
/*     */     
/*     */     boolean isManaged();
/*     */     
/*     */     boolean isManageEnabled(Player param1Player);
/*     */     
/*     */     boolean isOwner(Creature param1Creature);
/*     */     
/*     */     boolean isOwner(long param1Long);
/*     */     
/*     */     boolean isSameKingdom(Creature param1Creature);
/*     */     
/*     */     String mayManageHover(Player param1Player);
/*     */     
/*     */     String mayManageText(Player param1Player);
/*     */     
/*     */     boolean mayShowPermissions(Creature param1Creature);
/*     */     
/*     */     String messageOnTick();
/*     */     
/*     */     String messageUnTick();
/*     */     
/*     */     String questionOnTick();
/*     */     
/*     */     String questionUnTick();
/*     */     
/*     */     void removeGuest(long param1Long);
/*     */     
/*     */     void save() throws IOException;
/*     */     
/*     */     void setIsManaged(boolean param1Boolean, Player param1Player);
/*     */     
/*     */     boolean setNewOwner(long param1Long);
/*     */     
/*     */     boolean setObjectName(String param1String, Creature param1Creature);
/*     */     
/*     */     boolean isItem();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\PermissionsPlayerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */