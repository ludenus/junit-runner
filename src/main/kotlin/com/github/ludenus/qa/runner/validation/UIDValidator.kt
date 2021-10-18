package com.github.ludenus.qa.runner.validation

import org.apache.logging.log4j.kotlin.logger
import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class UIDValidator : ConstraintValidator<UID?, String> {

    override fun isValid(uid: String, context: ConstraintValidatorContext): Boolean {
        try {
            UUID.fromString(uid)
        } catch (e: IllegalArgumentException) {
            logger().error("failed to parse uuid from: $uid")
            return false
        }
        return true
    }
}