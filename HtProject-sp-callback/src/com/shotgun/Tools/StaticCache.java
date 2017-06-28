package com.shotgun.Tools;

import java.util.ArrayList;

public abstract class StaticCache{
    static ArrayList<StaticCache> _allCache;

   public enum Static_Cache_Status
    {
        Idel,
        Loading,
        AllLoad
    }
    
    public static void clearAllCache()
    {
        if (_allCache == null)
            return;
        StaticCache[] all = _allCache.toArray(new StaticCache[0]);
       for (StaticCache c : all)
        {
            c.clearCache();
        }
    }

    public abstract void clearCache();

    protected void add(StaticCache sc)
    {
        if (sc == null)
            return;
        if (_allCache == null)
            _allCache = new ArrayList<StaticCache>();
        if (_allCache.contains(sc))
            return;
        _allCache.add(sc);
    }

    protected static StaticCache[] GetAllCache()
    {
        if (_allCache == null)
            return null;
        return _allCache.toArray(new StaticCache[0]);
    }

   protected  void remove(StaticCache staticCache)
    {
        if (_allCache == null)
            return;
        _allCache.remove(staticCache);
    }
}
