/*     */ package javax.xml.bind.helpers;
/*     */ 
/*     */ import java.net.URL;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultValidationEventHandler
/*     */   implements ValidationEventHandler
/*     */ {
/*     */   public boolean handleEvent(ValidationEvent event) {
/*  41 */     if (event == null) {
/*  42 */       throw new IllegalArgumentException();
/*     */     }
/*     */ 
/*     */     
/*  46 */     String severity = null;
/*  47 */     boolean retVal = false;
/*  48 */     switch (event.getSeverity())
/*     */     { case 0:
/*  50 */         severity = Messages.format("DefaultValidationEventHandler.Warning");
/*  51 */         retVal = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  68 */         location = getLocation(event);
/*     */         
/*  70 */         System.out.println(Messages.format("DefaultValidationEventHandler.SeverityMessage", severity, event.getMessage(), location));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  77 */         return retVal;case 1: severity = Messages.format("DefaultValidationEventHandler.Error"); retVal = false; location = getLocation(event); System.out.println(Messages.format("DefaultValidationEventHandler.SeverityMessage", severity, event.getMessage(), location)); return retVal;case 2: severity = Messages.format("DefaultValidationEventHandler.FatalError"); retVal = false; location = getLocation(event); System.out.println(Messages.format("DefaultValidationEventHandler.SeverityMessage", severity, event.getMessage(), location)); return retVal; }  assert false : Messages.format("DefaultValidationEventHandler.UnrecognizedSeverity", Integer.valueOf(event.getSeverity())); String location = getLocation(event); System.out.println(Messages.format("DefaultValidationEventHandler.SeverityMessage", severity, event.getMessage(), location)); return retVal;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String getLocation(ValidationEvent event) {
/*  85 */     StringBuffer msg = new StringBuffer();
/*     */     
/*  87 */     ValidationEventLocator locator = event.getLocator();
/*     */     
/*  89 */     if (locator != null) {
/*     */       
/*  91 */       URL url = locator.getURL();
/*  92 */       Object obj = locator.getObject();
/*  93 */       Node node = locator.getNode();
/*  94 */       int line = locator.getLineNumber();
/*     */       
/*  96 */       if (url != null || line != -1) {
/*  97 */         msg.append("line " + line);
/*  98 */         if (url != null)
/*  99 */           msg.append(" of " + url); 
/* 100 */       } else if (obj != null) {
/* 101 */         msg.append(" obj: " + obj.toString());
/* 102 */       } else if (node != null) {
/* 103 */         msg.append(" node: " + node.toString());
/*     */       } 
/*     */     } else {
/* 106 */       msg.append(Messages.format("DefaultValidationEventHandler.LocationUnavailable"));
/*     */     } 
/*     */     
/* 109 */     return msg.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\helpers\DefaultValidationEventHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */