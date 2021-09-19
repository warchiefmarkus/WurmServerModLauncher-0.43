/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.mesh.Tiles;
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.skills.Skill;
/*     */ import com.wurmonline.server.zones.FaithZone;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RiteDeath
/*     */   extends ReligiousSpell
/*     */ {
/*     */   public static final int RANGE = 4;
/*     */   
/*     */   public RiteDeath() {
/*  51 */     super("Rite of Death", 402, 100, 300, 60, 50, 43200000L);
/*  52 */     this.isRitual = true;
/*  53 */     this.targetItem = true;
/*  54 */     this.description = Servers.localServer.PVPSERVER ? "spawns mycelium in your gods domain" : "awards followers with some skill and sleep bonus";
/*  55 */     this.type = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean precondition(Skill castSkill, Creature performer, Item target) {
/*  61 */     if (performer.getDeity() != null) {
/*     */       
/*  63 */       Deity deity = performer.getDeity();
/*  64 */       Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*  65 */       if (templateDeity.getFavor() < 100000 && !Servers.isThisATestServer()) {
/*     */         
/*  67 */         performer.getCommunicator().sendNormalServerMessage(deity
/*  68 */             .getName() + " can not grant that power right now.", (byte)3);
/*  69 */         return false;
/*     */       } 
/*  71 */       if (target.getBless() == deity)
/*     */       {
/*  73 */         if (target.isDomainItem())
/*     */         {
/*  75 */           return true;
/*     */         }
/*     */       }
/*  78 */       performer.getCommunicator().sendNormalServerMessage(
/*  79 */           String.format("You need to cast this spell at an altar of %s.", new Object[] { deity.getName() }), (byte)3);
/*     */     } 
/*     */     
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void doEffect(Skill castSkill, double power, Creature performer, Item target) {
/*  88 */     Deity deity = performer.getDeity();
/*  89 */     Deity templateDeity = Deities.getDeity(deity.getTemplateDeity());
/*     */     
/*  91 */     if (Servers.localServer.PVPSERVER) {
/*     */       
/*  93 */       performer.getCommunicator().sendNormalServerMessage("The domain of " + performer.getDeity().getName() + " is covered in mycelium.", (byte)2);
/*  94 */       Server.getInstance().broadCastSafe("As the Rite of Death is completed, followers of " + deity.getName() + " may now receive a blessing!");
/*  95 */       HistoryManager.addHistory(performer.getName(), "casts " + this.name + ". The domain of " + performer.getDeity().getName() + " is covered in mycelium.");
/*  96 */       templateDeity.setFavor(templateDeity.getFavor() - 100000);
/*     */       
/*  98 */       performer.achievement(635);
/*  99 */       for (Creature c : performer.getLinks()) {
/* 100 */         c.achievement(635);
/*     */       }
/*     */       
/* 103 */       new RiteEvent.RiteOfDeathEvent(-10, performer.getWurmId(), getNumber(), deity.getNumber(), System.currentTimeMillis(), 86400000L);
/*     */       
/* 105 */       if (Features.Feature.NEWDOMAINS.isEnabled()) {
/*     */         
/* 107 */         byte type = 0;
/* 108 */         for (FaithZone f : Zones.getFaithZones()) {
/*     */           
/* 110 */           if (f != null && f.getCurrentRuler().getTemplateDeity() == deity.getTemplateDeity())
/*     */             
/*     */             try {
/*     */               
/* 114 */               if (Zones.getFaithZone(f.getCenterX(), f.getCenterY(), true) == f)
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 120 */                 for (int tx = f.getStartX(); tx < f.getEndX(); tx++) {
/* 121 */                   for (int ty = f.getStartY(); ty < f.getEndY(); ty++)
/* 122 */                     effectTile(tx, ty, type); 
/*     */                 }  } 
/*     */             } catch (NoSuchZoneException e) {} 
/*     */         } 
/*     */       } else {
/* 127 */         FaithZone[][] surfaceZones = Zones.getFaithZones(true);
/* 128 */         byte type = 0;
/*     */         
/* 130 */         for (int x = 0; x < Zones.faithSizeX; x++) {
/* 131 */           for (int y = 0; y < Zones.faithSizeY; y++) {
/* 132 */             if (surfaceZones[x][y].getCurrentRuler().getTemplateDeity() == deity.getTemplateDeity())
/* 133 */               for (int tx = surfaceZones[x][y].getStartX(); tx <= surfaceZones[x][y].getEndX(); tx++)
/* 134 */               { for (int ty = surfaceZones[x][y].getStartY(); ty <= surfaceZones[x][y].getEndY(); ty++)
/* 135 */                   effectTile(tx, ty, type);  }  
/*     */           } 
/*     */         } 
/* 138 */       }  Player[] players = Players.getInstance().getPlayers();
/* 139 */       for (Player lPlayer : players)
/*     */       {
/* 141 */         if (lPlayer.getDeity() == null || lPlayer.getDeity().getTemplateDeity() != deity.getTemplateDeity())
/*     */         {
/* 143 */           lPlayer.getCommunicator().sendAlertServerMessage("You get a sudden headache.", (byte)3);
/* 144 */           lPlayer.addWoundOfType(performer, (byte)9, 1, false, 1.0F, false, 1000.0D, 0.0F, 0.0F, false, true);
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 151 */       performer.getCommunicator().sendNormalServerMessage("The followers of " + performer.getDeity().getName() + " may now receive a blessing.", (byte)2);
/* 152 */       Server.getInstance().broadCastSafe("As the Rite of Death is completed, followers of " + deity.getName() + " may now receive a blessing!");
/* 153 */       HistoryManager.addHistory(performer.getName(), "casts " + this.name + ". The followers of " + performer.getDeity().getName() + " may now receive a blessing.");
/* 154 */       templateDeity.setFavor(templateDeity.getFavor() - 100000);
/*     */       
/* 156 */       performer.achievement(635);
/* 157 */       for (Creature c : performer.getLinks()) {
/* 158 */         c.achievement(635);
/*     */       }
/*     */       
/* 161 */       new RiteEvent.RiteOfDeathEvent(-10, performer.getWurmId(), getNumber(), deity.getNumber(), System.currentTimeMillis(), 86400000L);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void effectTile(int tx, int ty, byte type) {
/* 167 */     int tile = Server.surfaceMesh.getTile(tx, ty);
/* 168 */     type = Tiles.decodeType(tile);
/* 169 */     Tiles.Tile theTile = Tiles.getTile(type);
/* 170 */     byte data = Tiles.decodeData(tile);
/* 171 */     if (type == Tiles.Tile.TILE_GRASS.id || type == Tiles.Tile.TILE_DIRT.id) {
/*     */       
/* 173 */       Server.setSurfaceTile(tx, ty, Tiles.decodeHeight(tile), Tiles.Tile.TILE_MYCELIUM.id, (byte)0);
/*     */       
/* 175 */       Players.getInstance().sendChangedTile(tx, ty, true, false);
/*     */     }
/* 177 */     else if (theTile.isNormalTree()) {
/*     */       
/* 179 */       Server.setSurfaceTile(tx, ty, Tiles.decodeHeight(tile), theTile
/* 180 */           .getTreeType(data).asMyceliumTree(), data);
/* 181 */       Players.getInstance().sendChangedTile(tx, ty, true, false);
/*     */     }
/* 183 */     else if (theTile.isNormalBush()) {
/*     */       
/* 185 */       Server.setSurfaceTile(tx, ty, Tiles.decodeHeight(tile), theTile
/* 186 */           .getBushType(data).asMyceliumBush(), data);
/* 187 */       Players.getInstance().sendChangedTile(tx, ty, true, false);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\RiteDeath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */