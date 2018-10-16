package application.controller;

import application.entity.Day;
import application.entity.Schedule;
import application.entity.ScheduleEvent;
import application.dto.ScheduleSenderDTO;
import application.service.implementations.DayServise;
import application.service.implementations.ScheduleService;
import application.service.implementations.SchedulesEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/schedules")
public class ScheduleAdminController {
    private static final int startTime = 8;
    private static final int endTime = 20;
    @Autowired
    ScheduleService scheduleServise;
    @Autowired
    SchedulesEventService schedulesEventService;
    @Autowired
    DayServise dayServise;

    @GetMapping()
    public String getSchedules(Model model) {
        List<Day> dayList = null;
        try {
            dayList = dayServise.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Schedule> scheduleList = null;
        try {
            scheduleList = scheduleServise.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        dayList=sortDay(dayList);
        List<List<ScheduleSenderDTO>> scheduleListTable = makeTable(dayList, scheduleList);
        model.addAttribute("daylist", dayList);
        model.addAttribute("schedulelist", scheduleListTable);
        return "schedule/schedule";
    }
    @GetMapping("/schedule_edit")
    public String getEditSchedules(Model model){
        List<ScheduleSenderDTO> listSender=new ArrayList<>();
        ScheduleSenderDTO sender=null;
        List<Schedule> scheduleList=null;
        try {
            scheduleList = scheduleServise.getAll();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        for(Schedule sch:scheduleList){
            String time =sch.getStarttime().toString();
            time=time.substring(0,5);
            //----------------------------------------------
            //----------------------------------------------
            sender=new ScheduleSenderDTO(sortingFlag(sch,time),sch.getId(),sch.getScheduleEvent().getName(),time,sch.getDay().getNameDay());
            listSender.add(sender);
        }
        listSender=sortScheduleSender(listSender);
        model.addAttribute("schedule",listSender);

        return "/schedule/schedule_edit";
    }

    @PostMapping("/save_schedule_event")
    public String postSaveScheduleEvent(ScheduleEvent event) {
        try {
            schedulesEventService.save(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/";
    }

    @GetMapping("/save_shedule")
    public String getSaveSchedule(Model model) {
        List<Day> dayList = null;
        List<ScheduleEvent> eventList = null;
        try {
            eventList = schedulesEventService.getAll();
            dayList = dayServise.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("eventshedule", eventList);
        model.addAttribute("weekday", dayList);
        return "schedule/save_schedule";
    }

    @PostMapping("/save_shedule")
    public String postSaveSchedule(@RequestParam String starttime, @RequestParam int sheduleEvent, @RequestParam int day) {
        Day newDay = null;
        ScheduleEvent scheduleEvent = new ScheduleEvent();
        try {
            newDay = dayServise.getById(day);
            scheduleEvent = schedulesEventService.getById(sheduleEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        localDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date date = null;
        try {
            date = localDateFormat.parse(starttime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Time sd = new java.sql.Time(date.getTime());
        Schedule schedule = new Schedule(newDay, sd, scheduleEvent);
        //    scheduleRepository.midifyingQuryInsertSchadule(Integer.parseInt(day),timestring,Integer.parseInt(event));
        try {
            scheduleServise.save(schedule);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/schedules";
    }

    @GetMapping("/dbclickcreate")
    public String getDoubleClickCreate(@RequestParam String time, @RequestParam String id, Model model) {
        List<Day> dayList = null;
        List<ScheduleEvent> eventList = null;
        try {
            eventList = schedulesEventService.getAll();
            dayList = dayServise.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("time", time);
        model.addAttribute("idday", id);
        model.addAttribute("eventshedule", eventList);
        model.addAttribute("weekday", dayList);
        return "schedule/dbclickcreate";
    }

    @GetMapping("/dbclickedit/{id}")
    public String getDoubleClickEdit(Model model, @PathVariable int id) {
        Schedule schedule = null;
        List<Day> dayList = null;
        List<ScheduleEvent> eventList = null;
        try {
            schedule = scheduleServise.getById(id);
            eventList = schedulesEventService.getAll();
            dayList = dayServise.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String time = schedule.getStarttime().toString();
        time = time.substring(0, 5);
        if (time.startsWith("0")) time = time.substring(1, 5);
        model.addAttribute("time", time);
        model.addAttribute("eventshedule", eventList);
        model.addAttribute("weekday", dayList);
        model.addAttribute("schedule", schedule);
        return "schedule/dbclickedit";
    }
//делаем таблицу
    private List<List<ScheduleSenderDTO>> makeTable(List<Day> dayList, List<Schedule> scheduleList) {
        ScheduleSenderDTO sender = null;
        //создаем двойной List для вывода всей таблицы
        List<List<ScheduleSenderDTO>> scheduleListSenders = new ArrayList<>();
        List<String> listTime=createListTime(scheduleList);
        for(String attributeTime:listTime){
     //   for (int i = startTime; i <= endTime; i++) {
            List<ScheduleSenderDTO> scheduleSenders = new ArrayList<>();
            //добавляем первый элемент для выода времени
         //   String attributeTime = i + ":00";
            sender = new ScheduleSenderDTO(attributeTime);
            scheduleSenders.add(sender);
            //перебираем последущие элементы для добавления в List scheduleSender
            for (Day d : dayList) {

                Integer idday = d.getId();
                sender = new ScheduleSenderDTO("", attributeTime, idday.toString());//имя ="",  атрибут Времени, арибут дня недели
                sender.setScheduleList(haveSchedules(d.getId(), attributeTime, scheduleList));
                scheduleSenders.add(sender);
            }
            scheduleListSenders.add(scheduleSenders);
       }
        return scheduleListSenders;
    }



    private List<Schedule> haveSchedules(int dayId, String time, List<Schedule> readScheduleList) {
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = localDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        List<Schedule> scheduleList = new ArrayList<>();
        for (Schedule schedule : readScheduleList) {
            //если совпадает в прочитаном List событий id дня недели с id дня и дата с датой события
            if ((schedule.getDay().getId() == dayId) && (date.getTime() == schedule.getStarttime().getTime())) {
                scheduleList.add(schedule);
            }
        }


        return scheduleList;
    }
    private int sortingFlag(Schedule sch,String time){
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        localDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date date = null;
        try {
            date = localDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int time_int=(int)date.getTime();
        int higttime=sch.getDay().getAttribute()*100000000;
        return time_int+higttime;
    }
    private  List<ScheduleSenderDTO> sortScheduleSender(List<ScheduleSenderDTO> listSender){
        Collections.sort(listSender, new Comparator<ScheduleSenderDTO>() {
            @Override
            public int compare(ScheduleSenderDTO o1, ScheduleSenderDTO o2) {

                if(o1.getNumbersort()>o2.getNumbersort())
                {
                    return 1;
                }
                if(o1.getNumbersort()<o2.getNumbersort()){
                    return -1;
                }
                return 0;
            }
        });
        for(int i=0;i<listSender.size();i++){
            listSender.get(i).setNumbersort(i+1);
        }

        return listSender;
    }
    //сортируем день недели по признаку
    private List<Day> sortDay(List<Day>dayList){
        Collections.sort(dayList, new Comparator<Day>() {
            @Override
            public int compare(Day day1, Day day2) {
                if(day1.getAttribute()>day2.getAttribute()){
                    return 1;
                }
                if(day1.getAttribute()<day2.getAttribute()){
                    return -1;
                }
                return 0;
            }
        });
        return dayList;
    }
    private List<String> createListTime(List<Schedule> scheduleList) {
        List<String> list=new ArrayList<>();
        for(Schedule schedule:scheduleList){
            String time=schedule.getStarttime().toString().substring(0,5);
            list.add(time);
        }
        list=sortTimeString(list);
        list=removeDublicate(list);
        return list;
    }
    private List<String> sortTimeString(List<String> list){
      Collections.sort(list, new Comparator<String>() {
          @Override
          public int compare(String o1, String o2) {
              if(stringTimeToLong(o1)>stringTimeToLong(o2)){
                  return 1;
              }
              if(stringTimeToLong(o1)<stringTimeToLong(o2)){
                  return -1;
              }
              return 0;
          }
      });
      return list;
    }
    private long stringTimeToLong(String time){
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = localDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
    private List<String> removeDublicate(List<String> list){
        List<String> list2 = new ArrayList<String>();
        HashSet<String> lookup = new HashSet<String>();
        for (String item : list) {
            if (lookup.add(item)) {
                // Set.add returns false if item is already in the set
                list2.add(item);
            }
        }
        return list2;
    }

}
