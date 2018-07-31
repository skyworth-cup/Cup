package com.yang.cupwar.utils;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StarUtil implements DAO<StarRecord> {


    public int getStars(SharedPreferences sharedPreferences, Integer order) {
        int stars = 0;
        stars = get(sharedPreferences, order, -1).getStars();
        return stars;
    }

    @Override
    public StarRecord get(SharedPreferences sharedPreferences, Integer key, Integer description) {
        int order = Integer.valueOf(key);
        int stars = sharedPreferences.getInt(String.valueOf(key), description.intValue());
        StarRecord starRecord = new StarRecord(order, stars);
        return new StarRecord(starRecord);
    }


    @Override
    public void update(SharedPreferences sharedPreferences, Map<Integer, Integer> starRecords) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Iterator iterator = starRecords.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = (Map.Entry) iterator.next();
            editor.putInt(String.valueOf(entry.getKey()), entry.getValue());
            editor.apply();
        }
    }

    public void updateOne(SharedPreferences sharedPreferences, Integer order, Integer stars) {
        Map map = new HashMap<Integer, Integer>();
        map.put(order, stars);
        update(sharedPreferences, map);
    }

    @Override
    public List<StarRecord> getList(SharedPreferences sharedPreferences, Map<Integer, Integer> key_to_description) {
        List starList = new ArrayList<StarRecord>();

        for (Map.Entry<Integer, Integer> entryTemp : key_to_description.entrySet()) {
            starList.add(get(sharedPreferences, entryTemp.getKey(), entryTemp.getValue()));
        }

        return starList;
    }
}
