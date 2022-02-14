# Qapital assignment

## Time estimate
I estimated the task to take about 6 hours but in reality I think I spent more about 12 hours. Not fully sure though, I was quite bussy with other things too and was a bit on and off the assignment but I was definetly quite of my estimate.

Main thing that took longer for me was that I spent quite a bit of time on the date range pager, previously I've only had to deal with incremental pagers. I also had to tweak my Dagger setup regarding unit tests to be able to mock the database that I didn't anticipate was gonna take a bit of time.

## Running the app
Running should be fairly straight forward for any Android developer. You just gotta make sure you got API 31 and Java 11 setup, pull down the project and hit the play button in Android Studio.

For running the unit tests just right click on `com.qapital (test)` and hit "Run 'Tests in 'com.qapital''

## Architecture
I know you mentioned in the assignment that you wanted to see an MVP architecture but to save some time I decided to go for my favorite architecture, that said it doesn't mean I won't respect an already existing and well defined architecture if I would start at Qapital. The architecture of my choice is a combination of MVVM and MVI where the UI has `ViewModel`s that observes the state of interactors and all state changes is delivered with partial states updating the interactors state object in a
unidirectional way without side effects.

The app consists of three modules:

### An `app` module where you find:

* the UI
* `ViewModel`s
* Interactors
* Repositories

### A `data` module where you find:

* Network and Database services
* Object mappers

### A `domain` module where you find:

* Domain model objects mapped from the network model objects.
* Network and Database service interfaces that the `data` module implements.

### Persistent storage
Regarding caching I decided to only cache the user object that I consider valid for 24h, otherwise refetch. The activity feed felt like something where the cache would become outdated very quickly so I decided to not cache that.

## Unit testing

There is unit tests that you can find in the `app` module. It's using `OkHttps` `MockWebServer` that reads a copy of the API responses from the `resources` folder and like that you can unit test
everything from `ViewModel`s all the way down to the network services and this can run on a CI server without any need of an emulator or device.

## Third party Dependencies

### RxKotlin

All the communication between the different layers are using RxKotlin.

### Retrofit

To talk to the REST API.

### Dagger

For dependency injection.

### [BindingCollectionAdapter](https://github.com/evant/binding-collection-adapter)

This library goes hand in hand with data binding where you pretty much never have to make your own adapters again.

### Apache Commons Lang

It contains a `HashCodeBuilder` that is very convenient to use when you wanna ensure unique ids for your adapters.
