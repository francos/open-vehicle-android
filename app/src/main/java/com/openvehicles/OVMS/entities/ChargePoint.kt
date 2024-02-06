package com.openvehicles.OVMS.entities

/**
 * OCM charge point
 *
 */
class ChargePoint {

    @JvmField
    var id: String? = null

    @JvmField
    var operatorInfo: OperatorInfo

    @JvmField
    var usageType: UsageType
    @JvmField
    var usageCost: String? = null

    @JvmField
    var addressInfo: AddressInfo
    @JvmField
    var numberOfPoints: String? = null
    @JvmField
    var generalComments: String? = null

    @JvmField
    var statusType: StatusType
    @JvmField
    var dateLastStatusUpdate: String? = null

    @JvmField
    var connections: Array<Connection?>

    init {
        // create sub class members:
        operatorInfo = OperatorInfo()
        usageType = UsageType()
        addressInfo = AddressInfo()
        statusType = StatusType()
        connections = arrayOfNulls(0)
    }

    /*
     * Inner types
     */

    class OperatorInfo {
        @JvmField
        var title: String? = null
    }

    class UsageType {
        @JvmField
        var title: String? = null
    }

    class AddressInfo {
        @JvmField
        var title: String? = null
        @JvmField
        var addressLine1: String? = null
        @JvmField
        var latitude: String? = null
        @JvmField
        var longitude: String? = null
        @JvmField
        var accessComments: String? = null
        @JvmField
        var relatedUrl: String? = null
    }

    class StatusType {
        @JvmField
        var title: String? = null
    }

    class Connection {

        @JvmField
        var connectionType: ConnectionType? = null

        @JvmField
        var level: Level? = null

        class ConnectionType {
            @JvmField
            var id: String? = null
            @JvmField
            var title: String? = null
        }

        class Level {
            @JvmField
            var title: String? = null
        }
    }
}
