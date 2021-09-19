/*     */ package com.wurmonline.math;
/*     */ 
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
/*     */ public final class FastMath
/*     */ {
/*     */   public static final double DBL_EPSILON = 2.220446049250313E-16D;
/*     */   public static final float FLT_EPSILON = 1.1920929E-7F;
/*     */   public static final float ZERO_TOLERANCE = 1.0E-4F;
/*     */   public static final float ONE_THIRD = 0.33333334F;
/*     */   public static final float PI = 3.1415927F;
/*     */   public static final float TWO_PI = 6.2831855F;
/*     */   public static final float HALF_PI = 1.5707964F;
/*     */   public static final float QUARTER_PI = 0.7853982F;
/*     */   public static final float INV_PI = 0.31830987F;
/*     */   public static final float INV_TWO_PI = 0.15915494F;
/*     */   public static final float DEG_TO_RAD = 0.017453292F;
/*     */   public static final float RAD_TO_DEG = 57.295776F;
/*  57 */   public static final Random rand = new Random(System.currentTimeMillis());
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
/*     */   public static boolean isPowerOfTwo(int number) {
/*  72 */     return (number > 0 && (number & number - 1) == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public static int nearestPowerOfTwo(int number) {
/*  77 */     return (int)Math.pow(2.0D, Math.ceil(Math.log(number) / Math.log(2.0D)));
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
/*     */   public static float lERP(float percent, float startValue, float endValue) {
/*  94 */     if (startValue == endValue)
/*  95 */       return startValue; 
/*  96 */     return (1.0F - percent) * startValue + percent * endValue;
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
/*     */   public static float acos(float fValue) {
/* 114 */     if (-1.0F < fValue) {
/*     */       
/* 116 */       if (fValue < 1.0F) {
/* 117 */         return (float)Math.acos(fValue);
/*     */       }
/* 119 */       return 0.0F;
/*     */     } 
/*     */     
/* 122 */     return 3.1415927F;
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
/*     */   public static float asin(float fValue) {
/* 140 */     if (-1.0F < fValue) {
/*     */       
/* 142 */       if (fValue < 1.0F) {
/* 143 */         return (float)Math.asin(fValue);
/*     */       }
/* 145 */       return 1.5707964F;
/*     */     } 
/*     */     
/* 148 */     return -1.5707964F;
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
/*     */   public static float atan(float fValue) {
/* 161 */     return (float)Math.atan(fValue);
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
/*     */   public static float atan2(float fY, float fX) {
/* 174 */     return (float)Math.atan2(fY, fX);
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
/*     */   public static float ceil(float fValue) {
/* 187 */     return (float)Math.ceil(fValue);
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
/*     */   public static float reduceSinAngle(float radians) {
/* 199 */     radians %= 6.2831855F;
/* 200 */     if (Math.abs(radians) > 3.1415927F)
/*     */     {
/* 202 */       radians -= 6.2831855F;
/*     */     }
/* 204 */     if (Math.abs(radians) > 1.5707964F)
/*     */     {
/* 206 */       radians = 3.1415927F - radians;
/*     */     }
/*     */     
/* 209 */     return radians;
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
/*     */   public static float sin(float fValue) {
/* 224 */     fValue = reduceSinAngle(fValue);
/* 225 */     if (Math.abs(fValue) <= 0.7853981633974483D)
/*     */     {
/* 227 */       return (float)Math.sin(fValue);
/*     */     }
/*     */     
/* 230 */     return (float)Math.cos(1.5707963267948966D - fValue);
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
/*     */   public static float cos(float fValue) {
/* 243 */     return sin(fValue + 1.5707964F);
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
/*     */   public static float exp(float fValue) {
/* 256 */     return (float)Math.exp(fValue);
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
/*     */   public static float abs(float fValue) {
/* 269 */     if (fValue < 0.0F)
/* 270 */       return -fValue; 
/* 271 */     return fValue;
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
/*     */   public static float floor(float fValue) {
/* 284 */     return (float)Math.floor(fValue);
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
/*     */   public static float invSqrt(float fValue) {
/* 297 */     return (float)(1.0D / Math.sqrt(fValue));
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
/*     */   public static float log(float fValue) {
/* 310 */     return (float)Math.log(fValue);
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
/*     */   public static float log(float value, float base) {
/* 325 */     return (float)(Math.log(value) / Math.log(base));
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
/*     */   public static float pow(float fBase, float fExponent) {
/* 340 */     return (float)Math.pow(fBase, fExponent);
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
/*     */   public static float sqr(float fValue) {
/* 352 */     return fValue * fValue;
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
/*     */   public static float sqrt(float fValue) {
/* 365 */     return (float)Math.sqrt(fValue);
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
/*     */   public static float tan(float fValue) {
/* 379 */     return (float)Math.tan(fValue);
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
/*     */   public static int sign(int iValue) {
/* 391 */     if (iValue > 0) {
/* 392 */       return 1;
/*     */     }
/* 394 */     if (iValue < 0) {
/* 395 */       return -1;
/*     */     }
/* 397 */     return 0;
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
/*     */   public static float sign(float fValue) {
/* 409 */     return Math.signum(fValue);
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
/*     */   
/*     */   public static float determinant(double m00, double m01, double m02, double m03, double m10, double m11, double m12, double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32, double m33) {
/* 471 */     double det01 = m20 * m31 - m21 * m30;
/* 472 */     double det02 = m20 * m32 - m22 * m30;
/* 473 */     double det03 = m20 * m33 - m23 * m30;
/* 474 */     double det12 = m21 * m32 - m22 * m31;
/* 475 */     double det13 = m21 * m33 - m23 * m31;
/* 476 */     double det23 = m22 * m33 - m23 * m32;
/* 477 */     return (float)(m00 * (m11 * det23 - m12 * det13 + m13 * det12) - m01 * (m10 * det23 - m12 * det03 + m13 * det02) + m02 * (m10 * det13 - m11 * det03 + m13 * det01) - m03 * (m10 * det12 - m11 * det02 + m12 * det01));
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
/*     */   public static float nextRandomFloat() {
/* 490 */     return rand.nextFloat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int nextRandomInt(int min, int max) {
/* 500 */     return (int)(nextRandomFloat() * (max - min + 1)) + min;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int nextRandomInt() {
/* 505 */     return rand.nextInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector3f sphericalToCartesian(Vector3f sphereCoords, Vector3f store) {
/* 514 */     store.y = sphereCoords.x * sin(sphereCoords.z);
/* 515 */     float a = sphereCoords.x * cos(sphereCoords.z);
/* 516 */     store.x = a * cos(sphereCoords.y);
/* 517 */     store.z = a * sin(sphereCoords.y);
/*     */     
/* 519 */     return store;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector3f cartesianToSpherical(Vector3f cartCoords, Vector3f store) {
/* 529 */     if (cartCoords.x == 0.0F)
/* 530 */       cartCoords.x = 1.1920929E-7F; 
/* 531 */     store
/* 532 */       .x = sqrt(cartCoords.x * cartCoords.x + cartCoords.y * cartCoords.y + cartCoords.z * cartCoords.z);
/*     */ 
/*     */     
/* 535 */     store.y = atan(cartCoords.z / cartCoords.x);
/* 536 */     if (cartCoords.x < 0.0F)
/* 537 */       store.y += 3.1415927F; 
/* 538 */     store.z = asin(cartCoords.y / store.x);
/* 539 */     return store;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector3f sphericalToCartesianZ(Vector3f sphereCoords, Vector3f store) {
/* 548 */     store.z = sphereCoords.x * sin(sphereCoords.z);
/* 549 */     float a = sphereCoords.x * cos(sphereCoords.z);
/* 550 */     store.x = a * cos(sphereCoords.y);
/* 551 */     store.y = a * sin(sphereCoords.y);
/*     */     
/* 553 */     return store;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector3f cartesianZToSpherical(Vector3f cartCoords, Vector3f store) {
/* 563 */     if (cartCoords.x == 0.0F)
/* 564 */       cartCoords.x = 1.1920929E-7F; 
/* 565 */     store
/* 566 */       .x = sqrt(cartCoords.x * cartCoords.x + cartCoords.y * cartCoords.y + cartCoords.z * cartCoords.z);
/*     */ 
/*     */     
/* 569 */     store.z = atan(cartCoords.z / cartCoords.x);
/* 570 */     if (cartCoords.x < 0.0F)
/* 571 */       store.z += 3.1415927F; 
/* 572 */     store.y = asin(cartCoords.y / store.x);
/* 573 */     return store;
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
/*     */   public static float normalize(float val, float min, float max) {
/* 585 */     if (Float.isInfinite(val) || Float.isNaN(val))
/* 586 */       return 0.0F; 
/* 587 */     float range = max - min;
/* 588 */     while (val > max)
/* 589 */       val -= range; 
/* 590 */     while (val < min)
/* 591 */       val += range; 
/* 592 */     return val;
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
/*     */   public static float copysign(float x, float y) {
/* 604 */     if (y >= 0.0F && x <= 0.0F)
/* 605 */       return -x; 
/* 606 */     if (y < 0.0F && x >= 0.0F) {
/* 607 */       return -x;
/*     */     }
/* 609 */     return x;
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
/*     */   public static float clamp(float input, float min, float max) {
/* 622 */     return (input < min) ? min : ((input > max) ? max : input);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\math\FastMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */