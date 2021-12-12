package com.smartcity.springapplication.flashDeal

import com.smartcity.springapplication.store.StoreService
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring", uses = [StoreService::class])
interface FlashDealMapper {
    @Mapping(
        source = "flash_deal_creation_dto.provider_id",
        target = "store",
        qualifiedByName = ["find_store_by_provider_id"]
    )
    fun toModel(flashDealCreationDto: FlashDealCreationDTO?): FlashDeal?

    @Mapping(source = "flash_deal.store", target = "store_name", qualifiedByName = ["get_store_name"])
    @Mapping(source = "flash_deal.store", target = "store_address", qualifiedByName = ["getStore_address"])
    @Mapping(source = "flash_deal.store", target = "latitude", qualifiedByName = ["get_store_address_lat"])
    @Mapping(source = "flash_deal.store", target = "longitude", qualifiedByName = ["get_store_address_lon"])
    fun toDTO(flashDeal: FlashDeal?): FlashDealDTO?
}