/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.items.ItemTemplate;
/*     */ import com.wurmonline.server.items.ItemTemplateFactory;
/*     */ import com.wurmonline.server.items.NoSuchTemplateException;
/*     */ import com.wurmonline.shared.util.MaterialUtilities;
/*     */ import java.util.Date;
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
/*     */ public abstract class Question
/*     */   implements MiscConstants, QuestionTypes
/*     */ {
/*  36 */   private static int ids = 0;
/*     */ 
/*     */   
/*     */   protected final int id;
/*     */ 
/*     */   
/*     */   private Creature responder;
/*     */   
/*     */   private final boolean answered = false;
/*     */   
/*  46 */   private final String html = "";
/*     */ 
/*     */ 
/*     */   
/*     */   final String title;
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String question;
/*     */ 
/*     */   
/*     */   final int type;
/*     */ 
/*     */   
/*     */   final long target;
/*     */ 
/*     */   
/*  63 */   private final long sent = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */   
/*  67 */   private Properties answer = null;
/*     */   
/*  69 */   protected static final Logger logger = Logger.getLogger(Question.class.getName());
/*     */   
/*     */   static final String CHECKED = "CHECKED";
/*     */   static final String CHECKED2 = ";selected='true'";
/*  73 */   public int windowSizeX = 600;
/*  74 */   public int windowSizeY = 400;
/*     */   
/*     */   static final String colourCommon = "";
/*     */   
/*     */   static final String colourRare = "color=\"66,153,225\";";
/*     */   
/*     */   static final String colourSupreme = "color=\"0,255,255\";";
/*     */   
/*     */   static final String colourFantastic = "color=\"255,0,255\";";
/*     */ 
/*     */   
/*     */   Question(Creature aResponder, String aTitle, String aQuestion, int aType, long aTarget) {
/*  86 */     this.id = ids++;
/*  87 */     this.responder = aResponder;
/*  88 */     this.title = aTitle;
/*  89 */     this.question = aQuestion;
/*  90 */     this.type = aType;
/*  91 */     this.target = aTarget;
/*  92 */     Questions.addQuestion(this);
/*  93 */     if (logger.isLoggable(Level.FINEST))
/*     */     {
/*  95 */       logger.finest("Created " + this);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public final long getSendTime() {
/* 101 */     return this.sent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getId() {
/* 110 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final long getTarget() {
/* 119 */     return this.target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getType() {
/* 128 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Creature getResponder() {
/* 137 */     return this.responder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clearResponder() {
/* 145 */     this.responder = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isAnswered() {
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getTitle() {
/* 163 */     return this.title;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String getQuestion() {
/* 172 */     return this.question;
/*     */   }
/*     */ 
/*     */   
/*     */   final String getHtmlHeader() {
/* 177 */     return "<B>" + this.question + "</B><FORM action=\"\" method=\"post\"><INPUT TYPE=\"hidden\" name=\"id\" value=\"" + this.id + "\"><BR>";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String getBmlHeader() {
/* 184 */     return "border{center{text{type='bold';text=\"" + this.question + "\"}};null;scroll{vertical=\"true\";horizontal=\"false\";varray{rescale=\"true\";passthrough{id=\"id\";text=\"" + this.id + "\"}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String getBmlHeaderNoQuestion() {
/* 193 */     return "border{null;null;scroll{vertical='true';horizontal='false';varray{rescale='true';passthrough{id='id';text='" + this.id + "'}";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   final String getBmlHeaderWithScroll() {
/* 199 */     return "border{scroll{vertical='true';horizontal='true';varray{rescale='false';passthrough{id='id';text='" + this.id + "'}";
/*     */   }
/*     */ 
/*     */   
/*     */   final String getBmlHeaderWithScrollAndQuestion() {
/* 204 */     return "border{center{text{type='bold';text=\"" + this.question + "\"}};null;scroll{vertical=\"true\";horizontal=\"true\";varray{rescale=\"false\";passthrough{id=\"id\";text=\"" + this.id + "\"}";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final String getBmlHeaderScrollOnly() {
/* 212 */     return "scroll{vertical='true';horizontal='true';varray{rescale='false';passthrough{id='id';text='" + this.id + "'}";
/*     */   }
/*     */ 
/*     */   
/*     */   final String createAnswerButtonForNoBorder() {
/* 217 */     return "harray {button{text='Send';id='submit'}}};}";
/*     */   }
/*     */ 
/*     */   
/*     */   final String createAnswerButton() {
/* 222 */     return "<INPUT type=\"submit\" VALUE=\"Send\">";
/*     */   }
/*     */ 
/*     */   
/*     */   final String createAnswerButton2() {
/* 227 */     return createAnswerButton2("Send");
/*     */   }
/*     */ 
/*     */   
/*     */   final String createAnswerButton2(String sendText) {
/* 232 */     return "harray {button{text='" + sendText + "';id='submit'}}}};null;null;}";
/*     */   }
/*     */ 
/*     */   
/*     */   final String createOkAnswerButton() {
/* 237 */     return "harray {button{text='Ok';id='submit'}}}};null;null;}";
/*     */   }
/*     */ 
/*     */   
/*     */   final String createBackAnswerButton() {
/* 242 */     return "harray {button{text='Send';id='submit'};label{text=' ';id='spacedlxg'};button{text='Previous';id='back'}}}};null;null;}";
/*     */   }
/*     */ 
/*     */   
/*     */   final String createSurveyPerimeterButton() {
/* 247 */     return "harray {button{text='Survey Perimeter';id='submit'};label{text=' ';id='spacedlxg'};button{text='Go Back';id='back'}}}};null;null;}";
/*     */   }
/*     */ 
/*     */   
/*     */   final String createSurveyButton() {
/* 252 */     return "harray {button{text='Cancel';id='cancel'};label{text=' ';id='spacedlxg'};button{text='Survey Area';id='submit'}}}};null;null;}";
/*     */   }
/*     */ 
/*     */   
/*     */   final String createAnswerButton3() {
/* 257 */     return "harray {button{text='Send';id='submit'}}}};null;null;null;null;}";
/*     */   }
/*     */ 
/*     */   
/*     */   final byte[] getHtmlAsBytes() {
/* 262 */     return "".getBytes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setAnswer(Properties aAnswer) {
/* 273 */     this.answer = aAnswer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   final Properties getAnswer() {
/* 282 */     return this.answer;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void answer(Properties paramProperties);
/*     */ 
/*     */   
/*     */   public abstract void sendQuestion();
/*     */ 
/*     */   
/*     */   public final String toString() {
/* 293 */     return "Question [id: " + this.id + ", type: " + this.type + ", target: " + this.target + ", sent: " + new Date(this.sent) + ", title: " + this.title + ']';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void timedOut() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String itemNameWithColorByRarity(Item litem) {
/* 310 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 312 */     String star = "";
/* 313 */     if ((litem.getItemsAsArray()).length > 0) {
/* 314 */       star = "* ";
/*     */     }
/* 316 */     if (litem.isBulkItem()) {
/*     */       
/* 318 */       int nums = litem.getBulkNums();
/*     */       
/*     */       try {
/* 321 */         ItemTemplate it = ItemTemplateFactory.getInstance().getTemplate(litem.getRealTemplateId());
/*     */         
/* 323 */         sb.append(it.sizeString);
/*     */         
/* 325 */         if (nums > 1) {
/* 326 */           MaterialUtilities.appendNameWithMaterialSuffix(sb, it.getPlural(), litem.getMaterial());
/*     */         } else {
/* 328 */           MaterialUtilities.appendNameWithMaterialSuffix(sb, it.getName(), litem.getMaterial());
/*     */         } 
/* 330 */       } catch (NoSuchTemplateException nst) {
/*     */         
/* 332 */         logger.log(Level.WARNING, litem.getWurmId() + " bulk nums=" + nums + " but template is " + litem
/* 333 */             .getBulkTemplateId());
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 338 */       String str = (litem.getName().length() == 0) ? litem.getTemplate().getName() : litem.getName();
/* 339 */       MaterialUtilities.appendNameWithMaterialSuffix(sb, str, litem.getMaterial());
/*     */     } 
/*     */     
/* 342 */     if (litem.getDescription().length() > 0) {
/* 343 */       sb.append(" (" + litem.getDescription() + ")");
/*     */     }
/* 345 */     String name = sb.toString();
/* 346 */     return nameColoredByRarity(name, star, litem.getRarity(), false);
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
/*     */   public static String nameColoredByRarity(String name, String star, byte rarity, boolean isBold) {
/* 358 */     String bold = isBold ? "type=\"bold\";" : "";
/* 359 */     switch (rarity) {
/*     */       
/*     */       case 1:
/* 362 */         return "label{" + bold + "color=\"66,153,225\";" + "text=\"" + star + "rare " + name.replace("\"", "''") + "\"};";
/*     */       case 2:
/* 364 */         return "label{" + bold + "color=\"0,255,255\";" + "text=\"" + star + "supreme " + name.replace("\"", "''") + "\"};";
/*     */       case 3:
/* 366 */         return "label{" + bold + "color=\"255,0,255\";" + "text=\"" + star + "fantastic " + name.replace("\"", "''") + "\"};";
/*     */     } 
/* 368 */     return "label{" + bold + "" + "text=\"" + star + name.replace("\"", "''") + "\"};";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean getBooleanProp(String name) {
/* 374 */     String bool = this.answer.getProperty(name);
/* 375 */     return (bool != null && bool.equalsIgnoreCase("true"));
/*     */   }
/*     */ 
/*     */   
/*     */   String getStringProp(String name) {
/* 380 */     String string = this.answer.getProperty(name);
/* 381 */     if (string == null)
/* 382 */       return ""; 
/* 383 */     return string;
/*     */   }
/*     */ 
/*     */   
/*     */   String colHeader(String aName, int numb, int sortBy) {
/* 388 */     StringBuilder buf = new StringBuilder();
/* 389 */     buf.append("button{text=\"" + aName + ((sortBy == numb) ? " /\\" : ((sortBy == -numb) ? " \\/" : "")) + "\";id=\"sort" + ((sortBy == numb) ? ("-" + numb) : ("" + numb)) + "\"};");
/*     */ 
/*     */ 
/*     */     
/* 393 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\Question.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */