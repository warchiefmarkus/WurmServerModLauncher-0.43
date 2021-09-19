/*    */ package com.wurmonline.website.news;
/*    */ 
/*    */ import com.wurmonline.website.Block;
/*    */ import com.wurmonline.website.LoginInfo;
/*    */ import java.io.PrintWriter;
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
/*    */ public class SubmitNewsBlock
/*    */   extends Block
/*    */ {
/*    */   public void write(HttpServletRequest req, PrintWriter out, LoginInfo loginInfo) {
/* 27 */     out.print("<b>Post news</b><br>");
/* 28 */     out.print("<br>");
/* 29 */     out.print("<form method=\"POST\" action=\"" + req.getRequestURL().toString() + "\">");
/* 30 */     out.print("<input type=\"hidden\" name=\"section\" value=\"news\">");
/* 31 */     out.print("<input type=\"hidden\" name=\"action\" value=\"submitnews\">");
/* 32 */     out.print("Title:<br><input type=\"text\" name=\"title\" size=\"60\" maxlength=\"128\"><br>");
/* 33 */     out.print("Text:<br><textarea name=\"text\" cols=\"60\" rows=\"20\"></textarea><br>");
/* 34 */     out.print("<input type=\"submit\" value=\"Post\">");
/* 35 */     out.print("</form>");
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\website\news\SubmitNewsBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */