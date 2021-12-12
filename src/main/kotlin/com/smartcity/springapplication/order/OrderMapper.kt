package com.smartcity.springapplication.order

import com.smartcity.springapplication.address.AddressMapper
import com.smartcity.springapplication.bill.BillMapper
import com.smartcity.springapplication.orderProductVariant.OrderProductVariantMapper
import com.smartcity.springapplication.store.StoreService
import com.smartcity.springapplication.user.simpleUser.SimpleUserService
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(
    componentModel = "spring",
    uses = [BillMapper::class, AddressMapper::class, OrderProductVariantMapper::class, SimpleUserService::class, StoreService::class]
)
interface OrderMapper {
    @Mapping(source = "order.store", target = "store_name", qualifiedByName = ["get_store_name"])
    @Mapping(source = "order.store", target = "store_address", qualifiedByName = ["get_store_address"])
    fun toDTO(order: Order?): OrderDTO?

    @Mapping(source = "order_creation_dto.user_id", target = "user")
    @Mapping(source = "order_creation_dto.store_id", target = "store", qualifiedByName = ["find_store_by_id"])
    fun toModel(orderCreationDto: OrderCreationDTO?): Order?
}