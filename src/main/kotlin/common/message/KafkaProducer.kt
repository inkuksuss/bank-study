package org.example.common.message

import org.example.common.exception.CustomException
import org.example.common.exception.ErrorCode
import org.example.common.logging.Logging
import org.slf4j.Logger
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class KafkaProducer(
    private val template: KafkaTemplate<String, Any>,
    private val log: Logger = Logging.getLogger(KafkaProducer::class.java)
) {
    fun sendMessage(topic: String, message: Any) {
        template.send(topic, message)
            .whenComplete{ result, ex ->
                if (ex == null) {
                    log.info("message send success - topic: ${topic} - time: ${LocalDateTime.now()}")
                }
                else {
                    throw CustomException(ErrorCode.FAILED_TO_SEND_MESSAGE)
                    log.error("message send failed - ${ex.message}")
                }
        }
    }
}