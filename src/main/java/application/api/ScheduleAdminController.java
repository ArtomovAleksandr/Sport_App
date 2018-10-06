package application.api;

import application.entity.Day;
import application.entity.Schedule;
import application.entity.ScheduleEvent;
import application.service.implementations.ScheduleServise;
import application.service.implementations.SchedulesEventServise;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/schedules")
public class ScheduleAdminController {
    @Autowired
    ScheduleServise scheduleServise;
    @Autowired

    SchedulesEventServise schedulesEventServise;
    public String getSchedules(Model model){
        List<Schedule> scheduleList=scheduleServise.getAll();
        return "schedule";
    }
    @GetMapping("/save_schedule_event")
    public String getSaveScheduleEvent(){
        return "save_schedule_event";
    }
    @PostMapping("/save_schedule_event")
    public String postSaveScheduleEvent(ScheduleEvent event){
        schedulesEventServise.save(event);
        return "redirect:/";
    }
    @GetMapping("/save_shedule")
    public String getSaveSchedule(){
        return "save_schedule";
    }
    @PostMapping("/save_shedule")
    public String postSaveSchedule(HttpServletRequest request){
        String event=request.getParameter("event_schedule");
        String day=request.getParameter("day");
        String hour=request.getParameter("hour");
        String min=request.getParameter("min");
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        String timestring = hour+":"+min;//"Mar 19 2018 - 14:39";
        Date date=null;
        try {
            date= localDateFormat.parse(timestring);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date==null)  return "redirect:/";
        Day day1=new Day(Integer.parseInt(day));
        java.sql.Date sd = new java.sql.Date(date.getTime());
         ScheduleEvent event1=new ScheduleEvent(Integer.parseInt(event));
         Schedule schedule=new Schedule(day1,sd,event1);
         scheduleServise.save(schedule);
        return "redirect:/";
    }
}
