/*     */ package com.wurmonline.server.spells;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.SpellEffects;
/*     */ import com.wurmonline.server.items.RuneUtilities;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
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
/*     */ public final class SpellEffect
/*     */   implements MiscConstants
/*     */ {
/*     */   private static final String DELETE_EFFECT = "DELETE FROM SPELLEFFECTS WHERE WURMID=?";
/*     */   private static final String DELETE_EFFECTS_FOR_PLAYER = "DELETE FROM SPELLEFFECTS WHERE OWNER=?";
/*     */   private static final String DELETE_EFFECTS_FOR_ITEM = "DELETE FROM SPELLEFFECTS WHERE ITEMID=?";
/*     */   private static final String UPDATE_POWER = "UPDATE SPELLEFFECTS SET POWER=? WHERE WURMID=?";
/*     */   private static final String UPDATE_TIMELEFT = "UPDATE SPELLEFFECTS SET TIMELEFT=? WHERE WURMID=?";
/*     */   private static final String GET_EFFECTS_FOR_PLAYER = "SELECT * FROM SPELLEFFECTS WHERE OWNER=?";
/*     */   private static final String CREATE_EFFECT = "INSERT INTO SPELLEFFECTS (WURMID, OWNER,TYPE,POWER,TIMELEFT,EFFTYPE,INFLUENCE) VALUES(?,?,?,?,?,?,?)";
/*     */   private static final String CREATE_ITEM_EFFECT = "INSERT INTO SPELLEFFECTS (WURMID, ITEMID,TYPE,POWER,TIMELEFT) VALUES(?,?,?,?,?)";
/*  57 */   private static final Logger logger = Logger.getLogger(SpellEffect.class.getName());
/*     */   public final long id;
/*  59 */   public float power = 0.0F;
/*  60 */   public int timeleft = 0;
/*     */ 
/*     */   
/*     */   public final long owner;
/*     */   
/*     */   public final byte type;
/*     */   
/*     */   private final boolean isplayer;
/*     */   
/*     */   private final boolean isItem;
/*     */   
/*     */   private final byte effectType;
/*     */   
/*     */   private final byte influence;
/*     */   
/*     */   private boolean persistant = true;
/*     */ 
/*     */   
/*     */   public SpellEffect(long aOwner, byte aType, float aPower, int aTimeleft) {
/*  79 */     this(aOwner, aType, aPower, aTimeleft, (byte)9, (byte)0, true);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public SpellEffect(long aOwner, byte aType, float aPower, int aTimeleft, byte effType, byte influenceType, boolean persist) {
/*  96 */     this.owner = aOwner;
/*  97 */     this.type = aType;
/*  98 */     this.power = aPower;
/*  99 */     this.timeleft = aTimeleft;
/* 100 */     this.effectType = effType;
/* 101 */     this.influence = influenceType;
/* 102 */     this.persistant = persist;
/* 103 */     this.id = WurmId.getNextSpellId();
/* 104 */     if (WurmId.getType(aOwner) == 0) {
/*     */       
/* 106 */       this.isplayer = true;
/* 107 */       this.isItem = false;
/*     */     }
/* 109 */     else if (WurmId.getType(aOwner) == 2 || 
/* 110 */       WurmId.getType(aOwner) == 19 || 
/* 111 */       WurmId.getType(aOwner) == 20) {
/*     */       
/* 113 */       this.isplayer = false;
/* 114 */       this.isItem = true;
/*     */     }
/*     */     else {
/*     */       
/* 118 */       this.isplayer = false;
/* 119 */       this.isItem = false;
/*     */     } 
/* 121 */     if ((this.isplayer || this.isItem) && this.persistant) {
/* 122 */       save();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public SpellEffect(long aId, long aOwner, byte aType, float aPower, int aTimeleft, byte efftype, byte influ) {
/* 128 */     this.id = aId;
/* 129 */     this.owner = aOwner;
/* 130 */     this.type = aType;
/* 131 */     this.power = aPower;
/* 132 */     this.timeleft = aTimeleft;
/* 133 */     this.effectType = efftype;
/* 134 */     this.influence = influ;
/*     */     
/* 136 */     this.persistant = true;
/* 137 */     if (WurmId.getType(aOwner) == 0) {
/*     */       
/* 139 */       this.isplayer = true;
/* 140 */       this.isItem = false;
/*     */     }
/* 142 */     else if (WurmId.getType(aOwner) == 2) {
/*     */       
/* 144 */       this.isplayer = false;
/* 145 */       this.isItem = true;
/*     */     }
/*     */     else {
/*     */       
/* 149 */       this.isplayer = false;
/* 150 */       this.isItem = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getSpellEffectType() {
/* 156 */     return this.effectType;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getSpellInfluenceType() {
/* 161 */     return this.influence;
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean isSmeared() {
/* 166 */     return (this.type >= 77 && this.type <= 92);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 171 */     if (this.type == 22 && getPower() > 70.0F) {
/* 172 */       return "Thornshell";
/*     */     }
/* 174 */     if (this.type == 73)
/* 175 */       return "Newbie agg range buff"; 
/* 176 */     if (this.type == 74)
/* 177 */       return "Newbie food and drink buff"; 
/* 178 */     if (this.type == 75) {
/* 179 */       return "Newbie healing buff";
/*     */     }
/* 181 */     if (this.type == 64)
/* 182 */       return "Hunted"; 
/* 183 */     if (this.type == 72)
/* 184 */       return "Illusion"; 
/* 185 */     if (this.type == 78)
/* 186 */       return "potion of the ropemaker"; 
/* 187 */     if (this.type == 79)
/* 188 */       return "potion of mining"; 
/* 189 */     if (this.type == 77)
/* 190 */       return "oil of the weapon smith"; 
/* 191 */     if (this.type == 80)
/* 192 */       return "ointment of tailoring"; 
/* 193 */     if (this.type == 81)
/* 194 */       return "oil of the armour smith"; 
/* 195 */     if (this.type == 82)
/* 196 */       return "fletching potion"; 
/* 197 */     if (this.type == 83)
/* 198 */       return "oil of the blacksmith"; 
/* 199 */     if (this.type == 84)
/* 200 */       return "potion of leatherworking"; 
/* 201 */     if (this.type == 85)
/* 202 */       return "potion of shipbuilding"; 
/* 203 */     if (this.type == 86)
/* 204 */       return "ointment of stonecutting"; 
/* 205 */     if (this.type == 87)
/* 206 */       return "ointment of masonry"; 
/* 207 */     if (this.type == 88)
/* 208 */       return "potion of woodcutting"; 
/* 209 */     if (this.type == 89)
/* 210 */       return "potion of carpentry"; 
/* 211 */     if (this.type == 99)
/* 212 */       return "potion of butchery"; 
/* 213 */     if (this.type == 94)
/* 214 */       return "Incineration"; 
/* 215 */     if (this.type == 98) {
/* 216 */       return "Shatter Protection";
/*     */     }
/* 218 */     if (this.type < -10L) {
/* 219 */       return RuneUtilities.getRuneName(this.type);
/*     */     }
/* 221 */     return (Spells.getEnchantment(this.type)).name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLongDesc() {
/* 226 */     if (this.type == 78)
/* 227 */       return "improves rope making max ql"; 
/* 228 */     if (this.type == 79)
/* 229 */       return "improves mining max ql"; 
/* 230 */     if (this.type == 77)
/* 231 */       return "improves weapon smithing max ql"; 
/* 232 */     if (this.type == 80)
/* 233 */       return "improves tailoring max ql"; 
/* 234 */     if (this.type == 81)
/* 235 */       return "improves armour smithing max ql"; 
/* 236 */     if (this.type == 82)
/* 237 */       return "improves fletching max ql"; 
/* 238 */     if (this.type == 83)
/* 239 */       return "improves blacksmithing max ql"; 
/* 240 */     if (this.type == 84)
/* 241 */       return "improves leather working max ql"; 
/* 242 */     if (this.type == 85)
/* 243 */       return "improves ship building max ql"; 
/* 244 */     if (this.type == 86)
/* 245 */       return "improves stone cutting max ql"; 
/* 246 */     if (this.type == 87)
/* 247 */       return "improves masonry max ql"; 
/* 248 */     if (this.type == 88)
/* 249 */       return "improves wood cutting max ql"; 
/* 250 */     if (this.type == 89)
/* 251 */       return "improves carpentry max ql"; 
/* 252 */     if (this.type == 99)
/* 253 */       return "improves butchery product max ql"; 
/* 254 */     if (this.type == 98) {
/* 255 */       return "protects against damage when spells are cast upon it";
/*     */     }
/* 257 */     if (this.type < -10L) {
/* 258 */       return "will " + RuneUtilities.getRuneLongDesc(this.type);
/*     */     }
/* 260 */     return (Spells.getEnchantment(this.type)).effectdesc;
/*     */   }
/*     */ 
/*     */   
/*     */   private void save() {
/* 265 */     if (this.isplayer && this.persistant) {
/*     */       
/* 267 */       Connection dbcon = null;
/* 268 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 271 */         dbcon = DbConnector.getPlayerDbCon();
/*     */         
/* 273 */         ps = dbcon.prepareStatement("INSERT INTO SPELLEFFECTS (WURMID, OWNER,TYPE,POWER,TIMELEFT,EFFTYPE,INFLUENCE) VALUES(?,?,?,?,?,?,?)");
/* 274 */         ps.setLong(1, this.id);
/* 275 */         ps.setLong(2, this.owner);
/* 276 */         ps.setByte(3, this.type);
/* 277 */         ps.setFloat(4, this.power);
/* 278 */         ps.setInt(5, this.timeleft);
/* 279 */         ps.setByte(6, this.effectType);
/* 280 */         ps.setByte(7, this.influence);
/* 281 */         ps.executeUpdate();
/*     */       }
/* 283 */       catch (SQLException sqex) {
/*     */         
/* 285 */         logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 289 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 290 */         DbConnector.returnConnection(dbcon);
/*     */       }
/*     */     
/* 293 */     } else if (this.isItem && this.persistant) {
/*     */       
/* 295 */       Connection dbcon = null;
/* 296 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 299 */         dbcon = DbConnector.getItemDbCon();
/*     */         
/* 301 */         ps = dbcon.prepareStatement("INSERT INTO SPELLEFFECTS (WURMID, ITEMID,TYPE,POWER,TIMELEFT) VALUES(?,?,?,?,?)");
/* 302 */         ps.setLong(1, this.id);
/* 303 */         ps.setLong(2, this.owner);
/* 304 */         ps.setByte(3, this.type);
/* 305 */         ps.setFloat(4, this.power);
/* 306 */         ps.setInt(5, this.timeleft);
/* 307 */         ps.executeUpdate();
/*     */       }
/* 309 */       catch (SQLException sqex) {
/*     */         
/* 311 */         logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 315 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 316 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getPower() {
/* 328 */     return this.power;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPower(float newpower) {
/* 337 */     if (this.power != newpower) {
/*     */       
/* 339 */       this.power = newpower;
/* 340 */       if (this.persistant)
/*     */       {
/* 342 */         if (this.isplayer) {
/*     */           
/* 344 */           Connection dbcon = null;
/* 345 */           PreparedStatement ps = null;
/*     */           
/*     */           try {
/* 348 */             dbcon = DbConnector.getPlayerDbCon();
/*     */             
/* 350 */             ps = dbcon.prepareStatement("UPDATE SPELLEFFECTS SET POWER=? WHERE WURMID=?");
/* 351 */             ps.setFloat(1, this.power);
/* 352 */             ps.setLong(2, this.id);
/* 353 */             ps.executeUpdate();
/*     */           }
/* 355 */           catch (SQLException sqex) {
/*     */             
/* 357 */             logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */           }
/*     */           finally {
/*     */             
/* 361 */             DbUtilities.closeDatabaseObjects(ps, null);
/* 362 */             DbConnector.returnConnection(dbcon);
/*     */           }
/*     */         
/* 365 */         } else if (this.isItem) {
/*     */           
/* 367 */           Connection dbcon = null;
/* 368 */           PreparedStatement ps = null;
/*     */           
/*     */           try {
/* 371 */             dbcon = DbConnector.getItemDbCon();
/*     */             
/* 373 */             ps = dbcon.prepareStatement("UPDATE SPELLEFFECTS SET POWER=? WHERE WURMID=?");
/* 374 */             ps.setFloat(1, this.power);
/* 375 */             ps.setLong(2, this.id);
/* 376 */             ps.executeUpdate();
/*     */           }
/* 378 */           catch (SQLException sqex) {
/*     */             
/* 380 */             logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */           }
/*     */           finally {
/*     */             
/* 384 */             DbUtilities.closeDatabaseObjects(ps, null);
/* 385 */             DbConnector.returnConnection(dbcon);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void improvePower(Creature performer, float newpower) {
/* 399 */     float mod = 5.0F * (1.0F - this.power / (performer.hasFlag(82) ? 105.0F : 100.0F));
/* 400 */     setPower(mod + newpower);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimeleft(int newTimeleft) {
/* 409 */     if (this.timeleft != newTimeleft) {
/*     */       
/* 411 */       this.timeleft = newTimeleft;
/* 412 */       if (this.isplayer && this.persistant) {
/*     */         
/* 414 */         Connection dbcon = null;
/* 415 */         PreparedStatement ps = null;
/*     */         
/*     */         try {
/* 418 */           dbcon = DbConnector.getPlayerDbCon();
/*     */           
/* 420 */           ps = dbcon.prepareStatement("UPDATE SPELLEFFECTS SET TIMELEFT=? WHERE WURMID=?");
/* 421 */           ps.setInt(1, this.timeleft);
/* 422 */           ps.setLong(2, this.id);
/* 423 */           ps.executeUpdate();
/*     */         }
/* 425 */         catch (SQLException sqex) {
/*     */           
/* 427 */           logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */         }
/*     */         finally {
/*     */           
/* 431 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 432 */           DbConnector.returnConnection(dbcon);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveTimeleft() {
/* 440 */     if (this.isplayer && this.persistant) {
/*     */       
/* 442 */       Connection dbcon = null;
/* 443 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 446 */         dbcon = DbConnector.getPlayerDbCon();
/*     */         
/* 448 */         ps = dbcon.prepareStatement("UPDATE SPELLEFFECTS SET TIMELEFT=? WHERE WURMID=?");
/* 449 */         ps.setInt(1, this.timeleft);
/* 450 */         ps.setLong(2, this.id);
/* 451 */         ps.executeUpdate();
/*     */       }
/* 453 */       catch (SQLException sqex) {
/*     */         
/* 455 */         logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */       }
/*     */       finally {
/*     */         
/* 459 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 460 */         DbConnector.returnConnection(dbcon);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete() {
/* 467 */     if (this.persistant)
/*     */     {
/* 469 */       if (this.isplayer) {
/*     */         
/* 471 */         Connection dbcon = null;
/* 472 */         PreparedStatement ps = null;
/*     */         
/*     */         try {
/* 475 */           dbcon = DbConnector.getPlayerDbCon();
/*     */           
/* 477 */           ps = dbcon.prepareStatement("DELETE FROM SPELLEFFECTS WHERE WURMID=?");
/* 478 */           ps.setLong(1, this.id);
/* 479 */           ps.executeUpdate();
/*     */         }
/* 481 */         catch (SQLException sqex) {
/*     */           
/* 483 */           logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */         }
/*     */         finally {
/*     */           
/* 487 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 488 */           DbConnector.returnConnection(dbcon);
/*     */         }
/*     */       
/* 491 */       } else if (this.isItem) {
/*     */         
/* 493 */         Connection dbcon = null;
/* 494 */         PreparedStatement ps = null;
/*     */         
/*     */         try {
/* 497 */           dbcon = DbConnector.getItemDbCon();
/*     */           
/* 499 */           ps = dbcon.prepareStatement("DELETE FROM SPELLEFFECTS WHERE WURMID=?");
/* 500 */           ps.setLong(1, this.id);
/* 501 */           ps.executeUpdate();
/*     */         }
/* 503 */         catch (SQLException sqex) {
/*     */           
/* 505 */           logger.log(Level.WARNING, sqex.getMessage(), sqex);
/*     */         }
/*     */         finally {
/*     */           
/* 509 */           DbUtilities.closeDatabaseObjects(ps, null);
/* 510 */           DbConnector.returnConnection(dbcon);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean poll(SpellEffects effects) {
/* 518 */     this.timeleft--;
/*     */     
/* 520 */     if (this.timeleft <= 0) {
/*     */       
/* 522 */       effects.removeSpellEffect(this);
/* 523 */       return true;
/*     */     } 
/* 525 */     if (this.timeleft % 60 == 0)
/* 526 */       saveTimeleft(); 
/* 527 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final SpellEffect[] loadEffectsForPlayer(long wurmid) {
/* 532 */     SpellEffect[] spells = new SpellEffect[0];
/* 533 */     Connection dbcon = null;
/* 534 */     PreparedStatement ps = null;
/* 535 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 538 */       dbcon = DbConnector.getPlayerDbCon();
/* 539 */       ps = dbcon.prepareStatement("SELECT * FROM SPELLEFFECTS WHERE OWNER=?");
/* 540 */       ps.setLong(1, wurmid);
/* 541 */       rs = ps.executeQuery();
/* 542 */       Set<SpellEffect> spset = new HashSet<>();
/* 543 */       while (rs.next()) {
/*     */ 
/*     */         
/* 546 */         SpellEffect sp = new SpellEffect(rs.getLong("WURMID"), rs.getLong("OWNER"), rs.getByte("TYPE"), rs.getFloat("POWER"), rs.getInt("TIMELEFT"), rs.getByte("EFFTYPE"), rs.getByte("INFLUENCE"));
/* 547 */         spset.add(sp);
/*     */       } 
/* 549 */       if (spset.size() > 0) {
/* 550 */         spells = spset.<SpellEffect>toArray(new SpellEffect[spset.size()]);
/*     */       }
/* 552 */     } catch (SQLException sqx) {
/*     */       
/* 554 */       logger.log(Level.WARNING, wurmid + ": " + sqx.getMessage(), sqx);
/*     */     }
/*     */     finally {
/*     */       
/* 558 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 559 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 561 */     return spells;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void deleteEffectsForPlayer(long playerid) {
/* 566 */     Connection dbcon = null;
/* 567 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 570 */       dbcon = DbConnector.getPlayerDbCon();
/*     */       
/* 572 */       if (logger.isLoggable(Level.FINEST))
/*     */       {
/* 574 */         logger.finest("Deleting Effects for Player ID: " + playerid);
/*     */       }
/* 576 */       ps = dbcon.prepareStatement("DELETE FROM SPELLEFFECTS WHERE OWNER=?");
/* 577 */       ps.setLong(1, playerid);
/* 578 */       ps.executeUpdate();
/*     */     }
/* 580 */     catch (SQLException sqex) {
/*     */       
/* 582 */       logger.log(Level.WARNING, "Problem deleting effects for playerid: " + playerid + " due to " + sqex.getMessage(), sqex);
/*     */     
/*     */     }
/*     */     finally {
/*     */       
/* 587 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 588 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void deleteEffectsForItem(long itemid) {
/* 594 */     Connection dbcon = null;
/* 595 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 598 */       dbcon = DbConnector.getItemDbCon();
/*     */       
/* 600 */       ps = dbcon.prepareStatement("DELETE FROM SPELLEFFECTS WHERE ITEMID=?");
/* 601 */       ps.setLong(1, itemid);
/* 602 */       ps.executeUpdate();
/*     */     }
/* 604 */     catch (SQLException sqex) {
/*     */       
/* 606 */       logger.log(Level.WARNING, "Problem deleting effects for itemid: " + itemid + " due to " + sqex.getMessage(), sqex);
/*     */     }
/*     */     finally {
/*     */       
/* 610 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 611 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\SpellEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */