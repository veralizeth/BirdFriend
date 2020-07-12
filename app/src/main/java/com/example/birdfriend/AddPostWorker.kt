package com.example.birdfriend


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters


class AddPostWorker(appContext: Context, workerParams: WorkerParameters):

    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        // Do the work here--in this case, change show text display

        try {

            addNewPostCard()
            return Result.success()
            Log.i("testing", "AddPostWorker initiated!")
            // below is where data is log at request method


        } catch(e:Exception) {
            return Result.failure()
            Log.i("testing", "AddPostWorker initiated!")
        }
    }

    //set up func

    private  fun addNewPostCard(){
        //check if home or not
        val dbLog = LogStateDatabase.getDatabase(applicationContext)
        val homeStatus = dbLog.logStateDao().getLastState()[0].stateHomeAway

        val dbCard = UserCardsRoomDatabase.getDatabase(applicationContext)
        val cardInAlbum = dbCard.userCardsDao().getShowCards()

        val postCardsList = listOf("post_movie","post_moon","post_1","post_2","post_4","lao_post")
        //check all existing post names in db

        var existingCardName = mutableListOf<String>()

        for(card in cardInAlbum){
            existingCardName.add(card.imgname)
        }

        val cardToAdd = postCardsList - existingCardName

        if (homeStatus == "Away" && cardToAdd.isEmpty() == false ){

            var nameToAdd = cardToAdd.shuffled()[0]

            dbCard.userCardsDao().insertCards(UserCards(nameToAdd, false ))

            Log.i("testing","$nameToAdd was added!")

        }else{
            Log.i("testing","nothing was added!")

        }


    }

    // add notification function
//    fun sendNotificationWork(){
//        MainActivity
//    }
}


//RECORD

