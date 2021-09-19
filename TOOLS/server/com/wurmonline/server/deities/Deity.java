/*      */ package com.wurmonline.server.deities;
/*      */ 
/*      */ import com.wurmonline.server.DbConnector;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.Players;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.behaviours.Action;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.epic.EpicMission;
/*      */ import com.wurmonline.server.epic.EpicServerStatus;
/*      */ import com.wurmonline.server.kingdom.Kingdom;
/*      */ import com.wurmonline.server.kingdom.Kingdoms;
/*      */ import com.wurmonline.server.players.Player;
/*      */ import com.wurmonline.server.spells.Spell;
/*      */ import com.wurmonline.server.utils.DbUtilities;
/*      */ import java.io.IOException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ 
/*      */ 
/*      */ public abstract class Deity
/*      */   implements MiscConstants
/*      */ {
/*      */   public static final byte TYPE_MISSIONAIRY = 0;
/*      */   public static final byte TYPE_CHAPLAIN = 1;
/*      */   public final int number;
/*      */   public final String name;
/*      */   public int alignment;
/*      */   public final byte sex;
/*      */   byte power;
/*      */   double faith;
/*      */   int favor;
/*      */   float attack;
/*      */   float vitality;
/*   70 */   private static Logger logger = Logger.getLogger(Deity.class.getName());
/*      */   public final int holyItem;
/*   72 */   public String[] convertText1 = new String[10];
/*   73 */   public String[] altarConvertText1 = new String[10];
/*   74 */   private final Set<Spell> spells = new HashSet<>();
/*   75 */   private final Set<Spell> creatureSpells = new HashSet<>();
/*   76 */   private final Set<Spell> itemSpells = new HashSet<>();
/*   77 */   private final Set<Spell> woundSpells = new HashSet<>();
/*   78 */   private final Set<Spell> tileSpells = new HashSet<>();
/*      */   
/*      */   private static final String insertKarma = "INSERT INTO HELPERS (WURMID,KARMA,DEITY) VALUES (?,?,?)";
/*      */   private static final String updateKarma = "UPDATE HELPERS SET KARMA=?,DEITY=? WHERE WURMID=?";
/*      */   private static final String loadKarma = "SELECT * FROM HELPERS WHERE DEITY=?";
/*   83 */   private final ConcurrentHashMap<Long, Float> karmavals = new ConcurrentHashMap<>();
/*      */   
/*   85 */   private int templateDeity = 0;
/*      */   
/*      */   private boolean roadProtector = false;
/*      */   
/*   89 */   private float buildWallBonus = 0.0F;
/*      */   
/*      */   private boolean warrior = false;
/*      */   
/*      */   private boolean befriendCreature = false;
/*      */   
/*      */   private boolean befriendMonster = false;
/*      */   
/*      */   private boolean staminaBonus = false;
/*      */   
/*      */   private boolean foodBonus = false;
/*      */   
/*      */   private boolean healer = false;
/*      */   
/*      */   private boolean deathProtector = false;
/*      */   
/*      */   private boolean deathItemProtector = false;
/*      */   
/*      */   private boolean favorRegenerator = false;
/*      */   
/*      */   private boolean allowsButchering = false;
/*      */   
/*      */   private boolean woodAffinity = false;
/*      */   
/*      */   private boolean metalAffinity = false;
/*      */   
/*      */   private boolean clothAffinity = false;
/*      */   
/*      */   private boolean clayAffinity = false;
/*      */   
/*      */   private boolean meatAffinity = false;
/*      */   
/*      */   private boolean foodAffinity = false;
/*      */   
/*      */   private boolean learner = false;
/*      */   
/*      */   private boolean itemProtector = false;
/*      */   
/*      */   private boolean repairer = false;
/*      */   
/*      */   private boolean waterGod = false;
/*      */   
/*      */   private boolean mountainGod = false;
/*      */   
/*      */   private boolean forestGod = false;
/*      */   
/*      */   private boolean hateGod = false;
/*      */   
/*      */   public int lastConfrontationTileX;
/*      */   public int lastConfrontationTileY;
/*  139 */   private int activeFollowers = 0;
/*      */   private final Random rand;
/*  141 */   private byte favoredKingdom = 0;
/*      */ 
/*      */ 
/*      */   
/*      */   Deity(int num, String nam, byte align, byte aSex, byte pow, double aFaith, int holyitem, int _favor, float _attack, float _vitality, boolean create) {
/*  146 */     this.number = num;
/*  147 */     this.name = nam;
/*  148 */     this.alignment = align;
/*  149 */     this.sex = aSex;
/*  150 */     this.power = pow;
/*  151 */     this.faith = aFaith;
/*  152 */     this.holyItem = holyitem;
/*  153 */     this.favor = _favor;
/*  154 */     this.attack = _attack;
/*  155 */     this.vitality = _vitality;
/*  156 */     this.rand = new Random((this.number * 1001));
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getHeSheItString() {
/*  161 */     if (this.sex == 0)
/*  162 */       return "he"; 
/*  163 */     if (this.sex == 1) {
/*  164 */       return "she";
/*      */     }
/*  166 */     return "it";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getCapHeSheItString() {
/*  172 */     if (this.sex == 0)
/*  173 */       return "He"; 
/*  174 */     if (this.sex == 1) {
/*  175 */       return "She";
/*      */     }
/*  177 */     return "It";
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getHisHerItsString() {
/*  182 */     if (this.sex == 0)
/*  183 */       return "his"; 
/*  184 */     if (this.sex == 1) {
/*  185 */       return "her";
/*      */     }
/*  187 */     return "its";
/*      */   }
/*      */ 
/*      */   
/*      */   public final String getHimHerItString() {
/*  192 */     if (this.sex == 0)
/*  193 */       return "him"; 
/*  194 */     if (this.sex == 1) {
/*  195 */       return "her";
/*      */     }
/*  197 */     return "it";
/*      */   }
/*      */ 
/*      */   
/*      */   public final int getTemplateDeity() {
/*  202 */     return this.templateDeity;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setTemplateDeity(int templateDeity) {
/*  207 */     this.templateDeity = templateDeity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean accepts(float align) {
/*  214 */     return (align >= (this.alignment - 100) && align <= (this.alignment + 100));
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
/*      */   public final boolean isActionFaithful(Action action) {
/*  237 */     int num = action.getNumber();
/*  238 */     return isActionFaithful(num);
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
/*      */   final boolean isActionFaithful(int num) {
/*  253 */     if (num == 191)
/*      */     {
/*  255 */       return !this.roadProtector;
/*      */     }
/*  257 */     if (num == 174 || num == 172 || num == 524)
/*      */     {
/*  259 */       return (this.buildWallBonus <= 0.0F);
/*      */     }
/*  261 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void punishCreature(Creature performer, int actionNumber) {
/*  266 */     float lPower = 0.0F;
/*  267 */     if (actionNumber == 191) {
/*  268 */       lPower = 0.05F;
/*  269 */     } else if (actionNumber == 174 || actionNumber == 172) {
/*  270 */       lPower = 0.05F;
/*  271 */     } else if (actionNumber == 221) {
/*  272 */       lPower = 0.5F;
/*  273 */     }  if (lPower > 0.0F) {
/*      */       
/*  275 */       performer.modifyFaith(-lPower);
/*      */       
/*      */       try {
/*  278 */         performer.setFavor(performer.getFavor() - lPower);
/*      */       }
/*  280 */       catch (IOException iox) {
/*      */         
/*  282 */         logger.log(Level.WARNING, performer.getName() + " " + this.name, iox);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean performActionOkey(Creature performer, Action action) {
/*  293 */     if (!isActionFaithful(action))
/*      */     {
/*  295 */       if (Server.rand.nextInt(100) <= 10) {
/*      */         
/*  297 */         punishCreature(performer, action.getNumber());
/*  298 */         return false;
/*      */       } 
/*      */     }
/*  301 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void removeSpell(Spell spell) {
/*  310 */     this.spells.remove(spell);
/*  311 */     if (spell.isTargetCreature())
/*      */     {
/*  313 */       this.creatureSpells.remove(spell);
/*      */     }
/*  315 */     if (spell.isTargetAnyItem())
/*      */     {
/*  317 */       this.itemSpells.remove(spell);
/*      */     }
/*  319 */     if (spell.isTargetWound())
/*      */     {
/*  321 */       this.woundSpells.remove(spell);
/*      */     }
/*  323 */     if (spell.isTargetTile())
/*      */     {
/*  325 */       this.tileSpells.remove(spell);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void addSpell(Spell spell) {
/*  335 */     this.spells.add(spell);
/*  336 */     if (spell.isTargetCreature())
/*      */     {
/*  338 */       this.creatureSpells.add(spell);
/*      */     }
/*  340 */     if (spell.isTargetAnyItem())
/*      */     {
/*  342 */       this.itemSpells.add(spell);
/*      */     }
/*  344 */     if (spell.isTargetWound())
/*      */     {
/*  346 */       this.woundSpells.add(spell);
/*      */     }
/*  348 */     if (spell.isTargetTile())
/*      */     {
/*  350 */       this.tileSpells.add(spell);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public final boolean hasSpell(Spell spell) {
/*  356 */     return this.spells.contains(spell);
/*      */   }
/*      */ 
/*      */   
/*      */   public final Set<Spell> getSpells() {
/*  361 */     return this.spells;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Spell[] getSpellsTargettingCreatures(int level) {
/*  366 */     Set<Spell> toReturn = new HashSet<>();
/*      */     
/*  368 */     for (Spell s : this.creatureSpells) {
/*      */       
/*  370 */       if (s.level <= level && (
/*  371 */         !s.isRitual || getFavor() > 100000))
/*  372 */         toReturn.add(s); 
/*      */     } 
/*  374 */     Spell[] spells = toReturn.<Spell>toArray(new Spell[toReturn.size()]);
/*  375 */     Arrays.sort((Object[])spells);
/*  376 */     return spells;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Spell[] getSpellsTargettingWounds(int level) {
/*  381 */     Set<Spell> toReturn = new HashSet<>();
/*      */     
/*  383 */     for (Spell s : this.woundSpells) {
/*      */       
/*  385 */       if (s.level <= level && (
/*  386 */         !s.isRitual || getFavor() > 100000))
/*  387 */         toReturn.add(s); 
/*      */     } 
/*  389 */     Spell[] spells = toReturn.<Spell>toArray(new Spell[toReturn.size()]);
/*  390 */     Arrays.sort((Object[])spells);
/*  391 */     return spells;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Spell[] getSpellsTargettingItems(int level) {
/*  396 */     Set<Spell> toReturn = new HashSet<>();
/*      */     
/*  398 */     for (Spell s : this.itemSpells) {
/*      */       
/*  400 */       if (s.level <= level && (
/*  401 */         !s.isRitual || getFavor() > 100000 || Servers.isThisATestServer()))
/*  402 */         toReturn.add(s); 
/*      */     } 
/*  404 */     Spell[] spells = toReturn.<Spell>toArray(new Spell[toReturn.size()]);
/*  405 */     Arrays.sort((Object[])spells);
/*  406 */     return spells;
/*      */   }
/*      */ 
/*      */   
/*      */   public final Spell[] getSpellsTargettingTiles(int level) {
/*  411 */     Set<Spell> toReturn = new HashSet<>();
/*  412 */     for (Spell s : this.tileSpells) {
/*      */       
/*  414 */       if (s.level <= level)
/*      */       {
/*  416 */         if (!s.isRitual || getFavor() > 100000)
/*      */         {
/*  418 */           toReturn.add(s);
/*      */         }
/*      */       }
/*      */     } 
/*  422 */     Spell[] spells = toReturn.<Spell>toArray(new Spell[toReturn.size()]);
/*  423 */     Arrays.sort((Object[])spells);
/*  424 */     return spells;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFavor() {
/*  429 */     return this.favor;
/*      */   }
/*      */ 
/*      */   
/*      */   public void increaseFavor() {
/*  434 */     setFavor(this.favor + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void save() throws IOException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract void setFaith(double paramDouble) throws IOException;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setFavor(int paramInt);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String toString() {
/*  466 */     return "Deity [Name: " + this.name + ", Number: " + this.number + ']';
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isLibila() {
/*  471 */     return (this.number == 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isMagranon() {
/*  476 */     return (this.number == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCustomDeity() {
/*  481 */     return (this.number > 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isFo() {
/*  486 */     return (this.number == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isVynora() {
/*  491 */     return (this.number == 3);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setActiveFollowers(int followers) {
/*  496 */     this.activeFollowers = followers;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getActiveFollowers() {
/*  501 */     return this.activeFollowers;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getFaithPerFollower() {
/*  506 */     return this.faith / Math.max(1.0F, this.activeFollowers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumber() {
/*  516 */     return this.number;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  526 */     return this.name;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getAlignment() {
/*  536 */     return this.alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte getSex() {
/*  546 */     return this.sex;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public abstract void setPower(byte paramByte);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   byte getPower() {
/*  558 */     return this.power;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   double getFaith() {
/*  568 */     return this.faith;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   float getAttack() {
/*  578 */     return this.attack;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   float getVitality() {
/*  588 */     return this.vitality;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getHolyItem() {
/*  598 */     return this.holyItem;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getConvertText1() {
/*  608 */     return this.convertText1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String[] getAltarConvertText1() {
/*  618 */     return this.altarConvertText1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isRoadProtector() {
/*  628 */     return this.roadProtector;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRoadProtector(boolean roadProtector) {
/*  633 */     this.roadProtector = roadProtector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public float getBuildWallBonus() {
/*  643 */     return this.buildWallBonus;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBuildWallBonus(float buildWallBonus) {
/*  648 */     this.buildWallBonus = buildWallBonus;
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
/*      */   public boolean isWarrior() {
/*  755 */     return this.warrior;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWarrior(boolean warrior) {
/*  760 */     this.warrior = warrior;
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
/*      */   public boolean isFavorRegenerator() {
/*  772 */     return this.favorRegenerator;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFavorRegenerator(boolean favorRegenerator) {
/*  777 */     this.favorRegenerator = favorRegenerator;
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
/*      */   public boolean isBefriendCreature() {
/*  804 */     return this.befriendCreature;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBefriendCreature(boolean befriendCreature) {
/*  809 */     this.befriendCreature = befriendCreature;
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
/*      */   public boolean isBefriendMonster() {
/*  821 */     return this.befriendMonster;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBefriendMonster(boolean befriendMonster) {
/*  826 */     this.befriendMonster = befriendMonster;
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
/*      */   public boolean isStaminaBonus() {
/*  838 */     return this.staminaBonus;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setStaminaBonus(boolean staminaBonus) {
/*  843 */     this.staminaBonus = staminaBonus;
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
/*      */   public boolean isFoodBonus() {
/*  855 */     return this.foodBonus;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFoodBonus(boolean foodBonus) {
/*  860 */     this.foodBonus = foodBonus;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isHealer() {
/*  870 */     return this.healer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHealer(boolean healer) {
/*  875 */     this.healer = healer;
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
/*      */   public boolean isDeathProtector() {
/*  887 */     return this.deathProtector;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDeathProtector(boolean deathProtector) {
/*  892 */     this.deathProtector = deathProtector;
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
/*      */   public boolean isDeathItemProtector() {
/*  904 */     return this.deathItemProtector;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setDeathItemProtector(boolean deathItemProtector) {
/*  909 */     this.deathItemProtector = deathItemProtector;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAllowsButchering() {
/*  919 */     return this.allowsButchering;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAllowsButchering(boolean allowsButchering) {
/*  924 */     this.allowsButchering = allowsButchering;
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
/*      */   public boolean isWoodAffinity() {
/*  936 */     return this.woodAffinity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWoodAffinity(boolean woodAffinity) {
/*  941 */     this.woodAffinity = woodAffinity;
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
/*      */   public boolean isMetalAffinity() {
/*  953 */     return this.metalAffinity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMetalAffinity(boolean metalAffinity) {
/*  958 */     this.metalAffinity = metalAffinity;
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
/*      */   public boolean isClothAffinity() {
/*  970 */     return this.clothAffinity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClothAffinity(boolean clothAffinity) {
/*  975 */     this.clothAffinity = clothAffinity;
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
/*      */   public boolean isClayAffinity() {
/*  987 */     return this.clayAffinity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClayAffinity(boolean clayAffinity) {
/*  992 */     this.clayAffinity = clayAffinity;
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
/*      */   public boolean isMeatAffinity() {
/* 1019 */     return this.meatAffinity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMeatAffinity(boolean meatAffinity) {
/* 1024 */     this.meatAffinity = meatAffinity;
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
/*      */   public boolean isFoodAffinity() {
/* 1036 */     return this.foodAffinity;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setFoodAffinity(boolean foodAffinity) {
/* 1041 */     this.foodAffinity = foodAffinity;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isLearner() {
/* 1051 */     return this.learner;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setLearner(boolean learner) {
/* 1056 */     this.learner = learner;
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
/*      */   public boolean isItemProtector() {
/* 1068 */     return this.itemProtector;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemProtector(boolean itemProtector) {
/* 1073 */     this.itemProtector = itemProtector;
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
/*      */   public boolean isRepairer() {
/* 1085 */     return this.repairer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRepairer(boolean repairer) {
/* 1090 */     this.repairer = repairer;
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
/*      */   public boolean isWaterGod() {
/* 1116 */     return this.waterGod;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setWaterGod(boolean waterGod) {
/* 1121 */     this.waterGod = waterGod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isMountainGod() {
/* 1131 */     return this.mountainGod;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setMountainGod(boolean mountainGod) {
/* 1136 */     this.mountainGod = mountainGod;
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
/*      */   public boolean isForestGod() {
/* 1151 */     return this.forestGod;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setForestGod(boolean forestGod) {
/* 1156 */     this.forestGod = forestGod;
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
/*      */   public boolean isHateGod() {
/* 1170 */     return this.hateGod;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHateGod(boolean hateGod) {
/* 1175 */     this.hateGod = hateGod;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLastConfrontationTileX() {
/* 1185 */     return this.lastConfrontationTileX;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getLastConfrontationTileY() {
/* 1195 */     return this.lastConfrontationTileY;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void clearKarma() {
/* 1200 */     this.karmavals.clear();
/*      */   }
/*      */ 
/*      */   
/*      */   public final ConcurrentHashMap<Long, Float> getHelpers() {
/* 1205 */     return this.karmavals;
/*      */   }
/*      */ 
/*      */   
/*      */   public final long getBestHelper(boolean wipeKarma) {
/* 1210 */     int totalTickets = 0;
/* 1211 */     for (Iterator<Float> iterator = this.karmavals.values().iterator(); iterator.hasNext(); ) { float i = ((Float)iterator.next()).floatValue();
/* 1212 */       if (i >= 300.0F)
/* 1213 */         totalTickets += (int)(i / 300.0F);  }
/*      */     
/* 1215 */     int currentTicket = 0;
/* 1216 */     long[] tickets = new long[totalTickets];
/* 1217 */     for (Map.Entry<Long, Float> entry : this.karmavals.entrySet()) {
/*      */       
/* 1219 */       if (((Float)entry.getValue()).floatValue() >= 300.0F) {
/*      */         
/* 1221 */         int totalNum = (int)(((Float)entry.getValue()).floatValue() / 300.0F);
/* 1222 */         for (int i = 0; i < totalNum; i++) {
/* 1223 */           tickets[currentTicket++] = ((Long)entry.getKey()).longValue();
/*      */         }
/*      */       } 
/*      */     } 
/* 1227 */     int winningTicket = Server.rand.nextInt((totalTickets < 1) ? 1 : totalTickets);
/* 1228 */     if (winningTicket < tickets.length) {
/*      */       
/* 1230 */       if (wipeKarma) {
/* 1231 */         this.karmavals.replace(Long.valueOf(tickets[winningTicket]), Float.valueOf(0.0F));
/*      */       }
/* 1233 */       return tickets[winningTicket];
/*      */     } 
/*      */     
/* 1236 */     return -10L;
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
/*      */   public final void loadAllKarmaHelpers() {
/* 1264 */     Connection dbcon = null;
/* 1265 */     PreparedStatement ps = null;
/* 1266 */     ResultSet rs = null;
/*      */     
/*      */     try {
/* 1269 */       dbcon = DbConnector.getDeityDbCon();
/* 1270 */       ps = dbcon.prepareStatement("SELECT * FROM HELPERS WHERE DEITY=?");
/* 1271 */       ps.setInt(1, this.number);
/* 1272 */       rs = ps.executeQuery();
/* 1273 */       while (rs.next())
/*      */       {
/* 1275 */         this.karmavals.put(Long.valueOf(rs.getLong("WURMID")), Float.valueOf(rs.getInt("KARMA")));
/*      */       }
/*      */       
/* 1278 */       ps.close();
/*      */     }
/* 1280 */     catch (SQLException sqx) {
/*      */       
/* 1282 */       logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */     }
/*      */     finally {
/*      */       
/* 1286 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 1287 */       DbConnector.returnConnection(dbcon);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setPlayerKarma(long pid, int value) {
/* 1293 */     Long playerId = Long.valueOf(pid);
/* 1294 */     Float karmaValue = Float.valueOf(value);
/* 1295 */     Connection dbcon = null;
/* 1296 */     PreparedStatement ps = null;
/* 1297 */     if (this.karmavals.keySet().contains(playerId)) {
/*      */       
/* 1299 */       this.karmavals.put(playerId, karmaValue);
/*      */       
/*      */       try {
/* 1302 */         dbcon = DbConnector.getDeityDbCon();
/* 1303 */         ps = dbcon.prepareStatement("UPDATE HELPERS SET KARMA=?,DEITY=? WHERE WURMID=?");
/* 1304 */         ps.setInt(1, value);
/* 1305 */         ps.setInt(2, this.number);
/* 1306 */         ps.setLong(3, pid);
/* 1307 */         ps.executeUpdate();
/* 1308 */         ps.close();
/*      */       }
/* 1310 */       catch (SQLException sqx) {
/*      */         
/* 1312 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1316 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1317 */         DbConnector.returnConnection(dbcon);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1322 */       this.karmavals.put(playerId, karmaValue);
/*      */       
/*      */       try {
/* 1325 */         dbcon = DbConnector.getDeityDbCon();
/* 1326 */         ps = dbcon.prepareStatement("INSERT INTO HELPERS (WURMID,KARMA,DEITY) VALUES (?,?,?)");
/* 1327 */         ps.setLong(1, pid);
/* 1328 */         ps.setInt(2, value);
/* 1329 */         ps.setInt(3, this.number);
/* 1330 */         ps.executeUpdate();
/* 1331 */         ps.close();
/*      */       }
/* 1333 */       catch (SQLException sqx) {
/*      */         
/* 1335 */         logger.log(Level.WARNING, sqx.getMessage(), sqx);
/*      */       }
/*      */       finally {
/*      */         
/* 1339 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 1340 */         DbConnector.returnConnection(dbcon);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Random initializeAndGetRand() {
/* 1347 */     this.rand.setSeed((this.number * 1001));
/* 1348 */     return this.rand;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Random getRand() {
/* 1358 */     return this.rand;
/*      */   }
/*      */ 
/*      */   
/*      */   public final void setMaxKingdom() {
/* 1363 */     Player[] players = Players.getInstance().getPlayers();
/* 1364 */     if (players.length > 10 || Servers.localServer.testServer) {
/*      */       
/* 1366 */       Map<Byte, Integer> maxWorshippers = new HashMap<>();
/* 1367 */       for (Player lPlayer : players) {
/*      */         
/* 1369 */         if (lPlayer.isPaying() && lPlayer.getDeity() != null && (lPlayer.getDeity()).number == getNumber()) {
/*      */           
/* 1371 */           Integer curr = maxWorshippers.get(Byte.valueOf(lPlayer.getKingdomId()));
/* 1372 */           if (curr == null) {
/*      */             
/* 1374 */             curr = Integer.valueOf(1);
/*      */           } else {
/*      */             
/* 1377 */             curr = Integer.valueOf(curr.intValue() + 1);
/* 1378 */           }  maxWorshippers.put(Byte.valueOf(lPlayer.getKingdomId()), curr);
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1383 */       byte maxKingdom = 0;
/* 1384 */       int maxNums = 0;
/* 1385 */       for (Map.Entry<Byte, Integer> me : maxWorshippers.entrySet()) {
/*      */         
/* 1387 */         int nums = ((Integer)me.getValue()).intValue();
/* 1388 */         if (nums > maxNums) {
/*      */           
/* 1390 */           maxNums = nums;
/* 1391 */           maxKingdom = ((Byte)me.getKey()).byteValue();
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1396 */       Kingdom k = Kingdoms.getKingdom(maxKingdom);
/* 1397 */       if (k != null)
/* 1398 */         setFavoredKingdom(k.getTemplate()); 
/* 1399 */       setFavoredKingdom(maxKingdom);
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
/*      */   public byte getFavoredKingdom() {
/* 1411 */     return this.favoredKingdom;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFavoredKingdom(byte fKingdom) {
/* 1422 */     if (fKingdom != this.favoredKingdom) {
/*      */       
/* 1424 */       EpicMission mission = EpicServerStatus.getEpicMissionForEntity(getNumber());
/* 1425 */       if (mission != null) {
/* 1426 */         Players.getInstance().sendUpdateEpicMission(mission);
/*      */       }
/*      */     } 
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
/* 1445 */     this.favoredKingdom = fKingdom;
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\deities\Deity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */