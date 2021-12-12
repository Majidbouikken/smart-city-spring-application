package com.smartcity.springapplication.policies

import lombok.AllArgsConstructor
import com.smartcity.springapplication.policies.taxRange.TaxRangeRepository
import com.smartcity.springapplication.utils.error_handler.PoliticsException
import com.smartcity.springapplication.policies.taxRange.TaxRange
import com.smartcity.springapplication.utils.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors
import javax.transaction.Transactional
import kotlin.collections.HashSet

@Service
@AllArgsConstructor
class PoliciesService @Autowired constructor(
    val policiesRepository: PoliciesRepository,
    val taxRangeRepository: TaxRangeRepository,
) {
    private val policiesMapper: PoliciesMapper? = null

    fun findPoliciesById(policyId: Long): Policies = policiesRepository.findById(policyId)
        .orElseThrow { PoliticsException("error.politics.notFound") }!!

    fun getPolitics(providerId: Long): PoliciesDTO = policiesRepository.findByStoreProviderId(providerId)!!
        .map<PoliciesDTO?>(Function { policies: Policies? -> policiesMapper!!.toDTO(policies) })
        .orElseThrow { PoliticsException("error.politics.notFound") }

    fun getStorePolitics(storeId: Long): PoliciesDTO = policiesRepository.findByStoreId(storeId)!!
        .map<PoliciesDTO?>(Function { policies: Policies? -> policiesMapper!!.toDTO(policies) })
        .orElseThrow { PoliticsException("error.politics.notFound") }

    @Transactional
    fun createPolitics(policiesDTO: PoliciesDTO): Response<String> {
        Optional.of(policiesDTO)
            .map { policiesDTO: PoliciesDTO -> checkPolitics(policiesDTO) }
            .map { policies: Policies? -> updateTaxRange(policies) }
            .map { policies: Policies? -> policiesRepository.save(policies!!) }
            .map { policies: Policies -> setTaxRange(policies) }
        return Response("created.")
    }

    @Transactional
    fun updateTaxRange(policies: Policies?): Policies? {
        if (policies!!.store!!.policies != null) {
            val collect = policies.store!!.policies?.taxRanges?.stream()!!
                .peek { entity: TaxRange -> taxRangeRepository.delete(entity) }
                .collect(Collectors.toList())
            policies.id = policies.store.policies?.id
        }
        return policies
    }

    fun setTaxRange(policies: Policies): Policies {
        val collect = policies.taxRanges.stream()
            .peek { taxRange: TaxRange -> taxRange.policies = policies }
            .map { entity: TaxRange -> taxRangeRepository.save(entity) }
            .collect(Collectors.toSet())
        policies.taxRanges = collect
        return policies
    }

    private fun checkPolitics(policiesDTO: PoliciesDTO): Policies? = Optional.of(policiesDTO)
        .map { policiesDTO: PoliciesDTO -> policiesMapper!!.toModel(policiesDTO) }
        .map { policies: Policies? -> politicsDispatcher(policies) }
        .orElseThrow { PoliticsException("error.politics.checkPolitics") }

    private fun politicsDispatcher(policies: Policies?): Policies? {
        when (policies!!.selfPickUpOption) {
            SelfPickUpOptions.SELF_PICK_UP -> return checkSelfPickUp(policies)
            SelfPickUpOptions.SELF_PICK_UP_PART_RANGE -> return checkSelfPickUpPartRange(policies)
            SelfPickUpOptions.SELF_PICK_UP_PART_PERCENTAGE -> return checkSelfPickUpPartPercentage(policies)
            SelfPickUpOptions.SELF_PICK_UP_TOTAL -> return checkSelfPickUpTotal(policies)
        }
        return null
    }

    private fun checkSelfPickUp(policies: Policies?): Policies {
        policies!!.selfPickUpOption = SelfPickUpOptions.SELF_PICK_UP
        policies.tax = 0
        policies.taxRanges = HashSet()
        return policies
    }

    private fun checkSelfPickUpPartRange(policies: Policies?): Policies {
        policies!!.selfPickUpOption = SelfPickUpOptions.SELF_PICK_UP_PART_RANGE
        policies.tax = 0
        return policies
    }

    private fun checkSelfPickUpPartPercentage(policies: Policies?): Policies {
        policies!!.selfPickUpOption = SelfPickUpOptions.SELF_PICK_UP_PART_PERCENTAGE
        policies.taxRanges = HashSet()
        return policies
    }

    private fun checkSelfPickUpTotal(policies: Policies?): Policies {
        policies!!.selfPickUpOption = SelfPickUpOptions.SELF_PICK_UP_TOTAL
        policies.tax = 0
        policies.taxRanges = HashSet()
        return policies
    }
}