package com.dragger.hilt.myapplication

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    val disposable = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val editText = view.findViewById<EditText>(R.id.editText)
        
        disposable.add(
            RxTextView
                .textChanges(editText)
                .skipInitialValue()
                .debounce(300, MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<CharSequence?>(), Observer<CharSequence> {
                    override fun onError(e: Throwable) {}
                    override fun onComplete() {}
                    override fun onNext(t: CharSequence?) {
                        view.findViewById<TextView>(R.id.textView).setText("Query: $editText")
                    }

                    override fun onSubscribe(d: Disposable?) {
                    }
                })
        );
        
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.clear();
    }

}
