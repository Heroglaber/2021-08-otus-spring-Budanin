package ru.otus.library.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.models.domain.Author;
import ru.otus.library.services.SequenceGeneratorService;

@Component
public class AuthorModelListener  extends AbstractMongoEventListener<Author> {
    private final SequenceGeneratorService sequenceGenerator;

    public AuthorModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Author> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Author.SEQUENCE_NAME));
        }
    }
}
