package com.yang.cupwar.utils;

import android.content.SharedPreferences;
import android.widget.ListView;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Vincent
 *
 * @param <T> DAO处理的具体类型
 */
public interface DAO<T> {


    /**
     * Insert Delete Update
     * @param sharedPreferences
     * @param starRecords
     */
    void update(SharedPreferences sharedPreferences, Map<Integer, Integer> starRecords);


    /**
     * get a enity
     * @param sharedPreferences
     * @param key
     * @param description
     * @return
     */
    T get(SharedPreferences sharedPreferences, Integer key, Integer description);

    /**
     * Select a  list of enity
     * @param sharedPreferences
     * @param key_to_description
     * @return
     */
    List<T> getList(SharedPreferences sharedPreferences, Map<Integer, Integer> key_to_description);


}
