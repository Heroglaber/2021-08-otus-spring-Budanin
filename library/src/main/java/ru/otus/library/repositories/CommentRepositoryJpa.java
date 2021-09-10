package ru.otus.library.repositories;

import org.springframework.stereotype.Repository;
import ru.otus.library.models.Book;
import ru.otus.library.models.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class CommentRepositoryJpa implements CommentRepository{
    @PersistenceContext
    private final EntityManager em;

    public CommentRepositoryJpa(EntityManager em) {
        this.em = em;
    }

    @Override
    public Comment save(Comment comment) {
        if(comment.getId() <= 0) {
            em.persist(comment);
            return comment;
        } else {
            return em.merge(comment);
        }
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findByBook(Book book) {
        TypedQuery<Comment> query =
                em.createQuery("select c from Comment c " +
                        "where c.book = :book", Comment.class);
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public List<Comment> findAll() {
        TypedQuery<Comment> query =
                em.createQuery("select c from Comment c", Comment.class);
        return query.getResultList();
    }

    @Override
    public void updateCommentById(long id, String message) {
        Query query = em.createQuery("update Comment c " +
                "set c.message = :message " +
                "where c.id = :id");
        query.setParameter("id", id);
        query.setParameter("message", message);
        query.executeUpdate();
    }

    @Override
    public void deleteById(long id) {
        Comment comment = em.find(Comment.class, id);
        if(comment != null) {
            em.remove(comment);
        }
    }
}
