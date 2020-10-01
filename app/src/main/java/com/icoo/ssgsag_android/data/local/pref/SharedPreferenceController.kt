package com.icoo.ssgsag_android.data.local.pref

import android.content.Context

object SharedPreferenceController{
    private val USER_NAME = "MYKEY"
    private val myAuth = "myAuth"
    private val walkthroughs = "walkthroughs"
    private val signupType ="signupType"

    private val coachMarker = "coachMarker"
    private val coachFlow = "coachFlow"

    private val receivableCard = "receivableCard"
    private val receivableTodo = "receivableTodo"

    private val isLogout ="isLogout"

    private val isFirstOpenEvent ="isFirstOpenEvent"

    private val seeFeedCoachMark = "feedCoachMark"
    private val seeSsgSagCoachMark = "ssgSagCoachMark"
    private val seeCalendarCoachMark = "calendarCoachMark"
    private val seeCommunityCoachMark = "communityCoachMark"

    private val firebaseInstanceId = "instanceId"


    fun setAuthorization(context: Context, authorization : String)
    {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(myAuth, authorization)
        editor.apply()
    }

    fun getAuthorization(context: Context) : String {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(myAuth, "")!!
    }

    fun deleteAuthorization(context: Context) {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.remove(myAuth)
        editor.apply()
    }

    fun setType(context : Context, type : String){
        val pref = context.getSharedPreferences(signupType, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(signupType, type)
        editor.apply()
    }

    fun getType(context: Context) : String {
        val pref = context.getSharedPreferences(signupType, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(signupType, "user")!!
    }

    fun deleteType(context: Context) {
        val pref = context.getSharedPreferences(signupType, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.remove(signupType)
        editor.apply()
    }

    fun setReceivableCard(context: Context, v : String)
    {
        val pref = context.getSharedPreferences(receivableCard, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(receivableCard, v)
        editor.apply()
    }

    fun getReceivableCard(context: Context) : String {
        val pref = context.getSharedPreferences(receivableCard, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(receivableCard, "true")!!
    }

    fun setReceivableTodo(context: Context, v : String)
    {
        val pref = context.getSharedPreferences(receivableTodo, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(receivableTodo, v)
        editor.apply()
    }

    fun getReceivableTodo(context: Context) : String {
        val pref = context.getSharedPreferences(receivableTodo, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(receivableTodo, "false")!!
    }

    fun setWalkthroughs(context: Context, v : String)
    {
        val pref = context.getSharedPreferences(walkthroughs, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(walkthroughs, v)
        editor.apply()
    }
    fun getWalkthroughs(context: Context) : String {
        val pref = context.getSharedPreferences(walkthroughs, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(walkthroughs, "false")!!
    }

    fun clearSPC(context: Context)
    {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }


    fun setIsLogout(context: Context, v : Boolean)
    {
        val pref = context.getSharedPreferences(isLogout, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putBoolean(isLogout, v)
        editor.apply()
    }

    fun getIsLogout(context: Context) : Boolean {
        val pref = context.getSharedPreferences(isLogout, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getBoolean(isLogout, false)
    }


    fun setIsFirstOpen(context: Context, v : Boolean)
    {
        val pref = context.getSharedPreferences(isFirstOpenEvent, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putBoolean(isFirstOpenEvent, v)
        editor.apply()
    }

    fun getIsFirstOpen(context: Context) : Boolean {
        val pref = context.getSharedPreferences(isFirstOpenEvent, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getBoolean(isFirstOpenEvent, true)
    }


    /* coach mark */
    fun setFeedCoachMark(context: Context, v : Boolean)
    {
        val pref = context.getSharedPreferences(seeFeedCoachMark, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putBoolean(seeFeedCoachMark, v)
        editor.apply()
    }

    fun getFeedCoachMark(context: Context) : Boolean {
        val pref = context.getSharedPreferences(seeFeedCoachMark, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getBoolean(seeFeedCoachMark, true)
    }

    fun setSsgSagCoachMark(context: Context, v : Boolean)
    {
        val pref = context.getSharedPreferences(seeSsgSagCoachMark, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putBoolean(seeSsgSagCoachMark, v)
        editor.apply()
    }

    fun getSsgSagCoachMark(context: Context) : Boolean {
        val pref = context.getSharedPreferences(seeSsgSagCoachMark, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getBoolean(seeSsgSagCoachMark, true)
    }

    fun setCalendarCoachMark(context: Context, v : Boolean)
    {
        val pref = context.getSharedPreferences(seeCalendarCoachMark, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putBoolean(seeCalendarCoachMark, v)
        editor.apply()
    }

    fun getCalendarCoachMark(context: Context) : Boolean {
        val pref = context.getSharedPreferences(seeCalendarCoachMark, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getBoolean(seeCalendarCoachMark, true)
    }

    fun setCommunityCoachMark(context: Context, v : Boolean)
    {
        val pref = context.getSharedPreferences(seeCommunityCoachMark, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putBoolean(seeCommunityCoachMark, v)
        editor.apply()
    }

    fun getCommunityCoachMark(context: Context) : Boolean {
        val pref = context.getSharedPreferences(seeCommunityCoachMark, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getBoolean(seeCommunityCoachMark, true)
    }

    fun setFireBaseInstanceId(context: Context, v: String) {
        val pref = context.getSharedPreferences(firebaseInstanceId, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        val editor = pref.edit()
        editor.putString(firebaseInstanceId, v)
        editor.apply()
    }

    fun getFireBaseInstanceId(context: Context) : String {
        val pref = context.getSharedPreferences(firebaseInstanceId, Context.MODE_PRIVATE) //현재 내 기기에서만 볼수 있는 데이터
        return pref.getString(firebaseInstanceId, "")!!
    }

}

