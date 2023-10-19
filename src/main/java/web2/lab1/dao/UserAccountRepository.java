package web2.lab1.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import web2.lab1.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
