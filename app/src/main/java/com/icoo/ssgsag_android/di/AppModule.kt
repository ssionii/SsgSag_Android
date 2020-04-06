package com.icoo.ssgsag_android.di

import com.icoo.ssgsag_android.data.local.pref.PreferenceManager
import com.icoo.ssgsag_android.data.model.clickHistory.UserLogRepository
import com.icoo.ssgsag_android.data.model.clickHistory.UserLogRepositoryImpl
import com.icoo.ssgsag_android.data.model.event.EventRepository
import com.icoo.ssgsag_android.data.model.event.EventRepositoryImpl
import com.icoo.ssgsag_android.data.model.feed.FeedRepository
import com.icoo.ssgsag_android.data.model.feed.FeedRepositoryImpl
import com.icoo.ssgsag_android.data.model.login.LoginRepository
import com.icoo.ssgsag_android.data.model.login.LoginRepositoryImpl
import com.icoo.ssgsag_android.data.model.poster.PosterRepository
import com.icoo.ssgsag_android.data.model.poster.PosterRepositoryImpl
import com.icoo.ssgsag_android.data.model.review.ReviewRepository
import com.icoo.ssgsag_android.data.model.review.ReviewRepositoryImpl
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepository
import com.icoo.ssgsag_android.data.model.review.club.ClubReviewRepositoryImpl
import com.icoo.ssgsag_android.data.model.schedule.ScheduleRepository
import com.icoo.ssgsag_android.data.model.schedule.ScheduleRepositoryImpl
import com.icoo.ssgsag_android.data.model.signUp.SignupRepository
import com.icoo.ssgsag_android.data.model.signUp.SignupRepositoryImpl
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeRepository
import com.icoo.ssgsag_android.data.model.subscribe.SubscribeRepositoryImpl
import com.icoo.ssgsag_android.data.model.user.UserRepository
import com.icoo.ssgsag_android.data.model.user.UserRepositoryImpl
import com.icoo.ssgsag_android.data.remote.api.NetworkService
import com.icoo.ssgsag_android.ui.login.LoginViewModel
import com.icoo.ssgsag_android.ui.main.MainViewModel
import com.icoo.ssgsag_android.ui.main.allPosters.AllPostersViewModel
import com.icoo.ssgsag_android.ui.main.allPosters.category.AllCategoryViewModel
import com.icoo.ssgsag_android.ui.main.allPosters.search.SearchViewModel
import com.icoo.ssgsag_android.ui.main.calendar.CalendarViewModel
import com.icoo.ssgsag_android.ui.main.calendar.calendarDetail.CalendarDetailViewModel
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.CalendarDialogViewModel
import com.icoo.ssgsag_android.ui.main.calendar.calendarDialog.calendarDialogPage.CalendarDialogPageViewModel
import com.icoo.ssgsag_android.ui.main.feed.FeedViewModel
import com.icoo.ssgsag_android.ui.main.myPage.MyPageViewModel
import com.icoo.ssgsag_android.ui.main.myPage.myReview.MyReviewViewModel
import com.icoo.ssgsag_android.ui.main.review.ReviewViewModel
import com.icoo.ssgsag_android.ui.main.review.club.registration.ClubRgstrViewModel
import com.icoo.ssgsag_android.ui.main.review.club.reviews.MoreReviewViewModel
import com.icoo.ssgsag_android.ui.main.review.club.reviews.blogReview.BlogReviewViewModel
import com.icoo.ssgsag_android.ui.main.review.club.write.ReviewWriteViewModel
import com.icoo.ssgsag_android.ui.main.review.event.ReviewEventViewModel
import com.icoo.ssgsag_android.ui.main.review.main.ReviewMainViewModel
import com.icoo.ssgsag_android.ui.main.review.reviewDetail.ReviewDetailViewModel
import com.icoo.ssgsag_android.ui.main.subscribe.SubscribeViewModel
import com.icoo.ssgsag_android.ui.main.ssgSag.SsgSagViewModel
import com.icoo.ssgsag_android.ui.main.ssgSag.filter.SsgSagFilterViewModel
import com.icoo.ssgsag_android.ui.main.ssgSag.todaySwipePoster.TodaySwipePosterViewModel
import com.icoo.ssgsag_android.ui.main.subscribe.subscribeDialog.SubscribeDialogViewModel
import com.icoo.ssgsag_android.ui.signUp.SignupViewModel
import com.icoo.ssgsag_android.util.scheduler.AndroidSchedulerProvider
import com.icoo.ssgsag_android.util.scheduler.SchedulerProvider
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val rxModule = module {
    //provide schedule provider
    factory<SchedulerProvider> { AndroidSchedulerProvider() }
}

