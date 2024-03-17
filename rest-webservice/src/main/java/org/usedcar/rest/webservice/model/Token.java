package org.usedcar.rest.webservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(length = 450)
    private String token;
    private boolean isRevoked;
    @Enumerated(EnumType.ORDINAL)
    private TokenType tokenType;

    public Token(String token, TokenType tokenType) {
        this.token = token;
        this.isRevoked = false;
        this.tokenType = tokenType;
    }
}
