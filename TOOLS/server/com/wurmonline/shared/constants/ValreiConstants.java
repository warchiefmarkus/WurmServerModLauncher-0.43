/*     */ package com.wurmonline.shared.constants;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValreiConstants
/*     */ {
/*     */   private static final int LONG = 8;
/*     */   private static final int INT = 4;
/*     */   private static final int FLOAT = 4;
/*     */   private static final int SHORT = 2;
/*     */   private static final int BYTE = 1;
/*     */   public static final short ACTION_NONE = 0;
/*     */   public static final short ACTION_ADDFIGHTER = 1;
/*     */   public static final short ACTION_MOVE = 2;
/*     */   public static final short ACTION_STARTTURN = 3;
/*     */   public static final short ACTION_MELEEATTACK = 4;
/*     */   public static final short ACTION_RANGEDATTACK = 5;
/*     */   public static final short ACTION_DEITYSPELL = 6;
/*     */   public static final short ACTION_SORCERYSPELL = 7;
/*     */   public static final short ACTION_ENDFIGHT = 8;
/*     */   public static final byte ATTACK_MISS = 0;
/*     */   public static final byte ATTACK_BLOCK = 1;
/*     */   public static final byte ATTACK_HIT = 2;
/*     */   public static final byte SPELLTYPE_HEAL = 0;
/*     */   public static final byte SPELLTYPE_OFFENSIVE = 1;
/*     */   public static final byte SPELLTYPE_PHYSICALDEFENSE = 2;
/*     */   public static final byte SPELLTYPE_SPELLDEFENSE = 3;
/*     */   public static final byte SPELLTYPE_ATTACKBUFF = 4;
/*     */   public static final float COST_HEAL = 30.0F;
/*     */   public static final float COST_OFFENSIVE = 20.0F;
/*     */   public static final float COST_PHYSICALDEFENSE = 60.0F;
/*     */   public static final float COST_SPELLDEFENSE = 60.0F;
/*     */   public static final float COST_ATTACKBUFF = 50.0F;
/*     */   
/*     */   public static String getFightActionName(ValreiFightAction fightAction) {
/*  66 */     switch (fightAction.getActionId()) {
/*     */       
/*     */       case 2:
/*  69 */         return "Move";
/*     */       case 3:
/*  71 */         return "Turn Begins";
/*     */       case 4:
/*  73 */         return "Melee Attack";
/*     */       case 5:
/*  75 */         return "Ranged Attack";
/*     */       case 6:
/*  77 */         return "Deity Spell";
/*     */       case 7:
/*  79 */         return "Sorcery Spell";
/*     */       case 8:
/*  81 */         return "Fight End";
/*     */     } 
/*     */     
/*  84 */     return "Unknown (" + fightAction.getActionId() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getFightActionSummary(ValreiFightAction fightAction) {
/*  89 */     ByteBuffer bb = ByteBuffer.wrap(fightAction.getActionData());
/*     */     
/*  91 */     switch (fightAction.getActionId()) {
/*     */       
/*     */       case 2:
/*  94 */         return "Fighter(" + bb.getLong() + ") to " + bb.getInt() + "," + bb.getInt();
/*     */       case 3:
/*  96 */         return "Fighter(" + bb.getLong() + ")";
/*     */       case 4:
/*     */       case 5:
/*  99 */         return "Fighter(" + bb.getLong() + ") vs Defender(" + bb.getLong() + ") " + getAttackStatus(bb.get()) + " for " + bb
/* 100 */           .getFloat() + " damage";
/*     */       case 6:
/*     */       case 7:
/* 103 */         return "Fighter(" + bb.getLong() + ") vs Defender(" + bb.getLong() + ") " + getSpellType(bb.get()) + "(" + 
/* 104 */           getAttackStatus(bb.get()) + ") for " + bb.getFloat() + " damage";
/*     */       case 8:
/* 106 */         return "Winner(" + bb.getLong() + ")";
/*     */     } 
/*     */     
/* 109 */     return "Unknown (" + fightAction.getActionId() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getFightActionActor(ValreiFightAction fightAction) {
/* 114 */     ByteBuffer bb = ByteBuffer.wrap(fightAction.getActionData());
/*     */     
/* 116 */     return bb.getLong();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getAttackStatus(byte status) {
/* 121 */     switch (status) {
/*     */       
/*     */       case 0:
/* 124 */         return "MISSES";
/*     */       case 1:
/* 126 */         return "BLOCKS";
/*     */       case 2:
/* 128 */         return "HITS";
/*     */     } 
/*     */     
/* 131 */     return "UNKNOWNS";
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getSpellType(byte spell) {
/* 136 */     switch (spell) {
/*     */       
/*     */       case 0:
/* 139 */         return "HEAL";
/*     */       case 1:
/* 141 */         return "OFFENSIVE";
/*     */       case 2:
/* 143 */         return "PHYSBUFF";
/*     */       case 3:
/* 145 */         return "SPELLBUFF";
/*     */       case 4:
/* 147 */         return "ATKBUFF";
/*     */     } 
/*     */     
/* 150 */     return "UNKNOWN";
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getMoveData(long fighterId, int xPos, int yPos) {
/* 155 */     ByteBuffer bb = ByteBuffer.allocate(16);
/*     */     
/* 157 */     bb.putLong(fighterId);
/* 158 */     bb.putInt(xPos);
/* 159 */     bb.putInt(yPos);
/*     */     
/* 161 */     return bb.array();
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getStartTurnData(long fighterId) {
/* 166 */     ByteBuffer bb = ByteBuffer.allocate(8);
/*     */     
/* 168 */     bb.putLong(fighterId);
/*     */     
/* 170 */     return bb.array();
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getAttackData(long fighterId, long defenderId, float damageDealt) {
/* 175 */     ByteBuffer bb = ByteBuffer.allocate(21);
/*     */     
/* 177 */     bb.putLong(fighterId);
/* 178 */     bb.putLong(defenderId);
/* 179 */     bb.put((damageDealt < 0.0F) ? 0 : ((damageDealt > 0.0F) ? 2 : 1));
/* 180 */     bb.putFloat(damageDealt);
/*     */     
/* 182 */     return bb.array();
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getSpellData(long fighterId, long defenderId, byte spellType, float damageDealt) {
/* 187 */     ByteBuffer bb = ByteBuffer.allocate(22);
/*     */     
/* 189 */     bb.putLong(fighterId);
/* 190 */     bb.putLong(defenderId);
/* 191 */     bb.put(spellType);
/* 192 */     bb.put((damageDealt == -1.0F) ? 0 : ((damageDealt > 0.0F) ? 2 : 1));
/* 193 */     bb.putFloat(damageDealt);
/*     */     
/* 195 */     return bb.array();
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] getEndFightData(long winnerId) {
/* 200 */     ByteBuffer bb = ByteBuffer.allocate(8);
/*     */     
/* 202 */     bb.putLong(winnerId);
/*     */     
/* 204 */     return bb.array();
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getEndFightWinner(byte[] actionData) {
/* 209 */     ByteBuffer bb = ByteBuffer.wrap(actionData);
/*     */     
/* 211 */     return bb.getLong();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class ValreiFightAction
/*     */   {
/*     */     private int actionNum;
/*     */     
/*     */     private short actionId;
/*     */     
/*     */     private byte[] actionData;
/*     */ 
/*     */     
/*     */     public ValreiFightAction(int actionNum, short actionId, byte[] actionData) {
/* 225 */       this.actionNum = actionNum;
/* 226 */       this.actionId = actionId;
/* 227 */       this.actionData = actionData;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getActionNum() {
/* 232 */       return this.actionNum;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setActionNum(int actionNum) {
/* 237 */       this.actionNum = actionNum;
/*     */     }
/*     */ 
/*     */     
/*     */     public short getActionId() {
/* 242 */       return this.actionId;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setActionId(short actionId) {
/* 247 */       this.actionId = actionId;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getActionData() {
/* 252 */       return this.actionData;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setActionData(byte[] actionData) {
/* 257 */       this.actionData = actionData;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\ValreiConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */