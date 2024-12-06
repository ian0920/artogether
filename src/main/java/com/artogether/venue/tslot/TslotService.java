package com.artogether.venue.tslot;

import com.artogether.common.member.Member;
import com.artogether.common.member.MemberRepo;
import com.artogether.common.member.MemberService;
import com.artogether.util.BinaryTools;
import com.artogether.venue.venue.Venue;
import com.artogether.venue.venue.VenueRepository;
import com.artogether.venue.vnedto.TslotDTO;
import com.artogether.venue.vnedto.VneOrderDTO;
import com.artogether.venue.vneorder.VneOrder;
import com.artogether.venue.vneorder.VneOrderRepository;
import com.artogether.venue.vneorder.VneOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    //只在第一次啟動時執行。
//    @Transactional
    @PostConstruct
    public void init() {
        tslotViewService.createTslotScheduleView();
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
                Integer daylyTslots = getDaylyTslotsByDay(tslot, DayOfWeek.of(i));
                weeklyTslots.add(daylyTslots);
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
    private Integer getDaylyTslotsByDay(Tslot tslot, DayOfWeek day) {
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
        Integer daylyTslot = BinaryTools.toBinaryInteger(hourOfDay);
        return daylyTslot;
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
                .hourOfMon(BinaryTools.toBinaryString(tslotDTO.getHourOfMon(),24))
                .hourOfTue(BinaryTools.toBinaryString(tslotDTO.getHourOfTue(),24))
                .hourOfWed(BinaryTools.toBinaryString(tslotDTO.getHourOfWed(),24))
                .hourOfThu(BinaryTools.toBinaryString(tslotDTO.getHourOfThu(),24))
                .hourOfFri(BinaryTools.toBinaryString(tslotDTO.getHourOfFri(),24))
                .hourOfSat(BinaryTools.toBinaryString(tslotDTO.getHourOfSat(),24))
                .hourOfSun(BinaryTools.toBinaryString(tslotDTO.getHourOfSun(),24))
                .build();
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

//    private void setHourStr(Timestamp timestamp) {
//        // 設置分鐘、秒數為0
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        Timestamp hourTimestamp = new Timestamp(calendar.getTimeInMillis());
//        System.out.println("當前小時的 Timestamp: " + hourTimestamp);
//
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
//        String hourString = sdf.format(hourTimestamp);
//        System.out.println("格式化到小時: " + hourString);
//    }
}
