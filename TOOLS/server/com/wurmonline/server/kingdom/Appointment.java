/*     */ package com.wurmonline.server.kingdom;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public final class Appointment
/*     */   implements MiscConstants
/*     */ {
/*     */   public static final byte TYPE_TITLE = 0;
/*     */   public static final byte TYPE_ORDER = 1;
/*     */   public static final byte TYPE_OFFICE = 2;
/*     */   private final String malename;
/*     */   private final String femalename;
/*     */   private final int id;
/*     */   private final byte type;
/*     */   private final byte kingdom;
/*     */   private final int level;
/*  49 */   private static final Map<Integer, Appointment> jennapps = new HashMap<>();
/*     */   
/*  51 */   private static final Map<Integer, Appointment> hotsapps = new HashMap<>();
/*     */   
/*  53 */   private static final Map<Integer, Appointment> molrapps = new HashMap<>();
/*     */   
/*  55 */   private static final Map<Integer, Appointment> nonerapps = new HashMap<>();
/*     */   
/*     */   static {
/*  58 */     new Appointment("Knight of the Land", "Knightess of the Land", 0, (byte)1, (byte)0, 1);
/*  59 */     new Appointment("Knight of the Sword", "Knightess of the Sword", 1, (byte)1, (byte)0, 2);
/*  60 */     new Appointment("Defender of the Crown", "Defendress of the Crown", 2, (byte)1, (byte)0, 3);
/*  61 */     new Appointment("Defender of the Heart", "Defendress of the Heart", 3, (byte)1, (byte)0, 4);
/*  62 */     new Appointment("Baron", "Baroness", 4, (byte)1, (byte)0, 5);
/*  63 */     new Appointment("Viscount", "Viscountess", 5, (byte)1, (byte)0, 6);
/*  64 */     new Appointment("Earl", "Countess", 6, (byte)1, (byte)0, 7);
/*  65 */     new Appointment("Marquess", "Marchioness", 7, (byte)1, (byte)0, 8);
/*  66 */     new Appointment("Duke", "Duchess", 8, (byte)1, (byte)0, 9);
/*     */     
/*  68 */     new Appointment("Blue Pearl", "Blue Pearl", 30, (byte)1, (byte)1, 1);
/*  69 */     new Appointment("White Pearl", "White Pearl", 31, (byte)1, (byte)1, 2);
/*  70 */     new Appointment("Golden Unicorn", "Golden Unicorn", 32, (byte)1, (byte)1, 3);
/*  71 */     new Appointment("Sad Goose", "Sad Goose", 33, (byte)1, (byte)1, 4);
/*  72 */     new Appointment("Shining Knight", "Shining Knight", 34, (byte)1, (byte)1, 5);
/*  73 */     new Appointment("Northern Star", "Northern Star", 35, (byte)1, (byte)1, 6);
/*  74 */     new Appointment("Information minister", "Information minister", 1500, (byte)1, (byte)2, 10);
/*  75 */     new Appointment("Court magus", "Court magus", 1501, (byte)1, (byte)2, 10);
/*  76 */     new Appointment("Earl Marshal", "Earl Marshal", 1502, (byte)1, (byte)2, 10);
/*  77 */     new Appointment("Court smith", "Court smith", 1503, (byte)1, (byte)2, 10);
/*  78 */     new Appointment("Court jester", "Court jester", 1504, (byte)1, (byte)2, 10);
/*  79 */     new Appointment("Chief of economy", "Chief of economy", 1505, (byte)1, (byte)2, 10);
/*  80 */     new Appointment("Royal priest", "Royal priest", 1506, (byte)1, (byte)2, 10);
/*  81 */     new Appointment("Royal lover", "Royal mistress", 1507, (byte)1, (byte)2, 10);
/*  82 */     new Appointment("Royal avenger", "Royal avenger", 1508, (byte)1, (byte)2, 10);
/*  83 */     new Appointment("Royal cook", "Royal cook", 1509, (byte)1, (byte)2, 10);
/*  84 */     new Appointment("Royal herald", "Royal herald", 1510, (byte)1, (byte)2, 10);
/*     */     
/*  86 */     new Appointment("Hoardmaster", "Hoardmaster", 0, (byte)3, (byte)0, 1);
/*  87 */     new Appointment("Fire of the cabal", "Fire of the cabal", 1, (byte)3, (byte)0, 2);
/*  88 */     new Appointment("Forcifunghi", "Forcafungha", 2, (byte)3, (byte)0, 3);
/*  89 */     new Appointment("Plagia", "Plagia", 3, (byte)3, (byte)0, 4);
/*  90 */     new Appointment("Nonsentie", "Nonsentiess", 4, (byte)3, (byte)0, 5);
/*  91 */     new Appointment("Ignora", "Ignoress", 5, (byte)3, (byte)0, 6);
/*  92 */     new Appointment("Immateria", "Immateriess", 6, (byte)3, (byte)0, 7);
/*  93 */     new Appointment("Submissant", "Submissa", 7, (byte)3, (byte)0, 8);
/*  94 */     new Appointment("Nonexist", "Nonexista", 8, (byte)3, (byte)0, 9);
/*  95 */     new Appointment("Steel Crucifix", "Steel Crucifix", 30, (byte)3, (byte)1, 1);
/*  96 */     new Appointment("Yarn Fungus", "Yarn Fungus", 31, (byte)3, (byte)1, 2);
/*  97 */     new Appointment("Pointless Nudge", "Pointless Nudge", 32, (byte)3, (byte)1, 3);
/*  98 */     new Appointment("Unholy Matramonic Insignia", "Unholy Matramonic Insignia", 33, (byte)3, (byte)1, 4);
/*     */     
/* 100 */     new Appointment("Silver Eyeball", "Silver Eyeball", 34, (byte)3, (byte)1, 5);
/* 101 */     new Appointment("Weird yellow stick", "Weird yellow stick", 35, (byte)3, (byte)1, 6);
/* 102 */     new Appointment("Bloodwhisperer", "Bloodwhisperer", 1500, (byte)3, (byte)2, 10);
/* 103 */     new Appointment("Shaman", "Shamaness", 1501, (byte)3, (byte)2, 10);
/* 104 */     new Appointment("Chief of the Cabal", "Chieftain of the Cabal", 1502, (byte)3, (byte)2, 10);
/* 105 */     new Appointment("Painwringer", "Painwringer", 1503, (byte)3, (byte)2, 10);
/* 106 */     new Appointment("Dancer", "Dancer", 1504, (byte)3, (byte)2, 10);
/* 107 */     new Appointment("Main thug", "Main thug", 1505, (byte)3, (byte)2, 10);
/* 108 */     new Appointment("Seer", "Seer", 1506, (byte)3, (byte)2, 10);
/* 109 */     new Appointment("Lover", "Mistress", 1507, (byte)3, (byte)2, 10);
/* 110 */     new Appointment("Assassin", "Nightingale", 1508, (byte)3, (byte)2, 10);
/* 111 */     new Appointment("Party fixer", "Party hostess", 1509, (byte)3, (byte)2, 10);
/* 112 */     new Appointment("Harbinger", "Harbinger", 1510, (byte)3, (byte)2, 10);
/*     */     
/* 114 */     new Appointment("Punisher", "Punisher", 0, (byte)2, (byte)0, 1);
/* 115 */     new Appointment("Dreadnaught", "Dreadnaught", 1, (byte)2, (byte)0, 2);
/* 116 */     new Appointment("Firestarter", "Firestarter", 2, (byte)2, (byte)0, 3);
/* 117 */     new Appointment("Firetyrant", "Firetyrant", 3, (byte)2, (byte)0, 4);
/* 118 */     new Appointment("Vassal", "Vassal", 4, (byte)2, (byte)0, 5);
/* 119 */     new Appointment("Blood Roy", "Blood Regin", 5, (byte)2, (byte)0, 6);
/* 120 */     new Appointment("Graf", "Grevin", 6, (byte)2, (byte)0, 7);
/* 121 */     new Appointment("Merchant king", "Merchant Queen", 7, (byte)2, (byte)0, 8);
/* 122 */     new Appointment("Grand Duke", "Grand Duchess", 8, (byte)2, (byte)0, 9);
/* 123 */     new Appointment("Concord of Blood", "Concord of Blood", 30, (byte)2, (byte)1, 1);
/* 124 */     new Appointment("Western Flame", "Eastern Flame", 31, (byte)2, (byte)1, 2);
/* 125 */     new Appointment("Papalegba", "Mamalegba", 32, (byte)2, (byte)1, 3);
/* 126 */     new Appointment("Holy Diver", "Holy Diver", 33, (byte)2, (byte)1, 4);
/* 127 */     new Appointment("Blood Crest", "Blood Crest", 34, (byte)2, (byte)1, 5);
/* 128 */     new Appointment("Silver Ribbon in Yellow and Red", "Silver Ribbon in Yellow and Red", 35, (byte)2, (byte)1, 6);
/*     */     
/* 130 */     new Appointment("Head of the secret police", "Head of the secret police", 1500, (byte)2, (byte)2, 10);
/*     */     
/* 132 */     new Appointment("Court magus", "Court magus", 1501, (byte)2, (byte)2, 10);
/* 133 */     new Appointment("Defense advisor", "Defense advisor", 1502, (byte)2, (byte)2, 10);
/* 134 */     new Appointment("Court smith", "Court smith", 1503, (byte)2, (byte)2, 10);
/* 135 */     new Appointment("Court Harlequin", "Court Harlequin", 1504, (byte)2, (byte)2, 10);
/* 136 */     new Appointment("Economic advisor", "Economic advisor", 1505, (byte)2, (byte)2, 10);
/* 137 */     new Appointment("Religious advisor", "Religious advisor", 1506, (byte)2, (byte)2, 10);
/* 138 */     new Appointment("Betrothed", "Betrothed", 1507, (byte)2, (byte)2, 10);
/* 139 */     new Appointment("Executioner", "Executioner", 1508, (byte)2, (byte)2, 10);
/* 140 */     new Appointment("Court chef", "Court chef", 1509, (byte)2, (byte)2, 10);
/* 141 */     new Appointment("Court Announcer", "Court Announcer", 1510, (byte)2, (byte)2, 10);
/*     */     
/* 143 */     new Appointment("Knight of some Land", "Knightess of some Land", 0, (byte)0, (byte)0, 1);
/* 144 */     new Appointment("Knight of a Sword", "Knightess of a Sword", 1, (byte)0, (byte)0, 2);
/* 145 */     new Appointment("Defender of a Crown", "Defendress of a Crown", 2, (byte)0, (byte)0, 3);
/* 146 */     new Appointment("Defender of the Hat", "Defendress of the Hat", 3, (byte)0, (byte)0, 4);
/* 147 */     new Appointment("Cent", "Cent", 4, (byte)0, (byte)0, 5);
/* 148 */     new Appointment("Shilling", "Shilling", 5, (byte)0, (byte)0, 6);
/* 149 */     new Appointment("Tenpiece", "Tenpiece", 6, (byte)0, (byte)0, 7);
/* 150 */     new Appointment("Quarter", "Quarter", 7, (byte)0, (byte)0, 8);
/* 151 */     new Appointment("Carrot", "Carrot", 8, (byte)0, (byte)0, 9);
/*     */     
/* 153 */     new Appointment("Faded Pearl", "Faded Pearl", 30, (byte)0, (byte)1, 1);
/* 154 */     new Appointment("Yellow Pearl", "Yellow Pearl", 31, (byte)0, (byte)1, 2);
/* 155 */     new Appointment("Silver Unicorn", "Silver Unicorn", 32, (byte)0, (byte)1, 3);
/* 156 */     new Appointment("Jolly Goose", "Jolly Goose", 33, (byte)0, (byte)1, 4);
/* 157 */     new Appointment("Lost Knight", "Lost Knight", 34, (byte)0, (byte)1, 5);
/* 158 */     new Appointment("Fading Star", "Fading Star", 35, (byte)0, (byte)1, 6);
/* 159 */     new Appointment("Disinformation Minister", "Disinformation Minister", 1500, (byte)0, (byte)2, 10);
/*     */     
/* 161 */     new Appointment("Court Trixter", "Court Trixter", 1501, (byte)0, (byte)2, 10);
/* 162 */     new Appointment("Earl Warmonger", "Earl Warmonger", 1502, (byte)0, (byte)2, 10);
/* 163 */     new Appointment("Court Rustfriend", "Court Rustfriend", 1503, (byte)0, (byte)2, 10);
/* 164 */     new Appointment("Royal Jest", "Royal Jest", 1504, (byte)0, (byte)2, 10);
/* 165 */     new Appointment("Economic catastrophe", "Economic catastrophe", 1505, (byte)0, (byte)2, 10);
/* 166 */     new Appointment("Abbot", "Abbot", 1506, (byte)0, (byte)2, 10);
/* 167 */     new Appointment("Royal Page", "Royal Page", 1507, (byte)0, (byte)2, 10);
/* 168 */     new Appointment("Royal Bully", "Royal Bully", 1508, (byte)0, (byte)2, 10);
/* 169 */     new Appointment("Royal Poisoner", "Royal Poisoner", 1509, (byte)0, (byte)2, 10);
/* 170 */     new Appointment("Royal Bragger", "Royal Bragger", 1510, (byte)0, (byte)2, 10);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void addAppointment(Appointment app) {
/* 175 */     Kingdom k = Kingdoms.getKingdom(app.kingdom);
/*     */     
/* 177 */     if (app.kingdom == 1 || k.getTemplate() == 1) {
/* 178 */       jennapps.put(Integer.valueOf(app.id), app);
/* 179 */     } else if (app.kingdom == 3 || k.getTemplate() == 3) {
/* 180 */       hotsapps.put(Integer.valueOf(app.id), app);
/* 181 */     } else if (app.kingdom == 2 || k.getTemplate() == 2) {
/* 182 */       molrapps.put(Integer.valueOf(app.id), app);
/*     */     } else {
/* 184 */       nonerapps.put(Integer.valueOf(app.id), app);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final Appointment getAppointment(int id, byte kingdom) {
/* 189 */     Kingdom k = Kingdoms.getKingdom(kingdom);
/* 190 */     if (kingdom == 1 || k.getTemplate() == 1) return jennapps.get(Integer.valueOf(id)); 
/* 191 */     if (kingdom == 3 || k.getTemplate() == 3) return hotsapps.get(Integer.valueOf(id)); 
/* 192 */     if (kingdom == 2 || k.getTemplate() == 2) return molrapps.get(Integer.valueOf(id)); 
/* 193 */     return nonerapps.get(Integer.valueOf(id));
/*     */   }
/*     */ 
/*     */   
/*     */   static void setAppointments(Appointments app) {
/* 198 */     app.addAppointment(getAppointment(0, app.kingdom));
/* 199 */     app.addAppointment(getAppointment(1, app.kingdom));
/* 200 */     app.addAppointment(getAppointment(2, app.kingdom));
/* 201 */     app.addAppointment(getAppointment(3, app.kingdom));
/* 202 */     app.addAppointment(getAppointment(4, app.kingdom));
/* 203 */     app.addAppointment(getAppointment(5, app.kingdom));
/* 204 */     app.addAppointment(getAppointment(6, app.kingdom));
/* 205 */     app.addAppointment(getAppointment(7, app.kingdom));
/* 206 */     app.addAppointment(getAppointment(8, app.kingdom));
/*     */     
/* 208 */     app.addAppointment(getAppointment(30, app.kingdom));
/* 209 */     app.addAppointment(getAppointment(31, app.kingdom));
/* 210 */     app.addAppointment(getAppointment(32, app.kingdom));
/* 211 */     app.addAppointment(getAppointment(33, app.kingdom));
/* 212 */     app.addAppointment(getAppointment(34, app.kingdom));
/* 213 */     app.addAppointment(getAppointment(35, app.kingdom));
/* 214 */     app.addAppointment(getAppointment(1500, app.kingdom));
/* 215 */     app.addAppointment(getAppointment(1501, app.kingdom));
/* 216 */     app.addAppointment(getAppointment(1502, app.kingdom));
/* 217 */     app.addAppointment(getAppointment(1503, app.kingdom));
/* 218 */     app.addAppointment(getAppointment(1504, app.kingdom));
/* 219 */     app.addAppointment(getAppointment(1505, app.kingdom));
/* 220 */     app.addAppointment(getAppointment(1506, app.kingdom));
/* 221 */     app.addAppointment(getAppointment(1507, app.kingdom));
/* 222 */     app.addAppointment(getAppointment(1508, app.kingdom));
/* 223 */     app.addAppointment(getAppointment(1509, app.kingdom));
/* 224 */     app.addAppointment(getAppointment(1510, app.kingdom));
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
/*     */   private Appointment(String _malename, String _femalename, int _id, byte _kingdom, byte _type, int _level) {
/* 253 */     this.malename = _malename;
/* 254 */     this.femalename = _femalename;
/* 255 */     this.id = _id;
/* 256 */     this.kingdom = _kingdom;
/* 257 */     this.type = _type;
/* 258 */     this.level = _level;
/* 259 */     addAppointment(this);
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
/*     */   public String getNameForGender(byte gender) {
/* 271 */     if (gender == 1) {
/* 272 */       return this.femalename;
/*     */     }
/* 274 */     return this.malename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMaleName() {
/* 284 */     return this.malename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getFemaleName() {
/* 294 */     return this.femalename;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/* 304 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getLevel() {
/* 314 */     return this.level;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getType() {
/* 324 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 335 */     int prime = 31;
/* 336 */     int result = 1;
/* 337 */     result = 31 * result + this.id;
/* 338 */     result = 31 * result + this.kingdom;
/* 339 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 350 */     if (this == obj)
/*     */     {
/* 352 */       return true;
/*     */     }
/* 354 */     if (obj == null)
/*     */     {
/* 356 */       return false;
/*     */     }
/* 358 */     if (!(obj instanceof Appointment))
/*     */     {
/* 360 */       return false;
/*     */     }
/* 362 */     Appointment other = (Appointment)obj;
/* 363 */     if (this.id != other.id)
/*     */     {
/* 365 */       return false;
/*     */     }
/* 367 */     if (this.kingdom != other.kingdom)
/*     */     {
/* 369 */       return false;
/*     */     }
/* 371 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 382 */     StringBuilder lBuilder = new StringBuilder(150);
/* 383 */     lBuilder.append("Appointment [" + this.id);
/* 384 */     lBuilder.append(", Male name: ").append(this.malename);
/* 385 */     lBuilder.append(", Female name: ").append(this.femalename);
/* 386 */     lBuilder.append(", Kingdom: ").append(Kingdoms.getNameFor(this.kingdom));
/* 387 */     lBuilder.append(", Type: ").append(this.type);
/* 388 */     lBuilder.append(", Level: ").append(this.level);
/*     */     
/* 390 */     return lBuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\kingdom\Appointment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */