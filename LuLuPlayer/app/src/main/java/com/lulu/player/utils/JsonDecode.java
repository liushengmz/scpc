package com.lulu.player.utils;

import com.lulu.player.model.Video;
import com.lulu.player.model.VideoListRsp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxc
 * @time 2016/9/28 0028上午 9:29
 */
public class JsonDecode {

    private VideoListRsp videoListRsp;


    public VideoListRsp decode(String str) {
        JSONObject json;
        try {
            json = new JSONObject(str);
            videoListRsp = new VideoListRsp();

            videoListRsp.setCount(json.getInt("count"));
            videoListRsp.setLevelId(json.getInt("levelId"));
            videoListRsp.setStatus(json.getInt("status"));

            List<Video> topVideos = new ArrayList<>();
            List<Video> videos = new ArrayList<>();

            JSONArray top = json.getJSONArray("topVideos");
            for (int i = 0; i < top.length(); i++) {
                int id = top.getJSONObject(i).getInt(
                        "id");
                String imgs = top.getJSONObject(i).getString(
                        "imgUrl");
                String name = top.getJSONObject(i).getString(
                        "name");
                String url = top.getJSONObject(i).getString(
                        "videoUrl");
                Video video = new Video(id, imgs, name, url);
                topVideos.add(video);
            }
            videoListRsp.setTopVideoList(topVideos);

            JSONArray jsonVideos = json.getJSONArray("videos");
            for (int i = 0; i < jsonVideos.length(); i++) {
                int id = jsonVideos.getJSONObject(i).getInt(
                        "id");
                String imgs = jsonVideos.getJSONObject(i).getString(
                        "imgUrl");
                String name = jsonVideos.getJSONObject(i).getString(
                        "name");
                String url = jsonVideos.getJSONObject(i).getString(
                        "videoUrl");
                Video video = new Video(id, imgs, name, url);
                videos.add(video);
            }
            videoListRsp.setVideoList(videos);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return videoListRsp;
    }

}