val networkModule = module {
    single { NetworkService.create() }
}

val localModule = module {
    //SharedPreference
    single { PreferenceManager(get()) }
    //Room
}

val factoryModule = module {
    factory<SubscribeRepository> {
        SubscribeRepositoryImpl(
            get(), get()
        )
    }
    factory<UserRepository> {
        UserRepositoryImpl(
            get(), get()
        )
    }
    factory<PosterRepository> {
        PosterRepositoryImpl(
            get(), get()
        )
    }
    factory<ScheduleRepository> {
        ScheduleRepositoryImpl(
            get(), get()
        )
    }
    factory<UserLogRepository> {
        UserLogRepositoryImpl(
            get(), get()
        )
    }
    factory<FeedRepository>{
        FeedRepositoryImpl(
            get(),get()
        )
    }
    factory<LoginRepository>{
        LoginRepositoryImpl(
            get(), get()
        )
    }
    factory<SignupRepository>{
        SignupRepositoryImpl(
            get(), get()
        )
    }
    factory<ReviewRepository>{
        ReviewRepositoryImpl(
            get(), get()
        )
    }
    factory<ClubReviewRepository>{
        ClubReviewRepositoryImpl(
            get(), get()
        )
    }
    factory<EventRepository>{
        EventRepositoryImpl(
            get(), get()
        )
    }
}

val viewModule = module {
    //Common
    viewModel { MainViewModel() }
    //Login
    viewModel {LoginViewModel(get(), get())}
    viewModel { SignupViewModel(get(), get(), get()) }
    //Feed
    viewModel { FeedViewModel(get(),get()) }
    //MyPage
    viewModel { MyPageViewModel(get(), get()) }
    viewModel {MyReviewViewModel(get(), get(), get())}
    //Subscribe
    viewModel { SubscribeViewModel(get(), get()) }
    viewModel { SubscribeDialogViewModel(get(), get())}
    //SsgSag
    viewModel { SsgSagViewModel(get(), get(), get()) }
    viewModel { SsgSagFilterViewModel(get(), get(), get()) }
    viewModel { AllPostersViewModel(get(), get()) }
    viewModel { AllCategoryViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel {TodaySwipePosterViewModel(get(), get())}
    //Schedule
    single { CalendarViewModel(get(), get()) }
    viewModel { CalendarDialogViewModel() }
    viewModel { CalendarDialogPageViewModel(get(), get()) }
    viewModel { CalendarDetailViewModel(get(), get(), get(), get()) }
    // review
    viewModel { ReviewViewModel(get(), get()) }
    single { ReviewDetailViewModel( get(), get(), get()) }
    single { ClubRgstrViewModel(get(), get()) }
    single { ReviewWriteViewModel(get(), get()) }
    viewModel { BlogReviewViewModel(get(), get()) }
    viewModel { MoreReviewViewModel(get(), get(), get()) }
    viewModel { ReviewEventViewModel(get(), get()) }
    viewModel { ReviewMainViewModel(get(), get()) }

}

val appModule = listOf(rxModule, networkModule, localModule, factoryModule, viewModule)