package com.artogether.venue.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessMemberRepo;
import com.artogether.venue.vnedto.VneCardDTO;
import com.artogether.venue.vnedto.VneDetailDTO;
import com.artogether.venue.vneimg.VneImg;
import com.artogether.venue.vneimg.VneImgRepository;
import com.artogether.venue.vneimg.VneImgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VenueService {

    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private BusinessMemberRepo businessMemberRepo;
    @Autowired
    private VneImgRepository vneImgRepository;
    @Autowired
    private VneImgService vneImgService;

    //創建場地
    public void creatVenue(VneDetailDTO vneDetailDTO, Integer businessId) {

        String name = vneDetailDTO.getVneName();
        String type = vneDetailDTO.getType();
        String description = vneDetailDTO.getDescription();
        Integer availableDays = vneDetailDTO.getAvailableDays();
        byte[] imageFile1 = vneDetailDTO.getImageFile1();
        byte[] imageFile2 = vneDetailDTO.getImageFile2();
        byte[] imageFile3 = vneDetailDTO.getImageFile3();

        Venue venue = Venue.builder()
                      .name(name)
                      .type(type)
                      .description(description)
                      .availableDays(availableDays)
                      .build();
        venueRepository.save(venue);
        int vneId = venue.getId();
        if (imageFile1 != null) {vneImgService.updateVneImg(vneId, 1, imageFile1);}
        if (imageFile2 != null) {vneImgService.updateVneImg(vneId, 2, imageFile1);}
        if (imageFile3 != null) {vneImgService.updateVneImg(vneId, 3, imageFile1);}
    }

    //首頁卡片
    public VneCardDTO getVenue(Integer vneId) {
        VneCardDTO vneCardDTO = null;

        Optional<Venue> venueOptional = venueRepository.findById(vneId);
        if (venueOptional.isPresent()) {
            Venue venue = venueOptional.get();
            Optional<BusinessMember> businessMemberOptional = businessMemberRepo.findById(venue.getBusinessMember().getId());
            if (businessMemberOptional.isPresent()) {
                BusinessMember businessMember = businessMemberOptional.get();

                vneCardDTO.setVneId(vneId);
                vneCardDTO.setVneName(venue.getName());
                vneCardDTO.setVneAddress(businessMember.getAddr());
                vneCardDTO.setDescription(venue.getDescription());

                Optional<VneImg> vneImgOptional = vneImgRepository.findByVenueIdAndPosition(vneId, 1);
                if (vneImgOptional.isPresent()) {
                    VneImg vneImg = vneImgOptional.get();

                    vneCardDTO.setVneImgUrl(vneImgService.getAssetPath(~vneId, vneImg.getPosition()));
                    return vneCardDTO;
                }

            }
        }
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
    //場地詳情
//    public DetelVenueBO getDetelVenue(Integer vneId) {
//        DetelVenueBO detelVenueBO = null;
//
//    }

}
