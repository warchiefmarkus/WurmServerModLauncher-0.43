/*      */ package com.wurmonline.server.behaviours;
/*      */ 
/*      */ import com.wurmonline.mesh.Tiles;
/*      */ import com.wurmonline.server.MiscConstants;
/*      */ import com.wurmonline.server.NoSuchPlayerException;
/*      */ import com.wurmonline.server.Server;
/*      */ import com.wurmonline.server.Servers;
/*      */ import com.wurmonline.server.bodys.Wound;
/*      */ import com.wurmonline.server.combat.CombatEngine;
/*      */ import com.wurmonline.server.creatures.Creature;
/*      */ import com.wurmonline.server.creatures.NoSuchCreatureException;
/*      */ import com.wurmonline.server.items.Item;
/*      */ import com.wurmonline.server.items.ItemTypes;
/*      */ import com.wurmonline.server.items.NotOwnedException;
/*      */ import com.wurmonline.server.structures.Blocking;
/*      */ import com.wurmonline.server.structures.BridgePart;
/*      */ import com.wurmonline.server.structures.Fence;
/*      */ import com.wurmonline.server.structures.Floor;
/*      */ import com.wurmonline.server.structures.NoSuchStructureException;
/*      */ import com.wurmonline.server.structures.Structure;
/*      */ import com.wurmonline.server.structures.Structures;
/*      */ import com.wurmonline.server.structures.Wall;
/*      */ import com.wurmonline.server.utils.StringUtil;
/*      */ import com.wurmonline.shared.constants.SoundNames;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.annotation.Nullable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Emotes
/*      */   implements ItemTypes, ActionTypes, SoundNames, MiscConstants
/*      */ {
/*   61 */   private static List<ActionEntry> defaultNiceEmotes = new LinkedList<>();
/*      */ 
/*      */ 
/*      */   
/*   65 */   private static List<ActionEntry> defaultNeutralEmotes = new LinkedList<>();
/*      */ 
/*      */ 
/*      */   
/*   69 */   private static List<ActionEntry> defaultOffensiveEmotes = new LinkedList<>();
/*   70 */   private static List<ActionEntry> allEmotes = new LinkedList<>();
/*      */   
/*   72 */   private static final Logger logger = Logger.getLogger(Emotes.class.getName());
/*      */   
/*      */   public static final short EMOTES = 2000;
/*      */   
/*      */   public static final short SMILE = 2000;
/*      */   
/*      */   public static final short CHUCKLE = 2001;
/*      */   
/*      */   public static final short APPLAUD = 2002;
/*      */   
/*      */   public static final short HUG = 2003;
/*      */   
/*      */   public static final short KISS = 2004;
/*      */   
/*      */   public static final short GROVEL = 2005;
/*      */   
/*      */   public static final short WORSHIP = 2006;
/*      */   
/*      */   public static final short COMFORT = 2007;
/*      */   
/*      */   public static final short DANCE = 2008;
/*      */   
/*      */   public static final short FLIRT = 2009;
/*      */   
/*      */   public static final short BOW = 2010;
/*      */   
/*      */   public static final short HKISS = 2011;
/*      */   
/*      */   public static final short TICKLE = 2012;
/*      */   
/*      */   public static final short WAVE = 2013;
/*      */   
/*      */   public static final short CALL = 2014;
/*      */   
/*      */   public static final short POKE = 2015;
/*      */   
/*      */   public static final short EYEROLL = 2016;
/*      */   public static final short DISBELIEVE = 2017;
/*      */   public static final short WORRY = 2018;
/*      */   public static final short DISAGREE = 2019;
/*      */   public static final short TEASE = 2020;
/*      */   public static final short LAUGH = 2021;
/*      */   public static final short CRY = 2022;
/*      */   public static final short POINT = 2023;
/*      */   public static final short FOLLOW = 2030;
/*      */   public static final short GOODBYE = 2031;
/*      */   public static final short LEAD = 2032;
/*      */   public static final short THAT_WAY = 2033;
/*      */   public static final short WRONG_WAY = 2034;
/*      */   public static final short SPIT = 2024;
/*      */   public static final short FART = 2025;
/*      */   public static final short INSULT = 2026;
/*      */   public static final short PUSH = 2027;
/*      */   public static final short CURSE = 2028;
/*      */   public static final short SLAP = 2029;
/*  127 */   public static final ActionEntry[] emoteEntrys = new ActionEntry[] { new ActionEntry((short)2000, "Smile", "smiling", new int[] { 0 }, 20), new ActionEntry((short)2001, "Chuckle", "chuckling", new int[] { 0 }, 20), new ActionEntry((short)2002, "Applaud", "applauding", new int[] { 0 }, 20), new ActionEntry((short)2003, "Hug", "hugging", new int[] { 0 }), new ActionEntry((short)2004, "Kiss", "kissing", new int[] { 0 }), new ActionEntry((short)2005, "Grovel", "grovelling", new int[] { 0 }, 20), new ActionEntry((short)2006, "Worship", "worshipping", new int[] { 0 }, 20), new ActionEntry((short)2007, "Comfort", "comforting", new int[] { 0 }, 20), new ActionEntry((short)2008, "Dance", "dancing", new int[] { 0 }, 20), new ActionEntry((short)2009, "Flirt", "flirting", new int[] { 0 }, 20), new ActionEntry((short)2010, "Bow", "bowing", new int[] { 0 }, 20), new ActionEntry((short)2011, "Kiss hand", "kissing", new int[] { 0 }), new ActionEntry((short)2012, "Tickle", "tickling", new int[] { 0 }), new ActionEntry((short)2013, "Wave", "waving", new int[] { 0 }, 200), new ActionEntry((short)2014, "Call", "calling", new int[] { 0 }, 200), new ActionEntry((short)2015, "Poke", "poking", new int[] { 0 }), new ActionEntry((short)2016, "Roll with the eyes", "rolling with the eyes", new int[] { 0 }, 20), new ActionEntry((short)2017, "Disbelieve", "disbelieving", new int[] { 0 }, 20), new ActionEntry((short)2018, "Worry", "worrying", new int[] { 0 }, 20), new ActionEntry((short)2019, "Disagree", "disagreeing", new int[] { 0 }, 20), new ActionEntry((short)2020, "Tease", "teasing", new int[] { 0 }, 20), new ActionEntry((short)2021, "Laugh", "laughing", new int[] { 0 }, 20), new ActionEntry((short)2022, "Cry", "crying", new int[] { 0 }, 20), new ActionEntry((short)2023, "Point", "pointing", new int[] { 0 }, 100), new ActionEntry((short)2024, "Spit", "spitting", new int[] { 0 }, 10), new ActionEntry((short)2025, "Fart", "farting", new int[] { 0 }, 20), new ActionEntry((short)2026, "Insult", "insulting", new int[] { 0 }, 40), new ActionEntry((short)2027, "Push", "pushing", new int[] { 0 }), new ActionEntry((short)2028, "Curse", "cursing", new int[] { 0 }, 20), new ActionEntry((short)2029, "Slap", "slapping", new int[] { 0 }), new ActionEntry((short)2030, "Follow", "following", new int[] { 0 }, 20), new ActionEntry((short)2031, "Goodbye", "saying goodbye", new int[] { 0 }, 200), new ActionEntry((short)2032, "Lead", "leading", new int[] { 0 }, 20), new ActionEntry((short)2033, "That way", "that way", new int[] { 0 }, 20), new ActionEntry((short)2034, "Wrong way", "wrong way", new int[] { 0 }, 20) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  176 */     createDefaultNiceEmotes();
/*  177 */     createDefaultNeutralEmotes();
/*  178 */     createDefaultOffensiveEmotes();
/*  179 */     createEmoteList();
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createDefaultNiceEmotes() {
/*  184 */     defaultNiceEmotes.add(emoteEntrys[0]);
/*  185 */     defaultNiceEmotes.add(emoteEntrys[1]);
/*  186 */     defaultNiceEmotes.add(emoteEntrys[2]);
/*  187 */     defaultNiceEmotes.add(emoteEntrys[3]);
/*  188 */     defaultNiceEmotes.add(emoteEntrys[4]);
/*  189 */     defaultNiceEmotes.add(emoteEntrys[5]);
/*  190 */     defaultNiceEmotes.add(emoteEntrys[6]);
/*  191 */     defaultNiceEmotes.add(emoteEntrys[7]);
/*  192 */     defaultNiceEmotes.add(emoteEntrys[8]);
/*  193 */     defaultNiceEmotes.add(emoteEntrys[9]);
/*  194 */     defaultNiceEmotes.add(emoteEntrys[10]);
/*  195 */     defaultNiceEmotes.add(emoteEntrys[11]);
/*  196 */     defaultNiceEmotes.add(emoteEntrys[12]);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createDefaultNeutralEmotes() {
/*  201 */     defaultNeutralEmotes.add(emoteEntrys[13]);
/*  202 */     defaultNeutralEmotes.add(emoteEntrys[14]);
/*  203 */     defaultNeutralEmotes.add(emoteEntrys[15]);
/*  204 */     defaultNeutralEmotes.add(emoteEntrys[16]);
/*  205 */     defaultNeutralEmotes.add(emoteEntrys[17]);
/*  206 */     defaultNeutralEmotes.add(emoteEntrys[18]);
/*  207 */     defaultNeutralEmotes.add(emoteEntrys[19]);
/*  208 */     defaultNeutralEmotes.add(emoteEntrys[20]);
/*  209 */     defaultNeutralEmotes.add(emoteEntrys[21]);
/*  210 */     defaultNeutralEmotes.add(emoteEntrys[22]);
/*  211 */     defaultNeutralEmotes.add(emoteEntrys[23]);
/*  212 */     defaultNeutralEmotes.add(emoteEntrys[30]);
/*  213 */     defaultNeutralEmotes.add(emoteEntrys[31]);
/*  214 */     defaultNeutralEmotes.add(emoteEntrys[32]);
/*  215 */     defaultNeutralEmotes.add(emoteEntrys[33]);
/*  216 */     defaultNeutralEmotes.add(emoteEntrys[34]);
/*      */   }
/*      */ 
/*      */   
/*      */   private static void createDefaultOffensiveEmotes() {
/*  221 */     defaultOffensiveEmotes.add(emoteEntrys[24]);
/*  222 */     defaultOffensiveEmotes.add(emoteEntrys[25]);
/*  223 */     defaultOffensiveEmotes.add(emoteEntrys[26]);
/*  224 */     defaultOffensiveEmotes.add(emoteEntrys[27]);
/*  225 */     defaultOffensiveEmotes.add(emoteEntrys[28]);
/*  226 */     defaultOffensiveEmotes.add(emoteEntrys[29]);
/*      */   }
/*      */ 
/*      */   
/*      */   static List<ActionEntry> getEmoteList() {
/*  231 */     return allEmotes;
/*      */   }
/*      */ 
/*      */   
/*      */   private static final void createEmoteList() {
/*  236 */     allEmotes.add(new ActionEntry((short)-3, "Emotes", "emoting", new int[] { 0 }));
/*      */     
/*  238 */     List<ActionEntry> niceEmotes = getDefaultNiceEmotes();
/*  239 */     List<ActionEntry> neutralEmotes = getDefaultNeutralEmotes();
/*  240 */     List<ActionEntry> offensiveEmotes = getDefaultOffensiveEmotes();
/*  241 */     allEmotes.add(new ActionEntry((short)-niceEmotes.size(), "Nice", "emoting nice", new int[] { 0 }));
/*      */     
/*  243 */     for (ActionEntry entry : niceEmotes)
/*      */     {
/*  245 */       allEmotes.add(entry);
/*      */     }
/*      */     
/*  248 */     allEmotes.add(new ActionEntry((short)-neutralEmotes.size(), "Neutral", "emoting neutral", new int[] { 0 }));
/*      */     
/*  250 */     for (ActionEntry entry : neutralEmotes)
/*      */     {
/*  252 */       allEmotes.add(entry);
/*      */     }
/*      */     
/*  255 */     allEmotes.add(new ActionEntry((short)-offensiveEmotes.size(), "Offensive", "emoting offensive", new int[] { 0 }));
/*      */     
/*  257 */     for (ActionEntry entry : offensiveEmotes)
/*      */     {
/*  259 */       allEmotes.add(entry);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static List<ActionEntry> getDefaultNiceEmotes() {
/*  269 */     return defaultNiceEmotes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static List<ActionEntry> getDefaultNeutralEmotes() {
/*  278 */     return defaultNeutralEmotes;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static List<ActionEntry> getDefaultOffensiveEmotes() {
/*  287 */     return defaultOffensiveEmotes;
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
/*      */   static void emoteAt(short emote, Creature performer, Creature receiver) {
/*  308 */     String emoteGender = ".male";
/*  309 */     if (performer.getSex() == 1)
/*  310 */       emoteGender = ".female"; 
/*  311 */     String emoteSound = "";
/*  312 */     String performerString = "";
/*  313 */     String receiverString = "";
/*  314 */     String bcastString = "";
/*  315 */     String pname = performer.getNameWithGenus();
/*  316 */     String rname = receiver.getNameWithGenus();
/*  317 */     if (emote == 2000) {
/*      */       
/*  319 */       performerString = "You smile at " + rname + ".";
/*  320 */       receiverString = pname + " smiles at you.";
/*  321 */       bcastString = pname + " smiles at " + rname + ".";
/*  322 */       if (receiver.isPlayer()) {
/*  323 */         performer.achievement(176);
/*      */       }
/*  325 */     } else if (emote == 2001) {
/*      */       
/*  327 */       emoteSound = "sound.emote.chuckle";
/*  328 */       performerString = "You chuckle happily as you think of " + rname + ".";
/*  329 */       receiverString = pname + " looks at you and chuckles happily.";
/*  330 */       bcastString = pname + " looks at " + rname + " and chuckles happily.";
/*      */     }
/*  332 */     else if (emote == 2002) {
/*      */       
/*  334 */       emoteSound = "sound.emote.applaud";
/*  335 */       performerString = "You applaud " + rname + " for " + receiver.getHisHerItsString() + " efforts.";
/*  336 */       receiverString = pname + " gives you a round of applause.";
/*  337 */       bcastString = pname + " gives " + rname + " a round of applause.";
/*      */     }
/*  339 */     else if (emote == 2003) {
/*      */       
/*  341 */       if (receiver.isGhost()) {
/*      */         
/*  343 */         performerString = "You try to hug " + rname + " but the air is too confusing.";
/*  344 */         receiverString = pname + " tries hugs you.";
/*  345 */         bcastString = pname + " awkwardly hugs " + rname + " mid-air.";
/*      */       }
/*      */       else {
/*      */         
/*  349 */         performerString = "You hug " + rname + ".";
/*  350 */         receiverString = pname + " hugs you.";
/*  351 */         bcastString = pname + " hugs " + rname + ".";
/*  352 */         if (receiver.isPlayer()) {
/*  353 */           performer.achievement(175);
/*      */         }
/*      */       } 
/*  356 */     } else if (emote == 2030) {
/*      */       
/*  358 */       emoteSound = "sound.emote.follow";
/*  359 */       performerString = StringUtil.format("You tell %s to follow.", new Object[] { rname });
/*  360 */       receiverString = StringUtil.format("%s tells you to follow.", new Object[] { pname });
/*  361 */       bcastString = StringUtil.format("%s tells %s to follow %s.", new Object[] { pname, rname, performer.getHimHerItString() });
/*      */     }
/*  363 */     else if (emote == 2031) {
/*      */       
/*  365 */       emoteSound = "sound.emote.goodbye";
/*  366 */       performerString = StringUtil.format("You say goodbye to %s.", new Object[] { rname });
/*  367 */       receiverString = StringUtil.format("%s says goodbye to you.", new Object[] { pname });
/*  368 */       bcastString = StringUtil.format("%s says goodbye to %s.", new Object[] { pname, rname });
/*      */     }
/*  370 */     else if (emote == 2032) {
/*      */       
/*  372 */       emoteSound = "sound.emote.lead";
/*  373 */       performerString = StringUtil.format("You ask %s to lead the way.", new Object[] { rname });
/*  374 */       receiverString = StringUtil.format("%s asks you to lead the way.", new Object[] { pname });
/*  375 */       bcastString = StringUtil.format("%s asks %s to lead the way.", new Object[] { pname, rname });
/*      */     }
/*  377 */     else if (emote == 2033) {
/*      */       
/*  379 */       emoteSound = "sound.emote.that.way";
/*  380 */       performerString = StringUtil.format("You tell %s to go that way.", new Object[] { rname });
/*  381 */       receiverString = StringUtil.format("%s tells you to go that way.", new Object[] { pname });
/*  382 */       bcastString = StringUtil.format("%s tells %s to go that way.", new Object[] { pname, rname });
/*      */     }
/*  384 */     else if (emote == 2034) {
/*      */       
/*  386 */       emoteSound = "sound.emote.wrong.way";
/*  387 */       performerString = StringUtil.format("You tell %s to not go that way.", new Object[] { rname });
/*  388 */       receiverString = StringUtil.format("%s tells you to not go that way.", new Object[] { pname });
/*  389 */       bcastString = StringUtil.format("%s tells %s to not go that way.", new Object[] { pname, rname });
/*      */     }
/*  391 */     else if (emote == 2004) {
/*      */       
/*  393 */       emoteSound = "sound.emote.kiss";
/*  394 */       if (receiver.isGhost())
/*      */       {
/*  396 */         performerString = "You try to kiss " + rname + " but the air leaves no mark.";
/*  397 */         receiverString = pname + " tries kiss you.";
/*  398 */         bcastString = pname + " blows a kiss towards " + rname + ".";
/*      */       }
/*      */       else
/*      */       {
/*  402 */         performerString = "You kiss " + rname + ".";
/*  403 */         receiverString = pname + " kisses you.";
/*  404 */         bcastString = pname + " kisses " + rname + ".";
/*      */       }
/*      */     
/*  407 */     } else if (emote == 2005) {
/*      */       
/*  409 */       performerString = "You grovel in the dirt before " + rname + ".";
/*  410 */       receiverString = pname + " grovels in the dirt before you.";
/*  411 */       bcastString = pname + " grovels in the dirt before " + rname + ".";
/*      */     }
/*  413 */     else if (emote == 2006) {
/*      */       
/*  415 */       performerString = "You fall to your knees and worship " + rname + ".";
/*  416 */       receiverString = pname + " falls to " + performer.getHisHerItsString() + " knees and worships you.";
/*  417 */       bcastString = pname + " falls to the ground and worships " + rname + ".";
/*      */     }
/*  419 */     else if (emote == 2007) {
/*      */       
/*  421 */       if (receiver.isGhost())
/*      */       {
/*  423 */         performerString = "You try to pat " + rname + " but without physical contact it is pointless.";
/*  424 */         receiverString = pname + " tries comfort you.";
/*  425 */         bcastString = pname + " pretends to comfort " + rname + " from a distance.";
/*      */       }
/*      */       else
/*      */       {
/*  429 */         performerString = "You gently comfort " + rname + ".";
/*  430 */         receiverString = pname + " gently comforts you.";
/*  431 */         bcastString = pname + " gently comforts " + rname + ".";
/*      */       }
/*      */     
/*  434 */     } else if (emote == 2008) {
/*      */       
/*  436 */       performerString = "You dance and frolic with " + rname + ".";
/*  437 */       receiverString = pname + " joyfully dances around with you.";
/*  438 */       bcastString = pname + " dances around with " + rname + ".";
/*  439 */       if (receiver.isPlayer()) {
/*  440 */         performer.achievement(181);
/*      */       }
/*  442 */     } else if (emote == 2009) {
/*      */       
/*  444 */       performerString = "You flirtilly wink at " + rname + ".";
/*  445 */       receiverString = pname + " winks invitingly to you.";
/*  446 */       bcastString = pname + " seems to have something in the eye.";
/*  447 */       if (receiver.isPlayer()) {
/*  448 */         performer.achievement(179);
/*      */       }
/*  450 */     } else if (emote == 2010) {
/*      */       
/*  452 */       performerString = "You bow before " + rname + ".";
/*  453 */       receiverString = pname + " bows before you.";
/*  454 */       bcastString = pname + " bows before " + rname + ".";
/*      */     }
/*  456 */     else if (emote == 2011) {
/*      */       
/*  458 */       if (receiver.isGhost())
/*      */       {
/*      */         
/*  461 */         performerString = "You bend down and gently pretend to kiss " + rname + "'s hand. " + receiver.getHeSheItString() + " is not impressed.";
/*  462 */         receiverString = pname + " gently bends down and kisses your hand.";
/*  463 */         bcastString = pname + " gently bends down and pretends to kiss " + rname + "'s hand.";
/*      */       }
/*      */       else
/*      */       {
/*  467 */         performerString = "You bend down and gently kiss " + rname + "'s hand.";
/*  468 */         receiverString = pname + " gently bends down and kisses your hand.";
/*  469 */         bcastString = pname + " gently bends down and kisses " + rname + "'s hand.";
/*      */       }
/*      */     
/*  472 */     } else if (emote == 2012) {
/*      */       
/*  474 */       if (receiver.isGhost())
/*      */       {
/*  476 */         performerString = "You try to tickle " + rname + " but only encounter air.";
/*  477 */         receiverString = pname + " tickles you! Tee-hee! Not.";
/*  478 */         bcastString = pname + " tries to tickle the air around " + rname + " playfully  but.. well.";
/*      */       }
/*      */       else
/*      */       {
/*  482 */         performerString = "You tickle " + rname + ".";
/*  483 */         receiverString = pname + " tickles you! Tee-hee!";
/*  484 */         bcastString = pname + " tickles " + rname + " playfully.";
/*      */       }
/*      */     
/*  487 */     } else if (emote == 2013) {
/*      */       
/*  489 */       emoteSound = "sound.emote.wave";
/*  490 */       performerString = "You wave at " + rname + ".";
/*  491 */       receiverString = pname + " waves at you.";
/*  492 */       bcastString = pname + " waves at " + rname + ".";
/*  493 */       if (receiver.isHorse()) {
/*  494 */         performer.achievement(174);
/*      */       }
/*  496 */     } else if (emote == 2014) {
/*      */       
/*  498 */       emoteSound = "sound.emote.call";
/*  499 */       performerString = "You call out to " + rname + " for " + receiver.getHisHerItsString() + " attention.";
/*  500 */       receiverString = "You hear " + pname + " call out for your attention.";
/*  501 */       bcastString = pname + " calls out for " + rname + "'s attention.";
/*      */     }
/*  503 */     else if (emote == 2021) {
/*      */       
/*  505 */       emoteSound = "sound.emote.laugh";
/*  506 */       performerString = "You laugh hysterically at " + rname + ".";
/*  507 */       receiverString = pname + " laughs hysterically and seems to think you are really funny.";
/*  508 */       bcastString = pname + " laughs hysterically at " + rname + ".";
/*      */     }
/*  510 */     else if (emote == 2022) {
/*      */       
/*  512 */       if (receiver.isGhost())
/*      */       {
/*  514 */         emoteSound = "sound.emote.cry";
/*  515 */         performerString = "You pretend to cry on " + rname + "'s shoulder.";
/*  516 */         receiverString = pname + " pretends to cry on your shoulder.";
/*  517 */         bcastString = pname + " pretends to cry on " + rname + "'s shoulder.";
/*      */       }
/*      */       else
/*      */       {
/*  521 */         emoteSound = "sound.emote.cry";
/*  522 */         performerString = "You cry on " + rname + "'s shoulder.";
/*  523 */         receiverString = pname + " cries on your shoulder.";
/*  524 */         bcastString = pname + " cries on " + rname + "'s shoulder.";
/*      */       }
/*      */     
/*  527 */     } else if (emote == 2023) {
/*      */       
/*  529 */       performerString = "You point at " + rname + ".";
/*  530 */       receiverString = pname + " points at you.";
/*  531 */       bcastString = pname + " points at " + rname + ".";
/*      */     }
/*  533 */     else if (emote == 2015) {
/*      */       
/*  535 */       performerString = "You poke " + rname + " in the ribs.";
/*  536 */       receiverString = pname + " pokes you in the ribs.";
/*  537 */       bcastString = pname + " pokes " + rname + " in the ribs.";
/*      */     }
/*  539 */     else if (emote == 2016) {
/*      */       
/*  541 */       performerString = "You roll your eyes at " + rname + ".";
/*  542 */       receiverString = pname + " rolls " + performer.getHisHerItsString() + " eyes in your direction.";
/*  543 */       bcastString = pname + " rolls " + performer.getHisHerItsString() + " eyes at " + rname + ".";
/*      */     }
/*  545 */     else if (emote == 2017) {
/*      */       
/*  547 */       performerString = "You stare at " + rname + " sceptically.";
/*  548 */       receiverString = pname + " stares at you sceptically.";
/*  549 */       bcastString = pname + " stares at " + rname + " sceptically.";
/*      */     }
/*  551 */     else if (emote == 2018) {
/*      */       
/*  553 */       emoteSound = "sound.emote.worry";
/*  554 */       performerString = "You sigh loudly and wonder what will happen to " + rname + ".";
/*  555 */       receiverString = pname + " lets out a worrying sigh in your direction.";
/*  556 */       bcastString = pname + " lets out a loud sigh, obviously worrying about " + rname + ".";
/*      */     }
/*  558 */     else if (emote == 2019) {
/*      */       
/*  560 */       emoteSound = "sound.emote.disagree";
/*  561 */       performerString = "You roll your eyes and shake your head in disagreement.";
/*      */       
/*  563 */       receiverString = pname + " shakes " + performer.getHisHerItsString() + " head and rolls " + performer.getHisHerItsString() + " eyes in disagreement.";
/*      */       
/*  565 */       bcastString = pname + " shakes " + performer.getHisHerItsString() + " head and rolls " + performer.getHisHerItsString() + " eyes in disagreement.";
/*      */     }
/*  567 */     else if (emote == 2020) {
/*      */       
/*  569 */       emoteSound = "sound.emote.tease";
/*  570 */       performerString = "You tease " + rname + " for " + receiver.getHisHerItsString() + " hairstyle.";
/*  571 */       receiverString = pname + " teases you for your hairstyle.";
/*  572 */       bcastString = pname + " teases " + rname + " for " + receiver.getHisHerItsString() + " hairstyle.";
/*      */     }
/*  574 */     else if (emote == 2024) {
/*      */       
/*  576 */       emoteSound = "sound.emote.spit";
/*  577 */       if (receiver.isGhost())
/*      */       {
/*  579 */         performerString = "You spit at " + rname + "'s face but it passes through.";
/*  580 */         receiverString = pname + " spits in your face!";
/*  581 */         bcastString = pname + " spits through " + rname + "'s face!";
/*      */       }
/*      */       else
/*      */       {
/*  585 */         performerString = "You spit in " + rname + "'s face.";
/*  586 */         receiverString = pname + " spits in your face!";
/*  587 */         bcastString = pname + " spits in " + rname + "'s face!";
/*      */       }
/*      */     
/*  590 */     } else if (emote == 2025) {
/*      */       
/*  592 */       emoteSound = "sound.emote.fart";
/*  593 */       performerString = "You fart in " + rname + "'s direction.";
/*  594 */       receiverString = pname + " farts in your direction.";
/*  595 */       bcastString = pname + " farts in the general direction of " + rname + ".";
/*      */     }
/*  597 */     else if (emote == 2026) {
/*      */       
/*  599 */       emoteSound = "sound.emote.insult";
/*  600 */       performerString = "You call " + rname + " names.";
/*  601 */       receiverString = pname + " calls you different names.";
/*  602 */       bcastString = pname + " calls " + rname + " names.";
/*      */     
/*      */     }
/*  605 */     else if (emote == 2027) {
/*      */       
/*  607 */       if (receiver.isGhost())
/*      */       {
/*  609 */         performerString = "You try to push " + rname + " in the chest but nothing is there is it?";
/*  610 */         receiverString = pname + " tries to push you in the chest.";
/*  611 */         bcastString = pname + " tries to push " + rname + " in the chest but passes through. Eerie.";
/*      */       }
/*      */       else
/*      */       {
/*  615 */         performerString = "You push " + rname + " in the chest.";
/*  616 */         receiverString = pname + " pushes you in the chest.";
/*  617 */         bcastString = pname + " pushes " + rname + " in the chest.";
/*      */       }
/*      */     
/*  620 */     } else if (emote == 2028) {
/*      */       
/*  622 */       emoteSound = "sound.emote.curse";
/*  623 */       String hisher = receiver.getHisHerItsString();
/*  624 */       performerString = "You let out heavy curses involving " + rname + ", " + hisher + " family and " + hisher + " relatives.";
/*      */       
/*  626 */       receiverString = pname + " lets out a long tirade of curses involving you, your family and your relatives.";
/*  627 */       bcastString = pname + " lets out a long tirade of curses involving " + rname + ", " + hisher + " family and " + hisher + " relatives.";
/*      */ 
/*      */     
/*      */     }
/*  631 */     else if (emote == 2029) {
/*      */       
/*  633 */       if (receiver.isGhost()) {
/*      */         
/*  635 */         emoteSound = "sound.combat.miss.light";
/*  636 */         performerString = "You slap " + rname + " in the area of the face.";
/*  637 */         receiverString = pname + " slaps you around where your face used to be.";
/*  638 */         bcastString = pname + " slaps " + rname + " around where " + receiver.getHisHerItsString() + " face used to be.";
/*      */       
/*      */       }
/*      */       else {
/*      */         
/*  643 */         emoteSound = "sound.emote.slap";
/*  644 */         performerString = "You slap " + rname + " in the face.";
/*  645 */         receiverString = pname + " slaps you in the face.";
/*  646 */         bcastString = pname + " slaps " + rname + " in the face.";
/*      */       } 
/*      */     } 
/*  649 */     if (performer.mayEmote()) {
/*      */       
/*  651 */       if (emoteSound != null && emoteSound.length() > 0) {
/*      */         
/*  653 */         performer.makeEmoteSound();
/*  654 */         Methods.sendSound(performer, emoteSound + emoteGender);
/*      */       } 
/*  656 */       performer.playAnimation(emoteEntrys[emote - 2000].getActionString().toLowerCase(), false);
/*      */     } 
/*      */     
/*  659 */     Server.getInstance().broadCastAction(bcastString, performer, receiver, 5);
/*  660 */     performer.getCommunicator().sendNormalServerMessage(performerString);
/*  661 */     receiver.getCommunicator().sendNormalServerMessage(receiverString);
/*  662 */     if (receiver.hasTrait(10))
/*      */     {
/*  664 */       if (Server.rand.nextInt(6) == 0 || Servers.localServer.testServer) {
/*      */         
/*  666 */         performerString = rname + " looks angry!";
/*  667 */         bcastString = rname + " looks angry with " + pname + "!";
/*  668 */         Server.getInstance().broadCastAction(bcastString, performer, receiver, 5);
/*  669 */         performer.getCommunicator().sendNormalServerMessage(performerString);
/*      */         
/*  671 */         if (performer.isWithinDistanceTo(receiver.getPosX(), receiver.getPosY(), receiver
/*  672 */             .getPositionZ() + receiver.getAltOffZ(), 5.0F, 0.0F))
/*      */         {
/*  674 */           if (Blocking.getBlockerBetween(receiver, performer, 5) == null) {
/*      */             
/*      */             try {
/*  677 */               CombatEngine.addWound(receiver, performer, (byte)3, performer
/*  678 */                   .getBody().getRandomWoundPos(), 4000.0D, performer.getArmourMod(), "bite", null, 0.0F, 0.0F, false, false, false, false);
/*      */             
/*      */             }
/*  681 */             catch (Exception ex) {
/*      */               
/*  683 */               logger.log(Level.WARNING, ex.getMessage(), ex);
/*      */             } 
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   static void emoteAt(short emote, Creature performer, Item target) {
/*  692 */     String pname = performer.getNameWithGenus();
/*  693 */     String rname = target.getNameWithGenus();
/*  694 */     boolean isBodyPart = target.isBodyPartAttached();
/*  695 */     if (target.isInventory()) {
/*      */       
/*  697 */       performer.getCommunicator().sendNormalServerMessage("You can't interact with that.");
/*      */       return;
/*      */     } 
/*  700 */     emoteAt(emote, performer, pname, rname, target, isBodyPart);
/*      */   }
/*      */ 
/*      */   
/*      */   static void emoteAt(short emote, Creature performer, Wall wall) {
/*  705 */     String pname = performer.getNameWithGenus();
/*      */     
/*      */     try {
/*  708 */       Structure structure = Structures.getStructure(wall.getStructureId());
/*  709 */       String rname = structure.getName();
/*  710 */       int tilex = wall.getTileX();
/*  711 */       int tiley = wall.getTileY();
/*      */       
/*  713 */       int tilez = (int)(performer.getStatus().getPositionZ() + performer.getAltOffZ()) >> 2;
/*      */       
/*  715 */       if (!performer.isWithinTileDistanceTo(tilex, tiley, tilez, emoteEntrys[emote - 2000].getRange() / 4)) {
/*  716 */         emote(emote, performer);
/*      */       } else {
/*  718 */         emoteAt(emote, performer, pname, rname, (Item)null, false);
/*      */       } 
/*  720 */     } catch (NoSuchStructureException nss) {
/*      */       
/*  722 */       performer.getCommunicator().sendNormalServerMessage("You can't interact with that.");
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void emoteAt(short emote, Creature performer, Floor floor) {
/*  729 */     String pname = performer.getNameWithGenus();
/*      */     
/*      */     try {
/*  732 */       Structure structure = Structures.getStructure(floor.getStructureId());
/*  733 */       String rname = structure.getName();
/*  734 */       int tilex = floor.getTileX();
/*  735 */       int tiley = floor.getTileY();
/*      */       
/*  737 */       int tilez = (int)(performer.getStatus().getPositionZ() + performer.getAltOffZ()) >> 2;
/*      */       
/*  739 */       if (!performer.isWithinTileDistanceTo(tilex, tiley, tilez, emoteEntrys[emote - 2000].getRange() / 4)) {
/*  740 */         emote(emote, performer);
/*      */       } else {
/*  742 */         emoteAt(emote, performer, pname, rname, (Item)null, false);
/*      */       } 
/*  744 */     } catch (NoSuchStructureException nss) {
/*      */       
/*  746 */       performer.getCommunicator().sendNormalServerMessage("You can't interact with that.");
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void emoteAt(short emote, Creature performer, BridgePart brisgePart) {
/*  753 */     String pname = performer.getNameWithGenus();
/*      */     
/*      */     try {
/*  756 */       Structure structure = Structures.getStructure(brisgePart.getStructureId());
/*  757 */       String rname = structure.getName();
/*  758 */       int tilex = brisgePart.getTileX();
/*  759 */       int tiley = brisgePart.getTileY();
/*      */       
/*  761 */       int tilez = (int)(performer.getStatus().getPositionZ() + performer.getAltOffZ()) >> 2;
/*      */       
/*  763 */       if (!performer.isWithinTileDistanceTo(tilex, tiley, tilez, emoteEntrys[emote - 2000].getRange() / 4)) {
/*  764 */         emote(emote, performer);
/*      */       } else {
/*  766 */         emoteAt(emote, performer, pname, rname, (Item)null, false);
/*      */       } 
/*  768 */     } catch (NoSuchStructureException nss) {
/*      */       
/*  770 */       performer.getCommunicator().sendNormalServerMessage("You can't interact with that.");
/*      */       return;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void emoteAt(short emote, Creature performer, Wound wound) {
/*  777 */     String pname = performer.getNameWithGenus();
/*  778 */     String rname = "a " + wound.getDescription() + " wound";
/*  779 */     emoteAt(emote, performer, pname, rname, (Item)null, false);
/*      */   }
/*      */ 
/*      */   
/*      */   static void emoteAt(short emote, Creature performer, Fence fence) {
/*  784 */     String pname = performer.getNameWithGenus();
/*  785 */     String rname = "a " + fence.getName();
/*  786 */     int tilex = fence.getTileX();
/*  787 */     int tiley = fence.getTileY();
/*      */     
/*  789 */     int tilez = (int)(performer.getStatus().getPositionZ() + performer.getAltOffZ()) >> 2;
/*  790 */     if (!performer.isWithinTileDistanceTo(tilex, tiley, tilez, emoteEntrys[emote - 2000].getRange() / 4)) {
/*  791 */       emote(emote, performer);
/*      */     } else {
/*  793 */       emoteAt(emote, performer, pname, rname, (Item)null, false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static void emoteAt(short emote, Creature performer, int tilex, int tiley, int tilez, int tile) {
/*  799 */     if (Tiles.isTree(Tiles.decodeType(tile))) {
/*      */ 
/*      */       
/*  802 */       if (!performer.isWithinTileDistanceTo(tilex, tiley, tilez, emoteEntrys[emote - 2000].getRange() / 4)) {
/*  803 */         emote(emote, performer);
/*      */       } else {
/*      */         
/*  806 */         String pname = performer.getNameWithGenus();
/*  807 */         String rname = "a tree";
/*  808 */         emoteAt(emote, performer, pname, "a tree", (Item)null, false);
/*      */       }
/*      */     
/*  811 */     } else if (Tiles.isBush(Tiles.decodeType(tile))) {
/*      */ 
/*      */       
/*  814 */       if (!performer.isWithinTileDistanceTo(tilex, tiley, tilez, emoteEntrys[emote - 2000].getRange() / 4)) {
/*  815 */         emote(emote, performer);
/*      */       } else {
/*      */         
/*  818 */         String pname = performer.getNameWithGenus();
/*  819 */         String rname = "a bush";
/*  820 */         emoteAt(emote, performer, pname, "a bush", (Item)null, false);
/*      */       } 
/*      */     } else {
/*      */       
/*  824 */       emote(emote, performer);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void emoteAt(short emote, Creature performer, String pname, String rname, @Nullable Item item, boolean isBodyPart) {
/*  834 */     String performerString = "";
/*  835 */     String bcastString = "";
/*  836 */     String detString = rname;
/*  837 */     String emoteSound = "";
/*  838 */     String emoteGender = ".male";
/*  839 */     if (performer.getSex() == 1)
/*  840 */       emoteGender = ".female"; 
/*  841 */     if (item != null)
/*  842 */       detString = "the " + item.getName(); 
/*  843 */     Creature c = null;
/*  844 */     if (isBodyPart && performer.getWurmId() != item.getOwnerId()) {
/*      */       
/*      */       try {
/*      */         
/*  848 */         c = Server.getInstance().getCreature(item.getOwnerId());
/*      */       }
/*  850 */       catch (NoSuchCreatureException noSuchCreatureException) {
/*      */ 
/*      */       
/*      */       }
/*  854 */       catch (NoSuchPlayerException noSuchPlayerException) {}
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  859 */     String ownerString = "your";
/*  860 */     String possessionString = performer.getHisHerItsString();
/*  861 */     if (c != null) {
/*      */       
/*  863 */       possessionString = c.getNameWithGenus() + (c.getName().endsWith("s") ? "'" : "'s");
/*  864 */       ownerString = possessionString;
/*      */     } 
/*  866 */     if (emote == 2000) {
/*      */       
/*  868 */       if (isBodyPart)
/*      */       {
/*  870 */         performerString = "You smile at " + ownerString + " " + item.getName() + ".";
/*  871 */         bcastString = pname + " smiles at " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/*  875 */         performerString = "You smile at " + detString + ".";
/*  876 */         bcastString = pname + " smiles at " + rname + ".";
/*      */       }
/*      */     
/*  879 */     } else if (emote == 2001) {
/*      */       
/*  881 */       emoteSound = "sound.emote.chuckle";
/*  882 */       if (isBodyPart)
/*      */       {
/*  884 */         performerString = "You chuckle happily as you think of " + ownerString + " " + item.getName() + ".";
/*  885 */         bcastString = pname + " looks at " + possessionString + " " + item.getName() + " and chuckles happily.";
/*      */       
/*      */       }
/*      */       else
/*      */       {
/*  890 */         performerString = "You chuckle happily as you think of " + detString + ".";
/*  891 */         bcastString = pname + " looks at " + rname + " and chuckles happily.";
/*      */       }
/*      */     
/*  894 */     } else if (emote == 2002) {
/*      */       
/*  896 */       emoteSound = "sound.emote.applaud";
/*  897 */       if (isBodyPart)
/*      */       {
/*  899 */         performerString = "You applaud " + ownerString + " " + item.getName() + ".";
/*  900 */         bcastString = pname + " gives a round of applause to " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/*  904 */         performerString = "You applaud " + detString + " for its efforts.";
/*  905 */         bcastString = pname + " gives " + rname + " a round of applause.";
/*      */       }
/*      */     
/*  908 */     } else if (emote == 2003) {
/*      */       
/*  910 */       if (isBodyPart)
/*      */       {
/*  912 */         performerString = "You grab " + ownerString + " " + item.getName() + " in a futile attempt of hugging.";
/*  913 */         bcastString = pname + " grabs " + possessionString + " " + item.getName() + " in a futile attempt of hugging.";
/*      */       }
/*      */       else
/*      */       {
/*  917 */         performerString = "You press " + detString + " close to your chest.";
/*  918 */         bcastString = pname + " presses " + rname + " close to " + performer.getHisHerItsString() + " chest.";
/*      */       }
/*      */     
/*  921 */     } else if (emote == 2004) {
/*      */       
/*  923 */       emoteSound = "sound.emote.kiss";
/*  924 */       if (isBodyPart)
/*      */       {
/*  926 */         performerString = "You would really love to kiss " + ownerString + " " + item.getName() + ", wouldn't you?";
/*  927 */         bcastString = pname + " stares a bit too long at " + possessionString + " " + item.getName() + ". What are they thinking about?";
/*      */       }
/*      */       else
/*      */       {
/*  931 */         performerString = "You fondly kiss " + detString + ".";
/*  932 */         bcastString = pname + " fondly kisses " + rname + ".";
/*      */       }
/*      */     
/*  935 */     } else if (emote == 2005) {
/*      */       
/*  937 */       if (isBodyPart)
/*      */       {
/*  939 */         performerString = "You would really love to grovel before " + ownerString + " " + item.getName() + ", wouldn't you?";
/*      */         
/*  941 */         bcastString = pname + " looks conflicted as their gaze lingers a bit too long on " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/*  945 */         performerString = "You throw yourself to the ground and grovel in the dirt before " + detString + ".";
/*  946 */         bcastString = pname + " grovels in the dirt before " + rname + ".";
/*      */       }
/*      */     
/*  949 */     } else if (emote == 2006) {
/*      */       
/*  951 */       if (isBodyPart)
/*      */       {
/*  953 */         performerString = "You worship " + ownerString + " beautiful " + item.getName() + ".";
/*  954 */         bcastString = pname + " worships " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/*  958 */         performerString = "You fall to your knees and worship " + detString + ".";
/*  959 */         bcastString = pname + " falls to the ground and worships " + rname + ".";
/*      */       }
/*      */     
/*  962 */     } else if (emote == 2030) {
/*      */       
/*  964 */       emoteSound = "sound.emote.follow";
/*  965 */       if (isBodyPart)
/*      */       {
/*  967 */         performerString = StringUtil.format("You tell %s %s to follow.", new Object[] { ownerString, item.getName() });
/*  968 */         bcastString = StringUtil.format("%s tells %s %s to follow %s lead.", new Object[] { pname, possessionString, item.getName(), performer
/*  969 */               .getHisHerItsString() });
/*      */       }
/*      */       else
/*      */       {
/*  973 */         performerString = StringUtil.format("You command %s to follow.", new Object[] { detString });
/*  974 */         bcastString = StringUtil.format("%s commands %s to follow %s.", new Object[] { pname, rname, performer.getHimHerItString() });
/*      */       }
/*      */     
/*  977 */     } else if (emote == 2031) {
/*      */       
/*  979 */       emoteSound = "sound.emote.goodbye";
/*  980 */       if (isBodyPart)
/*      */       {
/*  982 */         performerString = StringUtil.format("You say goodbye to %s.", new Object[] { item.getName() });
/*  983 */         bcastString = StringUtil.format("%s say goodbye to %s.", new Object[] { pname, item.getName() });
/*      */       }
/*      */       else
/*      */       {
/*  987 */         performerString = StringUtil.format("You say goodbye to %s.", new Object[] { detString });
/*  988 */         bcastString = StringUtil.format("%s say goodbye to %s.", new Object[] { pname, rname });
/*      */       }
/*      */     
/*  991 */     } else if (emote == 2032) {
/*      */       
/*  993 */       emoteSound = "sound.emote.lead";
/*  994 */       if (isBodyPart)
/*      */       {
/*  996 */         performerString = StringUtil.format("You tell %s to lead the way.", new Object[] { item.getName() });
/*  997 */         bcastString = StringUtil.format("%s tells %s to lead the way.", new Object[] { pname, item.getName() });
/*      */       }
/*      */       else
/*      */       {
/* 1001 */         performerString = StringUtil.format("You tell %s to lead the way.", new Object[] { detString });
/* 1002 */         bcastString = StringUtil.format("%s tells %s to lead the way.", new Object[] { pname, rname });
/*      */       }
/*      */     
/* 1005 */     } else if (emote == 2033) {
/*      */       
/* 1007 */       emoteSound = "sound.emote.that.way";
/* 1008 */       if (isBodyPart)
/*      */       {
/* 1010 */         performerString = StringUtil.format("You tell %s to go that way.", new Object[] { item.getName() });
/* 1011 */         bcastString = StringUtil.format("%s tells %s to go that way.", new Object[] { pname, item.getName() });
/*      */       }
/*      */       else
/*      */       {
/* 1015 */         performerString = StringUtil.format("You tell %s to go that way.", new Object[] { detString });
/* 1016 */         bcastString = StringUtil.format("%s tells %s to go that way.", new Object[] { pname, rname });
/*      */       }
/*      */     
/* 1019 */     } else if (emote == 2034) {
/*      */       
/* 1021 */       emoteSound = "sound.emote.wrong.way";
/* 1022 */       if (isBodyPart)
/*      */       {
/* 1024 */         performerString = StringUtil.format("You tell %s to not go that way.", new Object[] { item.getName() });
/* 1025 */         bcastString = StringUtil.format("%s tells %s to not go that way.", new Object[] { pname, item.getName() });
/*      */       }
/*      */       else
/*      */       {
/* 1029 */         performerString = StringUtil.format("You tell %s to not go that way.", new Object[] { detString });
/* 1030 */         bcastString = StringUtil.format("%s tells %s to not go that way.", new Object[] { pname, rname });
/*      */       }
/*      */     
/* 1033 */     } else if (emote == 2007) {
/*      */       
/* 1035 */       if (isBodyPart)
/*      */       {
/* 1037 */         performerString = "You gently comfort " + ownerString + " " + item.getName() + ".";
/* 1038 */         bcastString = pname + " gently comforts " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1042 */         performerString = "You gently pat " + detString + ".";
/* 1043 */         bcastString = pname + " gently pats " + rname + ".";
/*      */       }
/*      */     
/* 1046 */     } else if (emote == 2008) {
/*      */       
/* 1048 */       if (isBodyPart)
/*      */       {
/* 1050 */         performerString = "You wiggle and shake " + ownerString + " " + item.getName() + " a little.";
/* 1051 */         bcastString = pname + " wiggles and shakes " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1055 */         performerString = "You dance and frolic around " + detString + ".";
/* 1056 */         bcastString = pname + " dances around " + rname + ".";
/*      */       }
/*      */     
/* 1059 */     } else if (emote == 2009) {
/*      */       
/* 1061 */       if (isBodyPart)
/*      */       {
/* 1063 */         performerString = "You flirt around with " + ownerString + " " + item.getName() + " but get no response.";
/*      */         
/* 1065 */         bcastString = pname + " smiles and winks at " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1069 */         performerString = "You flirtilly wink at " + detString + ".";
/* 1070 */         bcastString = pname + " smiles and winks at " + rname + ".";
/*      */       }
/*      */     
/* 1073 */     } else if (emote == 2010) {
/*      */       
/* 1075 */       if (isBodyPart)
/*      */       {
/* 1077 */         performerString = "You wish you could bow before " + ownerString + " " + item.getName() + ".";
/* 1078 */         bcastString = pname + " sort of half bows towards " + possessionString + " " + item.getName() + " before stopping awkwardly.";
/*      */       }
/*      */       else
/*      */       {
/* 1082 */         performerString = "You bow before " + detString + ".";
/* 1083 */         bcastString = pname + " bows before " + rname + ".";
/*      */       }
/*      */     
/* 1086 */     } else if (emote == 2011) {
/*      */       
/* 1088 */       if (isBodyPart)
/*      */       {
/* 1090 */         performerString = "You bend down and gently kiss " + ownerString + " hand.";
/* 1091 */         bcastString = pname + " gently bends down and kisses " + possessionString + " hand.";
/*      */       }
/*      */       else
/*      */       {
/* 1095 */         performerString = "You bend down and gently kiss " + detString + ".";
/* 1096 */         bcastString = pname + " gently bends down and kisses " + rname + ".";
/*      */       }
/*      */     
/* 1099 */     } else if (emote == 2012) {
/*      */       
/* 1101 */       if (isBodyPart) {
/*      */         
/* 1103 */         performerString = "You tickle " + ownerString + " " + item.getName() + ". Tee-hee.";
/* 1104 */         bcastString = pname + " tickles " + possessionString + " " + item.getName() + " playfully.";
/*      */       }
/*      */       else {
/*      */         
/* 1108 */         performerString = "You tickle " + detString + " playfully.";
/* 1109 */         if (item != null) {
/* 1110 */           bcastString = pname + " tickles " + performer.getHisHerItsString() + " " + item.getName() + " playfully.";
/*      */         } else {
/* 1112 */           bcastString = pname + " tickles " + detString + " playfully.";
/*      */         }
/*      */       
/*      */       } 
/* 1116 */     } else if (emote == 2013) {
/*      */       
/* 1118 */       if (isBodyPart) {
/*      */         
/* 1120 */         performerString = "You frantically try to wave to " + ownerString + " " + item.getName() + ".";
/* 1121 */         bcastString = pname + " frantically tries to wave to " + possessionString + " " + item.getName() + ".";
/*      */       } else {
/*      */ 
/*      */         
/*      */         try {
/*      */           
/* 1127 */           if (item != null)
/*      */           {
/* 1129 */             item.getOwner();
/* 1130 */             performerString = "You wave " + detString + " frantically.";
/* 1131 */             bcastString = pname + " waves " + rname + " frantically.";
/*      */           }
/*      */           else
/*      */           {
/* 1135 */             emoteSound = "sound.emote.wave";
/* 1136 */             performerString = "You wave at " + detString + " frantically.";
/* 1137 */             bcastString = pname + " waves at " + rname + " frantically.";
/*      */           }
/*      */         
/* 1140 */         } catch (NotOwnedException no) {
/*      */           
/* 1142 */           emoteSound = "sound.emote.wave";
/* 1143 */           performerString = "You wave at " + detString + " frantically.";
/* 1144 */           bcastString = pname + " waves at " + rname + " frantically.";
/*      */         }
/*      */       
/*      */       } 
/* 1148 */     } else if (emote == 2014) {
/*      */       
/* 1150 */       emoteSound = "sound.emote.call";
/* 1151 */       if (isBodyPart)
/*      */       {
/* 1153 */         performerString = "You call out to " + ownerString + " " + item.getName() + ".";
/* 1154 */         bcastString = pname + " calls out to " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1158 */         performerString = "You call out to " + detString + ". It better be ready now!";
/* 1159 */         bcastString = pname + " calls out to " + rname + ".";
/*      */       }
/*      */     
/* 1162 */     } else if (emote == 2021) {
/*      */       
/* 1164 */       emoteSound = "sound.emote.laugh";
/* 1165 */       if (isBodyPart)
/*      */       {
/* 1167 */         performerString = "You laugh hysterically at " + ownerString + " " + item.getName() + ".";
/* 1168 */         bcastString = pname + " laughs hysterically at " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1172 */         performerString = "You laugh hysterically at " + detString + ".";
/* 1173 */         bcastString = pname + " laughs hysterically at " + rname + ".";
/*      */       }
/*      */     
/* 1176 */     } else if (emote == 2022) {
/*      */       
/* 1178 */       emoteSound = "sound.emote.cry";
/* 1179 */       if (isBodyPart)
/*      */       {
/* 1181 */         performerString = "You cry over " + ownerString + " " + item.getName() + ".";
/* 1182 */         bcastString = pname + " cries over " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1186 */         performerString = "You cry over " + detString + ".";
/* 1187 */         bcastString = pname + " cries over " + rname + ".";
/*      */       }
/*      */     
/* 1190 */     } else if (emote == 2023) {
/*      */       
/* 1192 */       if (isBodyPart)
/*      */       {
/* 1194 */         performerString = "You point at " + ownerString + " " + item.getName() + ".";
/* 1195 */         bcastString = pname + " points at " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1199 */         performerString = "You point at " + detString + ".";
/* 1200 */         bcastString = pname + " points at " + rname + ".";
/*      */       }
/*      */     
/* 1203 */     } else if (emote == 2015) {
/*      */       
/* 1205 */       if (isBodyPart)
/*      */       {
/* 1207 */         performerString = "You poke " + ownerString + " " + item.getName() + ".";
/* 1208 */         bcastString = pname + " pokes " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1212 */         performerString = "You poke " + detString + ".";
/* 1213 */         bcastString = pname + " pokes " + rname + ".";
/*      */       }
/*      */     
/* 1216 */     } else if (emote == 2016) {
/*      */       
/* 1218 */       if (isBodyPart)
/*      */       {
/* 1220 */         performerString = "You roll your eyes at " + ownerString + " " + item.getName() + ".";
/*      */         
/* 1222 */         bcastString = pname + " rolls " + performer.getHisHerItsString() + " eyes at " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1226 */         performerString = "You roll your eyes at " + detString + ".";
/* 1227 */         bcastString = pname + " rolls with " + performer.getHisHerItsString() + " eyes at " + rname + ".";
/*      */       }
/*      */     
/* 1230 */     } else if (emote == 2017) {
/*      */ 
/*      */       
/* 1233 */       if (isBodyPart)
/*      */       {
/* 1235 */         performerString = "You stare at " + ownerString + " " + item.getName() + " sceptically.";
/* 1236 */         bcastString = pname + " stares at " + possessionString + " " + item.getName() + " sceptically.";
/*      */       }
/*      */       else
/*      */       {
/* 1240 */         performerString = "You stare at " + detString + " sceptically.";
/* 1241 */         bcastString = pname + " stares at " + rname + " sceptically.";
/*      */       }
/*      */     
/* 1244 */     } else if (emote == 2018) {
/*      */       
/* 1246 */       if (isBodyPart)
/*      */       {
/* 1248 */         performerString = "You sigh and wonder what the future holds for " + ownerString + " " + item.getName() + ".";
/*      */         
/* 1250 */         bcastString = pname + " lets out a loud sigh, obviously worrying about " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1254 */         performerString = "You sigh and wonder what the future holds for " + detString + ".";
/* 1255 */         bcastString = pname + " lets out a loud sigh, obviously worrying about " + rname + ".";
/*      */       }
/*      */     
/* 1258 */     } else if (emote == 2019) {
/*      */       
/* 1260 */       emoteSound = "sound.emote.disagree";
/* 1261 */       if (isBodyPart)
/*      */       {
/* 1263 */         performerString = "You look at " + ownerString + " " + item.getName() + " and shake your head in disagreement.";
/*      */         
/* 1265 */         bcastString = pname + " looks at " + possessionString + " " + item.getName() + " and shakes " + performer.getHisHerItsString() + " head in disagreement.";
/*      */       }
/*      */       else
/*      */       {
/* 1269 */         performerString = "You roll your eyes and shake your head in disagreement with " + detString + ".";
/*      */         
/* 1271 */         bcastString = pname + " shakes " + performer.getHisHerItsString() + " head and rolls " + performer.getHisHerItsString() + " eyes in disagreement with " + rname + ".";
/*      */       }
/*      */     
/* 1274 */     } else if (emote == 2020) {
/*      */       
/* 1276 */       emoteSound = "sound.emote.tease";
/* 1277 */       if (isBodyPart)
/*      */       {
/* 1279 */         performerString = "You try to tease people with " + ownerString + " " + item.getName() + ".";
/* 1280 */         bcastString = pname + " tries to tease you with " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1284 */         performerString = "You try to tease people with " + detString + ".";
/* 1285 */         bcastString = pname + " tries to tease you with " + rname + ".";
/*      */       }
/*      */     
/*      */     }
/* 1289 */     else if (emote == 2024) {
/*      */       
/* 1291 */       emoteSound = "sound.emote.spit";
/* 1292 */       if (isBodyPart)
/*      */       {
/* 1294 */         performerString = "You drool all over " + ownerString + " " + item.getName() + ".";
/* 1295 */         bcastString = pname + " drools all over " + possessionString + " " + item.getName() + " .";
/*      */       }
/*      */       else
/*      */       {
/* 1299 */         performerString = "You spit at " + detString + ".";
/* 1300 */         bcastString = pname + " spits at " + rname + ".";
/*      */       }
/*      */     
/* 1303 */     } else if (emote == 2025) {
/*      */       
/* 1305 */       emoteSound = "sound.emote.fart";
/* 1306 */       if (isBodyPart)
/*      */       {
/* 1308 */         performerString = "You fart.";
/* 1309 */         bcastString = pname + " farts.";
/*      */       }
/*      */       else
/*      */       {
/* 1313 */         performerString = "You fart on " + detString + ".";
/* 1314 */         bcastString = pname + " farts on " + rname + ".";
/*      */       }
/*      */     
/* 1317 */     } else if (emote == 2026) {
/*      */       
/* 1319 */       emoteSound = "sound.emote.insult";
/* 1320 */       if (isBodyPart)
/*      */       {
/* 1322 */         performerString = "You call " + ownerString + " " + item.getName() + " names.";
/* 1323 */         bcastString = pname + " calls " + possessionString + " " + item.getName() + " names.";
/*      */       }
/*      */       else
/*      */       {
/* 1327 */         performerString = "You call " + detString + " names.";
/* 1328 */         bcastString = pname + " calls " + rname + " names.";
/*      */       }
/*      */     
/* 1331 */     } else if (emote == 2027) {
/*      */       
/* 1333 */       if (isBodyPart)
/*      */       {
/* 1335 */         performerString = "You try to push " + ownerString + " " + item.getName() + " to no avail.";
/* 1336 */         bcastString = pname + " tries to push " + possessionString + " " + item.getName() + " to no avail.";
/*      */       }
/*      */       else
/*      */       {
/* 1340 */         performerString = "You wish you could push around " + detString + ".";
/*      */       }
/*      */     
/* 1343 */     } else if (emote == 2028) {
/*      */       
/* 1345 */       emoteSound = "sound.emote.curse";
/* 1346 */       if (isBodyPart)
/*      */       {
/* 1348 */         performerString = "You let out heavy curses over " + ownerString + " " + item.getName() + ".";
/*      */         
/* 1350 */         bcastString = pname + " lets out a long tirade of curses over " + possessionString + " " + item.getName() + ".";
/*      */       }
/*      */       else
/*      */       {
/* 1354 */         performerString = "You let out heavy curses over " + detString + ".";
/* 1355 */         bcastString = pname + " lets out a long tirade of curses over " + rname + ".";
/*      */       }
/*      */     
/* 1358 */     } else if (emote == 2029) {
/*      */       
/* 1360 */       emoteSound = "sound.emote.slap";
/* 1361 */       if (isBodyPart) {
/*      */         
/* 1363 */         performerString = "You slap " + ownerString + " " + item.getName() + ". Ouch!";
/* 1364 */         bcastString = pname + " slaps " + possessionString + " " + item.getName() + " and causes some pain.";
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1369 */         performerString = "You slap at " + detString + ".";
/* 1370 */         bcastString = pname + " slaps at " + rname + ".";
/*      */       } 
/*      */     } 
/* 1373 */     if (performer.mayEmote()) {
/*      */       
/* 1375 */       performer.makeEmoteSound();
/* 1376 */       Methods.sendSound(performer, emoteSound + emoteGender);
/*      */       
/* 1378 */       performer.playAnimation(emoteEntrys[emote - 2000].getActionString().toLowerCase(), false);
/*      */     } 
/* 1380 */     Server.getInstance().broadCastAction(bcastString, performer, 5);
/* 1381 */     performer.getCommunicator().sendNormalServerMessage(performerString);
/*      */   }
/*      */ 
/*      */   
/*      */   static void emote(short emote, Creature performer) {
/* 1386 */     String performerString = "";
/* 1387 */     String bcastString = "";
/* 1388 */     String pname = performer.getNameWithGenus();
/* 1389 */     if (emote == 2000) {
/*      */       
/* 1391 */       performerString = "You smile happily.";
/* 1392 */       bcastString = pname + " smiles happily.";
/*      */     }
/* 1394 */     else if (emote == 2001) {
/*      */       
/* 1396 */       performerString = "You chuckle.";
/* 1397 */       bcastString = pname + " chuckles.";
/*      */     }
/* 1399 */     else if (emote == 2002) {
/*      */       
/* 1401 */       performerString = "You give a round of applause.";
/* 1402 */       bcastString = pname + " gives a round of applause.";
/*      */     }
/* 1404 */     else if (emote == 2003) {
/*      */       
/* 1406 */       performerString = "You look around for someone to hug but find only yourself.";
/*      */     }
/* 1408 */     else if (emote == 2004) {
/*      */       
/* 1410 */       performerString = "You fondly blow kisses to the air.";
/* 1411 */       bcastString = pname + " fondly blows kisses to the air.";
/*      */     }
/* 1413 */     else if (emote == 2005) {
/*      */       
/* 1415 */       performerString = "You throw yourself to the ground and grovel in the dirt, humiliated.";
/* 1416 */       bcastString = pname + " grovels in the dirt, humiliated.";
/*      */     }
/* 1418 */     else if (emote == 2030) {
/*      */       
/* 1420 */       performerString = "You command your imaginary friend to follow you.";
/* 1421 */       bcastString = StringUtil.format("%s commands someone or something to follow %s.", new Object[] { pname, performer.getHimHerItString() });
/*      */     }
/* 1423 */     else if (emote == 2031) {
/*      */       
/* 1425 */       performerString = "You stare vacantly into the sky and whisper goodbye.";
/* 1426 */       bcastString = StringUtil.format("%s stares vacantly into the sky and whispers goodbye.", new Object[] { pname });
/*      */     }
/* 1428 */     else if (emote == 2032) {
/*      */       
/* 1430 */       performerString = "You gaze into the sky and ask the gods for guidance.";
/* 1431 */       bcastString = StringUtil.format("%s turns %s gaze into the sky and asks for guidance.", new Object[] { pname, performer
/* 1432 */             .getHisHerItsString() });
/*      */     }
/* 1434 */     else if (emote == 2033) {
/*      */       
/* 1436 */       performerString = "Yes this is the way, I'm sure of it. You mumble quietly.";
/* 1437 */       bcastString = StringUtil.format("%s mumbles something.", new Object[] { pname });
/*      */     }
/* 1439 */     else if (emote == 2034) {
/*      */       
/* 1441 */       performerString = "With tear filled eyes you look around you, you were so sure this was the way home.";
/* 1442 */       bcastString = StringUtil.format("%s looks around with tear filled eyes.", new Object[] { pname });
/*      */     }
/* 1444 */     else if (emote == 2006) {
/*      */       
/* 1446 */       performerString = "You fall to your knees and worship.";
/* 1447 */       bcastString = pname + " falls to the ground and worships.";
/*      */     }
/* 1449 */     else if (emote == 2007) {
/*      */       
/* 1451 */       performerString = "You try to cheer up. Cheer up!";
/*      */     }
/* 1453 */     else if (emote == 2008) {
/*      */       
/* 1455 */       performerString = "You dance and frolic.";
/* 1456 */       bcastString = pname + " dances around joyfully.";
/*      */     }
/* 1458 */     else if (emote == 2009) {
/*      */       
/* 1460 */       performerString = "You flirtilly wink all around.";
/* 1461 */       bcastString = pname + " smiles and winks at everyone nearby.";
/*      */     }
/* 1463 */     else if (emote == 2010) {
/*      */       
/* 1465 */       performerString = "You bow deeply.";
/* 1466 */       bcastString = pname + " bows deeply.";
/*      */     }
/* 1468 */     else if (emote == 2011) {
/*      */       
/* 1470 */       performerString = "You bend down and gently kiss your own hand.";
/* 1471 */       bcastString = pname + " gently bends down and kisses " + performer.getHisHerItsString() + " own hand.";
/*      */     }
/* 1473 */     else if (emote == 2012) {
/*      */       
/* 1475 */       performerString = "You tickle the air! Weehee!";
/* 1476 */       bcastString = pname + " gleefully tickles, tickles, tickles the air around " + performer.getHimHerItString() + "!";
/*      */     
/*      */     }
/* 1479 */     else if (emote == 2013) {
/*      */       
/* 1481 */       performerString = "You wave.";
/* 1482 */       bcastString = pname + " waves.";
/*      */     }
/* 1484 */     else if (emote == 2014) {
/*      */       
/* 1486 */       performerString = "You call out to the wind! Now or never!";
/* 1487 */       bcastString = pname + " calls out to the wind, boosting morale.";
/*      */     }
/* 1489 */     else if (emote == 2021) {
/*      */       
/* 1491 */       performerString = "You laugh hysterically.";
/* 1492 */       bcastString = pname + " laughs hysterically.";
/*      */     }
/* 1494 */     else if (emote == 2022) {
/*      */       
/* 1496 */       performerString = "You cry bitter tears.";
/* 1497 */       bcastString = pname + " cries.";
/*      */     }
/* 1499 */     else if (emote == 2023) {
/*      */       
/* 1501 */       performerString = "You point forward.";
/* 1502 */       bcastString = pname + " points forward.";
/*      */     }
/* 1504 */     else if (emote == 2015) {
/*      */       
/* 1506 */       performerString = "You poke the air in front of you.";
/* 1507 */       bcastString = pname + " pokes with " + performer.getHisHerItsString() + " finger in mid-air.";
/*      */     }
/* 1509 */     else if (emote == 2016) {
/*      */       
/* 1511 */       performerString = "You roll your eyes.";
/* 1512 */       bcastString = pname + " rolls with " + performer.getHisHerItsString() + " eyes.";
/*      */     }
/* 1514 */     else if (emote == 2017) {
/*      */       
/* 1516 */       performerString = "You stare sceptically into the darkest corners of eternity.";
/* 1517 */       bcastString = pname + " suddenly has a very sceptic look upon " + performer.getHisHerItsString() + " face.";
/*      */     }
/* 1519 */     else if (emote == 2018) {
/*      */       
/* 1521 */       performerString = "You sigh and worry about the future.";
/* 1522 */       bcastString = pname + " lets out a huge sigh, obviously worrying about something.";
/*      */     }
/* 1524 */     else if (emote == 2019) {
/*      */       
/* 1526 */       performerString = "You roll your eyes and shake your head in disagreement with the situation.";
/*      */       
/* 1528 */       bcastString = pname + " shakes " + performer.getHisHerItsString() + " head and rolls " + performer.getHisHerItsString() + " eyes in disagreement with something.";
/*      */     }
/* 1530 */     else if (emote == 2020) {
/*      */       
/* 1532 */       performerString = "You try to tease everyone around you.";
/* 1533 */       bcastString = pname + " obviously tries to tease everyone into action.";
/*      */     
/*      */     }
/* 1536 */     else if (emote == 2024) {
/*      */       
/* 1538 */       performerString = "You spit.";
/* 1539 */       bcastString = pname + " spits.";
/*      */     }
/* 1541 */     else if (emote == 2025) {
/*      */       
/* 1543 */       performerString = "You fart.";
/* 1544 */       bcastString = pname + " farts.";
/*      */     }
/* 1546 */     else if (emote == 2026) {
/*      */       
/* 1548 */       performerString = "You call yourself names.";
/* 1549 */       bcastString = pname + " mumbles aggressively.";
/*      */     }
/* 1551 */     else if (emote == 2027) {
/*      */       
/* 1553 */       performerString = "You wish you could push someone around.";
/*      */     }
/* 1555 */     else if (emote == 2028) {
/*      */       
/* 1557 */       performerString = "You let out heavy curses.";
/* 1558 */       bcastString = pname + " lets out a long tirade of curses.";
/*      */     }
/* 1560 */     else if (emote == 2029) {
/*      */       
/* 1562 */       performerString = "You slap yourself in the face. Stupid!";
/* 1563 */       bcastString = pname + " slaps " + performer.getHisHerItsString() + " in the face, as if regretting something.";
/*      */     } 
/* 1565 */     if (performer.mayEmote()) {
/*      */       
/* 1567 */       performer.makeEmoteSound();
/* 1568 */       performer.playAnimation(emoteEntrys[emote - 2000].getActionString().toLowerCase(), false);
/*      */     } 
/* 1570 */     Server.getInstance().broadCastAction(bcastString, performer, 5);
/* 1571 */     performer.getCommunicator().sendNormalServerMessage(performerString);
/*      */   }
/*      */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\Emotes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */