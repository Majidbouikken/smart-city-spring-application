package com.smartcity.springapplication.bill

import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface BillMapper {
    fun toDTO(bill: Bill?): BillDTO?
}