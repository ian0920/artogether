package com.artogether.venue.venue;

import com.artogether.common.business_member.BusinessMember;
import com.artogether.common.business_member.BusinessMemberRepo;

import com.artogether.util.BinaryTools;

import com.artogether.common.business_member.BusinessService;
import com.artogether.venue.PublishErrorResponse;
import com.artogether.venue.VenueExceptions;
import com.artogether.venue.tslot.Tslot;
import com.artogether.venue.tslot.TslotRepository;

import com.artogether.venue.tslot.TslotService;
import com.artogether.venue.vnedto.TslotDTO;
import com.artogether.venue.vnedto.VneCardDTO;
import com.artogether.venue.vnedto.VneDetailDTO;
import com.artogether.venue.vnedto.VnePriceDTO;

import com.artogether.venue.vneimg.VneImgService;

import com.artogether.venue.vneimg.*;
import com.artogether.venue.vneprice.VnePrice;
import com.artogether.venue.vneprice.VnePriceRepository;

import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    @Autowired
    private TslotService tslotService;
    @Autowired
    private TslotRepository tslotRepository;
    @Autowired
    private VnePriceService vnePriceService;
    @Autowired
    private VnePriceRepository priceRepository;

    //檢查是否上架
    public Boolean isVneStatusOne(Integer vneId) {

        return venueRepository.existsByIdAndStatus(vneId,VenueStatusEnum.ONLINE);
    }

    //上架
    public void publishVenue(Integer vneId, LocalDateTime submissionTime) {
        //基本上應該都存在才會進這個方法吧?!
        Venue venue = venueRepository.findById(vneId).orElseThrow(() ->
                new VenueExceptions.InvalidListingException("場地不存在"));
        //名字是ChatGPT建議的，也太長了吧...
        List<PublishErrorResponse.MissingRequirement> missingRequirements = new ArrayList<>();

        //大前提，不是被停權
        if (venue.getStatus() != VenueStatusEnum.SUSPENDED) {
            // 條件 1: 場地名稱
            if (venue.getName() == null) {
                missingRequirements.add(new PublishErrorResponse.MissingRequirement(
                        "需取名方可上架",
                        "請為場地命名，例如：'夢想舞台' 或 '星空咖啡廳'"
                ));
            }

            // 條件 2: 可預約天數
            if (venue.getAvailableDays() == null) {
                missingRequirements.add(new PublishErrorResponse.MissingRequirement(
                        "需設定可預約的天數方可上架",
                        "請拉一下下拉選單，挑個喜歡的數字"
                ));
            }

            // 條件 3: 圖片
            List<String> imageUrls = vneImgUrlRepository.findImageUrlsByVneId(vneId);
            if (imageUrls == null || imageUrls.isEmpty()) {
                missingRequirements.add(new PublishErrorResponse.MissingRequirement(
                        "至少需設定一張照片方可上架",
                        "請上傳至少一張場地的圖片，例如外觀或內部裝修"
                ));
            }

            // 條件 4: 營業時間
            Optional<Tslot> tslotOptional = tslotRepository.getNearestPastRecord(vneId, submissionTime);
            if (tslotOptional.isEmpty()) {
                missingRequirements.add(new PublishErrorResponse.MissingRequirement(
                        "須設定營業時間方可上架",
                        "請設定場地的營業時間，例如上午 9:00 至晚上 6:00"
                ));
            }

            // 條件 5: 常態價位
            Optional<VnePrice> vnePriceOptional = priceRepository.getNearestPastRecord(vneId, submissionTime);
            if (vnePriceOptional.isEmpty()) {
                missingRequirements.add(new PublishErrorResponse.MissingRequirement(
                        "須設定常態價位方可上架",
                        "請設定場地的基本租用費用，例如每小時 500 元"
                ));
            }
        }else {
            missingRequirements.add(new PublishErrorResponse.MissingRequirement(
                    "停權中",
                    "停權中"
            ));
        }


        // 如果有任何條件未滿足，拋出異常
        if (!missingRequirements.isEmpty()) {

            PublishErrorResponse errorResponse = new PublishErrorResponse(
                    "場地上架失敗，請補充必要資訊",
                    missingRequirements
            );
            throw new VenueExceptions.InvalidListingException(errorResponse);
        }

        // 上架操作
        venue.setStatus(VenueStatusEnum.ONLINE);
        venueRepository.save(venue);
    }

    //下架
    public void archiveVenue (Integer vneId) {
        Venue venue = venueRepository.findById(vneId).orElseThrow(() ->
                new VenueExceptions.InvalidListingException("場地不存在"));
        if (venue.getStatus() == VenueStatusEnum.ONLINE) {
            venue.setStatus(VenueStatusEnum.ONLINE);
            venueRepository.save(venue);
        }
    }

    //創建場地
    public Integer createVenue(VneCardDTO vneCardDTO, BusinessMember businessMember) {
        String name = vneCardDTO.getVneName();
        System.out.println(name);
        String type = vneCardDTO.getType();
        System.out.println(type);
        String description = vneCardDTO.getDescription();
        System.out.println(description);
        Integer availableDays = vneCardDTO.getAvailableDays();
        System.out.println(availableDays);
        Venue venue = Venue.builder()
                      .name(name)
                      .businessMember(businessMember)
                      .type(type)
                      .status(VenueStatusEnum.OFFLINE)
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
        String name = vneDetailDTO.getVneName();
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
        LocalDateTime now = LocalDateTime.now();
        TslotDTO tslotDTO = tslotService.nearestTslot(vneId, now);
        VnePriceDTO vnePriceDTO = vnePriceService.getNearestVnePrice(vneId, now);
        BusinessMember businessMember = businessMemberRepo.findById(venue.getBusinessMember().getId()).get();
        VneDetailDTO vneDetailDTO = VneDetailDTO.builder()
                .vneId(vneId)
                .vneName(venue.getName())
                .bizName(businessMember.getName())
                .vneAddress(businessMember.getAddr())
                .type(venue.getType())
                .status(venue.getStatus().getDescription())
                .availableDays(venue.getAvailableDays())
                .imgUrls(vneImgService.getAllImgs(vneId))
                .tslot(tslotDTO)
                .price(vnePriceDTO)
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
        List<VneCardDTO> vneCardDTOs = new ArrayList<>();
        for (Venue venue : venues) {
            VneCardDTO vneCardDTO = getVenue(venue.getId());
            // 添加到結果清單
            vneCardDTOs.add(vneCardDTO);
        }
        return vneCardDTOs;
    }

    //場地基本資料
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
            vneCardDTO.setImgUrls(vneImgService.getAllImgs(vneId));
        });
    return vneCardDTO;
    }

    public List<VneCardDTO> getAllVneCard() {

        List<Venue> list = venueRepository.findAll();


        List<VneCardDTO> vneCardDTOs = list.stream()
                .map(e ->{

                    List<Integer> weeklyInteger = tslotService.getWeeklyTslots(e.getId(), LocalDateTime.now());
                    Integer bizTime = 0;
                    for (Integer dailyHours : weeklyInteger) {
                        bizTime |= dailyHours;
                    }
                    List<Integer> dailyList = BinaryTools.toList(bizTime, 24);
                    Integer startHour = dailyList.get(0);
                    Integer endHour = dailyList.get(dailyList.size() - 1);

                    VneCardDTO vneCardDTO =  new VneCardDTO();
                    vneCardDTO.setVneId(e.getId());
                    vneCardDTO.setVneName(e.getName());
                    vneCardDTO.setType(e.getType());
                    vneCardDTO.setVneAddress(e.getBusinessMember().getAddr());
                    vneCardDTO.setDescription(e.getDescription());
                    vneCardDTO.setAvailableDays(e.getAvailableDays());
                    vneCardDTO.setImgUrls(vneImgService.getAllImgs(e.getId()));
                    vneCardDTO.setStartHour(startHour);
                    vneCardDTO.setEndHour(endHour);

                    return vneCardDTO;

                }).toList();

        return vneCardDTOs;

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
