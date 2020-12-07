package furhatos.app.corhat.flow
import furhatos.nlu.*
import furhatos.app.corhat.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*

val Questions: State = state(Interaction){
    var nomatch = 0
    onResponse<AskTestTypeIntent> {
        if (it.intent.test?.value == "antibody") {
            furhat.say("a ${it.intent.test} test is to see if you have had covid-19 before by checking the antibody")
            reentry()
        }
        else {
            furhat.say ("a ${it.intent.test} test is to check if you current is infected by covid-19")
            reentry()
        }
    }
    onResponse<AskCovid19> {
        furhat.say("COVID-19 or the novel Coronavirus is a contagious disease.")
        reentry()
    }
    onResponse<AskSymptoms> {
        furhat.say("Possible symptoms for covid 19 includes" + Symptom().optionsToText())
        reentry()
    }
    onResponse<AskSafetyMeasure> {
        furhat.say("The best way to avoid being infected by COVID-19 is to follow social-distancing norms and good hand-hygiene.")
        reentry()
    }
    onResponse<BookTest> {
        goto(TestInit)
    }
    onResponse {
        nomatch++
        if (nomatch > 1)
            furhat.say("Sorry, I'm afraid I dont have any answer to that, for more information about covid please have a look at www.1177.se")
        else
            furhat.say("sorry, I dont have information on that question, you are welcome with other questions")
        reentry()
    }
}

val Start : State = state(parent = Questions) {
    onEntry {
        furhat.ask("Welcome to Corhat, How may I help you")
    }
    onReentry {
        random(furhat.ask ("what else can I do for you?"),
                furhat.ask ("do you have more questions?")
        )

    }

    /* TO DO: add one response to exit from here*/

    onResponse<RequestServiceOptions> {
        furhat.say ("I can help you with test booking for covid-19 and I can also provide you with general information of covid")
        goto(ChooseService)
    }

    onResponse<DescribeHealthIntent> {
        users.current.health.adjoin(it.intent)
        goto(HealthCheck)
    }
    onPartialResponse<DescribeHealthIntent> {
        users.current.health.adjoin(it.intent)
        raise(it, it.secondaryIntent)
    }

    onResponse<DescribeContactHistory> {
        users.current.contact.adjoin(it.intent)
        goto(HealthCheck)
    }
    onPartialResponse<DescribeContactHistory> {
        users.current.contact.adjoin(it.intent)
        raise(it, it.secondaryIntent)
    }
}

val ChooseService : State = state {
    onEntry { furhat.listen() }
    onResponse<BookTest> {
        goto(TestInit)
    }
    onResponse<GetInfo> {
        goto(GetInformation)
    }
}

val GetInformation : State = state(parent = Questions) {
    onEntry {
        random(furhat.ask ( "what do you wanna know about covid?" ),
                furhat.ask ( "anything you'd like to know about covid 19?" ))
    }
    onReentry { furhat.ask ( "do you have some more questions?" ) }

    onResponse<Yes> { furhat.ask ( "what questions do you have? " )
    }
    onResponse<No>  { furhat.say("thanks for your time, have a nice day. ")
    }
}

val TestInit : State = state() {
    onEntry {
        random(furhat.ask ( "To start with, can you describe for me your current health condition?" ),
                furhat.ask ( "I see, you want a test, how do you feel currently?" ))
    }

    onResponse<DescribeHealthIntent> {
        users.current.health.adjoin(it.intent)
        goto(HealthCheck)
    }

}

val HealthCheck : State = state {
    onEntry {
        val health = users.current.health
        val contact = users.current.contact
        when {
            health.symptoms == null -> goto(RequestSymptom)
            health.duration == null -> goto(RequestDuration)
            contact.person == null -> goto(RequestContact)
            else -> {
                if (contact.person?.value == null){
                    furhat.say("Alright, so you have $health and you don't have any contact with covid 19 patient")
                }
                else {
                    furhat.say("Alright, so you have $health and you've been in touch with $contact who has covid 19")
                }
                goto(ConfirmHealthStatus)
            }
        }
    }
}
val RequestContact : State = state {
    onEntry { furhat.ask("Have you had any contact with person who get infected by covid-19") }
    onReentry { furhat.ask("I see, please tell me more about your contact history?") }
    onResponse<DescribeContactHistory> {
        furhat.say("I see, so you have had contact with ${it.intent.person} who has covid 19")
        users.current.contact.person = it.intent.person
        goto(HealthCheck)
    }
    onResponse<Yes> {
        reentry()
    }
    onResponse<No> {
        furhat.say("that's great, so you don't have any contact with covid 19 patient.")
        users.current.contact.person = Person("no one")
        goto(HealthCheck)
    }
}


val RequestSymptom : State = state {
    onEntry {
        furhat.ask("How do you feel right now, what is your symptoms?")
    }

    onReentry { furhat.ask("Do you have more to add?") }

    onResponse<DescribeSymptomIntent> {
        furhat.say("I see, ${it.intent.symptoms}")
        users.current.health.symptoms = it.intent.symptoms
        goto(HealthCheck)
    }
}

val RequestDuration :State = state {
    onEntry {
        furhat.ask("Ok, can you tell me for how long you've been experiencing the symptoms?")
    }

    onResponse<DescribeDurationIntent> {
        furhat.say("I see, it has been about ${it.intent.duration}")
        users.current.health.duration = it.intent.duration
        goto(HealthCheck)
    }
}

val ConfirmHealthStatus : State = state {
    onEntry{
        if (users.current.contact.person?.value != null){
            if (users.current.health.duration?.timeunit?.value == "week" && users.current.health.duration?.count?.value!! >= 2 ) {
                furhat.say("I can see that you have contact history and you've been experiencing symptoms over 2 weeks, in this case I'd recommend you to do an antibody test test")
            }
            else{
                furhat.say("You have previous contact with covid patient and your symptoms has been less than 2 weeks, I'd recommend you with a PCR test")
            }
        }
        else{
            if (users.current.health.duration?.timeunit?.value == "day" && users.current.health.duration?.count?.value!! <= 2) {
                furhat.say( "since you have no previous contact history and your symptoms has only been less than 2 days, I'd recommend you to stay at home and if your symptoms persist after 24 hours you are welcome back to book a test")
            }
            else if (users.current.health.duration?.timeunit?.value == "week" && users.current.health.duration?.count?.value!! >= 2 ) {
                furhat.say("Even though you didn't have any contact with covid patient, but you've been experiencing symptoms over 2 weeks, in this case I'd recommend you to do an antibody test test")
            }
            else{
                furhat.say( "your symptoms has been over a period of time, I'd recommend you with a PCR test")
            }
            furhat.ask("Do you have anything to add or change?")
        }
    }
    onResponse<Yes> {
        furhat.say ( "let me do it next time" )
        goto(Idle)
    }
    onResponse<No> {
        furhat.say ( "thanks for using my service" )
        goto(Idle)
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