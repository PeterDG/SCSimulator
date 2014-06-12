/*   1:    */ package com.anylogic.tilemaplib;
/*   2:    */ 
/*   3:    */ import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/*   4:    */
/*   5:    */
/*   6:    */
/*   7:    */
/*   8:    */
/*   9:    */
/*  10:    */
/*  11:    */
/*  12:    */ 
/*  13:    */ public class PathOverlay
/*  14:    */   implements IOverlay, Serializable
/*  15:    */ {
/*  16: 61 */   private Color color = new Color(100, 149, 237, 180);
/*  17: 62 */   private Stroke stroke = new BasicStroke(2.0F);
/*  18:    */   List<Point2D.Double> points;
/*  19:    */   private static final long serialVersionUID = 1L;
/*  20:    */   
/*  21:    */   public PathOverlay() {}
/*  22:    */   
/*  23:    */   public PathOverlay(List<Point2D.Double> points)
/*  24:    */   {
/*  25: 72 */     this.points = points;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setPoints(List<Point2D.Double> points)
/*  29:    */   {
/*  30: 76 */     this.points = points;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setColor(Color c)
/*  34:    */   {
/*  35: 80 */     this.color = c;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public void setStroke(Stroke stroke)
/*  39:    */   {
/*  40: 84 */     this.stroke = stroke;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public boolean isVisible()
/*  44:    */   {
/*  45: 90 */     return true;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void draw(Graphics g, TileMapPanel tmp)
/*  49:    */   {
/*  50: 94 */     if (this.points == null) {
/*  51: 95 */       return;
/*  52:    */     }
/*  53: 97 */     int nP = this.points.size();
/*  54: 98 */     int[] px = new int[nP];
/*  55: 99 */     int[] py = new int[nP];
/*  56:    */     
/*  57:101 */     int ind = 0;
/*  58:102 */     for (Point2D.Double p : this.points)
/*  59:    */     {
/*  60:103 */       int x = MercatorProj.LonToX(p.x, tmp.zoom);
/*  61:104 */       int y = MercatorProj.LatToY(p.y, tmp.zoom);
/*  62:105 */       x = x - tmp.centerX + tmp.getWidth() / 2;
/*  63:106 */       y = y - tmp.centerY + tmp.getHeight() / 2;
/*  64:107 */       if ((x >= 0) && (y >= 0) && (x <= tmp.getWidth()) && (y <= tmp.getHeight()))
/*  65:    */       {
/*  66:109 */         px[ind] = x;
/*  67:110 */         py[ind] = y;
/*  68:    */         
/*  69:112 */         ind++;
/*  70:    */       }
/*  71:    */     }
/*  72:114 */     int[] px1 = Arrays.copyOf(px, ind);
/*  73:115 */     int[] py1 = Arrays.copyOf(py, ind);
/*  74:117 */     if (ind > 2)
/*  75:    */     {
/*  76:119 */       Color oldColor = g.getColor();
/*  77:120 */       g.setColor(this.color);
/*  78:    */       
/*  79:122 */       Stroke oldStroke = null;
/*  80:123 */       if ((g instanceof Graphics2D))
/*  81:    */       {
/*  82:124 */         Graphics2D g2 = (Graphics2D)g;
/*  83:125 */         oldStroke = g2.getStroke();
/*  84:126 */         g2.setStroke(this.stroke);
/*  85:    */       }
/*  86:130 */       g.drawPolyline(px1, py1, ind);
/*  87:    */       
/*  88:132 */       g.setColor(oldColor);
/*  89:133 */       if ((g instanceof Graphics2D)) {
/*  90:134 */         ((Graphics2D)g).setStroke(oldStroke);
/*  91:    */       }
/*  92:    */     }
/*  93:    */   }
/*  94:    */   
/*  95:    */   public List<Point2D.Double> getPoints()
/*  96:    */   {
/*  97:140 */     return this.points;
/*  98:    */   }
/*  99:    */   
/* 100:    */   public String toString()
/* 101:    */   {
/* 102:145 */     return super.toString();
/* 103:    */   }
/* 104:    */ }



/* Location:           D:\Universidad Nacional\SEPRO\3.Programa\Plataformas\AnyLogic\Cadena de Suministro Industria Panificadora Palmira\SCBIP\GIS_alternatives_for_AnyLogic\TileGISLibrary\

 * Qualified Name:     com.anylogic.tilemaplib.PathOverlay

 * JD-Core Version:    0.7.0.1

 */