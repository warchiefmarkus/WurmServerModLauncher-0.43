/*     */ package com.sun.tools.xjc.addon.locator;
/*     */ 
/*     */ import com.sun.codemodel.JAssignmentTarget;
/*     */ import com.sun.codemodel.JDefinedClass;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldVar;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JVar;
/*     */ import com.sun.tools.xjc.BadCommandLineException;
/*     */ import com.sun.tools.xjc.Options;
/*     */ import com.sun.tools.xjc.Plugin;
/*     */ import com.sun.tools.xjc.outline.ClassOutline;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.Locatable;
/*     */ import com.sun.xml.bind.annotation.XmlLocation;
/*     */ import java.io.IOException;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SourceLocationAddOn
/*     */   extends Plugin
/*     */ {
/*     */   private static final String fieldName = "locator";
/*     */   
/*     */   public String getOptionName() {
/*  66 */     return "Xlocator";
/*     */   }
/*     */   
/*     */   public String getUsage() {
/*  70 */     return "  -Xlocator          :  enable source location support for generated code";
/*     */   }
/*     */   
/*     */   public int parseArgument(Options opt, String[] args, int i) throws BadCommandLineException, IOException {
/*  74 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean run(Outline outline, Options opt, ErrorHandler errorHandler) {
/*  84 */     for (ClassOutline ci : outline.getClasses()) {
/*  85 */       JDefinedClass impl = ci.implClass;
/*  86 */       if (ci.getSuperClass() == null) {
/*  87 */         JFieldVar jFieldVar = impl.field(2, Locator.class, "locator");
/*  88 */         jFieldVar.annotate(XmlLocation.class);
/*  89 */         jFieldVar.annotate(XmlTransient.class);
/*     */         
/*  91 */         impl._implements(Locatable.class);
/*     */         
/*  93 */         impl.method(1, Locator.class, "sourceLocation").body()._return((JExpression)jFieldVar);
/*     */         
/*  95 */         JMethod setter = impl.method(1, void.class, "setSourceLocation");
/*  96 */         JVar $newLoc = setter.param(Locator.class, "newLocator");
/*  97 */         setter.body().assign((JAssignmentTarget)jFieldVar, (JExpression)$newLoc);
/*     */       } 
/*     */     } 
/*     */     
/* 101 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\addon\locator\SourceLocationAddOn.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */