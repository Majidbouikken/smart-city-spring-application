package com.smartcity.springapplication.flashDeal

import com.smartcity.springapplication.category.Category
import lombok.AllArgsConstructor
import com.smartcity.springapplication.notification.service.NotificationService
import com.smartcity.springapplication.user.simpleUser.SimpleUserService
import com.smartcity.springapplication.store.StoreService
import com.smartcity.springapplication.utils.error_handler.FlashDealException
import com.smartcity.springapplication.utils.error_handler.DateException
import java.util.concurrent.Executors
import com.smartcity.springapplication.notification.NotificationType
import com.smartcity.springapplication.user.simpleUser.SimpleUser
import com.smartcity.springapplication.notification.Notification
import com.smartcity.springapplication.store.Store
import com.smartcity.springapplication.utils.DateUtil
import com.smartcity.springapplication.utils.Response
import com.smartcity.springapplication.utils.pair.Pair
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.stream.Collectors
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Service
@AllArgsConstructor
class FlashDealService @Autowired constructor(
    val flashDealRepository: FlashDealRepository,
    val notificationService: NotificationService,
    val simpleUserService: SimpleUserService,
    val storeService: StoreService,
) {
    private val flashDealMapper: FlashDealMapper? = null

    @Transactional
    fun createFlashDeal(flashDealCreationDTO: FlashDealCreationDTO): Response<String> {
        flashDealCreationDTO.id = null
        val maxFlash = 2L // todo: this Long is hardcoded, later the admin should be able to edit it,
        // and it needs to be attached to the Provider
        // todo: this Long is hardcoded, later the admin should be able to edit it,
        val periodicityFlash = PeriodicityFlash.WEEK
        Optional.of(flashDealCreationDTO)
            .map { flashDealCreationDto: FlashDealCreationDTO? -> flashDealMapper!!.toModel(flashDealCreationDto) }
            .filter { flash: FlashDeal? -> checkValidFlash(flash, periodicityFlash, maxFlash) }
            .map { flashDeal: FlashDeal? -> setCreatAt(flashDeal) }
            .map { entity: FlashDeal? -> flashDealRepository.save(entity!!) }
            .map { flashDeal: FlashDeal -> setFlash(flashDeal, periodicityFlash) }
            .map { flashDeal: FlashDeal -> prepareNotification(flashDeal) }
            .map { flashDeal: FlashDeal -> setFlashDealUser(flashDeal) }
            .orElseThrow { FlashDealException("you can not send more then $maxFlash") }
        return Response("created.")
    }

    fun getRecentFlashDealsStore(providerId: Long?): List<FlashDealDTO> {
        return flashDealRepository.findByStoreProviderId(providerId)!!.stream()
            .sorted(Comparator.comparing({ it!!.createAt }, Comparator.reverseOrder()))
            .limit(10) // todo: this Long is hardcoded, later the admin should be able to edit it
            .map<FlashDealDTO?>(Function { flashDeal: FlashDeal? -> flashDealMapper!!.toDTO(flashDeal) })
            .collect(Collectors.toList())
    }

    fun searchFlashDealsStore(providerId: Long, startDate: String?, endDate: String?): List<FlashDealDTO?> {
        if (!DateUtil.isValidDate(startDate) || !DateUtil.isValidDate(endDate)) {
            throw DateException("error.date.invalid")
        }
        val startOfDate = LocalDateTime.of(LocalDate.parse(startDate), LocalTime.MIDNIGHT)
        val endOfDate = LocalDateTime.of(LocalDate.parse(endDate), LocalTime.MAX)
        return flashDealRepository.findByStoreProviderIdAndCreateAtBetween(providerId, startOfDate, endOfDate)!!
            .stream()
            .map<FlashDealDTO?>(Function { flashDeal: FlashDeal? -> flashDealMapper!!.toDTO(flashDeal) })
            .collect(Collectors.toList())
    }

    private fun setCreatAt(flashDeal: FlashDeal?): FlashDeal {
        flashDeal!!.createAt = LocalDateTime.now()
        return flashDeal
    }

    private fun prepareNotification(flashDeal: FlashDeal): FlashDeal {
        val threadPool = Executors.newCachedThreadPool()
        flashDeal.store!!.defaultCategories
            .forEach(Consumer { category: Category -> threadPool.submit { sendNotification(category, flashDeal) } })
        threadPool.shutdown()
        return flashDeal
    }

    private fun sendNotification(category: Category, flashDeal: FlashDeal) {
        notificationService.sendNotification(
            Notification(
                flashDeal.title,
                flashDeal.content,
                NotificationType.FLASH,
                category.name,
                null
            )
        )
    }

    private fun setFlashDealUser(flashDeal: FlashDeal): FlashDeal {
        simpleUserService.findSimpleUserByInterestCenterAndAround(flashDeal.store!!)!!
            .forEach(Consumer { user: SimpleUser? -> simpleUserService.setFlashDeal(user!!, flashDeal) })
        return flashDeal
    }

    private fun checkValidFlash(flashDeal: FlashDeal?, periodicityFlash: PeriodicityFlash, maxFlash: Long): Boolean {
        initStoreFlash(flashDeal!!.store, periodicityFlash)
        val periodicityQualifier = GetPeriodicityQualifier
            .apply(periodicityFlash)
            .apply(GetPeriodicity.get()).value.get()
        val store = flashDeal.store
        val now = LocalDate.now()
        return now.isAfter(periodicityQualifier.key.minusDays(1)) && now.isBefore(periodicityQualifier.value.plusDays(1)) && store!!.transmittedFlash!! < maxFlash
    }

    private fun setFlash(flashDeal: FlashDeal, periodicityFlash: PeriodicityFlash): FlashDeal {
        var store = flashDeal.store
        val periodicityQualifier = GetPeriodicityQualifier
            .apply(periodicityFlash)
            .apply(GetPeriodicity.get()).value.get()
        if (store!!.lastFlashStart != periodicityQualifier.key) {
            store.transmittedFlash = 0L
            store = storeService.saveStore(store)
        }
        store.transmittedFlash = store.transmittedFlash!! + 1
        store.lastFlashStart = periodicityQualifier.key
        storeService.saveStore(store)
        return flashDeal
    }

    private fun initStoreFlash(store: Store?, periodicityFlash: PeriodicityFlash) {
        if (store!!.transmittedFlash == null) {
            store.transmittedFlash = 0L
            storeService.saveStore(store)
        }
        if (store.lastFlashStart == null || store.periodicityFlash == null) {
            val periodicityQualifier = GetPeriodicityQualifier
                .apply(periodicityFlash)
                .apply(GetPeriodicity.get()).value.get()
            store.lastFlashStart = periodicityQualifier.key
            store.periodicityFlash = periodicityFlash
            storeService.saveStore(store)
        }
    }

    fun searchFlashByPosition(lat: Double, lon: Double, date: String?): List<FlashDealDTO?> {
        if (!DateUtil.isValidDate(date)) {
            throw DateException("error.date.invalid")
        }
        val startOfDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.MIDNIGHT)
        val endOfDate = LocalDateTime.of(LocalDate.parse(date), LocalTime.MAX)
        return flashDealRepository.findAll().stream()
            .filter(Predicate { flashDeal: FlashDeal? -> isAround(lat, lon, flashDeal!!.store) })
            .filter(Predicate { flashDeal: FlashDeal? ->
                flashDeal!!.createAt!!.isAfter(startOfDate) && flashDeal.createAt!!.isBefore(endOfDate)
            })
            .map<FlashDealDTO?>(Function { flashDeal: FlashDeal? -> flashDealMapper!!.toDTO(flashDeal) })
            .collect(Collectors.toList())
    }

    // Pour trouver tous les utilisateurs autours d'un rayon a 12 kilometres
    private fun isAround(latitude: Double, longitude: Double, store: Store?): Boolean {
        val distance = 12.0 // 12 kilometres
        return distFrom(
            latitude,
            longitude,
            store!!.storeAddress!!.latitude,
            store.storeAddress!!.longitude
        ) < distance
    }

    private fun distFrom(lat1: Double, lng1: Double, lat2: Double?, lng2: Double?): Double {
        val earthRadius = 6371000.0 //meters
        val dLat = Math.toRadians(lat2!! - lat1)
        val dLng = Math.toRadians(lng2!! - lng1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2) * sin(dLng / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return earthRadius * c / 1000
    }

    companion object {
        private val WeekStartEnd = Supplier {
            val firstDayOfWeek = WeekFields.of(Locale.ENGLISH).firstDayOfWeek
            val lastDayOfWeek = DayOfWeek.of((firstDayOfWeek.value + 5) % DayOfWeek.values().size + 1)
            Pair(
                LocalDate.now().with(TemporalAdjusters.previousOrSame(firstDayOfWeek)),
                LocalDate.now().with(TemporalAdjusters.nextOrSame(lastDayOfWeek))
            )
        }
        private val MonthStartEnd = Supplier {
            val initial = LocalDate.now()
            val start = initial.withDayOfMonth(1)
            val end = initial.withDayOfMonth(initial.lengthOfMonth())
            Pair(
                start,
                end
            )
        }
        private val YearStartEnd = Supplier {
            val initial = LocalDate.now()
            val startYear = initial.withDayOfYear(1)
            val endYear = initial.withDayOfYear(initial.lengthOfYear())
            Pair(
                startYear,
                endYear
            )
        }
        private val GetPeriodicity = Supplier<List<Pair<PeriodicityFlash, Supplier<Pair<LocalDate, LocalDate>>>>> {
            val rules: MutableList<Pair<PeriodicityFlash, Supplier<Pair<LocalDate, LocalDate>>>> = ArrayList()
            rules.add(Pair(PeriodicityFlash.WEEK, WeekStartEnd))
            rules.add(Pair(PeriodicityFlash.MONTH, MonthStartEnd))
            rules.add(Pair(PeriodicityFlash.YEAR, YearStartEnd))
            rules
        }
        private val GetPeriodicityQualifier = Function { periodicityFlash: PeriodicityFlash ->
            Function { periodicityList: List<Pair<PeriodicityFlash, Supplier<Pair<LocalDate, LocalDate>>>> ->
                periodicityList.stream()
                    .filter { periodicity: Pair<PeriodicityFlash, Supplier<Pair<LocalDate, LocalDate>>> -> periodicity.key == periodicityFlash }
                    .findFirst()
                    .orElseThrow { FlashDealException("error.flash.periodicity.notFound") }
            }
        }
    }
}