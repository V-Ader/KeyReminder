package com.example.keyreminder

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.keyreminder.MyListAdapter

class MyListAdapter(private val context: Activity, private val title: Array<String>, private val description: Array<String>)
    : ArrayAdapter<String>(context, R.layout.listviewelement, title) {

    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.listviewelement, null, true)

        val name = rowView.findViewById(R.id.name) as TextView
        val descriptionfield = rowView.findViewById(R.id.description) as TextView

        name.text = title[position]
        descriptionfield.text = description[position]


        return rowView
    }
}