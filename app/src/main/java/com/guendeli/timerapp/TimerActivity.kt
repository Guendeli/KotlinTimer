package com.guendeli.timerapp

import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.guendeli.timerapp.utils.PrefUtils

import kotlinx.android.synthetic.main.activity_timer.*
import kotlinx.android.synthetic.main.content_timer.*

class TimerActivity : AppCompatActivity() {

    enum class TimerState{
        Stopped, Paused, Running
    }

    private lateinit var timer : CountDownTimer;
    private var timerLenghtSeconds : Long = 0;
    private var timerState : TimerState = TimerState.Stopped;

    private var secondsRemaining = 0L;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        setSupportActionBar(toolbar)

        supportActionBar?.setIcon(R.drawable.ic_timer);
        supportActionBar?.title = "    Timer";

        fab_start.setOnClickListener{v ->
            startTimer();
            timerState = TimerState.Running;
            updateButtons();
        }

        fab_pause.setOnClickListener{ v ->
            timer.cancel();
            timerState = TimerState.Paused;
            updateButtons();
        }

        fab_stop.setOnClickListener { v ->
            timer.cancel();
            onTimerFinish();
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer();

        //TODO: remove Background timer, hide push notificaiton

    }

    override fun onPause() {
        super.onPause()

        if(timerState == TimerState.Running){
            timer.cancel();
            // TODO: start background timer and show notification
        } else if (timerState == TimerState.Paused){
            // TODO: show notifications
        }
        PrefUtils.setPreviousTimerLenghtSeconds(timerLenghtSeconds, this);
        PrefUtils.setRemainingSeconds(secondsRemaining, this);
        PrefUtils.setTimerState(timerState, this);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_timer, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    // private methods are done here

    private fun onTimerFinish(){
        timerState = TimerState.Stopped;
        setNewTimerLenght();

        progress_countdown.progress = 0;

        PrefUtils.setRemainingSeconds(timerLenghtSeconds, this);
        secondsRemaining = timerLenghtSeconds;

        updateButtons();
        updateCountdownUI();
    }
    private fun initTimer(){
        timerState = PrefUtils.getTimerState(this);
        if(timerState == TimerState.Running){
            setNewTimerLenght();
        } else {
            setPreviousTimerLenght();
        }

        secondsRemaining = if(timerState == TimerState.Running || timerState == TimerState.Paused){
            PrefUtils.getRemaningSeconds(this);
        } else {
            timerLenghtSeconds;
        }

        if(timerState == TimerState.Running){
            startTimer();
        }

        updateButtons();
        updateCountdownUI();
    }

    private fun startTimer(){
        timerState = TimerState.Running;

        timer = object : CountDownTimer(secondsRemaining*1000, 1000){
            override fun onFinish() = onTimerFinish();

            override fun onTick(p0: Long) {
                secondsRemaining = p0 / 1000;
                updateCountdownUI();
            }
        }.start();
    }

    private fun setNewTimerLenght(){
        val lenghtInMinutes = PrefUtils.getTimerLenght(this);
        timerLenghtSeconds = (lenghtInMinutes * 60L);
        progress_countdown.max = timerLenghtSeconds.toInt();
    }

    private fun setPreviousTimerLenght(){
        timerLenghtSeconds = PrefUtils.getPreviousTimerLenghtSeconds(this);
        progress_countdown.max = timerLenghtSeconds.toInt();
    }

    private fun updateCountdownUI(){
        val minutesUntilFinished = secondsRemaining / 60;
        val secondsInMinutesUntilFinished = secondsRemaining - minutesUntilFinished * 60;
        val secondsStr = secondsInMinutesUntilFinished.toString();

        textViewProgress.text = "$minutesUntilFinished:${
        if(secondsStr.length == 2) secondsStr
        else "0" + secondsStr}";

        progress_countdown.progress = (timerLenghtSeconds - secondsRemaining).toInt();
    }

    private fun updateButtons(){
        when(timerState){
            TimerState.Running ->{
                fab_start.isEnabled = false;
                fab_pause.isEnabled = true;
                fab_stop.isEnabled = true;
            }
            TimerState.Stopped ->{
                fab_start.isEnabled = true;
                fab_pause.isEnabled = false;
                fab_stop.isEnabled = false;
            }
            TimerState.Paused ->{
                fab_start.isEnabled = true;
                fab_pause.isEnabled = true;
                fab_stop.isEnabled = true;
            }
        }
    }
}
