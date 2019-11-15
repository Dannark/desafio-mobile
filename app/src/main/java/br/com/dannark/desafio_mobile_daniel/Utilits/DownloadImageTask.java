package br.com.dannark.desafio_mobile_daniel.Utilits;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

import br.com.dannark.desafio_mobile_daniel.Entity.Product;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    String urldisplay;
    Product product;

    public DownloadImageTask(ImageView bmImage, Product product, String urldisplay) {
        this.bmImage = bmImage;
        this.urldisplay = urldisplay;
        this.product = product;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("DownloadImageTask", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(final Bitmap result) {
        product.setImg(result);
        bmImage.setImageBitmap(product.getImg());

    }
}