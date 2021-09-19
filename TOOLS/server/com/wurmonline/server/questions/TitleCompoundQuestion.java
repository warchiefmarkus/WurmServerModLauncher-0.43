/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.Appointment;
/*     */ import com.wurmonline.server.kingdom.Appointments;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.players.Cults;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.Titles;
/*     */ import com.wurmonline.shared.util.StringUtilities;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import javax.annotation.Nullable;
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
/*     */ public final class TitleCompoundQuestion
/*     */   extends Question
/*     */ {
/*  38 */   private final List<Titles.Title> firstTitleList = new LinkedList<>();
/*  39 */   private final List<Titles.Title> secondTitleList = new LinkedList<>();
/*  40 */   private int totalTitles = 0;
/*     */ 
/*     */   
/*     */   public TitleCompoundQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  44 */     super(aResponder, aTitle, aQuestion, 39, aTarget);
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
/*  55 */     logger.info(String.format("%s answered question.", new Object[] { getResponder().getName() }));
/*  56 */     setAnswer(answers);
/*  57 */     QuestionParser.parseTitleCompoundQuestion(this);
/*     */   }
/*     */   
/*     */   private StringBuilder getKingdomTitleBox(Titles.Title[] titles, String key, List<Titles.Title> titleList, @Nullable Titles.Title currentTitle) {
/*  61 */     StringBuilder sb = new StringBuilder();
/*  62 */     this.totalTitles = 0;
/*  63 */     if (titles.length == 0) {
/*     */       
/*  65 */       if (getResponder().getAppointments() != 0L || getResponder().isAppointed()) {
/*     */         
/*  67 */         int defaultTitle = 0;
/*  68 */         sb.append("harray{text{text=\"" + key + ": \"};dropdown{id=\"" + key + "\";options=\"None");
/*     */         
/*  70 */         sb.append(",");
/*  71 */         sb.append(Titles.Title.Kingdomtitle.getName(getResponder().isNotFemale()));
/*  72 */         titleList.add(Titles.Title.Kingdomtitle);
/*  73 */         if (currentTitle != null)
/*     */         {
/*  75 */           if (currentTitle.isRoyalTitle())
/*  76 */             defaultTitle = 1; 
/*     */         }
/*  78 */         sb.append("\";default=\"" + defaultTitle + "\"}}");
/*  79 */         this.totalTitles++;
/*     */       } else {
/*     */         
/*  82 */         sb.append("text{text=\"You have no titles to select from.\"}");
/*     */       } 
/*     */     } else {
/*     */       
/*  86 */       int defaultTitle = 0;
/*     */       
/*  88 */       sb.append("harray{text{text=\"" + key + ": \"};dropdown{id=\"" + key + "\";options=\"None");
/*     */       
/*  90 */       for (int x = 0; x < titles.length; x++) {
/*     */         
/*  92 */         sb.append(",");
/*     */         
/*  94 */         sb.append(titles[x].getName(getResponder().isNotFemale()));
/*  95 */         if (currentTitle != null)
/*     */         {
/*  97 */           if ((titles[x]).id == currentTitle.id)
/*  98 */             defaultTitle = x + 1; 
/*     */         }
/* 100 */         titleList.add(titles[x]);
/* 101 */         this.totalTitles++;
/*     */       } 
/* 103 */       if (getResponder().getAppointments() != 0L || getResponder().isAppointed()) {
/*     */         
/* 105 */         sb.append(",");
/* 106 */         sb.append(Titles.Title.Kingdomtitle.getName(getResponder().isNotFemale()));
/* 107 */         titleList.add(Titles.Title.Kingdomtitle);
/* 108 */         if (currentTitle != null)
/*     */         {
/* 110 */           if (currentTitle.isRoyalTitle())
/* 111 */             defaultTitle = titles.length + 1; 
/*     */         }
/* 113 */         this.totalTitles++;
/*     */       } 
/* 115 */       sb.append("\";default=\"" + defaultTitle + "\"}}");
/*     */     } 
/* 117 */     return sb;
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
/* 128 */     StringBuilder sb = new StringBuilder(getBmlHeader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     final boolean isMale = ((Player)getResponder()).isNotFemale();
/* 137 */     Titles.Title[] titles = ((Player)getResponder()).getTitles();
/* 138 */     Arrays.sort(titles, new Comparator<Titles.Title>()
/*     */         {
/*     */           
/*     */           public int compare(Titles.Title t1, Titles.Title t2)
/*     */           {
/* 143 */             return t1.getName(isMale).compareTo(t2.getName(isMale));
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 148 */     String suff = "";
/* 149 */     String pre = "";
/* 150 */     if (!getResponder().hasFlag(24))
/* 151 */       pre = getResponder().getAbilityTitle(); 
/* 152 */     if (getResponder().getCultist() != null && !getResponder().hasFlag(25))
/* 153 */       suff = suff + " " + getResponder().getCultist().getCultistTitleShort(); 
/* 154 */     if (getResponder().isKing())
/* 155 */       suff = suff + " [" + King.getRulerTitle((getResponder().getSex() == 0), getResponder().getKingdomId()) + "]"; 
/* 156 */     if (getResponder().getTitle() != null || (Features.Feature.COMPOUND_TITLES.isEnabled() && getResponder().getSecondTitle() != null))
/* 157 */       suff = suff + " [" + getResponder().getTitleString() + "]"; 
/* 158 */     if (getResponder().isChampion() && getResponder().getDeity() != null) {
/* 159 */       suff = suff + " [Champion of " + (getResponder().getDeity()).name + "]";
/*     */     }
/* 161 */     String playerName = pre + StringUtilities.raiseFirstLetterOnly(getResponder().getName()) + suff;
/* 162 */     sb.append("text{text=\"You are currently known as: " + playerName + "\"}");
/* 163 */     sb.append("text{text=\"\"}");
/*     */ 
/*     */     
/* 166 */     sb.append(getKingdomTitleBox(titles, "First", this.firstTitleList, getResponder().getTitle()));
/* 167 */     sb.append("text{text=\"\"}");
/* 168 */     sb.append(getKingdomTitleBox(titles, "Second", this.secondTitleList, getResponder().getSecondTitle()));
/* 169 */     sb.append("text{text=\"\"}");
/* 170 */     sb.append("text{text=\"You have a total of " + this.totalTitles + " titles.\"}");
/* 171 */     sb.append("text{text=\"\"}");
/* 172 */     sb.append("text{type=\"italic\";text=\"Note: Armour smiths that use their title gets faster armour improvement rate.\"}");
/* 173 */     String occultist = getResponder().getAbilityTitle();
/* 174 */     String meditation = (getResponder().getCultist() != null) ? Cults.getNameForLevel(getResponder().getCultist().getPath(), getResponder().getCultist().getLevel()) : "";
/* 175 */     if (occultist.length() > 0 || meditation.length() > 0) {
/*     */       
/* 177 */       sb.append("text{type=\"bold\";text=\"Select which titles to hide (if any)\"}");
/* 178 */       if (occultist.length() > 0)
/* 179 */         sb.append("checkbox{id=\"hideoccultist\";text=\"" + occultist + "(Occultist)\";selected=\"" + getResponder().hasFlag(24) + "\"}"); 
/* 180 */       if (meditation.length() > 0)
/* 181 */         sb.append("checkbox{id=\"hidemeditation\";text=\"" + meditation + " (Meditation)\";selected=\"" + getResponder().hasFlag(25) + "\"}"); 
/* 182 */       sb.append("text{text=\"\"}");
/*     */     } 
/*     */     
/* 185 */     if (Servers.isThisAPvpServer()) {
/*     */       
/* 187 */       King king = King.getKing(getResponder().getKingdomId());
/* 188 */       if (king != null && (getResponder().getAppointments() != 0L || getResponder().isAppointed())) {
/*     */         
/* 190 */         sb.append("text{type=\"bold\";text=\"Select which kingdom office to remove (if any)\"}");
/* 191 */         Appointments a = Appointments.getAppointments(king.era);
/* 192 */         for (int x = 0; x < a.officials.length; x++) {
/*     */           
/* 194 */           int oId = x + 1500;
/* 195 */           Appointment o = a.getAppointment(oId);
/* 196 */           if (a.officials[x] == getResponder().getWurmId())
/*     */           {
/* 198 */             sb.append("checkbox{id=\"office" + oId + "\";text=\"" + o.getNameForGender(getResponder().getSex()) + " (Office)\";}");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 205 */     sb.append(createAnswerButton2());
/* 206 */     getResponder().getCommunicator().sendBml(500, 300, true, true, sb.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Titles.Title getFirstTitle(int aPosition) {
/* 216 */     return this.firstTitleList.get(aPosition);
/*     */   }
/*     */   
/*     */   Titles.Title getSecondTitle(int aPosition) {
/* 220 */     return this.secondTitleList.get(aPosition);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TitleCompoundQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */