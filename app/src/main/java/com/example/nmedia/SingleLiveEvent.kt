package com.example.nmedia

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveEvent<T>:MutableLiveData<T>() {
    private var pending = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        require(!hasActiveObservers()){
            error("Multiple observers registered but only will be notified of changes")
        }

        super.observe(owner){
            if (pending){
                pending = false
                observer.onChanged(it)
            }
        }
    }

    override fun setValue(value: T) {
       pending = true
        super.setValue(value)

    }
}