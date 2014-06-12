/*   1:    */ package com.anylogic.tilemaplib;
/*   2:    */ 
/*   3:    */ import com.xj.anylogic.engine.presentation.UtilitiesColor;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/*   4:    */
/*   5:    */
/*   6:    */
/*   7:    */
/*   8:    */
/*   9:    */
/*  10:    */ 
/*  11:    */ public class PointOverlay
/*  12:    */   implements IOverlay, Serializable
/*  13:    */ {
/*  14: 61 */   private Color color = UtilitiesColor.gold;
/*  15:    */   Point2D.Double point;
/*  16: 63 */   private int diameter = 10;
/*  17: 64 */   private String name = null;
/*  18:    */   private static final long serialVersionUID = 1L;
/*  19:    */   
/*  20:    */   public PointOverlay() {}
/*  21:    */   
/*  22:    */   public PointOverlay(Point2D.Double point, String name)
/*  23:    */   {
/*  24: 72 */     this.point = point;
/*  25: 73 */     this.name = name;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public PointOverlay(double lon, double lat, String name)
/*  29:    */   {
/*  30: 77 */     this.point = new Point2D.Double(lon, lat);
/*  31: 78 */     this.name = name;
/*  32:    */   }
/*  33:    */   
/*  34:    */   public void setPoint(Point2D.Double point)
/*  35:    */   {
/*  36: 82 */     this.point = point;
/*  37:    */   }
/*  38:    */   
/*  39:    */   public Point2D.Double getPoint()
/*  40:    */   {
/*  41: 86 */     return this.point;
/*  42:    */   }
/*  43:    */   
/*  44:    */   public void setColor(Color c)
/*  45:    */   {
/*  46: 90 */     this.color = c;
/*  47:    */   }
/*  48:    */   
/*  49:    */   public boolean isVisible()
/*  50:    */   {
/*  51: 96 */     return true;
/*  52:    */   }
/*  53:    */   
/*  54:    */   public void draw(Graphics g, TileMapPanel tmp)
/*  55:    */   {
/*  56:100 */     if (this.point == null) {
/*  57:101 */       return;
/*  58:    */     }
/*  59:103 */     int x = MercatorProj.LonToX(this.point.x, tmp.zoom);
/*  60:104 */     int y = MercatorProj.LatToY(this.point.y, tmp.zoom);
/*  61:105 */     x = x - tmp.centerX + tmp.getWidth() / 2;
/*  62:106 */     y = y - tmp.centerY + tmp.getHeight() / 2;
/*  63:107 */     if ((x < 0) || (y < 0) || (x > tmp.getWidth()) || (y > tmp.getHeight())) {
/*  64:108 */       return;
/*  65:    */     }
/*  66:110 */     Color oldColor = g.getColor();
/*  67:111 */     g.setColor(this.color);
/*  68:    */     
/*  69:    */ 
/*  70:    */ 
/*  71:    */ 
/*  72:    */ 
/*  73:    */ 
/*  74:    */ 
/*  75:    */ 
/*  76:    */ 
/*  77:    */ 
/*  78:122 */     g.fillOval(x, y, this.diameter, this.diameter);
/*  79:123 */     g.setColor(Color.black);
/*  80:124 */     g.drawOval(x, y, this.diameter, this.diameter);
/*  81:125 */     if (this.name != null)
/*  82:    */     {
/*  83:126 */       int nameX = x + this.diameter + 2;
/*  84:127 */       Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(this.name, g);
/*  85:128 */       int textHeight = (int)stringBounds.getHeight() - 5;
/*  86:129 */       int nameY = y - textHeight / 2;
/*  87:130 */       g.drawString(this.name, nameX, nameY);
/*  88:    */     }
/*  89:133 */     g.setColor(oldColor);
/*  90:    */   }
/*  91:    */   
/*  92:    */   public String toString()
/*  93:    */   {
/*  94:143 */     return super.toString();
/*  95:    */   }
/*  96:    */ }



/* Location:           D:\Universidad Nacional\SEPRO\3.Programa\Plataformas\AnyLogic\Cadena de Suministro Industria Panificadora Palmira\SCBIP\GIS_alternatives_for_AnyLogic\TileGISLibrary\

 * Qualified Name:     com.anylogic.tilemaplib.PointOverlay

 * JD-Core Version:    0.7.0.1

 */