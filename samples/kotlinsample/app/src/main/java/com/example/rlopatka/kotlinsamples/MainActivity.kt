package com.example.rlopatka.kotlinsamples

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.R.attr.duration
import android.opengl.Visibility
import android.view.View
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {

    val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // implicit property for java getters and setters
        txtTest.text = getString(R.string.TxtTest)

        // access layout elements directly without inflating or butterknife
        btnTest.setOnClickListener {
            // lambda instead of event listener implementations
            val toast = Toast.makeText(this, "Test", Toast.LENGTH_SHORT)
            toast.show()
        }

        // call java classes from kotlin
        btnDialog.setOnClickListener {
            val dialog = JavaDialog()
            dialog.show(this)
        }


        // rx java state management
        val service = SampleDataService()
        val loadDataObservable = service
                .callSlowService()
                .map { if (it.isFailure) LoadingState.Failure else LoadingState.Success }
                .startWith(LoadingState.Loading)
                .onErrorReturn{ LoadingState.Failure }

        val sampleSubscription = btnTogle
                .clickObservable()
                .switchMap { loadDataObservable }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    when(it){
                        LoadingState.NotStarted -> {
                            this.btnTogle.isEnabled = true
                            this.pgrLoading.visibility = View.GONE
                        }
                        LoadingState.Loading -> {
                            this.btnTogle.isEnabled = false
                            this.pgrLoading.visibility = View.VISIBLE
                        }
                        LoadingState.Success -> {
                            this.btnTogle.isEnabled = true
                            this.pgrLoading.visibility = View.GONE

                            val toast = Toast.makeText(this, "Success", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                        LoadingState.Failure -> {
                            this.btnTogle.isEnabled = true
                            this.pgrLoading.visibility = View.GONE

                            val toast = Toast.makeText(this, "Error", Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }

                }

        disposables.addAll(sampleSubscription)

        // retrofit service call
        val peopleService = PeopleService()
        val restSubscription = btnRest
                .clickObservable()
                .observeOn(Schedulers.io())
                .switchMap { peopleService.getPersonalInfo("russellwhyte") }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    val toast = Toast.makeText(this, it.firstName, Toast.LENGTH_SHORT)
                    toast.show()
                }

        disposables.add(restSubscription)
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}

fun View.clickObservable(): Observable<Unit> {
    return Observable.create {
        val stream = it
        this.setOnClickListener {
            stream.onNext(Unit)
        }
    }
}


enum class LoadingState {
    NotStarted,
    Loading,
    Success,
    Failure
}