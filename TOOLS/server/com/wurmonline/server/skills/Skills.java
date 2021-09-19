/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.NoSuchPlayerException;
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.WurmId;
/*     */ import com.wurmonline.server.creatures.Communicator;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import com.wurmonline.shared.constants.CounterTypes;
/*     */ import java.io.IOException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nonnull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Skills
/*     */   implements MiscConstants, CounterTypes, TimeConstants
/*     */ {
/*  72 */   private static final ConcurrentHashMap<Long, Set<Skill>> creatureSkillsMap = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   Map<Integer, Skill> skills;
/*     */ 
/*     */ 
/*     */   
/*  80 */   long id = -10L;
/*     */ 
/*     */ 
/*     */   
/*  84 */   String templateName = null;
/*     */   
/*  86 */   private static Logger logger = Logger.getLogger(Skills.class.getName());
/*     */   
/*     */   public boolean paying = true;
/*     */   
/*     */   public boolean priest = false;
/*     */   
/*     */   public boolean hasSkillGain = true;
/*     */   
/*     */   private static final String moveWeek = "UPDATE SKILLS SET WEEK2=DAY7";
/*     */   
/*     */   private static final String moveDays = "UPDATE LOW_PRIORITY WURMPLAYERS.SKILLS SET DAY7=DAY6, DAY6=DAY5, DAY5=DAY4, DAY4=DAY3, DAY3=DAY2, DAY2=DAY1, DAY1=VALUE WHERE DAY7!=DAY6 OR DAY6!=DAY5 OR DAY5!=DAY4 OR DAY4!=DAY3 OR DAY3!=DAY2 OR DAY2!=DAY1 OR DAY1!=VALUE";
/*     */   
/*     */   private static final String moveDay6 = "UPDATE SKILLS SET DAY7=DAY6";
/*     */   
/*     */   private static final String moveDay5 = "UPDATE SKILLS SET DAY6=DAY5";
/*     */   private static final String moveDay4 = "UPDATE SKILLS SET DAY5=DAY4";
/*     */   private static final String moveDay3 = "UPDATE SKILLS SET DAY4=DAY3";
/*     */   private static final String moveDay2 = "UPDATE SKILLS SET DAY3=DAY2";
/*     */   private static final String moveDay1 = "UPDATE SKILLS SET DAY2=DAY1";
/*     */   private static final String moveDay0 = "UPDATE SKILLS SET DAY1=VALUE";
/* 106 */   public static final AtomicBoolean daySwitcherBeingRun = new AtomicBoolean();
/*     */   
/*     */   public static final float minChallengeValue = 21.0F;
/*     */ 
/*     */   
/*     */   Skills() {
/* 112 */     this.skills = new TreeMap<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTemplate() {
/* 117 */     return (this.templateName != null);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isPersonal() {
/* 122 */     return (this.id != -10L);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void switchWeek() {
/* 127 */     Connection dbcon = null;
/* 128 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 131 */       dbcon = DbConnector.getPlayerDbCon();
/* 132 */       ps = dbcon.prepareStatement("UPDATE SKILLS SET WEEK2=DAY7");
/* 133 */       ps.executeUpdate();
/*     */     }
/* 135 */     catch (SQLException ex) {
/*     */       
/* 137 */       logger.log(Level.WARNING, "moveWeek: UPDATE SKILLS SET WEEK2=DAY7 - " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 141 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 142 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final String getSkillSwitchString(int day) {
/* 148 */     switch (day) {
/*     */       
/*     */       case 0:
/* 151 */         return "UPDATE SKILLS SET DAY1=VALUE";
/*     */       case 1:
/* 153 */         return "UPDATE SKILLS SET DAY2=DAY1";
/*     */       case 2:
/* 155 */         return "UPDATE SKILLS SET DAY3=DAY2";
/*     */       case 3:
/* 157 */         return "UPDATE SKILLS SET DAY4=DAY3";
/*     */       case 4:
/* 159 */         return "UPDATE SKILLS SET DAY5=DAY4";
/*     */       case 5:
/* 161 */         return "UPDATE SKILLS SET DAY6=DAY5";
/*     */       case 6:
/* 163 */         return "UPDATE SKILLS SET DAY7=DAY6";
/*     */     } 
/* 165 */     logger.log(Level.WARNING, "This shouldn't happen: " + day);
/* 166 */     return "UPDATE SKILLS SET DAY7=DAY6";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void switchDay(int day) {
/* 172 */     Connection dbcon = null;
/* 173 */     PreparedStatement ps = null;
/* 174 */     String psString = getSkillSwitchString(day);
/*     */     
/*     */     try {
/* 177 */       dbcon = DbConnector.getPlayerDbCon();
/* 178 */       ps = dbcon.prepareStatement(psString);
/* 179 */       ps.executeUpdate();
/*     */     }
/* 181 */     catch (SQLException ex) {
/*     */       
/* 183 */       logger.log(Level.WARNING, "Day: " + day + " - " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 187 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 188 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final void switchDays() {
/* 194 */     Connection dbcon = null;
/* 195 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 198 */       dbcon = DbConnector.getPlayerDbCon();
/* 199 */       ps = dbcon.prepareStatement("UPDATE LOW_PRIORITY WURMPLAYERS.SKILLS SET DAY7=DAY6, DAY6=DAY5, DAY5=DAY4, DAY4=DAY3, DAY3=DAY2, DAY2=DAY1, DAY1=VALUE WHERE DAY7!=DAY6 OR DAY6!=DAY5 OR DAY5!=DAY4 OR DAY4!=DAY3 OR DAY3!=DAY2 OR DAY2!=DAY1 OR DAY1!=VALUE");
/* 200 */       ps.executeUpdate();
/*     */     }
/* 202 */     catch (SQLException ex) {
/*     */       
/* 204 */       logger.log(Level.WARNING, "Update days - " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 208 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 209 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void switchSkills(final long now) {
/* 218 */     if (!Servers.localServer.LOGINSERVER && !Server.getInstance().isPS()) {
/*     */       
/* 220 */       if (daySwitcherBeingRun.get()) {
/*     */         return;
/*     */       }
/* 223 */       final boolean switchWeek = (now - Servers.localServer.getSkillWeekSwitch() > 604800000L);
/* 224 */       final boolean switchDay = (now - Servers.localServer.getSkillDaySwitch() > 86400000L);
/* 225 */       if (!switchDay && !switchWeek) {
/*     */         return;
/*     */       }
/* 228 */       daySwitcherBeingRun.set(true);
/* 229 */       Thread statsPoller = new Thread("Skills Day/Week Updater")
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void run()
/*     */           {
/* 238 */             long start = System.currentTimeMillis();
/* 239 */             if (switchWeek) {
/*     */               
/* 241 */               Skills.logger.log(Level.INFO, "Switching skill week");
/* 242 */               Skills.switchWeek();
/* 243 */               Servers.localServer.setSkillWeekSwitch(now);
/*     */             } 
/* 245 */             if (switchDay) {
/*     */               
/* 247 */               Skills.logger.log(Level.INFO, "Switching skill day");
/* 248 */               Skills.switchDays();
/* 249 */               Servers.localServer.setSkillDaySwitch(now);
/*     */             } 
/* 251 */             Skills.logger.log(Level.INFO, "Skills Day/Week Updater took " + (System.currentTimeMillis() - start) + "ms");
/* 252 */             Skills.daySwitcherBeingRun.set(false);
/*     */           }
/*     */         };
/* 255 */       statsPoller.start();
/*     */     }
/*     */     else {
/*     */       
/* 259 */       Servers.localServer.setSkillDaySwitch(now);
/* 260 */       Servers.localServer.setSkillWeekSwitch(now);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TempSkill learnTemp(int skillNumber, float startValue) {
/* 266 */     TempSkill skill = new TempSkill(skillNumber, startValue, this);
/* 267 */     int[] needed = skill.getDependencies();
/* 268 */     for (int x = 0; x < needed.length; x++) {
/*     */       
/* 270 */       if (!this.skills.containsKey(Integer.valueOf(needed[x]))) {
/* 271 */         learnTemp(needed[x], 1.0F);
/*     */       }
/*     */     } 
/*     */     
/* 275 */     if (this.id != -10L)
/*     */     {
/* 277 */       if (WurmId.getType(this.id) == 0) {
/*     */         
/* 279 */         int parentSkillId = 0;
/* 280 */         if (needed.length > 0) {
/* 281 */           parentSkillId = needed[0];
/*     */         }
/*     */         try {
/* 284 */           if (parentSkillId != 0) {
/*     */             
/* 286 */             int parentType = SkillSystem.getTypeFor(parentSkillId);
/* 287 */             if (parentType == 0) {
/* 288 */               parentSkillId = Integer.MAX_VALUE;
/*     */             }
/* 290 */           } else if (skill.getType() == 1) {
/* 291 */             parentSkillId = 2147483646;
/*     */           } else {
/* 293 */             parentSkillId = Integer.MAX_VALUE;
/* 294 */           }  Affinity[] affs = Affinities.getAffinities(this.id);
/* 295 */           if (affs.length > 0)
/* 296 */             for (int i = 0; i < affs.length; i++) {
/*     */               
/* 298 */               if ((affs[i]).skillNumber == skillNumber) {
/* 299 */                 skill.affinity = (affs[i]).number;
/*     */               }
/*     */             }  
/* 302 */           Players.getInstance().getPlayer(this.id).getCommunicator().sendAddSkill(skillNumber, parentSkillId, skill
/* 303 */               .getName(), startValue, startValue, skill.affinity);
/*     */         }
/* 305 */         catch (NoSuchPlayerException noSuchPlayerException) {}
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 312 */     skill.touch();
/* 313 */     this.skills.put(Integer.valueOf(skillNumber), skill);
/* 314 */     return skill;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Skill learn(int skillNumber, float startValue) {
/* 320 */     return learn(skillNumber, startValue, true);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Skill learn(int skillNumber, float startValue, boolean sendAdd) {
/* 326 */     Skill skill = new DbSkill(skillNumber, startValue, this);
/* 327 */     int[] needed = skill.getDependencies();
/*     */ 
/*     */     
/* 330 */     for (int aNeeded : needed) {
/*     */       
/* 332 */       if (!this.skills.containsKey(Integer.valueOf(aNeeded))) {
/* 333 */         learn(aNeeded, 1.0F);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 340 */     if (this.id != -10L && WurmId.getType(this.id) == 0) {
/*     */       
/* 342 */       int parentSkillId = 0;
/* 343 */       if (needed.length > 0) {
/* 344 */         parentSkillId = needed[0];
/*     */       }
/*     */ 
/*     */       
/*     */       try {
/* 349 */         if (parentSkillId != 0) {
/*     */           
/* 351 */           int parentType = SkillSystem.getTypeFor(parentSkillId);
/* 352 */           if (parentType == 0) {
/* 353 */             parentSkillId = Integer.MAX_VALUE;
/*     */           }
/* 355 */         } else if (skill.getType() == 1) {
/* 356 */           parentSkillId = 2147483646;
/*     */         } else {
/* 358 */           parentSkillId = Integer.MAX_VALUE;
/*     */         } 
/*     */         
/* 361 */         for (Affinity aff : Affinities.getAffinities(this.id)) {
/*     */           
/* 363 */           if (aff.skillNumber == skillNumber)
/*     */           {
/* 365 */             skill.affinity = aff.number;
/*     */           }
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 372 */         Communicator comm = Players.getInstance().getPlayer(this.id).getCommunicator();
/* 373 */         if (sendAdd)
/*     */         {
/* 375 */           comm.sendAddSkill(skillNumber, parentSkillId, skill
/*     */               
/* 377 */               .getName(), startValue, startValue, skill.affinity);
/*     */ 
/*     */         
/*     */         }
/*     */         else
/*     */         {
/*     */           
/* 384 */           comm.sendUpdateSkill(skillNumber, startValue, skill.affinity);
/*     */         }
/*     */       
/* 387 */       } catch (NoSuchPlayerException nsp) {
/*     */         
/* 389 */         logger.log(Level.WARNING, "skillNumber: " + skillNumber + ", startValue: " + startValue, (Throwable)nsp);
/*     */       } 
/*     */     } 
/*     */     
/* 393 */     skill.touch();
/* 394 */     this.skills.put(Integer.valueOf(skillNumber), skill);
/*     */     
/*     */     try {
/* 397 */       skill.save();
/* 398 */       save();
/*     */     }
/* 400 */     catch (Exception ex) {
/*     */       
/* 402 */       logger.log(Level.WARNING, "Failed to save skill " + skill.getName() + "(" + skillNumber + ")", ex);
/*     */     } 
/* 404 */     return skill;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Skill getSkill(String name) throws NoSuchSkillException {
/* 436 */     Skill toReturn = null;
/* 437 */     for (Iterator<Skill> it = this.skills.values().iterator(); it.hasNext(); ) {
/*     */       
/* 439 */       Skill checked = it.next();
/* 440 */       if (checked.getName().equals(name)) {
/*     */         
/* 442 */         toReturn = checked;
/*     */         break;
/*     */       } 
/*     */     } 
/* 446 */     if (toReturn == null)
/* 447 */       throw new NoSuchSkillException("Unknown skill - " + name + ", total number of skills known is: " + this.skills.size()); 
/* 448 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Skill getSkill(int number) throws NoSuchSkillException {
/* 458 */     Skill toReturn = this.skills.get(Integer.valueOf(number));
/*     */     
/* 460 */     if (toReturn == null)
/*     */     {
/* 462 */       throw new NoSuchSkillException("Unknown skill - " + SkillSystem.getNameFor(number) + ", total number of skills known is: " + this.skills
/* 463 */           .size());
/*     */     }
/* 465 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void switchSkillNumbers(Skill skillOne, Skill skillTwo) {
/* 470 */     int numberOne = skillTwo.getNumber();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 475 */       skillTwo.setNumber(skillOne.getNumber());
/* 476 */       this.skills.put(Integer.valueOf(skillTwo.number), skillTwo);
/*     */       
/* 478 */       skillTwo.setKnowledge(skillTwo.knowledge, false, false);
/*     */     }
/* 480 */     catch (IOException iox2) {
/*     */       
/* 482 */       logger.log(Level.INFO, iox2.getMessage());
/*     */     } 
/*     */     
/*     */     try {
/* 486 */       skillOne.setNumber(numberOne);
/* 487 */       this.skills.put(Integer.valueOf(skillOne.number), skillOne);
/*     */       
/* 489 */       skillOne.setKnowledge(skillOne.knowledge, false, false);
/*     */     }
/* 491 */     catch (IOException iox) {
/*     */       
/* 493 */       logger.log(Level.INFO, iox.getMessage());
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
/*     */   @Nonnull
/*     */   public Skill getSkillOrLearn(int number) {
/* 506 */     Skill toReturn = this.skills.get(Integer.valueOf(number));
/*     */     
/* 508 */     if (toReturn == null)
/* 509 */       return learn(number, 1.0F); 
/* 510 */     return toReturn;
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
/*     */   public void checkDecay() {
/* 527 */     Set<Skill> memorySkills = new HashSet<>();
/* 528 */     Set<Skill> otherSkills = new HashSet<>();
/* 529 */     Set<Map.Entry<Integer, Skill>> toRemove = new HashSet<>();
/* 530 */     for (Iterator<Map.Entry<Integer, Skill>> iterator1 = this.skills.entrySet().iterator(); iterator1.hasNext(); ) {
/*     */       
/* 532 */       Map.Entry<Integer, Skill> entry = iterator1.next();
/* 533 */       Skill toCheck = entry.getValue();
/*     */       
/*     */       try {
/* 536 */         if (toCheck.getType() == 1) {
/* 537 */           memorySkills.add(toCheck); continue;
/*     */         } 
/* 539 */         otherSkills.add(toCheck);
/*     */       }
/* 541 */       catch (NullPointerException np) {
/*     */         
/* 543 */         toRemove.add(entry);
/*     */       } 
/*     */     } 
/*     */     Iterator<Skill> iterator;
/* 547 */     for (iterator = memorySkills.iterator(); iterator.hasNext(); ) {
/*     */       
/* 549 */       Skill mem = iterator.next();
/* 550 */       mem.checkDecay();
/*     */     } 
/*     */     
/* 553 */     for (iterator = otherSkills.iterator(); iterator.hasNext(); ) {
/*     */       
/* 555 */       Skill other = iterator.next();
/* 556 */       other.checkDecay();
/*     */     } 
/* 558 */     for (Iterator<Map.Entry<Integer, Skill>> it = toRemove.iterator(); it.hasNext(); ) {
/*     */       
/* 560 */       Map.Entry<Integer, Skill> entry = it.next();
/* 561 */       Integer toremove = entry.getKey();
/* 562 */       this.skills.remove(toremove);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Integer, Skill> getSkillTree() {
/* 568 */     return this.skills;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Skill[] getSkills() {
/* 577 */     Skill[] toReturn = new Skill[this.skills.size()];
/* 578 */     int i = 0;
/* 579 */     for (Iterator<Skill> it = this.skills.values().iterator(); it.hasNext(); ) {
/*     */       
/* 581 */       toReturn[i] = it.next();
/* 582 */       i++;
/*     */     } 
/* 584 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Skill[] getSkillsNoTemp() {
/* 593 */     Set<Skill> noTemps = new HashSet<>();
/* 594 */     for (Iterator<Skill> it = this.skills.values().iterator(); it.hasNext(); ) {
/*     */       
/* 596 */       Skill isTemp = it.next();
/* 597 */       if (!isTemp.isTemporary()) {
/* 598 */         noTemps.add(isTemp);
/*     */       }
/*     */     } 
/* 601 */     Skill[] toReturn = noTemps.<Skill>toArray(new Skill[noTemps.size()]);
/* 602 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clone(Skill[] skillarr) {
/* 607 */     this.skills = new TreeMap<>();
/* 608 */     for (int x = 0; x < skillarr.length; x++) {
/*     */       
/* 610 */       if (!skillarr[x].isTemporary() && !(skillarr[x] instanceof TempSkill)) {
/*     */         
/* 612 */         DbSkill newSkill = new DbSkill(skillarr[x].getNumber(), (skillarr[x]).knowledge, this);
/* 613 */         this.skills.put(Integer.valueOf(skillarr[x].getNumber()), newSkill);
/*     */         
/*     */         try {
/* 616 */           newSkill.touch();
/* 617 */           newSkill.save();
/*     */         }
/* 619 */         catch (Exception iox) {
/*     */           
/* 621 */           logger.log(Level.WARNING, "Failed to save skill " + newSkill.getName() + " for " + this.id, iox);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 626 */         TempSkill newSkill = new TempSkill(skillarr[x].getNumber(), (skillarr[x]).knowledge, this);
/* 627 */         this.skills.put(Integer.valueOf(skillarr[x].getNumber()), newSkill);
/* 628 */         newSkill.touch();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getId() {
/* 639 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void clearCreatureLoadMap() {
/* 644 */     creatureSkillsMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void loadAllCreatureSkills() throws Exception {
/* 649 */     Connection dbcon = null;
/* 650 */     PreparedStatement ps = null;
/* 651 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 654 */       dbcon = DbConnector.getCreatureDbCon();
/* 655 */       ps = dbcon.prepareStatement("SELECT * FROM SKILLS");
/* 656 */       rs = ps.executeQuery();
/* 657 */       while (rs.next())
/*     */       {
/*     */         
/* 660 */         Skill skill = new DbSkill(rs.getLong("ID"), rs.getInt("NUMBER"), rs.getDouble("VALUE"), rs.getDouble("MINVALUE"), rs.getLong("LASTUSED"));
/* 661 */         long owner = rs.getLong("OWNER");
/*     */         
/* 663 */         Set<Skill> skills = creatureSkillsMap.get(Long.valueOf(owner));
/* 664 */         if (skills == null)
/* 665 */           skills = new HashSet<>(); 
/* 666 */         skills.add(skill);
/* 667 */         creatureSkillsMap.put(Long.valueOf(owner), skills);
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 672 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 673 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void fillCreatureTempSkills(Creature creature) {
/* 679 */     Skills cSkills = creature.getSkills();
/* 680 */     Map<Integer, Skill> treeSkills = creature.getSkills().getSkillTree();
/* 681 */     CreatureTemplate template = creature.getTemplate();
/*     */     
/*     */     try {
/* 684 */       Skills tSkills = template.getSkills();
/*     */       
/* 686 */       for (Skill ts : tSkills.getSkills())
/*     */       {
/* 688 */         if (!treeSkills.containsKey(Integer.valueOf(ts.getNumber())))
/*     */         {
/* 690 */           cSkills.learnTemp(ts.getNumber(), (float)ts.knowledge);
/*     */         }
/*     */       }
/*     */     
/* 694 */     } catch (Exception e) {
/*     */       
/* 696 */       logger.log(Level.WARNING, "Unknown error while checking temp skill for creature: " + creature.getWurmId() + ".", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void initializeSkills() {
/* 702 */     Set<Skill> skillSet = creatureSkillsMap.get(Long.valueOf(this.id));
/* 703 */     if (skillSet == null)
/*     */       return; 
/* 705 */     for (Skill skill : skillSet) {
/*     */       
/* 707 */       Skill dbSkill = new DbSkill(skill.id, this, skill.getNumber(), skill.knowledge, skill.minimum, skill.lastUsed);
/*     */       
/* 709 */       this.skills.put(Integer.valueOf(dbSkill.getNumber()), dbSkill);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTemplateName() {
/* 719 */     return this.templateName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveDirty() throws IOException {
/* 724 */     if (this.id != -10L)
/*     */     {
/* 726 */       if (WurmId.getType(this.id) == 0)
/*     */       {
/* 728 */         for (Skill skill : this.skills.values())
/*     */         {
/* 730 */           skill.saveValue(true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/* 738 */     if (this.id != -10L)
/*     */     {
/* 740 */       if (WurmId.getType(this.id) == 0)
/*     */       {
/* 742 */         for (Skill skill : this.skills.values()) {
/*     */           
/* 744 */           if (skill.isDirty()) {
/* 745 */             skill.saveValue(true);
/*     */           }
/*     */         } 
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final void addTempSkills() {
/* 754 */     float initialTempValue = (WurmId.getType(this.id) == 0) ? Servers.localServer.getSkilloverallval() : 1.0F;
/* 755 */     for (int i = 0; i < SkillList.skillArray.length; i++) {
/*     */       
/* 757 */       Integer key = Integer.valueOf(SkillList.skillArray[i]);
/* 758 */       if (!this.skills.containsKey(key))
/*     */       {
/* 760 */         if (key.intValue() == 1023 && WurmId.getType(this.id) == 0) {
/*     */           
/* 762 */           learnTemp(key.intValue(), Servers.localServer.getSkillfightval());
/*     */         }
/* 764 */         else if (key.intValue() == 100 && 
/* 765 */           WurmId.getType(this.id) == 0) {
/*     */           
/* 767 */           learnTemp(key.intValue(), Servers.localServer.getSkillmindval());
/*     */         }
/* 769 */         else if (key.intValue() == 104 && 
/* 770 */           WurmId.getType(this.id) == 0) {
/*     */           
/* 772 */           learnTemp(key.intValue(), Servers.localServer.getSkillbcval());
/*     */         }
/*     */         else {
/*     */           
/* 776 */           learnTemp(key.intValue(), initialTempValue);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract void load() throws Exception;
/*     */   
/*     */   public abstract void delete() throws Exception;
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\Skills.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */