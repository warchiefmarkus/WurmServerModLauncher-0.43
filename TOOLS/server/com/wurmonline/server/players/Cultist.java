/*      */ package com.wurmonline.server.players;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.TimeConstants;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.creatures.SpellEffectsEnum;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import com.wurmonline.shared.exceptions.WurmServerException;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Cultist
/*      */   implements MiscConstants, TimeConstants
/*      */ {
/*      */   private final long wurmid;
/*   53 */   private long lastMeditated = 0L;
/*      */   
/*   55 */   private long lastReceivedLevel = 0L;
/*      */   
/*   57 */   private long lastAppointedLevel = 0L;
/*      */   
/*   59 */   private long lastEnlightened = 0L;
/*      */   
/*   61 */   private long cooldown1 = 0L;
/*      */   
/*   63 */   private long cooldown2 = 0L;
/*      */   
/*   65 */   private long cooldown3 = 0L;
/*      */   
/*   67 */   private long cooldown4 = 0L;
/*      */   
/*   69 */   private long cooldown5 = 0L;
/*      */   
/*   71 */   private long cooldown6 = 0L;
/*      */   
/*   73 */   private long cooldown7 = 0L;
/*      */   
/*   75 */   private byte skillgainCount = 0;
/*      */   
/*   77 */   private byte level = 0;
/*      */   
/*   79 */   private byte path = 0;
/*      */   
/*      */   private boolean sendUseBody = false;
/*      */   private boolean bLoveEff = false;
/*      */   private boolean bWarDam = false;
/*      */   private boolean bStructDam = false;
/*      */   private boolean bFear = false;
/*      */   private boolean bNoElem = false;
/*      */   private boolean bTraps = false;
/*   88 */   private static final Logger logger = Logger.getLogger(Cultist.class.getName());
/*      */   
/*      */   private static final String GET_ALL_CULTISTS = "SELECT * FROM CULT";
/*      */   private static final String UPDATE_CULTIST = "UPDATE CULT SET LASTMEDITATED=?, LASTRECEIVEDLEVEL=?, LASTAPPOINTEDLEVEL=?, LEVEL=?, PATH=?, COOLDOWN1=?, COOLDOWN2=?, COOLDOWN3=?, COOLDOWN4=?,COOLDOWN5=?,COOLDOWN6=?,COOLDOWN7=? WHERE WURMID=?";
/*      */   private static final String CREATE_CULTIST = "INSERT INTO CULT (LASTMEDITATED, LASTRECEIVEDLEVEL, LASTAPPOINTEDLEVEL, LEVEL, PATH,COOLDOWN1,COOLDOWN2,COOLDOWN3,COOLDOWN4,COOLDOWN5,    COOLDOWN6,COOLDOWN7, WURMID) VALUES (?,?,?,?,?,?,?,?,?,?,   ?,?,?)";
/*      */   private static final String DELETE_CULTIST = "DELETE FROM CULT WHERE WURMID=?";
/*   94 */   private static final Map<Long, Cultist> CULTISTS = new HashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Cultist(long _wurmid, byte _path) {
/*  104 */     this.wurmid = _wurmid;
/*  105 */     this.path = _path;
/*  106 */     this.lastReceivedLevel = System.currentTimeMillis();
/*  107 */     this.skillgainCount = 1;
/*      */     
/*      */     try {
/*  110 */       saveCultist(true);
/*  111 */       CULTISTS.put(Long.valueOf(this.wurmid), this);
/*      */     }
/*  113 */     catch (IOException iox) {
/*      */       
/*  115 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Cultist(long _wurmid, long _lastMeditated, long _lastReceivedLevel, long _lastAppointedLevel, byte _level, byte _path, long _cd1, long _cd2, long _cd3, long _cd4, long _cd5, long _cd6, long _cd7) {
/*  144 */     this.wurmid = _wurmid;
/*  145 */     this.lastMeditated = _lastMeditated;
/*  146 */     this.lastReceivedLevel = _lastReceivedLevel;
/*  147 */     this.lastAppointedLevel = _lastAppointedLevel;
/*  148 */     this.level = _level;
/*  149 */     this.path = _path;
/*  150 */     this.cooldown1 = _cd1;
/*  151 */     this.cooldown2 = _cd2;
/*  152 */     this.cooldown3 = _cd3;
/*  153 */     this.cooldown4 = _cd4;
/*  154 */     this.cooldown5 = _cd5;
/*  155 */     this.cooldown6 = _cd6;
/*  156 */     this.cooldown7 = _cd7;
/*      */     
/*  158 */     CULTISTS.put(Long.valueOf(this.wurmid), this);
/*  159 */     this.bLoveEff = hasLoveEffect();
/*  160 */     this.bWarDam = doubleWarDamage();
/*  161 */     this.bStructDam = doubleStructDamage();
/*  162 */     this.bFear = hasFearEffect();
/*  163 */     this.bNoElem = hasNoElementalDamage();
/*  164 */     this.bTraps = ignoresTraps();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastEnlightened() {
/*  174 */     return this.lastEnlightened;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastEnlightened(long aLastEnlightened) {
/*  185 */     this.lastEnlightened = aLastEnlightened;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendBuffs() {
/*      */     try {
/*  192 */       Creature cultist = Server.getInstance().getCreature(this.wurmid);
/*  193 */       if (hasLoveEffect()) {
/*      */         
/*  195 */         int leLeft = getLoveEffectTimeLeftSeconds();
/*  196 */         cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.LOVE_EFFECT, leLeft, 100.0F);
/*      */       } 
/*      */ 
/*      */       
/*  200 */       if (doubleWarDamage()) {
/*      */         
/*  202 */         int timeLeft = getDoubleWarDamageTimeLeftSeconds();
/*  203 */         cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.HATE_DOUBLE_WAR, timeLeft, 100.0F);
/*      */       } 
/*      */ 
/*      */       
/*  207 */       if (doubleStructDamage()) {
/*      */         
/*  209 */         int timeLeft = getDoubleStructDamageTimeLeftSeconds();
/*  210 */         cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.HATE_DOUBLE_STRUCT, timeLeft, 100.0F);
/*      */       } 
/*      */ 
/*      */       
/*  214 */       if (hasFearEffect()) {
/*      */         
/*  216 */         int timeLeft = getFearEffectTimeLeftSeconds();
/*  217 */         cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.HATE_FEAR_EFFECT, timeLeft, 100.0F);
/*      */       } 
/*      */ 
/*      */       
/*  221 */       if (hasNoElementalDamage()) {
/*      */         
/*  223 */         int timeLeft = getElementalImmunityTimeLeftSeconds();
/*  224 */         cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POWER_NO_ELEMENTAL, timeLeft, 100.0F);
/*      */       } 
/*      */ 
/*      */       
/*  228 */       if (ignoresTraps()) {
/*      */         
/*  230 */         int trapsLeft = getIgnoreTrapsTimeLeftSeconds();
/*  231 */         cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POWER_IGNORE_TRAPS, trapsLeft, 100.0F);
/*      */       } 
/*      */ 
/*      */       
/*  235 */       sendPassiveBuffs(cultist, false, false, false, false, false, false);
/*      */     }
/*  237 */     catch (NoSuchPlayerException noSuchPlayerException) {
/*      */ 
/*      */     
/*      */     }
/*  241 */     catch (NoSuchCreatureException noSuchCreatureException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void sendPassiveBuffs(Creature cultist, boolean sentRegeneration, boolean sentSpellImmunity, boolean sentNoStaminaUse, boolean sentNoDecay, boolean sentIncreasedSkilGain, boolean sentShieldGone) {
/*  252 */     if (healsFaster() && !sentRegeneration)
/*      */     {
/*  254 */       cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.LOVE_HEALING_HANDS, -1, 100.0F);
/*      */     }
/*      */ 
/*      */     
/*  258 */     if (ignoresSpells() && !sentSpellImmunity)
/*      */     {
/*  260 */       cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.HATE_SPELL_IMMUNITY, -1, 100.0F);
/*      */     }
/*      */ 
/*      */     
/*  264 */     if (usesNoStamina() && !sentNoStaminaUse)
/*      */     {
/*  266 */       cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.POWER_USES_LESS_STAMINA, -1, 100.0F);
/*      */     }
/*      */ 
/*      */     
/*  270 */     if (isNoDecay() && !sentNoDecay)
/*      */     {
/*  272 */       cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.KNOWLEDGE_NO_DECAY, -1, 100.0F);
/*      */     }
/*      */ 
/*      */     
/*  276 */     if (levelElevenSkillgain() && !sentIncreasedSkilGain)
/*      */     {
/*  278 */       cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.KNOWLEDGE_INCREASED_SKILL_GAIN, -1, 100.0F);
/*      */     }
/*      */ 
/*      */     
/*  282 */     if (getHalfDamagePercentage() > 0.0F && !sentShieldGone)
/*      */     {
/*  284 */       cultist.getCommunicator().sendAddSpellEffect(SpellEffectsEnum.INSANITY_SHIELD_GONE, -1, 100.0F * 
/*  285 */           getHalfDamagePercentage());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getDoubleStructDamageTimeLeftSeconds() {
/*  291 */     return (int)(900000L - System.currentTimeMillis() - this.cooldown2) / 1000;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getDoubleWarDamageTimeLeftSeconds() {
/*  296 */     return (int)(900000L - System.currentTimeMillis() - this.cooldown1) / 1000;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getFearEffectTimeLeftSeconds() {
/*  301 */     return (int)(180000L - System.currentTimeMillis() - this.cooldown3) / 1000;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getElementalImmunityTimeLeftSeconds() {
/*  306 */     return (int)(1800000L - System.currentTimeMillis() - this.cooldown1) / 1000;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getIgnoreTrapsTimeLeftSeconds() {
/*  311 */     return (int)(1800000L - System.currentTimeMillis() - this.cooldown3) / 1000;
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getLoveEffectTimeLeftSeconds() {
/*  316 */     return (int)(180000L - System.currentTimeMillis() - this.cooldown3) / 1000;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getWurmId() {
/*  321 */     return this.wurmid;
/*      */   }
/*      */ 
/*      */   
/*      */   public long getTimeLeftToIncreasePath(long currentTime, double meditationSkill) {
/*  326 */     long time = currentTime - this.lastReceivedLevel;
/*  327 */     float modifier = ((this.level * 15) - meditationSkill < 0.0D) ? 0.5F : 1.0F;
/*  328 */     long neededTime = 0L;
/*  329 */     switch (this.level)
/*      */     
/*      */     { case 0:
/*  332 */         if (this.lastReceivedLevel > 0L) {
/*  333 */           neededTime = 43200000L;
/*      */         } else {
/*  335 */           neededTime = 0L;
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  344 */         return (long)((float)neededTime * modifier - (float)time);case 1: neededTime = 86400000L; return (long)((float)neededTime * modifier - (float)time); }  neededTime = Math.min(1555200000L, (long)((this.level * this.level) / 2.0F * 8.64E7F)); return (long)((float)neededTime * modifier - (float)time);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Map<Integer, Set<Cultist>> getCultistLeaders(byte inpath, int kingdom) {
/*  349 */     Map<Integer, Set<Cultist>> toReturn = new TreeMap<>();
/*  350 */     for (Cultist cultist : CULTISTS.values()) {
/*      */ 
/*      */       
/*  353 */       PlayerInfo pInfo = PlayerInfoFactory.getPlayerInfoWithWurmId(cultist.getWurmId());
/*  354 */       if (cultist.path == inpath && cultist.level > 3) {
/*      */         
/*  356 */         boolean show = false;
/*  357 */         if (pInfo != null) {
/*      */           
/*      */           try {
/*      */             
/*  361 */             pInfo.load();
/*  362 */             if (pInfo.currentServer == Servers.localServer.id) {
/*      */               
/*  364 */               byte kingdomId = Players.getInstance().getKingdomForPlayer(pInfo.wurmId);
/*      */               
/*  366 */               if (kingdomId == kingdom) {
/*  367 */                 show = true;
/*      */               }
/*      */             } 
/*  370 */           } catch (IOException iOException) {}
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  375 */         if (show) {
/*      */           
/*  377 */           Set<Cultist> subSet = toReturn.get(Integer.valueOf(cultist.level));
/*  378 */           if (subSet == null)
/*  379 */             subSet = new HashSet<>(); 
/*  380 */           subSet.add(cultist);
/*  381 */           toReturn.put(Integer.valueOf(cultist.level), subSet);
/*      */         } 
/*      */       } 
/*      */     } 
/*  385 */     return toReturn;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCultistTitle() {
/*  390 */     return Cults.getNameForLevel(this.path, this.level) + " of " + Cults.getPathNameFor(this.path);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCultistTitleShort() {
/*  395 */     return "the " + Cults.getNameForLevel(this.path, this.level);
/*      */   }
/*      */ 
/*      */   
/*      */   public void failedToLevel() {
/*  400 */     this.lastReceivedLevel = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  403 */       saveCultist(false);
/*      */     }
/*  405 */     catch (IOException iox) {
/*      */       
/*  407 */       logger.log(Level.WARNING, iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void increaseLevel() {
/*  413 */     boolean sentNoStamina = usesNoStamina();
/*  414 */     boolean sentSpellImmunity = ignoresSpells();
/*  415 */     boolean sentRegeneration = healsFaster();
/*  416 */     boolean sentNoDecay = isNoDecay();
/*  417 */     boolean sentSkillGain = levelElevenSkillgain();
/*  418 */     boolean sentShieldGone = (getHalfDamagePercentage() > 0.0F);
/*  419 */     setLevel((byte)(this.level + 1));
/*  420 */     this.lastReceivedLevel = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  423 */       Creature cultist = Server.getInstance().getCreature(this.wurmid);
/*      */       
/*  425 */       cultist.getCommunicator().sendSafeServerMessage("Congratulations! You have now reached the level of " + 
/*  426 */           getCultistTitle() + "!", (byte)2);
/*  427 */       Server.getInstance().broadCastAction(cultist
/*  428 */           .getName() + " has reached the level of " + getCultistTitle() + "!", cultist, 5);
/*  429 */       cultist.refreshVisible();
/*  430 */       cultist.getCommunicator().sendOwnTitles();
/*      */ 
/*      */       
/*  433 */       if (this.level == 4)
/*  434 */         cultist.achievement(570); 
/*  435 */       if (this.level == 7)
/*  436 */         cultist.achievement(578); 
/*  437 */       if (this.level == 9) {
/*  438 */         cultist.achievement(599);
/*      */       }
/*  440 */       String lg = getLevelGainString();
/*  441 */       if (!lg.equals(""))
/*      */       {
/*  443 */         cultist.getCommunicator().sendSafeServerMessage(lg);
/*  444 */         if (this.sendUseBody)
/*  445 */           cultist.getCommunicator().sendSafeServerMessage("Use your body to activate this knowledge."); 
/*  446 */         this.sendUseBody = false;
/*  447 */         sendPassiveBuffs(cultist, sentRegeneration, sentSpellImmunity, sentNoStamina, sentNoDecay, sentSkillGain, sentShieldGone);
/*      */       
/*      */       }
/*      */     
/*      */     }
/*  452 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */     
/*      */     }
/*  456 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setLastMeditated() {
/*  467 */     this.lastMeditated = System.currentTimeMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastMeditated() {
/*  477 */     return this.lastMeditated;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastMeditated(long aLastMeditated) {
/*  488 */     this.lastMeditated = aLastMeditated;
/*      */   }
/*      */ 
/*      */   
/*      */   void increaseSkillgain() {
/*  493 */     this.skillgainCount = (byte)(this.skillgainCount + 1);
/*      */   }
/*      */ 
/*      */   
/*      */   void decreaseSkillGain() {
/*  498 */     this.skillgainCount = (byte)(this.skillgainCount - 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public static final Cultist getCultist(long wid) {
/*  503 */     return CULTISTS.get(Long.valueOf(wid));
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void resetSkillGain() throws IOException {
/*  508 */     for (Cultist c : CULTISTS.values())
/*      */     {
/*  510 */       c.skillgainCount = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final void loadAllCultists() throws IOException {
/*  516 */     long start = System.nanoTime();
/*  517 */     Connection dbcon = null;
/*  518 */     PreparedStatement ps = null;
/*  519 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  522 */       dbcon = DbConnector.getPlayerDbCon();
/*  523 */       ps = dbcon.prepareStatement("SELECT * FROM CULT");
/*  524 */       rs = ps.executeQuery();
/*  525 */       while (rs.next())
/*      */       {
/*  527 */         new Cultist(rs.getLong("WURMID"), rs.getLong("LASTMEDITATED"), rs.getLong("LASTRECEIVEDLEVEL"), rs
/*  528 */             .getLong("LASTAPPOINTEDLEVEL"), rs.getByte("LEVEL"), rs.getByte("PATH"), rs.getLong("COOLDOWN1"), rs
/*  529 */             .getLong("COOLDOWN2"), rs.getLong("COOLDOWN3"), rs.getLong("COOLDOWN4"), rs.getLong("COOLDOWN5"), rs
/*  530 */             .getLong("COOLDOWN6"), rs.getLong("COOLDOWN7"));
/*      */       }
/*      */     }
/*  533 */     catch (SQLException sqex) {
/*      */       
/*  535 */       throw new IOException("Failed to load cultists", sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  539 */       DbUtilities.closeDatabaseObjects(ps, rs);
/*  540 */       DbConnector.returnConnection(dbcon);
/*      */       
/*  542 */       float lElapsedTime = (float)(System.nanoTime() - start) / 1000000.0F;
/*  543 */       logger.log(Level.INFO, "Loaded all cultists. It took " + lElapsedTime + " millis.");
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void saveCultist(boolean createNew) throws IOException {
/*  549 */     Connection dbcon = null;
/*  550 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  553 */       dbcon = DbConnector.getPlayerDbCon();
/*  554 */       if (createNew) {
/*  555 */         ps = dbcon.prepareStatement("INSERT INTO CULT (LASTMEDITATED, LASTRECEIVEDLEVEL, LASTAPPOINTEDLEVEL, LEVEL, PATH,COOLDOWN1,COOLDOWN2,COOLDOWN3,COOLDOWN4,COOLDOWN5,    COOLDOWN6,COOLDOWN7, WURMID) VALUES (?,?,?,?,?,?,?,?,?,?,   ?,?,?)");
/*      */       } else {
/*  557 */         ps = dbcon.prepareStatement("UPDATE CULT SET LASTMEDITATED=?, LASTRECEIVEDLEVEL=?, LASTAPPOINTEDLEVEL=?, LEVEL=?, PATH=?, COOLDOWN1=?, COOLDOWN2=?, COOLDOWN3=?, COOLDOWN4=?,COOLDOWN5=?,COOLDOWN6=?,COOLDOWN7=? WHERE WURMID=?");
/*  558 */       }  ps.setLong(1, this.lastMeditated);
/*  559 */       ps.setLong(2, this.lastReceivedLevel);
/*  560 */       ps.setLong(3, this.lastAppointedLevel);
/*  561 */       ps.setByte(4, this.level);
/*  562 */       ps.setByte(5, this.path);
/*  563 */       ps.setLong(6, this.cooldown1);
/*  564 */       ps.setLong(7, this.cooldown2);
/*  565 */       ps.setLong(8, this.cooldown3);
/*  566 */       ps.setLong(9, this.cooldown4);
/*  567 */       ps.setLong(10, this.cooldown5);
/*  568 */       ps.setLong(11, this.cooldown6);
/*  569 */       ps.setLong(12, this.cooldown7);
/*  570 */       ps.setLong(13, this.wurmid);
/*  571 */       ps.executeUpdate();
/*      */     }
/*  573 */     catch (SQLException sqex) {
/*      */       
/*  575 */       throw new IOException("Failed to save cultist " + this.wurmid, sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  579 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  580 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void deleteCultist() throws IOException {
/*  586 */     Connection dbcon = null;
/*  587 */     PreparedStatement ps = null;
/*      */     
/*      */     try {
/*  590 */       dbcon = DbConnector.getPlayerDbCon();
/*  591 */       ps = dbcon.prepareStatement("DELETE FROM CULT WHERE WURMID=?");
/*  592 */       ps.setLong(1, this.wurmid);
/*  593 */       ps.executeUpdate();
/*      */     }
/*  595 */     catch (SQLException sqex) {
/*      */       
/*  597 */       throw new IOException("Failed to save cultist " + this.wurmid, sqex);
/*      */     }
/*      */     finally {
/*      */       
/*  601 */       DbUtilities.closeDatabaseObjects(ps, null);
/*  602 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*  604 */     CULTISTS.remove(Long.valueOf(this.wurmid));
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchCooldown1() {
/*  609 */     this.cooldown1 = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  612 */       saveCultist(false);
/*      */     }
/*  614 */     catch (IOException iox) {
/*      */       
/*  616 */       logger.log(Level.WARNING, this.wurmid + " " + iox.getMessage(), iox);
/*      */     } 
/*  618 */     this.bWarDam = doubleWarDamage();
/*  619 */     this.bNoElem = hasNoElementalDamage();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchCooldown2() {
/*  624 */     this.cooldown2 = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  627 */       saveCultist(false);
/*      */     }
/*  629 */     catch (IOException iox) {
/*      */       
/*  631 */       logger.log(Level.WARNING, this.wurmid + " " + iox.getMessage(), iox);
/*      */     } 
/*  633 */     this.bStructDam = doubleStructDamage();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchCooldown3() {
/*  638 */     this.cooldown3 = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  641 */       saveCultist(false);
/*      */     }
/*  643 */     catch (IOException iox) {
/*      */       
/*  645 */       logger.log(Level.WARNING, this.wurmid + " " + iox.getMessage(), iox);
/*      */     } 
/*  647 */     this.bLoveEff = hasLoveEffect();
/*  648 */     this.bFear = hasFearEffect();
/*  649 */     this.bTraps = ignoresTraps();
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchCooldown4() {
/*  654 */     this.cooldown4 = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  657 */       saveCultist(false);
/*      */     }
/*  659 */     catch (IOException iox) {
/*      */       
/*  661 */       logger.log(Level.WARNING, this.wurmid + " " + iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchCooldown5() {
/*  667 */     this.cooldown5 = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  670 */       saveCultist(false);
/*      */     }
/*  672 */     catch (IOException iox) {
/*      */       
/*  674 */       logger.log(Level.WARNING, this.wurmid + " " + iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchCooldown6() {
/*  680 */     this.cooldown6 = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  683 */       saveCultist(false);
/*      */     }
/*  685 */     catch (IOException iox) {
/*      */       
/*  687 */       logger.log(Level.WARNING, this.wurmid + " " + iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void touchCooldown7() {
/*  693 */     this.cooldown7 = System.currentTimeMillis();
/*      */     
/*      */     try {
/*  696 */       saveCultist(false);
/*      */     }
/*  698 */     catch (IOException iox) {
/*      */       
/*  700 */       logger.log(Level.WARNING, this.wurmid + " " + iox.getMessage(), iox);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean skipsCooldown() {
/*      */     try {
/*  707 */       Creature cultist = Server.getInstance().getCreature(this.wurmid);
/*  708 */       if (Servers.localServer.testServer && cultist.getPower() >= 5)
/*      */       {
/*  710 */         return true;
/*      */       }
/*      */     }
/*  713 */     catch (NoSuchPlayerException|NoSuchCreatureException e) {
/*      */       
/*  715 */       e.printStackTrace();
/*      */     } 
/*  717 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayRefresh() {
/*  725 */     return (this.path == 1 && this.level > 3 && (System.currentTimeMillis() - this.cooldown1 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayEnchantNature() {
/*  735 */     return (this.path == 1 && this.level > 6 && (System.currentTimeMillis() - this.cooldown2 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasLoveEffect() {
/*  745 */     return (this.path == 1 && this.level > 8 && System.currentTimeMillis() - this.cooldown3 < 180000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayStartLoveEffect() {
/*  755 */     return (this.path == 1 && this.level > 8 && (System.currentTimeMillis() - this.cooldown3 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean healsFaster() {
/*  765 */     return (this.path == 1 && this.level > 10);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doubleWarDamage() {
/*  775 */     return (this.path == 2 && this.level > 6 && System.currentTimeMillis() - this.cooldown1 < 900000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayStartDoubleWarDamage() {
/*  785 */     return (this.path == 2 && this.level > 6 && (System.currentTimeMillis() - this.cooldown1 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean doubleStructDamage() {
/*  795 */     return (this.path == 2 && this.level > 3 && System.currentTimeMillis() - this.cooldown2 < 900000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayStartDoubleStructDamage() {
/*  805 */     return (this.path == 2 && this.level > 3 && (System.currentTimeMillis() - this.cooldown2 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasFearEffect() {
/*  815 */     return (this.path == 2 && this.level > 8 && System.currentTimeMillis() - this.cooldown3 < 180000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayStartFearEffect() {
/*  825 */     return (this.path == 2 && this.level > 8 && (System.currentTimeMillis() - this.cooldown3 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean ignoresSpells() {
/*  835 */     return (this.path == 2 && this.level > 10);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasNoElementalDamage() {
/*  845 */     return (this.path == 5 && this.level > 8 && System.currentTimeMillis() - this.cooldown1 < 1800000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayStartNoElementalDamage() {
/*  855 */     return (this.path == 5 && this.level > 8 && (System.currentTimeMillis() - this.cooldown1 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean maySpawnVolcano() {
/*  865 */     return (this.path == 5 && this.level > 6 && (System.currentTimeMillis() - this.cooldown2 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean ignoresTraps() {
/*  875 */     return (this.path == 5 && this.level > 3 && System.currentTimeMillis() - this.cooldown3 < 1800000L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayStartIgnoreTraps() {
/*  885 */     return (this.path == 5 && this.level > 3 && (System.currentTimeMillis() - this.cooldown3 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean usesNoStamina() {
/*  895 */     return (this.path == 5 && this.level > 10);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayCreatureInfo() {
/*  905 */     return (this.path == 3 && this.level > 3 && (System.currentTimeMillis() - this.cooldown1 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayInfoLocal() {
/*  915 */     return (this.path == 3 && this.level > 6 && (System.currentTimeMillis() - this.cooldown2 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isNoDecay() {
/*  925 */     return (this.path == 3 && this.level > 8);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean levelElevenSkillgain() {
/*  935 */     return (this.path == 3 && this.level > 10);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayCleanWounds() {
/*  945 */     return (this.path == 4 && this.level > 3 && (System.currentTimeMillis() - this.cooldown1 > 3600000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayRecall() {
/*  955 */     return (this.level > 11 && System.currentTimeMillis() - this.cooldown4 > 3600000L * Math.max(1, 12 - this.level - 12));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayDealFinalBreath() {
/*  965 */     return (this.level > 12 && ((float)(System.currentTimeMillis() - this.cooldown5) > 3600000.0F * 
/*  966 */       Math.max(1.0F, 2.0F - Math.max(0, this.level - 13) * 0.1F) || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayFillup() {
/*  976 */     return (this.path == 4 && this.level > 6 && (System.currentTimeMillis() - this.cooldown2 > 64800000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean mayTeleport() {
/*  986 */     return (this.path == 4 && this.level > 8 && (System.currentTimeMillis() - this.cooldown3 > 3600000L || skipsCooldown()));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendEffectEnd(String toSend, SpellEffectsEnum effect) {
/*      */     try {
/*  993 */       Creature cultist = Server.getInstance().getCreature(this.wurmid);
/*  994 */       cultist.getCommunicator().sendAlertServerMessage(toSend);
/*  995 */       cultist.getCommunicator().sendRemoveSpellEffect(effect);
/*      */     }
/*  997 */     catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */     
/*      */     }
/* 1001 */     catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void poll() {
/* 1009 */     if (this.bLoveEff && !hasLoveEffect()) {
/*      */       
/* 1011 */       this.bLoveEff = false;
/* 1012 */       sendEffectEnd("The stream of love fades.", SpellEffectsEnum.LOVE_EFFECT);
/*      */     } 
/* 1014 */     if (this.bWarDam && !doubleWarDamage()) {
/*      */       
/* 1016 */       this.bWarDam = false;
/* 1017 */       sendEffectEnd("You calm down.", SpellEffectsEnum.HATE_DOUBLE_WAR);
/*      */     } 
/* 1019 */     if (this.bStructDam && !doubleStructDamage()) {
/*      */       
/* 1021 */       this.bStructDam = false;
/* 1022 */       sendEffectEnd("Your rage goes away.", SpellEffectsEnum.HATE_DOUBLE_STRUCT);
/*      */     } 
/* 1024 */     if (this.bFear && !hasFearEffect()) {
/*      */       
/* 1026 */       this.bFear = false;
/* 1027 */       sendEffectEnd("You are no longer as fearful.", SpellEffectsEnum.HATE_FEAR_EFFECT);
/*      */     } 
/* 1029 */     if (this.bNoElem && !hasNoElementalDamage()) {
/*      */       
/* 1031 */       this.bNoElem = false;
/* 1032 */       sendEffectEnd("You are no longer protected from the elements.", SpellEffectsEnum.POWER_NO_ELEMENTAL);
/*      */     } 
/* 1034 */     if (this.bTraps && !ignoresTraps()) {
/*      */       
/* 1036 */       this.bTraps = false;
/* 1037 */       sendEffectEnd("You no longer focus on traps.", SpellEffectsEnum.POWER_IGNORE_TRAPS);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasHalfDamage() {
/* 1048 */     return (this.path == 4 && this.level > 10);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getHalfDamagePercentage() {
/* 1053 */     if (this.path != 4 || this.level < 7) {
/* 1054 */       return 0.0F;
/*      */     }
/* 1056 */     return Math.min(1.0F, Math.max(0.0F, (this.level - 6) * 20.0F) / 100.0F);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getLevelGainString() {
/* 1061 */     String toReturn = "";
/* 1062 */     this.sendUseBody = false;
/* 1063 */     switch (this.path)
/*      */     
/*      */     { case 1:
/* 1066 */         switch (this.level)
/*      */         
/*      */         { case 4:
/* 1069 */             toReturn = "You may now refresh people.";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1206 */             return toReturn;case 7: toReturn = "You may now enchant nature."; return toReturn;case 9: toReturn = "Your love may now protect you from most vile enemies for a short while."; this.sendUseBody = true; return toReturn;case 11: toReturn = "You now heal faster."; return toReturn;case 12: toReturn = "You may now recall home."; return toReturn;case 13: toReturn = "Your willpower now gives you the ability to deal a powerful short range blow to your enemies."; return toReturn; }  toReturn = ""; return toReturn;case 2: switch (this.level) { case 4: toReturn = "You may now harm structures more for a while."; this.sendUseBody = true; return toReturn;case 7: toReturn = "You may now rage, doing more harm in combat."; this.sendUseBody = true; return toReturn;case 9: toReturn = "You may now spread fear for a short while, protecting you."; this.sendUseBody = true; return toReturn;case 11: toReturn = "You now ignore aggressive spells targeted directly at you."; return toReturn;case 12: toReturn = "You may now recall home."; return toReturn;case 13: toReturn = "Your willpower now gives you the ability to deal a powerful short range blow to your enemies."; return toReturn; }  toReturn = ""; return toReturn;case 4: switch (this.level) { case 4: toReturn = "You find a new interest in cleaning dirty wounds."; return toReturn;case 7: toReturn = "You come to the conclusion that you need to eat less now."; this.sendUseBody = true; return toReturn;case 9: toReturn = "You realize that you can fly."; this.sendUseBody = true; return toReturn;case 11: toReturn = "You now stand above physical damage."; return toReturn;case 12: toReturn = "You may now recall home."; return toReturn;case 13: toReturn = "Your willpower now gives you the ability to deal a powerful short range blow to your enemies."; return toReturn; }  toReturn = ""; return toReturn;case 3: switch (this.level) { case 4: toReturn = "You have received deep insights in physiology."; return toReturn;case 7: toReturn = "You are now attuned to the surrounding area."; return toReturn;case 9: toReturn = "You understand how to cement your knowledge, never forgetting anything. You also feel the skills of creatures."; return toReturn;case 11: toReturn = "You now have mastered the learning process, and learn immensely fast."; return toReturn;case 12: toReturn = "You may now recall home."; return toReturn;case 13: toReturn = "Your willpower now gives you the ability to deal a powerful short range blow to your enemies."; return toReturn; }  toReturn = ""; return toReturn;case 5: switch (this.level) { case 4: toReturn = "You may heighten your senses for a while, avoiding traps."; this.sendUseBody = true; return toReturn;case 7: toReturn = "You attune to the earth, and may spawn magma."; return toReturn;case 9: toReturn = "You may now sometimes ignore elemental damage such as from fire, ice and even water."; this.sendUseBody = true; return toReturn;case 11: toReturn = "You can now work tirelessly."; return toReturn;case 12: toReturn = "You may now recall home."; return toReturn;case 13: toReturn = "Your willpower now gives you the ability to deal a powerful short range blow to your enemies."; return toReturn; }  toReturn = ""; return toReturn; }  toReturn = ""; return toReturn;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastReceivedLevel() {
/* 1216 */     return this.lastReceivedLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastReceivedLevel(long aLastReceivedLevel) {
/* 1227 */     this.lastReceivedLevel = aLastReceivedLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getLastAppointedLevel() {
/* 1237 */     return this.lastAppointedLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLastAppointedLevel(long aLastAppointedLevel) {
/* 1248 */     this.lastAppointedLevel = aLastAppointedLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCooldown1() {
/* 1258 */     return this.cooldown1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCooldown1(long aCooldown1) {
/* 1269 */     this.cooldown1 = aCooldown1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCooldown2() {
/* 1279 */     return this.cooldown2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCooldown2(long aCooldown2) {
/* 1290 */     this.cooldown2 = aCooldown2;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCooldown3() {
/* 1300 */     return this.cooldown3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCooldown3(long aCooldown3) {
/* 1311 */     this.cooldown3 = aCooldown3;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCooldown4() {
/* 1321 */     return this.cooldown4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCooldown4(long aCooldown4) {
/* 1332 */     this.cooldown4 = aCooldown4;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCooldown5() {
/* 1342 */     return this.cooldown5;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCooldown5(long aCooldown5) {
/* 1353 */     this.cooldown5 = aCooldown5;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCooldown6() {
/* 1363 */     return this.cooldown6;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCooldown6(long aCooldown6) {
/* 1374 */     this.cooldown6 = aCooldown6;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long getCooldown7() {
/* 1384 */     return this.cooldown7;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCooldown7(long aCooldown7) {
/* 1395 */     this.cooldown7 = aCooldown7;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getSkillgainCount() {
/* 1405 */     return this.skillgainCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setSkillgainCount(byte aSkillgainCount) {
/* 1416 */     this.skillgainCount = aSkillgainCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getLevel() {
/* 1426 */     return this.level;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLevel(byte aLevel) {
/* 1437 */     if (aLevel > Byte.MAX_VALUE) {
/* 1438 */       aLevel = Byte.MAX_VALUE;
/*      */     }
/* 1440 */     if (aLevel < 0) {
/* 1441 */       aLevel = 0;
/*      */     }
/* 1443 */     this.level = aLevel;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getPath() {
/* 1453 */     return this.path;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setPath(byte aPath) {
/* 1464 */     this.path = aPath;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\players\Cultist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */