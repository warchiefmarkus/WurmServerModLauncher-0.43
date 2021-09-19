/*     */ package com.wurmonline.server.skills;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
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
/*     */ public class SkillTop5
/*     */ {
/*     */   private static final String GET_TOP_5 = "SELECT S.NUMBER, S.OWNER, P.NAME, S.VALUE FROM WURMPLAYERS.SKILLS S JOIN WURMPLAYERS.PLAYERS P ON S.OWNER=P.WURMID WHERE S.NUMBER=? AND P.POWER=0 ORDER BY S.VALUE DESC LIMIT 5;";
/*  43 */   private static final int[] skills = new int[] { 1, 104, 103, 102, 2, 100, 101, 3, 106, 105, 1021, 10042, 10069, 10095, 1030, 10081, 10080, 10079, 1003, 10003, 10025, 10024, 10001, 1005, 1031, 10044, 1032, 10082, 10051, 10073, 1025, 10064, 10036, 1018, 10039, 10083, 10059, 10037, 10038, 1009, 1023, 10053, 10054, 10055, 10058, 10057, 10052, 1010, 1027, 10070, 1024, 10056, 1001, 10029, 10007, 1013, 10074, 1004, 10061, 10062, 10063, 10040, 1008, 1020, 10026, 10009, 10004, 10035, 10008, 10047, 10002, 10046, 10030, 1019, 10078, 10085, 10072, 10049, 10033, 10071, 10048, 10045, 10086, 10060, 10091, 10031, 1033, 10089, 10088, 10090, 1011, 10032, 1026, 10067, 10068, 10066, 10065, 1014, 1002, 10023, 10021, 10006, 10020, 10022, 10019, 1015, 1017, 10012, 10013, 10014, 10015, 10043, 10034, 10041, 1016, 10010, 10011, 1000, 10005, 10027, 10028, 1012, 10016, 10017, 10092, 1028, 10076, 10075, 10084, 1022, 10087, 10050, 10018, 1029, 10093, 10077, 10094, 1007 };
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
/*     */   private final int number;
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
/*     */   private final int pos;
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
/*     */   private final long owner;
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
/*     */   private final String ownerName;
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
/*     */   private final double value;
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
/*     */   public SkillTop5(int aNumber, int aPos, long aOwner, String aOwnerName, double aValue) {
/* 117 */     this.number = aNumber;
/* 118 */     this.pos = aPos;
/* 119 */     this.owner = aOwner;
/* 120 */     this.ownerName = aOwnerName;
/* 121 */     this.value = aValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumber() {
/* 126 */     return this.number;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPos() {
/* 131 */     return this.pos;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getOwner() {
/* 136 */     return this.owner;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOwnerName() {
/* 141 */     return this.ownerName;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getValue() {
/* 146 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public static SkillTop5[][] getAllSkillsTop5() throws Exception {
/* 151 */     SkillTop5[][] skillsTop5 = new SkillTop5[skills.length][5];
/* 152 */     Connection dbcon = null;
/* 153 */     PreparedStatement ps = null;
/* 154 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 157 */       dbcon = DbConnector.getPlayerDbCon();
/* 158 */       ps = dbcon.prepareStatement("SELECT S.NUMBER, S.OWNER, P.NAME, S.VALUE FROM WURMPLAYERS.SKILLS S JOIN WURMPLAYERS.PLAYERS P ON S.OWNER=P.WURMID WHERE S.NUMBER=? AND P.POWER=0 ORDER BY S.VALUE DESC LIMIT 5;");
/*     */       
/* 160 */       for (int i = 0; i < skills.length; i++)
/*     */       {
/* 162 */         ps.setInt(1, skills[i]);
/* 163 */         rs = ps.executeQuery();
/* 164 */         int count = 0;
/*     */         
/* 166 */         while (rs.next() && count < 5) {
/*     */ 
/*     */           
/* 169 */           SkillTop5 skill = new SkillTop5(rs.getInt("NUMBER"), count, rs.getLong("OWNER"), rs.getString("NAME"), rs.getDouble("VALUE"));
/* 170 */           skillsTop5[i][count] = skill;
/* 171 */           count++;
/*     */         } 
/* 173 */         DbUtilities.closeDatabaseObjects(null, rs);
/*     */       }
/*     */     
/*     */     } finally {
/*     */       
/* 178 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 179 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/* 181 */     return skillsTop5;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\skills\SkillTop5.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */