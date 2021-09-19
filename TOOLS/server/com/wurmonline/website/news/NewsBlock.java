/*    */ package com.wurmonline.website.news;
/*    */ 
/*    */ import com.wurmonline.website.Block;
/*    */ import com.wurmonline.website.LoginInfo;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Date;
/*    */ import javax.servlet.http.HttpServletRequest;
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
/*    */ public class NewsBlock
/*    */   extends Block
/*    */ {
/*    */   private News news;
/*    */   
/*    */   public NewsBlock(News aNews) {
/* 32 */     this.news = aNews;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void write(HttpServletRequest req, PrintWriter out, LoginInfo loginInfo) {
/* 38 */     out.print("<b>" + this.news.getTitle() + "</b> posted " + new Date(this.news.getTimestamp()) + " by " + this.news.getPostedBy() + "<br>");
/*    */     
/* 40 */     out.print("<br>");
/* 41 */     out.print(this.news.getText());
/*    */     
/* 43 */     if (loginInfo != null && loginInfo.isAdmin())
/*    */     {
/* 45 */       out.print("<hr>ADMIN [ <a href=\"news.jsp?id=" + this.news.getId() + "&action=edit\">Edit</a> | <a href=\"news.jsp?id=" + this.news.getId() + "&action=delete\">Delete</a> ]");
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected News getNews() {
/* 55 */     return this.news;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\website\news\NewsBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */