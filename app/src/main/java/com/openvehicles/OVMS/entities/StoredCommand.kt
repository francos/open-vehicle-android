package com.openvehicles.OVMS.entities

class StoredCommand(

    @JvmField var mKey: Long,
    @JvmField var mTitle: String,
    @JvmField var mCommand: String

) : Comparable<StoredCommand> {

    constructor(pTitle: String, pCommand: String) : this(0, pTitle, pCommand)

    override fun compareTo(other: StoredCommand): Int {
        return mTitle.compareTo(other.mTitle, ignoreCase = true)
    }

    override fun equals(other: Any?): Boolean {
        return if (other is StoredCommand) {
            other.mKey == mKey
        } else {
            false
        }
    }
}
