package qa.runner.asm

import org.apache.logging.log4j.kotlin.logger
import org.objectweb.asm.*
import java.io.File

class ClassAnalyzer(file: File) {

    val classReader: ClassReader

    init {
        file.inputStream().use {
            classReader = ClassReader(it)
            classReader.accept(MyClassVisitor(), 0)
        }
    }

    var hasJunitTestMethod: Boolean = false
        private set


    private inner class MyMethodAnnotationVisitor(val methodName: String?) : MethodVisitor(Opcodes.ASM5) {
        private val log = logger()
        override fun visitAnnotation(desc: String, visible: Boolean): AnnotationVisitor? {
            if (desc == "Lorg/junit/jupiter/api/Test;") {
                log.info { "methodName:$methodName has Test annotation" }
                hasJunitTestMethod = true
            }
            return null
        }
    }

    private inner class MyClassVisitor : ClassVisitor(Opcodes.ASM5) {

        override fun visitMethod(
            access: Int,
            name: String?,
            descriptor: String?,
            signature: String?,
            exceptions: Array<out String>?,
        ): MethodVisitor? {
            return MyMethodAnnotationVisitor(name)
        }

    }


}