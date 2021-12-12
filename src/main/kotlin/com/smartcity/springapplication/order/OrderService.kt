package com.smartcity.springapplication.order

import com.google.firebase.database.annotations.NotNull
import lombok.AllArgsConstructor
import com.smartcity.springapplication.orderProductVariant.OrderProductVariantRepository
import com.smartcity.springapplication.user.simpleUser.cart.CartService
import com.smartcity.springapplication.notification.service.NotificationService
import com.smartcity.springapplication.bill.BillService
import com.smartcity.springapplication.productVariant.ProductVariantService
import com.smartcity.springapplication.utils.error_handler.OrderException
import com.smartcity.springapplication.utils.error_handler.DateException
import com.smartcity.springapplication.notification.NotificationType
import com.smartcity.springapplication.user.simpleUser.cart.CartProductVariant
import com.smartcity.springapplication.user.simpleUser.cart.CartProductVariantId
import com.smartcity.springapplication.orderProductVariant.OrderProductVariant
import com.smartcity.springapplication.bill.Bill
import com.smartcity.springapplication.bill.BillTotalDTO
import com.smartcity.springapplication.notification.Notification
import com.smartcity.springapplication.productVariant.ProductVariant
import com.smartcity.springapplication.offer.OfferType
import com.smartcity.springapplication.utils.pair.Pair
import java.math.BigDecimal
import com.smartcity.springapplication.orderProductVariant.OrderProductVariantId
import com.smartcity.springapplication.store.Store
import com.smartcity.springapplication.user.simpleUser.cart.Cart
import com.smartcity.springapplication.utils.DateUtil
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.stream.Collectors
import javax.transaction.Transactional

@Service
@AllArgsConstructor
class OrderService @Autowired constructor(
    val orderRepository: OrderRepository,
    val orderProductVariantRepository: OrderProductVariantRepository,
    val cartService: CartService,
    val notificationService: NotificationService,
    val billService: BillService,
    val productVariantService: ProductVariantService,
) {
    private val orderMapper: OrderMapper? = null

    // Find Order by order_id, this is used in Service methods
    fun findOrderById(orderId: Long): Order = orderRepository.findById(orderId)
        .orElseThrow { OrderException("error.order.notFound") }!!

    // Provider method: Change Provider Note value in an Order
    @NotNull
    fun setNote(orderId: Long, note: String): Response<String> {
        Optional.of(findOrderById(orderId))
            .map { order: Order -> setProviderNote(order, note) }
            .map { entity: Order -> orderRepository.save(entity) }
        return Response("created.")
    }

    // Change Provider Note value in an Order, this is used in setNote method in Service
    private fun setProviderNote(order: Order, note: String): Order {
        order.providerNote = note
        return order
    }

    // Provider method: Gives all Orders
    fun searchProviderOrdersById(providerId: Long?, orderId: Long): List<OrderDTO?> {
        val sort = sortOrdersByProperty("DESC", "NONE")
        return orderRepository.findByStoreProviderId(providerId, sort)!!.stream()
            .filter(Predicate { order: Order? -> order!!.id == orderId })
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    // Provider method: Gives all Orders of a receiver
    fun searchProviderOrdersByReceiver(
        providerId: Long?,
        receiverFirstName: String?,
        receiverLastName: String?
    ): List<OrderDTO?> {
        val sort = sortOrdersByProperty("DESC", "NONE")
        return orderRepository.findByStoreProviderId(providerId, sort)!!.stream()
            .filter(Predicate { order: Order? ->
                order!!.receiverFirstName.equals(
                    receiverFirstName,
                    ignoreCase = true
                ) && order.receiverLastName.equals(receiverLastName, ignoreCase = true)
            })
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    // Provider method: Gives all Orders of a Date
    fun searchProviderOrdersByDate(providerId: Long?, date: String?): List<OrderDTO?> {
        val sort = sortOrdersByProperty("DESC", "NONE")
        if (!DateUtil.isValidDate(date)) {
            throw DateException("error.date.invalid")
        }
        val startOfDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.MIDNIGHT)
        val endOfDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX)
        return orderRepository.findByStoreProviderIdAndCreateAtBetween(providerId, startOfDate, endOfDate, sort)!!
            .stream()
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    fun toConfirmedOrders(): MutableList<Order> {
        val todayDate = Date()
        return orderRepository.findAll().stream()
            .filter(Predicate { order: Order? -> order!!.providerDate != null })
            .filter(Predicate { order: Order? -> todayDate.after(DateUtil.addDaysToDate(order!!.providerDate, 3)) })
            .filter(Predicate { order: Order? -> order!!.orderState!!.isAccepted() && order.orderState!!.isReady() && !order.orderState!!.isReceived() })
            .filter(Predicate { order: Order? -> order!!.orderState!!.isDelivered() || order.orderState!!.isPickedUp() })
            .collect(Collectors.toList())
    }

    fun toSendUserNotification() {
        val todayDate = Date()
        val collect = orderRepository.findAll().stream()
            .filter(Predicate { order: Order? -> order!!.providerDate != null })
            .filter(Predicate { order: Order? -> todayDate.before(DateUtil.addDaysToDate(order!!.providerDate, 3)) })
            .filter(Predicate { order: Order? -> order!!.orderState!!.isAccepted() && order.orderState!!.isReady() && !order.orderState!!.isReceived() })
            .filter(Predicate { order: Order? -> order!!.orderState!!.isDelivered() || order.orderState!!.isPickedUp() })
            .collect(Collectors.toList())
        collect.forEach(Consumer { order: Order? -> sendUserNotificationConfirmReceiveOrder(order!!) })
    }

    private fun sendUserNotificationConfirmReceiveOrder(order: Order) {
        notificationService.sendNotification(
            Notification(
                "Confirm reception",
                "When you receive your order confirm it.",
                NotificationType.ORDER,
                "user-" + order.user!!.email!!.replace("@", ""),
                null
            )
        )
    }

    private fun sortOrdersByProperty(dateFilter: String, amountFilter: String): Sort {
        if (dateFilter != "NONE") {
            return Sort.by(Sort.Order(getSortDirection(dateFilter), "createAt"))
        }
        if (amountFilter != "NONE") {
            return Sort.by(Sort.Order(getSortDirection(amountFilter), "bill.total"))
        }
        throw OrderException("error.sort.invalid")
    }

    private fun filterOrdersByType(type: String, order: Order): Boolean {
        return if (type == "NONE") {
            true
        } else {
            type == order.orderType!!.name
        }
    }

    private fun filterOrdersByStatus(status: String, order: Order): Boolean {
        return if (status == "NONE") {
            true
        } else {
            if (status == "ACCEPTED" && order.orderState!!.accepted) {
                true
            } else status == "REJECTED" && order.orderState!!.rejected
        }
    }

    fun getInProgressOrdersByUserId(
        id: Long?,
        dateFilter: String,
        amountFilter: String,
        type: String
    ): List<OrderDTO?> {
        val sort = sortOrdersByProperty(dateFilter, amountFilter)
        return orderRepository.findByUserId(id, sort)!!.stream()
            .filter(Predicate { order: Order? -> filterOrdersByType(type, order!!) })
            .filter(Predicate { order: Order? -> !order!!.orderState!!.rejected && !order.orderState!!.received })
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    fun getFinalizedOrdersByUserId(
        id: Long?,
        dateFilter: String,
        amountFilter: String,
        type: String,
        status: String
    ): List<OrderDTO?> {
        val sort = sortOrdersByProperty(dateFilter, amountFilter)
        return orderRepository.findByUserId(id, sort)!!.stream()
            .filter(Predicate { order: Order? -> filterOrdersByType(type, order!!) })
            .filter(Predicate { order: Order? -> order!!.orderState!!.isRejected() || order.orderState!!.isReceived() })
            .filter(Predicate { order: Order? -> filterOrdersByStatus(status, order!!) })
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    fun getPastOrders(id: Long?): List<OrderDTO?> {
        val sort = sortOrdersByProperty("DESC", "NONE")
        return orderRepository.findByStoreProviderId(id, sort)!!.stream()
            .filter(Predicate { order: Order? -> PastOrderQualifier.apply(order!!) })
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    fun getOrderByProviderId(
        id: Long?,
        dateFilter: String,
        amountFilter: String,
        step: OrderStep,
        type: String
    ): List<OrderDTO?> {
        val stepQualifier = GetStepQualifier.apply(step).apply(GetSteps.get())
        val sort = sortOrdersByProperty(dateFilter, amountFilter)
        return orderRepository.findByStoreProviderId(id, sort)!!.stream()
            .filter(Predicate { order: Order? -> stepQualifier.value.apply(order!!) })
            .filter(Predicate { order: Order? -> filterOrdersByType(type, order!!) })
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    fun getTodayOrdersByProviderId(
        id: Long?,
        dateFilter: String,
        amountFilter: String,
        step: OrderStep,
        type: String
    ): List<OrderDTO?> {
        val stepQualifier = GetStepQualifier.apply(step).apply(GetSteps.get())
        val sort = sortOrdersByProperty(dateFilter, amountFilter)
        val today = LocalDateTime.now()
        val startOfDate = today
            .toLocalDate().atTime(LocalTime.MIDNIGHT)
        val endOfDate = today
            .toLocalDate().atTime(LocalTime.MAX)
        return orderRepository.findByStoreProviderIdAndCreateAtBetween(id, startOfDate, endOfDate, sort)!!
            .stream()
            .filter(Predicate { order: Order? -> stepQualifier.getValue().apply(order!!) })
            .filter(Predicate { order: Order? -> filterOrdersByType(type, order!!) })
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    fun getBetweenOrdersByCreatAtByProviderId(
        id: Long?,
        startDate: String?,
        endDate: String?,
        dateFilter: String,
        amountFilter: String,
        step: OrderStep,
        type: String
    ): List<OrderDTO?> {
        if (!DateUtil.isValidDate(startDate) || !DateUtil.isValidDate(endDate)) {
            throw DateException("error.date.invalid")
        }
        val stepQualifier = GetStepQualifier.apply(step).apply(GetSteps.get())
        val sort = sortOrdersByProperty(dateFilter, amountFilter)
        val startOfDate = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIDNIGHT)
        val endOfDate = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX)
        return orderRepository.findByStoreProviderIdAndCreateAtBetween(id, startOfDate, endOfDate, sort)!!
            .stream()
            .filter(Predicate { order: Order? -> stepQualifier.getValue().apply(order!!) })
            .filter(Predicate { order: Order? -> filterOrdersByType(type, order!!) })
            .map<OrderDTO?>(Function { order: Order? -> orderMapper!!.toDTO(order) })
            .collect(Collectors.toList())
    }

    private fun getSortDirection(sortingOrder: String): Sort.Direction {
        return if (sortingOrder == "ASC") {
            Sort.Direction.ASC
        } else {
            if (sortingOrder == "DESC") {
                Sort.Direction.DESC
            } else {
                throw OrderException("error.sort.invalid")
            }
        }
    }

    /* private Sort sortOrdersByProperties(String dateFilter, String amountFilter){
        return Sort.by(Arrays.asList(
                new Sort.Order(getSortDirection(dateFilter),"createAt"),
                new Sort.Order(getSortDirection(amountFilter),"bill.total")
                )
        );
    }*/
    @Transactional
    fun createOrder(orderCreationDTO: OrderCreationDTO): Response<String> {
        val cartProductVariants = orderCreationDTO.cartProductVariantIds!!.stream()
            .map { cartProductVariantId: CartProductVariantId? ->
                cartService.findCartProductVariantById(cartProductVariantId!!)
            }
            .collect(Collectors.toList())
        Optional.of(orderCreationDTO)
            .map { orderCreationDto: OrderCreationDTO? -> orderMapper!!.toModel(orderCreationDto) }
            .map { order: Order? -> setCreatAt(order) }
            .map { order: Order? -> checkValidOrder(order, cartProductVariants) }
            .map { order: Order? -> setOrderState(order!!) }
            .map { entity: Order -> orderRepository.save(entity) }
            .map { order: Order -> setOrderProductVariantByStore(order, cartProductVariants) }
            .map { entity: Order -> orderRepository.save(entity) }
            .map { order: Order -> sendStoreNotification(order) }
        deleteCartProductVariant(cartProductVariants)
        return Response("created.")
    }

    @NotNull
    private fun setOrderState(order: Order): Order {
        OrderState(
            null,
            newOrder = true,
            accepted = false,
            rejected = false,
            ready = false,
            delivered = false,
            pickedUp = false,
            received = false
        ).also { order.orderState = it }
        return order
    }

    private fun checkValidOrder(order: Order?, cartProductVariants: List<CartProductVariant>): Order {
        if (cartProductVariants.isEmpty() || order!!.orderType == null)
            throw OrderException("error.order.invalid")
        //todo check if delivery address is set
        if (order.orderType == OrderType.DELIVERY)
            if (!order.store!!.policies!!.delivery!!)
                throw OrderException("error.order.invalid")
        return order
    }

    private fun deleteCartProductVariant(cartProductVariants: List<CartProductVariant>) {
        cartProductVariants.stream()
            .map { cartProductVariant: CartProductVariant? -> cartService.deleteCartProductVariant(cartProductVariant!!) }
            .collect(Collectors.toList())
    }

    private fun getProductByStore(order: Order): List<CartProductVariant> =
        order.user!!.cart!!.cartProductVariants.stream()
            .filter { (_, _, cartProductVariant1): CartProductVariant -> cartProductVariant1!!.product!!.customCategory!!.store!!.id === order.store!!.id }
            .collect(Collectors.toList())

    private fun sendStoreNotification(order: Order): Order {
        notificationService.sendNotification(
            Notification(
                "New order",
                "New order arrived, check it!",
                NotificationType.ORDER,
                "provider-" + order.store!!.provider!!.email!!.replace("@", ""),
                null
            )
        )
        return order
    }

    private fun setOrderProductVariantByStore(order: Order, cartProductVariants: List<CartProductVariant>): Order {
        val orderProductVariants = cartProductVariants.stream()
            .map { variant: CartProductVariant -> initOrderProductVariant(order, variant) }
            .map { orderProductVariant: OrderProductVariant -> setOffer(orderProductVariant) }
            .map { entity: OrderProductVariant -> orderProductVariantRepository.save(entity) }
            .collect(Collectors.toSet())
        order.orderProductVariants = orderProductVariants
        order.bill = setOrderBill(order, cartProductVariants)
        return order
    }

    private fun setOffer(orderProductVariant: OrderProductVariant): OrderProductVariant {
        orderProductVariant.offer = productVariantService.getVariantOffer(
            orderProductVariant.orderProductVariant!!.offers
        )
        return orderProductVariant
    }

    private fun setOrderBill(order: Order, cartProductVariants: List<CartProductVariant>): Bill {
        val bill = Bill(
            null,
            orderTotal(cartProductVariants),
            billService.getTotalToPay(
                BillTotalDTO(
                    order.store!!.policies!!.id,
                    orderTotal(cartProductVariants),
                    order.orderType
                )
            ).total,
            LocalDateTime.now(),
            order,
            order.store
        )
        return billService.saveBill(bill)
    }

    private fun orderTotal(cartProductVariants: List<CartProductVariant>): Double = cartProductVariants.stream()
        .map { (_, _, cartProductVariant1, unit): CartProductVariant ->
            getVariantPrice(
                cartProductVariant1
            )!! * unit!!
        }
        .mapToDouble { obj: Double -> obj }
        .sum()

    private fun getVariantPrice(productVariant: ProductVariant?): Double? {
        var price: Double? = 0.0
        val offer = productVariantService.getVariantOffer(productVariant!!.offers)
        if (offer != null) {
            if (offer.type === OfferType.FIXED) {
                price = productVariant.price!! - offer.newPrice!!
            }
            if (offer.type === OfferType.PERCENTAGE) {
                val p = BigDecimal(productVariant.price!! - productVariant.price * offer.percentage!! / 100).setScale(
                    2,
                    RoundingMode.HALF_EVEN
                )
                price = p.toDouble()
            }
        } else {
            price = productVariant.price
        }
        return price
    }

    private fun initOrderProductVariant(order: Order, cartProductVariant: CartProductVariant): OrderProductVariant {
        return OrderProductVariant(
            OrderProductVariantId(
                order.id,
                cartProductVariant.cartProductVariant!!.id
            ),
            order,
            cartProductVariant.cartProductVariant,
            cartProductVariant.unit,
            null
        )
    }

    private fun setCreatAt(order: Order?): Order {
        order!!.createAt = LocalDateTime.now()
        return order
    }

    private fun getCartByProvider(cart: Cart): Map<Store?, MutableList<CartProductVariant>> {
        val map: MutableMap<Store?, MutableList<CartProductVariant>> = HashMap()
        for (cartProductVariant in cart.cartProductVariants) {
            val store = cartProductVariant.cartProductVariant!!.product!!.customCategory!!.store
            if (map.containsKey(store)) {
                map[store]!!.add(cartProductVariant)
            } else {
                map[store] = ArrayList(listOf(cartProductVariant))
            }
        }
        return map
    }

    private fun sendUserNotificationAcceptedOrder(order: Order): Order {
        notificationService.sendNotification(
            Notification(
                "Accepted order",
                "Your order is accepted by the store.",
                NotificationType.ORDER,
                "user-" + order.user!!.email!!.replace("@", ""),
                null
            )
        )
        return order
    }

    fun acceptOrderByStore(id: Long): Response<String> {
        Optional.of(findOrderById(id))
            .map { order: Order -> setAccepted(order) }
            .map { order: Order -> setNewOrder(order) }
            .map { entity: Order -> orderRepository.save(entity) }
            .map { order: Order -> sendUserNotificationAcceptedOrder(order) }
        return Response("created.")
    }

    fun rejectOrderByStore(id: Long): Response<String> {
        Optional.of(findOrderById(id))
            .map { order: Order -> setRejected(order) }
            .map { order: Order -> setNewOrder(order) }
            .map { entity: Order -> orderRepository.save(entity) }
        return Response("created.")
    }

    private fun sendUserNotificationReadyOrder(order: Order): Order {
        notificationService.sendNotification(
            Notification(
                "Ready order",
                "Your order is ready to picked up.",
                NotificationType.ORDER,
                "user-" + order.user!!.email!!.replace("@", ""),
                null
            )
        )
        return order
    }

    fun readyOrderByStore(id: Long): Response<String> {
        Optional.of(findOrderById(id))
            .map { order: Order -> setReady(order) }
            .map { entity: Order -> orderRepository.save(entity) }
            .filter { order: Order -> order.orderType == OrderType.SELFPICKUP }
            .map { order: Order -> sendUserNotificationReadyOrder(order) }
        return Response("created.")
    }

    fun deliveredOrderByStore(id: Long, comment: String, date: String): Response<String> {
        Optional.of(findOrderById(id))
            .map { order: Order -> setDelivered(order) }
            .map { order: Order -> setComment(order, comment, date) }
            .map { entity: Order -> orderRepository.save(entity) }
        return Response("created.")
    }

    fun pickedUpOrderByStore(id: Long, comment: String, date: String): Response<String> {
        Optional.of(findOrderById(id))
            .map { order: Order -> setPickedUp(order) }
            .map { order: Order -> setComment(order, comment, date) }
            .map { entity: Order -> orderRepository.save(entity) }
        return Response("created.")
    }

    fun receivedOrderByUser(id: Long): Response<String> {
        Optional.of(findOrderById(id))
            .map { order: Order -> setReceived(order) }
            .map { entity: Order -> orderRepository.save(entity) }
        return Response("created.")
    }

    /*public Response<String> cancelOrderByUser(Long id){
        Optional.of(findOrderById(id))
                .map(this::setCanceled)
                .map(orderRepository::save);
        return new Response<>("created.");
    }*/
    @NotNull
    private fun setComment(order: Order, comment: String, date: String): Order {
        if (!DateUtil.isValidDateTime(date)) {
            throw DateException("error.date.invalid")
        }
        order.providerDate = DateUtil.parseDateTime(date)
        order.providerComment = comment
        return order
    }

    @NotNull
    private fun setAccepted(order: Order): Order {
        order.orderState!!.accepted = true
        return order
    }

    @NotNull
    private fun setRejected(order: Order): Order {
        order.orderState!!.rejected = true
        return order
    }

    @NotNull
    private fun setReady(order: Order): Order {
        order.orderState!!.ready = true
        return order
    }

    @NotNull
    private fun setDelivered(order: Order): Order {
        order.orderState!!.delivered = true
        return order
    }

    @NotNull
    private fun setPickedUp(order: Order): Order {
        order.orderState!!.pickedUp = true
        return order
    }

    @NotNull
    private fun setReceived(order: Order): Order {
        order.orderState!!.received = true
        return order
    }

    @NotNull
    private fun setNewOrder(order: Order): Order {
        order.orderState!!.newOrder = false
        return order
    } /* @NotNull
    private Order setCanceled(Order order) {
        order.getOrderState().setCanceled(true);
        return order;
    }

    @NotNull
    private Order setArchived(Order order) {
        order.getOrderState().setArchived(true);
        return order;
    }

    @NotNull
    private Order setArchivedProblem(Order order) {
        order.getOrderState().setArchivedProblem(true);
        return order;
    }*/

    companion object {
        private val NewOrderQualifier = Function { order: Order -> order.orderState!!.newOrder }
        private val AcceptOrderQualifier = Function { order: Order ->
            order.orderState!!.isAccepted() &&
                    !order.orderState!!.isReady() &&
                    !order.orderState!!.isDelivered() &&
                    !order.orderState!!.isPickedUp() &&
                    !order.orderState!!.isReceived()
        }
        private val ReadyOrderQualifier = Function { order: Order ->
            order.orderState!!.isAccepted() &&
                    order.orderState!!.isReady() &&
                    !order.orderState!!.isDelivered() &&
                    !order.orderState!!.isPickedUp() &&
                    !order.orderState!!.isReceived()
        }
        private val ConfirmationOrderQualifier = Function { order: Order ->
            (order.orderState!!.isPickedUp() || order.orderState!!.isDelivered()) &&
                    order.orderState!!.isAccepted() &&
                    order.orderState!!.isReady()
        }
        private val PastOrderQualifier = Function { order: Order ->
            order.orderState!!.isRejected() ||
                    order.orderState!!.isReceived()
        }
        private val GetSteps = Supplier<List<Pair<OrderStep, Function<Order, Boolean>>>> {
            val rules: MutableList<Pair<OrderStep, Function<Order, Boolean>>> = ArrayList()
            rules.add(Pair(OrderStep.NEW_ORDER, NewOrderQualifier))
            rules.add(Pair(OrderStep.ACCEPT_ORDER, AcceptOrderQualifier))
            rules.add(Pair(OrderStep.READY_ORDER, ReadyOrderQualifier))
            rules.add(Pair(OrderStep.CONFIRMATION_ORDER, ConfirmationOrderQualifier))
            rules
        }
        private val GetStepQualifier = Function { orderStep: OrderStep? ->
            Function { steps: List<Pair<OrderStep, Function<Order, Boolean>>> ->
                steps.stream()
                    .filter { step: Pair<OrderStep, Function<Order, Boolean>> -> step.key.equals(orderStep) }
                    .findFirst()
                    .orElseThrow { OrderException("error.order.step.notFound") }
            }
        }
    }
}