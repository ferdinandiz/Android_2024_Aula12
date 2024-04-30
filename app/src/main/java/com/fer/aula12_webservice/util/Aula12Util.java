package com.fer.aula12_webservice.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Aula12Util {

   public static void desativaComponente(Context context, RelativeLayout relativeLayout) {
      ImageView imageView = (ImageView) relativeLayout.getChildAt(0);
      TextView textView = (TextView) relativeLayout.getChildAt(1);

      textView.setTextColor(Color.GRAY);

      String nomeImagem = context.getResources().getResourceEntryName(imageView.getId());
      int newImageResId = context.getResources().getIdentifier(nomeImagem + "_inativo", "drawable", context.getPackageName());
      imageView.setImageResource(newImageResId);
      relativeLayout.setEnabled(false);
   }
}
