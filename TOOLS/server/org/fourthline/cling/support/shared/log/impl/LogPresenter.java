/*    */ package org.fourthline.cling.support.shared.log.impl;
/*    */ 
/*    */ import javax.annotation.PreDestroy;
/*    */ import javax.enterprise.context.ApplicationScoped;
/*    */ import javax.enterprise.event.Event;
/*    */ import javax.inject.Inject;
/*    */ import javax.swing.SwingUtilities;
/*    */ import org.fourthline.cling.support.shared.TextExpand;
/*    */ import org.fourthline.cling.support.shared.log.LogView;
/*    */ import org.seamless.swing.logging.LogMessage;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ApplicationScoped
/*    */ public class LogPresenter
/*    */   implements LogView.Presenter
/*    */ {
/*    */   @Inject
/*    */   protected LogView view;
/*    */   @Inject
/*    */   protected Event<TextExpand> textExpandEvent;
/*    */   
/*    */   public void init() {
/* 41 */     this.view.setPresenter(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onExpand(LogMessage logMessage) {
/* 46 */     this.textExpandEvent.fire(new TextExpand(logMessage.getMessage()));
/*    */   }
/*    */   
/*    */   @PreDestroy
/*    */   public void destroy() {
/* 51 */     SwingUtilities.invokeLater(new Runnable() {
/*    */           public void run() {
/* 53 */             LogPresenter.this.view.dispose();
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void pushMessage(final LogMessage message) {
/* 60 */     SwingUtilities.invokeLater(new Runnable() {
/*    */           public void run() {
/* 62 */             LogPresenter.this.view.pushMessage(message);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\fourthline\cling\support\shared\log\impl\LogPresenter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */