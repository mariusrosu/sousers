# Stack Overflow Top Users Android App

This is an Android application that fetches the top StackOverflow users and displays them in a list. The goal of this project is to showcase clean architecture in Android.

## Demo

https://github.com/user-attachments/assets/ec6c4a86-6aa9-4e3e-bb0e-ef4f636063e8

## Functional requirements
The application fetches the top 20 users from StackOverflow based on reputation and displays them into a scrollable list. 

The app user can see the profile picture, name and reputation points for each StackOverflow user. Also they can follow and unfollow users from that list.

## Architecture
The application follows Uncle Bob’s clean architecture by defining clear separation of concerns and a unidirectional flow of data using the `ui`, `business` and `data` modules.

<img width="1787" height="752" alt="Screenshot 2026-04-22 at 12 46 45" src="https://github.com/user-attachments/assets/411fb0bf-476f-4044-9849-eb08b2cc2937" />

### Data

`Data` is an Android module responsible for getting and storing the application data. It fetches the StackOverflow users from a remote API and also stores locally the follows users using `SharedPreferences`.

It implements the `UserRepository` interface from the `business` module to provide a concrete implementation details of fetching the users and combining them with the followed users local data.

### Business

The `business` module is a pure Kotlin module that defines the interfaces and domain models from the app. It should not have any Android dependencies. 

By looking at the `business` module someone can understand exactly all the applications functionalities. For example in `UserRepository` we can see that the app can get top users, follow and unfollow them.

### UI

UI is an Android module that depends on the business module and is responsible of managing the UI state and render the UI.

It uses the MVI pattern using Kotlin flows to collect the data from the repository in a lifecycle aware way and expose a state flow to the composable to be rendered. It also captures user's interactions with the app (e.g follow clicked) and propagates it to the repository.

UI module also contains all the UI components that are needed through the app to build the screens, like `UserCard`, `Toolbar` or `InlineButton`.

### App

The app module is a simple Android application module that wires everything together: `ui`, `business` and `data`.

## Technologies Used
**Hilt** for dependency injection and dependency inversion in UI and data.

**Kotlin Coroutines and Flow** for asynchronous programming and unidirectional data flow in a reactive manner.

**Jetpack Compose** for rendering UI in a declarative and lifecycle aware pure Kotlin way, tied with View Model to preserve the state and allow unit testing.

**Retrofit** as the networking library to fetch StackOverflow top users from a REST API.

**Shared Preferences** for local storage for the followed users. 
_(Here I wanted initially to use DataStore or maybe Room because it provides out of the box a reactive Kotlin flow but due to library limitations I chose Shared Preferences for simple key value storage)._

## Testing
Added unit tests for all the components like `UserRepository`, `FollowedUsersDataStore` or `UserListViewModel`.

The libraries used for unit tests are **MockK** (a more Kotlin friendly library like Mockito) and **Kotest** (a library that provides useful assertion tools).

Code also includes an integration test, `StackOverflowApiTest` that calls the real API and asserts the result. _(For the sake of this demo I kept this integration test in the unit tests folder, but in a real application it should be in a separate test suite because unlike unit tests those are slower and flacky)._
