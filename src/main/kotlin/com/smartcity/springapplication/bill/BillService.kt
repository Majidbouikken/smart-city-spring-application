package com.smartcity.springapplication.bill

import com.smartcity.springapplication.order.OrderType
import com.smartcity.springapplication.policies.Policies
import com.smartcity.springapplication.policies.PoliciesService
import com.smartcity.springapplication.policies.SelfPickUpOptions
import com.smartcity.springapplication.policies.taxRange.TaxRange
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BillService @Autowired constructor(
    val policiesService: PoliciesService,
    val billRepository: BillRepository,
) {
    fun saveBill(bill: Bill): Bill = billRepository.save(bill)

    fun getTotalToPay(billTotalDTO: BillTotalDTO): BillTotalDTO {
        if (billTotalDTO.orderType!! == OrderType.DELIVERY)
            BillTotalDTO(total = billTotalDTO.total)
        else {
            val policies: Policies = policiesService.findPoliciesById(billTotalDTO.policyId!!)
            when (policies.selfPickUpOption) {
                SelfPickUpOptions.SELF_PICK_UP_TOTAL -> return BillTotalDTO(total = billTotalDTO.total)
                SelfPickUpOptions.SELF_PICK_UP_PART_PERCENTAGE -> return BillTotalDTO(
                    total = percentageCalculate(billTotalDTO.total!!, policies.tax!!)
                )
                SelfPickUpOptions.SELF_PICK_UP_PART_RANGE -> return BillTotalDTO(
                    total = rangeCalculate(billTotalDTO.total!!, policies.taxRanges)
                )
                SelfPickUpOptions.SELF_PICK_UP -> return BillTotalDTO(total = 0.0)
                else -> {}
            }
        }
        return BillTotalDTO(total = billTotalDTO.total)
    }

    private fun rangeCalculate(total: Double, ranges: Set<TaxRange>): Double {
        for (taxRange in ranges)
            if (total >= taxRange.startRange!! && total <= taxRange.endRange!!) return taxRange.fixAmount!!.toDouble()
        return total
    }

    private fun percentageCalculate(total: Double, percentage: Int): Double = total * percentage / 100
}