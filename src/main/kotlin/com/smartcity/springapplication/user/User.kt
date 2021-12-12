package com.smartcity.springapplication.user

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,
    val email: String? = null,
    val userName: String? = null,
    val passWord: String? = null,
)