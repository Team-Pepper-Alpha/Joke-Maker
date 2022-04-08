/*
 * Copyright (C) 2018 Softbank Robotics Europe
 * See COPYING for the license
 */

package com.softbankrobotics.qisdktutorials.ui.tutorials.gettingstarted

import android.os.Bundle
import android.util.Log
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
class HelloHumanTutorialActivity : TutorialActivity(), RobotLifecycleCallbacks, OnBasicEmotionChangedListener {
    // Store the basic emotion observer.
    private var basicEmotionObserver: BasicEmotionObserver? = null

    private var conversationBinder: ConversationBinder? = null

    private val TAG = "MyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create the basic emotion observer and listen to it.
        basicEmotionObserver = BasicEmotionObserver()
        basicEmotionObserver?.listener = this

        // Register the RobotLifecycleCallbacks to this Activity.
        QiSDK.register(this, this)
    }

    override fun onDestroy() {
        // Stop listening to basic emotion observer and remove it.
        basicEmotionObserver?.listener = null
        basicEmotionObserver = null

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

        // This will give user sometime to laugh
        Log.i(TAG, "Sleep start")
        Thread.sleep(2000)
        Log.i(TAG, "Sleep complete")

        // Use built-in emotion detection and access the camera to detect people's emotion
        // Start the basic emotion observation.
        basicEmotionObserver?.startObserving(qiContext)

        Log.i(TAG, "start observing")

        var reaction = ""

        // If it detects JOYFUL, then return a positive comment
        if (basicEmotionObserver?.notifyListener() == BasicEmotion.JOYFUL || basicEmotionObserver?.notifyListener() == BasicEmotion.CONTENT) {
            reaction = "Thanks! You have a good sense of humour. Haha!"
        } else if (basicEmotionObserver?.notifyListener() == BasicEmotion.NEUTRAL || basicEmotionObserver?.notifyListener() == BasicEmotion.SAD || basicEmotionObserver?.notifyListener() == BasicEmotion.ANGRY) {
            reaction = "Oh well, this one kills in the robotverse. He he?"
        }

        Log.i(TAG, "Reaction: " + reaction)

        // From the detected result to output the right comment
        val say2 = SayBuilder.with(qiContext) // Create the builder with the context.
                .withText(reaction) // Set the text to say.
                .build() // Build the say action.

        // Execute the action.
        say2.run()

    }

    override fun onRobotFocusLost() {
        conversationBinder?.unbind()

        // Stop the basic emotion observation.
        basicEmotionObserver?.stopObserving()
    }

    override fun onRobotFocusRefused(reason: String) {
        // Nothing here.
    }

    override fun onBasicEmotionChanged(basicEmotion: BasicEmotion) {
        // Update basic emotion image.
//        runOnUiThread { emotion_image_view.setImageResource(emotionImageRes(basicEmotion)) }
    }


//    private fun emotionImageRes(basicEmotion: BasicEmotion): Int {
//        return when (basicEmotion) {
//            BasicEmotion.UNKNOWN -> R.drawable.ic_icons_cute_anon_unknown
//            BasicEmotion.NEUTRAL -> R.drawable.ic_icons_cute_anon_neutral
//            BasicEmotion.CONTENT -> R.drawable.ic_icons_cute_anon_smile
//            BasicEmotion.JOYFUL -> R.drawable.ic_icons_cute_anon_joyful
//            BasicEmotion.SAD -> R.drawable.ic_icons_cute_anon_sad
//            BasicEmotion.ANGRY -> R.drawable.ic_icons_cute_anon_anger
//        }
//    }
}
