/*    */ package com.wurmonline.website.news;
/*    */ 
/*    */ import com.wurmonline.website.Block;
/*    */ import com.wurmonline.website.LoginInfo;
/*    */ import com.wurmonline.website.Section;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
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
/*    */ public class NewsSection
/*    */   extends Section
/*    */ {
/* 29 */   private SubmitNewsBlock submitBlock = new SubmitNewsBlock();
/* 30 */   private List<NewsBlock> news = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getName() {
/* 39 */     return "News";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getId() {
/* 45 */     return "news";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Block> getBlocks(HttpServletRequest req, LoginInfo loginInfo) {
/* 51 */     List<Block> list = new ArrayList<>();
/*    */     
/* 53 */     if ("delete".equals(req.getParameter("action")))
/*    */     {
/* 55 */       if (loginInfo != null && loginInfo.isAdmin())
/*    */       {
/* 57 */         delete(req.getParameter("id"));
/*    */       }
/*    */     }
/*    */     
/* 61 */     list.addAll((Collection)this.news);
/*    */     
/* 63 */     if (loginInfo != null && loginInfo.isAdmin())
/*    */     {
/* 65 */       list.add(this.submitBlock);
/*    */     }
/*    */     
/* 68 */     return list;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void delete(String id) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void handlePost(HttpServletRequest req, LoginInfo loginInfo) {
/* 78 */     if (loginInfo != null && loginInfo.isAdmin()) {
/*    */       
/* 80 */       String title = req.getParameter("title");
/* 81 */       String text = req.getParameter("text");
/* 82 */       text = text.replaceAll("\r\n", "<br>");
/* 83 */       text = text.replaceAll("\r", "<br>");
/* 84 */       text = text.replaceAll("\n", "<br>");
/*    */       
/* 86 */       this.news.add(new NewsBlock(new News(title, text, loginInfo.getName())));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\website\news\NewsSection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */