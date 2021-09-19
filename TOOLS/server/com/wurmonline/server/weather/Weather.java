/*     */ package com.wurmonline.server.weather;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.shared.constants.WeatherConstants;
/*     */ import com.wurmonline.shared.util.MovementChecker;
/*     */ import java.util.Calendar;
/*     */ import java.util.Random;
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
/*     */ public final class Weather
/*     */   implements MiscConstants
/*     */ {
/*  38 */   private float cloudiness = 0.0F;
/*  39 */   private float fog = 0.0F;
/*  40 */   private float rain = 0.0F;
/*  41 */   private int windChange = 0;
/*  42 */   private float windAdd = 0.0F;
/*     */   
/*  44 */   private float fogAdd = 0.0F;
/*  45 */   private float rainAdd = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRainAdd() {
/*  54 */     return this.rainAdd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRainAdd(float aRainAdd) {
/*  65 */     this.rainAdd = aRainAdd;
/*     */   }
/*     */   
/*  68 */   private float fogTarget = 0.0F;
/*  69 */   private float rainTarget = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getRainTarget() {
/*  80 */     return this.rainTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRainTarget(float aRainTarget) {
/*  91 */     this.rainTarget = aRainTarget;
/*     */   }
/*     */   
/*  94 */   private float cloudTarget = 0.0F;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCloudTarget() {
/* 103 */     return this.cloudTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCloudTarget(float aCloudTarget) {
/* 114 */     this.cloudTarget = aCloudTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 120 */   private final Random random = new Random();
/* 121 */   private float windDir = this.random.nextFloat();
/* 122 */   private float windRotation = normalizeAngle(this.windDir * 360.0F);
/* 123 */   private float windPower = this.random.nextFloat() - 0.5F;
/* 124 */   private int rainTicks = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean runningMain = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final float normalizeAngle(float angle) {
/* 138 */     return MovementChecker.normalizeAngle(angle);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void modifyFogTarget(float modification) {
/* 143 */     this.fogTarget += modification;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void modifyRainTarget(float modification) {
/* 148 */     this.rainTarget += modification;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void modifyCloudTarget(float modification) {
/* 153 */     this.cloudTarget += modification;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean tick() {
/* 158 */     int day = Calendar.getInstance().get(7);
/* 159 */     if (!runningMain && Servers.localServer.LOGINSERVER) {
/*     */       
/* 161 */       this.windChange++;
/* 162 */       if (this.windChange == 1) {
/*     */         
/* 164 */         this.windDir = (float)(this.windDir + this.random.nextGaussian() * this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat() * 0.10000000149011612D);
/*     */ 
/*     */ 
/*     */         
/* 168 */         this.windRotation = normalizeAngle(this.windDir * 360.0F);
/*     */ 
/*     */         
/* 171 */         float p = 0.3F;
/* 172 */         this.windAdd = (float)(this.windAdd + this.random.nextGaussian() * this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat() * 0.30000001192092896D);
/* 173 */         this.windPower += this.windAdd;
/* 174 */         this.windAdd *= 0.94F;
/* 175 */         this.windPower *= 0.82F;
/* 176 */         if (this.windPower > 0.5F)
/* 177 */           this.windPower = 0.5F; 
/* 178 */         if (this.windPower < -0.5F) {
/* 179 */           this.windPower = -0.5F;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 184 */       if (this.windChange > 20)
/* 185 */         this.windChange = 0; 
/*     */     } 
/* 187 */     this.rainAdd *= 0.9F;
/*     */ 
/*     */     
/* 190 */     if (this.rainTicks > 15 && this.rainAdd > 0.0F)
/* 191 */       this.rainAdd *= 0.5F; 
/* 192 */     this.rainAdd = (float)(this.rainAdd + this.random.nextGaussian() * this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat());
/* 193 */     float precipitation = 0.97F;
/* 194 */     if (!runningMain && WurmCalendar.isSpring())
/* 195 */       precipitation = 0.9F; 
/* 196 */     if (!runningMain && WurmCalendar.isAutumn())
/* 197 */       precipitation = 0.9F; 
/* 198 */     if (!runningMain && WurmCalendar.isSummer())
/* 199 */       precipitation = 0.99F; 
/* 200 */     this.rainTarget *= precipitation;
/* 201 */     this.rainTarget += this.rainAdd;
/* 202 */     if (this.rainTarget > this.cloudiness)
/* 203 */       this.rainTarget = this.cloudiness; 
/* 204 */     if (day == 2) {
/*     */       
/* 206 */       if (this.rainTarget < -8.0F) {
/* 207 */         this.rainTarget = -8.0F;
/*     */       
/*     */       }
/*     */     }
/* 211 */     else if (this.rainTarget < -16.0F) {
/* 212 */       this.rainTarget = -16.0F;
/*     */     } 
/*     */     
/* 215 */     this.fogAdd *= 0.8F;
/* 216 */     this.fogAdd = (float)(this.fogAdd + this.random.nextGaussian() * this.random.nextFloat() * this.random.nextFloat() * this.random.nextFloat() * 0.20000000298023224D);
/* 217 */     this.fogTarget *= 0.9F;
/* 218 */     this.fogTarget += this.fogAdd;
/* 219 */     if (this.fogTarget > 1.0F)
/* 220 */       this.fogTarget = 1.0F; 
/* 221 */     if (this.fogTarget < -16.0F) {
/* 222 */       this.fogTarget = -16.0F;
/*     */     }
/* 224 */     if (this.rainTarget > 0.0F)
/* 225 */       this.fogTarget *= 1.0F - this.rainTarget; 
/* 226 */     if (this.windPower > 0.2D) {
/* 227 */       this.fogTarget *= 1.0F - this.windPower;
/*     */     }
/* 229 */     float stability = 0.8F;
/* 230 */     if (!runningMain && WurmCalendar.isSpring())
/* 231 */       stability = 0.8F; 
/* 232 */     if (!runningMain && WurmCalendar.isAutumn())
/* 233 */       stability = 0.7F; 
/* 234 */     if (!runningMain && WurmCalendar.isSummer())
/* 235 */       stability = 0.3F; 
/* 236 */     this.rain = this.rain * stability + this.rainTarget * (1.0F - stability);
/* 237 */     if (this.rain < 0.0F) {
/*     */       
/* 239 */       this.rain = 0.0F;
/* 240 */       this.rainTicks = 0;
/*     */     }
/* 242 */     else if (this.rain > 0.0F) {
/*     */       
/* 244 */       this.rainTicks++;
/*     */     } 
/* 246 */     this.fog = this.fog * 0.9F + this.fogTarget * 0.1F;
/* 247 */     if (this.fog < 0.0F) {
/* 248 */       this.fog = 0.0F;
/*     */     }
/* 250 */     if (this.cloudiness < this.rain * 0.33F)
/* 251 */       this.cloudiness = this.rain * 0.33F; 
/* 252 */     this.cloudTarget += (float)this.random.nextGaussian() * 0.2F * this.random.nextFloat();
/*     */     
/* 254 */     if (day == 2) {
/*     */       
/* 256 */       if (this.cloudTarget > 1.0F)
/* 257 */         this.cloudTarget = 1.0F; 
/* 258 */       if (this.cloudTarget < -0.4F) {
/* 259 */         this.cloudTarget = -0.4F;
/*     */       }
/* 261 */     } else if (day == 6) {
/*     */       
/* 263 */       if (this.cloudTarget > 0.2F)
/* 264 */         this.cloudTarget = 0.2F; 
/* 265 */       if (this.cloudTarget < -0.2F) {
/* 266 */         this.cloudTarget = -0.2F;
/*     */       }
/*     */     } else {
/*     */       
/* 270 */       if (this.cloudTarget > 1.0F)
/* 271 */         this.cloudTarget = 1.0F; 
/* 272 */       if (this.cloudTarget < -0.1F) {
/* 273 */         this.cloudTarget = -0.1F;
/*     */       }
/*     */     } 
/* 276 */     this.cloudiness = this.cloudiness * 0.98F + this.cloudTarget * 0.02F;
/* 277 */     return (this.windChange == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRainTicks() {
/* 287 */     return this.rainTicks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRainTicks(int aRainTicks) {
/* 298 */     this.rainTicks = aRainTicks;
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
/*     */   public void setWindOnly(float aWindRotation, float aWindpower, float aWindDir) {
/* 315 */     this.windDir = aWindDir;
/* 316 */     this.windPower = aWindpower;
/* 317 */     this.windRotation = aWindRotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getCloudiness() {
/* 327 */     return this.cloudiness;
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
/*     */   public float getFog() {
/* 340 */     return this.fog;
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
/*     */   public float getRain() {
/* 352 */     return this.rain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getEvaporationRate() {
/* 363 */     return 0.1F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getXWind() {
/* 374 */     return WeatherConstants.getWindX(this.windRotation, this.windPower);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getYWind() {
/* 385 */     return WeatherConstants.getWindY(this.windRotation, this.windPower);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWindRotation() {
/* 396 */     return this.windRotation;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWindDir() {
/* 407 */     return this.windDir;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getWindPower() {
/* 418 */     return this.windPower;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getWeatherString(boolean addNumbers) {
/* 423 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 425 */     float absoluteWindPower = Math.abs(this.windPower);
/* 426 */     if (absoluteWindPower > 0.4D) {
/* 427 */       buf.append("A gale ");
/* 428 */     } else if (absoluteWindPower > 0.3D) {
/* 429 */       buf.append("A strong wind ");
/* 430 */     } else if (absoluteWindPower > 0.2D) {
/* 431 */       buf.append("A strong breeze ");
/* 432 */     } else if (absoluteWindPower > 0.1D) {
/* 433 */       buf.append("A breeze ");
/*     */     } else {
/* 435 */       buf.append("A light breeze ");
/* 436 */     }  buf.append("is coming from the ");
/* 437 */     byte dir = 0;
/* 438 */     float degree = 22.5F;
/* 439 */     if (this.windRotation >= 337.5D || this.windRotation < 22.5F) {
/* 440 */       dir = 0;
/*     */     } else {
/*     */       
/* 443 */       for (int x = 0; x < 8; x++) {
/*     */         
/* 445 */         if (this.windRotation < 22.5F + (45 * x)) {
/*     */           
/* 447 */           dir = (byte)x;
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 452 */     if (dir == 0) {
/*     */       
/* 454 */       buf.append("north.");
/*     */     }
/* 456 */     else if (dir == 7) {
/*     */       
/* 458 */       buf.append("northwest.");
/*     */     }
/* 460 */     else if (dir == 6) {
/*     */       
/* 462 */       buf.append("west.");
/*     */     }
/* 464 */     else if (dir == 5) {
/*     */       
/* 466 */       buf.append("southwest.");
/*     */     }
/* 468 */     else if (dir == 4) {
/*     */       
/* 470 */       buf.append("south.");
/*     */     }
/* 472 */     else if (dir == 3) {
/*     */       
/* 474 */       buf.append("southeast.");
/*     */     }
/* 476 */     else if (dir == 2) {
/*     */       
/* 478 */       buf.append("east");
/*     */     }
/* 480 */     else if (dir == 1) {
/*     */       
/* 482 */       buf.append("northeast.");
/*     */     } 
/* 484 */     if (addNumbers)
/* 485 */       buf.append("(" + absoluteWindPower + " from " + this.windRotation + ")"); 
/* 486 */     return buf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 491 */     Weather weather = new Weather();
/* 492 */     runningMain = true;
/* 493 */     int nums = 0;
/* 494 */     int ticksRain = 0;
/* 495 */     float maxRain = 0.0F;
/* 496 */     int ticksCloud = 0;
/* 497 */     int ticksSame = 0;
/* 498 */     int ticksAnyRain = 0;
/* 499 */     boolean keepGoing = true;
/* 500 */     int maxTicks = 5000;
/* 501 */     while (keepGoing) {
/*     */       
/* 503 */       weather.tick();
/* 504 */       nums++;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 509 */       if (weather.getRain() > 0.5D)
/* 510 */         ticksRain++; 
/* 511 */       if (weather.getRain() > 0.0F)
/* 512 */         ticksAnyRain++; 
/* 513 */       if (weather.getRain() > maxRain)
/*     */       {
/* 515 */         maxRain = weather.getRain();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 520 */       if (weather.getCloudiness() > 0.5D)
/* 521 */         ticksCloud++; 
/* 522 */       if (weather.getRain() > 0.5D && weather.getCloudiness() > 0.5D)
/* 523 */         ticksSame++; 
/* 524 */       if (nums > 5000)
/*     */       {
/* 526 */         keepGoing = false;
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\weather\Weather.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */