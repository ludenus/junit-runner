package qa.runner.validation

import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UIDValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
annotation class UID(
    val message: String = "invalid uuid",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)
