package com.guendeli.timerapp.utils
import android.content.Context;
import android.preference.PreferenceManager
import com.guendeli.timerapp.TimerActivity

class PrefUtils {
    companion object {
        fun getTimerLenght(context : Context) : Int {
            // placeHolder stuff
            return 1;
        }

        private const val PREVIOUS_TIMER_LENGHT_SECONDS_ID = "com.guendeli.timer.previous_timer_lenght";
        private const val REMAINING_SECONDS_ID = "com.guendeli.timer.remaining_seconds";
        private const val TIMER_STATE_ID = "com.guendeli.timer.timer_state";

        fun getPreviousTimerLenghtSeconds(context : Context) : Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getLong(PREVIOUS_TIMER_LENGHT_SECONDS_ID, 0L);
        }

        fun setPreviousTimerLenghtSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putLong(PREVIOUS_TIMER_LENGHT_SECONDS_ID, seconds);
            editor.apply();
        }

        fun getRemaningSeconds(context : Context) : Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context);
            return preferences.getLong(REMAINING_SECONDS_ID, 0L);
        }

        fun setRemainingSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            editor.putLong(REMAINING_SECONDS_ID, seconds);
            editor.apply();
        }

        fun getTimerState(context : Context) : TimerActivity.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context);
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0);
            return  TimerActivity.TimerState.values()[ordinal];
        }

        fun setTimerState(state : TimerActivity.TimerState, context : Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
            val ordinal = state.ordinal;
            editor.putInt(TIMER_STATE_ID, ordinal);
            editor.apply();
        }
    }
}