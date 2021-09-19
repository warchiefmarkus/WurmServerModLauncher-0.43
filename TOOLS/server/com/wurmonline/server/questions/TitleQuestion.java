/*     */ package com.wurmonline.server.questions;
/*     */ 
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
/*     */ public final class TitleQuestion
/*     */   extends Question
/*     */ {
/*  41 */   private final List<Titles.Title> titlelist = new LinkedList<>();
/*     */ 
/*     */   
/*     */   public TitleQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  45 */     super(aResponder, aTitle, aQuestion, 39, aTarget);
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
/*  56 */     setAnswer(answers);
/*  57 */     QuestionParser.parseTitleQuestion(this);
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
/*  68 */     StringBuilder sb = new StringBuilder(getBmlHeader());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  76 */     final boolean isMale = ((Player)getResponder()).isNotFemale();
/*  77 */     Titles.Title[] titles = ((Player)getResponder()).getTitles();
/*  78 */     Arrays.sort(titles, new Comparator<Titles.Title>()
/*     */         {
/*     */           
/*     */           public int compare(Titles.Title t1, Titles.Title t2)
/*     */           {
/*  83 */             return t1.getName(isMale).compareTo(t2.getName(isMale));
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  88 */     String suff = "";
/*  89 */     String pre = "";
/*  90 */     if (!getResponder().hasFlag(24))
/*  91 */       pre = getResponder().getAbilityTitle(); 
/*  92 */     if (getResponder().getCultist() != null && !getResponder().hasFlag(25))
/*  93 */       suff = suff + " " + getResponder().getCultist().getCultistTitleShort(); 
/*  94 */     Titles.Title lTempTitle = getResponder().getTitle();
/*  95 */     if (getResponder().isKing())
/*  96 */       suff = suff + " [" + King.getRulerTitle((getResponder().getSex() == 0), getResponder().getKingdomId()) + "]"; 
/*  97 */     if (lTempTitle != null)
/*     */     {
/*  99 */       if (lTempTitle.isRoyalTitle()) {
/*     */         
/* 101 */         if (getResponder().getAppointments() != 0L || getResponder().isAppointed()) {
/* 102 */           suff = suff + " [" + getResponder().getKingdomTitle() + "]";
/*     */         }
/*     */       } else {
/* 105 */         suff = suff + " [" + lTempTitle.getName(getResponder().isNotFemale()) + "]";
/*     */       }  } 
/* 107 */     if (getResponder().isChampion() && getResponder().getDeity() != null) {
/* 108 */       suff = suff + " [Champion of " + (getResponder().getDeity()).name + "]";
/*     */     }
/* 110 */     String playerName = pre + StringUtilities.raiseFirstLetterOnly(getResponder().getName()) + suff;
/* 111 */     sb.append("text{text=\"You are currently known as: " + playerName + "\"}");
/* 112 */     sb.append("text{text=\"\"}");
/*     */     
/* 114 */     Titles.Title currentTitle = ((Player)getResponder()).getTitle();
/* 115 */     int totalTitles = 0;
/* 116 */     if (titles.length == 0) {
/*     */       
/* 118 */       if (getResponder().getAppointments() != 0L || getResponder().isAppointed()) {
/*     */         
/* 120 */         int defaultTitle = 0;
/* 121 */         sb.append("harray{text{text=\"Title: \"};dropdown{id=\"TITLE\";options=\"None");
/*     */         
/* 123 */         sb.append(",");
/* 124 */         sb.append(Titles.Title.Kingdomtitle.getName(getResponder().isNotFemale()));
/* 125 */         this.titlelist.add(Titles.Title.Kingdomtitle);
/* 126 */         if (currentTitle != null)
/*     */         {
/* 128 */           if (currentTitle.isRoyalTitle())
/* 129 */             defaultTitle = 1; 
/*     */         }
/* 131 */         sb.append("\";default=\"" + defaultTitle + "\"}}");
/* 132 */         totalTitles++;
/*     */       } else {
/*     */         
/* 135 */         sb.append("text{text=\"You have no titles to select from.\"}");
/*     */       } 
/*     */     } else {
/*     */       
/* 139 */       int defaultTitle = 0;
/*     */       
/* 141 */       sb.append("harray{text{text=\"Title: \"};dropdown{id=\"TITLE\";options=\"None");
/*     */       
/* 143 */       for (int x = 0; x < titles.length; x++) {
/*     */         
/* 145 */         sb.append(",");
/*     */         
/* 147 */         sb.append(titles[x].getName(getResponder().isNotFemale()));
/* 148 */         if (currentTitle != null)
/*     */         {
/* 150 */           if ((titles[x]).id == currentTitle.id)
/* 151 */             defaultTitle = x + 1; 
/*     */         }
/* 153 */         this.titlelist.add(titles[x]);
/* 154 */         totalTitles++;
/*     */       } 
/* 156 */       if (getResponder().getAppointments() != 0L || getResponder().isAppointed()) {
/*     */         
/* 158 */         sb.append(",");
/* 159 */         sb.append(Titles.Title.Kingdomtitle.getName(getResponder().isNotFemale()));
/* 160 */         this.titlelist.add(Titles.Title.Kingdomtitle);
/* 161 */         if (currentTitle != null)
/*     */         {
/* 163 */           if (currentTitle.isRoyalTitle())
/* 164 */             defaultTitle = titles.length + 1; 
/*     */         }
/* 166 */         totalTitles++;
/*     */       } 
/* 168 */       sb.append("\";default=\"" + defaultTitle + "\"}}");
/* 169 */       sb.append("text{text=\"\"}");
/* 170 */       sb.append("text{text=\"You have a total of " + totalTitles + " titles.\"}");
/* 171 */       sb.append("text{text=\"\"}");
/* 172 */       sb.append("text{type=\"italic\";text=\"Note: Armour smiths that use their title gets faster armour improvement rate.\"}");
/*     */     } 
/* 174 */     String occultist = getResponder().getAbilityTitle();
/* 175 */     String meditation = (getResponder().getCultist() != null) ? Cults.getNameForLevel(getResponder().getCultist().getPath(), getResponder().getCultist().getLevel()) : "";
/* 176 */     if (occultist.length() > 0 || meditation.length() > 0) {
/*     */       
/* 178 */       sb.append("text{type=\"bold\";text=\"Select which titles to hide (if any)\"}");
/* 179 */       if (occultist.length() > 0)
/* 180 */         sb.append("checkbox{id=\"hideoccultist\";text=\"" + occultist + "(Occultist)\";selected=\"" + getResponder().hasFlag(24) + "\"}"); 
/* 181 */       if (meditation.length() > 0)
/* 182 */         sb.append("checkbox{id=\"hidemeditation\";text=\"" + meditation + " (Meditation)\";selected=\"" + getResponder().hasFlag(25) + "\"}"); 
/* 183 */       sb.append("text{text=\"\"}");
/*     */     } 
/*     */     
/* 186 */     if (Servers.isThisAPvpServer()) {
/*     */       
/* 188 */       King king = King.getKing(getResponder().getKingdomId());
/* 189 */       if (king != null && (getResponder().getAppointments() != 0L || getResponder().isAppointed())) {
/*     */         
/* 191 */         sb.append("text{type=\"bold\";text=\"Select which kingdom office to remove (if any)\"}");
/* 192 */         Appointments a = Appointments.getAppointments(king.era);
/* 193 */         for (int x = 0; x < a.officials.length; x++) {
/*     */           
/* 195 */           int oId = x + 1500;
/* 196 */           Appointment o = a.getAppointment(oId);
/* 197 */           if (a.officials[x] == getResponder().getWurmId())
/*     */           {
/* 199 */             sb.append("checkbox{id=\"office" + oId + "\";text=\"" + o.getNameForGender(getResponder().getSex()) + " (Office)\";}");
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 206 */     sb.append(createAnswerButton2());
/* 207 */     getResponder().getCommunicator().sendBml(500, 300, true, true, sb.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Titles.Title getTitle(int aPosition) {
/* 217 */     return this.titlelist.get(aPosition);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TitleQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */