/*     */ package com.wurmonline.server.villages;
/*     */ 
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeadVillage
/*     */ {
/*   9 */   private static final String[] directions = new String[] { "north", "northeast", "east", "southeast", "south", "southwest", "west", "northwest" };
/*     */ 
/*     */   
/*  12 */   private static final String[] distances = new String[] { "very close", "nearby", "close", "far", "quite distant", "very far" };
/*     */   
/*     */   private final long deedId;
/*     */   
/*     */   private final int startX;
/*     */   
/*     */   private final int startY;
/*     */   
/*     */   private final int endX;
/*     */   
/*     */   private final int endY;
/*     */   
/*     */   private final String deedName;
/*     */   
/*     */   private final String founderName;
/*     */   private final String mayorName;
/*     */   private final long creationDate;
/*     */   private final long disbandDate;
/*     */   private final long lastLoginDate;
/*     */   private final byte kingdomId;
/*     */   
/*     */   public DeadVillage(long deedId, int startx, int starty, int endx, int endy, String name, String founder, String mayor, long creationDate, long disbandDate, long lastLogin, byte kingdom) {
/*  34 */     this.deedId = deedId;
/*  35 */     this.startX = startx;
/*  36 */     this.startY = starty;
/*  37 */     this.endX = endx;
/*  38 */     this.endY = endy;
/*  39 */     this.deedName = name;
/*  40 */     this.founderName = founder;
/*  41 */     this.mayorName = mayor;
/*  42 */     this.creationDate = creationDate;
/*  43 */     this.disbandDate = disbandDate;
/*  44 */     this.lastLoginDate = lastLogin;
/*  45 */     this.kingdomId = kingdom;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getDeedId() {
/*  50 */     return this.deedId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartX() {
/*  55 */     return this.startX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStartY() {
/*  60 */     return this.startY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndX() {
/*  65 */     return this.endX;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEndY() {
/*  70 */     return this.endY;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCenterX() {
/*  75 */     return getStartX() + (getEndX() - getStartX()) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCenterY() {
/*  80 */     return getStartY() + (getEndY() - getStartY()) / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDeedName() {
/*  85 */     return this.deedName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getFounderName() {
/*  90 */     return this.founderName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getMayorName() {
/*  95 */     return this.mayorName;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCreationDate() {
/* 100 */     return this.creationDate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDisbandDate() {
/* 110 */     return this.disbandDate;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastLoginDate() {
/* 115 */     return this.lastLoginDate;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getKingdomId() {
/* 120 */     return this.kingdomId;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTimeSinceDisband() {
/* 125 */     return (float)(System.currentTimeMillis() - getLastLoginDate()) / 2.4192E9F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTotalAge() {
/* 130 */     return (float)(getLastLoginDate() - getCreationDate()) / 2.4192E9F;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDistanceFrom(int tilex, int tiley) {
/* 135 */     int centerX = getStartX() + (getEndX() - getStartX()) / 2;
/* 136 */     int centerY = getStartY() + (getEndY() - getStartY()) / 2;
/* 137 */     int xDiff = centerX - tilex;
/* 138 */     int yDiff = centerY - tiley;
/* 139 */     int dist = Math.max(Math.abs(xDiff), Math.abs(yDiff));
/*     */     
/* 141 */     return getDistance(dist);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDirectionFrom(int tilex, int tiley) {
/* 146 */     int centerX = getStartX() + (getEndX() - getStartX()) / 2;
/* 147 */     int centerY = getStartY() + (getEndY() - getStartY()) / 2;
/* 148 */     int xDiff = centerX - tilex;
/* 149 */     int yDiff = centerY - tiley;
/*     */     
/* 151 */     double degrees = Math.atan2(yDiff, xDiff) * 57.29577951308232D + 90.0D;
/* 152 */     if (degrees < 0.0D) {
/* 153 */       degrees += 360.0D;
/*     */     }
/* 155 */     return getDirection(degrees);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getDistance(int dist) {
/* 160 */     if (dist <= 20)
/* 161 */       return distances[0]; 
/* 162 */     if (dist <= 40)
/* 163 */       return distances[1]; 
/* 164 */     if (dist <= 80)
/* 165 */       return distances[2]; 
/* 166 */     if (dist <= 120)
/* 167 */       return distances[3]; 
/* 168 */     if (dist <= 180) {
/* 169 */       return distances[4];
/*     */     }
/* 171 */     return distances[5];
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getDirection(double degrees) {
/* 176 */     return directions[(int)Math.round(degrees % 360.0D / 45.0D) % 8];
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
/*     */   public static final String getTimeString(float monthsTotal, boolean provideYear) {
/* 188 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 190 */     int years = (int)(monthsTotal * 8.0F / 12.0F);
/* 191 */     int months = (int)(monthsTotal * 8.0F) % 12;
/* 192 */     if (years > 0)
/* 193 */       sb.append(years + " year" + ((years > 1) ? "s" : "") + ((months > 0) ? ", " : "")); 
/* 194 */     if (months > 0)
/* 195 */       sb.append(months + " month" + ((months > 1) ? "s" : "")); 
/* 196 */     if (years <= 0 && months <= 0) {
/* 197 */       sb.append("less than a month");
/*     */     }
/* 199 */     if (provideYear) {
/* 200 */       sb.append(", somewhere around the year " + (WurmCalendar.getYear() - years));
/*     */     }
/* 202 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\villages\DeadVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */