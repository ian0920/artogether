package com.artogether.venue.tslot;

import com.artogether.util.BinaryTools;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vnedto.AvailableDTO;
import com.artogether.venue.vnedto.FlatpickrDTO;
import com.artogether.venue.vnedto.TslotDTO;
import com.artogether.venue.vnedto.VnePriceDTO;
import com.artogether.venue.vneorder.VneOrder;
import com.artogether.venue.vneorder.VneOrderRepository;
import com.artogether.venue.vneprice.VnePriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class TslotService {

    @Autowired
    private TslotRepository tslotRepository;
    @Autowired
    private TslotViewService tslotViewService;
    @Autowired
    private VneOrderRepository vneOrderRepository;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private VnePriceService vnePriceService;


    //只在第一次啟動時執行。
//    @Transactional
    @PostConstruct
    public void init() {
        tslotViewService.createTslotScheduleView();
    }

    public TslotDTO nearestTslot(Integer vneId, LocalDateTime now) {
        List<Integer> weeklyInteger = getWeeklyTslots(vneId, now);
        TslotDTO tslotDTO = TslotDTO.builder()
                .vneId(vneId)
                .vneName(venueRepository.findById(vneId).get().getName())
                .hourOfMon(BinaryTools.toList(weeklyInteger.get(0), 24))
                .hourOfTue(BinaryTools.toList(weeklyInteger.get(1), 24))
                .hourOfWed(BinaryTools.toList(weeklyInteger.get(2), 24))
                .hourOfThu(BinaryTools.toList(weeklyInteger.get(3), 24))
                .hourOfFri(BinaryTools.toList(weeklyInteger.get(4), 24))
                .hourOfSat(BinaryTools.toList(weeklyInteger.get(5), 24))
                .hourOfSun(BinaryTools.toList(weeklyInteger.get(6), 24))
                .build();
        return tslotDTO;
    }

        //找不到是給初始設定用的
    public List<Integer> getWeeklyTslots(Integer vneId, LocalDateTime submissionTime) {
        Optional<Tslot> tslotOptional = tslotRepository.getNearestPastRecord(vneId, submissionTime);
        if (tslotOptional.isEmpty()) {
            //如果沒找到提供一個全部開啟的預設表單
            //24小時全開(用二進位)
            Integer AllDayOpening = 16777215;
            List<Integer> AllWeekOpening = new ArrayList<>(Collections.nCopies(5, AllDayOpening));
            return AllWeekOpening;
        }else {
            Tslot tslot= tslotOptional.get();
            //如果有找到，找出一個符合當時設定的行事曆
            List<Integer> weeklyTslots = new ArrayList<>();
            // 遍歷一周的每一天
            for(int i = 1; i <= 7; i++){
                Integer dailyTslots = getDailyTslotsByDay(tslot, DayOfWeek.of(i));
                weeklyTslots.add(dailyTslots);
            }
            return weeklyTslots;
        }
    }

    //取出是否有公休日
    public Integer getBinaryWeek(Integer vneId, LocalDateTime submissionTime) {
        List<Integer> weeklyTslots = getWeeklyTslots(vneId, submissionTime);
        Integer size = weeklyTslots.size();
        Integer binaryWeek = 0;
        for (int i = 0; i < size; i++) {
            if (weeklyTslots.get(i) != 0) {
                binaryWeek |= 1 << (size-1-i);
            }
        }
        return binaryWeek;
    }

    //單日有營業的數字
    private Integer getDailyTslotsByDay(Tslot tslot, DayOfWeek day) {
        String hourOfDay;
        //這邊不用".name()"是因為"switch"支持枚舉類
        switch (day) {
            case MONDAY -> hourOfDay = tslot.getHourOfMon();
            case TUESDAY -> hourOfDay = tslot.getHourOfTue();
            case WEDNESDAY -> hourOfDay = tslot.getHourOfWed();
            case THURSDAY -> hourOfDay = tslot.getHourOfThu();
            case FRIDAY -> hourOfDay = tslot.getHourOfFri();
            case SATURDAY -> hourOfDay = tslot.getHourOfSat();
            case SUNDAY -> hourOfDay = tslot.getHourOfSun();
            default -> throw new IllegalArgumentException("無效數字");
        }
        Integer dailyTslot = BinaryTools.toBinaryInteger(hourOfDay);
        return dailyTslot;
    }

    // 創建或更新時段
    @Transactional
    public void updateTslot(LocalDateTime submissionTime, TslotDTO tslotDTO) {
        int vneId = tslotDTO.getVneId();
        //找到最靠近現在的前一筆資料寫入更改時間
        Optional<Tslot> tslotOptional =tslotRepository.getNearestPastRecord(vneId, submissionTime);
        tslotOptional.ifPresent(tslot->{
            tslot.setExpirationTime(submissionTime);
            tslotRepository.save(tslot);
        });
    //不論有沒有找到都新建一個
        Tslot tslot = Tslot.builder()
                .venue(Venue.id(vneId)) // Venue 有靜態方法"id()"(嘗試看看)
                .hourOfMon(BinaryTools.toBinaryString(BinaryTools.toBitSet(tslotDTO.getHourOfMon()),24))
                .hourOfTue(BinaryTools.toBinaryString(BinaryTools.toBitSet(tslotDTO.getHourOfTue()),24))
                .hourOfWed(BinaryTools.toBinaryString(BinaryTools.toBitSet(tslotDTO.getHourOfWed()),24))
                .hourOfThu(BinaryTools.toBinaryString(BinaryTools.toBitSet(tslotDTO.getHourOfThu()),24))
                .hourOfFri(BinaryTools.toBinaryString(BinaryTools.toBitSet(tslotDTO.getHourOfFri()),24))
                .hourOfSat(BinaryTools.toBinaryString(BinaryTools.toBitSet(tslotDTO.getHourOfSat()),24))
                .hourOfSun(BinaryTools.toBinaryString(BinaryTools.toBitSet(tslotDTO.getHourOfSun()),24))
                .effectiveTime(submissionTime)
                .build();
        tslotRepository.save(tslot);
        System.out.println("updateTslot");
        System.out.println(tslot);
    }

    //有個Multimap(Guava Library)可能可以用
    //取出訂單中所有預約的小時
    public Map<LocalDate, Integer> getBookingTslot (Integer vneId) {
        List<VneOrder> vneOrders = vneOrderRepository.findUnclosedOrders(vneId,LocalDate.now());
        Map<LocalDate, Integer> bookingTslotMap = new HashMap<>();
        for (VneOrder vneOrder : vneOrders) {
            LocalDate startDate = vneOrder.getStartDate();
            Integer startTime = vneOrder.getStartTime();
            LocalDate endDate = vneOrder.getEndDate();
            Integer endTime = vneOrder.getEndTime();
            long days = ChronoUnit.DAYS.between(startDate, endDate);
            Integer bookingTslot = 0;
            //若為單日訂單
            if (days == 0) {
                //設定為<=因為希望設定一小時的緩衝時間
                for (int i = startTime; i <= endTime; i++) {
                    bookingTslot |= 1 << (24-1-i);
                    if (bookingTslotMap.containsKey(startDate)) {
                        bookingTslot |= bookingTslotMap.get(startDate);
                        bookingTslotMap.put(startDate, bookingTslot);
                    }
                    bookingTslotMap.put(startDate, bookingTslot);
                }
            }
        }
        return bookingTslotMap;
    }

    //整理出已被整天預約的日期
    public List<LocalDate> getOrderedDates(Integer vneId, LocalDateTime submissionTime){

        List<LocalDate> nonBizDays = new ArrayList<>();
        List<Integer> weeklyTslot = getWeeklyTslots(vneId,submissionTime);
        Map<LocalDate, Integer> bookingTslotMap = getBookingTslot(vneId);
        for (Map.Entry<LocalDate, Integer> entry: bookingTslotMap.entrySet()) {
            LocalDate date = entry.getKey();
            Integer bookingTslot = entry.getValue();
            Integer weekday = date.getDayOfWeek().getValue()-1;
            if ((weeklyTslot.get(weekday) ^ bookingTslot)==0) {
                nonBizDays.add(date);
            }
        }
        return nonBizDays;
    }

    //找出表訂不能預約的天數(尚須排除已預約
    public List<LocalDate> getNonBizDays(Integer vneId){
        Venue venue = venueRepository.findById(vneId).get();
        Integer availableDays = venue.getAvailableDays();
        List<LocalDate> openDates = venueRepository.getAvailableDates(availableDays);
        List<LocalDate> disableDates = new ArrayList<>();
        int binaryWeek = getBinaryWeek(vneId, LocalDateTime.now());

        if (binaryWeek == 127){
            return disableDates;
        }else {
            for (LocalDate openDate : openDates) {
                int weekday = openDate.getDayOfWeek().getValue();
                if ((binaryWeek & 1 << (7-weekday))==0){
                    disableDates.add(openDate);
                }
            }
            return disableDates;
        }
    }

    //列出確切無法被選取的日期(未排序，但應該不用排)
    public List<LocalDate> getDisableDates(Integer vneId, LocalDateTime submissionTime){
        List<LocalDate>nonBizDays = getNonBizDays(vneId);
        List<LocalDate>orderedDates = getOrderedDates(vneId,submissionTime);
        List<LocalDate> disableDates = new ArrayList<>();
        disableDates.addAll(nonBizDays);
        disableDates.addAll(orderedDates);
        return disableDates;
    }

    //設定該場地可預約的日期
    public FlatpickrDTO getFlatpickrDTO(Integer vneId, LocalDateTime submissionTime) {
        Integer days = venueRepository.findById(vneId).get().getAvailableDays();
        List<LocalDate> availableDates = venueRepository.getAvailableDates(days);
        LocalDate first = availableDates.get(0);
        LocalDate last = availableDates.get(availableDates.size()-1);
        List<LocalDate> disableDates = getDisableDates(vneId, submissionTime);
        FlatpickrDTO flatpickrDTO = FlatpickrDTO.builder()
                .startDate(first)
                .endDate(last)
                .disableDates(disableDates)
                .build();
        return flatpickrDTO;
    }

    //獲取預約選取日期的尚可預約的時間
    public BitSet getAvailableBitSet(Integer vneId, LocalDate bookingDate) {
        Map<LocalDate, Integer> bookingTslot = getBookingTslot(vneId);
        Integer weekDay = bookingDate.getDayOfWeek().getValue();

        if (bookingTslot.containsKey(bookingDate)) {
            Integer disableHours = bookingTslot.get(bookingDate);
            weekDay ^= disableHours;
            BitSet hoursBitSet = BinaryTools.toBitSet(weekDay,24);

            return hoursBitSet;
        }else {
            BitSet hoursBitSet = BinaryTools.toBitSet(weekDay,24);
            List<Integer> segment = new ArrayList<>();
            return hoursBitSet;
        }
    }

    //整理出連續的區段
    public List<List<Integer>> getAvailableSegments(Integer vneId, LocalDate bookingDate){
        List<List<Integer>> availableSegments = new ArrayList<>();
        BitSet hoursBitSet = getAvailableBitSet(vneId, bookingDate);

        int start = hoursBitSet.nextSetBit(0);
        while (start != -1){
            int end = hoursBitSet.nextClearBit(start);
            List<Integer> segment = new ArrayList<>();

            for (int i = start; i <= end; i++) {
                segment.add(i);
            }
            availableSegments.add(segment);
            //找下一個存在的
            start = hoursBitSet.nextSetBit(start+1);
        }
        return availableSegments;
    }

    //單日可預約時間和價錢
    public AvailableDTO getAvailableDTO(Integer vneId, LocalDate bookingDate){
        AvailableDTO availableDTO = AvailableDTO.builder()
                .availableSegments(getAvailableSegments(vneId,bookingDate))
                .hourlyPrice(getPriceMap(vneId,bookingDate))
                .build();
        return availableDTO;
    }

    //製作可營業的時間價錢對照表
    public Map<Integer, Integer> getPriceMap (Integer vneId, LocalDate bookingDate) {
        Map<Integer, Integer> priceMap = new HashMap<>();
        VnePriceDTO vnePriceDTO = vnePriceService.getNearestVnePrice(vneId, LocalDateTime.now());
        List<Integer> dayOfWeek = vnePriceDTO.getDayOfWeek();
        int value = bookingDate.getDayOfWeek().getValue();
        Integer defaultPrice = vnePriceDTO.getDefaultPrice();
        Integer price = vnePriceDTO.getPrice();
        BitSet availableHours = getAvailableBitSet(vneId, bookingDate);

        if (dayOfWeek.contains(value)) {
            if (price != null) {
                int start = availableHours.nextSetBit(0);
                while (start != -1) {
                    priceMap.put(start, defaultPrice);
                    start = availableHours.nextSetBit(start + 1);
                }
                return priceMap;
            }else {
                Integer startTime = vnePriceDTO.getStartTime();
                Integer endTime = vnePriceDTO.getEndTime();
                BitSet specialPriceList = BinaryTools.toBitSet(vnePriceService.getPriceTslotList(startTime, endTime));
                int start = availableHours.nextSetBit(0);
                while (start != -1) {
                    if (specialPriceList.get(start)) {
                        priceMap.put(start, price);
                    }else {
                        priceMap.put(start, defaultPrice);
                    }
                    start = availableHours.nextSetBit(start + 1);
                }
                return priceMap;
            }
        }else {
            int start = availableHours.nextSetBit(0);
            while (start != -1) {
                priceMap.put(start, defaultPrice);
                start = availableHours.nextSetBit(start + 1);
            }
            return priceMap;
        }
    }
}
