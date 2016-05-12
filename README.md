# SmartSms
SmartSms is a messaging app which can perform some smart tasks related to SMS. Its main features are

  - Shows all the messages received by the user (Inbox)
  - Able to send and receive SMS (Also shows notification when SMS is recieved)
  - Also show sent and draft messages and shows total number of messages of inbox,sent and draft on the top in actionbar
  - All the messages are grouped according to address of the user and show user address,date and some content of recent SMS
    and also shows number of messages of that sender in the list item 
  - Implemented full text search ie. the user is able to search through all his SMS's
  - Can Back up all the messages to the user’s google drive
  
##Code Explanation
I have added many features in this SMS app which are very useful in any SMS app.This whole app is based on Material Design like Floating Button,SnackBar,CardView,Recycler View,Color patterns.

When we launch this app a Splash Screen is started which contains logo and name of the app and then is redirected to home activity of the app.
Home activity shows inbox of the user with all the messages grouped according to address of the user(For this I have used HashMap and ArrayList data structure) . Each list item shows address of user,number of messages of that user,date and some content of recent SMS.
On the top in action bar number of SMS are shown with some other options menu.You can write a new message and can send it to anyone.
I have fetched all data about SMS from OS using Content Provider and for android version 5x we have to always request for runtime permissions.

Also when you recieves any SMS then notification will popup which will give you some information about the sender.You can send SMS also
When you click on any of the list item then in all the messages of that address are shown collectively and you can directly write a new message to that address.

This app can backup your messages on user's google drive in the form of a serializable text file.For this I have used Google Drive API.
This app has full text search feature using which user can search from all his messages.For this I have used SearchVew in action bar and implemented it by adding filter to the List adapter which dynamically shows you the search results.
I have used ListView for showing inbox messages, so to make it more efficient I have added View Holder.Also for each message a account circle is shown with different colors for implementing this I used solid circle shape.
