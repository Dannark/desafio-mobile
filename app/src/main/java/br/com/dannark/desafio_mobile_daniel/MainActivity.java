package br.com.dannark.desafio_mobile_daniel;

import android.content.Context;
import android.os.Bundle;
import android.os.Debug;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.dannark.desafio_mobile_daniel.Adapters.MyAdapter;
import br.com.dannark.desafio_mobile_daniel.Entity.Product;
import br.com.dannark.desafio_mobile_daniel.Utilits.PHPConnection;
import br.com.dannark.desafio_mobile_daniel.Utilits.Resposta;

public class MainActivity extends AppCompatActivity implements Resposta {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyAdapter adapter;


    ArrayList<Product> products = new ArrayList<>();

    private boolean loading = true;
    Context context;

    public int indexPage = 0;

    EditText searchBox;
    ProgressBar loader;
    ProgressBar bottomloader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplication();

        //INSTANTIATE / SETUP LIST
        recyclerView = (RecyclerView) findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(products);
        recyclerView.setAdapter(adapter);
        loadMoreItems();

        new PHPConnection(context,getString(R.string.ip2)+indexPage,"", indexPage, 10, MainActivity.this).execute();

        searchBox = (EditText) findViewById(R.id.searchBox);
        loader = (ProgressBar) findViewById(R.id.loader);
        bottomloader = (ProgressBar) findViewById(R.id.bottomloader);
    }

    void loadMoreItems(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {

                    if (loading)
                    {
                        loading = false;

                        // CARREGA MAIS ITENS
                        indexPage+=10;

                        new PHPConnection(context,getString(R.string.ip2)+indexPage,"", indexPage, 10, MainActivity.this).execute();
                        bottomloader.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public void callBack(Object param) {
        products.addAll( (ArrayList<Product>)param );
        Log.e("MainActivity","Total:"+products.size()+" indexPage="+indexPage);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();

                loader.setVisibility(View.GONE);
                bottomloader.setVisibility(View.GONE);
                loading = true;
            }
        });
    }


    public void search(View v){
        String inputText = searchBox.getText().toString();

        products.clear();

        new PHPConnection(context,getString(R.string.ip1), inputText, indexPage, 10, MainActivity.this).execute();

    }
}
