package application.service.implementations;

import application.entity.Card;
import application.repository.CardRepository;
import application.service.interfaces.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService implements EntityService<Card> {
    @Autowired
    CardRepository repository;

    @Override
    public List<Card> getAll() throws Throwable {
        return repository.findAll();
    }

    @Override
    public Card getById(int id) throws Throwable {
        return repository.findById(id).get();
    }

    @Override
    public void save(Card card) throws Throwable {
        repository.save(card);
    }

    @Override
    public void delete(int id) throws Throwable {
        repository.delete(repository.findById(id).get());
    }
}