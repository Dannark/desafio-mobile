package br.com.dannark.desafio_mobile_daniel.Utilits;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import br.com.dannark.desafio_mobile_daniel.Entity.Product;

public class PHPConnection extends AsyncTask<Void, Void, Boolean> {

    String urlString;

    private final String TAG = "PHPConnection";
    private Context context;
    Resposta res;

    String Query;
    int Offset, Size;

    public PHPConnection(Context context, String urlString, String Query, int Offset, int Size, Resposta res) {
        this.urlString = urlString;
        this.context = context;
        this.Query = Query;
        this.Offset = Offset;
        this.Size = Size;
        this.res = res;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean status = false;

        String response = "";

        try {
            response = performPostCall(urlString, new HashMap<String, String>() {

                private static final long serialVersionUID = 1L;

                {
                    put("Accept", "application/json");
                    put("Content-Type", "application/json");
                }
            });

        } catch (Exception e) {
            // displayLoding(false);

            Log.e(TAG, "Error ..."+e.getMessage());
        }

        if (!response.equalsIgnoreCase("")) {
            try {

                ArrayList<Product> products = new ArrayList<>();

                JSONObject jRoot = new JSONObject(response);
                JSONArray Products = jRoot.getJSONArray("Products");

                for (int i = 0; i < Products.length(); i++) {
                    JSONObject produto = Products.getJSONObject(i);
                    JSONObject Skus = produto.getJSONArray("Skus").getJSONObject(0);

                    String SkusName = Skus.getString("Name");

                    //PRODUCT DETAILS
                    JSONObject Sellers = Skus.getJSONArray("Sellers").getJSONObject(0);
                    String nameSeller = Sellers.getString("Name");
                    Double price = Sellers.getDouble("Price");
                    Double listPrice = Sellers.getDouble("ListPrice");

                    //BEST INSTALLMENT
                    String count = "";
                    String value = "";
                    String total = "";
                    String rate = "";
                    if(Sellers.isNull("BestInstallment") == false) {
                        JSONObject BestInstallment = Sellers.getJSONObject("BestInstallment");
                        count = BestInstallment.getString("Count");
                        value = BestInstallment.getString("Value");
                        total = BestInstallment.getString("Total");
                        rate = BestInstallment.getString("Rate");
                    }

                    //IMAGES
                    ArrayList<String> imagesURLs = new ArrayList<>();
                    JSONArray productImages = Skus.getJSONArray("Images");
                    for (int x = 0; x < productImages.length(); x++) {
                        JSONObject productImage = productImages.getJSONObject(x);

                        String imageTag =  productImage.getString("ImageTag");
                        imagesURLs.add(imageTag);
                    }

                    products.add(new Product(SkusName,nameSeller,price,listPrice,count,value,total,rate,imagesURLs));

                }

                res.callBack(products);

            } catch (JSONException e) {
                // displayLoding(false);
                e.printStackTrace();
                //Log.e(TAG, "Error " + e.getMessage());
            }
        } else {

            status = false;
        }

        return status;
    }

    @Override
    protected void onPostExecute(Boolean result) {

    }

    public String performPostCall(String requestURL, HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            /*conn.setReadTimeout(context.getResources().getInteger(
                    R.integer.maximum_timeout_to_server));
            conn.setConnectTimeout(context.getResources().getInteger(
                    R.integer.maximum_timeout_to_server));*/
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json");

            /*
             * JSON
             */

            JSONObject root = new JSONObject();

            root.put("Query", Query);
            root.put("Offset", Offset);
            root.put("Size", Size);

            String str = root.toString();
            byte[] outputBytes = str.getBytes("UTF-8");
            OutputStream os = conn.getOutputStream();
            os.write(outputBytes);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}