package qa.runner


//TODO: find better way to prevent recursive run
object RecursiveRun {

    /**
     * return false on first run, return true on subsequent runs
     */
    var isPerformed: Boolean = false
        get() = synchronized(this) {
            if (field == false) {
                field = true
                false
            } else {
                true
            }
        }
}