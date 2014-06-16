package com.anylogic.tilemaplib;

import java.awt.Graphics;

abstract interface IOverlay
{
    public abstract void draw(Graphics paramGraphics, TileMapPanel paramTileMapPanel);

    public abstract boolean isVisible();
}



/* Location:           D:\Universidad Nacional\SEPRO\3.Programa\Plataformas\AnyLogic\Cadena de Suministro Industria Panificadora Palmira\SCBIP\GIS_alternatives_for_AnyLogic\TileGISLibrary\

 * Qualified Name:     com.anylogic.tilemaplib.IOverlay

 * JD-Core Version:    0.7.0.1

 */