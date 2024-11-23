package ru.practicum.data.adminData;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.User;

import java.util.List;

public interface AdminUserRepository extends JpaRepository<User, Long> {

    @Query("select us " +
            "from User as us " +
            "where us.id in ?1 " +
            "order by us.id asc " +
            "limit ?3 offset ?2")
    List<User> getAllByIdIn(List<Long> ids, Integer from, Integer size);

    @Query("select us " +
            "from User as us " +
            "order by us.id asc " +
            "limit ?2 offset ?1")
    List<User> getAllByPage(Integer from, Integer size);
}
