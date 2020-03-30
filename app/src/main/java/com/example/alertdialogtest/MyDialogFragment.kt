package com.example.alertdialogtest

import android.R.layout
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit


class MyDialogFragment : DialogFragment() {
    private var disposable: Disposable? = null
    var time = "dsfsfsf"
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        timer()
        val alertDialog: AlertDialog
        return activity?.let {
            val builder = AlertDialog.Builder(it)
          //  builder.setTitle("Важное сообщение!")
           //     .setMessage(time)

            var inflater = activity!!.layoutInflater
            var view = inflater.inflate(R.layout.custom_layout, null);
            builder.setView(view)
            textView = view.findViewById<TextView>(R.id.textView)
            builder.create()

          //   alertDialog = builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
        alertDialog.window!!.setLayout(600, 400) //Controlling width and height.

        alertDialog.show()
    }

    private fun timer()  {

        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
            .take(20)
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                Log.d(MainActivity.TAG, it.toString())
                textView.setText(it.toString()) }
            .subscribe({}, {}, {})

    }

    override fun onDestroy() {
        Log.d(MainActivity.TAG, "onDestroy()")
        super.onDestroy()
        disposable?.dispose()

    }

    override fun onDetach() {
        super.onDetach()
        Log.d(MainActivity.TAG, "onDetach()")
    }


    override fun onDestroyView() {
        Log.d(MainActivity.TAG, "onDestroyView()")

        val dialog = dialog
        // Work around bug: http://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && retainInstance) dialog.setDismissMessage(null)
        super.onDestroyView()
    }
}