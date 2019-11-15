package br.com.dannark.desafio_mobile_daniel.Adapters;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.com.dannark.desafio_mobile_daniel.Entity.Product;
import br.com.dannark.desafio_mobile_daniel.R;
import br.com.dannark.desafio_mobile_daniel.Utilits.DownloadImageTask;
import br.com.dannark.desafio_mobile_daniel.Utilits.Resposta;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private ArrayList<Product> products;

    private static final String TAG = "MyAdapter";

    public MyAdapter(ArrayList<Product> products) {
        this.products = products;

    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder viewHolder, int i) {
        viewHolder.producName.setText(products.get(i).getName());
        viewHolder.lastprice.setText("R$ "+products.get(i).getListPrice());
        viewHolder.price.setText("R$ "+products.get(i).getPrice());
        viewHolder.discount.setText( (int)((1-(products.get(i).getPrice() / products.get(i).getListPrice()))*100) +"%");
        viewHolder.installments.setText(products.get(i).getCount() + "x de "+ products.get(i).getValue());



        viewHolder.img.setScaleType(ImageView.ScaleType.FIT_CENTER);

        String urlImage = products.get(i).getImagesURLs().get(0);
        urlImage = urlImage.replace("#width#", "300").replace("#height#", "300");

        viewHolder.img.setImageBitmap(products.get(i).getImg());
        if(products.get(i).getImg() == null) {
            //load image first time
            new DownloadImageTask(viewHolder.img, products.get(i), urlImage).execute();
        }

    }

    @Override
    public int getItemCount() {
        int tamanho = 0;
        if(products != null){
            tamanho = products.size();
        }

        return tamanho;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView producName;
        private ImageView img;

        private TextView lastprice;
        private TextView price;
        private TextView installments;
        private TextView discount;

        public ViewHolder(View view) {
            super(view);

            producName = (TextView)view.findViewById(R.id.producName);
            img = (ImageView) view.findViewById(R.id.img);

            lastprice = (TextView)view.findViewById(R.id.lastprice);
            price = (TextView)view.findViewById(R.id.price);
            installments = (TextView)view.findViewById(R.id.installments);
            discount = (TextView)view.findViewById(R.id.discount);
        }
    }
}