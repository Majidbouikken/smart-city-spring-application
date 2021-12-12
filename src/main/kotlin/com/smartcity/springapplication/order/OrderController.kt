package com.smartcity.springapplication.order

import com.smartcity.springapplication.utils.Results
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/order")
class OrderController @Autowired constructor(
    val orderService: OrderService
) {
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    fun addProductToCart(@RequestBody orderCreationDTO: OrderCreationDTO): Response<String> =
        orderService.createOrder(orderCreationDTO)

    @GetMapping("/current-provider-search-orders-id")
    @ResponseStatus(HttpStatus.OK)
    fun searchProviderOrdersById(
        @RequestParam("id") id: Long,
        @RequestParam(name = "orderId") orderId: Long
    ): Results<OrderDTO?> =
        Results(orderService.searchProviderOrdersById(id, orderId))

    @GetMapping("/current-provider-search-orders-receiver")
    @ResponseStatus(HttpStatus.OK)
    fun searchProviderOrdersByReceiver(
        @RequestParam("id") id: Long,
        @RequestParam(name = "firstName") firstName: String?,
        @RequestParam(name = "lastName") lastName: String?
    ): Results<OrderDTO?> =
        Results(orderService.searchProviderOrdersByReceiver(id, firstName, lastName))

    @GetMapping("/current-provider-search-orders-date")
    @ResponseStatus(HttpStatus.OK)
    fun searchProviderOrdersByDate(
        @RequestParam("id") id: Long,
        @RequestParam(name = "date") date: String?
    ): Results<OrderDTO?> =
        Results(orderService.searchProviderOrdersByDate(id, date))

    @GetMapping("/current-provider-past-orders")
    @ResponseStatus(HttpStatus.OK)
    fun getPastOrderByProviderId(
        @RequestParam("id") id: Long
    ): Results<OrderDTO?> =
        Results(orderService.getPastOrders(id))

    @GetMapping("/current-provider-orders")
    @ResponseStatus(HttpStatus.OK)
    fun getOrderByProviderId(
        @RequestParam("id") id: Long,
        @RequestParam(name = "date", defaultValue = "NONE", required = false) dateFilter: String?,
        @RequestParam(name = "amount", defaultValue = "NONE", required = false) amountFilter: String?,
        @RequestParam(name = "type", defaultValue = "NONE", required = false) type: String?,
        @RequestParam(name = "step") step: OrderStep?
    ): Results<OrderDTO?> =
        Results(orderService.getOrderByProviderId(id, dateFilter!!, amountFilter!!, step!!, type!!))

    @GetMapping("/current-provider-today-orders")
    @ResponseStatus(HttpStatus.OK)
    fun getTodayOrdersByProviderId(
        @RequestParam("id") id: Long,
        @RequestParam(name = "date", defaultValue = "NONE", required = false) dateFilter: String?,
        @RequestParam(name = "amount", defaultValue = "NONE", required = false) amountFilter: String?,
        @RequestParam(name = "type", defaultValue = "NONE", required = false) type: String?,
        @RequestParam(name = "step") step: OrderStep?
    ): Results<OrderDTO?> =
        Results(orderService.getTodayOrdersByProviderId(id, dateFilter!!, amountFilter!!, step!!, type!!))

    @GetMapping("/current-provider-dates-orders")
    @ResponseStatus(HttpStatus.OK)
    fun filterOrdersByCreatAtByProviderId(
        @RequestParam(name = "id") id: Long?,
        @RequestParam(name = "startDate") startDate: String?,
        @RequestParam(name = "endDate") endDate: String?,
        @RequestParam(name = "date", defaultValue = "NONE", required = false) dateFilter: String?,
        @RequestParam(name = "amount", defaultValue = "NONE", required = false) amountFilter: String?,
        @RequestParam(name = "type", defaultValue = "NONE", required = false) type: String?,
        @RequestParam(name = "step") step: OrderStep?
    ): Results<OrderDTO?> =
        Results(
            orderService.getBetweenOrdersByCreatAtByProviderId(
                id,
                startDate,
                endDate,
                dateFilter!!,
                amountFilter!!,
                step!!,
                type!!
            )
        )

    @GetMapping("/current-user/inProgress")
    @ResponseStatus(HttpStatus.OK)
    fun getInProgressOrdersByUserId(
        @RequestParam("id") id: Long,
        @RequestParam(name = "date", defaultValue = "NONE", required = false) dateFilter: String?,
        @RequestParam(name = "amount", defaultValue = "NONE", required = false) amountFilter: String?,
        @RequestParam(name = "type", defaultValue = "NONE", required = false) type: String?
    ): Results<OrderDTO?> = Results(orderService.getInProgressOrdersByUserId(id, dateFilter!!, amountFilter!!, type!!))

    @GetMapping("/current-user/finalized")
    @ResponseStatus(HttpStatus.OK)
    fun getFinalizedOrdersByUserId(
        @RequestParam("id") id: Long,
        @RequestParam(name = "date", defaultValue = "NONE", required = false) dateFilter: String?,
        @RequestParam(name = "amount", defaultValue = "NONE", required = false) amountFilter: String?,
        @RequestParam(name = "type", defaultValue = "NONE", required = false) type: String?,
        @RequestParam(name = "status", defaultValue = "NONE", required = false) status: String?
    ): Results<OrderDTO?> =
        Results(orderService.getFinalizedOrdersByUserId(id, dateFilter!!, amountFilter!!, type!!, status!!))

    @PutMapping("/current-store/{id}/accept")
    @ResponseStatus(HttpStatus.OK)
    fun acceptedOrderByStore(@PathVariable("id") id: Long): Response<String> = orderService.acceptOrderByStore(id)

    @PutMapping("/current-store/{id}/reject")
    @ResponseStatus(HttpStatus.OK)
    fun rejectOrderByStore(@PathVariable("id") id: Long): Response<String> = orderService.rejectOrderByStore(id)

    @PutMapping("/current-store/{id}/ready")
    @ResponseStatus(HttpStatus.OK)
    fun readyOrderByStore(@PathVariable("id") id: Long): Response<String> = orderService.readyOrderByStore(id)

    @PutMapping("/current-store/{id}/delivered")
    @ResponseStatus(HttpStatus.OK)
    fun deliveredOrderByStore(
        @PathVariable("id") id: Long,
        @RequestParam(name = "comment") comment: String?,
        @RequestParam(name = "date") date: String?
    ): Response<String> = orderService.deliveredOrderByStore(id, comment!!, date!!)

    @PutMapping("/current-store/{id}/pickedUp")
    @ResponseStatus(HttpStatus.OK)
    fun pickedUpOrderByStore(
        @PathVariable("id") id: Long,
        @RequestParam(name = "comment") comment: String?,
        @RequestParam(name = "date") date: String?
    ): Response<String> = orderService.pickedUpOrderByStore(id, comment!!, date!!)

    @PutMapping("/current-store-note")
    @ResponseStatus(HttpStatus.OK)
    fun setOrderNote(
        @RequestParam(name = "id") id: Long,
        @RequestParam(name = "note") note: String
    ): Response<String> = orderService.setNote(id, note)

    @PutMapping("/current-user/{id}/received")
    @ResponseStatus(HttpStatus.OK)
    fun deliveredOrderByStoreConfirmed(@PathVariable("id") id: Long): Response<String> =
        orderService.receivedOrderByUser(id)

    /*  @PutMapping("/current-user/{id}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public Response<String> cancelOrderByUser(@PathVariable("id") Long id) {
        return orderService.cancelOrderByUser(id);
    }*/
}