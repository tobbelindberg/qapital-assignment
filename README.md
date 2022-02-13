# Qapital assignment

## Architecture

An MVVM/MVI architecture where the UI has `ViewModel`s that observes the state of interactors and all state changes is delivered with partial states updating the interactors state object in a
unidirectional way without side effects.

The app consists of three modules:

### An `app` module where you find:

* the UI
* `ViewModel`s
* Interactors
* Repositories

### A `data` module where you find:

* Network services
* Object mappers

### A `domain` module where you find:

* Domain model objects mapped from the network model objects.
* Network service interfaces that the `data` module implements.

## Unit testing

There is unit tests that you can find in the `app` module. It's using `OkHttps` `MockWebServer` that reads a copy of the GitHub API response from the `resources` folder and like that you can unit test
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
