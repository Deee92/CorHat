package furhatos.app.corhat.flow

import furhatos.flow.kotlin.*
import furhatos.util.*
import java.io.File

val Idle: State = state {

    init {
        furhat.setVoice(Language.ENGLISH_US, Gender.MALE)
//        furhat.setVoice(Language.ENGLISH_GB, "Brian", Gender.MALE)
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        }
    }

    onEntry {
        furhat.attendNobody()
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }
}

val Interaction: State = state {
    var silences = 0
    onUserLeave(instant = true) {
        println("Interaction.Leave Users:" + users.count)
        if (users.count > 0) {
            if (it == users.current) {
                furhat.attend(users.other)
                goto(Start)
            } else {
                furhat.glance(it)
            }
        } else {
            goto(Idle)
        }
    }

    onUserEnter(instant = true) {
        furhat.glance(it)
    }

    onNoResponse {
        silences++
        when (silences)  {
            1 -> furhat.ask("I didn't hear anything")
            2 -> furhat.ask("I still didn't hear you. Could you speak up please?")
            else -> {
                // TODO: Why nagging?
                furhat.say("Still didn't hear anything")
                reentry()
            }
        }
    }
}