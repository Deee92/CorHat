package furhatos.app.corhat.flow

import furhatos.app.corhat.nlu.DescribeContactHistory
import furhatos.app.corhat.nlu.DescribeHealthIntent
import furhatos.flow.kotlin.NullSafeUserDataDelegate
import furhatos.records.User

val User.health by NullSafeUserDataDelegate { DescribeHealthIntent() }
val User.contact by NullSafeUserDataDelegate { DescribeContactHistory() }
var User.rolled by NullSafeUserDataDelegate { false }
