package com.android.todoapp.utils

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.android.todoapp.R
import com.android.todoapp.model.Priority
import com.android.todoapp.model.ToDoModel
import com.android.todoapp.ui.list.ListFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapters {

    companion object {

        // List Fragment - "No Data" Image and Text Visibility
        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when(emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
                else -> {}
            }
        }

        // List Fragment - Navigate to Add Fragment
        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(floatingButton: FloatingActionButton, navigate: Boolean) {
            floatingButton.setOnClickListener {
                if(navigate) {
                    floatingButton.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }

        // Row Layout - Priority Color
        @RequiresApi(Build.VERSION_CODES.M)
        @BindingAdapter("android:parsePriorityColor")
        @JvmStatic
        fun parsePriorityColor(cardView: CardView, priority: Priority) {
            when(priority){
                Priority.HIGH -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.red))
                }
                Priority.MEDIUM -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))
                }
                Priority.LOW -> {
                    cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
                }
            }
        }

        // Row Layout - Send Data to Update Fragment
        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(layout: ConstraintLayout, currentItem: ToDoModel) {
            layout.setOnClickListener {
                val action = ListFragmentDirections.actionListFragmentToUpdateFragment(currentItem)
                layout.findNavController().navigate(action)
            }
        }
    }
}
