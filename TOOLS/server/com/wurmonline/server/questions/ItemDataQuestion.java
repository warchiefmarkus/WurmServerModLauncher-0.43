/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Items;
/*     */ import com.wurmonline.server.NoSuchItemException;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.WurmColor;
/*     */ import com.wurmonline.shared.constants.ItemMaterials;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Properties;
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
/*     */ public final class ItemDataQuestion
/*     */   extends Question
/*     */   implements ItemMaterials
/*     */ {
/*  33 */   private LinkedList<ItemTemplate> itemplates = new LinkedList<>();
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
/*     */   public ItemDataQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  45 */     super(aResponder, aTitle, aQuestion, 4, aTarget);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  82 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/*  84 */     int height = 380;
/*  85 */     int data1 = -1;
/*  86 */     int data2 = -1;
/*  87 */     int extra1 = -1;
/*  88 */     int extra2 = -1;
/*  89 */     byte auxData = 0;
/*     */     
/*     */     try {
/*  92 */       Item it = Items.getItem(this.target);
/*  93 */       data1 = it.getData1();
/*  94 */       data2 = it.getData2();
/*  95 */       extra1 = it.getExtra1();
/*  96 */       extra2 = it.getExtra2();
/*  97 */       auxData = it.getAuxData();
/*     */       
/*  99 */       buf.append("harray{input{id='itemName'; maxchars='60'; text='" + it.getActualName() + "'}label{text='Item Actual Name'}}");
/* 100 */       if (it.hasData()) {
/*     */         
/* 102 */         buf.append("harray{input{id='data1'; maxchars='20'; text='" + data1 + "'}label{text='Data1'}}");
/* 103 */         buf.append("harray{input{id='data2'; maxchars='20'; text='" + data2 + "'}label{text='Data2'}}");
/* 104 */         buf.append("harray{input{id='extra1'; maxchars='20'; text='" + extra1 + "'}label{text='Extra1'}}");
/* 105 */         buf.append("harray{input{id='extra2'; maxchars='20'; text='" + extra2 + "'}label{text='Extra2'}}");
/*     */       } 
/* 107 */       if (it.usesFoodState()) {
/*     */ 
/*     */         
/* 110 */         buf.append("label{type=\"bold\";text=\"Food State (AuxByte)\"}");
/* 111 */         int raux = it.getRightAuxData();
/* 112 */         buf.append("table{rows=\"4\";cols=\"8\";");
/*     */         
/* 114 */         buf.append(addRaux(0, raux, "(Raw)"));
/* 115 */         buf.append(addRaux(1, raux, "Fried"));
/* 116 */         buf.append(addRaux(2, raux, "Grilled"));
/* 117 */         buf.append(addRaux(3, raux, "Boiled"));
/* 118 */         buf.append(addRaux(4, raux, "Roasted"));
/* 119 */         buf.append(addRaux(5, raux, "Steamed"));
/* 120 */         buf.append(addRaux(6, raux, "Baked"));
/* 121 */         buf.append(addRaux(7, raux, "Cooked"));
/* 122 */         buf.append(addRaux(8, raux, "Candied"));
/* 123 */         buf.append(addRaux(9, raux, "Choc Coated"));
/* 124 */         buf.append("label{text=\"\"};label{text=\"\"}");
/* 125 */         buf.append("label{text=\"\"};label{text=\"\"}");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 133 */         if (it.isHerb() || it.isVegetable() || it.isFish() || it.isMushroom()) {
/* 134 */           buf.append(addLaux("chopped", it.isChopped(), "Chopped"));
/* 135 */         } else if (it.isMeat()) {
/* 136 */           buf.append(addLaux("chopped", it.isDiced(), "Diced"));
/* 137 */         } else if (it.isSpice()) {
/* 138 */           buf.append(addLaux("chopped", it.isGround(), "Ground"));
/* 139 */         } else if (it.canBeFermented()) {
/* 140 */           buf.append(addLaux("chopped", it.isUnfermented(), "Unfermented"));
/* 141 */         } else if (it.getTemplateId() == 1249) {
/* 142 */           buf.append(addLaux("chopped", it.isWhipped(), "Whipped"));
/*     */         } else {
/* 144 */           buf.append(addLaux("chopped", it.isZombiefied(), "Zombiefied"));
/*     */         } 
/* 146 */         if (it.isVegetable()) {
/* 147 */           buf.append(addLaux("mashed", it.isMashed(), "Mashed"));
/* 148 */         } else if (it.isMeat()) {
/* 149 */           buf.append(addLaux("mashed", it.isMinced(), "Minced"));
/* 150 */         } else if (it.canBeFermented()) {
/* 151 */           buf.append(addLaux("mashed", it.isFermenting(), "Fermenting"));
/* 152 */         } else if (it.isFish()) {
/* 153 */           buf.append(addLaux("mashed", it.isUnderWeight(), "Underweight"));
/*     */         } else {
/* 155 */           buf.append(addLaux("mashed", it.isClotted(), "Clotted"));
/*     */         } 
/* 157 */         if (it.canBeDistilled()) {
/* 158 */           buf.append(addLaux("wrap", it.isUndistilled(), "Undistilled"));
/*     */         } else {
/* 160 */           buf.append(addLaux("wrap", it.isWrapped(), "Wrapped"));
/*     */         } 
/* 162 */         if (it.isHerb() || it.isSpice()) {
/* 163 */           buf.append(addLaux("fresh", it.isFresh(), "Fresh"));
/* 164 */         } else if (it.isDish() || it.isLiquid()) {
/* 165 */           buf.append(addLaux("fresh", it.isSalted(), "Salted"));
/* 166 */         } else if (it.isFish()) {
/* 167 */           buf.append(addLaux("fresh", it.isLive(), "live"));
/*     */         } else {
/* 169 */           buf.append(addLaux("fresh", false, "n/a"));
/*     */         } 
/* 171 */         buf.append("}");
/* 172 */         buf.append("label{text=\"\"}");
/*     */       } else {
/*     */         
/* 175 */         buf.append("harray{input{id='aux'; maxchars='4'; text='" + auxData + "'}label{text='Auxdata'}}");
/* 176 */       }  buf.append("harray{input{id='dam'; maxchars='2'; text='" + (int)it.getDamage() + "'}label{text='Damage'}}");
/* 177 */       buf.append("harray{input{id='temp'; maxchars='5'; text='" + it.getTemperature() + "'}label{text='Temperature'}}");
/* 178 */       buf.append("harray{input{id='weight'; maxchars='7'; text='" + it.getWeightGrams(false) + "'}label{text='Weight'}}");
/* 179 */       buf.append("harray{input{id='rarity'; maxchars='1'; text='" + it.getRarity() + "'}label{text='Rarity'}}");
/*     */       
/* 181 */       buf.append("table{rows=\"1\";cols=\"4\";");
/* 182 */       String red = "";
/* 183 */       String green = "";
/* 184 */       String blue = "";
/* 185 */       boolean tick = false;
/* 186 */       if (it.getColor() != -1) {
/*     */         
/* 188 */         red = Integer.toString(WurmColor.getColorRed(it.getColor()));
/* 189 */         green = Integer.toString(WurmColor.getColorGreen(it.getColor()));
/* 190 */         blue = Integer.toString(WurmColor.getColorBlue(it.getColor()));
/* 191 */         tick = true;
/*     */       } 
/* 193 */       buf.append("checkbox{id=\"primary\";text='Primary ';selected=\"" + tick + "\";hover=\"tick if has color\"}");
/* 194 */       buf.append("harray{label{text=' Red:'}input{id='c_red'; maxchars='3'; text='" + red + "'}}");
/* 195 */       buf.append("harray{label{text=' Green:'}input{id='c_green'; maxchars='3'; text='" + green + "'}}");
/* 196 */       buf.append("harray{label{text=' Blue:'}input{id='c_blue'; maxchars='3'; text='" + blue + "'}}");
/*     */       
/* 198 */       if (it.getTemplate().supportsSecondryColor()) {
/*     */         
/* 200 */         red = "";
/* 201 */         green = "";
/* 202 */         blue = "";
/* 203 */         tick = false;
/* 204 */         if (it.getColor2() != -1) {
/*     */           
/* 206 */           red = Integer.toString(WurmColor.getColorRed(it.getColor2()));
/* 207 */           green = Integer.toString(WurmColor.getColorGreen(it.getColor2()));
/* 208 */           blue = Integer.toString(WurmColor.getColorBlue(it.getColor2()));
/* 209 */           tick = true;
/*     */         } 
/* 211 */         buf.append("checkbox{id=\"secondary\";text='Secondary ';selected=\"" + tick + "\";hover=\"tick if has color\"}");
/* 212 */         buf.append("harray{label{text=' Red:'}input{id='c2_red'; maxchars='3'; text='" + red + "'}}");
/* 213 */         buf.append("harray{label{text=' Green:'}input{id='c2_green'; maxchars='3'; text='" + green + "'}}");
/* 214 */         buf.append("harray{label{text=' Blue:'}input{id='c2_blue'; maxchars='3'; text='" + blue + "'}}");
/*     */       } 
/* 216 */       buf.append("}");
/*     */       
/* 218 */       if (getResponder().getPower() >= 4) {
/*     */         long decayTick;
/*     */         
/* 221 */         Item lunchbox = it.getParentOuterItemOrNull();
/* 222 */         if (it.isInLunchbox() && lunchbox != null) {
/* 223 */           decayTick = it.getLastMaintained() - it.getDecayTime() * (lunchbox.getRarity() / 4 + 2) - 1L;
/*     */         } else {
/* 225 */           decayTick = it.getLastMaintained() - it.getDecayTime() - 1L;
/* 226 */         }  buf.append("harray{input{id='lastMaintained'; maxchars='60'; text='" + it.getLastMaintained() + "'}label{text='Last Maintained'}}");
/*     */         
/* 228 */         buf.append("harray{label{text=\"(Set to\"};label{text=\"" + decayTick + "\"hover=\"this number can be copied using copy line\"};label{text=\"for a decay tick!)\"}}");
/*     */       } 
/*     */ 
/*     */       
/* 232 */       buf.append("label{text=\"\"}");
/*     */ 
/*     */       
/* 235 */       if (it.getTemplate().usesRealTemplate()) {
/*     */         
/* 237 */         ItemTemplate[] templates = ItemTemplateFactory.getInstance().getTemplates();
/* 238 */         Arrays.sort((Object[])templates);
/* 239 */         this.itemplates.add(null);
/* 240 */         int def = 0;
/* 241 */         if (it.getTemplate().isRune()) {
/*     */           
/* 243 */           for (int x = 0; x < templates.length; x++) {
/*     */             
/* 245 */             if (templates[x].getTemplateId() == 1102 || templates[x].getTemplateId() == 1104 || templates[x]
/* 246 */               .getTemplateId() == 1103)
/*     */             {
/* 248 */               if (it.getRealTemplate() == templates[x])
/* 249 */                 def = this.itemplates.size(); 
/* 250 */               this.itemplates.add(templates[x]);
/*     */             }
/*     */           
/*     */           } 
/*     */         } else {
/*     */           
/* 256 */           for (int x = 0; x < templates.length; x++) {
/*     */             
/* 258 */             if (templates[x].isFood() || templates[x].isLiquid() || templates[x].isFruit()) {
/*     */               
/* 260 */               if (it.getRealTemplate() == templates[x])
/* 261 */                 def = this.itemplates.size(); 
/* 262 */               this.itemplates.add(templates[x]);
/*     */             }
/* 264 */             else if (it.getTemplateId() == 1307) {
/*     */               
/* 266 */               this.itemplates.add(templates[x]);
/*     */             } 
/*     */           } 
/*     */         } 
/* 270 */         buf.append("harray{label{text=\"Real Template\"};");
/*     */         
/* 272 */         buf.append("dropdown{id=\"fruit\";default=\"" + def + "\";options=\"");
/*     */         
/* 274 */         for (int i = 0; i < this.itemplates.size(); i++) {
/*     */           
/* 276 */           if (i > 0)
/* 277 */             buf.append(","); 
/* 278 */           ItemTemplate tp = this.itemplates.get(i);
/* 279 */           if (tp == null) {
/* 280 */             buf.append("None");
/*     */           } else {
/* 282 */             buf.append(tp.getName());
/*     */           } 
/* 284 */         }  buf.append("\"}}");
/* 285 */         buf.append("label{text=\"\"}");
/*     */       } 
/* 287 */       if (it.isFood());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 292 */       buf.append("harray{label{text='LastOwner'}input{id='lastowner'; maxchars='11'; text='" + it.getLastOwnerId() + "'}}");
/*     */     }
/* 294 */     catch (NoSuchItemException noSuchItemException) {}
/*     */ 
/*     */     
/* 297 */     buf.append(createAnswerButton2());
/* 298 */     getResponder().getCommunicator().sendBml(320, height, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 304 */     setAnswer(answers);
/* 305 */     QuestionParser.parseItemDataQuestion(this);
/*     */   }
/*     */ 
/*     */   
/*     */   ItemTemplate getTemplate(int fruitId) {
/* 310 */     return this.itemplates.get(fruitId);
/*     */   }
/*     */ 
/*     */   
/*     */   private String addRaux(int id, int raux, String name) {
/* 315 */     StringBuilder buf = new StringBuilder();
/* 316 */     buf.append("radio{group=\"raux\";id=\"");
/* 317 */     buf.append(id);
/* 318 */     buf.append("\"");
/* 319 */     if (raux == id)
/* 320 */       buf.append(";selected=\"true\""); 
/* 321 */     buf.append("};");
/* 322 */     buf.append("label{text=\"");
/* 323 */     buf.append(name);
/* 324 */     buf.append("\"};");
/* 325 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private String addLaux(String id, boolean sel, String name) {
/* 330 */     StringBuilder buf = new StringBuilder();
/* 331 */     buf.append("checkbox{id=\"");
/* 332 */     buf.append(id);
/* 333 */     buf.append("\"");
/* 334 */     if (sel)
/* 335 */       buf.append(";selected=\"true\""); 
/* 336 */     buf.append("};");
/* 337 */     buf.append("label{text=\"");
/* 338 */     buf.append(name);
/* 339 */     buf.append("\"};");
/* 340 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ItemDataQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */