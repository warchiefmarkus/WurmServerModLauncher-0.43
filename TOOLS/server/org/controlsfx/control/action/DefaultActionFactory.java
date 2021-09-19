/*     */ package org.controlsfx.control.action;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import javafx.scene.Node;
/*     */ import javafx.scene.image.Image;
/*     */ import javafx.scene.image.ImageView;
/*     */ import javafx.scene.input.KeyCombination;
/*     */ import org.controlsfx.glyphfont.Glyph;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultActionFactory
/*     */   implements AnnotatedActionFactory
/*     */ {
/*     */   public AnnotatedAction createAction(ActionProxy annotation, Method method, Object target) {
/*     */     AnnotatedAction action;
/*  54 */     if (method.isAnnotationPresent((Class)ActionCheck.class)) {
/*  55 */       action = new AnnotatedCheckAction(annotation.text(), method, target);
/*     */     } else {
/*  57 */       action = new AnnotatedAction(annotation.text(), method, target);
/*     */     } 
/*     */     
/*  60 */     configureAction(annotation, action);
/*     */     
/*  62 */     return action;
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
/*     */   protected void configureAction(ActionProxy annotation, AnnotatedAction action) {
/*  74 */     Node graphic = resolveGraphic(annotation);
/*  75 */     action.setGraphic(graphic);
/*     */ 
/*     */     
/*  78 */     String longText = annotation.longText().trim();
/*  79 */     if (graphic != null) {
/*  80 */       action.setLongText(longText);
/*     */     }
/*     */ 
/*     */     
/*  84 */     String acceleratorText = annotation.accelerator().trim();
/*  85 */     if (!acceleratorText.isEmpty()) {
/*  86 */       action.setAccelerator(KeyCombination.keyCombination(acceleratorText));
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
/*     */   protected Node resolveGraphic(ActionProxy annotation) {
/* 100 */     String graphicDef = annotation.graphic().trim();
/* 101 */     if (!graphicDef.isEmpty()) {
/*     */       
/* 103 */       String[] def = graphicDef.split("\\>");
/* 104 */       if (def.length == 1) return (Node)new ImageView(new Image(def[0])); 
/* 105 */       switch (def[0]) { case "font":
/* 106 */           return (Node)Glyph.create(def[1]);
/* 107 */         case "image": return (Node)new ImageView(new Image(def[1])); }
/* 108 */        throw new IllegalArgumentException(String.format("Unknown ActionProxy graphic protocol: %s", new Object[] { def[0] }));
/*     */     } 
/*     */     
/* 111 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\controlsfx\control\action\DefaultActionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */