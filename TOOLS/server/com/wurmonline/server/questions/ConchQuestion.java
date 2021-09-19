/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.players.Abilities;
/*     */ import com.wurmonline.server.players.Achievements;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.zones.FocusZone;
/*     */ import com.wurmonline.server.zones.NoSuchZoneException;
/*     */ import com.wurmonline.server.zones.Zones;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConchQuestion
/*     */   extends Question
/*     */ {
/*     */   Item conch;
/*  26 */   int nextTemplateId = 0;
/*     */ 
/*     */   
/*     */   public ConchQuestion(Creature aResponder, long aTarget) {
/*  30 */     super(aResponder, "The Conch Speaks", "As you listen, you hear voices from beyond", 12800, aTarget);
/*     */     
/*     */     try {
/*  33 */       this.conch = Items.getItem(aTarget);
/*     */     }
/*  35 */     catch (NoSuchItemException nsi) {
/*     */       
/*  37 */       this.conch = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean sendAchievementNeeded(Creature responder, StringBuilder buf, Item conch) {
/*  43 */     Achievements ach = Achievements.getAchievementObject(responder.getWurmId());
/*     */     
/*  45 */     if (conch.getAuxData() == 10 || ach.getAchievement(1) == null) {
/*  46 */       return sendSixthBml(responder, buf, 1, conch);
/*     */     }
/*  48 */     if (ach.getAchievement(52) != null) {
/*     */       
/*  50 */       conch.setData(14L);
/*  51 */       return false;
/*     */     } 
/*     */     
/*  54 */     if (conch.getAuxData() == 12 || ach.getAchievement(52) == null)
/*  55 */       return sendSeventhBml(responder, buf, 52, conch); 
/*  56 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean sendItemNeeded(Creature responder, StringBuilder buf, Item conch) {
/*  61 */     if (responder.getKingdomTemplateId() == 2) {
/*     */       
/*  63 */       if (conch.getAuxData() == 0 || !responder.hasAbility(Abilities.getAbilityForItem(809, responder)))
/*  64 */         return sendFirstBml(responder, buf, 809, conch); 
/*  65 */       if (conch.getAuxData() == 2 || !responder.hasAbility(Abilities.getAbilityForItem(808, responder)))
/*  66 */         return sendSecondBml(responder, buf, 808, conch); 
/*  67 */       if (conch.getAuxData() == 4 || !responder.hasAbility(Abilities.getAbilityForItem(798, responder)))
/*  68 */         return sendThirdBml(responder, buf, 798, conch); 
/*  69 */       if (conch.getAuxData() == 6 || !responder.hasAbility(Abilities.getAbilityForItem(810, responder)))
/*  70 */         return sendFourthBml(responder, buf, 810, conch); 
/*  71 */       if (conch.getAuxData() == 8 || !responder.hasAbility(Abilities.getAbilityForItem(807, responder))) {
/*  72 */         return sendFifthBml(responder, buf, 807, conch);
/*     */       }
/*  74 */     } else if (responder.getKingdomTemplateId() == 3) {
/*     */       
/*  76 */       if (conch.getAuxData() == 0 || !responder.hasAbility(Abilities.getAbilityForItem(808, responder)))
/*  77 */         return sendFirstBml(responder, buf, 808, conch); 
/*  78 */       if (conch.getAuxData() == 2 || !responder.hasAbility(Abilities.getAbilityForItem(809, responder)))
/*  79 */         return sendSecondBml(responder, buf, 809, conch); 
/*  80 */       if (conch.getAuxData() == 4 || !responder.hasAbility(Abilities.getAbilityForItem(807, responder)))
/*  81 */         return sendThirdBml(responder, buf, 807, conch); 
/*  82 */       if (conch.getAuxData() == 6 || !responder.hasAbility(Abilities.getAbilityForItem(810, responder)))
/*  83 */         return sendFourthBml(responder, buf, 810, conch); 
/*  84 */       if (conch.getAuxData() == 8 || !responder.hasAbility(Abilities.getAbilityForItem(798, responder))) {
/*  85 */         return sendFifthBml(responder, buf, 798, conch);
/*     */       }
/*     */     } else {
/*     */       
/*  89 */       if (conch.getAuxData() == 0 || !responder.hasAbility(Abilities.getAbilityForItem(807, responder)))
/*  90 */         return sendFirstBml(responder, buf, 807, conch); 
/*  91 */       if (conch.getAuxData() == 2 || !responder.hasAbility(Abilities.getAbilityForItem(808, responder)))
/*  92 */         return sendSecondBml(responder, buf, 808, conch); 
/*  93 */       if (conch.getAuxData() == 4 || !responder.hasAbility(Abilities.getAbilityForItem(810, responder)))
/*  94 */         return sendThirdBml(responder, buf, 810, conch); 
/*  95 */       if (conch.getAuxData() == 6 || !responder.hasAbility(Abilities.getAbilityForItem(809, responder)))
/*  96 */         return sendFourthBml(responder, buf, 809, conch); 
/*  97 */       if (conch.getAuxData() == 8 || !responder.hasAbility(Abilities.getAbilityForItem(798, responder)))
/*  98 */         return sendFifthBml(responder, buf, 798, conch); 
/*     */     } 
/* 100 */     if (conch.getAuxData() < 10) {
/* 101 */       conch.setAuxData((byte)10);
/*     */     }
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 109 */     if (this.conch == null) {
/*     */       
/* 111 */       getResponder().getCommunicator().sendAlertServerMessage("The Conch is gone!");
/*     */       return;
/*     */     } 
/* 114 */     String key = "listen";
/* 115 */     String val = answers.getProperty("listen");
/* 116 */     if (Boolean.parseBoolean(val)) {
/*     */       
/* 118 */       if (this.conch.getAuxData() == 0) {
/* 119 */         this.conch.setAuxData((byte)1);
/* 120 */       } else if (this.conch.getAuxData() % 2 == 0) {
/* 121 */         this.conch.setAuxData((byte)(this.conch.getAuxData() + 1));
/*     */       } 
/* 123 */       ConchQuestion newq = new ConchQuestion(getResponder(), this.conch.getWurmId());
/* 124 */       newq.sendQuestionAfter();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 133 */     StringBuilder buf = new StringBuilder();
/* 134 */     buf.append(getBmlHeader());
/* 135 */     if (getResponder().getPower() > 0) {
/*     */       
/* 137 */       buf.append("text{text='All you hear is muffled sounds.'}text{text=''}");
/*     */     
/*     */     }
/* 140 */     else if (this.conch != null) {
/*     */       
/* 142 */       addBml(getResponder(), this.conch, buf);
/* 143 */       buf.append("text{text='Do you wish to continue listening to the voices?'}text{text=''}");
/* 144 */       buf.append("radio{ group='listen'; id='true';text='Ok'}");
/* 145 */       buf.append("radio{ group='listen'; id='false';text='Not now';selected='true'}");
/*     */     } else {
/*     */       
/* 148 */       buf.append("text{text='The Conch is gone!'}text{text=''}");
/* 149 */     }  buf.append(createAnswerButton2());
/*     */     
/* 151 */     getResponder().getCommunicator().sendBml(300, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestionAfter() {
/* 157 */     StringBuilder buf = new StringBuilder();
/* 158 */     buf.append(getBmlHeader());
/* 159 */     if (getResponder().getPower() > 0) {
/*     */       
/* 161 */       buf.append("text{text='All you hear is muffled sounds.'}text{text=''}");
/*     */     
/*     */     }
/* 164 */     else if (this.conch != null) {
/*     */       
/* 166 */       if (this.conch.getAuxData() == 1) {
/* 167 */         sendAfterFirstBml(getResponder(), buf, this.conch);
/* 168 */       } else if (this.conch.getAuxData() == 3) {
/* 169 */         sendAfterSecondBml(getResponder(), buf, this.conch);
/* 170 */       } else if (this.conch.getAuxData() == 5) {
/* 171 */         sendAfterThirdBml(getResponder(), buf, this.conch);
/* 172 */       } else if (this.conch.getAuxData() == 7) {
/* 173 */         sendAfterFourthBml(getResponder(), buf, this.conch);
/* 174 */       } else if (this.conch.getAuxData() == 9) {
/* 175 */         sendAfterFifthBml(getResponder(), buf, this.conch);
/* 176 */       } else if (this.conch.getAuxData() == 11) {
/* 177 */         sendAfterSixthBml(getResponder(), buf, this.conch);
/* 178 */       } else if (this.conch.getAuxData() == 13) {
/* 179 */         sendAfterSeventhBml(getResponder(), buf, this.conch);
/* 180 */       } else if (this.conch.getAuxData() == 15) {
/* 181 */         addFinalBml(getResponder(), buf, this.conch);
/*     */       } else {
/* 183 */         buf.append("text{text='The conch grows silent in anticipation.'}text{text=''}");
/*     */       } 
/*     */     } else {
/* 186 */       buf.append("text{text='The conch is gone!'}text{text=''}");
/* 187 */     }  buf.append(createOkAnswerButton());
/*     */     
/* 189 */     getResponder().getCommunicator().sendBml(300, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void addBml(Creature responder, Item conch, StringBuilder buf) {
/* 195 */     if (!sendItemNeeded(responder, buf, conch))
/*     */     {
/* 197 */       if (!sendAchievementNeeded(responder, buf, conch)) {
/*     */         
/* 199 */         Achievements ach = Achievements.getAchievementObject(responder.getWurmId());
/*     */         
/* 201 */         if (ach.getAchievement(322) == null) {
/* 202 */           buf.append("text{text='It seems the conch is trying to get your attention.'}text{text=''}");
/*     */         } else {
/* 204 */           buf.append("text{text='All you hear is muffled sounds but maybe you can make something out of it.'}text{text=''}");
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static final boolean addFinalBml(Creature responder, StringBuilder buf, Item conch) {
/* 211 */     if (isThisAdventureServer()) {
/*     */       
/* 213 */       buf.append("text{text=\"The " + addSpiritVoiceType(responder) + " says: \"}text{text=\"\"}");
/* 214 */       buf.append("text{text=\"'You seem to be ready. Ceyer, Brightberry and even Zampooklidin would have been proud of you.\"}text{text=\"\"}");
/* 215 */       buf.append("text{text=\"Find the Key in the darkness and if you so choose, use it. May your soul rule the Heavens wisely.\"}text{text=\"\"}");
/* 216 */       buf.append("text{text=\"Know however, that you will remain here and only the essence of your being travels.\"}text{text=\"\"}");
/* 217 */       buf.append("text{text=\"We hope you don't feel cheated by us. We really need help and hope that your deity will find a way to return us to Valrei.\"}text{text=\"\"}");
/* 218 */       buf.append("text{text=\"We will show you the way now.'\"}text{text=\"\"}");
/* 219 */       sendSignalToEntrance(responder, conch);
/*     */     } 
/* 221 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean sendFirstBml(Creature responder, StringBuilder buf, int templateId, Item conch) {
/* 226 */     conch.setAuxData((byte)0);
/* 227 */     conch.setData1(templateId);
/* 228 */     buf.append("text{text=\"A faint " + addSpiritVoiceType(responder) + " is heard over the muffled sounds of the conch shell: \"}text{text=\"\"}");
/* 229 */     buf.append("text{text=\"'Can you hear me?\"}text{text=\"\"}");
/* 230 */     buf.append("text{text=\"I am a spirit of the nature. We can only see living beings as if through a veil and you sound very distant.\"}text{text=\"\"}");
/* 231 */     buf.append("text{text=\"We will show you the way to a powerful item if you want.\"}text{text=\"\"}");
/* 232 */     if (responder.hasAbility(Abilities.getAbilityForItem(templateId, responder))) {
/*     */       
/* 234 */       buf.append("text{text=\"You already are " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + ". That will speed things up!'\"}text{text=\"\"}");
/* 235 */       conch.setAuxData((byte)2);
/*     */     }
/*     */     else {
/*     */       
/* 239 */       buf.append("text{text=\"The item will teach you how to become " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + ".\"}text{text=\"\"}");
/* 240 */       buf.append("text{text=\"We will explain more afterwards.'\"}text{text=\"\"}");
/*     */     } 
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendAfterFirstBml(Creature responder, StringBuilder buf, Item conch) {
/* 247 */     String tome = "tome";
/*     */     
/*     */     try {
/* 250 */       ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(conch.getData1());
/* 251 */       tome = template.getName();
/*     */     }
/* 253 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 258 */     buf.append("text{text=\"'You should look for a " + tome + ".\"}text{text=\"\"}");
/* 259 */     Item item = getItemToSend(conch.getData1());
/* 260 */     if (item == null) {
/*     */ 
/*     */       
/* 263 */       buf.append("text{text=\"We could not locate that item. It seems you have to do missions for the gods and hope for the best.'\"}text{text=\"\"}");
/*     */     }
/*     */     else {
/*     */       
/* 267 */       sendSignalToItemTemplate(item);
/*     */       
/* 269 */       buf.append("text{text=\"'Look in the sky. We have created a colored spirit light for you to follow', the " + addSpiritVoiceType(responder) + " whispers.\"}text{text=\"\"}");
/*     */     } 
/* 271 */     buf.append("text{text=\"'You need to prepare carefully for the journey.\"}text{text=\"\"}");
/* 272 */     buf.append("text{text=\"Take your time. It will be a hard trip. Build up your strength and resources.\"}text{text=\"\"}");
/* 273 */     buf.append("text{text=\"Listen to this shell again if you require guidance.\"}text{text=\"\"}");
/*     */     
/* 275 */     buf.append("text{text=\"Safe journeys.'\"}text{text=\"\"}");
/* 276 */     buf.append("text{text=\"The voice grows silent.\"}text{text=\"\"}");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean sendSecondBml(Creature responder, StringBuilder buf, int templateId, Item conch) {
/* 283 */     conch.setData1(templateId);
/* 284 */     conch.setAuxData((byte)2);
/* 285 */     buf.append("text{text=\"As before, a " + addSpiritVoiceType(responder) + " is heard from the shell: \"}text{text=\"\"}");
/* 286 */     buf.append("text{text=\"'We were once servants to the Gods on the Moon of Valrei. Immortal and powerful.\"}text{text=\"\"}");
/* 287 */     buf.append("text{text=\"We heard and saw too much. Too much misery and.. pain. We were cast out. To this place.. this spirit world.\"}text{text=\"\"}");
/* 288 */     buf.append("text{text=\"Here we will remain forever.\"}text{text=\"\"}");
/* 289 */     buf.append("text{text=\"Unless, perhaps.. something dramatic happens on Valrei. We can only hope.\"}text{text=\"\"}");
/* 290 */     buf.append("text{text=\"At least there is a way.\"}text{text=\"\"}");
/* 291 */     if (responder.hasAbility(Abilities.getAbilityForItem(templateId, responder))) {
/*     */       
/* 293 */       buf.append("text{text=\"As you already are " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + " we will show you the next tome.'\"}text{text=\"\"}");
/* 294 */       conch.setAuxData((byte)4);
/*     */     }
/*     */     else {
/*     */       
/* 298 */       buf.append("text{text=\"For now, let us show you where you can become " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + ".'\"}text{text=\"\"}");
/*     */     } 
/* 300 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendAfterSecondBml(Creature responder, StringBuilder buf, Item conch) {
/* 305 */     String tome = "tome";
/*     */     
/*     */     try {
/* 308 */       ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(conch.getData1());
/* 309 */       tome = template.getName();
/*     */     }
/* 311 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 316 */     buf.append("text{text=\"'You should look for a " + tome + ".\"}text{text=\"\"}");
/* 317 */     Item item = getItemToSend(conch.getData1());
/* 318 */     if (item == null) {
/*     */ 
/*     */       
/* 321 */       buf.append("text{text=\"'We could not locate that item. It seems you have to do missions for the gods and hope for the best.'\"}text{text=\"\"}");
/*     */     }
/*     */     else {
/*     */       
/* 325 */       sendSignalToItemTemplate(item);
/* 326 */       buf.append("text{text=\"'We have created a colored spirit light for you to follow', the " + addSpiritVoiceType(responder) + " whispers.\"}text{text=\"\"}");
/*     */     } 
/* 328 */     buf.append("text{text=\"'Listen to this shell again if you require guidance.\"}text{text=\"\"}");
/* 329 */     buf.append("text{text=\"Good luck on your travels.'\"}text{text=\"\"}");
/* 330 */     buf.append("text{text=\"The voice grows silent.\"}text{text=\"\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean sendThirdBml(Creature responder, StringBuilder buf, int templateId, Item conch) {
/* 335 */     conch.setData1(templateId);
/* 336 */     conch.setAuxData((byte)4);
/* 337 */     buf.append("text{text=\"Again, the " + addSpiritVoiceType(responder) + " whispers from the shell: \"}text{text=\"\"}");
/* 338 */     buf.append("text{text=\"'There was a war. But before the war there was unity.\"}text{text=\"\"}");
/* 339 */     buf.append("text{text=\"The three masters Ceyer, Brightberry and Zampooklidin all were friends.\"}text{text=\"\"}");
/* 340 */     buf.append("text{text=\"The gods wanted different and handed down a Key to the Heavens, well aware of the impact this would have on the friendship.\"}text{text=\"\"}");
/* 341 */     buf.append("text{text=\"Ceyer was the one who received it and he consulted the others.\"}text{text=\"\"}");
/* 342 */     buf.append("text{text=\"Things did not turn out well.\"}text{text=\"\"}");
/* 343 */     if (responder.hasAbility(Abilities.getAbilityForItem(templateId, responder))) {
/*     */       
/* 345 */       buf.append("text{text=\"You already are " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + ". That will make the journey quicker.'\"}text{text=\"\"}");
/* 346 */       conch.setAuxData((byte)6);
/*     */     }
/*     */     else {
/*     */       
/* 350 */       buf.append("text{text=\"We will tell you more when you are " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + ".'\"}text{text=\"\"}");
/*     */     } 
/* 352 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendAfterThirdBml(Creature responder, StringBuilder buf, Item conch) {
/* 357 */     String tome = "tome";
/*     */     
/*     */     try {
/* 360 */       ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(conch.getData1());
/* 361 */       tome = template.getName();
/*     */     }
/* 363 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 368 */     buf.append("text{text=\"'You should look for a " + tome + ".\"}text{text=\"\"}");
/* 369 */     Item item = getItemToSend(conch.getData1());
/* 370 */     if (item == null) {
/*     */ 
/*     */       
/* 373 */       buf.append("text{text=\"'We could not locate that item. It seems you have to do missions for the gods and hope for the best.'\"}text{text=\"\"}");
/*     */     }
/*     */     else {
/*     */       
/* 377 */       sendSignalToItemTemplate(item);
/* 378 */       buf.append("text{text=\"'Look to the skies. The spirit light should guide you, says the " + addSpiritVoiceType(responder) + ".\"}text{text=\"\"}");
/*     */     } 
/* 380 */     buf.append("text{text=\"We wish you all the best.'\"}text{text=\"\"}");
/* 381 */     buf.append("text{text=\"The voice grows silent.\"}text{text=\"\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean sendFourthBml(Creature responder, StringBuilder buf, int templateId, Item conch) {
/* 386 */     conch.setData1(templateId);
/* 387 */     conch.setAuxData((byte)6);
/* 388 */     buf.append("text{text=\"From the shell, a " + addSpiritVoiceType(responder) + " continues the story: \"}text{text=\"\"}");
/* 389 */     buf.append("text{text=\"'As Ceyer, Zampooklidin and Brightberry discussed who should use the Key and ascend to Valrei a stranger approached.\"}text{text=\"\"}");
/* 390 */     buf.append("text{text=\"It was Malinkaan, a powerful Arch Mage who had found out about the Key. Rumours have it that Vynora gave him divination.\"}text{text=\"\"}");
/* 391 */     buf.append("text{text=\"As he made a lunge at Ceyer with his sword, Brightberry stepped in the way and shed her blood instead.\"}text{text=\"\"}");
/* 392 */     buf.append("text{text=\"Ceyer ran while Zampooklidin stayed and vanquished Malinkaan at Grimoleth Peak.\"}text{text=\"\"}");
/* 393 */     buf.append("text{text=\"Thoughts started to grow in Zampooklidins mind that he now had the right to the key, no doubt without the influence of Libila.\"}text{text=\"\"}");
/* 394 */     if (responder.hasAbility(Abilities.getAbilityForItem(templateId, responder))) {
/*     */       
/* 396 */       buf.append("text{text=\"You are a " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + " already indeed. We will show you the next place.'\"}text{text=\"\"}");
/* 397 */       conch.setAuxData((byte)8);
/*     */     }
/*     */     else {
/*     */       
/* 401 */       buf.append("text{text=\"Now you should become " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + " as well.'\"}text{text=\"\"}");
/*     */     } 
/* 403 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendAfterFourthBml(Creature responder, StringBuilder buf, Item conch) {
/* 408 */     String tome = "tome";
/*     */     
/*     */     try {
/* 411 */       ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(conch.getData1());
/* 412 */       tome = template.getName();
/*     */     }
/* 414 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 419 */     buf.append("text{text=\"'You should look for a " + tome + ".\"}text{text=\"\"}");
/* 420 */     Item item = getItemToSend(conch.getData1());
/* 421 */     if (item == null) {
/*     */ 
/*     */       
/* 424 */       buf.append("text{text=\"'We could not locate that item. It seems you have to do missions for the gods and hope for the best.'\"}text{text=\"\"}");
/*     */     }
/*     */     else {
/*     */       
/* 428 */       sendSignalToItemTemplate(item);
/* 429 */       buf.append("text{text=\"'Find the spirit light we have created', advices the " + addSpiritVoiceType(responder) + ".\"}text{text=\"\"}");
/*     */     } 
/* 431 */     buf.append("text{text=\"'And stay out of harms way.'\"}text{text=\"\"}");
/* 432 */     buf.append("text{text=\"The voice grows silent.\"}text{text=\"\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean sendFifthBml(Creature responder, StringBuilder buf, int templateId, Item conch) {
/* 437 */     conch.setData1(templateId);
/* 438 */     conch.setAuxData((byte)8);
/* 439 */     buf.append("text{text=\"'Darkness ascended on the lands as Zampooklidin declared war on Ceyer, and soon Brightberrys people craved the Key as well for their loss.'\"}text{text=\"\"}");
/* 440 */     buf.append("text{text=\"'Ceyer was too proud to disregard his cowardness', the " + addSpiritVoiceType(responder) + " declares.\"}text{text=\"\"}");
/* 441 */     buf.append("text{text=\"'He decided that someone else was worthy of the Key, but not even Zampooklidin.\"}text{text=\"\"}");
/* 442 */     buf.append("text{text=\"As he hid the key, he put a magical enchantment on the container which now only the most worthy of persons can open.\"}text{text=\"\"}");
/* 443 */     buf.append("text{text=\"You are soon that person.\"}text{text=\"\"}");
/* 444 */     if (responder.hasAbility(Abilities.getAbilityForItem(templateId, responder))) {
/*     */       
/* 446 */       buf.append("text{text=\"You are a " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + " already indeed. We would ask two things more from you before we show where to find the container.'\"}text{text=\"\"}");
/*     */     }
/*     */     else {
/*     */       
/* 450 */       buf.append("text{text=\"After you become " + Abilities.getAbilityString(Abilities.getAbilityForItem(templateId, responder)) + " we will ask two small matters more before we show you the way to the container'.\"}text{text=\"\"}");
/*     */     } 
/* 452 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendAfterFifthBml(Creature responder, StringBuilder buf, Item conch) {
/* 457 */     String tome = "tome";
/*     */     
/*     */     try {
/* 460 */       ItemTemplate template = ItemTemplateFactory.getInstance().getTemplate(conch.getData1());
/* 461 */       tome = template.getName();
/*     */     }
/* 463 */     catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 468 */     buf.append("text{text=\"'You should look for a " + tome + ".\"}text{text=\"\"}");
/* 469 */     Item item = getItemToSend(conch.getData1());
/* 470 */     if (item == null) {
/*     */       
/* 472 */       buf.append("text{text=\"'We could not locate that item. It seems you have to do missions for the gods and hope for the best.'\"}text{text=\"\"}");
/*     */     }
/*     */     else {
/*     */       
/* 476 */       sendSignalToItemTemplate(item);
/* 477 */       buf.append("text{text=\"'This spirit light will show you to the last thing you need to find.', says the " + addSpiritVoiceType(responder) + ".\"}text{text=\"\"}");
/*     */     } 
/* 479 */     buf.append("text{text=\"'Godspeed.'\"}text{text=\"\"}");
/* 480 */     buf.append("text{text=\"The voice grows silent.\"}text{text=\"\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean sendSixthBml(Creature responder, StringBuilder buf, int achievementId, Item conch) {
/* 485 */     conch.setData1(achievementId);
/* 486 */     conch.setAuxData((byte)10);
/* 487 */     buf.append("text{text=\"'Before we show you the hidden entrance, we will ask you to show your worth in the Hunt Of The Ancients.'\"}text{text=\"\"}");
/* 488 */     buf.append("text{text=\"'It is a test we do to honour Ceyer since he himself understood his cowardness', the " + addSpiritVoiceType(responder) + " explains.\"}text{text=\"\"}");
/* 489 */     buf.append("text{text=\"'All we ask is that you conquer a pillar in the Hunt.\"}text{text=\"\"}");
/* 490 */     Achievements ach = Achievements.getAchievementObject(responder.getWurmId());
/*     */     
/* 492 */     if (ach.getAchievement(achievementId) != null) {
/*     */       
/* 494 */       buf.append("text{text=\"You have already done that, so let us move on to the next thing.'\"}text{text=\"\"}");
/* 495 */       conch.setAuxData((byte)12);
/*     */     }
/*     */     else {
/*     */       
/* 499 */       buf.append("text{text=\"You can find out when the next Hunt begins at a settlement token.\"}text{text=\"\"}");
/* 500 */       buf.append("text{text=\"After you conquer the pillar, there is only one small matter left.'\"}text{text=\"\"}");
/*     */     } 
/* 502 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendAfterSixthBml(Creature responder, StringBuilder buf, Item conch) {
/* 507 */     sendSignalToHota(conch, responder);
/* 508 */     buf.append("text{text=\"There should be a light guiding your way', the " + addSpiritVoiceType(responder) + " says. 'Let us show you the way to the area where the Hunt Of The Ancients take place.'\"}text{text=\"\"}");
/* 509 */     buf.append("text{text=\"'Good Hunting!'\"}text{text=\"\"}");
/* 510 */     buf.append("text{text=\"The voice grows silent.\"}text{text=\"\"}");
/*     */   }
/*     */ 
/*     */   
/*     */   private static final boolean sendSeventhBml(Creature responder, StringBuilder buf, int achievementId, Item conch) {
/* 515 */     conch.setData1(achievementId);
/* 516 */     conch.setAuxData((byte)12);
/* 517 */     buf.append("text{text=\"'The hidden entrance to the place where the container with Ceyer's Key lies hidden is one task away now', concludes " + addSpiritVoiceType(responder) + ".\"}text{text=\"\"}");
/* 518 */     buf.append("text{text=\"'If you have not already tried to help the deities out by doing a mission for them, now is the time.\"}text{text=\"\"}");
/* 519 */     buf.append("text{text=\"If you are to ascend to Valrei, you must be aware of how those missions will effect you up there. We have noticed you have a Valrei map available.\"}text{text=\"\"}");
/* 520 */     buf.append("text{text=\"By doing those missions, the Deity you help will move quicker on the map and achieve its goals faster.\"}text{text=\"\"}");
/* 521 */     buf.append("text{text=\"If a Deity achieves those goals, their helpers will usually receive rewards.\"}text{text=\"\"}");
/* 522 */     Achievements ach = Achievements.getAchievementObject(responder.getWurmId());
/*     */     
/* 524 */     if (ach.getAchievement(achievementId) != null) {
/*     */       
/* 526 */       buf.append("text{text=\"You have already done that, which pleases us greatly.'\"}text{text=\"\"}");
/* 527 */       conch.setAuxData((byte)14);
/*     */     }
/*     */     else {
/*     */       
/* 531 */       buf.append("text{text=\"Once you have helped in a mission of your choice, we will show you the entrance.'\"}text{text=\"\"}");
/*     */     } 
/* 533 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendAfterSeventhBml(Creature responder, StringBuilder buf, Item conch) {
/* 538 */     buf.append("text{text=\"There will be no light this time', says the " + addSpiritVoiceType(responder) + ". 'We will save our energy for the final push.'\"}text{text=\"\"}");
/* 539 */     buf.append("text{text=\"'Best of luck.'\"}text{text=\"\"}");
/* 540 */     buf.append("text{text=\"The voice grows silent.\"}text{text=\"\"}");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean isThisAdventureServer() {
/*     */     try {
/* 547 */       Item i = Items.getItem(5390755858690L);
/* 548 */       if (i.getTemplateId() == 664) {
/* 549 */         return true;
/*     */       }
/* 551 */     } catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */ 
/*     */     
/* 555 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String addSpiritVoiceType(Creature responder) {
/* 561 */     int spirit = Zones.getSpiritsForTile(responder.getTileX(), responder.getTileY(), responder.isOnSurface());
/* 562 */     String sname = "distant voice";
/* 563 */     if (spirit == 4)
/*     */     {
/* 565 */       sname = "echoing voice";
/*     */     }
/* 567 */     if (spirit == 2)
/*     */     {
/* 569 */       sname = "voice with undertones of running water";
/*     */     }
/* 571 */     if (spirit == 3)
/*     */     {
/* 573 */       sname = "metallic voice";
/*     */     }
/* 575 */     if (spirit == 1)
/*     */     {
/* 577 */       sname = "crackling voice";
/*     */     }
/* 579 */     return sname;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendSignalToEntrance(Creature responder, Item conch) {
/* 584 */     if (isThisAdventureServer() && responder.isPlayer()) {
/*     */       
/* 586 */       Player presp = (Player)responder;
/* 587 */       int x = 822;
/* 588 */       int y = 493;
/*     */       
/* 590 */       presp.addItemEffect(conch.getWurmId(), 822, 493, 10.0F);
/*     */       return;
/*     */     } 
/* 593 */     responder.getCommunicator().sendNormalServerMessage("'Oops', the shell declares. 'We couldn't find that place..'");
/*     */   }
/*     */ 
/*     */   
/*     */   public static final void sendSignalToHota(Item conch, Creature responder) {
/* 598 */     FocusZone hota = FocusZone.getHotaZone();
/* 599 */     if (responder.isPlayer()) {
/*     */       
/* 601 */       Player presp = (Player)responder;
/* 602 */       if (hota != null) {
/*     */         
/* 604 */         int cx = hota.getStartX() + (hota.getEndX() - hota.getStartX()) / 2;
/* 605 */         int cy = hota.getStartY() + (hota.getEndY() - hota.getStartY()) / 2;
/* 606 */         float posz = 10.0F;
/*     */         
/*     */         try {
/* 609 */           posz = Zones.calculateHeight((cx * 4), (cy * 4), true);
/*     */         }
/* 611 */         catch (NoSuchZoneException noSuchZoneException) {}
/*     */ 
/*     */ 
/*     */         
/* 615 */         presp.addItemEffect(conch.getWurmId(), cx, cy, posz);
/*     */         return;
/*     */       } 
/*     */     } 
/* 619 */     responder.getCommunicator().sendNormalServerMessage("'Oops', the shell declares. 'We couldn't find the HOTA zone..'");
/* 620 */     conch.setAuxData((byte)12);
/*     */   }
/*     */ 
/*     */   
/*     */   private final Item getItemToSend(int templateId) {
/* 625 */     LinkedList<Item> availItems = new LinkedList<>();
/* 626 */     for (Item item : Items.getAllItems()) {
/*     */       
/* 628 */       if (item.getTemplateId() == templateId)
/*     */       {
/* 630 */         availItems.add(item);
/*     */       }
/*     */     } 
/* 633 */     if (availItems.size() == 1)
/*     */     {
/* 635 */       return availItems.get(0);
/*     */     }
/* 637 */     if (availItems.size() > 0) {
/*     */       
/* 639 */       int num = Server.rand.nextInt(availItems.size());
/* 640 */       return availItems.get(num);
/*     */     } 
/* 642 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sendSignalToItemTemplate(Item item) {
/* 647 */     if (getResponder().isPlayer() && item != null) {
/*     */       
/* 649 */       Player presp = (Player)getResponder();
/* 650 */       presp.addItemEffect(item.getWurmId(), item.getTileX(), item.getTileY(), item.getPosZ());
/*     */       return;
/*     */     } 
/* 653 */     getResponder().getCommunicator().sendNormalServerMessage("'Oops', the shell declares. 'We couldn't find the item.. You have to do some deity missions..'");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ConchQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */