package ru.otus.library.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.models.domain.Book;
import ru.otus.library.services.SequenceGeneratorService;

@Component
public class BookModelListener  extends AbstractMongoEventListener<Book> {
    private final SequenceGeneratorService sequenceGenerator;

    public BookModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Book> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Book.SEQUENCE_NAME));
        }
    }
}
