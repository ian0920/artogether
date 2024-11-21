package com.artogether.event.evt_track;

import java.util.List;



public interface EvtTrackDAO_interface {
	 public void insert(EvtTrackVO evtTrackVO);
     public void update(EvtTrackVO evtTrackVO);
     public void delete(Integer empno);
     public EvtTrackVO findByPrimaryKey(Integer empno);
     public List<EvtTrackVO> getAll();
}
