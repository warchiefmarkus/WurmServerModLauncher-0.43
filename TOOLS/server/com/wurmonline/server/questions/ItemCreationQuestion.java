/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ItemCreationQuestion
/*     */   extends Question
/*     */ {
/*  38 */   private LinkedList<ItemTemplate> itemplates = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String filter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemCreationQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  51 */     super(aResponder, aTitle, aQuestion, 5, aTarget);
/*  52 */     this.filter = "*";
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
/*     */   public ItemCreationQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, String aFilter) {
/*  67 */     super(aResponder, aTitle, aQuestion, 5, aTarget);
/*  68 */     this.filter = aFilter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  77 */     setAnswer(answers);
/*     */ 
/*     */ 
/*     */     
/*  81 */     String val = getAnswer().getProperty("filterme");
/*  82 */     if (val != null && val.equals("true")) {
/*     */ 
/*     */       
/*  85 */       val = getAnswer().getProperty("filtertext");
/*  86 */       if (val == null || val.length() == 0)
/*  87 */         val = "*"; 
/*  88 */       ItemCreationQuestion icq = new ItemCreationQuestion(getResponder(), this.title, this.question, this.target, val);
/*     */       
/*  90 */       icq.sendQuestion();
/*     */     } else {
/*     */       
/*  93 */       QuestionParser.parseItemCreationQuestion(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 104 */     int height = 225;
/* 105 */     this.itemplates = new LinkedList<>();
/* 106 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/* 107 */     buf.append("harray{label{text=\"List shows name -material\"}}");
/* 108 */     ItemTemplate[] templates = ItemTemplateFactory.getInstance().getTemplates();
/*     */     
/* 110 */     Arrays.sort((Object[])templates);
/*     */     
/* 112 */     for (int j = 0; j < templates.length; j++) {
/*     */       
/* 114 */       if (!templates[j].isNoCreate() && (
/* 115 */         getResponder().getPower() == 5 || (!(templates[j]).unique && 
/*     */         
/* 117 */         !templates[j].isPuppet() && templates[j]
/* 118 */         .getTemplateId() != 175 && templates[j]
/* 119 */         .getTemplateId() != 654 && templates[j]
/* 120 */         .getTemplateId() != 738 && templates[j]
/* 121 */         .getTemplateId() != 972 && templates[j]
/* 122 */         .getTemplateId() != 1032 && templates[j]
/* 123 */         .getTemplateId() != 1297 && templates[j]
/* 124 */         .getTemplateId() != 1437 && !(templates[j]).isRoyal && 
/*     */         
/* 126 */         !templates[j].isUnstableRift())))
/*     */       {
/*     */         
/* 129 */         if (getResponder().getPower() >= 2 || templates[j].getTemplateId() == 781 || (templates[j]
/* 130 */           .isBulk() && !templates[j].isFood() && templates[j]
/* 131 */           .getTemplateId() != 683 && templates[j]
/* 132 */           .getTemplateId() != 737 && templates[j]
/* 133 */           .getTemplateId() != 175 && templates[j]
/* 134 */           .getTemplateId() != 654 && templates[j]
/* 135 */           .getTemplateId() != 738 && templates[j]
/* 136 */           .getTemplateId() != 972 && templates[j]
/* 137 */           .getTemplateId() != 1032))
/*     */         {
/*     */           
/* 140 */           if (PlayerInfoFactory.wildCardMatch(templates[j].getName().toLowerCase(), this.filter.toLowerCase()))
/*     */           {
/* 142 */             this.itemplates.add(templates[j]);
/*     */           }
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 148 */     if (this.itemplates.size() != 1) {
/* 149 */       this.itemplates.add(0, null);
/*     */     }
/*     */     
/* 152 */     buf.append("harray{label{text=\"Item\"};dropdown{id=\"data1\";options=\"");
/* 153 */     for (int i = 0; i < this.itemplates.size(); i++) {
/*     */       
/* 155 */       if (i > 0)
/* 156 */         buf.append(","); 
/* 157 */       ItemTemplate tp = this.itemplates.get(i);
/* 158 */       if (tp == null) {
/* 159 */         buf.append("Nothing");
/* 160 */       } else if (tp.isMetal() || tp.isWood() || tp.isOre || tp.isShard) {
/* 161 */         buf.append(tp.getName() + " - " + tp.sizeString + Item.getMaterialString(tp.getMaterial()) + " ");
/* 162 */       } else if (tp.bowUnstringed) {
/* 163 */         buf.append(tp.getName() + " - " + tp.sizeString + " [unstringed]");
/*     */       } else {
/* 165 */         buf.append(tp.getName() + (tp.sizeString.isEmpty() ? "" : (" - " + tp.sizeString)));
/*     */       } 
/* 167 */     }  buf.append("\"}}");
/*     */     
/* 169 */     buf.append("harray{button{text=\"Filter list\";id=\"filterme\"};label{text=\" using \"};input{maxchars=\"30\";id=\"filtertext\";text=\"" + this.filter + "\";onenter=\"filterme\"}}");
/*     */     
/* 171 */     buf.append("harray{label{text=\"Material\"};dropdown{id=\"material\";options=\"");
/* 172 */     for (int x = 0; x <= 96; x++) {
/*     */       
/* 174 */       if (x == 0) {
/* 175 */         buf.append("standard");
/*     */       } else {
/*     */         
/* 178 */         buf.append(",");
/* 179 */         buf.append(Item.getMaterialString((byte)x));
/*     */       } 
/*     */     } 
/* 182 */     buf.append("\"}");
/* 183 */     if (Servers.isThisATestServer() && getResponder().getPower() > 2) {
/*     */       
/* 185 */       buf.append("label{text=\"   \"}");
/* 186 */       buf.append("checkbox{id=\"alltypes\";text=\"All Types \";selected=\"false\";hover=\"If qty is 1 and standard material, makes one of each normal material type\"}");
/*     */     } 
/* 188 */     buf.append("}");
/* 189 */     buf.append("harray{label{text=\"Number of items   \"};input{maxchars=\"3\"; id=\"number\"; text=\"1\"}}");
/* 190 */     buf.append("harray{label{text=\"Item qualitylevel \"};input{maxchars=\"2\"; id=\"data2\"; text=\"1\"}}");
/*     */     
/* 192 */     buf.append("harray{label{text=\"Custom size mod (float.eg. 0.3)\"};input{maxchars=\"4\"; id=\"sizemod\"; text=\"\"}}");
/*     */ 
/*     */     
/* 195 */     if (getResponder().getPower() >= 4) {
/*     */       
/* 197 */       buf.append("table{rows=\"1\";cols=\"8\";");
/* 198 */       buf.append("radio{group=\"rare\";id=\"0\";selected=\"true\"};label{text=\"Common\"};");
/* 199 */       buf.append("radio{group=\"rare\";id=\"1\"};label{text=\"Rare\"};");
/* 200 */       buf.append("radio{group=\"rare\";id=\"2\"};label{text=\"Supreme\"};");
/* 201 */       buf.append("radio{group=\"rare\";id=\"3\"};label{text=\"Fantastic\"};");
/* 202 */       buf.append("}");
/* 203 */       buf.append("harray{label{text='Item Actual Name';hover=\"leave blank to use its base name\"};input{id='itemName'; maxchars='60'; text=''}}");
/*     */ 
/*     */       
/* 206 */       buf.append("harray{label{text=\"Colour:\";hover=\"leave blank to use default\"};label{text='R'};input{id='c_red'; maxchars='3'; text=''}label{text='G'};input{id='c_green'; maxchars='3'; text=''}label{text='B'};input{id='c_blue'; maxchars='3'; text=''}}");
/*     */ 
/*     */ 
/*     */       
/* 210 */       height += 50;
/*     */     }
/*     */     else {
/*     */       
/* 214 */       buf.append("passthrough{id=\"rare\";text=\"0\"}");
/*     */     } 
/*     */     
/* 217 */     buf.append(createAnswerButton2());
/* 218 */     getResponder().getCommunicator().sendBml(250, height, true, true, buf.toString(), 200, 200, 200, this.title);
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
/*     */   ItemTemplate getTemplate(int aTemplateId) {
/* 230 */     return this.itemplates.get(aTemplateId);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\ItemCreationQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */