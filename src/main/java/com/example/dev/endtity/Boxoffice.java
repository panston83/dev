package com.example.dev.endtity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Boxoffice extends  BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // index 자동생성
    private Long gno;

    @Column(nullable = false)
    private Integer rank;

    @Column(length = 50, nullable = false)
    private String name;
}
