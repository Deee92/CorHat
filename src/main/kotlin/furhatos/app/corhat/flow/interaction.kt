package furhatos.app.corhat.flow
import furhatos.nlu.*
import furhatos.app.corhat.nlu.*
import furhatos.app.corhat.nlu.GetGeneralInfoIntent
import furhatos.app.corhat.nlu.GiveSymptomsIntent
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val Start : State = state(Interaction) {

    onEntry {
        furhat.ask("Hi there, my name is CorHat. Do you have questions about COVID-19?")
    }

    onResponse<Yes>{
        furhat.say("You came to the right place!")
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
        furhat.say("Sorry to hear that!")
        goto(GetContactHistory)
    }
}

val GetContactHistory = state {
    onEntry {
        furhat.ask("Do you think you may have been in contact with someone at work, or in your social circle, who tested positive for COVID-19 within the last seven days?")
    }

    onResponse<Yes>{
        furhat.say("If you feel unwell, and have recently been in contact with someone who tested positive, you should consider getting a COVID-19 test.")
        goto(GetTestInterest)
    }

    onResponse<No>{
        furhat.say("If you feel unwell, but have not been in contact with someone who tested positive, you should self-isolate and monitor your symptoms.")
        furhat.say("Please consider getting a COVID-19 test if your symptoms persist or worsen.")
        goto(EndInteraction)
    }
}

val GetTestInterest = state {
    onEntry {
        furhat.ask("Would you like information on COVID-19 testing?")
    }

    onResponse<Yes> {
        goto(GetAvailability)
    }

    onResponse<No> {
        goto(EndInteraction)
    }
}


val EndInteraction = state {
    onEntry {
        furhat.say("Thanks for your time. Goodbye!")
        goto(Idle)
    }
}

//Javad- start - testing facilities and directions////////////////////////////////////////////////////////////////////////////////////////////
var a : Centers? = null
var b : Day? = null
val GetAvailability : State = state(Interaction) {
    onEntry {
        furhat.ask("Alright, when are you available for doing the test?")
    }

    onResponse<Availability> {
        b =  it.intent.day
        goto(ChooseCenter)
    }
}
val ChooseCenter : State = state(Interaction) {
    onEntry {
        furhat.say("${b}, that is great !")
        furhat.say("You are welcomed to do your test in ${Available_centers(b?.text).optionsToText()} on ${b}")
        furhat.ask("Now, Please let me know which center you prefer to do your test.")
    }
    onResponse<Show_direction> {
        a = it.intent.center
        goto(give_address)
    }

}
val give_address : State = state(Interaction) {
    onEntry {
        furhat.say("Good choice!")
        furhat.ask("Do you want me to help with the directions to ${a} ?")
    }
    onResponse<Yes> {
        furhat.say("alright!, to get to the ${a}, you can follow ${Center_Direction(a?.text).optionsToText()}")
        goto(EndInteraction)
    }
    onResponse<No> {
        furhat.say("Great! Then good luck with your test!")
        goto(EndInteraction)
    }

}
//Javad - end - testing facilities and directions////////////////////////////////////////////////////////////////////////////////////////////