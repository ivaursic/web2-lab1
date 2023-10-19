package web2.lab1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web2.lab1.dao.ResultRepository;
import web2.lab1.service.ResultService;

@Service
public class ResultServiceJpa implements ResultService {

    @Autowired
    private ResultRepository resultRepository;
}
