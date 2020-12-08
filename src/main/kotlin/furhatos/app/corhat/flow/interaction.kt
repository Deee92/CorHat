package furhatos.app.corhat.flow

import furhatos.app.corhat.nlu.*
import furhatos.nlu.common.*
import furhatos.flow.kotlin.*
import furhatos.gestures.Gestures

val Questions: State = state(parent = Interaction) {
    var nomatch = 0
    onResponse<AskTestTypeIntent> {
        if (it.intent.test?.value == "antibody") {
            furhat.say("An ${it.intent.test} test can determine if you have had COVID-19 before.")
            reentry()
        } else {
            furhat.say("A ${it.intent.test} test can determine if you are currently infected with COVID-19.")
            reentry()
        }
    }
    onResponse<AskCovid19> {
        furhat.say("COVID-19, or the novel Coronavirus, is a contagious disease.")
        reentry()
    }
    onResponse<AskSymptoms> {
        furhat.say("Possible symptoms for COVID-19 include " + Symptom().optionsToText())
        reentry()
    }
    onResponse<AskSafetyMeasure> {
        furhat.say("The best way to avoid being infected with COVID-19, is to follow social-distancing norms, and good hand-hygiene.")
        reentry()
    }
    onResponse<BookTest> {
        goto(TestInit)
    }
    onResponse {
        nomatch++
        // TODO: Make shorter
        furhat.ask("Can you say that again, please?")
        /*
        if (nomatch > 1)
            furhat.say("I'm afraid I don't know that, for more information about covid please visit www.1177.se")
        else
            furhat.say("sorry, I dont have information on that question, you are welcome with other questions")
         */
        reentry()
    }
}

val Start: State = state(parent = Questions) {
    onEntry {
        furhat.say {
            +"Hi there, my name is CorHat."
            +Gestures.BigSmile
        }
        goto(Start2)
    }
}

