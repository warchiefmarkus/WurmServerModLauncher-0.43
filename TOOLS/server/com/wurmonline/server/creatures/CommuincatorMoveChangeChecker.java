/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.players.Player;
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
/*     */ final class CommuincatorMoveChangeChecker
/*     */ {
/*  35 */   private static final Logger logger = Logger.getLogger(CommuincatorMoveChangeChecker.class.getName());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean checkMoveChanges(PlayerMove currentmove, MovementScheme ticker, Player player, Logger cheatlogger) {
/*  55 */     checkIsFalling(currentmove, ticker);
/*  56 */     checkFloorOverride(currentmove, ticker);
/*     */     
/*  58 */     if ((checkBridgeChange(currentmove, player, cheatlogger) | 
/*  59 */       checkClimb(currentmove, ticker) | checkWeather(currentmove, ticker) | checkWindMod(currentmove, player, cheatlogger) | 
/*  60 */       checkMountSpeed(currentmove, player, cheatlogger) | checkSpeedMod(currentmove, player, cheatlogger) | checkHeightOffsetChanged(currentmove, player)) != 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  68 */       return true;
/*     */     }
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void checkFloorOverride(PlayerMove currentmove, MovementScheme ticker) {
/*  81 */     if (currentmove != null)
/*     */     {
/*  83 */       ticker.setOnFloorOverride(currentmove.isOnFloor());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean checkClimb(PlayerMove currentmove, MovementScheme ticker) {
/*  95 */     if (currentmove != null && currentmove.isToggleClimb()) {
/*     */       
/*  97 */       ticker.setServerClimbing(currentmove.isClimbing());
/*  98 */       currentmove.setToggleClimb(false);
/*  99 */       return true;
/*     */     } 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static void checkIsFalling(PlayerMove currentmove, MovementScheme ticker) {
/* 106 */     if (currentmove != null)
/*     */     {
/* 108 */       ticker.setIsFalling(currentmove.isFalling());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean checkWeather(PlayerMove currentmove, MovementScheme ticker) {
/* 120 */     if (currentmove != null && currentmove.isWeatherChange()) {
/*     */       
/* 122 */       ticker.diffWindX = Server.getWeather().getXWind();
/* 123 */       ticker.diffWindY = Server.getWeather().getYWind();
/* 124 */       ticker.setWindRotation(Server.getWeather().getWindRotation());
/* 125 */       ticker.setWindStrength(Server.getWeather().getWindPower());
/* 126 */       currentmove.setWeatherChange(false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 132 */       return true;
/*     */     } 
/* 134 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean checkSpeedMod(PlayerMove currentmove, Player player, Logger cheatlogger) {
/* 148 */     if (currentmove != null && currentmove.getNewSpeedMod() != -100.0F) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 170 */       player.getMovementScheme().setSpeedModifier(currentmove.getNewSpeedMod());
/*     */       
/* 172 */       currentmove.setNewSpeedMod(-100.0F);
/* 173 */       return true;
/*     */     } 
/* 175 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean checkBridgeChange(PlayerMove currentmove, Player player, Logger cheatlogger) {
/* 187 */     if (currentmove != null && currentmove.getNewBridgeId() != 0L) {
/*     */       
/* 189 */       if (player.getBridgeId() > 0L && currentmove.getNewBridgeId() < 0L)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 194 */         if (currentmove.getNewHeightOffset() > 0)
/*     */         {
/* 196 */           player.getMovementScheme().setGroundOffset(currentmove.getNewHeightOffset(), currentmove
/* 197 */               .isChangeHeightImmediately());
/*     */         }
/*     */       }
/*     */       
/* 201 */       player.setBridgeId(currentmove.getNewBridgeId(), false);
/* 202 */       player.getMovementScheme().setBridgeId(currentmove.getNewBridgeId());
/* 203 */       currentmove.setNewBridgeId(0L);
/* 204 */       return true;
/*     */     } 
/* 206 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean checkWindMod(PlayerMove currentmove, Player player, Logger cheatlogger) {
/* 219 */     if (currentmove != null && currentmove.getNewWindMod() != -100) {
/*     */       
/* 221 */       player.getMovementScheme().setWindMod(currentmove.getNewWindMod());
/* 222 */       currentmove.setNewWindMod((byte)-100);
/* 223 */       return true;
/*     */     } 
/* 225 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean checkMountSpeed(PlayerMove currentmove, Player player, Logger cheatlogger) {
/* 239 */     if (currentmove != null && currentmove.getNewMountSpeed() != -100) {
/*     */       
/* 241 */       player.getMovementScheme().setMountSpeed(currentmove.getNewMountSpeed());
/* 242 */       currentmove.setNewMountSpeed((short)-100);
/* 243 */       return true;
/*     */     } 
/* 245 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean checkHeightOffsetChanged(PlayerMove currentMove, Player player) {
/* 250 */     if (currentMove != null && currentMove.getNewHeightOffset() != -10000) {
/*     */       
/* 252 */       player.getMovementScheme().setGroundOffset(currentMove.getNewHeightOffset(), currentMove
/* 253 */           .isChangeHeightImmediately());
/* 254 */       currentMove.setNewHeightOffset(-10000);
/* 255 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 260 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\CommuincatorMoveChangeChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */