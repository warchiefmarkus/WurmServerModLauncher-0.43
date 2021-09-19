/*     */ package org.controlsfx.tools;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import javafx.scene.Node;
/*     */ import javafx.stage.Window;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utils
/*     */ {
/*     */   public static Window getWindow(Object owner) throws IllegalArgumentException {
/*  46 */     if (owner == null) {
/*  47 */       Window window = null;
/*     */ 
/*     */       
/*  50 */       Iterator<Window> windows = Window.impl_getWindows();
/*  51 */       while (windows.hasNext()) {
/*  52 */         window = windows.next();
/*  53 */         if (window.isFocused() && !(window instanceof javafx.stage.PopupWindow)) {
/*     */           break;
/*     */         }
/*     */       } 
/*  57 */       return window;
/*  58 */     }  if (owner instanceof Window)
/*  59 */       return (Window)owner; 
/*  60 */     if (owner instanceof Node) {
/*  61 */       return ((Node)owner).getScene().getWindow();
/*     */     }
/*  63 */     throw new IllegalArgumentException("Unknown owner: " + owner.getClass());
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
/*     */   public static final String getExcelLetterFromNumber(int number) {
/*  80 */     String letter = "";
/*     */ 
/*     */     
/*  83 */     while (number >= 0) {
/*  84 */       int remainder = number % 26;
/*  85 */       letter = (char)(remainder + 65) + letter;
/*  86 */       number = number / 26 - 1;
/*     */     } 
/*     */     
/*  89 */     return letter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double clamp(double min, double value, double max) {
/*  97 */     if (value < min) return min; 
/*  98 */     if (value > max) return max; 
/*  99 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double nearest(double less, double value, double more) {
/* 108 */     double lessDiff = value - less;
/* 109 */     double moreDiff = more - value;
/* 110 */     if (lessDiff < moreDiff) return less; 
/* 111 */     return more;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\tools\Utils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */