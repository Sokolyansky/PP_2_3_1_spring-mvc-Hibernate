package web.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import web.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User updatedUser) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser == null) {
            // Обработка случая, когда записи не существует
            throw new RuntimeException("Пользователь не найден");
        }
        // Выполнение обновления сущности внутри транзакции
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());

    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser == null) {
            // Обработка случая, когда записи не существует
            throw new RuntimeException("Пользователь не найден");
        }
        entityManager.remove(existingUser);
    }
}