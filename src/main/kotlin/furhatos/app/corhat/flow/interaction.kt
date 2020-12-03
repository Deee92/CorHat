package furhatos.app.corhat.flow

import furhatos.app.corhat.nlu.GetGeneralInfoIntent
import furhatos.app.corhat.nlu.GiveSymptomsIntent
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val Start : State = state(Interaction) {

    onEntry {
        furhat.ask("Hi there, my name is CorHat. Do you have questions about COVID-19?")
    }

    onResponse<Yes>{
        furhat.say("You came to the right place!");
        goto(GetQuery)
    }

    onResponse<No> {
        furhat.say("Okay, have a nice day!")
        goto(Idle)
    }
}

// Placeholder answers :)
val GetQuery = state(parent = Start) {
    onEntry {
        furhat.ask("How can I help you today?")
    }

    onResponse<GetGeneralInfoIntent>{
        furhat.say("COVID-19 or the novel Coronavirus is a bad disease");
        goto(EndInteraction)
    }

    onResponse<GiveSymptomsIntent>{
        furhat.say("Sorry to hear that!");
        goto(EndInteraction)
    }
}

val EndInteraction = state {
    onEntry {
        furhat.say("Thanks for your time. Goodbye!")
        goto(Idle)
    }
}
