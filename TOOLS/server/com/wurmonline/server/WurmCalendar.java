/*     */ package com.wurmonline.server;
/*     */ 
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.utils.StringUtil;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.Year;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.TimeZone;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class WurmCalendar
/*     */   implements TimeConstants
/*     */ {
/*  42 */   private static final DateFormat gmtDateFormat = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  47 */     gmtDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  56 */   private static final String[] day_names = new String[] { "day of the Ant", "Luck day", "day of the Wurm", "Wrath day", "day of Tears", "day of Sleep", "day of Awakening" };
/*     */ 
/*     */ 
/*     */   
/*  60 */   private static final Logger logger = Logger.getLogger(WurmCalendar.class.getName());
/*     */   
/*     */   static final int STARFALL_DIAMONDS = 0;
/*     */   
/*     */   static final int STARFALL_SAW = 1;
/*     */   
/*     */   static final int STARFALL_DIGGING = 2;
/*     */   
/*     */   static final int STARFALL_LEAF = 3;
/*     */   
/*     */   static final int STARFALL_BEAR = 4;
/*     */   
/*     */   static final int STARFALL_SNAKE = 5;
/*     */   
/*     */   static final int STARFALL_SHARK = 6;
/*     */   
/*     */   static final int STARFALL_FIRES = 7;
/*     */   
/*     */   static final int STARFALL_RAVEN = 8;
/*     */   static final int STARFALL_DANCERS = 9;
/*     */   static final int STARFALL_OMENS = 10;
/*     */   static final int STARFALL_SILENCE = 11;
/*     */   private static boolean isSpring = false;
/*     */   private static boolean isTestChristmas = false;
/*     */   public static boolean wasTestChristmas = false;
/*     */   private static boolean isTestEaster = false;
/*     */   private static boolean isTestWurm = false;
/*     */   private static boolean isTestHalloween = false;
/*  88 */   private static boolean personalGoalsActive = nowIsBefore(0, 1, 1, 1, 2019);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final boolean ENABLE_CHECK_SPRING = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final int startYear = 980;
/*     */ 
/*     */ 
/*     */   
/* 101 */   public static long currentTime = 0L;
/* 102 */   public static long lastHarvestableCheck = 0L;
/*     */   
/* 104 */   private static final String[] starfall_names = new String[] { "the starfall of Diamonds", "the starfall of the Saw", "the starfall of the Digging", "the starfall of the Leaf", "the Bear's starfall", "the Snake's starfall", "the White Shark starfall", "the starfall of Fires", "the Raven's starfall", "the starfall of Dancers", "the starfall of Omens", "the starfall of Silence" };
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
/*     */   public static String getTime() {
/* 128 */     long year = 980L + currentTime / 29030400L;
/* 129 */     int starfall = (int)(currentTime % 29030400L / 2419200L);
/* 130 */     int day = (int)(currentTime % 2419200L / 86400L);
/* 131 */     int dayOfWeek = day % 7;
/* 132 */     long week = (day / 7 + 1);
/* 133 */     int hour = (int)(currentTime % 86400L / 3600L);
/* 134 */     int minute = getMinute();
/* 135 */     int second = getSecond();
/* 136 */     String toReturn = StringUtil.format("It is %02d:%02d:%02d", new Object[] { Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second) });
/* 137 */     toReturn = toReturn + " on " + day_names[dayOfWeek] + " in week " + week + " of " + starfall_names[starfall] + " in the year of " + year + ".";
/*     */     
/* 139 */     return toReturn;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final boolean mayDestroyHugeAltars() {
/* 144 */     int day = (int)(currentTime % 2419200L / 86400L);
/* 145 */     long week = (day / 7 + 1);
/* 146 */     return ((getDay() == 3 || getDay() == 6) && (week == 1L || week == 3L));
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
/*     */   public static final String getTimeFor(long wurmtime) {
/* 161 */     long year = 980L + wurmtime / 29030400L;
/* 162 */     int starfall = (int)Math.max(0L, wurmtime % 29030400L / 2419200L);
/* 163 */     int day = (int)(wurmtime % 2419200L / 86400L);
/* 164 */     int dayOfWeek = Math.max(0, day % 7);
/* 165 */     long week = (day / 7 + 1);
/* 166 */     int hour = (int)(wurmtime % 86400L / 3600L);
/* 167 */     int minute = (int)(wurmtime % 3600L / 60L);
/* 168 */     int second = (int)(wurmtime % 60L);
/*     */     
/* 170 */     String toReturn = StringUtil.format("%02d:%02d:%02d", new Object[] { Integer.valueOf(hour), Integer.valueOf(minute), Integer.valueOf(second) });
/* 171 */     toReturn = toReturn + " on " + day_names[dayOfWeek] + " in week " + week + " of " + starfall_names[starfall] + " in the year of " + year + ".";
/*     */     
/* 173 */     return toReturn;
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
/*     */   public static final String getDateFor(long wurmtime) {
/* 186 */     long year = 980L + wurmtime / 29030400L;
/* 187 */     int starfall = (int)(wurmtime % 29030400L / 2419200L);
/* 188 */     int day = (int)(wurmtime % 2419200L / 86400L);
/* 189 */     int dayOfWeek = day % 7;
/* 190 */     long week = (day / 7 + 1);
/* 191 */     String toReturn = "";
/*     */     
/* 193 */     toReturn = toReturn + day_names[dayOfWeek] + ", week " + week + " of " + starfall_names[starfall] + ", " + year + ".";
/* 194 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String getDaysFrom(long wurmtime) {
/* 204 */     boolean inPast = (currentTime > wurmtime);
/* 205 */     long diff = Math.abs(currentTime - wurmtime);
/* 206 */     long diffYear = diff / 29030400L;
/* 207 */     int diffMonth = (int)(diff % 29030400L / 2419200L);
/* 208 */     int diffWeek = (int)(diff % 2419200L / 604800L);
/* 209 */     int diffDay = (int)(diff % 604800L / 86400L);
/*     */     
/* 211 */     StringBuilder buf = new StringBuilder();
/* 212 */     if (diffYear > 0L)
/*     */     {
/* 214 */       if (diffYear == 1L) {
/* 215 */         buf.append(diffYear + " year, ");
/*     */       } else {
/* 217 */         buf.append(diffYear + " years, ");
/*     */       }  } 
/* 219 */     if (diffYear > 0L || diffMonth > 0)
/*     */     {
/* 221 */       if (diffMonth == 1) {
/* 222 */         buf.append(diffMonth + " month, ");
/*     */       } else {
/* 224 */         buf.append(diffMonth + " months, ");
/*     */       }  } 
/* 226 */     if (diffYear > 0L || diffMonth > 0 || diffWeek > 0)
/*     */     {
/* 228 */       if (diffWeek == 1) {
/* 229 */         buf.append(diffWeek + " week, ");
/*     */       } else {
/* 231 */         buf.append(diffWeek + " weeks, ");
/*     */       }  } 
/* 233 */     if (diffDay == 1) {
/* 234 */       buf.append(diffDay + " day");
/*     */     } else {
/* 236 */       buf.append(diffDay + " days");
/* 237 */     }  if (inPast) {
/* 238 */       buf.append(" ago.");
/*     */     } else {
/* 240 */       buf.append(".");
/* 241 */     }  return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void tickSecond() {
/* 250 */     currentTime++;
/*     */     
/* 252 */     if (currentTime < WurmHarvestables.getLastHarvestableCheck() + 3600L) {
/*     */       return;
/*     */     }
/* 255 */     WurmHarvestables.checkHarvestables(currentTime);
/*     */     
/* 257 */     if (personalGoalsActive && !nowIsBefore(0, 1, 1, 1, 2019)) {
/*     */       
/* 259 */       personalGoalsActive = false;
/* 260 */       Server.getInstance().broadCastAlert("Alert: Personal Goals are now disabled", true);
/*     */       
/* 262 */       for (Player p : Players.getInstance().getPlayers()) {
/* 263 */         p.getCommunicator().sendCloseWindow((short)27);
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
/*     */   public static int getYear() {
/* 278 */     return 980 + getYearOffset();
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
/*     */   public static int getYearOffset() {
/* 291 */     return (int)(currentTime / 29030400L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getStarfallWeek() {
/* 301 */     return (int)(currentTime % 29030400L / 604800L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getStarfall() {
/* 311 */     return (int)(currentTime % 29030400L / 2419200L);
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
/*     */   public static int getDay() {
/* 323 */     int day = (int)(currentTime % 29030400L / 86400L);
/* 324 */     int dayOfWeek = day % 7;
/* 325 */     return dayOfWeek;
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
/*     */   public static int getHour() {
/* 337 */     return (int)(currentTime % 86400L / 3600L);
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
/*     */   public static int getMinute() {
/* 349 */     return (int)(currentTime % 3600L / 60L);
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
/*     */   public static int getSecond() {
/* 361 */     return (int)(currentTime % 60L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void incrementHour() {
/* 370 */     setTime(currentTime + 3600L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void setTime(long time) {
/* 380 */     currentTime = time;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNight() {
/* 390 */     int h = getHour();
/* 391 */     return (h > 20 || h < 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMorning() {
/* 401 */     int h = getHour();
/* 402 */     return (h <= 8 && h >= 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isNewYear1() {
/* 407 */     return nowIsBetween(0, 1, 1, 0, Year.now().getValue(), 0, 5, 1, 0, Year.now().getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAfterNewYear1() {
/* 412 */     return nowIsAfter(0, 5, 1, 0, Year.now().getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean toggleSpecial(String special) {
/* 418 */     wasTestChristmas = isTestChristmas;
/* 419 */     isTestChristmas = false;
/* 420 */     isTestEaster = false;
/* 421 */     isTestWurm = false;
/* 422 */     isTestHalloween = false;
/* 423 */     switch (special) {
/*     */       
/*     */       case "xmas":
/* 426 */         isTestChristmas = true;
/* 427 */         return true;
/*     */       case "easter":
/* 429 */         isTestEaster = true;
/* 430 */         return true;
/*     */       case "wurm":
/* 432 */         isTestWurm = true;
/* 433 */         return true;
/*     */       case "halloween":
/* 435 */         isTestHalloween = true;
/* 436 */         return true;
/*     */     } 
/* 438 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isTestChristmas() {
/* 444 */     return isTestChristmas;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isChristmas() {
/* 449 */     if (isTestChristmas)
/* 450 */       return true; 
/* 451 */     return nowIsBetween(15, 0, 23, 11, Year.now().getValue(), 12, 0, 31, 11, Year.now().getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isBeforeChristmas() {
/* 456 */     return nowIsBefore(17, 0, 23, 11, Year.now().getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAfterChristmas() {
/* 461 */     return nowIsAfter(12, 0, 31, 11, Year.now().getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAfterEaster() {
/* 466 */     Calendar c = EasterCalculator.findHolyDay(Year.now().getValue());
/* 467 */     return nowIsAfter(10, 0, c.get(5) + 2, c.get(2), c.get(1));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEaster() {
/* 472 */     if (isTestEaster)
/* 473 */       return true; 
/* 474 */     Calendar c = EasterCalculator.findHolyDay(Year.now().getValue());
/* 475 */     return (nowIsAfter(10, 0, c.get(5), c.get(2), c.get(1)) && 
/* 476 */       nowIsBefore(10, 0, c.get(5) + 2, c.get(2), c.get(1)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isHalloween() {
/* 481 */     if (isTestHalloween)
/* 482 */       return true; 
/* 483 */     return nowIsBetween(0, 1, 28, 9, Year.now().getValue(), 23, 59, 5, 10, Year.now().getValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isAnniversary() {
/* 488 */     if (isTestWurm)
/* 489 */       return true; 
/* 490 */     return nowIsBetween(0, 1, 6, 5, Year.now().getValue(), 23, 59, 12, 5, Year.now().getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getSpecialMapping(boolean predot) {
/* 501 */     if (isChristmas())
/* 502 */       return predot ? ".xmas" : "xmas."; 
/* 503 */     if (isEaster())
/* 504 */       return predot ? ".easter" : "easter."; 
/* 505 */     if (isHalloween())
/* 506 */       return predot ? ".halloween" : "halloween."; 
/* 507 */     if (isAnniversary())
/* 508 */       return predot ? ".wurm" : "wurm."; 
/* 509 */     return "";
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
/*     */   public static void checkSpring() {}
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
/*     */   public static boolean isSpring() {
/* 543 */     int starfall = getStarfall();
/* 544 */     return (starfall > 2 && starfall < 6);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSummer() {
/* 555 */     int starfall = getStarfall();
/* 556 */     return (starfall > 5 && starfall < 9);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAutumn() {
/* 566 */     int starfall = getStarfall();
/* 567 */     return (starfall > 8 && starfall < 12);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isWinter() {
/* 577 */     int starfall = getStarfall();
/* 578 */     return (starfall > 11 || starfall < 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAutumnWinter() {
/* 588 */     int starfall = getStarfall();
/* 589 */     return (starfall > 8 || starfall < 3);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSeasonSpring() {
/* 598 */     int starfallWeek = getStarfallWeek();
/* 599 */     return (starfallWeek >= 2 && starfallWeek < 12);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSeasonSummer() {
/* 608 */     int starfallWeek = getStarfallWeek();
/* 609 */     return (starfallWeek >= 12 && starfallWeek < 35);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSeasonAutumn() {
/* 618 */     int starfallWeek = getStarfallWeek();
/* 619 */     return (starfallWeek >= 35 && starfallWeek < 45);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSeasonWinter() {
/* 628 */     int starfallWeek = getStarfallWeek();
/* 629 */     return (starfallWeek >= 46 || starfallWeek < 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getSeasonNumber() {
/* 634 */     int season = 0;
/* 635 */     if (isWinter())
/* 636 */       season = 4; 
/* 637 */     if (isSpring())
/* 638 */       season = 0; 
/* 639 */     if (isSummer())
/* 640 */       season = 2; 
/* 641 */     if (isAutumn())
/* 642 */       season = 3; 
/* 643 */     return season;
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
/*     */   public static boolean nowIsBetween(int shour, int sminute, int sday, int smonth, int syear, int ehour, int eminute, int eday, int emonth, int eyear) {
/* 656 */     Calendar start = Calendar.getInstance();
/* 657 */     start.set(syear, smonth, sday, shour, sminute);
/* 658 */     long startTime = start.getTimeInMillis();
/* 659 */     Calendar end = Calendar.getInstance();
/* 660 */     end.set(eyear, emonth, eday, ehour, eminute);
/* 661 */     long endTime = end.getTimeInMillis();
/* 662 */     long now = System.currentTimeMillis();
/*     */ 
/*     */     
/* 665 */     if (now >= startTime && now <= endTime)
/*     */     {
/*     */       
/* 668 */       return true;
/*     */     }
/* 670 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean nowIsBefore(int shour, int sminute, int sday, int smonth, int syear) {
/* 675 */     Calendar start = Calendar.getInstance();
/* 676 */     start.set(syear, smonth, sday, shour, sminute);
/*     */     
/* 678 */     return (System.currentTimeMillis() < start.getTimeInMillis());
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean nowIsAfter(int hour, int minute, int day, int month, int year) {
/* 683 */     Calendar cnow = Calendar.getInstance();
/* 684 */     cnow.set(year, month, day, hour, minute);
/*     */     
/* 686 */     return (System.currentTimeMillis() > cnow.getTimeInMillis());
/*     */   }
/*     */ 
/*     */   
/*     */   public static String formatGmt(long time) {
/* 691 */     return gmtDateFormat.format(new Date(time)) + " GMT";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getCurrentTime() {
/* 701 */     return currentTime;
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
/*     */   static final class Ticker
/*     */     implements Runnable
/*     */   {
/*     */     public void run() {
/* 720 */       if (WurmCalendar.logger.isLoggable(Level.FINEST))
/*     */       {
/* 722 */         WurmCalendar.logger.finest("Running newSingleThreadScheduledExecutor for calling WurmCalendar.tickSecond()");
/*     */       }
/*     */       
/*     */       try {
/* 726 */         long now = System.nanoTime();
/*     */ 
/*     */         
/*     */         try {
/* 730 */           WurmCalendar.tickSecond();
/*     */         }
/* 732 */         catch (Exception e) {
/*     */           
/* 734 */           WurmCalendar.logger.log(Level.WARNING, "Exception in WurmCalendar.tickSecond");
/* 735 */           e.printStackTrace();
/*     */         } 
/*     */         
/* 738 */         float lElapsedTime = (float)(System.nanoTime() - now) / 1000000.0F;
/* 739 */         if (lElapsedTime > (float)Constants.lagThreshold)
/*     */         {
/* 741 */           WurmCalendar.logger.info("Finished calling WurmCalendar.tickSecond(), which took " + lElapsedTime + " millis.");
/*     */         
/*     */         }
/*     */       }
/* 745 */       catch (RuntimeException e) {
/*     */         
/* 747 */         WurmCalendar.logger.log(Level.WARNING, "Caught exception in ScheduledExecutorService while calling WurmCalendar.tickSecond()", e);
/*     */         
/* 749 */         throw e;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\WurmCalendar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */