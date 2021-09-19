/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.creatures.CreatureTemplate;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateFactory;
/*     */ import com.wurmonline.server.creatures.CreatureTemplateIds;
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
/*     */ public final class CreatureCreationQuestion
/*     */   extends Question
/*     */   implements CreatureTemplateIds
/*     */ {
/*  35 */   private final LinkedList<CreatureTemplate> cretemplates = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final int tilex;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int tiley;
/*     */ 
/*     */ 
/*     */   
/*     */   private final long structureId;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int layer;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CreatureCreationQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget, int aTilex, int aTiley, int aLayer, long structureId) {
/*  58 */     super(aResponder, aTitle, aQuestion, 6, aTarget);
/*  59 */     this.tilex = aTilex;
/*  60 */     this.tiley = aTiley;
/*  61 */     this.layer = aLayer;
/*  62 */     this.structureId = structureId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTileX() {
/*  67 */     return this.tilex;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTileY() {
/*  72 */     return this.tiley;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLayer() {
/*  81 */     return this.layer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  92 */     setAnswer(answers);
/*  93 */     QuestionParser.parseCreatureCreationQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 104 */     StringBuilder buf = new StringBuilder();
/* 105 */     buf.append(getBmlHeader());
/* 106 */     buf.append("harray{label{text='Player name:'}input{id='pname';maxchars='30'}}");
/* 107 */     buf.append("harray{label{text='Or, if left empty:'}");
/*     */     
/* 109 */     CreatureTemplate[] templates = CreatureTemplateFactory.getInstance().getTemplates();
/*     */ 
/*     */     
/* 112 */     Arrays.sort((Object[])templates);
/* 113 */     buf.append("dropdown{id='data1';options=\"");
/* 114 */     for (int x = 0; x < templates.length; x++) {
/*     */       
/* 116 */       if (!templates[x].isUnique() || 
/* 117 */         getResponder().getPower() >= 3 || Servers.isThisATestServer()) {
/*     */         
/* 119 */         if (this.cretemplates.size() > 0) {
/* 120 */           buf.append(",");
/*     */         }
/* 122 */         if (templates[x].getTemplateId() != 119) {
/*     */           
/* 124 */           this.cretemplates.add(templates[x]);
/* 125 */           buf.append(templates[x].getName());
/*     */         } 
/*     */       } 
/*     */     } 
/* 129 */     buf.append("\"}}");
/* 130 */     buf.append("table{rows=\"1\";cols=\"3\";");
/* 131 */     buf.append("text{type=\"bold\";text=\"Gender\"};radio{group=\"gender\";id=\"female\";text=\"Female\"};radio{group=\"gender\";id=\"male\";text=\"Male\";selected=\"true\"};}");
/*     */ 
/*     */     
/* 134 */     buf.append("harray{label{text='Age(2..100):'};input{id='age';maxchars='3';text='0'};label{text=\" 0 = random age\"}}");
/* 135 */     buf.append("harray{label{text='Number of creatures:'};input{id='number';maxchars='4';text='1'}}");
/* 136 */     buf.append("harray{label{text='Name (instead of template name):'};input{id='cname';maxchars='40';text=''}}");
/* 137 */     buf.append("harray{text{type=\"bold\";text=\"Type\"};label{text=\"(ignored if not applicable!)\"};};");
/* 138 */     buf.append("table{rows=\"1\";cols=\"2\";");
/* 139 */     buf.append("radio{group=\"tid\";id=\"0\";selected=\"true\"};label{text=\"None\"};}");
/*     */     
/* 141 */     buf.append("table{rows=\"3\";cols=\"8\";");
/* 142 */     buf.append("radio{group=\"tid\";id=\"5\"};label{text=\"Alert\"};");
/* 143 */     buf.append("radio{group=\"tid\";id=\"11\"};label{text=\"Diseased\"};");
/* 144 */     buf.append("radio{group=\"tid\";id=\"9\"};label{text=\"Hardened\"};");
/* 145 */     buf.append("radio{group=\"tid\";id=\"10\"};label{text=\"Scared\"};");
/*     */     
/* 147 */     buf.append("radio{group=\"tid\";id=\"2\"};label{text=\"Angry\"};");
/* 148 */     buf.append("radio{group=\"tid\";id=\"1\"};label{text=\"Fierce\"};");
/* 149 */     buf.append("radio{group=\"tid\";id=\"7\"};label{text=\"Lurking\"};");
/* 150 */     buf.append("radio{group=\"tid\";id=\"4\"};label{text=\"Slow\"};");
/*     */     
/* 152 */     buf.append("radio{group=\"tid\";id=\"99\"};label{text=\"Champion\"};");
/* 153 */     buf.append("radio{group=\"tid\";id=\"6\"};label{text=\"Greenish\"};");
/* 154 */     buf.append("radio{group=\"tid\";id=\"3\"};label{text=\"Raging\"};");
/* 155 */     buf.append("radio{group=\"tid\";id=\"8\"};label{text=\"Sly\"};");
/*     */     
/* 157 */     buf.append("}");
/*     */     
/* 159 */     buf.append(createAnswerButton2());
/*     */     
/* 161 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
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
/*     */   CreatureTemplate getTemplate(int aTemplateId) {
/* 173 */     return this.cretemplates.get(aTemplateId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStructureId() {
/* 181 */     return this.structureId;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\CreatureCreationQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */