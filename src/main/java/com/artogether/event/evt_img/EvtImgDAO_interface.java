package com.artogether.event.evt_img;

import java.util.List;


public interface EvtImgDAO_interface {
	public void insert(EvtImgVO evtImgVO);

	public void update(EvtImgVO evtImgVO);

	public void delete(Integer id);

	public EvtImgVO findByPrimaryKey(Integer id);

	public List<EvtImgVO> getAll();
}
