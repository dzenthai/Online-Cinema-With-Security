package org.online.cinema.store.repo;

import org.online.cinema.store.entity.User;
import org.online.cinema.store.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findUserByUser(User user);

    UserInfo findByUser_Email(String email);

}
