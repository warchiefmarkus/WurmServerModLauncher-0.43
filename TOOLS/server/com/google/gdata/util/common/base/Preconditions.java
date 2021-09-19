/*     */ package com.google.gdata.util.common.base;
/*     */ 
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Preconditions
/*     */ {
/*     */   public static void checkArgument(boolean expression) {
/*  71 */     if (!expression) {
/*  72 */       throw new IllegalArgumentException();
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
/*     */   public static void checkArgument(boolean expression, Object errorMessage) {
/*  86 */     if (!expression) {
/*  87 */       throw new IllegalArgumentException(String.valueOf(errorMessage));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
/* 112 */     if (!expression) {
/* 113 */       throw new IllegalArgumentException(format(errorMessageTemplate, errorMessageArgs));
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
/*     */   public static void checkState(boolean expression) {
/* 126 */     if (!expression) {
/* 127 */       throw new IllegalStateException();
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
/*     */   public static void checkState(boolean expression, Object errorMessage) {
/* 141 */     if (!expression) {
/* 142 */       throw new IllegalStateException(String.valueOf(errorMessage));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs) {
/* 167 */     if (!expression) {
/* 168 */       throw new IllegalStateException(format(errorMessageTemplate, errorMessageArgs));
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
/*     */   public static <T> T checkNotNull(T reference) {
/* 182 */     if (reference == null) {
/* 183 */       throw new NullPointerException();
/*     */     }
/* 185 */     return reference;
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
/*     */   public static <T> T checkNotNull(T reference, Object errorMessage) {
/* 199 */     if (reference == null) {
/* 200 */       throw new NullPointerException(String.valueOf(errorMessage));
/*     */     }
/* 202 */     return reference;
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
/*     */   public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs) {
/* 224 */     if (reference == null)
/*     */     {
/* 226 */       throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
/*     */     }
/*     */     
/* 229 */     return reference;
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
/*     */   public static <T extends Iterable<?>> T checkContentsNotNull(T iterable) {
/* 242 */     if (containsOrIsNull((Iterable<?>)iterable)) {
/* 243 */       throw new NullPointerException();
/*     */     }
/* 245 */     return iterable;
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
/*     */   public static <T extends Iterable<?>> T checkContentsNotNull(T iterable, Object errorMessage) {
/* 261 */     if (containsOrIsNull((Iterable<?>)iterable)) {
/* 262 */       throw new NullPointerException(String.valueOf(errorMessage));
/*     */     }
/* 264 */     return iterable;
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
/*     */   public static <T extends Iterable<?>> T checkContentsNotNull(T iterable, String errorMessageTemplate, Object... errorMessageArgs) {
/* 287 */     if (containsOrIsNull((Iterable<?>)iterable)) {
/* 288 */       throw new NullPointerException(format(errorMessageTemplate, errorMessageArgs));
/*     */     }
/*     */     
/* 291 */     return iterable;
/*     */   }
/*     */   
/*     */   private static boolean containsOrIsNull(Iterable<?> iterable) {
/* 295 */     if (iterable == null) {
/* 296 */       return true;
/*     */     }
/*     */     
/* 299 */     if (iterable instanceof Collection) {
/* 300 */       Collection<?> collection = (Collection)iterable;
/*     */       try {
/* 302 */         return collection.contains(null);
/* 303 */       } catch (NullPointerException e) {
/*     */         
/* 305 */         return false;
/*     */       } 
/*     */     } 
/* 308 */     for (Object element : iterable) {
/* 309 */       if (element == null) {
/* 310 */         return true;
/*     */       }
/*     */     } 
/* 313 */     return false;
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
/*     */   public static void checkElementIndex(int index, int size) {
/* 330 */     checkElementIndex(index, size, "index");
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
/*     */   public static void checkElementIndex(int index, int size, String desc) {
/* 347 */     checkArgument((size >= 0), "negative size: %s", new Object[] { Integer.valueOf(size) });
/* 348 */     if (index < 0) {
/* 349 */       throw new IndexOutOfBoundsException(format("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) }));
/*     */     }
/*     */     
/* 352 */     if (index >= size) {
/* 353 */       throw new IndexOutOfBoundsException(format("%s (%s) must be less than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkPositionIndex(int index, int size) {
/* 371 */     checkPositionIndex(index, size, "index");
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
/*     */   public static void checkPositionIndex(int index, int size, String desc) {
/* 388 */     checkArgument((size >= 0), "negative size: %s", new Object[] { Integer.valueOf(size) });
/* 389 */     if (index < 0) {
/* 390 */       throw new IndexOutOfBoundsException(format("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) }));
/*     */     }
/*     */     
/* 393 */     if (index > size) {
/* 394 */       throw new IndexOutOfBoundsException(format("%s (%s) must not be greater than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void checkPositionIndexes(int start, int end, int size) {
/* 414 */     checkPositionIndex(start, size, "start index");
/* 415 */     checkPositionIndex(end, size, "end index");
/* 416 */     if (end < start) {
/* 417 */       throw new IndexOutOfBoundsException(format("end index (%s) must not be less than start index (%s)", new Object[] { Integer.valueOf(end), Integer.valueOf(start) }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String format(String template, Object... args) {
/* 437 */     StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
/*     */     
/* 439 */     int templateStart = 0;
/* 440 */     int i = 0;
/* 441 */     while (i < args.length) {
/* 442 */       int placeholderStart = template.indexOf("%s", templateStart);
/* 443 */       if (placeholderStart == -1) {
/*     */         break;
/*     */       }
/* 446 */       builder.append(template.substring(templateStart, placeholderStart));
/* 447 */       builder.append(args[i++]);
/* 448 */       templateStart = placeholderStart + 2;
/*     */     } 
/* 450 */     builder.append(template.substring(templateStart));
/*     */ 
/*     */     
/* 453 */     if (i < args.length) {
/* 454 */       builder.append(" [");
/* 455 */       builder.append(args[i++]);
/* 456 */       while (i < args.length) {
/* 457 */         builder.append(", ");
/* 458 */         builder.append(args[i++]);
/*     */       } 
/* 460 */       builder.append("]");
/*     */     } 
/*     */     
/* 463 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\google\gdat\\util\common\base\Preconditions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */