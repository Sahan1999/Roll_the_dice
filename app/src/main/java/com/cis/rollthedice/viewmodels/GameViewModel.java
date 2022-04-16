package com.cis.rollthedice.viewmodels;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cis.rollthedice.models.Dice;
import com.cis.rollthedice.remote.VollySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.cis.rollthedice.remote.ApiCall.BASEURL;

public class GameViewModel extends ViewModel {

    private String type, value;
    private RequestQueue mQueue;
    private Dice dice;
    private MutableLiveData<Dice> diceR;

    public LiveData<Dice> getData(Context context){
        diceR = new MutableLiveData<>();
        loadData(context);
        return diceR;
    }

    private void loadData(final Context context) {
        mQueue = VollySingleton.getInstance(context).getRequestQueue();
        Uri baseUri = Uri.parse(BASEURL);
        Uri.Builder builder = baseUri.buildUpon();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, builder.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("VolleyResponse", "response: " + response);

                            JSONArray articles = response.getJSONArray("dice");
                            Log.d("articles", "response: " + articles);

                            for (int i = 0; i < articles.length(); i++) {
                                JSONObject current = articles.getJSONObject(i);
                                value = current.getString("value");
                                type = current.getString("type");
                                Log.d("value", "response: " + value);
                                dice = new Dice(Integer.parseInt(value),type);
                            }
                            diceR.setValue(dice);
                            Log.d("return", "dice: " + dice.getValue());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

        mQueue.add(request);
    }
}
