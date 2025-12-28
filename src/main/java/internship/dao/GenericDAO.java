package internship.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
public abstract class GenericDAO<T> {

    @PersistenceContext(unitName = "InternshipPU")
    protected EntityManager em;

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        try {
            T result = em.merge(entity);
            em.flush();
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde", e);
        }
    }

    public Optional<T> findById(Long id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }

    public List<T> findAll() {
        CriteriaQuery<T> cq = em.getCriteriaBuilder().createQuery(entityClass);
        cq.select(cq.from(entityClass));
        return em.createQuery(cq).getResultList();
    }

    public void delete(T entity) {
        try {
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            em.flush();
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }

    public void deleteById(Long id) {
        try {
            T entity = findById(id).orElse(null);
            if (entity != null) {
                em.remove(entity);
                em.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la suppression", e);
        }
    }
}
