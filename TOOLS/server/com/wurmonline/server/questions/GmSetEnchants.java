/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.spells.Spell;
/*     */ import com.wurmonline.server.spells.SpellEffect;
/*     */ import com.wurmonline.server.spells.Spells;
/*     */ import com.wurmonline.shared.util.MaterialUtilities;
/*     */ import java.util.Arrays;
/*     */ import java.util.Properties;
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
/*     */ public class GmSetEnchants
/*     */   extends Question
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(GmSetEnchants.class.getName());
/*     */ 
/*     */   
/*     */   private final Item item;
/*     */ 
/*     */   
/*     */   private final Spell[] spells;
/*     */ 
/*     */   
/*     */   public GmSetEnchants(Creature aResponder, Item aTarget) {
/*  49 */     super(aResponder, "Item Enchants", itemNameWithDescription(aTarget), 104, aTarget.getWurmId());
/*  50 */     this.item = aTarget;
/*  51 */     this.spells = Spells.getSpellsEnchantingItems();
/*  52 */     Arrays.sort((Object[])this.spells);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String itemNameWithDescription(Item litem) {
/*  60 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  62 */     String name = (litem.getActualName().length() == 0) ? litem.getTemplate().getName() : litem.getActualName();
/*  63 */     MaterialUtilities.appendNameWithMaterialSuffix(sb, name, litem.getMaterial());
/*     */     
/*  65 */     if (litem.getDescription().length() > 0)
/*  66 */       sb.append(" (" + litem.getDescription() + ")"); 
/*  67 */     return "Enchants of " + sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswer) {
/*  78 */     setAnswer(aAnswer);
/*  79 */     if (this.type == 0) {
/*     */       
/*  81 */       logger.log(Level.INFO, "Received answer for a question with NOQUESTION.");
/*     */       return;
/*     */     } 
/*  84 */     if (this.type == 104)
/*     */     {
/*  86 */       if (getResponder().getPower() >= 4) {
/*     */         
/*  88 */         boolean somethingChanged = false;
/*  89 */         byte itemEnch = this.item.enchantment;
/*     */         
/*  91 */         for (int x = 0; x < this.spells.length; x++) {
/*     */           
/*  93 */           boolean newsel = Boolean.parseBoolean(aAnswer.getProperty("newsel" + x));
/*  94 */           int newpow = 50;
/*     */           
/*     */           try {
/*  97 */             newpow = Math.min(Integer.parseInt(aAnswer.getProperty("newpow" + x)), (this.spells[x].getEnchantment() == 45) ? 10000 : 104);
/*     */           }
/*  99 */           catch (NumberFormatException numberFormatException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 105 */           byte ench = this.spells[x].getEnchantment();
/* 106 */           SpellEffect eff = this.item.getSpellEffect(ench);
/* 107 */           boolean oldsel = false;
/* 108 */           int oldpow = 50;
/* 109 */           if (eff != null) {
/*     */             
/* 111 */             oldsel = true;
/* 112 */             oldpow = (int)eff.power;
/*     */           }
/* 114 */           else if (ench == itemEnch) {
/*     */             
/* 116 */             oldsel = true;
/*     */           } 
/*     */           
/* 119 */           if (newsel != oldsel || (oldsel == true && newpow != oldpow)) {
/*     */ 
/*     */             
/* 122 */             somethingChanged = true;
/* 123 */             if (oldsel)
/*     */             {
/*     */               
/* 126 */               if ((this.spells[x]).singleItemEnchant) {
/*     */                 
/* 128 */                 this.item.enchant((byte)0);
/*     */ 
/*     */ 
/*     */               
/*     */               }
/* 133 */               else if (eff != null) {
/* 134 */                 this.item.getSpellEffects().removeSpellEffect(eff.type);
/*     */               } 
/*     */             }
/* 137 */             if (newsel) {
/*     */ 
/*     */               
/* 140 */               this.spells[x].castSpell(newpow, getResponder(), this.item);
/* 141 */               logger.log(Level.INFO, getResponder().getName() + " enchanting " + this.spells[x].getName() + " " + this.item
/* 142 */                   .getName() + ", " + this.item.getWurmId() + ", " + newpow);
/* 143 */               getResponder().getLogger().log(Level.INFO, " enchanting " + this.spells[x].getName() + " " + this.item
/* 144 */                   .getName() + ", " + this.item.getWurmId() + ", " + newpow);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 149 */         if (somethingChanged) {
/*     */ 
/*     */           
/* 152 */           GmSetEnchants gt = new GmSetEnchants(getResponder(), this.item);
/* 153 */           gt.sendQuestion();
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
/*     */   public void sendQuestion() {
/* 177 */     StringBuilder buf = new StringBuilder();
/* 178 */     buf.append(getBmlHeader());
/* 179 */     if (getResponder().getPower() >= 4) {
/*     */       
/* 181 */       byte itemEnch = this.item.enchantment;
/*     */       
/* 183 */       buf.append("table{rows=\"" + this.spells.length + "\";cols=\"4\";label{text=\"\"};text{type=\"bold\";text=\"Power\"};text{type=\"bold\";text=\"Name\"};text{type=\"bold\";text=\"Description\"};");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 189 */       for (int x = 0; x < this.spells.length; x++) {
/*     */         
/* 191 */         byte ench = this.spells[x].getEnchantment();
/* 192 */         SpellEffect eff = this.item.getSpellEffect(ench);
/* 193 */         boolean sel = false;
/* 194 */         String pow = "";
/* 195 */         if (eff != null) {
/*     */           
/* 197 */           sel = true;
/* 198 */           pow = String.valueOf((int)eff.power);
/*     */         }
/* 200 */         else if (ench == itemEnch) {
/* 201 */           sel = true;
/*     */         } 
/* 203 */         int maxChars = (ench == 45) ? 5 : 3;
/* 204 */         buf.append("checkbox{id=\"newsel" + x + "\";selected=\"" + sel + "\"};" + ((this.spells[x]).singleItemEnchant ? "text{type=\"italic\";text=\"(none)\"};" : ("input{id=\"newpow" + x + "\";maxchars=\"" + maxChars + "\";text=\"" + pow + "\"};")) + "label{text=\"" + this.spells[x]
/*     */ 
/*     */             
/* 207 */             .getName() + "\"};label{text=\"" + this.spells[x]
/* 208 */             .getDescription() + "\"};");
/*     */       } 
/* 210 */       buf.append("}");
/*     */       
/* 212 */       buf.append("label{text=\"\"};");
/* 213 */       buf.append("text{type=\"bold\";text=\"--------------- Help -------------------\"}");
/* 214 */       buf.append("text{text=\"Can add or change or remove enchants to specific powers, it maybe necessary to remove an enchant before modifying its power. If the enchant requires a power, then if none is specified it will default to 50, also \"}");
/* 215 */       buf.append("text{text=\"Note: Checks to see if the item can have the enchantment are not performed.\"}");
/* 216 */       buf.append("text{text=\"If anything is changed, then once the change is applied it will show this screen again.\"}");
/* 217 */       buf.append(createAnswerButton2());
/* 218 */       getResponder().getCommunicator().sendBml(500, 500, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\GmSetEnchants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */