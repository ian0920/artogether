package com.artogether.venue.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessMemberRepo;
import com.artogether.common.business_member.BusinessService;
import com.artogether.venue.vnedto.VneCardDTO;
import com.artogether.venue.vnedto.VneDetailDTO;
import com.artogether.venue.vneimg.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private BusinessMemberRepo businessMemberRepo;
    @Autowired
    private VneImgService vneImgService;
    @Autowired
    private VneImgUrlRepository vneImgUrlRepository;

    //創建場地
    public Integer createVenue(VneDetailDTO vneDetailDTO, BusinessMember businessMember) {
        String name = vneDetailDTO.getName();
        String type = vneDetailDTO.getType();
        String description = vneDetailDTO.getDescription();
        Integer availableDays = vneDetailDTO.getAvailableDays();

        Venue venue = Venue.builder()
                      .name(name)
                .businessMember(businessMember)
                      .type(type)
                      .availableDays(availableDays)
                      .build();
        if (description != null) {
            venue.setDescription(description);
        }
        venueRepository.save(venue);
        return venue.getId();
    }

    //場地資料更新
    public Venue updateVenue( Integer vneId, VneDetailDTO vneDetailDTO) {
//        全部抓下來
//        Venue venue = Venue.builder().id(vneId).name(name).type(type).description(description).availableDays(availableDays).build();
//        venueRepository.save(venue);
        Optional<Venue> venueOptional = venueRepository.findById(vneId);
        venueOptional.ifPresent(venue -> {
        String name = vneDetailDTO.getName();
            System.out.println(name);
        String type = vneDetailDTO.getType();
            System.out.println(type);
        String description = vneDetailDTO.getDescription();
            System.out.println(description);
        Integer availableDays = vneDetailDTO.getAvailableDays();
            System.out.println(availableDays);
        if (name != null) {
            venue.setName(name);
        }
        if (type != null) {
            venue.setType(type);
        }
        if (description != null) {
            venue.setDescription(description);
        }
        if (availableDays != null) {
            venue.setAvailableDays(availableDays);
        }
        venueRepository.save(venue);
        });
        Venue venue = venueRepository.findById(vneId).get();
        return venue;
    }

    //場地詳情
    public VneDetailDTO getDetailVenue(Integer vneId) {
        Venue venue = venueRepository.findById(vneId).get();
        VneDetailDTO vneDetailDTO = VneDetailDTO.builder()
                .vneId(vneId)
                .name(venue.getName())
                .vneAddress(businessMemberRepo.findById(venue.getBusinessMember().getId()).get().getAddr())
                .type(venue.getType())
                .availableDays(venue.getAvailableDays())
                .imgUrls(vneImgService.getAllImgs(vneId))
                .allStars(venue.getAllStars())
                .allReviews(venue.getAllReviews())
                .build();

        String description = venue.getDescription();
        if (description != null) {
            vneDetailDTO.setDescription(description);
        }
        return vneDetailDTO;
    }

    //商家場地總覽
    public List<VneCardDTO> bizVneList(Integer businessId) {
        List<Venue> venues = venueRepository.findByBusinessMember_Id(businessId);
        //匿名類別
        //Comparator<Venue> comparator = new Comparator<Venue>() {
        //    public int compare(Venue o1, Venue o2) {
        //        return o1.getId().compareTo(o2.getId());
        //    }
        //};

        //Lambda
        Comparator<Venue> comparator =((o1, o2) -> o1.getId().compareTo(o2.getId()));
        venues.sort(comparator);
        //靜態方法static <T, U>Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor)
        //好像還要更了解"Function"目前看不太懂
        //Comparator<Venue> comparator = Comparator.comparing(Venue::getId);
        //可以直接寫成這樣!!!!!
        //venues.sort(Comparator.comparing(Venue::getName));
        Optional<BusinessMember> businessMemberOptional = businessMemberRepo.findById(businessId);
        BusinessMember businessMember = businessMemberOptional.get();
        String addr = businessMember.getAddr();
        List<VneCardDTO> vneCardDTOs = new ArrayList<>();
        for (Venue venue : venues) {
            VneCardDTO vneCardDTO = new VneCardDTO();
            vneCardDTO.setVneId(venue.getId());
            vneCardDTO.setVneName(venue.getName());
            vneCardDTO.setVneAddress(addr);
            String description = venue.getDescription();
            if (description != null) {
                vneCardDTO.setDescription(description);
            }
            Optional<VneImgUrl> vneImgUrlOptional = vneImgUrlRepository.findByVenue_IdAndPosition(venue.getId(),1);
            vneImgUrlOptional.ifPresent(vneImgUrl -> {
                String imgUrl = vneImgUrl.getImageUrl();
                    if (imgUrl != null) {
                        vneCardDTO.setVneImgUrl(imgUrl);
                    } else {
                        vneCardDTO.setVneImgUrl("public/venue/images/0_0.jpg");
                    }
                }
            );
            vneCardDTOs.add(vneCardDTO);
        }
        return vneCardDTOs;
    }

    //首頁卡片
    public VneCardDTO getVenue(Integer vneId) {
        VneCardDTO vneCardDTO = new VneCardDTO();
        Optional<Venue> venueOptional = venueRepository.findById(vneId);
        venueOptional.ifPresent(venue -> {
            vneCardDTO.setVneId(venue.getId());
            vneCardDTO.setVneName(venue.getName());
            vneCardDTO.setVneAddress(venue.getBusinessMember().getAddr());
            String description = venue.getDescription();
            if (description != null) {
                vneCardDTO.setDescription(description);
            }
            Optional<VneImgUrl> vneImgOptional = vneImgUrlRepository.findByVenue_IdAndPosition(vneId, 1);
            vneImgOptional.ifPresent(
                    vneImgUrl -> {
                        String imgUrl = vneImgUrl.getImageUrl();
                        if (imgUrl != null) {
                            vneCardDTO.setVneImgUrl(imgUrl);
                        } else {
                            vneCardDTO.setVneImgUrl("public/venue/images/0_0.jpg");
                        }
                    }
            );
        });
    return vneCardDTO;
    }
// 水好深阿...lambda還沒看

//    public VneCardBO getVenue(Integer vneId) {
//        return venueRepository.findById(vneId)
//                .flatMap(venue -> businessMemberRepo.findById(venue.getBusinessMember().getId())
//                        .flatMap(businessMember -> {
//                            VneCardBO card = new VneCardBO();
//                            card.setVneId(vneId);
//                            card.setName(venue.getName());
//                            card.setVneAddress(businessMember.getAddr());
//                            card.setDescription(venue.getDescription());
//                            return vneImgRepository.findByVenueIdAndPosition(vneId, 1)
//                                    .map(img -> {
//                                        card.setVneImage(img.getImageFile());
//                                        return card;
//                                    });
//                        })
//                ).orElse(null);
//    }

}
