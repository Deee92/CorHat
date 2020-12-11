package furhatos.app.corhat.flow

import furhatos.app.corhat.nlu.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures
import furhatos.nlu.LogisticMultiIntentClassifier
import furhatos.nlu.Response
import furhatos.util.*
import java.io.File

val Idle: State = state {
    include(DebugState)

    init {
        val logFile = File("logs/flowlogger.txt") // Under skill directory
        flowLogger.start(logFile) // Start the logger
        /// TODO: Worse?
        // LogisticMultiIntentClassifier.setAsDefault()
        furhat.param.noSpeechTimeout = 10000

        furhat.setVoice(Language.ENGLISH_US, Gender.MALE)
        // TODO: Better voice?
        // furhat.setVoice(Language.ENGLISH_GB, "Brian", Gender.MALE)
        send("LookAround")
    }

    onEntry {
        furhat.attendNobody()
    }

    onEvent("LookAround") {
        furhat.gesture(Gestures.Shake)
        if (users.count > 0) {
            furhat.attend(users.random)
            goto(Start)
        } else {
            furhat.gesture(Gestures.Smile)
        }
    }

    onUserEnter {
        furhat.attend(it)
        goto(Start)
    }

    // Note: There is no speech input in this state.
}

val Interaction: State = state {
    var silences = 0  // This is not per user, instead per state.

    include(DebugState)

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

    // Final fallback for non-recognized stuff
    onResponse {
        random(
                { furhat.say("Can you rephrase that?") },
                { furhat.say("Sorry, I didn't understand.") },
                { furhat.say {
                    +"I need to buy me a dictionary for that."
                    +Gestures.BigSmile
                   }
                }
        )
        reentry()
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
    // TODO: Should this inherit Question or Interaction?
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
        /// furhat.attendNobody()

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

val DebugState = partialState {
    onResponse("print user", "producer", "print juicer") {
        println(users.current.dump())
        furhat.say("user printed")
        reentry()
    }

    onResponse("current state", "currant state", "corrent state") {
        val s = "Current state: " + this.currentState.name
        println(s)
        furhat.say(s)
        reentry()
    }

    onButton("print user", key="p", id="print user") {
        println(users.current.dump())
        reentry()
    }

    onButton("clear user", key="c", id="clear user") {
        println(users.current.clear())
        reentry()
    }

    onButton("get state", key="s", id ="current state") {
        val s = "Current state: " + this.currentState.name
        println(s)
        furhat.say(s)
        reentry()
    }

    onButton("stop talking", key="z", id="stop talking") {
        furhat.say("shh", abort = true) // Aborts the current speech
        reentry()
    }

    onButton("Goto Idle", key="q", id="goto idle") {
        goto(Idle)
    }

    onButton("LookAround", id="LookAround") {
        send("LookAround")
    }

    onButton("Goto Start2", id="goto Start2") {
        goto(Start2)
    }

    onButton("Goto TestInit", id="goto TestInit") {
        goto(TestInit)
    }

    onButton("Goto ConfirmHealthStatus", id="goto ConfirmHealthStatus") {
        goto(ConfirmHealthStatus)
    }

    onButton("Goto HealthCheck", id="goto HealthCheck") {
        goto(HealthCheck)
    }

    onButton("Goto AskToBookTest", id="goto AskToBookTest") {
        goto(AskToBookTest)
    }

    onButton("Goto RandomTalk", id="goto RandomTalk") {
        goto(RandomTalk)
    }

}
