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

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import br.com.dannark.desafio_mobile_daniel.Adapters.MyAdapter;
import br.com.dannark.desafio_mobile_daniel.Entity.Product;
import br.com.dannark.desafio_mobile_daniel.Utilits.PHPConnection;
import br.com.dannark.desafio_mobile_daniel.Utilits.Resposta;

public class MainActivity extends AppCompatActivity implements Resposta {

    ArrayList<Product> products = new ArrayList<>();

    private boolean loading = true;
    Context context;

    public int indexPage = 0;

    EditText searchBox;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplication();

        new PHPConnection(context,"https://desafio.mobfiq.com.br/Search/Criteria",
                "", indexPage, 10, MainActivity.this).execute();


        searchBox = (EditText) findViewById(R.id.searchBox);
        loader = (ProgressBar) findViewById(R.id.loader);
    }

    void updateItems(){

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        MyAdapter adapter = new MyAdapter(products);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if(dy > 0) //check for scroll down
                {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisiblesItems = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();

                    if (loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                        {
                            loading = false;

                            Log.e("MainActivity","CARREGA MAIS ITENS");
                            // CARREGA MAIS ITENS
                            indexPage+=10;
                            new PHPConnection(context,"https://desafio.mobfiq.com.br/Search/Criteria",
                                    "", indexPage, 10, MainActivity.this).execute();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void callBack(Object param) {
        products.addAll( (ArrayList<Product>)param );

        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                updateItems();
                loading = true;
                loader.setVisibility(View.GONE);
            }
        });
    }


    public void search(View v){
        String inputText = searchBox.getText().toString();

        products.clear();

        Log.e("MainActivity","inputText="+inputText);
        new PHPConnection(context,"https://desafio.mobfiq.com.br/Search/Criteria",
                inputText, indexPage, 10, MainActivity.this).execute();

    }
}
