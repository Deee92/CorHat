package furhatos.app.corhat

import furhatos.app.corhat.flow.*
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class CorhatSkill : Skill() {
    override fun start() {
        Flow().run(Idle)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}
