package furhatos.app.corhat.flow

import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.Response
import furhatos.util.*
import java.io.File

val Idle: State = state {

    init {
        val logFile = File("logs/flowlogger.txt") // Under skill directory
        flowLogger.start(logFile) // Start the logger

        furhat.param.noSpeechTimeout = 10000

        furhat.setVoice(Language.ENGLISH_US, Gender.MALE)
        // furhat.setVoice(Language.ENGLISH_GB, "Brian", Gender.MALE)
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
    var silences = 0  // This is not per user, instead per state.

    onUserLeave(instant = true) {
        println("Interaction.Leave Remaining: " + users.count)
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
        println("Interaction Silence: $silences")
        when (silences) {
            1 -> furhat.ask("I didn't hear anything. Can you say that again?")
            2 -> furhat.ask("I still didn't hear you. Could you speak up please?")
            else -> {
                val res = call(PleaseHold) as Response<*>
                // TODO: Consider what prints to remove
                println("PleaseHold terminated")
                silences = 0
                raise(res)
            }
        }
    }
}

/** Dummy state for inheritance */
val SubInteraction: State = state(parent = Interaction) {
    // TODO: Should this inherit Question?
}

/** Note that this state is meant to be accessed through call().
 * The calling state will automatically reactivate if there are triggers incoming
 * that aren't caught here.
  */
val PleaseHold: State = state {
    val holdTheTime = 30000

    onEntry {
        furhat.ask("Please check your microphone and let me know when you're back!", timeout = holdTheTime)
        /// TODO: Should unset attention if that allows to still listen
    }

    onReentry {
        if (!users.current.rolled) {
            users.current.rolled = true
            val staccato = "2ms"
            val rickroll = """Never gonna ${furhat.voice.emphasis("give")}${furhat.voice.pause(staccato)} 
                you${furhat.voice.pause(staccato)} up! 
                Never gonna ${furhat.voice.emphasis("let")}${furhat.voice.pause(staccato)} 
                you${furhat.voice.pause(staccato)} down!"""
            furhat.say(furhat.voice.prosody(rickroll, rate = 1.20, pitch = "high"))
        }
        random (
                { furhat.say("Oh, sorry, forgot you were here!") },
                { furhat.say("Anybody there?") },
                { furhat.gesture(Gestures.Shake) }
        )
        furhat.listen(timeout = holdTheTime)
    }

    // Overriding to stay on hold
    onNoResponse {
        reentry()
    }

    // Overriding to terminate call
    onResponse {
        terminate(it)
    }
}
