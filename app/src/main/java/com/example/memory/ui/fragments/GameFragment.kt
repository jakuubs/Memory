package com.example.memory.ui.fragments

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.memory.R
import com.example.memory.data.Settings
import com.example.memory.databinding.FragmentGameBinding
import com.example.memory.ui.base.BaseFragment
import com.example.memory.viewmodels.GameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : BaseFragment() {

    var fragmentGameBinding: FragmentGameBinding? = null
    val gViewModel: GameViewModel by viewModels()

    lateinit var settings: Settings

    var pairs: MutableMap<ImageView, Int> = mutableMapOf()
    var tags: MutableMap<Int, String> = mutableMapOf()

    lateinit var cards: MutableList<ImageView>
    lateinit var imgs: MutableList<Int>

    var gameTime = 0L
    var punish = 0

    var clicked = 0
    var guessed = 0
    var timePassed = false

    var points = 0

    var time = 0L
    var ctdTime = 0L

    var gameDifficulty = "Normal"

    lateinit var firstCard: ImageView
    lateinit var secondCard: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game, container, false
        )

        fragmentBinding.youWon.visibility = View.INVISIBLE
        fragmentBinding.playAgainButton.visibility = View.INVISIBLE
        fragmentBinding.ggButton.visibility = View.INVISIBLE
        fragmentBinding.youLost.visibility = View.INVISIBLE
        fragmentBinding.areYouSure.visibility = View.INVISIBLE
        fragmentBinding.yes.visibility = View.INVISIBLE
        fragmentBinding.no.visibility = View.INVISIBLE

        gViewModel.getCurrentSettings().observe(viewLifecycleOwner, Observer {
            settings = it

            if (settings.theme == "Animals") {
                imgs = mutableListOf(R.drawable.bat, R.drawable.bird,
                    R.drawable.camel,R.drawable.cow,
                    R.drawable.dolphin, R.drawable.elephant,
                    R.drawable.giraffe, R.drawable.kangaroo,
                    R.drawable.koala, R.drawable.lion,
                    R.drawable.squirrel, R.drawable.tiger)
            } else if (settings.theme == "Cars") {
                imgs = mutableListOf(R.drawable.aston_martin, R.drawable.bmw,
                    R.drawable.bugatti, R.drawable.chevrolet,
                    R.drawable.ferrari, R.drawable.honda,
                    R.drawable.lamborghini, R.drawable.mercedes,
                    R.drawable.mitsubishi, R.drawable.porsche,
                    R.drawable.tesla, R.drawable.toyota)
            } else if (settings.theme == "Emojis") {
                imgs = mutableListOf(R.drawable.alien, R.drawable.amazed,
                    R.drawable.angry, R.drawable.cold,
                    R.drawable.cool, R.drawable.exploding,
                    R.drawable.ghost, R.drawable.greed,
                    R.drawable.ninja, R.drawable.robot,
                    R.drawable.sick, R.drawable.skull)
            } else if (settings.theme == "Fruit") {
                imgs = mutableListOf(R.drawable.apple, R.drawable.banana,
                    R.drawable.blueberries, R.drawable.grapes,
                    R.drawable.orange, R.drawable.pear,
                    R.drawable.pineapple, R.drawable.pitaya,
                    R.drawable.pomegranate, R.drawable.raspberry,
                    R.drawable.strawberry, R.drawable.watermelon)
            } else if (settings.theme == "Logos") {
                imgs = mutableListOf(R.drawable.android, R.drawable.facebook,
                    R.drawable.instagram, R.drawable.paypal,
                    R.drawable.pinterest, R.drawable.skype,
                    R.drawable.snapchat, R.drawable.soundcloud,
                    R.drawable.spotify, R.drawable.twitter,
                    R.drawable.whatsapp, R.drawable.youtube)
            }

            for (i in 0..11) {
                tags.put(imgs[i], i.toString())
            }

            gameDifficulty = settings.difficulty

            if (settings.difficulty == "Easy") {
                gameTime = 300000
                punish = 1
            } else if (settings.difficulty == "Normal") {
                gameTime = 180000
                punish = 1
            } else if (settings.difficulty == "Hard") {
                gameTime = 120000
                punish = 3
            } else if (settings.difficulty == "Hardcore") {
                gameTime = 60000
                punish = 5
            }

            cards = mutableListOf(fragmentBinding.que11, fragmentBinding.que12, fragmentBinding.que13, fragmentBinding.que14,
                fragmentBinding.que21, fragmentBinding.que22, fragmentBinding.que23, fragmentBinding.que24,
                fragmentBinding.que31, fragmentBinding.que32, fragmentBinding.que33, fragmentBinding.que34,
                fragmentBinding.que41, fragmentBinding.que42, fragmentBinding.que43, fragmentBinding.que44,
                fragmentBinding.que51, fragmentBinding.que52, fragmentBinding.que53, fragmentBinding.que54,
                fragmentBinding.que61, fragmentBinding.que62, fragmentBinding.que63, fragmentBinding.que64)

            pairs = drawRandomImages(cards, imgs, tags)

            fragmentBinding.pts.text = points.toString()

            val timer = object: CountDownTimer(gameTime, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val mins = millisUntilFinished / 1000 / 60
                    val secs = millisUntilFinished / 1000 % 60
                    if (secs < 10L)
                        fragmentBinding.timeLeft.text = "$mins : 0$secs"
                    else
                        fragmentBinding.timeLeft.text = "$mins : $secs"
                    if (millisUntilFinished < 11000)
                        fragmentBinding.timeLeft.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))
                    ctdTime = millisUntilFinished
                }

                override fun onFinish() {
                    if (guessed < 12) {
                        // fragmentBinding.board.visibility = View.GONE
                        for (card in cards)
                            card.visibility = View.GONE
                        fragmentBinding.surrenderButton.visibility = View.GONE
                        fragmentBinding.youLost.visibility = View.VISIBLE
                        fragmentBinding.playAgainButton.visibility = View.VISIBLE
                        fragmentBinding.ggButton.visibility = View.VISIBLE
                        timePassed = true
                    }
                }
            }

            timer.start()

            for (i in 0..23) {
                cards[i].setOnClickListener(object: View.OnClickListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onClick(view: View?) {
                        if (clicked == 0) {
                            firstCard = cards[i]
                            cards[i].setImageResource(pairs[cards[i]]!!)
                            clicked = 1
                        } else if (clicked == 1) {
                            secondCard = cards[i]
                            cards[i].setImageResource(pairs[cards[i]]!!)
                            clicked = 2
                        }
                        if (clicked == 2) {
                            for (card in cards)
                                card.setEnabled(false)
                            if (firstCard == secondCard) {
                                clicked = 1
                                for (card in cards)
                                    card.setEnabled(true)
                            } else if (firstCard.tag == secondCard.tag) {
                                Handler().postDelayed({
                                    firstCard.visibility = View.INVISIBLE
                                    secondCard.visibility = View.INVISIBLE
                                    clicked = 0
                                    points += 20
                                    guessed++
                                    fragmentBinding.pts.text = points.toString()
                                    for (card in cards)
                                        card.setEnabled(true)
                                    if (guessed == 12) {
                                        // fragmentBinding.board.visibility = View.GONE
                                        fragmentBinding.surrenderButton.visibility = View.GONE
                                        fragmentBinding.youWon.visibility = View.VISIBLE
                                        fragmentBinding.playAgainButton.visibility = View.VISIBLE
                                        fragmentBinding.ggButton.visibility = View.VISIBLE
                                        Toast.makeText(context, "Congratulations, you\'ve won!", Toast.LENGTH_LONG).show()
                                        timer.cancel()
                                        time = gameTime - ctdTime
                                    }
                                }, 750)
                            } else {
                                Handler().postDelayed({
                                    firstCard.setImageResource(R.drawable.question_mark)
                                    secondCard.setImageResource(R.drawable.question_mark)
                                    if (guessed > 0)
                                        points -= punish
                                    clicked = 0
                                    fragmentBinding.pts.text = points.toString()
                                    for (card in cards)
                                        card.setEnabled(true)
                                }, 750)
                            }
                        }
                        /*Handler().postDelayed({
                            if (clicked == 2) {
                                if (firstCard == secondCard) {
                                    clicked = 1
                                } else if (firstCard.drawable.constantState == secondCard.drawable.constantState) {
                                    firstCard.visibility = View.INVISIBLE
                                    secondCard.visibility = View.INVISIBLE
                                    clicked = 0
                                    points += 20
                                    guessed++
                                    if (guessed == 12) {
                                        fragmentBinding.board.visibility = View.GONE
                                        fragmentBinding.surrenderButton.visibility = View.GONE
                                        fragmentBinding.youWon.visibility = View.VISIBLE
                                        fragmentBinding.playAgainButton.visibility = View.VISIBLE
                                        fragmentBinding.ggButton.visibility = View.VISIBLE
                                        Toast.makeText(context, "Congratulations, you\'ve won!", Toast.LENGTH_LONG).show()
                                        timer.cancel()
                                        time = gameTime - ctdTime
                                    }
                                } else {
                                    firstCard.setImageResource(R.drawable.question_mark)
                                    secondCard.setImageResource(R.drawable.question_mark)
                                    if (guessed > 0)
                                        points -= punish
                                    clicked = 0
                                }
                                fragmentBinding.pts.text = points.toString()
                            }
                        }, 1000)*/
                    }
                })
            }
        })

        this.fragmentGameBinding = fragmentBinding

        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentGameBinding?.apply {
            lifecycleOwner = viewLifecycleOwner
            gameViewModel = gViewModel
        }
        observeModelNavigation(gViewModel)

        fragmentGameBinding?.ggButton!!.setOnClickListener(object: View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(v: View?) {
                gViewModel.finishGame(points, time, gameDifficulty, timePassed)
            }
        })

        fragmentGameBinding?.playAgainButton!!.setOnClickListener(object: View.OnClickListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onClick(v: View?) {
                gViewModel.playAgain(points, time, gameDifficulty, timePassed)
            }
        })

        fragmentGameBinding?.yes!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                gViewModel.yes()
            }
        })

        fragmentGameBinding?.no!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                fragmentGameBinding?.areYouSure!!.visibility = View.INVISIBLE
                fragmentGameBinding?.yes!!.visibility = View.INVISIBLE
                fragmentGameBinding?.no!!.visibility = View.INVISIBLE
            }
        })

        fragmentGameBinding?.surrenderButton!!.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {
                if (fragmentGameBinding?.areYouSure!!.visibility == View.INVISIBLE
                        && fragmentGameBinding?.yes!!.visibility == View.INVISIBLE
                        && fragmentGameBinding?.no!!.visibility == View.INVISIBLE) {
                    fragmentGameBinding?.areYouSure!!.visibility = View.VISIBLE
                    fragmentGameBinding?.yes!!.visibility = View.VISIBLE
                    fragmentGameBinding?.no!!.visibility = View.VISIBLE
                } else {
                    fragmentGameBinding?.areYouSure!!.visibility = View.INVISIBLE
                    fragmentGameBinding?.yes!!.visibility = View.INVISIBLE
                    fragmentGameBinding?.no!!.visibility = View.INVISIBLE
                }
            }
        })
    }

    private fun drawRandomImages(cards: MutableList<ImageView>, imgs: MutableList<Int>, tags: MutableMap<Int, String>): MutableMap<ImageView, Int> {
        val p: MutableMap<ImageView, Int> = mutableMapOf()

        val imagesList: MutableList<Int> = mutableListOf()
        imagesList.addAll(imgs)
        imagesList.addAll(imgs)

        imagesList.shuffle()

        for (i in 0..23) {
            cards[i].tag = tags[imagesList[i]]
            p.put(cards[i], imagesList[i])
        }

        return p
    }
}