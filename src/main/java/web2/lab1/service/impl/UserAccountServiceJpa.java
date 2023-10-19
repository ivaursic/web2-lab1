package web2.lab1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web2.lab1.dao.UserAccountRepository;
import web2.lab1.service.UserAccountService;

@Service
public class UserAccountServiceJpa implements UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;
}
