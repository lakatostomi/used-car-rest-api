package org.usedcar.rest.webservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.usedcar.rest.webservice.model.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    Token findTokenByToken(String token);
}
