/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.mesh.Tiles;
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.Servers;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.skills.Skill;
/*    */ import com.wurmonline.server.zones.Zones;
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
/*    */ 
/*    */ 
/*    */ public class ZombieInfestation
/*    */   extends ReligiousSpell
/*    */ {
/*    */   public static final int RANGE = 50;
/*    */   
/*    */   public ZombieInfestation() {
/* 40 */     super("Zombie Infestation", 431, 30, 120, 50, 50, 1800000L);
/* 41 */     this.targetTile = true;
/* 42 */     this.description = "summons your best friends";
/* 43 */     this.type = 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean precondition(Skill castSkill, Creature performer, int tilex, int tiley, int layer) {
/* 49 */     if (!Servers.localServer.PVPSERVER) {
/*    */       
/* 51 */       performer.getCommunicator().sendNormalServerMessage("This spell does not work here.", (byte)3);
/* 52 */       return false;
/*    */     } 
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void doEffect(Skill castSkill, double power, Creature performer, int tilex, int tiley, int layer, int heightOffset) {
/* 61 */     performer.getCommunicator().sendNormalServerMessage("You call for aid from the dead spirits!");
/* 62 */     Server.getInstance().broadCastAction(performer.getName() + " calls for aid from the spirits of the dead.", performer, 10);
/*    */ 
/*    */     
/* 65 */     int minx = Zones.safeTileX(tilex - 5);
/* 66 */     int miny = Zones.safeTileY(tiley - 5);
/* 67 */     double maxnums = (1.0F + performer.getFaith() / 5.0F);
/* 68 */     maxnums = maxnums * power / 100.0D;
/* 69 */     for (int nums = 0; nums < maxnums; nums++) {
/*    */       
/* 71 */       int x = Zones.safeTileX(minx + Server.rand.nextInt(10));
/* 72 */       int y = Zones.safeTileY(miny + Server.rand.nextInt(10));
/* 73 */       boolean skip = false;
/* 74 */       if (!performer.isOnSurface())
/*    */       {
/* 76 */         if (Tiles.isSolidCave(Tiles.decodeType(Server.caveMesh.getTile(x, y))))
/* 77 */           skip = true; 
/*    */       }
/* 79 */       if (!skip)
/*    */         
/*    */         try {
/*    */           
/* 83 */           if (Zones.calculateHeight(((x << 2) + 2), ((y << 2) + 2), performer.isOnSurface()) > 0.0F)
/*    */           {
/* 85 */             byte sex = 0;
/* 86 */             if (Server.rand.nextInt(2) == 0)
/* 87 */               sex = 1; 
/* 88 */             byte ctype = (byte)Math.max(0, Server.rand.nextInt(22) - 10);
/* 89 */             if (Server.rand.nextInt(20) == 0)
/* 90 */               ctype = 99; 
/* 91 */             Creature.doNew(69, true, ((x << 2) + 2), ((y << 2) + 2), Server.rand
/* 92 */                 .nextFloat() * 360.0F, performer.getLayer(), "Zombie", sex, performer.getKingdomId(), ctype, true);
/*    */           }
/*    */         
/* 95 */         } catch (Exception exception) {} 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ZombieInfestation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */