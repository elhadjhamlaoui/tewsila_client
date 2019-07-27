package com.tewsila.client;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by elhadj on 07/07/2018.
 */

public class Poly {

    ArrayList<ArrayList<LatLng>> list = new ArrayList<>();
    public  Poly(Context c){

        String[] s = {c.getResources().getString(R.string.oran),c.getResources().getString(R.string.alger),c.getResources().getString(R.string.mosta),c.getResources().getString(R.string.mascara),c.getResources().getString(R.string.bab)};




        for (int i=0;i<5;i++){
            JSONArray jsonArray = getJson(s[i]);
            ArrayList<LatLng> city = new ArrayList<>();
            for (int  j=0;j<jsonArray.length();j++){
                try {
                    JSONArray array = jsonArray.getJSONArray(j);
                    city.add(new LatLng(array.getDouble(1),array.getDouble(0)));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            list.add(city);
        }



    }

    public boolean contains(LatLng latLng){
       /* boolean bool=false;
        int i=0;
        while (i<5&&!bool){
            bool = PolyUtil.containsLocation (latLng, list.get(i), true);
            i++;
        }
       return bool;
       */
        return true;

    }
    public boolean contains_oran(LatLng latLng){
       /* boolean bool=PolyUtil.containsLocation (latLng, list.get(0), true);
        return bool;*/
        return true;
    }

    private JSONArray getJson(String s){
        try {
            return new JSONObject(s).getJSONArray("geometries").getJSONObject(0).getJSONArray("coordinates").getJSONArray(0).getJSONArray(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}
