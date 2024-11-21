package com.artogether.venue.vne_track;

import java.util.List;

public interface Vne_trackDao {

    // 新增一個系統管理員
	Vne_track add(Vne_track.Vne_trackCom vneTrackCom);

    // 更新系統管理員的資料
	void update(Vne_track.Vne_trackCom vneTrackCom);

    // 刪除系統管理員，根據 id（主鍵）
	Boolean delete(Vne_track.Vne_trackCom vneTrackCom);

    // 根據 id 查找單個系統管理員
	Vne_track findByPK(Vne_track.Vne_trackCom vneTrackCom);

    // 查找所有的系統管理員
    List<Vne_track> getAll();

}
