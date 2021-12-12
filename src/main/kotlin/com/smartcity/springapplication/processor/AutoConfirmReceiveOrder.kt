package com.smartcity.springapplication.processor

import com.smartcity.springapplication.order.Order
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.repeat.RepeatStatus
import java.lang.Exception
import java.util.function.Consumer

/*class AutoConfirmReceiveOrder(var orderService: OrderService) : Tasklet {
    @Throws(Exception::class)
    override fun execute(stepContribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        orderService.toConfirmedOrders()
            .forEach(Consumer { order: Order ->
                order.orderState!!.received = true
                println(order.id)
            })
        println("AutoConfirmReceiveOrder FINISHED")
        orderService.toSendUserNotification()
        println("SendUserNotification FINISHED")
        return RepeatStatus.FINISHED
    }
}*/