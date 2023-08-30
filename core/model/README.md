# Model -> (:core:model)

The model module is a simple JVM  (Kotlin) module with simple POKOS (Plain Old Kotlin Object),
mostly data objects.

> **Warning**
> You can not use `Parcelable` or `@Parcelize` annotations here because it the module does not apply
> any of the android application of library plugins, hence, the extension `android` does not exist.

<br>

> **Important**
> If one needs to use a Parcelable in the app for a given feature, consider implementing another
> data class in the given feature with subsets of the all the fields.properties needed at any given
> time. You can then go ahead and apply `@Parcelize` annotation to the new class. This way, we can
> ensures that we are only accessing the data we need at any point in time.