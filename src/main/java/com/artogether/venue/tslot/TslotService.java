package com.artogether.venue.tslot;

import com.artogether.util.BinaryTools;
import com.artogether.venue.PublishErrorResponse;
import com.artogether.venue.VenueExceptions;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vnedto.*;
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

    //處理轉型
    public List<LocalDate> getAvailableDates(Integer availableDays) {
        List<java.sql.Date> sqlDates = tslotRepository.getAvailableDates(availableDays);
        return sqlDates.stream()
                .map(java.sql.Date::toLocalDate)
                .toList();
    }

    public TslotDTO nearestTslot(Integer vneId, LocalDateTime now) {
        // 確認是否有營業時間設定
        Tslot tslot = tslotRepository.getNearestPastRecord(vneId, now).orElseGet(() -> {
            return null; // 如果不存在，回傳 null
        });
        //不為空才運算
        if (tslot != null) {
            List<Integer> weeklyInteger = getWeeklyTslots(vneId, now);
            Integer bizTime = 0;
            for (Integer dailyHours : weeklyInteger) {
                bizTime |= dailyHours;
            }
            List<Integer> dailyList = BinaryTools.toList(bizTime, 24);
            Integer startHour = dailyList.get(0);
            Integer endHour = dailyList.get(dailyList.size() - 1);
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
                    .startHour(startHour)
                    .endHour(endHour)
                    .build();
            return tslotDTO;
            //若為空，回裝空的
        }else {return TslotDTO.builder().build();}
    }

        //找不到是給初始設定用的
    public List<Integer> getWeeklyTslots(Integer vneId, LocalDateTime submissionTime) {
        Optional<Tslot> tslotOptional = tslotRepository.getNearestPastRecord(vneId, submissionTime);
        if (tslotOptional.isEmpty()) {
            //如果沒找到提供一個全部開啟的預設表單
            //24小時全開(用二進位)
//            Integer AllDayOpening = 16777215;
            Integer AllDayClose = 0;
            List<Integer> AllWeekOpening = new ArrayList<>(Collections.nCopies(5, AllDayClose));
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
    //TimeSlotSearch
    public FlatpickrDTO timeSlotSearch (Integer vneId, LocalDateTime submissionTime, VneCardDTO vneCardDTO) {
        //使用者期待的時段
        Integer startHour = vneCardDTO.getStartHour();
        //實際時間區段不包含"endHour"
        Integer endHour = vneCardDTO.getEndHour();
        Integer wishTime = BinaryTools.toBinaryInteger(startHour, endHour,24);
        //該場地營業狀態
        List<Integer> weeklyTslot = getWeeklyTslots(vneId,submissionTime);
        //檢查找出符合時段的星期
        BitSet bizDay = new BitSet();
        //0-6為周一到周日，先檢查出符合設定時段的日期
        for (int i = 0; i < 7; i++) {
            Integer daily = weeklyTslot.get(i);
            Integer result = daily & wishTime;
            //包裝型別不能直接用"=="，因為那是比較其內存地址
            if (result.equals(wishTime)) {
                bizDay.set(i);
            }
        }
        System.out.println("bizDay"+bizDay);
        //最早可以預約及最晚可以預約的日期
        Integer days = venueRepository.findById(vneId).get().getAvailableDays();
        List<LocalDate> availableDates = getAvailableDates(days);
        LocalDate first = availableDates.get(0);
        LocalDate last = availableDates.get(availableDates.size()-1);
        //遍歷可預約的日期，抓出不符合的日期
        List<LocalDate> disableDate = new ArrayList<>();
        Map<LocalDate, Integer> bookingTslot = getBookingTslot(vneId);
        for (LocalDate date : availableDates){
            int dayOfWeek = date.getDayOfWeek().getValue()-1;
            boolean validOption = bizDay.get(dayOfWeek);
            System.out.println(validOption);
            if (validOption) {
                Integer bizHour = weeklyTslot.get(dayOfWeek);
                Integer orderedHour = bookingTslot.get(date);
                if (orderedHour != null) {
                    System.out.println("orderedHour:"+orderedHour);
                    //轉換後我需要24位數，取出扣除預約時間後的營業時間
                    int invertedHour = ~orderedHour & 0xFFFFFF;
                    int availableHour = bizHour & invertedHour;
                    System.out.println("availableHour:"+availableHour);
                    availableHour &= wishTime;
                    System.out.println("availableHourWithWish:"+availableHour);
                    if (availableHour != wishTime) {
                        disableDate.add(date);
                    }
                }
            }else {
                disableDate.add(date);
            }
        }

        System.out.println("disableDate"+disableDate);
        List<LocalDate> disableList = new ArrayList<>();
        List<LocalDate>orderedDates = getOrderedDates(vneId,submissionTime);
        disableList.addAll(disableDate);
        disableList.addAll(orderedDates);

        FlatpickrDTO flatpickrDTO = FlatpickrDTO.builder()
                .minDate(first)
                .maxDate(last)
                .disableDates(disableList)
                .build();
        return flatpickrDTO;
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
            //檢查訂單是單日還是跨日
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
            //因為會緩衝一小時，所以如果預約滿會跟營業時間不同，
            // 會多一個小時，導致無法抓出來，
            // 目前調整方式是抓出營業時間的最後一小時並加上再做比對
            Integer bizHours = weeklyTslot.get(weekday);
            BitSet bitSet = BinaryTools.toBitSet(bizHours, 24);
            int afterLastTrue = bitSet.previousSetBit(bitSet.length() - 1)+1;
            Integer checkHour = bizHours | (1 << 24 - afterLastTrue - 1);
            if ((checkHour ^ bookingTslot)==0) {
                nonBizDays.add(date);
            }
        }
        return nonBizDays;
    }

    //找出表訂不能預約的天數(尚須排除已預約
    public List<LocalDate> getNonBizDays(Integer vneId){
        Venue venue = venueRepository.findById(vneId).get();
        Integer availableDays = venue.getAvailableDays();
        List<LocalDate> openDates = getAvailableDates(availableDays);
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
        List<LocalDate> availableDates = getAvailableDates(days);
        LocalDate first = availableDates.get(0);
        LocalDate last = availableDates.get(availableDates.size()-1);
        List<LocalDate> disableDates = getDisableDates(vneId, submissionTime);
        FlatpickrDTO flatpickrDTO = FlatpickrDTO.builder()
                .minDate(first)
                .maxDate(last)
                .disableDates(disableDates)
                .build();
        return flatpickrDTO;
    }

    //獲取預約選取日期的尚可預約的時間
    public BitSet getAvailableBitSet(Integer vneId, LocalDate bookingDate, LocalDateTime submissionTime) {
        Map<LocalDate, Integer> bookingTslot = getBookingTslot(vneId);
        Integer weekDay = bookingDate.getDayOfWeek().getValue()-1;
        List<Integer> weeklyTslots = getWeeklyTslots(vneId, submissionTime);
        Integer businessHours =weeklyTslots.get(weekDay);

        if (bookingTslot.containsKey(bookingDate)) {
            Integer disableHours = bookingTslot.get(bookingDate);
            businessHours ^= disableHours;
            BitSet hoursBitSet = BinaryTools.toBitSet(businessHours,24);

            return hoursBitSet;
        }else {
            BitSet hoursBitSet = BinaryTools.toBitSet(businessHours,24);
            return hoursBitSet;
        }
    }

    //整理出連續的區段
    public List<List<Integer>> getAvailableSegments(Integer vneId, LocalDate bookingDate,  LocalDateTime submissionTime){
        List<List<Integer>> availableSegments = new ArrayList<>();
        BitSet hoursBitSet = getAvailableBitSet(vneId, bookingDate, submissionTime);

        int start = hoursBitSet.nextSetBit(0);
        while (start != -1){
            int end = hoursBitSet.nextClearBit(start);
            List<Integer> segment = new ArrayList<>();

            for (int i = start; i < end; i++) {
                segment.add(i);
            }
            availableSegments.add(segment);
            //找下一個存在的
            start = hoursBitSet.nextSetBit(end);
        }
        return availableSegments;
    }

    //單日可預約時間和價錢
    public AvailableDTO getAvailableDTO(Integer vneId, LocalDate bookingDate,  LocalDateTime submissionTime){
        AvailableDTO availableDTO = AvailableDTO.builder()
                .availableSegments(getAvailableSegments(vneId,bookingDate, submissionTime))
                .hourlyPrice(getPriceMap(vneId,bookingDate, submissionTime))
                .build();
        return availableDTO;
    }

    //製作可營業的時間價錢對照表
    public Map<Integer, Integer> getPriceMap (Integer vneId, LocalDate bookingDate,  LocalDateTime submissionTime) {
        Map<Integer, Integer> priceMap = new HashMap<>();
        VnePriceDTO vnePriceDTO = vnePriceService.getNearestVnePrice(vneId, LocalDateTime.now());
        System.out.println(vnePriceDTO);
        List<Integer> dayOfWeek = vnePriceDTO.getDayOfWeek();
        int value = bookingDate.getDayOfWeek().getValue()-1;
        Integer defaultPrice = vnePriceDTO.getDefaultPrice();
        Integer price = vnePriceDTO.getPrice();
        BitSet availableHours = getAvailableBitSet(vneId, bookingDate, submissionTime);

        if (price != null) {
            if (dayOfWeek.contains(value)) {
                Integer startTime = vnePriceDTO.getStartTime();
                Integer endTime = vnePriceDTO.getEndTime();
                BitSet specialPriceList = BinaryTools.toBitSet(vnePriceService.getTslotList(startTime, endTime));
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
            }else {
                int start = availableHours.nextSetBit(0);
                while (start != -1) {
                    priceMap.put(start, defaultPrice);
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
    public List<LocalDate> test(Integer i){
        List<LocalDate> availableDates = getAvailableDates(i);
        System.out.println(availableDates);
        return availableDates;
    }
}
