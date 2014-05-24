package com.mobezer.jet;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;


public class TextWrapper{
   public String Text;
   public Vector2 Position;
   int width;
   int height;
   BitmapFont fnt;
   public TextWrapper(String txt,BitmapFont font,Vector2 pos){
	 fnt=font;
     Text=txt;
     Position=pos;
   }

   public void Draw(SpriteBatch sp){
       width=(int)fnt.getBounds(Text).width; //Get the width of the text we draw using the current font
       height=(int)fnt.getBounds(Text).height; //Get the height of the text we draw using the current font
       fnt.draw(sp,Text,Position.x-width/2, // Get center value in x direction
                Position.y-height/2 //Get center value in y direction
                );
   }

public float GetWidth() {
	TextBounds bnd =fnt.getBounds(Text);
	return bnd.width;
}
public float GetHeight() {
	TextBounds bnd =fnt.getBounds(Text);
	return bnd.width;
}

}