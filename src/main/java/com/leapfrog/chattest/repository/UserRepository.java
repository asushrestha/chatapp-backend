package com.leapfrog.chattest.repository;

import com.leapfrog.chattest.dto.LoginResponse;
import com.leapfrog.chattest.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

    @Query("SELECT users FROM Users users WHERE UPPER(users.userName)=:userName")
    Optional<Users> findUserForRegistration(String userName);

    @Query("SELECT users FROM Users users WHERE UPPER(users.userName)=:userName AND  users.password=:password")
    Optional<Users> findUserForAuthentication(String userName, String password);


    @Query("SELECT users FROM Users users WHERE UPPER(users.userName)=:userName")
    Optional<Users> findUserByUserName(String userName);

    @Query("SELECT users FROM Users users WHERE users!=:validUsers")
    List<Users> findAllButNotCurrentUser(Users validUsers);
}
