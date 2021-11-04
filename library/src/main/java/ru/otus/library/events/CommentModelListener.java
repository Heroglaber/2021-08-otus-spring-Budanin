package ru.otus.library.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.models.domain.Comment;
import ru.otus.library.services.SequenceGeneratorService;

@Component
public class CommentModelListener  extends AbstractMongoEventListener<Comment> {
    private final SequenceGeneratorService sequenceGenerator;

    public CommentModelListener(SequenceGeneratorService sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<Comment> event) {
        if (event.getSource().getId() < 1) {
            event.getSource().setId(sequenceGenerator.generateSequence(Comment.SEQUENCE_NAME));
        }
    }
}
