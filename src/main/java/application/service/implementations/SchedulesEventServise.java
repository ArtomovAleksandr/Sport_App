package application.service.implementations;

import application.entity.ScheduleEvent;
import application.repository.ScheduleRepository;
import application.repository.SheduleEventRepository;
import application.service.interfaces.EntityService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SchedulesEventServise implements EntityService<ScheduleEvent> {
    @Autowired
    SheduleEventRepository repository;
    @Override
    public List<ScheduleEvent> getAll() {
        return repository.findAll();
    }

    @Override
    public ScheduleEvent getById(int id) {
        return repository.getOne(id);
    }

    @Override
    public void save(ScheduleEvent scheduleEvent) {
        repository.save(scheduleEvent);
    }

    @Override
    public void delete(ScheduleEvent scheduleEvent) {
        repository.delete(scheduleEvent);
    }
}
