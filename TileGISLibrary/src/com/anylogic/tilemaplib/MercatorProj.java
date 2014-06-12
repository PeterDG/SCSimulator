/*   1:    */ package com.anylogic.tilemaplib;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ 
/*   5:    */ public class MercatorProj
/*   6:    */   implements Serializable
/*   7:    */ {
/*   8: 68 */   private static int TILE_SIZE = 256;
/*   9:    */   public static final double MAX_LAT = 85.051128779806589D;
/*  10:    */   public static final double MIN_LAT = -85.051128779806589D;
/*  11:    */   private static final long serialVersionUID = 1L;
/*  12:    */   
/*  13:    */   public static int LonToX(double lon, int z)
/*  14:    */   {
/*  15:120 */     int rPx = 128 * (1 << z);
/*  16:121 */     return (int)Math.round(rPx / 3.141592653589793D * Math.toRadians(lon) + rPx);
/*  17:    */   }
/*  18:    */   
/*  19:    */   public static double XtoLon(int x, int z)
/*  20:    */   {
/*  21:126 */     int rPx = 128 * (1 << z);
/*  22:127 */     return Math.toDegrees(3.141592653589793D / rPx * (x - rPx));
/*  23:    */   }
/*  24:    */   
/*  25:    */   public static int LatToY(double lat, int z)
/*  26:    */   {
/*  27:132 */     int rPx = 128 * (1 << z);
/*  28:133 */     if (lat > 89.5D) {
/*  29:134 */       lat = 89.5D;
/*  30:    */     }
/*  31:136 */     if (lat < -89.5D) {
/*  32:137 */       lat = -89.5D;
/*  33:    */     }
/*  34:139 */     double temp = 1.0D;
/*  35:140 */     double es = 0.0D;
/*  36:141 */     double eccent = 0.0D;
/*  37:142 */     double phi = Math.toRadians(lat);
/*  38:143 */     double sinphi = Math.sin(phi);
/*  39:144 */     double con = 0.0D;
/*  40:145 */     double com = 0.0D;
/*  41:146 */     con = 1.0D;
/*  42:147 */     double ts = Math.tan(0.5D * (1.570796326794897D - phi)) / con;
/*  43:148 */     int y = (int)Math.round(rPx + rPx / 3.141592653589793D * Math.log(ts));
/*  44:149 */     return y;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public static double YtoLat(int y, int z)
/*  48:    */   {
/*  49:153 */     int rPx = 128 * (1 << z);
/*  50:154 */     int yT = y - rPx;
/*  51:155 */     double alpha = 1.570796326794897D - 2.0D * Math.atan(Math.exp(3.141592653589793D * yT / rPx));
/*  52:156 */     return Math.toDegrees(alpha);
/*  53:    */   }
/*  54:    */ }


/* Location:           D:\Universidad Nacional\SEPRO\3.Programa\Plataformas\AnyLogic\Cadena de Suministro Industria Panificadora Palmira\SCBIP\GIS_alternatives_for_AnyLogic\TileGISLibrary\
 * Qualified Name:     com.anylogic.tilemaplib.MercatorProj
 * JD-Core Version:    0.7.0.1
 */