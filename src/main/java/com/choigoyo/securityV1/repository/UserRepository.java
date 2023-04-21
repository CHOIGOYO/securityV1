package com.choigoyo.securityV1.repository;

import com.choigoyo.securityV1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * CRUD 함수를 JPA Repository가 들고있음.
 * UserRepository는 @Repository가 없어도 ioc가 가능 => 이유는 JpaRepository 확장하여 사용하기 때문에
*/
public interface UserRepository extends JpaRepository<User, Integer> {
}
