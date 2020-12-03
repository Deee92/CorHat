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

val GetQuery = state(parent = Start) {
    onEntry {
        furhat.ask("Are you feeling unwell, or do you need general information about COVID-19?")
    }

    onResponse<GetGeneralInfoIntent>{
        furhat.say("COVID-19 or the novel Coronavirus is a contagious disease.")
        furhat.say("Possible symptoms include fever, dry cough, a loss in the sense of smell or taste, fatigue, or breathing difficulties.")
        furhat.say("The best way to avoid being infected by COVID-19 is to follow social-distancing norms and good hand-hygiene.")
        goto(EndInteraction)
    }

    onResponse<GiveSymptomsIntent>{
        furhat.say("Sorry to hear that!");
        goto(GetContactHistory)
    }
}

val GetContactHistory = state {
    onEntry {
        furhat.ask("Do you think you may have been in contact with someone at work, or in your social circle, who tested positive for COVID-19 within the last seven days?")
        goto(Idle)
    }

    onResponse<Yes>{
        furhat.say("You should consider getting a COVID-19 test.")
        goto(EndInteraction)
    }

    onResponse<No>{
        furhat.say("You should self-isolate and monitor your symptoms. You should consider getting a COVID-19 test if symptoms persist or worsen.")
        goto(EndInteraction)
    }
}


val EndInteraction = state {
    onEntry {
        furhat.say("Thanks for your time. Goodbye!")
        goto(Idle)
    }
}
