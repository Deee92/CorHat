package furhatos.app.corhat.flow

import furhatos.app.corhat.nlu.DescribeContactHistory
import furhatos.app.corhat.nlu.DescribeHealthIntent
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.Location
import furhatos.records.User

var User.health by NullSafeUserDataDelegate { DescribeHealthIntent() }
var User.contact by NullSafeUserDataDelegate { DescribeContactHistory() }
var User.rolled by NullSafeUserDataDelegate { false }

fun User.distance() : Double {
    return head.location.distance(Location.ORIGIN)
}

fun User.dump(): String {
    return "Id: $id Health: $health Contact: $contact Rolled: $rolled"
}

fun User.clear(): String {
    health = DescribeHealthIntent()
    contact = DescribeContactHistory()
    rolled = false
    return "Id: $id Health: $health Contact: $contact Rolled: $rolled"
}