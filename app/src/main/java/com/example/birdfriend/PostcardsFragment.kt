package com.example.birdfriend

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_postcards.*

/**
 * A simple [Fragment] subclass.
 */

class PostcardsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_postcards, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.outofpost_button).setOnClickListener {
            findNavController().navigate(R.id.action_PostcardsFragment_to_SecondFragment)
        }
        val cardList = view.findViewById<LinearLayout>(R.id.cards_display)
// Build data

        val context = activity?.applicationContext
        if (context != null) {
            val db = UserCardsRoomDatabase.getDatabase(context)


            //add new data
//            db.userCardsDao().insertCards(UserCards(3,R.drawable.post_4,true))
            Log.d("check", R.drawable.post_4.toString())

            //check if resId is different than R.drawable.name etc.

            var resourceID = resources.getIdentifier(
                "post_4",
                "drawable",
                "com.example.birdfriend"
            )

            Log.d("check", resourceID.toString())
            //

            val userCardList = db.userCardsDao().getAlluserCards()
            for (card in userCardList) {

                Log.d("help", card.nameid.toString())

                val imgSrc = ImageView(getActivity())
                imgSrc.layoutParams = LinearLayout.LayoutParams(400, 400)
                imgSrc.setImageResource(card.nameid)
                cardList.addView(imgSrc)

                //click on image to show popup window
                imgSrc.setOnClickListener(){

                    var window = PopupWindow(activity)
                    var view = layoutInflater.inflate(R.layout.dialog_pop_up, null)
                    window.contentView = view
                    var imageView = view.findViewById<ImageView>(R.id.imageView)
                    imageView.setImageResource(card.nameid)
//                    imageView.setOnClickListener{
//                        window.dismiss()
//                    }
                    val dismissButton = view.findViewById<Button>(R.id.dismiss_button)
                    dismissButton.setOnClickListener{
                        window.dismiss()
                    }

                    window.showAsDropDown(textView)

                }
            }
        } else {
            Log.d("help", "context was null")
        }

//
//        var c1 = Cards( R.drawable.post_1)
//        var c2 = Cards(R.drawable.post_2)
//        val testList =  arrayListOf<Cards>(c1,c2)
//        Log.d("cards", c1.toString())
//        for (card in testList){
//            var imgSrc =  ImageView(getActivity())
//            imgSrc.layoutParams = LinearLayout.LayoutParams(400, 400)
//            imgSrc.setImageResource(card.name)
//            cardList.addView(imgSrc)
//        }


    }
}