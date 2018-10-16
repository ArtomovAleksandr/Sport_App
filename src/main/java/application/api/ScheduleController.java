package application.api;

import application.dto.ScheduleDTO;
import application.dto.ScheduleEventDTO;
import application.entity.Day;
import application.entity.Schedule;
import application.entity.ScheduleEvent;
import application.helper.JSONResult;
import application.helper.JSONResultError;
import application.helper.JSONResultOk;
import application.service.implementations.DayServise;
import application.service.implementations.ScheduleService;
import application.service.implementations.SchedulesEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequestMapping("/api/1.0/schedules")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleServise;
    @Autowired
    SchedulesEventService schedulesEventService;
    @Autowired
    DayServise dayServise;

    // получить список расписание
    @GetMapping()
    public JSONResult<List<Schedule>> getSchedule() {
        List<Schedule> schedules = new ArrayList<>();
        try {
            schedules = scheduleServise.getAll();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONResultError<>(schedules, ex.getMessage());
        }
        return new JSONResultOk<>(schedules);
    }

    @PutMapping("/scheduleedit/{id}")
    public JSONResult<Schedule> putSaveSchedule(@RequestBody ScheduleDTO schedule, @PathVariable("id") int id) {
        Schedule currentSchedule = new Schedule();
        Day newDay = null;
        SimpleDateFormat localDateFormat = new SimpleDateFormat("HH:mm");
        localDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
        Date date = null;
        ScheduleEvent scheduleEvent = new ScheduleEvent();
        try {
            newDay = dayServise.getById(schedule.getDayid());
        //    date = localDateFormat.parse(currentSchedule.getStarttime().toString());
            date=localDateFormat.parse(schedule.getStarttime());
            scheduleEvent = schedulesEventService.getById(schedule.getEventschedule());
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONResultError<>(currentSchedule, "day not read!");
        }

        java.sql.Time sd = new java.sql.Time(date.getTime());
        Schedule editschedule = new Schedule(newDay, sd, scheduleEvent);
      //  schedule.setId(id);

        try {
            currentSchedule = scheduleServise.getById(id);
            if (currentSchedule == null) {
                return new JSONResultError<>(currentSchedule, "schedule not find!");
            }
            currentSchedule.setDay(editschedule.getDay());
            currentSchedule.setScheduleEvent(editschedule.getScheduleEvent());
            currentSchedule.setStarttime(editschedule.getStarttime());
            scheduleServise.save(currentSchedule);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new JSONResultError<>(currentSchedule, ex.getMessage());
        }
        return new JSONResultOk<>(currentSchedule);
    }
/*    @PutMapping("scheduleevent/{id}")
    public JSONResult<ScheduleEvent> putScheduleEvent(@RequestBody ScheduleEventDTO scheduleEven,@PathVariable("id") int id){
        ScheduleEvent currentscheduleEvent=new ScheduleEvent();
        try {
            currentscheduleEvent=schedulesEventService.getById(id);
            if(currentscheduleEvent==null){
                return new JSONResultError<>(currentscheduleEvent,"sheduleevent not find!");
            }
            currentscheduleEvent.setName(scheduleEven.getName());
            currentscheduleEvent.setDescription(scheduleEven.getDescription());
            schedulesEventService.save(currentscheduleEvent);
        }catch (Exception ex){
            ex.printStackTrace();
            return new JSONResultError<>(currentscheduleEvent,ex.getMessage());
        }
      return new JSONResultOk<>(currentscheduleEvent);

    }*/

    @DeleteMapping("/{id}")
    public JSONResult<Schedule> deleteSchedule(@PathVariable("id") int id){
        Schedule schedule=new Schedule();
        try{
           schedule=scheduleServise.getById(id);
           scheduleServise.delete(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return new JSONResultError<>(schedule,ex.getMessage());
        }
        return new JSONResultOk<>(schedule);
    }
}
