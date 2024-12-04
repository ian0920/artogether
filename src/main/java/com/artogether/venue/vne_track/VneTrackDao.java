package com.artogether.venue.vne_track;

import java.util.List;

public interface VneTrackDao {

    // 新增一個系統管理員
	VneTrack add(VneTrack.Vne_trackCom vneTrackCom);

    // 更新系統管理員的資料
	void update(VneTrack.Vne_trackCom vneTrackCom);

    // 刪除系統管理員，根據 id（主鍵）
	Boolean delete(VneTrack.Vne_trackCom vneTrackCom);

    // 根據 id 查找單個系統管理員
	VneTrack findByPK(VneTrack.Vne_trackCom vneTrackCom);

    // 查找所有的系統管理員
    List<VneTrack> getAll();

}
