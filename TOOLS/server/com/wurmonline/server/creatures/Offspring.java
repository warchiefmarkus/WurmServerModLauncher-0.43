/*     */ package com.wurmonline.server.creatures;
/*     */ 
/*     */ import com.wurmonline.server.DbConnector;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.utils.DbUtilities;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class Offspring
/*     */ {
/*  39 */   private static final Logger logger = Logger.getLogger(Offspring.class.getName());
/*  40 */   private static final Map<Long, Offspring> offsprings = new ConcurrentHashMap<>();
/*  41 */   private byte deliveryDays = 0;
/*     */   
/*     */   private final long traits;
/*     */   
/*     */   private final long mother;
/*     */   
/*     */   private final long father;
/*     */   
/*     */   private boolean checked = false;
/*     */   
/*     */   private static final String LOAD_ALL_OFFSPRING = "SELECT * FROM OFFSPRING";
/*     */   
/*     */   private static final String DELETE_OFFSPRING = "DELETE FROM OFFSPRING WHERE MOTHERID=?";
/*     */   
/*     */   private static final String UPDATE_OFFSPRING_DAYS = "UPDATE OFFSPRING SET DELIVERYDAYS=? WHERE MOTHERID=?";
/*     */   private static final String CREATE_OFFSPRING = "INSERT INTO OFFSPRING (MOTHERID,FATHERID,TRAITS,DELIVERYDAYS) VALUES (?,?,?,?)";
/*  57 */   private static final String[] MALE_NAMES = createMaleNames();
/*     */   
/*  59 */   private static final String[] FEMALE_NAMES = createFemaleNames();
/*     */   
/*  61 */   private static final String[] GENERIC_NAMES = createGenericNames();
/*     */   
/*  63 */   private static final String[] MALE_UNI_NAMES = createMaleUnicornNames();
/*  64 */   private static final String[] FEMALE_UNI_NAMES = createFemaleUnicornNames();
/*     */ 
/*     */   
/*     */   Offspring(long _motherId, long _fatherId, long _traits, byte _deliveryDays, boolean load) {
/*  68 */     this.father = _fatherId;
/*  69 */     this.mother = _motherId;
/*  70 */     this.traits = _traits;
/*  71 */     this.deliveryDays = _deliveryDays;
/*  72 */     if (!load)
/*  73 */       create(); 
/*  74 */     offsprings.put(Long.valueOf(this.mother), this);
/*     */   }
/*     */ 
/*     */   
/*     */   private void create() {
/*  79 */     if (this.deliveryDays > 100) {
/*  80 */       this.deliveryDays = 100;
/*  81 */     } else if (this.deliveryDays < 1) {
/*  82 */       this.deliveryDays = 1;
/*  83 */     }  Connection dbcon = null;
/*  84 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/*  87 */       dbcon = DbConnector.getCreatureDbCon();
/*  88 */       ps = dbcon.prepareStatement("INSERT INTO OFFSPRING (MOTHERID,FATHERID,TRAITS,DELIVERYDAYS) VALUES (?,?,?,?)");
/*  89 */       ps.setLong(1, this.mother);
/*  90 */       ps.setLong(2, this.father);
/*  91 */       ps.setLong(3, this.traits);
/*  92 */       ps.setByte(4, this.deliveryDays);
/*  93 */       ps.executeUpdate();
/*     */     }
/*  95 */     catch (SQLException ex) {
/*     */       
/*  97 */       logger.log(Level.WARNING, "Failed to create offspring for " + this.mother, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 101 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 102 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean decreaseDaysLeft() {
/* 112 */     this.checked = true;
/* 113 */     this.deliveryDays = (byte)(this.deliveryDays - 1); if (this.deliveryDays > 0) {
/*     */       
/* 115 */       Connection dbcon = null;
/* 116 */       PreparedStatement ps = null;
/*     */       
/*     */       try {
/* 119 */         dbcon = DbConnector.getCreatureDbCon();
/* 120 */         ps = dbcon.prepareStatement("UPDATE OFFSPRING SET DELIVERYDAYS=? WHERE MOTHERID=?");
/* 121 */         ps.setByte(1, this.deliveryDays);
/* 122 */         ps.setLong(2, this.mother);
/* 123 */         ps.executeUpdate();
/*     */       }
/* 125 */       catch (SQLException ex) {
/*     */         
/* 127 */         logger.log(Level.WARNING, "Failed to update offspring for " + this.mother + ", days=" + this.deliveryDays, ex);
/*     */       }
/*     */       finally {
/*     */         
/* 131 */         DbUtilities.closeDatabaseObjects(ps, null);
/* 132 */         DbConnector.returnConnection(dbcon);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 137 */       offsprings.remove(Long.valueOf(this.mother));
/* 138 */       deleteSettings(this.mother);
/* 139 */       return true;
/*     */     } 
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void deleteSettings(long motherid) {
/* 150 */     offsprings.remove(Long.valueOf(motherid));
/* 151 */     Connection dbcon = null;
/* 152 */     PreparedStatement ps = null;
/*     */     
/*     */     try {
/* 155 */       dbcon = DbConnector.getCreatureDbCon();
/* 156 */       ps = dbcon.prepareStatement("DELETE FROM OFFSPRING WHERE MOTHERID=?");
/* 157 */       ps.setLong(1, motherid);
/* 158 */       ps.executeUpdate();
/*     */     }
/* 160 */     catch (SQLException ex) {
/*     */       
/* 162 */       logger.log(Level.WARNING, "Failed to delete offspring for " + motherid, ex);
/*     */     }
/*     */     finally {
/*     */       
/* 166 */       DbUtilities.closeDatabaseObjects(ps, null);
/* 167 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static void loadAllOffspring() {
/* 173 */     Connection dbcon = null;
/* 174 */     PreparedStatement ps = null;
/* 175 */     ResultSet rs = null;
/*     */     
/*     */     try {
/* 178 */       dbcon = DbConnector.getCreatureDbCon();
/* 179 */       ps = dbcon.prepareStatement("SELECT * FROM OFFSPRING");
/* 180 */       rs = ps.executeQuery();
/* 181 */       while (rs.next())
/*     */       {
/* 183 */         new Offspring(rs.getLong("MOTHERID"), rs.getLong("FATHERID"), rs.getLong("TRAITS"), rs.getByte("DELIVERYDAYS"), true);
/*     */       
/*     */       }
/*     */     }
/* 187 */     catch (SQLException ex) {
/*     */       
/* 189 */       logger.log(Level.WARNING, "Failed loading all offspring " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       
/* 193 */       DbUtilities.closeDatabaseObjects(ps, rs);
/* 194 */       DbConnector.returnConnection(dbcon);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static Offspring getOffspring(long wurmid) {
/* 200 */     return offsprings.get(Long.valueOf(wurmid));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getTraits() {
/* 208 */     return this.traits;
/*     */   }
/*     */ 
/*     */   
/*     */   long getMother() {
/* 213 */     return this.mother;
/*     */   }
/*     */ 
/*     */   
/*     */   long getFather() {
/* 218 */     return this.father;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDaysLeft() {
/* 223 */     return this.deliveryDays;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void resetOffspringCounters() {
/* 228 */     ArrayList<Offspring> invalid = new ArrayList<>();
/* 229 */     for (Offspring lOffspring : offsprings.values()) {
/*     */ 
/*     */       
/* 232 */       if (Creatures.getInstance().getCreatureOrNull(lOffspring.mother) == null)
/* 233 */         invalid.add(lOffspring); 
/* 234 */       lOffspring.checked = false;
/*     */     } 
/* 236 */     for (Offspring offspring : invalid) {
/*     */       
/* 238 */       logger.warning("Deleting offspring data, mother not found. mother=" + offspring.mother + ", father=" + offspring.father + ", traits=" + offspring.traits + ", deliveryDays=" + offspring.deliveryDays);
/*     */       
/* 240 */       deleteSettings(offspring.mother);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isChecked() {
/* 251 */     return this.checked;
/*     */   }
/*     */ 
/*     */   
/*     */   static String generateMaleName() {
/* 256 */     if (MALE_NAMES.length > 0) {
/*     */       
/* 258 */       int num = Server.rand.nextInt(100);
/* 259 */       if (num == 99)
/* 260 */         return MALE_NAMES[Server.rand.nextInt(MALE_NAMES.length)]; 
/* 261 */       if (num < 50) {
/* 262 */         return generateGenericPrefix() + MALE_NAMES[Server.rand.nextInt(MALE_NAMES.length)];
/*     */       }
/* 264 */       return MALE_NAMES[Server.rand.nextInt(MALE_NAMES.length)] + generateGenericPrefix();
/*     */     } 
/* 266 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   static String generateGenericPrefix() {
/* 271 */     if (GENERIC_NAMES.length > 0)
/*     */     {
/* 273 */       return GENERIC_NAMES[Server.rand.nextInt(GENERIC_NAMES.length)];
/*     */     }
/* 275 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   static String generateGenericName() {
/* 280 */     if (GENERIC_NAMES.length > 0)
/*     */     {
/* 282 */       return GENERIC_NAMES[Server.rand.nextInt(GENERIC_NAMES.length)] + GENERIC_NAMES[Server.rand
/* 283 */           .nextInt(GENERIC_NAMES.length)];
/*     */     }
/* 285 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   static String generateFemaleName() {
/* 290 */     if (FEMALE_NAMES.length > 0) {
/*     */       
/* 292 */       int num = Server.rand.nextInt(100);
/* 293 */       if (num == 99)
/* 294 */         return FEMALE_NAMES[Server.rand.nextInt(FEMALE_NAMES.length)]; 
/* 295 */       if (num < 50) {
/* 296 */         return generateGenericPrefix() + FEMALE_NAMES[Server.rand.nextInt(FEMALE_NAMES.length)];
/*     */       }
/* 298 */       return FEMALE_NAMES[Server.rand.nextInt(FEMALE_NAMES.length)] + generateGenericPrefix();
/*     */     } 
/* 300 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String generateFemaleUnicornName() {
/* 307 */     if (FEMALE_UNI_NAMES.length > 0) {
/*     */       
/* 309 */       int num = Server.rand.nextInt(100);
/* 310 */       if (num == 99)
/*     */       {
/* 312 */         return FEMALE_UNI_NAMES[Server.rand.nextInt(FEMALE_UNI_NAMES.length)];
/*     */       }
/* 314 */       if (num < 50)
/*     */       {
/* 316 */         return generateGenericPrefix() + FEMALE_UNI_NAMES[Server.rand
/* 317 */             .nextInt(FEMALE_UNI_NAMES.length)];
/*     */       }
/*     */ 
/*     */       
/* 321 */       return FEMALE_UNI_NAMES[Server.rand.nextInt(FEMALE_UNI_NAMES.length)] + 
/* 322 */         generateGenericPrefix();
/*     */     } 
/*     */     
/* 325 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   static String generateMaleUnicornName() {
/* 330 */     if (MALE_UNI_NAMES.length > 0) {
/*     */       
/* 332 */       int num = Server.rand.nextInt(100);
/* 333 */       if (num == 99)
/*     */       {
/* 335 */         return MALE_UNI_NAMES[Server.rand.nextInt(MALE_UNI_NAMES.length)];
/*     */       }
/* 337 */       if (num < 50)
/*     */       {
/* 339 */         return generateGenericPrefix() + MALE_UNI_NAMES[Server.rand
/* 340 */             .nextInt(MALE_UNI_NAMES.length)];
/*     */       }
/*     */ 
/*     */       
/* 344 */       return MALE_UNI_NAMES[Server.rand.nextInt(MALE_UNI_NAMES.length)] + 
/* 345 */         generateGenericPrefix();
/*     */     } 
/*     */     
/* 348 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getRandomMaleName() {
/* 354 */     return MALE_NAMES[Server.rand.nextInt(MALE_NAMES.length)];
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getRandomFemaleName() {
/* 359 */     return FEMALE_NAMES[Server.rand.nextInt(FEMALE_NAMES.length)];
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getRandomGenericName() {
/* 364 */     return GENERIC_NAMES[Server.rand.nextInt(GENERIC_NAMES.length)];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String[] createMaleNames() {
/* 372 */     List<String> malelist = new ArrayList<>(80);
/* 373 */     malelist.add("lars");
/* 374 */     malelist.add("bom");
/* 375 */     malelist.add("ally");
/* 376 */     malelist.add("dom");
/* 377 */     malelist.add("mack");
/* 378 */     malelist.add("hard");
/* 379 */     malelist.add("billy");
/* 380 */     malelist.add("flint");
/* 381 */     malelist.add("wart");
/* 382 */     malelist.add("stark");
/* 383 */     malelist.add("tom");
/* 384 */     malelist.add("master");
/* 385 */     malelist.add("prancer");
/* 386 */     malelist.add("bouncer");
/* 387 */     malelist.add("mark");
/* 388 */     malelist.add("rolf");
/* 389 */     malelist.add("notch");
/* 390 */     malelist.add("bear");
/* 391 */     malelist.add("minsc");
/* 392 */     malelist.add("abbas");
/* 393 */     malelist.add("ace");
/* 394 */     malelist.add("baron");
/* 395 */     malelist.add("duke");
/* 396 */     malelist.add("baxter");
/* 397 */     malelist.add("ben");
/* 398 */     malelist.add("benny");
/* 399 */     malelist.add("cesar");
/* 400 */     malelist.add("cactus");
/* 401 */     malelist.add("dale");
/* 402 */     malelist.add("damien");
/* 403 */     malelist.add("eagle");
/* 404 */     malelist.add("ears");
/* 405 */     malelist.add("echo");
/* 406 */     malelist.add("eben");
/* 407 */     malelist.add("eclipse");
/* 408 */     malelist.add("ed");
/* 409 */     malelist.add("faith");
/* 410 */     malelist.add("falcon");
/* 411 */     malelist.add("fancy");
/* 412 */     malelist.add("fantasy");
/* 413 */     malelist.add("gage");
/* 414 */     malelist.add("gallant");
/* 415 */     malelist.add("hal");
/* 416 */     malelist.add("ibn");
/* 417 */     malelist.add("ice");
/* 418 */     malelist.add("jack");
/* 419 */     malelist.add("kaden");
/* 420 */     malelist.add("kalil");
/* 421 */     malelist.add("lad");
/* 422 */     malelist.add("man");
/* 423 */     malelist.add("maestro");
/* 424 */     malelist.add("nada");
/* 425 */     malelist.add("nafa");
/* 426 */     malelist.add("ocho");
/* 427 */     malelist.add("oblivion");
/* 428 */     malelist.add("paddy");
/* 429 */     malelist.add("quail");
/* 430 */     malelist.add("raffle");
/* 431 */     malelist.add("rags");
/* 432 */     malelist.add("sage");
/* 433 */     malelist.add("tails");
/* 434 */     malelist.add("vigor");
/* 435 */     malelist.add("venture");
/* 436 */     malelist.add("waldo");
/* 437 */     malelist.add("walt");
/* 438 */     malelist.add("thunder");
/* 439 */     malelist.add("xo");
/* 440 */     malelist.add("yasin");
/* 441 */     malelist.add("cliff");
/* 442 */     malelist.add("hill");
/* 443 */     malelist.add("max");
/* 444 */     malelist.add("alex");
/* 445 */     malelist.add("erik");
/* 446 */     malelist.add("roman");
/* 447 */     malelist.add("johan");
/* 448 */     malelist.add("emil");
/* 449 */     malelist.add("uze");
/*     */     
/* 451 */     return malelist.<String>toArray(new String[malelist.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String[] createFemaleNames() {
/* 459 */     List<String> femalelist = new ArrayList<>(80);
/* 460 */     femalelist.add("molly");
/* 461 */     femalelist.add("tess");
/* 462 */     femalelist.add("lily");
/* 463 */     femalelist.add("anna");
/* 464 */     femalelist.add("bella");
/* 465 */     femalelist.add("ann");
/* 466 */     femalelist.add("pinkie");
/* 467 */     femalelist.add("belle");
/* 468 */     femalelist.add("adriana");
/* 469 */     femalelist.add("abia");
/* 470 */     femalelist.add("adara");
/* 471 */     femalelist.add("agnes");
/* 472 */     femalelist.add("aisha");
/* 473 */     femalelist.add("ballet");
/* 474 */     femalelist.add("babe");
/* 475 */     femalelist.add("bashira");
/* 476 */     femalelist.add("benita");
/* 477 */     femalelist.add("caine");
/* 478 */     femalelist.add("daisy");
/* 479 */     femalelist.add("dalia");
/* 480 */     femalelist.add("echo");
/* 481 */     femalelist.add("eben");
/* 482 */     femalelist.add("fabiola");
/* 483 */     femalelist.add("fancy");
/* 484 */     femalelist.add("fantasy");
/* 485 */     femalelist.add("gala");
/* 486 */     femalelist.add("halim");
/* 487 */     femalelist.add("hall");
/* 488 */     femalelist.add("ida");
/* 489 */     femalelist.add("jade");
/* 490 */     femalelist.add("kia");
/* 491 */     femalelist.add("kim");
/* 492 */     femalelist.add("kalil");
/* 493 */     femalelist.add("kalypso");
/* 494 */     femalelist.add("lace");
/* 495 */     femalelist.add("lady");
/* 496 */     femalelist.add("mac");
/* 497 */     femalelist.add("madia");
/* 498 */     femalelist.add("nafar");
/* 499 */     femalelist.add("nana");
/* 500 */     femalelist.add("nanook");
/* 501 */     femalelist.add("napa");
/* 502 */     femalelist.add("oak");
/* 503 */     femalelist.add("ocean");
/* 504 */     femalelist.add("paint");
/* 505 */     femalelist.add("queen");
/* 506 */     femalelist.add("sara");
/* 507 */     femalelist.add("sadie");
/* 508 */     femalelist.add("sage");
/* 509 */     femalelist.add("sahar");
/* 510 */     femalelist.add("taffy");
/* 511 */     femalelist.add("tahu");
/* 512 */     femalelist.add("tammy");
/* 513 */     femalelist.add("ula");
/* 514 */     femalelist.add("uma");
/* 515 */     femalelist.add("umbra");
/* 516 */     femalelist.add("unity");
/* 517 */     femalelist.add("vanessa");
/* 518 */     femalelist.add("vanilla");
/* 519 */     femalelist.add("rose");
/* 520 */     femalelist.add("xena");
/* 521 */     femalelist.add("li");
/* 522 */     femalelist.add("lei");
/* 523 */     femalelist.add("zoe");
/* 524 */     femalelist.add("zafir");
/* 525 */     femalelist.add("zara");
/* 526 */     femalelist.add("yazmeen");
/* 527 */     femalelist.add("yahya");
/* 528 */     femalelist.add("yoana");
/* 529 */     femalelist.add("cliff");
/* 530 */     femalelist.add("pifa");
/* 531 */     femalelist.add("tich");
/* 532 */     femalelist.add("panda");
/* 533 */     return femalelist.<String>toArray(new String[femalelist.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String[] createGenericNames() {
/* 541 */     List<String> genericlist = new ArrayList<>(59);
/* 542 */     genericlist.add("lightning");
/* 543 */     genericlist.add("flash");
/* 544 */     genericlist.add("flea");
/* 545 */     genericlist.add("osio");
/*     */     
/* 547 */     genericlist.add("stark");
/* 548 */     genericlist.add("strong");
/* 549 */     genericlist.add("fast");
/* 550 */     genericlist.add("hard");
/* 551 */     genericlist.add("brisk");
/* 552 */     genericlist.add("sweet");
/* 553 */     genericlist.add("jolly");
/* 554 */     genericlist.add("ecker");
/* 555 */     genericlist.add("golden");
/* 556 */     genericlist.add("silver");
/* 557 */     genericlist.add("pearl");
/* 558 */     genericlist.add("gold");
/* 559 */     genericlist.add("heart");
/* 560 */     genericlist.add("honey");
/* 561 */     genericlist.add("kiss");
/* 562 */     genericlist.add("dance");
/* 563 */     genericlist.add("swift");
/* 564 */     genericlist.add("hop");
/* 565 */     genericlist.add("pie");
/* 566 */     genericlist.add("iron");
/* 567 */     genericlist.add("copper");
/* 568 */     genericlist.add("grey");
/* 569 */     genericlist.add("blood");
/* 570 */     genericlist.add("north");
/* 571 */     genericlist.add("west");
/* 572 */     genericlist.add("wind");
/* 573 */     genericlist.add("east");
/* 574 */     genericlist.add("rain");
/* 575 */     genericlist.add("south");
/* 576 */     genericlist.add("cloud");
/* 577 */     genericlist.add("rock");
/* 578 */     genericlist.add("mountain");
/* 579 */     genericlist.add("happy");
/* 580 */     genericlist.add("halt");
/* 581 */     genericlist.add("sad");
/* 582 */     genericlist.add("tear");
/* 583 */     genericlist.add("clip");
/* 584 */     genericlist.add("dream");
/* 585 */     genericlist.add("chaser");
/* 586 */     genericlist.add("hunting");
/* 587 */     genericlist.add("pick");
/* 588 */     genericlist.add("dog");
/* 589 */     genericlist.add("call");
/* 590 */     genericlist.add("ebony");
/* 591 */     genericlist.add("rage");
/* 592 */     genericlist.add("raid");
/* 593 */     genericlist.add("wing");
/* 594 */     genericlist.add("warrior");
/* 595 */     genericlist.add("war");
/* 596 */     genericlist.add("walking");
/* 597 */     genericlist.add("run");
/* 598 */     genericlist.add("wild");
/* 599 */     genericlist.add("coffee");
/* 600 */     return genericlist.<String>toArray(new String[genericlist.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String[] createMaleUnicornNames() {
/* 605 */     List<String> mul = new ArrayList<>();
/* 606 */     mul.add("Amor");
/* 607 */     mul.add("Amulius");
/* 608 */     mul.add("Aries");
/* 609 */     mul.add("Belial");
/* 610 */     mul.add("Cai");
/* 611 */     mul.add("Consus");
/* 612 */     mul.add("Cupid");
/* 613 */     mul.add("Faunus");
/* 614 */     mul.add("Gemini");
/* 615 */     mul.add("Caesar");
/* 616 */     mul.add("Iovis");
/* 617 */     mul.add("Italus");
/* 618 */     mul.add("Janus");
/* 619 */     mul.add("Ausimus");
/* 620 */     mul.add("Jupiter");
/* 621 */     mul.add("Kay");
/* 622 */     mul.add("Liber");
/* 623 */     mul.add("Mars");
/* 624 */     mul.add("Mercury");
/* 625 */     mul.add("Neptune");
/* 626 */     mul.add("Numitor");
/* 627 */     mul.add("Quirinus");
/* 628 */     mul.add("Remus");
/* 629 */     mul.add("Romulus");
/* 630 */     mul.add("Saturn");
/* 631 */     mul.add("Silvius");
/* 632 */     mul.add("Summanus");
/* 633 */     mul.add("Tatius");
/* 634 */     mul.add("Terminus");
/* 635 */     mul.add("Vulcan");
/* 636 */     mul.add("Saro");
/* 637 */     return mul.<String>toArray(new String[mul.size()]);
/*     */   }
/*     */ 
/*     */   
/*     */   private static String[] createFemaleUnicornNames() {
/* 642 */     List<String> ful = new ArrayList<>();
/* 643 */     ful.add("Amor");
/* 644 */     ful.add("Angerona");
/* 645 */     ful.add("Aurora");
/* 646 */     ful.add("Bellona");
/* 647 */     ful.add("Camilla");
/* 648 */     ful.add("Cardea");
/* 649 */     ful.add("Ceres");
/* 650 */     ful.add("Concordia");
/* 651 */     ful.add("Cora");
/* 652 */     ful.add("Diana");
/* 653 */     ful.add("Dido");
/* 654 */     ful.add("Elissa");
/* 655 */     ful.add("Fauna");
/* 656 */     ful.add("Corona");
/* 657 */     ful.add("Flora");
/* 658 */     ful.add("Hersilia");
/* 659 */     ful.add("Juno");
/* 660 */     ful.add("Juturna");
/* 661 */     ful.add("Juventas");
/* 662 */     ful.add("Laverna");
/* 663 */     ful.add("Lavinia");
/* 664 */     ful.add("Libitina");
/* 665 */     ful.add("Lucina");
/* 666 */     ful.add("Ausimus");
/* 667 */     ful.add("Luna");
/* 668 */     ful.add("Maia");
/* 669 */     ful.add("Minerva");
/* 670 */     ful.add("Naenia");
/* 671 */     ful.add("Nona");
/* 672 */     ful.add("Pax");
/* 673 */     ful.add("Pomona");
/* 674 */     ful.add("Leonia");
/* 675 */     ful.add("Salacia");
/* 676 */     ful.add("Silvia");
/* 677 */     ful.add("Venus");
/* 678 */     ful.add("Victoria");
/* 679 */     return ful.<String>toArray(new String[ful.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\Offspring.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */