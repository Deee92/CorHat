package furhatos.app.corhat.flow

import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val Start : State = state(Interaction) {

    onEntry {
        furhat.ask("Hi there. Do you have questions about COVID-19?")
    }

    onResponse<Yes>{
        furhat.say("You came to the right place! How can I help you today?")
    }

    onResponse<No>{
        furhat.say("Okay, have a nice day!")
    }
}
