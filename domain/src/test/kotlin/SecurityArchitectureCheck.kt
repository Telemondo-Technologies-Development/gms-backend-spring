import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.lang.ArchCondition
import com.tngtech.archunit.lang.ArchRule
import com.tngtech.archunit.lang.ConditionEvents
import com.tngtech.archunit.lang.SimpleConditionEvent
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RestController

@AnalyzeClasses(packages = ["com.gms.backend.domain"]) // Use your root package
class SecurityArchitectureTest {

    @ArchTest
    val `controllers must have class level PreAuthorize annotation`: ArchRule = classes()
        .that().areAnnotatedWith(Service::class.java)
        .should(object : ArchCondition<JavaClass>("have @PreAuthorize(\"denyAll()\")"){
            override fun check(item: JavaClass, events: ConditionEvents?) {
                val annotation = item.getAnnotationOfType(PreAuthorize::class.java)
                if (!annotation.value.contains("denyAll()")) {
                    val message = "${item.name} is missing 'denyAll()' in its @PreAuthorize annotation"
                    events?.add(SimpleConditionEvent.violated(item, message))
                }
            }

        })

    @ArchTest
    val `controllers must have class level Tag annotation`: ArchRule = classes()
        .that().areAnnotatedWith(RestController::class.java)
        .or().areAnnotatedWith(Controller::class.java)
        .should().beAnnotatedWith(Tag::class.java)
        .because("We enforce swagger @Tag annotation across all controllers for readability.")
}