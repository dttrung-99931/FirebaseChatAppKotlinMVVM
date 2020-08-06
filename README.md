# FirebaseChatAppKotlinMVVM
An android chat app built with Firebase (Authentication, Firestore, Storage) in mvvp architecture

## Screenshots
<p>
   <a target="_blank" rel="noopener noreferrer" href="https://github.com/pandahacker9x/FirebaseChatAppKotlinMVVM/blob/master/screenshots/login.png">
      <img src="https://github.com/pandahacker9x/FirebaseChatAppKotlinMVVM/blob/master/screenshots/login.png" alt="Login" title="Login" style="max-width:100%">
   </a>
   <a target="_blank" rel="noopener noreferrer" href="https://github.com/pandahacker9x/FirebaseChatAppKotlinMVVM/blob/master/screenshots/chat_list.png">
      <img src="https://github.com/pandahacker9x/FirebaseChatAppKotlinMVVM/blob/master/screenshots/chat_list.png" alt="Chat list" title="Chat list" style="max-width:100%">
   </a>
   <a target="_blank" rel="noopener noreferrer" href="https://github.com/pandahacker9x/FirebaseChatAppKotlinMVVM/blob/master/screenshots/chat.png">
      <img src="https://github.com/pandahacker9x/FirebaseChatAppKotlinMVVM/blob/master/screenshots/chat.png" alt="Chat" title="Chat" style="max-width:100%">
   </a>
</p>

## Library and concept references
<ol>
  <li>Dependency Injection with <a href="https://medium.com/@iammert/new-android-injector-with-dagger-2-part-1-8baa60152abe">Dagger2</a></li>
  <li>Android MVVM architecture with <a href="https://www.journaldev.com/22561/android-mvvm-livedata-data-binding">LiveData and DataBinding</a> </li>
  <li>Data storage and realtime chat with <a href="https://firebase.google.com/docs/firestore/quickstart">Firebase Cloud Firestore</a> </li>
  <li>Authentication with <a href="https://firebase.google.com/docs/auth/android/firebaseui">Firebase Authentication</a> </li>
  <li>Image storage with <a href="https://firebase.google.com/docs/storage/android/start">Firebase Cloud Storage</a> </li>
</ol>

## Data structure
#### Firebase Authentication
  |------uid (userId) <br/>
  |------email <br/>
  |------(password) <br/>

#### Cloud Firestore
  |------Collection(users) <br/>
  |------------Document(uid) (uid in Auth) <br/>
  |------------------nickname <br/>
  |------------------email <br/>
  |------------------avatarUrl <br/>
  |------------------online <br/>
  |------------------offlineAt <br/>
  |------------------Collection(chats) <br/> 
  |---------------------------Document(chatId) <br/> 
  |---------------------------------newMsgNum <br/> 
  |---------------------------------thumbMsg <br/> 
  |---------------------------------Map(chatUser) <br/> 
  |---------------------------------------id <br/> 
  |---------------------------------------avatarUrl <br/> 
  |---------------------------------------nickname <br/> 
  
  |------Collection(chats) <br/>
  |------------Document(chatId) <br/>
  |------------------o // just a tmp field to create document with random id<br/> 
  |------------------Collection(messages) <br/>
  |---------------------------Document(messageId) <br/> 
  |---------------------------------type <br/> 
  |---------------------------------content <br/> 
  |---------------------------------createdAt <br/> 
  |---------------------------------senderUserId <br/> 
  
  #### Cloud storage
  |------chats <br/>
  |------------chatId <br/>
  |------------------images <br/>
  |------users <br/>
  |------------userId <br/>
  |------------------avatar.jpg <br/>

