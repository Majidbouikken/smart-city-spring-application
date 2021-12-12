package com.smartcity.springapplication.user.simpleUser

import com.smartcity.springapplication.utils.DateUtil
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Named

import java.util.Date

@Mapper(componentModel = "spring")
interface SimpleUserInformationMapper {
    @Mapping(source = "simpleUser.birthDay", target = "birthDay", qualifiedByName = ["getStringDate"])
    fun toDTO(simpleUser: SimpleUser?): SimpleUserInformationDTO?

    @Named("getStringDate")
    fun getStringDate(date: Date?): String? = if (date != null) {
        DateUtil.parseString(date)
    } else null
}