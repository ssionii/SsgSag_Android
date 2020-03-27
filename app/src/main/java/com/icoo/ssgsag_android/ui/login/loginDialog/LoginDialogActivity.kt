package com.icoo.ssgsag_android.ui.login.loginDialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.Preference
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.Observer
import com.icoo.ssgsag_android.data.local.pref.SharedPreferenceController
import com.icoo.ssgsag_android.SsgSagApplication
import com.icoo.ssgsag_android.util.service.network.NetworkService
import com.icoo.ssgsag_android.R
import com.icoo.ssgsag_android.base.BaseActivity
import com.icoo.ssgsag_android.databinding.ActivityCalendarDetailBinding
import com.icoo.ssgsag_android.ui.login.LoginActivity.GetLogin.loginType
import com.icoo.ssgsag_android.ui.login.LoginActivity.GetLogin.token
import com.icoo.ssgsag_android.ui.login.LoginViewModel
import com.icoo.ssgsag_android.ui.main.MainActivity
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailViewModel
import com.icoo.ssgsag_android.ui.signUp.profile.SignUpProfileActivity
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.log.Logger
import com.nhn.android.naverlogin.OAuthLogin
import com.nhn.android.naverlogin.OAuthLoginHandler
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_dialog.*
import com.icoo.ssgsag_android.databinding.ActivityLoginDialogBinding
import com.icoo.ssgsag_android.ui.signUp.SignupActivity
import com.icoo.ssgsag_android.util.extensionFunction.setSafeOnClickListener
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.IllegalStateException
import java.util.*

class LoginDialogActivity : BaseActivity<ActivityLoginDialogBinding, LoginViewModel>() {

    override val layoutResID: Int
        get() = R.layout.activity_login_dialog

    override val viewModel: LoginViewModel by viewModel()

    internal val disposable = CompositeDisposable()

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    private val TAG = "OAuthSampleActivity"

    /**
     * client 정보를 넣어준다.
     */
    private val OAUTH_CLIENT_ID = "jsLE7cET_smOvsjkJYSc"
    private val OAUTH_CLIENT_SECRET = "uAnTlBnsd8"
    private val OAUTH_CLIENT_NAME = "네이버 아이디로 로그인"

    private var naverOAuthLoginInstance: OAuthLogin? = null
    private var naverContext: Context? = null
    private var mOAuthLoginButton: OAuthLoginButton? = null

    private var kakaoCallback: SessionCallback? = null

    /**
     * 로그인 버튼을 클릭 했을시 access token을 요청하도록 설정한다.
     *
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewDataBinding.vm = viewModel
        navigator()

        // naver login
        naverContext = this
        initNaverData()
        setOnClickListener()
        this.title = "OAuthLoginSample Ver." + OAuthLogin.getVersion()

        //kakao login
        /*
        if(getIntent.getBooleanExtra("isLogout", false)) {

        }*/
        Session.getCurrentSession().close()
        kakaoCallback = SessionCallback()
        Session.getCurrentSession().addCallback(kakaoCallback)
        Session.getCurrentSession().checkAndImplicitOpen()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(kakaoCallback)
    }


    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            var keys = ArrayList<String>()
            keys.add("properties.nickname")
            keys.add("properties.profile_image")
            keys.add("kakao_account.adminEmail")

            UserManagement.getInstance().me(keys, object : MeV2ResponseCallback() {
                override fun onFailure(errorResult: ErrorResult) {
                    val message = "failed to get user info. msg=$errorResult"
                    Logger.d(message)
                }

                override fun onSessionClosed(errorResult: ErrorResult) {
                }

                override fun onSuccess(response: MeV2Response) {
                    token = Session.getCurrentSession().accessToken
                    loginType = 0
                    Log.e("KAKAOTOKEN", "" + Session.getCurrentSession().accessToken)

                    viewModel.login(token, loginType)
                }
            })
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Logger.e(exception)
            }
        }

    }


    private fun initNaverData() {
        naverOAuthLoginInstance = OAuthLogin.getInstance()

        naverOAuthLoginInstance!!.showDevelopersLog(true)
        naverOAuthLoginInstance!!.init(naverContext!!, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME)

        /*
	     * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent clubWebsite 을 넣어줘야 로그인하는데 문제가 안생긴다.
		 * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package clubName 을 넣어준 경우 callback intent clubWebsite 을 생략해도 된다.
		 */
    }

    private fun setOnClickListener() {

        act_login_dialog_btn_naver.setSafeOnClickListener {
            naverOAuthLoginInstance!!.startOauthLoginActivity(this,naverOAuthLoginHandler)
        }

        act_login_dialog_iv_cancel.setSafeOnClickListener {
            finish()
        }
    }

    private fun navigator() {
        viewModel.activityToStart.observe(this@LoginDialogActivity, Observer { value ->
            val intent = Intent(this, value.first.java)
            startActivity(intent)
            finishAffinity()
        })
    }

    override fun onResume() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        super.onResume()
    }

    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    private val naverOAuthLoginHandler = @SuppressLint("HandlerLeak")
    object : OAuthLoginHandler() {
        override fun run(success: Boolean) {
            if (success) {
                token = naverOAuthLoginInstance!!.getAccessToken(naverContext)
//                val refreshToken = mOAuthLoginInstance!!.getRefreshToken(mContext)
//                val expiresAt = mOAuthLoginInstance!!.getExpiresAt(mContext)
//                val tokenType = mOAuthLoginInstance!!.getTokenType(mContext)
                Log.d("NAVER", "" + token)
                loginType=1
                viewModel.login(token, loginType)
//                finish()
            } else {
                val errorCode = naverOAuthLoginInstance!!.getLastErrorCode(naverContext).code
                val errorDesc = naverOAuthLoginInstance!!.getLastErrorDesc(naverContext)
                //Toast.makeText(naverContext, "errorCode:$errorCode, errorDesc:$errorDesc", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