val Start2: State = state(parent = Questions) {
    onEntry {
        // TODO: Random not working
        random({ furhat.ask("How may I help you?") },
                { furhat.ask("What can I do for you?") }
        )
    }

    onReentry {
        random({ furhat.ask("what else can I do for you?") },
                { furhat.ask("Do you have more questions?") }
        )
    }

    /* TODO: add one response to exit from here*/

    onResponse<RequestServiceOptions> {
        furhat.say("I can help you book a test for COVID-19. I can also share general information about COVID-19.")
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

val ChooseService: State = state(parent = SubInteraction) {
    onEntry { furhat.listen() }
    onResponse<BookTest> {
        goto(TestInit)
    }
    onResponse<GetInfo> {
        goto(GetInformation)
    }
}

val GetInformation: State = state(parent = Questions) {
    onEntry {
        random(furhat.ask("What do you want to know about COVID-19?"),
                furhat.ask("Is there anything you would like to know about COVID-19?"))
    }

    onReentry { furhat.ask("Do you have more questions?") }

    onResponse<Yes> {
        furhat.ask("What questions do you have? ")
    }

    onResponse<No> {
        goto(EndInteraction)
    }
}

val TestInit: State = state(parent = SubInteraction) {
    onEntry {
        random(furhat.ask("Let's start with your current health condition. Can you please describe your symptoms?"),
                furhat.ask("I see, you want to take a test. How do you feel currently?"))
    }

    onResponse<DescribeHealthIntent> {
        users.current.health.adjoin(it.intent)
        goto(HealthCheck)
    }
}

val HealthCheck: State = state(parent = SubInteraction) {
    onEntry {
        val health = users.current.health
        val contact = users.current.contact
        when {
            health.symptoms == null -> goto(RequestSymptom)
            health.duration == null -> goto(RequestDuration)
            contact.person == null -> goto(RequestContact)
            else -> {
                if (contact.person?.value == null) {
                    furhat.say("Alright, so you have $health, and you have not had any contact with a COVID-19 patient.")
                } else {
                    furhat.say("Alright, so you have $health, and you've been in contact with $contact, who has COVID-19")
                }
                goto(ConfirmHealthStatus)
            }
        }
    }
}

val RequestContact: State = state(parent = SubInteraction) {
    onEntry { furhat.ask("Have you had any contact with a person infected with COVID-19?") }
    onReentry { furhat.ask("I see, can you please tell me more about your contact history?") }
    onResponse<DescribeContactHistory> {
        furhat.say("I see, so you have had contact with ${it.intent.person}, who has COVID-19")
        users.current.contact.person = it.intent.person
        goto(HealthCheck)
    }
    onResponse<Yes> {
        reentry()
    }
    onResponse<No> {
        furhat.say("That's great, so you have not had any contact with a COVID-19 patient.")
        users.current.contact.person = Person("no one")
        goto(HealthCheck)
    }
}

val RequestSymptom: State = state(parent = SubInteraction) {
    onEntry {
        furhat.ask("How do you feel right now? What are your symptoms?")
    }

    onReentry { furhat.ask("Do you have more to add?") }

    onResponse<DescribeSymptomIntent> {
        furhat.say("I see, ${it.intent.symptoms}")
        users.current.health.symptoms = it.intent.symptoms
        goto(HealthCheck)
    }
}

val RequestDuration: State = state(parent = SubInteraction) {
    onEntry {
        furhat.ask("Okay, can you tell me how long you've been experiencing these symptoms?")
    }

    onResponse<DescribeDurationIntent> {
        furhat.say("I see, it has been about ${it.intent.duration}")
        users.current.health.duration = it.intent.duration
        goto(HealthCheck)
    }
}

// TODO: Separate confirm from recommend.
// TODO: Need to continue from here.
val ConfirmHealthStatus: State = state(parent = SubInteraction) {
    onEntry {
        if (users.current.contact.person?.value != null) {
            if (users.current.health.duration?.timeunit?.value == "week" && users.current.health.duration?.count?.value!! >= 2) {
                furhat.say("I can see that you have contact history and you've been experiencing symptoms over 2 weeks, in this case I'd recommend you to do an antibody test.")
                goto(AskToBookTest)
            } else {
                furhat.say("You have had contact with a COVID-19 patient, and have had symptoms for less than 2 weeks. I'd recommend you to take a PCR test.")
                goto(AskToBookTest)
            }
        } else {
            if (users.current.health.duration?.timeunit?.value == "day" && users.current.health.duration?.count?.value!! <= 2) {
                furhat.say("Since you have no previous contact history, and you have had symptoms for about 2 days, I'd recommend you to stay at home. If your symptoms persist after 24 hours, you are welcome back to book a test!")
            } else if (users.current.health.duration?.timeunit?.value == "week" && users.current.health.duration?.count?.value!! >= 2) {
                // TODO: Can't test for antibody while sick!
                furhat.say("Though you were not in contact with a COVID-19 patient, you've been experiencing symptoms for over 2 weeks. I'd recommend you to take an antibody test.")
                goto(AskToBookTest)
            } else {
                furhat.say("You have had symptoms for a while now. I'd recommend you to take a PCR test.")
                goto(AskToBookTest)
            }
            furhat.ask("Do you have anything to add or change?")
        }
    }
    onResponse<Yes> {
        furhat.say("let me do it next time")
        goto(Idle)
    }
    onResponse<No> {
        goto(EndInteraction)
    }
}


val AskToBookTest: State = state(parent = Interaction) {
    onEntry {
        furhat.ask("Can I help you book your test?")
    }

    onResponse<Yes> {
        furhat.say("Great, I would love to help you book a test.")
        goto(GetCityLocation)
    }

    onResponse<No> {
        goto(EndInteraction)
    }
}
// TODO: Untested
var c: City? = null
val GetCityLocation: State = state(parent = Interaction) {
    onEntry {
        furhat.ask("You can get tested in " + City().optionsToText() + ". Which would you prefer?")
    }

    onResponse<Location> {
        c = it.intent.city
        furhat.ask("${c?.text}?")
        reentry()
    }
    onResponse<Yes> {
        furhat.say("Alright!")
        goto(GetAvailability)
    }
    onResponse<No> {
        c = null
        furhat.ask("Sorry, where then?")
        reentry()
    }
}

// start - testing facilities and directions ////////////
var a: Centers? = null
var b: Day? = null
val GetAvailability: State = state(parent = Interaction) {
    onEntry {
        furhat.ask("When are you available to take the test?")
    }

    onResponse<Availability> {
        b = it.intent.day
        goto(ChooseCenter)
    }
}
val ChooseCenter: State = state(parent = Interaction) {
    onEntry {
        furhat.say("${b}, that is great !")
        furhat.say("You are welcome to take a test in ${Available_centers(b?.text, c?.text).optionsToText()} on ${b}")
        // TODO: Wasn't center covered?
        furhat.ask("Now, please let me know which center you prefer to take your test.")
    }
    onResponse<Show_direction> {
        a = it.intent.center
        goto(give_address)
    }
}
val give_address: State = state(parent = Interaction) {
    onEntry {
        furhat.say("Good choice!")
        furhat.ask("Do you want me to help with the directions to ${a} ?")
    }
    onResponse<Yes> {
        furhat.say("Alright! To get to ${a}, you can follow ${Center_Direction(a?.text).optionsToText()}")
        goto(EndInteraction)
    }
    onResponse<No> {
        furhat.say("Great! Good luck with your test!")
        goto(EndInteraction)
    }
}
// end - testing facilities and directions ////////////

val EndInteraction = state {
    onEntry {
        furhat.say("Thanks for your time. Goodbye!")
        goto(Idle)
    }
}