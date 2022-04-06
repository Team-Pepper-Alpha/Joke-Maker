/*
 * Copyright (C) 2018 Softbank Robotics Europe
 * See COPYING for the license
 */

package com.softbankrobotics.qisdktutorials.ui.tutorials.gettingstarted

import android.os.Bundle

import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.builder.SayBuilder
import com.softbankrobotics.qisdktutorials.R
import com.softbankrobotics.qisdktutorials.ui.conversation.ConversationBinder
import com.softbankrobotics.qisdktutorials.ui.tutorials.TutorialActivity
import kotlinx.android.synthetic.main.conversation_layout.*
import kotlin.random.Random

/**
 * The activity for the Hello human tutorial.
 */
class HelloHumanTutorialActivity : TutorialActivity(), RobotLifecycleCallbacks {
    private var conversationBinder: ConversationBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, this)
        super.onDestroy()
    }

    override val layoutId = R.layout.conversation_layout

    override fun onRobotFocusGained(qiContext: QiContext) {
        // Bind the conversational events to the view.
        val conversationStatus = qiContext.conversation.status(qiContext.robotContext)
        conversationBinder = conversation_view.bindConversationTo(conversationStatus)

        val jokes = arrayOf("What's the deal with bananas? I mean they got orange juice. You got apple juice. Where's the banana juice?",
                "You can't have everything. Where would you put it?",
                "A lot of people are afraid of heights. Not me, I'm afraid of widths.",
                "A conclusion is the place where you got tired or thinking.",
                "Cross country skiing is great if you live in a small country.",
                "Everyone has a photographic memory. Some just don't have film.")

        val randomIndex = Random.nextInt(0, jokes.size)

        // Create a new say action.
        val say = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText(jokes[randomIndex]) // Set the text to say.
                .build() // Build the say action.

        // Execute the action.
        say.run()

        // Use built-in emotion detection and access the camera to detect people's emotion

        // From the detected result to output the right reaction

    }

    override fun onRobotFocusLost() {
        conversationBinder?.unbind()
    }

    override fun onRobotFocusRefused(reason: String) {
        // Nothing here.
    }
}
