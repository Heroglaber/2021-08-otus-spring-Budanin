package ru.otus.library.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.models.domain.Genre;
import ru.otus.library.services.SequenceGeneratorService;

@Component
public class GenreModelListener  extends AbstractMongoEventListener<Genre> {
    private final SequenceGeneratorService sequenceGenerator;

    public GenreModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Genre> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Genre.SEQUENCE_NAME));
        }
    }
}
